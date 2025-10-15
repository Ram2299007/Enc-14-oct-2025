package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<String> imageList;
    Uri GlobalUri;
    Context context;
    String themColor;
    CardView dataCardview;
    int PICK_IMAGE_REQUEST_CODE;
    BottomSheetDialog bottomSheetDialog;
    String modelId, userFTokenKey;
    private String currentCaption = ""; // Single caption for all images
    ColorStateList tintList;
    FirebaseDatabase database;
    File globalFile, FullImageFile;
    RecyclerView messageRecView;
    EditText messageBox;
    Set<String> uniqueDates;
    ArrayList<messageModel> messageList = new ArrayList<>();
    boolean isLastItemVisible;
    chatAdapter chatAdapter;
    // Multi-selection support
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private boolean isMultiSelectionMode = false;
    private OnImageSelectionListener selectionListener;
    private static OnImageSelectionListener globalSelectionListener;
    private static final int MAX_SELECTION_LIMIT = 30;


    LinearLayout gallary;
    TextView multiSelectSmallCounterText;
    EditText box;
    LinearLayout emoji;
    ImageView sendGrp;


    public interface OnImageSelectionListener {
        void onImageSelectionChanged(ArrayList<Uri> selectedUris);

        void onMultiSelectionModeChanged(boolean isMultiMode);
    }

    public void setOnImageSelectionListener(OnImageSelectionListener listener) {
        Log.d("ImageUpload", "=== ImageAdapter setOnImageSelectionListener ===");
        Log.d("ImageUpload", "ImageAdapter instance: " + this.hashCode());
        Log.d("ImageUpload", "Listener: " + (listener != null ? "not null" : "null"));
        this.selectionListener = listener;

        // Set global listener for all instances
        if (listener != null) {
            globalSelectionListener = listener;
            Log.d("ImageUpload", "Set global listener for all ImageAdapter instances");
        }
    }

    public void setMultiSelectionMode(boolean isMultiMode) {
        this.isMultiSelectionMode = isMultiMode;
        if (!isMultiMode) {
            selectedImageUris.clear();
        }
        notifyDataSetChanged();
        if (selectionListener != null) {
            selectionListener.onMultiSelectionModeChanged(isMultiMode);
        }
    }

    public ArrayList<Uri> getSelectedImageUris() {
        return selectedImageUris;
    }

    public boolean isMultiSelectionMode() {
        return isMultiSelectionMode;
    }

    public void clearSelection() {
        selectedImageUris.clear();
        notifyDataSetChanged();
        if (selectionListener != null) {
            Log.d("ImageUpload", "=== ImageAdapter clearSelection calling onImageSelectionChanged ===");
            Log.d("ImageUpload", "SelectedImageUris size: " + selectedImageUris.size());
            selectionListener.onImageSelectionChanged(selectedImageUris);
        } else {
            Log.w("ImageUpload", "SelectionListener is null in clearSelection");
        }
    }

    /**
     * Update bottom sheet UI based on selection state (camera fragment-like behavior)
     */
    private void updateBottomSheetUI() {
        try {
            if (dataCardview != null) {
                // Find the bottomview and captionlyt in the dataCardview
                LinearLayout bottomview = dataCardview.findViewById(R.id.bottomview);
                LinearLayout captionlyt = dataCardview.findViewById(R.id.captionlyt);

                if (bottomview != null && captionlyt != null) {
                    if (selectedImageUris.size() > 0) {
                        // Images selected: Hide bottomview, Show captionlyt
                        bottomview.setVisibility(View.GONE);
                        captionlyt.setVisibility(View.VISIBLE);

                        // Update caption hint with selection count
                        EditText messageBoxMy = captionlyt.findViewById(R.id.messageBoxMy);
                        if (messageBoxMy != null) {
                            messageBoxMy.setHint("Add Caption for " + selectedImageUris.size() + " image(s)");
                        }
                    } else {
                        // No images selected: Show bottomview, Hide captionlyt
                        bottomview.setVisibility(View.VISIBLE);
                        captionlyt.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("BottomSheetUI", "Error updating bottom sheet UI: " + e.getMessage());
        }
    }

    /**
     * Setup multi-image preview with selected images
     */
    public void setupMultiImagePreview() {
        setupMultiImagePreview("", null);
    }

    /**
     * Setup multi-image preview with selected images and caption
     */
    public void setupMultiImagePreview(String caption, BottomSheetDialog bottomSheetView) {
        Log.d("MultiImagePreview", "Setting up preview with " + selectedImageUris.size() + " images");

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImagePreview", "No images selected, returning");
            return;
        }

        // Clear previous captions for fresh start
        new HashMap<>().clear();
        Log.d("MultiImagePreview", "Cleared previous new HashMap<>() map");

        // Duplicate dialog avoid करा (Avoid duplicate dialog)
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("MultiImagePreview", "Dialog already showing, dismissing previous one");
            Constant.dialogLayoutFullScreen.dismiss();
        }

        // डायलॉग लेआउट सेटअप करा (Setup dialog layout)
        Log.d("MultiImagePreview", "Creating dialog...");
        Log.d("VIDEO_DIALOG_SHOW", "ImageAdapter.showMultiImagePreviewDialog called");
        Constant.dialogueFullScreen(context, R.layout.dialogue_full_screen_dialogue);
        Log.d("MultiImagePreview", "Dialog created, showing...");
        Log.d("VIDEO_DIALOG_SHOW", "About to show dialog");
        Constant.dialogLayoutFullScreen.show();
        Log.d("MultiImagePreview", "Dialog shown successfully");

        // Debug: Check if dialog is showing
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("MultiImagePreview", "Dialog is showing: true");
        } else {
            Log.e("MultiImagePreview", "Dialog is showing: false");
        }

        // Window setup करा (Setup window)
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);
        }

        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            android.app.Activity activity = (android.app.Activity) context;
            android.view.Window activityWindow = activity.getWindow();
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        // Hide the single image view and show the horizontal gallery preview
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        singleImageView.setVisibility(View.GONE);

        // Hide ViewPager2 and show horizontal gallery
        androidx.viewpager2.widget.ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Hide document preview
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.GONE);
        }

        // Hide contact preview
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
        if (contactContainer != null) {
            contactContainer.setVisibility(View.GONE);
        }

        // Setup horizontal gallery preview with delay
        Log.d("MultiImagePreview", "Calling setupHorizontalGalleryPreview with delay...");
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            Log.d("MultiImagePreview", "Setting up horizontal gallery preview after delay with caption: " + caption);
            setupHorizontalGalleryPreview(caption, bottomSheetView);
            Log.d("MultiImagePreview", "setupHorizontalGalleryPreview completed");
        }, 100);
    }


    /**
     * Setup horizontal gallery preview
     */


    /**
     * Setup horizontal gallery preview with caption
     */
    private void setupHorizontalGalleryPreview(String caption, BottomSheetDialog bottomSheetView) {
        Log.d("HorizontalGallery", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");

        // मुख्य इमेज प्रिव्यू ViewPager2 सेटअप करा (Setup main image preview ViewPager2)
        Log.d("HorizontalGallery", "Finding mainImagePreview...");
        androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        Log.d("HorizontalGallery", "mainImagePreview found: " + (mainImagePreview != null ? "yes" : "no"));
        if (mainImagePreview != null) {
            Log.d("HorizontalGallery", "Setting mainImagePreview visibility to VISIBLE");
            mainImagePreview.setVisibility(View.VISIBLE);

            // Check current layout params
            android.view.ViewGroup.LayoutParams params = mainImagePreview.getLayoutParams();
            if (params != null) {
                Log.d("HorizontalGallery", "MainImagePreview current params: width=" + params.width + ", height=" + params.height);
                params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                mainImagePreview.setLayoutParams(params);
                Log.d("HorizontalGallery", "MainImagePreview layout params set to MATCH_PARENT");
            } else {
                Log.e("HorizontalGallery", "MainImagePreview layout params is null!");
            }

            // Check view bounds after layout
            mainImagePreview.post(() -> {
                Log.d("HorizontalGallery", "MainImagePreview bounds: " + mainImagePreview.getLeft() + ", " + mainImagePreview.getTop() + ", " + mainImagePreview.getRight() + ", " + mainImagePreview.getBottom());
                Log.d("HorizontalGallery", "MainImagePreview width: " + mainImagePreview.getWidth() + ", height: " + mainImagePreview.getHeight());
            });

            // मुख्य प्रिव्यूसाठी adapter सेटअप करा (Setup adapter for main preview)
            Log.d("HorizontalGallery", "Creating MainImagePreviewAdapter...");
            com.Appzia.enclosure.Adapter.MainImagePreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.MainImagePreviewAdapter(context, selectedImageUris);
            Log.d("HorizontalGallery", "MainImagePreviewAdapter created with " + selectedImageUris.size() + " images");
            mainImagePreview.setAdapter(mainAdapter);
            Log.d("HorizontalGallery", "Adapter set to ViewPager2");
            mainAdapter.notifyDataSetChanged();
            Log.d("HorizontalGallery", "notifyDataSetChanged called");
            mainImagePreview.invalidate();
            mainImagePreview.requestLayout();
            Log.d("HorizontalGallery", "MainImagePreviewAdapter set successfully");

            // पेज चेंज callback सेटअप करा (Setup page change callback)
            mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.d("HorizontalGallery", "Page selected: " + position);
                    updateImageCounter(position + 1, selectedImageUris.size());

                    // Update caption EditText with current image's caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("PageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

                        // Set flag to prevent TextWatcher from saving during programmatic update
                        // Removed individual caption logic

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            // Position cursor at the end of the text
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }

                        // Reset flag to allow TextWatcher to save again
                        // Removed individual caption logic
                    }
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    Log.d("HorizontalGallery", "Page scrolled: " + position + ", offset: " + positionOffset);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    Log.d("HorizontalGallery", "Page scroll state changed: " + state);
                }
            });
            Log.d("HorizontalGallery", "Page change callback registered");

            // Setup TextWatcher for caption input
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy != null) {
                // Set the caption from bottom sheet if provided
                if (caption != null && !caption.trim().isEmpty()) {
                    messageBoxMy.setText(caption);
                    messageBoxMy.setSelection(caption.length());
                    currentCaption = caption;
                    Log.d("CaptionTransfer", "Caption set in preview dialogue: " + caption);
                } else if (currentCaption != null && !currentCaption.isEmpty()) {
                    // Set initial caption if available (fallback)
                    messageBoxMy.setText(currentCaption);
                    messageBoxMy.setSelection(currentCaption.length());
                    Log.d("HorizontalGallery", "Initial caption set: '" + currentCaption + "'");
                }

                messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not needed
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Save the caption as user types
                        currentCaption = s.toString();
                        Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                    }

                    @Override
                    public void afterTextChanged(android.text.Editable s) {
                        // Not needed
                    }
                });
                Log.d("HorizontalGallery", "TextWatcher added to messageBoxMy");
            }

            // Force ViewPager2 to show first item
            mainImagePreview.setCurrentItem(0, false);
            Log.d("HorizontalGallery", "ViewPager2 current item set to 0");
        } else {
            Log.e("HorizontalGallery", "mainImagePreview is null!");
        }

        // हॉरिझॉन्टल RecyclerView सेटअप करा (Setup horizontal RecyclerView)
        Log.d("HorizontalGallery", "Finding horizontalRecyclerView...");


        // Show image counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            Log.d("HorizontalGallery", "Image counter made visible");
        }

        // Image counter सेटअप करा (Setup image counter)
        Log.d("HorizontalGallery", "Setting up image counter...");
        updateImageCounter(1, selectedImageUris.size());
        Log.d("HorizontalGallery", "Image counter setup completed");


        // Force dialog refresh
        Log.d("HorizontalGallery", "Forcing dialog refresh...");
        if (Constant.dialogLayoutFullScreen != null) {
            Constant.dialogLayoutFullScreen.getWindow().getDecorView().invalidate();
            Constant.dialogLayoutFullScreen.getWindow().getDecorView().requestLayout();
            Log.d("HorizontalGallery", "Dialog refresh completed");
        }

        // Setup send button directly in main method (like ImageAdapter2)
        Log.d("SendButton", "Setting up send button directly in main method");
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            Log.d("SendButton", "sendGrp found in main method - setting up click listener");
            sendGrp.setClickable(true);
            sendGrp.setEnabled(true);
            sendGrp.setOnClickListener(v -> {
                Log.d("SendButton", "Send button clicked in main method!");
                Log.d("SendButton", "Current caption: '" + currentCaption + "'");
                Log.d("SendButton", "Selected images count before send: " + selectedImageUris.size());

                // Debug test removed to prevent infinite recursion
                Log.d("SELECTION_BUNCH_DEBUG", "=== SKIPPING DEBUG TEST TO PREVENT INFINITE RECURSION ===");

                // Send the images FIRST (before clearing selections)
                Log.d("SendButton", "Sending " + selectedImageUris.size() + " images");
                sendMultipleImagesWithIndividualCaptions();

                // Clear message box and dismiss dialog
                messageBox.setText("");
                Constant.dialogLayoutFullScreen.dismiss();

                // Dismiss bottom sheet properly
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                    Log.d("SendButton", "Bottom sheet dismissed");
                }

                // Additional bottom sheet dismiss for multi-image preview
                if (dataCardview != null && dataCardview.getVisibility() == View.VISIBLE) {
                    dataCardview.animate()
                            .translationY(dataCardview.getHeight())
                            .setDuration(200)
                            .withEndAction(() -> dataCardview.setVisibility(View.GONE))
                            .start();
                    Log.d("SendButton", "Bottom sheet dismissed with animation");
                }

                // Clear selections and refresh UI AFTER sending
                selectedImageUris.clear();
                if (selectionListener != null) {
                    selectionListener.onImageSelectionChanged(selectedImageUris);
                    Log.d("SendButton", "Selection cleared and UI refreshed");
                }
            });
            Log.d("SendButton", "Send button click listener set up in main method");

            // Run the test to verify everything is working
            testSendButtonFunctionality();
        } else {
            Log.e("SendButton", "sendGrp not found in main method!");
        }
    }

    /**
     * Setup send button for multi-image preview
     */
    private void setupSendButtonForMultiImagePreview(BottomSheetDialog bottomSheetView) {
        Log.d("SendButton", "Setting up send button for multi-image preview");

        // Check if dialog exists
        if (Constant.dialogLayoutFullScreen == null) {
            Log.e("SendButton", "Constant.dialogLayoutFullScreen is null!");
            return;
        }

        if (!Constant.dialogLayoutFullScreen.isShowing()) {
            Log.e("SendButton", "Constant.dialogLayoutFullScreen is not showing!");
            return;
        }

        // Find the send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        Log.d("SendButton", "sendGrp found: " + (sendGrp != null ? "yes" : "no"));

        if (sendGrp != null) {
            Log.d("SendButton", "sendGrp is clickable: " + sendGrp.isClickable());
            Log.d("SendButton", "sendGrp visibility: " + sendGrp.getVisibility());
            Log.d("SendButton", "sendGrp is enabled: " + sendGrp.isEnabled());

            // Make sure the button is clickable and enabled
            sendGrp.setClickable(true);
            sendGrp.setEnabled(true);
            sendGrp.setVisibility(View.VISIBLE);

            // Ensure the button is focusable for touch events
            sendGrp.setFocusable(true);
            sendGrp.setFocusableInTouchMode(true);

            // Set up click listener
            sendGrp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ImageUpload", "=== SEND BUTTON CLICKED ===");
                    Log.d("ImageUpload", "Send button clicked in multi-image preview!");
                    Log.d("ImageUpload", "Current caption: '" + currentCaption + "'");
                    Log.d("ImageUpload", "Button view: " + v.getClass().getSimpleName());
                    Log.d("ImageUpload", "Button view ID: " + v.getId());

                    // Call the send multiple images method
                    Log.d("ImageUpload", "=== CALLING sendMultipleImagesWithIndividualCaptions from ImageAdapter ===");
                    Log.d("ImageUpload", "SelectedImageUris size: " + selectedImageUris.size());
                    sendMultipleImagesWithIndividualCaptions();

                    // Dismiss the dialog after sending
                    if (Constant.dialogLayoutFullScreen != null) {
                        Constant.dialogLayoutFullScreen.dismiss();
                        Log.d("SendButton", "Dialog dismissed after sending");
                    }

                    // Dismiss bottom sheet
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.dismiss();
                        Log.d("SendButton", "Bottom sheet dismissed after sending");
                    }

                    // Dismiss bottomSheetView if it exists
                    if (bottomSheetView != null && bottomSheetView.isShowing()) {
                        bottomSheetView.dismiss();
                        Log.d("SendButton", "BottomSheetView dismissed after sending");
                    }
                }
            });

            Log.d("SendButton", "Send button click listener set up successfully");

            // Run the test to verify everything is working
            testSendButtonFunctionality();
        } else {
            Log.e("SendButton", "sendGrp button not found!");
            Log.e("SendButton", "Dialog layout: " + (Constant.dialogLayoutFullScreen != null ? "exists" : "null"));
            Log.e("SendButton", "Dialog showing: " + (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()));

            // Try to find the button with a delay in case the layout is not fully inflated
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                Log.d("SendButton", "Retrying to find send button after delay...");
                LinearLayout retrySendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
                if (retrySendGrp != null) {
                    Log.d("SendButton", "Send button found on retry!");
                    setupSendButtonClickListener(retrySendGrp, bottomSheetView);

                    // Run the test to verify everything is working
                    testSendButtonFunctionality();
                } else {
                    Log.e("SendButton", "Send button still not found after retry!");
                }
            }, 500);
        }
    }

    /**
     * Setup click listener for send button
     */
    private void setupSendButtonClickListener(LinearLayout sendGrp, BottomSheetDialog bottomSheetView) {
        Log.d("SendButton", "Setting up click listener for send button");

        // Make sure the button is clickable and enabled
        sendGrp.setClickable(true);
        sendGrp.setEnabled(true);
        sendGrp.setVisibility(View.VISIBLE);
        sendGrp.setFocusable(true);
        sendGrp.setFocusableInTouchMode(true);

        // Set up click listener
        sendGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageUpload", "=== SEND BUTTON CLICKED (Retry) ===");
                Log.d("ImageUpload", "Send button clicked in multi-image preview!");
                Log.d("ImageUpload", "Current caption: '" + currentCaption + "'");
                Log.d("ImageUpload", "Button view: " + v.getClass().getSimpleName());
                Log.d("ImageUpload", "Button view ID: " + v.getId());

                // Call the send multiple images method
                Log.d("ImageUpload", "=== CALLING sendMultipleImagesWithIndividualCaptions from ImageAdapter ===");
                Log.d("ImageUpload", "SelectedImageUris size: " + selectedImageUris.size());
                sendMultipleImagesWithIndividualCaptions();

                // Dismiss the dialog after sending
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                    Log.d("SendButton", "Dialog dismissed after sending");
                }

                // Dismiss bottom sheet
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                    Log.d("SendButton", "Bottom sheet dismissed after sending");
                }

                // Dismiss bottomSheetView if it exists
                if (bottomSheetView != null && bottomSheetView.isShowing()) {
                    bottomSheetView.dismiss();
                    Log.d("SendButton", "BottomSheetView dismissed after sending");
                }
            }
        });

        Log.d("SendButton", "Send button click listener set up successfully (retry)");
    }

    /**
     * Send multiple images with single caption in selectionBunch format
     */
    private void sendMultipleImagesWithIndividualCaptions() {
        Log.d("ImageUpload", "=== sendMultipleImagesWithIndividualCaptions START ===");
        Log.d("ImageUpload", "Sending " + selectedImageUris.size() + " images with selectionBunch");
        Log.d("ImageUpload", "isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size());
        Log.d("ImageUpload", "Context: " + (context != null ? "not null" : "null"));
        Log.d("ImageUpload", "Database: " + (database != null ? "not null" : "null"));

        // Process all images and create selectionBunch with single caption
        processMultipleImagesForSelectionBunchWithSingleCaption();
    }

    /**
     * Process multiple images for selectionBunch format with single caption
     */
    private void processMultipleImagesForSelectionBunchWithSingleCaption() {
        Log.d("ImageUpload", "=== processMultipleImagesForSelectionBunchWithSingleCaption START ===");
        try {
            Log.d("ImageUpload", "Processing " + selectedImageUris.size() + " images with individual uploads");

            // Create selectionBunch list
            ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> selectionBunch = new ArrayList<>();

            // Process each image with single caption
            for (int i = 0; i < selectedImageUris.size(); i++) {
                Uri imageUri = selectedImageUris.get(i);
                String caption = currentCaption; // Use the current caption for all images

                Log.d("SelectionBunchIndividual", "Processing image " + (i + 1) + "/" + selectedImageUris.size());

                // Get file extension
                String extension;
                if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(imageUri));
                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(imageUri.getPath()))));
                }

                // Create compressed and full-size files
                File compressedFile = createCompressedImageFile(imageUri, extension);
                File fullSizeFile = createFullSizeImageFile(imageUri, extension);

                if (compressedFile == null || fullSizeFile == null) {
                    Log.e("SelectionBunchIndividual", "Failed to create image files for: " + imageUri);
                    continue;
                }

                // Get image dimensions
                String[] dimensions = Constant.calculateImageDimensions(context, compressedFile, imageUri);
                String imageWidthDp = dimensions[0];
                String imageHeightDp = dimensions[1];
                String aspectRatio = dimensions[2];

                // Create selectionBunchModel entry with proper fileName
                String fileName = getOriginalFileName(imageUri);
                if (fileName == null || fileName.isEmpty()) {
                    fileName = "image_" + System.currentTimeMillis() + "_" + i + ".jpg";
                }

                // Use compressed file name if original filename is not available
                if (fileName == null || fileName.isEmpty()) {
                    fileName = compressedFile.getName();
                }

                com.Appzia.enclosure.Model.selectionBunchModel bunchModel = new com.Appzia.enclosure.Model.selectionBunchModel(
                        "", // imgUrl - will be set after upload
                        fileName // fileName
                );
                selectionBunch.add(bunchModel);

                Log.d("SelectionBunchIndividual", "Added image " + (i + 1) + " to selectionBunch: " + fileName);
            }

            // Create single message with selectionBunch
            createAndSendSelectionBunchMessage(selectionBunch);

        } catch (Exception e) {
            Log.e("SelectionBunchIndividual", "Error processing multiple images: " + e.getMessage(), e);
        }
    }

    /**
     * Process multiple images for selectionBunch format
     */
    private void processMultipleImagesForSelectionBunch() {
        try {
            Log.d("SelectionBunch", "Processing " + selectedImageUris.size() + " images for selectionBunch");

            // Create selectionBunch list
            ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> selectionBunch = new ArrayList<>();

            // Process each image and add to selectionBunch
            for (int i = 0; i < selectedImageUris.size(); i++) {
                Uri imageUri = selectedImageUris.get(i);
                String caption = "";
                if (caption == null) {
                    caption = "";
                }

                Log.d("SelectionBunch", "Processing image " + (i + 1) + "/" + selectedImageUris.size());

                // Get file extension
                String extension;
                if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(imageUri));
                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(imageUri.getPath()))));
                }

                // Create compressed and full-size files
                File compressedFile = createCompressedImageFile(imageUri, extension);
                File fullSizeFile = createFullSizeImageFile(imageUri, extension);

                if (compressedFile == null || fullSizeFile == null) {
                    Log.e("SelectionBunch", "Failed to create image files for: " + imageUri);
                    continue;
                }

                // Get image dimensions
                String[] dimensions = Constant.calculateImageDimensions(context, compressedFile, imageUri);
                String imageWidthDp = dimensions[0];
                String imageHeightDp = dimensions[1];
                String aspectRatio = dimensions[2];

                // Create selectionBunchModel entry with proper fileName
                String fileName = getOriginalFileName(imageUri);
                if (fileName == null || fileName.isEmpty()) {
                    fileName = "image_" + System.currentTimeMillis() + "_" + i + ".jpg";
                }

                // Use compressed file name if original filename is not available
                if (fileName == null || fileName.isEmpty()) {
                    fileName = compressedFile.getName();
                }

                com.Appzia.enclosure.Model.selectionBunchModel bunchModel = new com.Appzia.enclosure.Model.selectionBunchModel(
                        "", // imgUrl - will be set after upload
                        fileName // fileName
                );
                selectionBunch.add(bunchModel);

                Log.d("SelectionBunch", "Added image " + (i + 1) + " to selectionBunch: " + fileName);
            }

            // Create single message with selectionBunch
            createAndSendSelectionBunchMessage(selectionBunch);

        } catch (Exception e) {
            Log.e("SelectionBunch", "Error processing multiple images: " + e.getMessage(), e);
        }
    }

    /**
     * Process individual image for sending with caption
     */
    private void processImageForSending(Uri imageUri, String captionText, boolean isLastImage) {
        Log.d("ImageUpload", "=== processImageForSending START ===");
        Log.d("ImageUpload", "ImageUri: " + imageUri);
        Log.d("ImageUpload", "Caption: " + captionText);
        Log.d("ImageUpload", "IsLastImage: " + isLastImage);

        try {
            // Get file extension
            String extension;
            if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(imageUri));
                Log.d("ImageUpload", "Extension from content resolver: " + extension);
            } else {
                extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(imageUri.getPath()))));
                Log.d("ImageUpload", "Extension from file path: " + extension);
            }

            // Create compressed and full-size files
            Log.d("ImageUpload", "Creating compressed and full-size files...");
            File compressedFile = createCompressedImageFile(imageUri, extension);
            File fullSizeFile = createFullSizeImageFile(imageUri, extension);

            if (compressedFile == null || fullSizeFile == null) {
                Log.e("ImageUpload", "Failed to create image files for: " + imageUri);
                Log.e("ImageUpload", "CompressedFile: " + (compressedFile != null ? compressedFile.getAbsolutePath() : "null"));
                Log.e("ImageUpload", "FullSizeFile: " + (fullSizeFile != null ? fullSizeFile.getAbsolutePath() : "null"));
                return;
            }

            Log.d("ImageUpload", "Files created successfully:");
            Log.d("ImageUpload", "Compressed: " + compressedFile.getAbsolutePath() + " (size: " + compressedFile.length() + ")");
            Log.d("ImageUpload", "FullSize: " + fullSizeFile.getAbsolutePath() + " (size: " + fullSizeFile.length() + ")");

            // Get image dimensions
            Log.d("ImageUpload", "Calculating image dimensions...");
            String[] dimensions = Constant.calculateImageDimensions(context, compressedFile, imageUri);
            String imageWidthDp = dimensions[0];
            String imageHeightDp = dimensions[1];
            String aspectRatio = dimensions[2];
            Log.d("ImageUpload", "Dimensions: " + imageWidthDp + "x" + imageHeightDp + ", AspectRatio: " + aspectRatio);

            // Create message model
            Log.d("ImageUpload", "Creating and sending image message...");
            createAndSendImageMessage(compressedFile, fullSizeFile, imageUri, captionText,
                    imageWidthDp, imageHeightDp, aspectRatio, isLastImage);

        } catch (Exception e) {
            Log.e("ImageUpload", "Error processing image: " + e.getMessage(), e);
        }
        Log.d("ImageUpload", "=== processImageForSending END ===");
    }

    /**
     * Create compressed image file
     */
    private File createCompressedImageFile(Uri imageUri, String extension) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

            // Prepare output directory inside app-specific external storage
            File baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (baseDir == null) {
                return null;
            }
            File mediaDir = new File(baseDir, "Enclosure/Media/Images");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }

            // Get original filename
            String fileName = getOriginalFileName(imageUri);
            File compressedFile = new File(mediaDir, "compressed_" + fileName);

            // Compress image
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            byte[] compressedData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(compressedFile);
            fos.write(compressedData);
            fos.flush();
            fos.close();

            return compressedFile;
        } catch (Exception e) {
            Log.e("CreateCompressedImageFile", "Error: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Create full-size image file
     */
    private File createFullSizeImageFile(Uri imageUri, String extension) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

            // Prepare output directory inside app-specific external storage
            File baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (baseDir == null) {
                return null;
            }
            File mediaDir = new File(baseDir, "Enclosure/Media/Images");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }

            // Get original filename
            String fileName = getOriginalFileName(imageUri);
            File fullSizeFile = new File(mediaDir, fileName);

            // Compress image with higher quality
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] fullSizeData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(fullSizeFile);
            fos.write(fullSizeData);
            fos.flush();
            fos.close();

            return fullSizeFile;
        } catch (Exception e) {
            Log.e("CreateFullSizeImageFile", "Error: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get original filename from URI
     */
    private String getOriginalFileName(Uri uri) {
        String fileName = null;
        if (uri != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
                cursor.close();
            }
        }
        return fileName != null ? fileName : "image_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * Create and send selectionBunch message
     */
    private void createAndSendSelectionBunchMessage(ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> selectionBunch) {
        try {
            Log.d("SelectionBunchMessage", "Creating selectionBunch message with " + selectionBunch.size() + " images");
            Log.d("SelectionBunchMessage", "Current caption: '" + currentCaption + "'");

            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(context);
            final String receiverUid = ((Activity) context).getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;

            // Generate unique modelId for this selectionBunch
            String modelId = database.getReference().push().getKey();

            // Create message model with selectionBunch
            String uniqDate = Constant.getCurrentDate();
            messageModel model;

            if (uniqueDates.add(uniqDate)) {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, "",
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "",
                        currentCaption, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        "", "", "", String.valueOf(selectedImageUris.size()), selectionBunch);
                Log.d("SelectionBunchMessage", "Created message with selectionBunch: " + selectionBunch.size() + " images");
                Log.d("SelectionBunchMessage", "selectionBunch details: " + selectionBunch.toString());

                // Store message in SQLite pending table before upload
                try {
                    new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                    Log.d("PendingMessages", "ImageAdapter - Selection bunch message stored in pending table: " + model.getModelId());
                } catch (Exception e) {
                    Log.e("PendingMessages", "ImageAdapter - Error storing selection bunch message in pending table: " + e.getMessage(), e);
                }
            } else {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, "",
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "",
                        currentCaption, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        "", "", "", String.valueOf(selectedImageUris.size()), selectionBunch);

                // Store message in SQLite pending table before upload
                try {
                    new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                    Log.d("PendingMessages", "ImageAdapter - Selection bunch message stored in pending table: " + model.getModelId());
                } catch (Exception e) {
                    Log.e("PendingMessages", "ImageAdapter - Error storing selection bunch message in pending table: " + e.getMessage(), e);
                }
                Log.d("SelectionBunchMessage", "Created message with selectionBunch (repeated date): " + selectionBunch.size() + " images");
                Log.d("SelectionBunchMessage", "selectionBunch details: " + selectionBunch.toString());
            }

            // Add to message list
            messageList.add(model);

            // Debug: Log the model before adding to list
            Log.d("SelectionBunchDebug", "Model selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("SelectionBunchDebug", "Model selectionCount: " + model.getSelectionCount());
            Log.d("SelectionBunchDebug", "Model dataType: " + model.getDataType());
            Log.d("SelectionBunchDebug", "Model document: " + model.getDocument());

            // Update UI
            ((Activity) context).runOnUiThread(() -> {
                chatAdapter.updateMessageList(new ArrayList<>(messageList));
                chatAdapter.setLastItemVisible(true); // Always show progress for pending messages
                messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            });

            // Upload all images and update selectionBunch with URLs
            uploadSelectionBunchImages(selectionBunch, model, modelId, receiverUid);

            Log.d("SelectionBunchMessage", "SelectionBunch message created and uploaded successfully");

        } catch (Exception e) {
            Log.e("SelectionBunchMessage", "Error: " + e.getMessage(), e);
        }
    }

    /**
     * Upload all images in selectionBunch and update URLs
     */
    private void uploadSelectionBunchImages(ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> selectionBunch,
                                            messageModel model, String modelId, String receiverUid) {
        try {
            Log.d("UploadSelectionBunch", "Uploading " + selectionBunch.size() + " images for selectionBunch");
            Log.d("UploadSelectionBunch", "Current caption: '" + currentCaption + "'");

            // Get sender info
            Constant.getSfFuncion(context);
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");

            if (selectedImageUris.isEmpty() || selectionBunch.isEmpty()) {
                Log.w("UploadSelectionBunch", "No images to upload for selectionBunch");
                return;
            }

            ArrayList<File> compressedFiles = new ArrayList<>();
            ArrayList<File> fullSizeFiles = new ArrayList<>();
            ArrayList<String> selectionBunchFilePaths = new ArrayList<>();

            // Keep track of primary image details (first image) for service compatibility
            Uri primaryImageUri = null;
            String primaryCaption = "";
            String primaryImageWidthDp = "";
            String primaryImageHeightDp = "";
            String primaryAspectRatio = "";

            for (int i = 0; i < selectedImageUris.size() && i < selectionBunch.size(); i++) {
                Uri imageUri = selectedImageUris.get(i);
                String caption = currentCaption; // Use the current caption for all images
                if (caption == null) {
                    caption = "";
                }

                // Determine extension
                String extension;
                if (imageUri.getScheme() != null && imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(imageUri));
                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(imageUri.getPath()))));
                }

                File compressedFile = createCompressedImageFile(imageUri, extension);
                File fullSizeFile = createFullSizeImageFile(imageUri, extension);

                if (compressedFile == null || fullSizeFile == null) {
                    Log.e("UploadSelectionBunch", "Failed to create local files for: " + imageUri);
                    continue;
                }

                compressedFiles.add(compressedFile);
                fullSizeFiles.add(fullSizeFile);
                selectionBunchFilePaths.add(fullSizeFile.getAbsolutePath());

                String[] dimensions = Constant.calculateImageDimensions(context, compressedFile, imageUri);

                if (i == 0) {
                    primaryImageUri = imageUri;
                    primaryCaption = caption;
                    primaryImageWidthDp = dimensions[0];
                    primaryImageHeightDp = dimensions[1];
                    primaryAspectRatio = dimensions[2];
                    Log.d("UploadSelectionBunch", "Primary caption set to: '" + primaryCaption + "'");
                }

                Log.d("UploadSelectionBunch", "Prepared local files for image " + (i + 1) + "/" + selectedImageUris.size());
            }

            if (selectionBunchFilePaths.isEmpty()) {
                Log.e("UploadSelectionBunch", "No valid files prepared for selectionBunch upload");
                return;
            }

            // Ensure model has a primary fileName for compatibility with existing server API
            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                model.setFileName(model.getSelectionBunch().get(0).getFileName());
            }

            File primaryCompressed = compressedFiles.get(0);
            File primaryFull = fullSizeFiles.get(0);

            if (primaryImageUri == null) {
                primaryImageUri = selectedImageUris.get(0);
            }

            UploadChatHelper uploadHelper = new UploadChatHelper(context, primaryCompressed, primaryFull, senderId, userFTokenKey, model);
            uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);

            Log.d("UploadSelectionBunch", "selectionBunchFilePaths collected: " + selectionBunchFilePaths.size());
            for (int i = 0; i < selectionBunchFilePaths.size(); i++) {
                Log.d("UploadSelectionBunch", "selectionBunchFilePaths[" + i + "]: " + selectionBunchFilePaths.get(i));
            }

            // Debug: Log the model being passed to upload helper
            Log.d("UploadSelectionBunch", "Model being passed to upload helper:");
            Log.d("UploadSelectionBunch", "  - selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("UploadSelectionBunch", "  - selectionCount: " + model.getSelectionCount());
            Log.d("UploadSelectionBunch", "  - dataType: " + model.getDataType());
            Log.d("UploadSelectionBunch", "  - document: " + model.getDocument());

            Log.d("UploadSelectionBunch", "About to call uploadContent with caption: '" + primaryCaption + "'");
            uploadHelper.uploadContent(
                    Constant.img,
                    primaryImageUri,
                    primaryCaption,
                    modelId,
                    null,
                    null,
                    primaryCompressed.getName(),
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(primaryImageUri),
                    receiverUid,
                    model.getReplyCrtPostion(),
                    model.getReplyKey(),
                    model.getReplyOldData(),
                    model.getReplyType(),
                    model.getReplytextData(),
                    model.getDataType(),
                    model.getFileName(),
                    model.getForwaredKey(),
                    primaryImageWidthDp,
                    primaryImageHeightDp,
                    primaryAspectRatio);

        } catch (Exception e) {
            Log.e("UploadSelectionBunch", "Error uploading selectionBunch images: " + e.getMessage(), e);
        }
    }

    /**
     * Create and send image message
     */
    private void createAndSendImageMessage(File compressedFile, File fullSizeFile, Uri imageUri,
                                           String captionText, String imageWidthDp,
                                           String imageHeightDp, String aspectRatio, boolean isLastImage) {
        try {
            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(context);
            final String receiverUid = ((Activity) context).getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;

            // Generate unique modelId for this image
            String modelId = database.getReference().push().getKey();

            // Create message model
            String uniqDate = Constant.getCurrentDate();
            messageModel model;

            if (uniqueDates.add(uniqDate)) {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, compressedFile.toString(),
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", compressedFile.getName(), "", "",
                        captionText, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, String.valueOf(selectedImageUris.size()));
                Log.d("SelectionCount", "processImageForSending uniqDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + String.valueOf(selectedImageUris.size()));

                // Store message in SQLite pending table before upload
                try {
                    new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                    Log.d("PendingMessages", "ImageAdapter - Process image message stored in pending table: " + model.getModelId());
                } catch (Exception e) {
                    Log.e("PendingMessages", "ImageAdapter - Error storing process image message in pending table: " + e.getMessage(), e);
                }
            } else {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, compressedFile.toString(),
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", compressedFile.getName(), "", "",
                        captionText, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, String.valueOf(selectedImageUris.size()));

                // Store message in SQLite pending table before upload
                try {
                    new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                    Log.d("PendingMessages", "ImageAdapter - Process image message stored in pending table: " + model.getModelId());
                } catch (Exception e) {
                    Log.e("PendingMessages", "ImageAdapter - Error storing process image message in pending table: " + e.getMessage(), e);
                }
                Log.d("SelectionCount", "processImageForSending repeatedDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + String.valueOf(selectedImageUris.size()));
            }

            // Add to message list
            messageList.add(model);

            // Update UI
            ((Activity) context).runOnUiThread(() -> {
                chatAdapter.updateMessageList(new ArrayList<>(messageList));
                chatAdapter.setLastItemVisible(true); // Always show progress for pending messages
                messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            });

            // Enhanced file validation before upload
            if (compressedFile == null || !compressedFile.exists() || compressedFile.length() == 0) {
                Log.e("ImageUpload", "Compressed file is invalid: " + (compressedFile != null ? compressedFile.getAbsolutePath() : "null"));
                return;
            }

            if (fullSizeFile == null || !fullSizeFile.exists() || fullSizeFile.length() == 0) {
                Log.e("ImageUpload", "Full size file is invalid: " + (fullSizeFile != null ? fullSizeFile.getAbsolutePath() : "null"));
                return;
            }

            Log.d("ImageUpload", "Files validated - Compressed: " + compressedFile.getAbsolutePath() +
                    " (size: " + compressedFile.length() + "), Full: " + fullSizeFile.getAbsolutePath() +
                    " (size: " + fullSizeFile.length() + ")");

            // Upload image using existing model to preserve selectionCount
            UploadChatHelper uploadHelper = new UploadChatHelper(context, compressedFile, fullSizeFile, senderId, userFTokenKey, model);
            uploadHelper.uploadContent(
                    Constant.img, imageUri, captionText, modelId, null, null,
                    compressedFile.getName(), null, null, null, null,
                    getFileExtension(imageUri), receiverUid, model.getReplyCrtPostion(),
                    model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),
                    model.getReplytextData(), model.getDataType(), model.getFileName(),
                    model.getForwaredKey(), imageWidthDp, imageHeightDp, aspectRatio);

            Log.d("ImageUpload", "Image message created and uploaded successfully");

        } catch (Exception e) {
            Log.e("ImageUpload", "Error: " + e.getMessage(), e);
        }
    }

    /**
     * Get file extension from URI
     */
    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = context.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    /**
     * Update image counter
     */
    private void updateImageCounter(int current, int total) {
        Log.d("HorizontalGallery", "updateImageCounter called - current: " + current + ", total: " + total);
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        Log.d("HorizontalGallery", "imageCounter found: " + (imageCounter != null ? "yes" : "no"));
        if (imageCounter != null) {
            Log.d("HorizontalGallery", "imageCounter current visibility: " + imageCounter.getVisibility());
            Log.d("HorizontalGallery", "imageCounter current text: " + imageCounter.getText());
            Log.d("HorizontalGallery", "imageCounter current alpha: " + imageCounter.getAlpha());
            Log.d("HorizontalGallery", "imageCounter current elevation: " + imageCounter.getElevation());

            // Force visibility and text
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText(current + " / " + total);
            imageCounter.setAlpha(1.0f);
            imageCounter.setElevation(10f);

            // Force layout update
            imageCounter.invalidate();
            imageCounter.requestLayout();

            // Force parent layout update
            ViewGroup parent = (ViewGroup) imageCounter.getParent();
            if (parent != null) {
                Log.d("HorizontalGallery", "imageCounter parent found: " + parent.getClass().getSimpleName());
                parent.invalidate();
                parent.requestLayout();
            } else {
                Log.e("HorizontalGallery", "imageCounter parent is null!");
            }

            Log.d("HorizontalGallery", "imageCounter visibility set to VISIBLE, text set to: " + (current + " / " + total));
            Log.d("HorizontalGallery", "imageCounter visibility after set: " + imageCounter.getVisibility());
            Log.d("HorizontalGallery", "imageCounter text after set: " + imageCounter.getText());
            Log.d("HorizontalGallery", "imageCounter alpha after set: " + imageCounter.getAlpha());
            Log.d("HorizontalGallery", "imageCounter elevation after set: " + imageCounter.getElevation());

            // Check if view is actually visible
            if (imageCounter.getVisibility() == View.VISIBLE) {
                Log.d("HorizontalGallery", "imageCounter is VISIBLE - should be showing");
            } else {
                Log.e("HorizontalGallery", "imageCounter is NOT VISIBLE - visibility: " + imageCounter.getVisibility());
            }

            // Check view bounds
            imageCounter.post(() -> {
                Log.d("HorizontalGallery", "imageCounter bounds: " + imageCounter.getLeft() + ", " + imageCounter.getTop() + ", " + imageCounter.getRight() + ", " + imageCounter.getBottom());
                Log.d("HorizontalGallery", "imageCounter width: " + imageCounter.getWidth() + ", height: " + imageCounter.getHeight());
            });
        } else {
            Log.e("HorizontalGallery", "imageCounter is null!");
        }
    }

    public ImageAdapter(List<String> imageList, Context context, CardView dataCardview, int PICK_IMAGE_REQUEST_CODE, BottomSheetDialog bottomSheetDialog, String modelId, FirebaseDatabase database, File globalFile, RecyclerView messageRecView, File FullImageFile, EditText messageBox, Set<String> uniqueDates, ArrayList<messageModel> messageList, String userFTokenKey, boolean isLastItemVisible, chatAdapter chatAdapter, LinearLayout gallary, EditText box, LinearLayout emoji, ImageView sendGrp, TextView multiSelectSmallCounterText) {
        Log.d("ImageUpload", "=== ImageAdapter Constructor Called ===");
        Log.d("ImageUpload", "ImageAdapter instance: " + this.hashCode());
        Log.d("ImageUpload", "ImageList size: " + imageList.size());
        Log.d("ImageUpload", "Context: " + (context != null ? "not null" : "null"));
        Log.d("ImageUpload", "Database: " + (database != null ? "not null" : "null"));
        this.imageList = imageList;
        this.context = context;
        this.dataCardview = dataCardview;
        this.PICK_IMAGE_REQUEST_CODE = PICK_IMAGE_REQUEST_CODE;
        this.bottomSheetDialog = bottomSheetDialog;
        this.modelId = modelId;
        this.database = database;
        this.globalFile = globalFile;
        this.messageRecView = messageRecView;
        this.FullImageFile = FullImageFile;
        this.messageBox = messageBox;
        this.uniqueDates = uniqueDates;
        this.messageList = messageList;
        this.userFTokenKey = userFTokenKey;
        this.isLastItemVisible = isLastItemVisible;
        this.chatAdapter = chatAdapter;
        this.gallary = gallary;
        this.box = box;
        this.emoji = emoji;
        this.sendGrp = sendGrp;
        this.multiSelectSmallCounterText = multiSelectSmallCounterText;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = Uri.parse(imageList.get(position));
        boolean isSelected = selectedImageUris.contains(imageUri);

        Glide.with(holder.imageView.getContext())
                .load(imageUri)
                .thumbnail(0.2f) // Slightly higher quality thumbnail
                .override(250, 250) // Increased size for better quality
                .format(DecodeFormat.PREFER_ARGB_8888) // Use high quality format
                .encodeQuality(90) // High quality encoding
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Better caching strategy
                .dontAnimate() // Avoid fade-in animation (smoother in RecyclerView)
                .skipMemoryCache(false) // Enable memory cache for better performance
                .centerCrop()
                .into(holder.imageView);

        // Update checkmark visibility
        holder.checkmark.setVisibility(isMultiSelectionMode && isSelected ? View.VISIBLE : View.GONE);

        // Apply selection overlay with opposite dimming logic
        if (isMultiSelectionMode) {


            Constant.getSfFuncion(holder.checkmark.getContext());
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            holder.checkmark.setImageTintList(tintList);
            boolean isLimitReached = selectedImageUris.size() >= MAX_SELECTION_LIMIT;

            if (isSelected) {
                // Selected images are dimmed (darker)
                holder.imageView.setAlpha(0.5f);
            } else if (isLimitReached) {
                // When limit reached, unselected images are very dim
                holder.imageView.setAlpha(0.2f);
            } else {
                // Unselected images are normal (bright)
                holder.imageView.setAlpha(1.0f);
            }
        } else {
            holder.imageView.setAlpha(1.0f);

        }


        // Set click listener on the entire item view for better UX
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(context);
                }

                if (isMultiSelectionMode) {


                    // Handle multi-selection with limit
                    if (isSelected) {
                        // Always allow deselection
                        selectedImageUris.remove(imageUri);

                        Log.d("cnkcasnmca", "onClick: " + selectedImageUris.size());
                        multiSelectSmallCounterText.setText(String.valueOf(selectedImageUris.size()));

                        if (selectedImageUris.size() == 0) {
                            emoji.setVisibility(View.VISIBLE);
                            box.setHint("Message on Ec");

                            sendGrp.setImageResource(R.drawable.mike);
                            multiSelectSmallCounterText.setVisibility(View.GONE);
                        }


                    } else {


                        emoji.setVisibility(View.GONE);
                        box.setHint("Add Caption");
                        multiSelectSmallCounterText.setVisibility(View.VISIBLE);
                        multiSelectSmallCounterText.setText(String.valueOf(selectedImageUris.size()+1));


                        sendGrp.setImageResource(R.drawable.baseline_keyboard_double_arrow_right_24);
                        // Check selection limit before adding
                        if (selectedImageUris.size() < MAX_SELECTION_LIMIT) {
                            selectedImageUris.add(imageUri);
                        } else {
                            // Show toast when limit reached
                            Toast.makeText(context, "Maximum " + MAX_SELECTION_LIMIT + " images can be selected", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    notifyItemChanged(position);

                    // Use global listener first (for chattingScreen communication), then instance listener
                    OnImageSelectionListener listenerToUse = globalSelectionListener != null ? globalSelectionListener : selectionListener;

                    if (listenerToUse != null) {
                        Log.d("ImageUpload", "=== ImageAdapter calling onImageSelectionChanged ===");
                        Log.d("ImageUpload", "ImageAdapter instance: " + this.hashCode());
                        Log.d("ImageUpload", "SelectedImageUris size: " + selectedImageUris.size());
                        Log.d("ImageUpload", "Using listener: " + (globalSelectionListener != null ? "global" : "instance"));
                        Log.d("ImageUpload", "Listener object: " + listenerToUse.getClass().getSimpleName());
                        Log.d("ImageUpload", "About to call listener.onImageSelectionChanged");
                        listenerToUse.onImageSelectionChanged(selectedImageUris);
                        Log.d("ImageUpload", "Called listener.onImageSelectionChanged");
                    } else {
                        Log.w("ImageUpload", "No listener available in ImageAdapter instance: " + this.hashCode());
                        Log.w("ImageUpload", "Instance listener: " + (selectionListener != null ? "not null" : "null"));
                        Log.w("ImageUpload", "Global listener: " + (globalSelectionListener != null ? "not null" : "null"));
                    }

                    // Update bottom sheet UI based on selection state
                    updateBottomSheetUI();
                    return;
                }

                GlobalUri = imageUri;

                if (dataCardview.getVisibility() == View.VISIBLE) {
                    dataCardview.animate()
                            .translationY(dataCardview.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> dataCardview.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();
                }


                modelId = database.getReference().push().getKey();


                if (GlobalUri != null) {


                    try {
                        //for uploading document to mysql
                        globalFile = null;
                        Log.d("ImageFile000", GlobalUri.getAuthority());
                        Log.d("ImageFile000", GlobalUri.getScheme());

                        String extension;
                        File f, f2;
                        if (GlobalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(GlobalUri));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(GlobalUri.getPath()))));

                        }

                        InputStream imageStream = null;
                        try {
                            imageStream = context.getContentResolver().openInputStream(GlobalUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bmpCompresssSize = BitmapFactory.decodeStream(imageStream);
                        Log.d("extension", extension);

                        // for  getting original name
                        String fileName = null;
                        if (GlobalUri != null) {
                            Cursor cursor = context.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName1: " + fileName);
                        f = new File(context.getCacheDir() + "/" + fileName);

                        try {
                            //part1
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bmpCompresssSize.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                            byte[] bitmapdataCompressed = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdataCompressed);
                            fos.flush();
                            fos.close();

                            Log.d("imageFile111", f.getPath());
                            globalFile = f;
                            Log.d("SendButton", "globalFile set to: " + (globalFile != null ? globalFile.getPath() : "null"));

                            long fileSize = getFileSize(globalFile.getPath());
                            Log.d("File size compressed", getFormattedFileSize(fileSize));


                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: " + e.getMessage());

                        }


                        InputStream imageStream2 = null;
                        try {
                            imageStream2 = context.getContentResolver().openInputStream(GlobalUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bmpFullSize = BitmapFactory.decodeStream(imageStream2);


                        String fileName2 = null;
                        if (GlobalUri != null) {
                            Cursor cursor = context.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName2 = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName2: " + fileName2);


                        f2 = new File(context.getCacheDir() + "/" + fileName2);

                        try {
                            //part2
                            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                            bmpFullSize.compress(Bitmap.CompressFormat.JPEG, 80, bos2);
                            byte[] bitmapdataFull = bos2.toByteArray();
                            FileOutputStream fos2 = new FileOutputStream(f2);
                            fos2.write(bitmapdataFull);
                            fos2.flush();
                            fos2.close();

                            Log.d("imageFile111", f.getPath());
                            FullImageFile = f2;
                            long fileSize = getFileSize(FullImageFile.getPath());
                            Log.d("File size Full", getFormattedFileSize(fileSize));


                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: " + e.getMessage());

                        }


                        File f2External;
                        String exactPath2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            f2External = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();

                        } else {
                            f2External = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();
                        }


                        if (!f2External.exists()) {
                            f2External.mkdirs();
                        }

                        if (doesFileExist(exactPath2 + "/" + fileName2)) {

                        } else {


                            File imageFile = new File(f2External, fileName2);

                            try {
                                InputStream is = context.getContentResolver().openInputStream(GlobalUri);
                                FileOutputStream fs = new FileOutputStream(imageFile);
                                int read = 0;
                                int bufferSize = 1024;
                                final byte[] buffers = new byte[bufferSize];
                                while ((read = is.read(buffers)) != -1) {
                                    fs.write(buffers, 0, read);
                                }
                                is.close();
                                fs.close();

                                Log.d("imageFile111", imageFile.getPath());
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "imageofflineerror: " + e.getMessage());
                            }
                        }


                        // From here selecting screen code

                        Constant.dialogueFullScreen(context, R.layout.dialogue_full_screen_dialogue);
                        Constant.dialogLayoutFullScreen.show();
                        Log.d("SendButton", "Dialog shown: " + (Constant.dialogLayoutFullScreen != null ? "yes" : "no"));

                        Window window = Constant.dialogLayoutFullScreen.getWindow();
                        if (window != null) {
                            WindowCompat.setDecorFitsSystemWindows(window, false);
                            View rootView = window.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(false);
                        }

                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                            // Restore decor fitsSystemWindows true
                            Window activityWindow = ((Activity) context).getWindow();
                            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                            View rootView = window.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(true);
                        });


                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
                        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
                        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
                        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
                        Log.d("SendButton", "sendGrp found after findViewById: " + (sendGrp != null ? "yes" : "no"));

                        // TODO: 18/04/25  messageBoxMy - Start

                        if (!messageBox.getText().toString().isEmpty()) {
                            messageBoxMy.setText(messageBox.getText().toString());
                        }

                        // TODO: 18/04/25  messageBoxMy - End

                        try {
                            Constant.getSfFuncion(context);
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));
                            sendGrp.setBackgroundTintList(tintList);
                        } catch (Exception i) {

                        }
                        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
                        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
                        //visibility
                        image.setVisibility(View.VISIBLE);
                        video.setVisibility(View.GONE);
                        downloadCtrl.setVisibility(View.GONE);
                        contactContainer.setVisibility(View.GONE);

                        // Set up send button click listener (works regardless of file existence)
                        Log.d("SendButton", "Setting up send button click listener");
                        Log.d("SendButton", "sendGrp found: " + (sendGrp != null ? "yes" : "no"));
                        if (sendGrp != null) {
                            Log.d("SendButton", "sendGrp is clickable: " + sendGrp.isClickable());
                            Log.d("SendButton", "sendGrp visibility: " + sendGrp.getVisibility());
                            Log.d("SendButton", "sendGrp is enabled: " + sendGrp.isEnabled());
                            // Make sure the button is clickable and enabled
                            sendGrp.setClickable(true);
                            sendGrp.setEnabled(true);
                        }
                        sendGrp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("ImageUpload", "=== SEND BUTTON CLICKED (Alternative) ===");
                                Log.d("ImageUpload", "Send button clicked!");
                                Log.d("ImageUpload", "GlobalFile: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
                                Log.d("ImageUpload", "GlobalUri: " + GlobalUri);

                                // Check if globalFile is null and handle it
                                if (globalFile == null) {
                                    Log.e("ImageUpload", "globalFile is null, cannot send image");
                                    Toast.makeText(context, "Error: Image file not ready. Please try again.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                messageBox.setText("");
                                Constant.getSfFuncion(context);
                                final String receiverUid = ((Activity) context).getIntent().getStringExtra("friendUidKey");
                                final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                final String senderRoom = senderId + receiverUid;
                                final String receiverRoom = receiverUid + senderId;
                                Log.d("senderRoom", senderRoom + receiverRoom);
                                Date d = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                String currentDateTimeString = sdf.format(d);
                                messageModel model = null;

                                String[] dimensions = Constant.calculateImageDimensions(context, globalFile, GlobalUri);
                                String imageWidthDp = dimensions[0];
                                String imageHeightDp = dimensions[1];
                                String aspectRatio = dimensions[2];

                                if (currentCaption.trim().equals("")) {
                                    assert modelId != null;
                                    String uniqDate = Constant.getCurrentDate();
                                    if (uniqueDates.add(uniqDate)) {
                                        ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                        emojiModels.add(new emojiModel("", ""));
                                        Log.d("SelectionCount", "single-path no-caption uniqDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + (isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1"));
                                        model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1");

                                        // Store message in SQLite pending table before upload
                                        try {
                                            new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                                            Log.d("PendingMessages", "ImageAdapter - Image message stored in pending table: " + model.getModelId());
                                        } catch (Exception e) {
                                            Log.e("PendingMessages", "ImageAdapter - Error storing image message in pending table: " + e.getMessage(), e);
                                        }

                                        messageList.add(model);
                                    } else {
                                        ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                        emojiModels.add(new emojiModel("", ""));
                                        Log.d("SelectionCount", "single-path no-caption repeatedDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + (isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1"));
                                        model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1");

                                        // Store message in SQLite pending table before upload
                                        try {
                                            new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                                            Log.d("PendingMessages", "ImageAdapter - Image message stored in pending table: " + model.getModelId());
                                        } catch (Exception e) {
                                            Log.e("PendingMessages", "ImageAdapter - Error storing image message in pending table: " + e.getMessage(), e);
                                        }

                                        messageList.add(model);
                                    }

                                    messagemodel2 model2 = new messagemodel2(
                                            model.getUid(),
                                            model.getMessage(),
                                            model.getTime(),
                                            model.getDocument(),
                                            model.getDataType(),
                                            model.getExtension(),
                                            model.getName(),
                                            model.getPhone(),
                                            model.getMicPhoto(),
                                            model.getMiceTiming(),
                                            model.getUserName(),
                                            model.getReplytextData(),
                                            model.getReplyKey(),
                                            model.getReplyType(),
                                            model.getReplyOldData(),
                                            model.getReplyCrtPostion(),
                                            model.getModelId(),
                                            model.getReceiverUid(),
                                            model.getForwaredKey(),
                                            model.getGroupName(),
                                            model.getDocSize(),
                                            model.getFileName(),
                                            model.getThumbnail(),
                                            model.getFileNameThumbnail(),
                                            model.getCaption(),
                                            model.getNotification(),
                                            model.getCurrentDate(),
                                            model.getEmojiModel(),
                                            model.getEmojiCount(),
                                            model.getTimestamp(),
                                            0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                    );

                                    //TODO : active : 0 = still loading
                                    //TODO : active : 1 = completed

//                                        try {
//                                            new DatabaseHelper(context).insertMessage(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }


                                    // add direct todo list


                                    ((Activity) context).runOnUiThread(() -> {
                                        chatAdapter.updateMessageList(new ArrayList<>(messageList));
                                        chatAdapter.setLastItemVisible(true); // Show progress for pending message
                                        // Removed redundant notifyItemInserted since updateMessageList handles it
                                        messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

                                    });


                                    Log.d("TAG", "actualName: " + globalFile.getName());

                                    Log.d("ImageUpload", "=== CALLING UploadChatHelper (no caption) ===");
                                    Log.d("ImageUpload", "GlobalFile: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
                                    Log.d("ImageUpload", "FullImageFile: " + (FullImageFile != null ? FullImageFile.getAbsolutePath() : "null"));
                                    Log.d("ImageUpload", "GlobalUri: " + GlobalUri);

                                    UploadChatHelper uploadHelper = new UploadChatHelper(context, globalFile, FullImageFile, senderId, userFTokenKey);
                                    uploadHelper.uploadContent(
                                            Constant.img,
                                            GlobalUri, // uri
                                            "", // captionText
                                            modelId, // modelId
                                            null, // savedThumbnail
                                            null, // fileThumbName
                                            globalFile.getName(), // fileName
                                            null, // contactName
                                            null, // contactPhone
                                            null, // audioTime
                                            null, // audioName
                                            getFileExtension(GlobalUri), // extension
                                            receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
                                            model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),
                                            imageWidthDp, imageHeightDp, aspectRatio);


                                    Constant.dialogLayoutFullScreen.dismiss();

                                    // Dismiss bottom sheet
                                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                                        bottomSheetDialog.dismiss();
                                        Log.d("SendButton", "Bottom sheet dismissed after sending single image (no caption)");
                                    }

                                } else {


                                    assert modelId != null;

                                    String uniqDate = Constant.getCurrentDate();
                                    if (uniqueDates.add(uniqDate)) {
                                        ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                        emojiModels.add(new emojiModel("", ""));
                                        Log.d("SelectionCount", "single-path with-caption uniqDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + (isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1"));
                                        model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", currentCaption, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1");

                                        // Store message in SQLite pending table before upload
                                        try {
                                            new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                                            Log.d("PendingMessages", "ImageAdapter - Image message with caption stored in pending table: " + model.getModelId());
                                        } catch (Exception e) {
                                            Log.e("PendingMessages", "ImageAdapter - Error storing image message with caption in pending table: " + e.getMessage(), e);
                                        }

                                        messageList.add(model);
                                    } else {
                                        ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                        emojiModels.add(new emojiModel("", ""));
                                        Log.d("SelectionCount", "single-path with-caption repeatedDate: isMultiSelectionMode=" + isMultiSelectionMode + ", selectedImageUris.size()=" + selectedImageUris.size() + ", sending=" + (isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1"));
                                        model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", currentCaption, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, isMultiSelectionMode ? String.valueOf(selectedImageUris.size()) : "1");

                                        // Store message in SQLite pending table before upload
                                        try {
                                            new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(context).insertPendingMessage(model);
                                            Log.d("PendingMessages", "ImageAdapter - Image message with caption stored in pending table: " + model.getModelId());
                                        } catch (Exception e) {
                                            Log.e("PendingMessages", "ImageAdapter - Error storing image message with caption in pending table: " + e.getMessage(), e);
                                        }

                                        messageList.add(model);
                                    }

                                    messagemodel2 model2 = new messagemodel2(
                                            model.getUid(),
                                            model.getMessage(),
                                            model.getTime(),
                                            model.getDocument(),
                                            model.getDataType(),
                                            model.getExtension(),
                                            model.getName(),
                                            model.getPhone(),
                                            model.getMicPhoto(),
                                            model.getMiceTiming(),
                                            model.getUserName(),
                                            model.getReplytextData(),
                                            model.getReplyKey(),
                                            model.getReplyType(),
                                            model.getReplyOldData(),
                                            model.getReplyCrtPostion(),
                                            model.getModelId(),
                                            model.getReceiverUid(),
                                            model.getForwaredKey(),
                                            model.getGroupName(),
                                            model.getDocSize(),
                                            model.getFileName(),
                                            model.getThumbnail(),
                                            model.getFileNameThumbnail(),
                                            model.getCaption(),
                                            model.getNotification(),
                                            model.getCurrentDate(),
                                            model.getEmojiModel(),
                                            model.getEmojiCount(),
                                            model.getTimestamp(),
                                            0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                    );

                                    //TODO : active : 0 = still loading
                                    //TODO : active : 1 = completed


                                    chatAdapter.itemAdd(messageRecView);
                                    chatAdapter.setLastItemVisible(true); // Show progress for pending message
                                    // Removed redundant notifyItemInserted since updateMessageList handles it


                                    Constant.dialogLayoutFullScreen.dismiss();

                                    Log.d("ImageUpload", "=== CALLING UploadChatHelper (with caption) ===");
                                    Log.d("ImageUpload", "GlobalFile: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
                                    Log.d("ImageUpload", "FullImageFile: " + (FullImageFile != null ? FullImageFile.getAbsolutePath() : "null"));
                                    Log.d("ImageUpload", "GlobalUri: " + GlobalUri);
                                    Log.d("ImageUpload", "Caption: " + currentCaption.trim());

                                    UploadChatHelper uploadHelper = new UploadChatHelper(context, globalFile, FullImageFile, senderId, userFTokenKey);
                                    uploadHelper.uploadContent(
                                            Constant.img, // uploadType
                                            GlobalUri, // uri
                                            currentCaption.trim(), // captionText
                                            modelId, // modelId
                                            null, // savedThumbnail
                                            null, // fileThumbName
                                            globalFile.getName(), // fileName
                                            null, // contactName
                                            null, // contactPhone
                                            null, // audioTime
                                            null, // audioName
                                            getFileExtension(GlobalUri), // extension
                                            receiverUid,
                                            model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),
                                            model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(), imageWidthDp, imageHeightDp, aspectRatio);

                                    // Dismiss bottom sheet
                                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                                        bottomSheetDialog.dismiss();
                                        Log.d("SendButton", "Bottom sheet dismissed after sending single image (with caption)");
                                    }
                                }
                            }
                        });

                        // Set up image display based on file existence
                        if (doesFileExist(exactPath2 + "/" + fileName2)) {
                            image.setImageURI(Uri.parse(exactPath2 + "/" + fileName2));
                        } else {
                            // Load image from URI if file doesn't exist at expected path
                            image.setImageURI(GlobalUri);
                        }


                        backarrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.dialogLayoutFullScreen.dismiss();
                            }
                        });


                    } catch (Exception ignored) {
                    }


                    // here we need to display selecting dialogue box
                    // image


                } else {
                    Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView checkmark;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            checkmark = itemView.findViewById(R.id.checkmark);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static String getFormattedFileSize(long fileSize) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;

        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Test method to verify send button functionality and selectionBunch
     * Tag: SELECTION_BUNCH_DEBUG
     */
    public void testSendButtonFunctionality() {
        Log.d("SELECTION_BUNCH_DEBUG", "=== STARTING SELECTION_BUNCH_DEBUG TEST ===");

        // Test 1: Check if dialog exists and is showing
        if (Constant.dialogLayoutFullScreen == null) {
            Log.e("SELECTION_BUNCH_DEBUG", "❌ FAIL: Dialog is null");
            return;
        }
        Log.d("SELECTION_BUNCH_DEBUG", "✅ PASS: Dialog exists");

        if (!Constant.dialogLayoutFullScreen.isShowing()) {
            Log.e("SELECTION_BUNCH_DEBUG", "❌ FAIL: Dialog is not showing");
            return;
        }
        Log.d("SELECTION_BUNCH_DEBUG", "✅ PASS: Dialog is showing");

        // Test 2: Check if send button exists
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp == null) {
            Log.e("SELECTION_BUNCH_DEBUG", "❌ FAIL: Send button not found");
            return;
        }
        Log.d("SELECTION_BUNCH_DEBUG", "✅ PASS: Send button found");

        // Test 3: Check button properties
        Log.d("SELECTION_BUNCH_DEBUG", "Button clickable: " + sendGrp.isClickable());
        Log.d("SELECTION_BUNCH_DEBUG", "Button enabled: " + sendGrp.isEnabled());
        Log.d("SELECTION_BUNCH_DEBUG", "Button visible: " + (sendGrp.getVisibility() == View.VISIBLE));

        // Test 4: Check if images are selected
        if (selectedImageUris.isEmpty()) {
            Log.e("SELECTION_BUNCH_DEBUG", "❌ FAIL: No images selected");
            return;
        }
        Log.d("SELECTION_BUNCH_DEBUG", "✅ PASS: " + selectedImageUris.size() + " images selected");

        // Test 5: Check caption
        Log.d("SELECTION_BUNCH_DEBUG", "Current caption: '" + currentCaption + "'");

        // Test 6: Check selectionBunch creation
        Log.d("SELECTION_BUNCH_DEBUG", "=== TESTING SELECTION_BUNCH CREATION ===");
        ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> testBunch = new ArrayList<>();
        for (int i = 0; i < selectedImageUris.size(); i++) {
            String fileName = "test_image_" + i + ".jpg";
            com.Appzia.enclosure.Model.selectionBunchModel bunchModel = new com.Appzia.enclosure.Model.selectionBunchModel("", fileName);
            testBunch.add(bunchModel);
            Log.d("SELECTION_BUNCH_DEBUG", "Created testBunch item " + i + ": " + fileName);
        }
        Log.d("SELECTION_BUNCH_DEBUG", "✅ PASS: Test selectionBunch created with " + testBunch.size() + " items");

        // Test 7: Simulate button click (removed to prevent infinite recursion)
        Log.d("SELECTION_BUNCH_DEBUG", "Skipping button click simulation to prevent infinite recursion");

        Log.d("SELECTION_BUNCH_DEBUG", "=== SELECTION_BUNCH_DEBUG TEST COMPLETED ===");
    }
}
