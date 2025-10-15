package com.Appzia.enclosure.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.MultipleImageDialogAdapter;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class MultipleImageDialog extends DialogFragment {

    private static final String TAG = "MultipleImageDialog";
    private static final String ARG_IMAGE_LIST = "imageList";
    private static final String ARG_CURRENT_POSITION = "currentPosition";
    private static final String ARG_VIEW_HOLDER_TYPE_KEY = "viewHolderTypeKey";
    private static final String ARG_TARGET_FILENAME = "targetFilename";

    private RecyclerView recyclerView;
    private MultipleImageDialogAdapter adapter;
    private List<String> imageList;
    private int currentPosition;
    private String viewHolderTypeKey;
    private String targetFilename;
    LottieAnimationView lottieAnimationView;
    private LinearLayout backButton;
    private TextView titleText;

    // Interface for dialog callbacks
    public interface OnImageClickListener {
        void onImageClick(String imagePath, int position);
    }

    private OnImageClickListener imageClickListener;

    public static MultipleImageDialog newInstance(List<String> imageList, int currentPosition, String viewHolderTypeKey) {
        MultipleImageDialog dialog = new MultipleImageDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_LIST, new ArrayList<>(imageList));
        args.putInt(ARG_CURRENT_POSITION, currentPosition);
        args.putString(ARG_VIEW_HOLDER_TYPE_KEY, viewHolderTypeKey);
        dialog.setArguments(args);
        return dialog;
    }

    public static MultipleImageDialog newInstanceWithFilename(List<String> imageList, String targetFilename, String viewHolderTypeKey) {
        MultipleImageDialog dialog = new MultipleImageDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_LIST, new ArrayList<>(imageList));
        args.putString(ARG_TARGET_FILENAME, targetFilename);
        args.putString(ARG_VIEW_HOLDER_TYPE_KEY, viewHolderTypeKey);
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.imageClickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            imageList = args.getStringArrayList(ARG_IMAGE_LIST);
            currentPosition = args.getInt(ARG_CURRENT_POSITION, 0);
            viewHolderTypeKey = args.getString(ARG_VIEW_HOLDER_TYPE_KEY);
            targetFilename = args.getString(ARG_TARGET_FILENAME);
        }

        // Log received data
        Log.d(TAG, "=== DIALOG CREATED ===");
        Log.d(TAG, "Image list size: " + (imageList != null ? imageList.size() : 0));
        Log.d(TAG, "Current position: " + currentPosition);
        Log.d(TAG, "View holder type key: " + viewHolderTypeKey);
        Log.d(TAG, "Target filename: " + targetFilename);

        // Log the complete image list
        if (imageList != null) {
            Log.d(TAG, "=== COMPLETE IMAGE LIST ===");
            for (int i = 0; i < imageList.size(); i++) {
                Log.d(TAG, "Image " + i + ": " + imageList.get(i));
            }
        }

        if (imageList == null || imageList.isEmpty()) {
            Log.w(TAG, "No images found in arguments");
            dismiss();
            return;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Remove title bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = dialog.getWindow();
        if (window != null) {
            // Make dialog full screen with transparent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
            );

            // Set dialog to match parent width and height (full screen)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            // Make dialog background transparent for custom styling
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set window attributes for full screen with transparent status bar
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
            window.setAttributes(params);

            // Make status bar transparent and allow content to extend behind it
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );

            // Apply bottom-to-top animation
            window.setWindowAnimations(R.style.DialogAnimation);
        }

        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_multiple_image, container, false);

        // Initialize views
        initViews(view);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup click listeners
        setupClickListeners();
        
        // Initialize animation state
        initializeAnimationState();

        return view;
    }


    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        backButton = view.findViewById(R.id.backButton);
        titleText = view.findViewById(R.id.titleText);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isAtTop = false;
            private boolean isAtBottom = false;
            private long lastScrollTime = 0;
            private static final long SCROLL_THRESHOLD_MS = 100; // Prevent rapid toggling
            
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                // Debug logging
                Log.d(TAG, "Scroll: firstVisible=" + firstVisibleItem + ", lastVisible=" + lastVisibleItem + ", total=" + totalItemCount + ", dy=" + dy);

                // Handle edge cases
                if (totalItemCount <= 1) {
                    // Single item or empty list - hide animation
                    if (lottieAnimationView.getVisibility() == View.VISIBLE) {
                        lottieAnimationView.setVisibility(View.GONE);
                    }
                    return;
                }

                // Throttle scroll events to prevent rapid toggling
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastScrollTime < SCROLL_THRESHOLD_MS) {
                    return;
                }
                lastScrollTime = currentTime;

                // Check if at top (more lenient detection)
                boolean currentlyAtTop = firstVisibleItem == 0;
                if (currentlyAtTop != isAtTop) {
                    isAtTop = currentlyAtTop;
                    if (isAtTop) {
                        Log.d(TAG, "Reached top!");
                   //     Toast.makeText(recyclerView.getContext(), "Reached top!", Toast.LENGTH_SHORT).show();
                        // Show animation when at top (indicating more content below)
                        if (lottieAnimationView.getVisibility() == View.GONE) {
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                // Check if at bottom (more lenient detection)
                boolean currentlyAtBottom = lastVisibleItem >= totalItemCount - 1;
                if (currentlyAtBottom != isAtBottom) {
                    isAtBottom = currentlyAtBottom;
                    if (isAtBottom) {
                        Log.d(TAG, "Reached bottom!");
                   //     Toast.makeText(recyclerView.getContext(), "Reached bottom!", Toast.LENGTH_SHORT).show();
                        // Hide animation when at bottom (no more content below)
                        if (lottieAnimationView.getVisibility() == View.VISIBLE) {
                            lottieAnimationView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

    }

    private void setupRecyclerView() {
        // Create custom adapter for dialog
        adapter = new MultipleImageDialogAdapter(requireContext(), imageList, viewHolderTypeKey);

        // Set click listener for images
        adapter.setOnImageClickListener(new MultipleImageDialogAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String imagePath, int position) {
                if (imageClickListener != null) {
                    imageClickListener.onImageClick(imagePath, position);
                } else {
                    // Default behavior - open individual image screen
                    Intent intent = new Intent(requireContext(), show_image_Screen.class);

                    // Parse bundle data to get fileName and networkUrl
                    String fileName = null;
                    String networkUrl = null;

                    if (imagePath.contains("|")) {
                        String[] parts = imagePath.split("\\|");
                        if (parts.length >= 2) {
                            fileName = parts[0];
                            networkUrl = parts[1];
                        }
                    } else {
                        fileName = extractFileNameFromUrl(imagePath);
                        networkUrl = imagePath;
                    }

                    intent.putExtra("imageKey", fileName);
                    intent.putExtra("viewHolderTypeKey", viewHolderTypeKey);
                    intent.putExtra("imagePath", networkUrl);
                    intent.putExtra("originalImagePath", imagePath);

                    startActivity(intent);
                }
            }
        });

        // Optimize RecyclerView for smooth scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Smart positioning logic - WhatsApp-like behavior
        int targetPosition = calculateTargetPosition();
        Log.d(TAG, "=== SCROLLING TO POSITION ===");
        Log.d(TAG, "Calculated target position: " + targetPosition);
        Log.d(TAG, "Image list size: " + imageList.size());

        // Scroll to target position with smooth animation
        if (targetPosition >= 0 && targetPosition < imageList.size()) {
            Log.d(TAG, "✅ Scrolling to position: " + targetPosition);
            recyclerView.post(() -> {
                Log.d(TAG, "🔄 Starting smooth scroll to position: " + targetPosition);
                // Smooth scroll to position
                recyclerView.smoothScrollToPosition(targetPosition);
            });
        } else {
            Log.w(TAG, "❌ Invalid target position: " + targetPosition + " (list size: " + imageList.size() + ")");
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            dismissWithAnimation();
        });
    }
    
    /**
     * Initialize the animation state based on current scroll position
     */
    private void initializeAnimationState() {
        if (recyclerView == null || lottieAnimationView == null) return;
        
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager == null) return;
        
        int totalItemCount = layoutManager.getItemCount();
        
        // Hide animation for single item or empty list
        if (totalItemCount <= 1) {
            lottieAnimationView.setVisibility(View.GONE);
            return;
        }
        
        // For multiple items, show animation initially (assuming we start at top)
        // The scroll listener will handle the proper state
        lottieAnimationView.setVisibility(View.VISIBLE);
        
        Log.d(TAG, "Animation initialized - total items: " + totalItemCount);
    }

    /**
     * Calculate target position based on filename or custom behavior
     * - If targetFilename is provided: find and scroll to that specific image
     * - Special case: If selectionCount = 2 and clicking img3, scroll to position 1
     * - If 1-4 images: show the clicked image
     * - If more than 4 images: show from position 0
     */
    private int calculateTargetPosition() {
        int imageCount = imageList.size();
        Log.d(TAG, "=== CALCULATING TARGET POSITION ===");
        Log.d(TAG, "Image count: " + imageCount);
        Log.d(TAG, "Target filename: " + targetFilename);
        Log.d(TAG, "Current position: " + currentPosition);

        // If target filename is provided, find the position of that image
        if (targetFilename != null && !targetFilename.isEmpty()) {
            Log.d(TAG, "🔍 Searching for filename: " + targetFilename);
            int filenamePosition = findImagePositionByFilename(targetFilename);
            if (filenamePosition >= 0) {
                Log.d(TAG, "✅ Found target filename '" + targetFilename + "' at position: " + filenamePosition);

                // Special case: If selectionCount = 2
                if (imageCount == 2) {
                    if (filenamePosition == 0) {
                        // Click img1 → scroll to position 0
                        Log.d(TAG, "🎯 Special case: selectionCount=2, clicking img1, scrolling to position 0");
                        return 0;
                    } else if (filenamePosition >= 2) {
                        // Click img3 or higher → scroll to position 1
                        Log.d(TAG, "🎯 Special case: selectionCount=2, clicking img" + (filenamePosition + 1) + ", scrolling to position 1");
                        return 1;
                    } else {
                        // Click img2 → scroll to position 1 (last position)
                        Log.d(TAG, "🎯 Special case: selectionCount=2, clicking img2, scrolling to position 1");
                        return 1;
                    }
                }

                return filenamePosition;
            } else {
                Log.w(TAG, "❌ Target filename '" + targetFilename + "' not found, using fallback positioning");
            }
        } else {
            Log.d(TAG, "ℹ️ No target filename provided, using fallback positioning");
        }

        // Fallback to original WhatsApp-like behavior
        int fallbackPosition;
        if (imageCount <= 4) {
            // For 1-4 images, show the clicked image
            fallbackPosition = Math.max(0, Math.min(currentPosition, imageCount - 1));
            Log.d(TAG, "📱 Using WhatsApp-like positioning (1-4 images): " + fallbackPosition);
        } else {
            // For more than 4 images, always start from position 0
            fallbackPosition = 0;
            Log.d(TAG, "📱 Using WhatsApp-like positioning (5+ images): " + fallbackPosition);
        }

        Log.d(TAG, "🎯 Final target position: " + fallbackPosition);
        return fallbackPosition;
    }

    /**
     * Find the position of an image by its filename
     *
     * @param filename The filename to search for
     * @return Position of the image, or -1 if not found
     */
    private int findImagePositionByFilename(String filename) {
        Log.d(TAG, "=== FINDING IMAGE BY FILENAME ===");
        Log.d(TAG, "Searching for filename: " + filename);
        Log.d(TAG, "Image list size: " + (imageList != null ? imageList.size() : 0));

        if (imageList == null || filename == null) {
            Log.w(TAG, "❌ Image list or filename is null");
            return -1;
        }

        for (int i = 0; i < imageList.size(); i++) {
            String imageData = imageList.get(i);
            Log.d(TAG, "Checking image " + i + ": " + imageData);

            // Parse bundle data if it contains fileName|URL format
            if (imageData.contains("|")) {
                String[] parts = imageData.split("\\|");
                if (parts.length >= 2) {
                    String imageFileName = parts[0];
                    Log.d(TAG, "  → Bundle format - filename: " + imageFileName);

                    // Try exact match first
                    if (filename.equals(imageFileName)) {
                        Log.d(TAG, "✅ EXACT MATCH FOUND! Filename '" + filename + "' at position: " + i);
                        return i;
                    }

                    // Try partial match (filename is contained in imageFileName)
                    if (imageFileName.contains(filename)) {
                        Log.d(TAG, "✅ PARTIAL MATCH FOUND! Filename '" + filename + "' contained in '" + imageFileName + "' at position: " + i);
                        return i;
                    }
                }
            } else {
                // For single URL format, extract filename and compare
                String extractedFilename = extractFileNameFromUrl(imageData);
                Log.d(TAG, "  → Single URL format - extracted filename: " + extractedFilename);

                // Try exact match first
                if (filename.equals(extractedFilename)) {
                    Log.d(TAG, "✅ EXACT MATCH FOUND! Filename '" + filename + "' at position: " + i);
                    return i;
                }

                // Try partial match (filename is contained in extractedFilename)
                if (extractedFilename.contains(filename)) {
                    Log.d(TAG, "✅ PARTIAL MATCH FOUND! Filename '" + filename + "' contained in '" + extractedFilename + "' at position: " + i);
                    return i;
                }
            }
        }

        Log.w(TAG, "❌ Filename '" + filename + "' not found in any image");
        return -1; // Not found
    }

    /**
     * Dismiss dialog with smooth bottom-to-top animation
     */
    private void dismissWithAnimation() {
        if (getView() != null) {
            // Add bottom-to-top exit animation
            getView().animate()
                    .alpha(0f)
                    .translationY(getView().getHeight())
                    .setDuration(250)
                    .setInterpolator(new android.view.animation.AccelerateInterpolator())
                    .withEndAction(() -> {
                        dismiss();
                    })
                    .start();
        } else {
            dismiss();
        }
    }

    // Helper method to extract filename from URL (copied from adapter)
    private String extractFileNameFromUrl(String url) {
        try {
            if (url.startsWith("/") || url.startsWith("file://")) {
                return new java.io.File(url).getName();
            }

            android.net.Uri uri = android.net.Uri.parse(url);
            String path = uri.getPath();
            if (path != null) {
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                if (fileName.startsWith("1_")) {
                    fileName = fileName.substring(2);
                }
                return fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Ensure full screen behavior with transparent status bar after dialog is shown
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                // Force full screen flags with transparent status bar
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                );

                // Set layout parameters
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                // Set window attributes
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
                window.setAttributes(params);

                // Make status bar transparent and allow content to extend behind it
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Force full screen behavior with transparent status bar in onResume as well
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                // Force full screen flags with transparent status bar
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                );

                // Set layout parameters
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                // Set window attributes
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
                window.setAttributes(params);

                // Make status bar transparent and allow content to extend behind it
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up resources
        if (adapter != null) {
            adapter = null;
        }
    }
}
