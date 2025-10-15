package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Appzia.enclosure.Adapter.ImageGridAdapter;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppLikeImagePicker extends AppCompatActivity {
    public static final String EXTRA_SELECTED_IMAGES = "selected_images";
    public static final String EXTRA_MAX_SELECTION = "max_selection";
    public static final int DEFAULT_MAX_SELECTION = 30;
    private GridView gridView;
    private TextView counterText;
    private TextView smallCounterText;
    private LinearLayout doneButton;
    private LinearLayout cancelButton;
    private ProgressBar loadingIndicator;
    private ImageGridAdapter adapter;
    private List<Uri> selectedImages = new ArrayList<>();
    private int maxSelection = DEFAULT_MAX_SELECTION;

    // New: WhatsApp-like caption bar controls
    private LinearLayout multiSelectDoneButton;
    private TextView multiSelectSmallCounterText;
    private LinearLayout multiSelectSendGrpLyt;
    private android.widget.EditText messageBoxMy;
    
    // Keyboard handling variables
    private boolean isMessageBoxFocused = false;
    private LinearLayout captionLyt;
    
    // Permission text handling
    private TextView managePermissionText;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_image_picker);
        
        // Fix keyboard behavior - adjust pan to move content above keyboard
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
        // Get max selection from intent
        maxSelection = getIntent().getIntExtra(EXTRA_MAX_SELECTION, DEFAULT_MAX_SELECTION);
        
        // Get already selected images from intent
        ArrayList<Uri> alreadySelected = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_IMAGES);
        if (alreadySelected != null) {
            selectedImages.addAll(alreadySelected);
        }
        
        initViews();
        setupAdapter();
        updateUI();
    }
    
    private void initViews() {
        gridView = findViewById(R.id.imageGrid);
        counterText = findViewById(R.id.counterText);
        smallCounterText = findViewById(R.id.smallCounterText);
        doneButton = findViewById(R.id.doneButton);
        cancelButton = findViewById(R.id.cancelButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // Bind new caption UI
        messageBoxMy = findViewById(R.id.messageBoxMy);
        multiSelectDoneButton = findViewById(R.id.multiSelectDoneButton);
        multiSelectSmallCounterText = findViewById(R.id.multiSelectSmallCounterText);
        multiSelectSendGrpLyt = findViewById(R.id.multiSelectSendGrpLyt);
        captionLyt = findViewById(R.id.captionlyt);
        managePermissionText = findViewById(R.id.managePermissionText);
        
        // Apply theme colors
        applyThemeColors();
        
        // Load all images from MediaStore
        loadImages();
        
        // Hide legacy doneButton UI; use new WhatsApp-like bar
        if (doneButton != null) {
            doneButton.setVisibility(View.GONE);
        }

        // Setup new Done button to return images + caption
        if (multiSelectDoneButton != null) {
            multiSelectDoneButton.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_IMAGES, new ArrayList<>(selectedImages));
                String caption = messageBoxMy != null ? messageBoxMy.getText().toString().trim() : "";
                resultIntent.putExtra("caption", caption);
                setResult(Activity.RESULT_OK, resultIntent);
                SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeImagePicker.this);
            });
        }
        
        cancelButton.setOnClickListener(v -> {
            // Clear all selections when canceling
            selectedImages.clear();
            if (adapter != null) {
                adapter.clearSelection();
            }
            setResult(Activity.RESULT_CANCELED);
            SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeImagePicker.this);
        });
        
        // Setup keyboard handling and messageBox listeners
        setupKeyboardHandling();
        
        // Setup permission text
        setupPermissionText();
    }
    
    private void applyThemeColors() {
        // Get theme color from SharedPreferences
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(themeColor);

       ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));
        
        // Create circular background for Done button (legacy/new)
        GradientDrawable doneButtonBg = new GradientDrawable();
        doneButtonBg.setShape(GradientDrawable.OVAL);
        doneButtonBg.setColor(color);
        if (doneButton != null) {
            doneButton.setBackground(doneButtonBg);
        }
        if (multiSelectDoneButton != null) {
            multiSelectDoneButton.setBackground(doneButtonBg);
        }
        
        // Create circular background for small counter (legacy/new)
        GradientDrawable counterBg = new GradientDrawable();
        counterBg.setShape(GradientDrawable.OVAL);
        counterBg.setColor(color);
        counterBg.setStroke(2, Color.WHITE); // White border
        if (smallCounterText != null) {
            smallCounterText.setBackground(counterBg);
        }
        if (multiSelectSmallCounterText != null) {
            multiSelectSmallCounterText.setBackground(counterBg);
        }
        
        // Update adapter with theme color
        if (adapter != null) {
            adapter.setThemeColor(color,tintList);
        }
    }
    
    private void loadImages() {
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
        
        // Load images in background thread
        new Thread(() -> {
            List<Uri> imageUris = new ArrayList<>();
            
            String[] projection = {MediaStore.Images.Media._ID};
            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
            
            Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            );
            
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        String.valueOf(id)
                    );
                    imageUris.add(contentUri);
                }
                cursor.close();
            }
            
            // Update UI on main thread
            runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                adapter.updateImages(imageUris);
                updateUI();
            });
        }).start();
    }
    
    private void setupAdapter() {
        adapter = new ImageGridAdapter(this, new ArrayList<>(), selectedImages, maxSelection);
        
        // Apply theme color to adapter
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(themeColor);
        ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));
        adapter.setThemeColor(color, tintList);
        
        adapter.setOnImageSelectedListener(new ImageGridAdapter.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri imageUri, boolean isSelected) {
                if (isSelected) {
                    if (selectedImages.size() < maxSelection) {
                        selectedImages.add(imageUri);
                    }
                } else {
                    selectedImages.remove(imageUri);
                }
                updateUI();
                updateCaptionAndDoneState();
            }
        });
        gridView.setAdapter(adapter);
    }
    
    private void updateUI() {
        int selectedCount = selectedImages.size();
        counterText.setText(String.valueOf(selectedCount));
        
        // Update small counter (legacy header). Null-guard if header not present
        if (smallCounterText != null) {
            if (selectedCount > 0) {
                smallCounterText.setText(String.valueOf(selectedCount));
                smallCounterText.setVisibility(View.VISIBLE);
            } else {
                smallCounterText.setVisibility(View.GONE);
            }
        }
        
        // Update legacy done button state (kept for safety)
        if (doneButton != null) {
            doneButton.setEnabled(selectedCount > 0);
            doneButton.setAlpha(selectedCount > 0 ? 1.0f : 0.5f);
        }

        // Also sync new WhatsApp-like bar
        updateCaptionAndDoneState();
        
        // Update adapter selection state
        adapter.notifyDataSetChanged();
    }

    private void updateCaptionAndDoneState() {
        if (multiSelectSendGrpLyt != null) {
            multiSelectSendGrpLyt.setVisibility(View.VISIBLE);
        }
        int selectedCount = selectedImages.size();
        if (multiSelectSmallCounterText != null) {
            if (selectedCount > 0) {
                multiSelectSmallCounterText.setText(String.valueOf(selectedCount));
                multiSelectSmallCounterText.setVisibility(View.VISIBLE);
            } else {
                multiSelectSmallCounterText.setVisibility(View.GONE);
            }
        }
        if (multiSelectDoneButton != null) {
            multiSelectDoneButton.setEnabled(selectedCount > 0);
            multiSelectDoneButton.setAlpha(selectedCount > 0 ? 1.0f : 0.5f);
        }
    }
    
    private void setupKeyboardHandling() {
        if (messageBoxMy == null) return;
        
        // Set initial 55dp bottom margin for captionLyt
        updateCaptionLayoutMargin(false);
        
        // Setup messageBox listeners
        messageBoxMy.setOnClickListener(v -> {
            Log.d("MessageBoxTouch", "MessageBox touched, setting focus to true");
            isMessageBoxFocused = true;
            updateCaptionLayoutMargin(true);
        });
        
        messageBoxMy.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d("MessageBoxFocus", "MessageBox focus changed: " + hasFocus);
            isMessageBoxFocused = hasFocus;
            updateCaptionLayoutMargin(hasFocus);
        });
        
        // Setup keyboard visibility listener
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Detect keyboard visibility
                View rootView = getWindow().getDecorView().getRootView();
                android.graphics.Rect r = new android.graphics.Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                
                // Keyboard is visible if keypad height is more than 15% of screen height
                boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;
                
                Log.d("KeyboardLayout", "Keypad height: " + keypadHeight + ", Screen height: " + screenHeight + 
                      ", Keyboard visible: " + isKeyboardVisible);
                
                updateCaptionLayoutMargin(isKeyboardVisible);
            }
        });
    }
    
    private void updateCaptionLayoutMargin(boolean isKeyboardVisible) {
        if (captionLyt == null) return;
        
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) captionLyt.getLayoutParams();
        if (params != null) {
            int baseMargin = (int) (55 * getResources().getDisplayMetrics().density); // 55dp base margin
            int keyboardMargin = (int) (120 * getResources().getDisplayMetrics().density); // 120dp when keyboard is open
            
            if (isKeyboardVisible && isMessageBoxFocused) {
                params.bottomMargin = keyboardMargin;
                Log.d("CaptionMargin", "Keyboard visible and focused - setting margin to " + keyboardMargin + "px (120dp)");
            } else {
                params.bottomMargin = baseMargin;
                Log.d("CaptionMargin", "Keyboard not visible or not focused - setting margin to " + baseMargin + "px (55dp)");
            }
            
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
        }
    }
    
    @Override
    public void onBackPressed() {
        Log.d("BackPressed", "Back button pressed - messageBoxMy focus: " + (messageBoxMy != null ? messageBoxMy.hasFocus() : "null") + ", isMessageBoxFocused: " + isMessageBoxFocused);
        
        // Simple one-tap logic: if messageBox is focused, clear focus; otherwise finish activity
        if (messageBoxMy != null && (messageBoxMy.hasFocus() || isMessageBoxFocused)) {
            Log.d("BackPressed", "MessageBox is focused - clearing focus and hiding keyboard");
            
            // Clear focus immediately
            messageBoxMy.clearFocus();
            isMessageBoxFocused = false;
            
            // Hide keyboard with the most reliable method
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(messageBoxMy.getWindowToken(), 0);
            }
            
            Log.d("BackPressed", "Focus cleared - staying in activity");
            return; // Don't finish activity, just clear focus
        }
        
        Log.d("BackPressed", "MessageBox not focused - finishing activity");
        SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeImagePicker.this); // Finish activity with swipe
    }
    
    private void setupPermissionText() {
        if (managePermissionText == null) {
            Log.d("PermissionText", "managePermissionText is null - TextView not found");
            return;
        }
        
        boolean hasLimitedAccess = hasLimitedPhotoAccess();
        Log.d("PermissionText", "hasLimitedAccess: " + hasLimitedAccess);
        
        if (hasLimitedAccess) {
            Log.d("PermissionText", "Showing managePermissionText");
            managePermissionText.setVisibility(View.VISIBLE);
            setupUnderlinedManageText();
        } else {
            Log.d("PermissionText", "Hiding managePermissionText");
            managePermissionText.setVisibility(View.GONE);
        }
    }
    
    private boolean hasLimitedPhotoAccess() {
        return com.Appzia.enclosure.Utils.GlobalPermissionPopup.hasLimitedPhotoAccess(this);
    }
    
    private void setupUnderlinedManageText() {
        if (managePermissionText == null) return;
        
        String fullText = "You've given Enclosure permission to access only a select number of photos. Manage";
        String clickableText = "Manage";
        
        // Create SpannableString
        android.text.SpannableString spannableString = new android.text.SpannableString(fullText);
        
        // Find the start and end positions of the clickable text
        int start = fullText.indexOf(clickableText);
        int end = start + clickableText.length();
        
        if (start != -1) {
            // Get theme color from SharedPreferences
            String themeColor = com.Appzia.enclosure.Utils.Constant.getSF.getString(com.Appzia.enclosure.Utils.Constant.ThemeColorKey, "#00A3E9");
            int color = android.graphics.Color.parseColor(themeColor);
            
            // Create clickable span
            android.text.style.ClickableSpan clickableSpan = new android.text.style.ClickableSpan() {
                @Override
                public void onClick(android.view.View widget) {
                    // Open app settings
                    android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    android.net.Uri uri = android.net.Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    SwipeNavigationHelper.startActivityWithSwipe(WhatsAppLikeImagePicker.this, intent);
                }
                
                @Override
                public void updateDrawState(android.text.TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                    ds.setColor(color); // Use theme color for the "Manage" text
                }
            };
            
            // Apply underline span
            spannableString.setSpan(new android.text.style.UnderlineSpan(), start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Apply clickable span
            spannableString.setSpan(clickableSpan, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Set the text and make it clickable
            managePermissionText.setText(spannableString);
            managePermissionText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        }
    }
    
}
