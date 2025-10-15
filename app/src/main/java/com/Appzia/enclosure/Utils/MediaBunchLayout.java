package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.Appzia.enclosure.R;
import java.util.ArrayList;
import java.util.List;

public class MediaBunchLayout extends FrameLayout {

    private static final int MAX_VISIBLE_IMAGES = 4;
    private static final int GRID_COLUMNS = 2;
    private static final int GRID_ROWS = 2;

    private GridLayout gridLayout;
    private LinearLayout singleImageLayout;
    private TextView moreCountText;
    private OnMediaItemClickListener onMediaItemClickListener;
    private OnImageLoadListener onImageLoadListener;
    private List<MediaItem> mediaItems;

    public interface OnMediaItemClickListener {
        void onMediaItemClick(int position, MediaItem item);
        void onMediaItemLongClick(int position, MediaItem item);
    }

    public interface OnImageLoadListener {
        void loadImage(ImageView imageView, MediaItem item, int position);
    }

    public static class MediaItem {
        public String imageUrl;
        public String videoUrl;
        public String caption;
        public long sentTime;
        public boolean isVideo;
        public String fileName;
        public String document;
        public String imageWidth;
        public String imageHeight;

        public MediaItem(String imageUrl, String videoUrl, String caption, long sentTime, boolean isVideo, String fileName, String document) {
            this.imageUrl = imageUrl;
            this.videoUrl = videoUrl;
            this.caption = caption;
            this.sentTime = sentTime;
            this.isVideo = isVideo;
            this.fileName = fileName;
            this.document = document;
            this.imageWidth = "0";
            this.imageHeight = "0";
        }

        public MediaItem(String imageUrl, String videoUrl, String caption, long sentTime, boolean isVideo, String fileName, String document, String imageWidth, String imageHeight) {
            this.imageUrl = imageUrl;
            this.videoUrl = videoUrl;
            this.caption = caption;
            this.sentTime = sentTime;
            this.isVideo = isVideo;
            this.fileName = fileName;
            this.document = document;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
        }
    }

    public MediaBunchLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MediaBunchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaBunchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);

        // Ensure the MediaBunchLayout doesn't interfere with parent's rounded corners
        setClipToPadding(false);
        setClipChildren(false);

        // Make sure this layout respects the parent's rounded corners
        setClipToOutline(true);

        // Ensure touch events are properly handled
        setClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    public void setMediaItems(List<MediaItem> items) {
        // Store the media items for later access
        this.mediaItems = items;

        // Validate input
        if (items == null || items.isEmpty()) {
            removeAllViews();
            return;
        }

        // Always create fresh views to ensure bunching works properly
        // Disabled view recycling completely to prevent bunching issues

        // Only clear views when absolutely necessary to prevent flickering
        removeAllViews();

        // Set maximum width and height to 300dp for the main container
        int maxWidthPx = (int) (300 * getContext().getResources().getDisplayMetrics().density);
        int maxHeightPx = (int) (300 * getContext().getResources().getDisplayMetrics().density);

        ViewGroup.LayoutParams containerParams = getLayoutParams();
        if (containerParams == null) {
            containerParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            containerParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            containerParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        setLayoutParams(containerParams);

        // Group items by sentTime
        List<List<MediaItem>> groupedItems = groupBySentTime(items);
        android.util.Log.d("MediaBunchLayout", "Grouped items into " + groupedItems.size() + " groups");

        // Debug: Log each group
        for (int i = 0; i < groupedItems.size(); i++) {
            android.util.Log.d("MediaBunchLayout", "Group " + i + " has " + groupedItems.get(i).size() + " items");
        }

        // Create layout for each group
        for (int i = 0; i < groupedItems.size(); i++) {
            List<MediaItem> group = groupedItems.get(i);
            android.util.Log.d("MediaBunchLayout", "Processing group " + i + " with " + group.size() + " items");

            if (group.size() == 1) {
                android.util.Log.d("MediaBunchLayout", "Adding single item layout for group " + i);
                addSingleItemLayout(group.get(0));
            } else {
                android.util.Log.d("MediaBunchLayout", "Adding linear grid layout for group " + i + " with " + group.size() + " items");
                addLinearGridLayout(group);
            }
        }

        android.util.Log.d("MediaBunchLayout", "Finished creating layouts - Total child count: " + getChildCount());

        // Force immediate layout for immediate visibility
        requestLayout();
        invalidate();
    }

    private boolean canReuseExistingViews(List<MediaItem> newItems) {
        // More intelligent recycling logic
        if (mediaItems == null || newItems == null) {
            return false;
        }

        // If we have no existing views, don't reuse
        if (getChildCount() == 0) {
            return false;
        }

        // If different number of items, don't reuse
        if (mediaItems.size() != newItems.size()) {
            return false;
        }

        // Check if the items are the same (by URL comparison)
        for (int i = 0; i < mediaItems.size(); i++) {
            if (!mediaItems.get(i).document.equals(newItems.get(i).document)) {
                return false;
            }
        }

        return true;
    }

    private void updateExistingViews(List<MediaItem> newItems) {
        // Update existing views instead of recreating them
        // This prevents blank screens during scrolling

        // Update the stored items
        this.mediaItems = newItems;

        // Update image sources without recreating views
        updateImageSources(newItems);

        // Ensure visibility is maintained
        setVisibility(View.VISIBLE);

        // Force a layout update without recreating views
        post(() -> {
            requestLayout();
            invalidate();
        });
    }

    private void updateImageSources(List<MediaItem> newItems) {
        // Update image sources in existing views
        int childIndex = 0;
        for (int i = 0; i < newItems.size(); i++) {
            MediaItem item = newItems.get(i);

            // Find the corresponding view and update its image
            if (childIndex < getChildCount()) {
                View child = getChildAt(childIndex);
                if (child instanceof LinearLayout) {
                    LinearLayout layout = (LinearLayout) child;
                    for (int j = 0; j < layout.getChildCount(); j++) {
                        View view = layout.getChildAt(j);
                        if (view instanceof ImageView) {
                            ImageView imageView = (ImageView) view;
                            // Update the image source
                            if (onImageLoadListener != null) {
                                onImageLoadListener.loadImage(imageView, item, i);
                            }
                        }
                    }
                }
                childIndex++;
            }
        }
    }

    private List<List<MediaItem>> groupBySentTime(List<MediaItem> items) {
        List<List<MediaItem>> grouped = new ArrayList<>();
        List<MediaItem> currentGroup = new ArrayList<>();
        long currentSentTime = -1;
        final long TIME_WINDOW_MS = 10000; // 10 seconds

        for (MediaItem item : items) {
            if (currentSentTime == -1 || Math.abs(item.sentTime - currentSentTime) <= TIME_WINDOW_MS) {
                currentGroup.add(item);
                // Update currentSentTime to the earliest time in the group
                if (currentSentTime == -1 || item.sentTime < currentSentTime) {
                    currentSentTime = item.sentTime;
                }
            } else {
                if (!currentGroup.isEmpty()) {
                    grouped.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                }
                currentGroup.add(item);
                currentSentTime = item.sentTime;
            }
        }

        if (!currentGroup.isEmpty()) {
            grouped.add(currentGroup);
        }

        return grouped;
    }

    private void addSingleItemLayout(MediaItem item) {
        android.util.Log.d("MediaBunchLayout", "addSingleItemLayout called for: " + item.document);

        LinearLayout singleLayout = new LinearLayout(getContext());
        singleLayout.setOrientation(LinearLayout.VERTICAL);

        // Set maximum width and height to 300dp and 250dp
        final float MAX_WIDTH_DP = 300f;
        final float MAX_HEIGHT_DP = 250f;
        int maxWidthPx = (int) (MAX_WIDTH_DP * getContext().getResources().getDisplayMetrics().density);
        int maxHeightPx = (int) (MAX_HEIGHT_DP * getContext().getResources().getDisplayMetrics().density);

        singleLayout.setLayoutParams(new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // Wrap content width to remove empty spaces
            ViewGroup.LayoutParams.WRAP_CONTENT // Let height wrap content
        ));

        // No padding - let image fit perfectly within the rounded container
        singleLayout.setPadding(0, 0, 0, 0);

        // Make sure the single layout respects the parent's rounded corners
        singleLayout.setClipToOutline(true);
        singleLayout.setClipChildren(true);

        ImageView imageView = createMediaImageView(item);

        // Use dynamic dimensions based on database values, respecting max constraints
        int finalWidthPx = maxWidthPx;  // Default to max width
        int finalHeightPx = maxHeightPx; // Default to max height

        // Parse database values for dynamic sizing
        if (item.imageWidth != null && item.imageHeight != null) {
            try {
                float dbWidth = Float.parseFloat(item.imageWidth);
                float dbHeight = Float.parseFloat(item.imageHeight);

                // Calculate aspect ratio
                float aspectRatio = dbWidth / dbHeight;

                // Calculate dimensions based on aspect ratio and max constraints
                if (aspectRatio > 1) {
                    // Landscape: width is the limiting factor
                    finalWidthPx = maxWidthPx;
                    finalHeightPx = (int) (maxWidthPx / aspectRatio);

                    // Ensure height doesn't exceed max height
                    if (finalHeightPx > maxHeightPx) {
                        finalHeightPx = maxHeightPx;
                        finalWidthPx = (int) (maxHeightPx * aspectRatio);
                    }
                } else {
                    // Portrait or square: height is the limiting factor
                    finalHeightPx = maxHeightPx;
                    finalWidthPx = (int) (maxHeightPx * aspectRatio);

                    // Ensure width doesn't exceed max width
                    if (finalWidthPx > maxWidthPx) {
                        finalWidthPx = maxWidthPx;
                        finalHeightPx = (int) (maxWidthPx / aspectRatio);
                    }
                }

            } catch (NumberFormatException e) {
                // Use default dimensions if parsing fails
            }
        }

        imageView.setMinimumWidth(finalWidthPx);
        imageView.setMinimumHeight(finalHeightPx);
        imageView.setMaxWidth(finalWidthPx);
        imageView.setMaxHeight(finalHeightPx);
        imageView.setVisibility(View.VISIBLE);
        imageView.setAlpha(1.0f);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Ensure the single image respects the parent's rounded corners with 20dp radius
        imageView.setClipToOutline(true);
        imageView.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20 * getContext().getResources().getDisplayMetrics().density);
            }
        });

        // Set layout params for the image view with dynamic dimensions
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(finalWidthPx, finalHeightPx);
        imageParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(imageParams);

        singleLayout.addView(imageView);

        if (item.caption != null && !item.caption.isEmpty()) {
            TextView captionText = createCaptionText(item.caption);
            singleLayout.addView(captionText);
        }

        addView(singleLayout);

        android.util.Log.d("MediaBunchLayout", "Single item layout added - Child count: " + getChildCount() +
                          ", Image size: " + finalWidthPx + "x" + finalHeightPx);
    }

    private void addBunchLayout(List<MediaItem> items) {
        android.util.Log.d("MediaBunchLayout", "addBunchLayout called with " + items.size() + " items");

        int captionCount = getCaptionCount(items);
        android.util.Log.d("MediaBunchLayout", "Caption count: " + captionCount);

        // Always use grid layout for multiple images, regardless of caption count
        if (items.size() == 1) {
            android.util.Log.d("MediaBunchLayout", "Single item, using single layout");
            addSingleItemLayout(items.get(0));
        } else {
            // Use grid layout for multiple images
            int visibleCount = Math.min(items.size(), MAX_VISIBLE_IMAGES);
            android.util.Log.d("MediaBunchLayout", "Adding grid layout for " + visibleCount + " items");
            addGridLayout(items, visibleCount);

            if (items.size() > MAX_VISIBLE_IMAGES) {
                android.util.Log.d("MediaBunchLayout", "Adding +" + (items.size() - MAX_VISIBLE_IMAGES) + " indicator");
                addMoreIndicator(items.size() - MAX_VISIBLE_IMAGES);
            }
        }
    }

    private int getCaptionCount(List<MediaItem> items) {
        int count = 0;
        for (MediaItem item : items) {
            if (item.caption != null && !item.caption.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private String getFirstCaption(List<MediaItem> items) {
        for (MediaItem item : items) {
            if (item.caption != null && !item.caption.trim().isEmpty()) {
                return item.caption;
            }
        }
        return null;
    }

    private void addGridLayout(List<MediaItem> items, int visibleCount) {
        android.util.Log.d("MediaBunchLayout", "addGridLayout called with " + visibleCount + " visible items");

        GridLayout grid = new GridLayout(getContext());
        grid.setColumnCount(GRID_COLUMNS);
        grid.setRowCount(GRID_ROWS);
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        // Optimize for smooth scrolling
        grid.setWillNotDraw(false); // Allow drawing for images
        grid.setDrawingCacheEnabled(false); // Disable drawing cache for better memory usage

        // Set maximum width and height to 300dp and 250dp
        final float MAX_WIDTH_DP = 300f;
        final float MAX_HEIGHT_DP = 250f;
        int maxWidthPx = (int) (MAX_WIDTH_DP * getContext().getResources().getDisplayMetrics().density);
        int maxHeightPx = (int) (MAX_HEIGHT_DP * getContext().getResources().getDisplayMetrics().density);

        LayoutParams params = new LayoutParams(
            maxWidthPx, // Set maximum width
            ViewGroup.LayoutParams.WRAP_CONTENT // Let height wrap content
        );
        params.setMargins(0, 0, 0, 0); // No margins to remove empty spaces
        grid.setLayoutParams(params);

        // Ensure the grid is visible and properly sized
        grid.setVisibility(View.VISIBLE);

        // No padding - let images fit perfectly within the rounded container
        grid.setPadding(0, 0, 0, 0);

        // Make sure the grid respects the parent's rounded corners
        grid.setClipToOutline(true);
        grid.setClipChildren(true);

        // Configure GridLayout for proper spacing
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        grid.setColumnCount(GRID_COLUMNS);
        grid.setRowCount(GRID_ROWS);

        // Force GridLayout to use proper layout
        grid.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        // Calculate the size for each image in the grid (within max dimensions)
        // Account for 1 pixel gap between images
        int imageSize = (maxWidthPx - 3) / 2; // 3 pixels total gap for 2 images

        android.util.Log.d("MediaBunchLayout", "Max width: " + maxWidthPx + ", Max height: " + maxHeightPx + ", Image size: " + imageSize);
        android.util.Log.d("MediaBunchLayout", "Visible count: " + visibleCount + ", Grid columns: " + GRID_COLUMNS + ", Grid rows: " + GRID_ROWS);

        for (int i = 0; i < visibleCount; i++) {
            MediaItem item = items.get(i);
            ImageView imageView = createMediaImageView(item);

            // Set explicit size for the image view
            imageView.setMinimumWidth(imageSize);
            imageView.setMinimumHeight(imageSize);
            imageView.setMaxWidth(imageSize);
            imageView.setMaxHeight(imageSize);

            // Ensure the image view is visible and properly configured
            imageView.setVisibility(View.VISIBLE);
            imageView.setAlpha(1.0f);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
            gridParams.width = imageSize;
            gridParams.height = imageSize;

            // Set proper column and row specs for GridLayout
            int column = i % GRID_COLUMNS;
            int row = i / GRID_COLUMNS;
            gridParams.columnSpec = GridLayout.spec(column, 1f);
            gridParams.rowSpec = GridLayout.spec(row, 1f);

            android.util.Log.d("MediaBunchLayout", "Image " + i + " - Column: " + column + ", Row: " + row);

            // Add minimal gap between images
            int gap = 1; // 1 pixel gap
            gridParams.setMargins(gap, gap, gap, gap);

            imageView.setLayoutParams(gridParams);
            grid.addView(imageView);

            // Debug: Log image view details
            android.util.Log.d("MediaBunchLayout", "Added image " + i + " to grid - Visibility: " + imageView.getVisibility() + ", Alpha: " + imageView.getAlpha() + ", Size: " + imageView.getWidth() + "x" + imageView.getHeight());

            // Ensure the image respects the parent's rounded corners
            imageView.setClipToOutline(true);

            // Optimize for smooth scrolling
            imageView.setDrawingCacheEnabled(false);
            imageView.setWillNotCacheDrawing(false);

            android.util.Log.d("MediaBunchLayout", "Added image " + i + " to grid at position (" + (i % GRID_COLUMNS) + ", " + (i / GRID_COLUMNS) + ") with 1px gap, size: " + imageSize + "x" + imageSize);
        }

        addView(grid);
        android.util.Log.d("MediaBunchLayout", "Grid layout added with " + grid.getChildCount() + " children");

        // Force layout to ensure proper display
        grid.requestLayout();
        grid.invalidate();
    }

    private void addMoreIndicator(int remainingCount) {
        FrameLayout indicatorLayout = new FrameLayout(getContext());
        indicatorLayout.setLayoutParams(new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        ImageView indicatorImage = new ImageView(getContext());
        indicatorImage.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
        indicatorImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LayoutParams imageParams = new LayoutParams(200, 200);
        imageParams.gravity = Gravity.CENTER;
        indicatorImage.setLayoutParams(imageParams);

        moreCountText = new TextView(getContext());
        moreCountText.setText("+" + remainingCount);
        moreCountText.setTextColor(Color.WHITE);
        moreCountText.setTextSize(16);
        moreCountText.setGravity(Gravity.CENTER);

        LayoutParams textParams = new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.CENTER;
        moreCountText.setLayoutParams(textParams);

        indicatorLayout.addView(indicatorImage);
        indicatorLayout.addView(moreCountText);

        addView(indicatorLayout);
    }

    private View createPlusIndicator(int remainingCount) {
        // Create a container for the +N indicator
        LinearLayout indicatorContainer = new LinearLayout(getContext());
        indicatorContainer.setOrientation(LinearLayout.VERTICAL);
        indicatorContainer.setGravity(android.view.Gravity.CENTER);
        indicatorContainer.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));

        // Create the +N text
        TextView plusText = new TextView(getContext());
        plusText.setText("+ " + remainingCount);
        plusText.setTextColor(android.graphics.Color.WHITE);
        plusText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
        plusText.setGravity(android.view.Gravity.CENTER);
        plusText.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

        // Add rounded corners to the indicator
        indicatorContainer.setClipToOutline(true);
        indicatorContainer.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20 * getContext().getResources().getDisplayMetrics().density);
            }
        });

        indicatorContainer.addView(plusText);
        return indicatorContainer;
    }

    private View createPlusIndicatorOverlay(int remainingCount) {
        android.util.Log.d("MediaBunchLayout", "createPlusIndicatorOverlay called with remainingCount: " + remainingCount);
        // Create a semi-transparent overlay for the +N indicator
        LinearLayout overlayContainer = new LinearLayout(getContext());
        overlayContainer.setOrientation(LinearLayout.VERTICAL);
        overlayContainer.setGravity(android.view.Gravity.CENTER);
        overlayContainer.setBackgroundColor(android.graphics.Color.argb(180, 0, 0, 0)); // Semi-transparent black

        // Create the +N text with increased size and custom font
        TextView plusText = new TextView(getContext());
        plusText.setText("+ " + remainingCount);
        plusText.setTextColor(android.graphics.Color.WHITE);
        plusText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 20); // Increased from 16 to 20
        plusText.setGravity(android.view.Gravity.CENTER);

        // Apply custom font family (normal weight)
        try {
            android.graphics.Typeface customFont = android.graphics.Typeface.createFromAsset(getContext().getAssets(), "fonts/san_pro.ttf");
            plusText.setTypeface(customFont);
        } catch (Exception e) {
            android.util.Log.w("MediaBunchLayout", "Could not load san_pro font, using default: " + e.getMessage());
            plusText.setTypeface(android.graphics.Typeface.DEFAULT);
        }

        // Add rounded corners to the overlay
        overlayContainer.setClipToOutline(true);
        overlayContainer.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20 * getContext().getResources().getDisplayMetrics().density);
            }
        });

        overlayContainer.addView(plusText);
        android.util.Log.d("MediaBunchLayout", "createPlusIndicatorOverlay completed - returning overlay container");
        return overlayContainer;
    }


    private ImageView createMediaImageView(MediaItem item) {
        ImageView imageView = new ImageView(getContext());

        // Set transparent background for all images for cleaner look
        imageView.setBackgroundColor(Color.TRANSPARENT);

        // Apply rounded corners to individual images in grid
        imageView.setClipToOutline(true);
        imageView.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                // Apply rounded corners to individual images (same 20dp radius as outer container)
                int cornerRadius = (int) (20 * getContext().getResources().getDisplayMetrics().density);
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Set minimum size to ensure visibility (will be overridden by grid params)
        imageView.setMinimumWidth(100);
        imageView.setMinimumHeight(100);

        // Ensure the ImageView can receive touch events properly
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setEnabled(true);

        // Prevent parent from intercepting touch events
        imageView.setDuplicateParentStateEnabled(false);

        // Add touch listener to handle touch events more directly
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    android.util.Log.d("MediaBunchLayout", "Touch DOWN detected - single image");
                    return false; // Let the click listener handle it
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    android.util.Log.d("MediaBunchLayout", "Touch UP detected - single image");
                    // Immediately trigger click if listener is available
                    if (onMediaItemClickListener != null) {
                        android.util.Log.d("MediaBunchLayout", "Directly triggering click from touch UP - single image");
                        onMediaItemClickListener.onMediaItemClick(0, item);
                        return true; // Consume the event
                    }
                    return false; // Let the click listener handle it
                }
                return false;
            }
        });

        // Set click and long-click listeners
        imageView.setOnClickListener(v -> {
            android.util.Log.d("MediaBunchLayout", "*** SINGLE IMAGE CLICKED *** - document: " + item.document);
            if (onMediaItemClickListener != null) {
                try {
                    onMediaItemClickListener.onMediaItemClick(0, item);
                    android.util.Log.d("MediaBunchLayout", "Single image click listener called successfully");
                } catch (Exception e) {
                    android.util.Log.e("MediaBunchLayout", "Error in single image click listener: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                android.util.Log.w("MediaBunchLayout", "onMediaItemClickListener is NULL for single image");
            }
        });

        imageView.setOnLongClickListener(v -> {
            android.util.Log.d("MediaBunchLayout", "*** SINGLE IMAGE LONG CLICKED *** - document: " + item.document);
            if (onMediaItemClickListener != null) {
                try {
                    onMediaItemClickListener.onMediaItemLongClick(0, item);
                    android.util.Log.d("MediaBunchLayout", "Single image long click listener called successfully");
                    return true; // Consume the long click event
                } catch (Exception e) {
                    android.util.Log.e("MediaBunchLayout", "Error in single image long click listener: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            } else {
                android.util.Log.w("MediaBunchLayout", "onMediaItemClickListener is NULL for single image long click");
                return false;
            }
        });

        // Load the image using the callback
        if (onImageLoadListener != null) {
            onImageLoadListener.loadImage(imageView, item, 0);
        } else {
            // Fallback: Set transparent placeholder background
            imageView.setBackgroundColor(Color.TRANSPARENT);
        }

        return imageView;
    }

    private TextView createCaptionText(String caption) {
        TextView captionText = new TextView(getContext());
        captionText.setText(caption);
        captionText.setTextColor(Color.BLACK);
        captionText.setTextSize(14);
        captionText.setPadding(8, 8, 8, 8);

        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 0);
        captionText.setLayoutParams(params);

        return captionText;
    }

    public void setOnMediaItemClickListener(OnMediaItemClickListener listener) {
        this.onMediaItemClickListener = listener;
    }

    public void setOnImageLoadListener(OnImageLoadListener listener) {
        this.onImageLoadListener = listener;
    }

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    // Method to load images using existing image loading logic
    public void loadImageIntoBunch(ImageView imageView, MediaItem item, Object requestOptions, ViewGroup parentLayout, int position, boolean loadHighQuality, Object model, Object videoIcon) {
        // This method will be called from the chat adapter with proper image loading
        // The actual image loading is handled in the chat adapter using existing logic
        imageView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
    }

    private void addLinearGridLayout(List<MediaItem> items) {
        // Use LinearLayout for better reliability than GridLayout
        android.util.Log.d("MediaBunchLayout", "addLinearGridLayout called with " + items.size() + " items");

        // Debug: Log each item
        for (int i = 0; i < items.size(); i++) {
            android.util.Log.d("MediaBunchLayout", "Item " + i + ": " + items.get(i).document);
        }
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);

        // Ensure container doesn't interfere with touch events
        container.setClickable(false);
        container.setFocusable(false);
        container.setFocusableInTouchMode(false);

        // Set maximum width and height to 300dp and 250dp
        final float MAX_WIDTH_DP = 300f;
        final float MAX_HEIGHT_DP = 250f;
        int maxWidthPx = (int) (MAX_WIDTH_DP * getContext().getResources().getDisplayMetrics().density);
        int maxHeightPx = (int) (MAX_HEIGHT_DP * getContext().getResources().getDisplayMetrics().density);

        // Calculate visible count first
        int visibleCount = Math.min(items.size(), MAX_VISIBLE_IMAGES);

        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // Wrap content width to remove empty spaces
            ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set maximum height to 250dp for 2, 3, and 4 images
        if (visibleCount == 2 || visibleCount == 3 || visibleCount == 4) {
            params.height = maxHeightPx; // 250dp
        }

        container.setLayoutParams(params);
        container.setVisibility(View.VISIBLE);
        container.setPadding(0, 0, 0, 0);
        container.setClipToOutline(true);
        container.setClipChildren(true);

        // Optimize for smooth scrolling
        container.setDrawingCacheEnabled(false);
        container.setWillNotDraw(false);

        // Calculate image size for consistent square dimensions
        int imageSize = (maxWidthPx - 2) / 2; // Account for 1px gap between images

        // For 2, 3, and 4 images, use same width as 3 images
        if (visibleCount == 2) {
            imageSize = (maxHeightPx - 4) / 2; // Same width as 3 images, but will be stretched to full height
        } else if (visibleCount == 3) {
            imageSize = (maxHeightPx - 4) / 2; // For 3 images, account for row gap
        } else if (visibleCount == 4) {
            imageSize = (maxHeightPx - 4) / 2; // For 4 images, same as 3 images
        }
        int gap = 1; // 1 pixel gap

        // Create rows based on number of images
        android.util.Log.d("MediaBunchLayout", "Creating layout for " + visibleCount + " visible images");

        if (visibleCount == 3) {
            // Special layout for 3 images: Left side with 2 squares, Right side with 1 rectangle
            LinearLayout mainContainer = new LinearLayout(getContext());
            mainContainer.setOrientation(LinearLayout.HORIZONTAL);
            mainContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Ensure container doesn't interfere with touch events
            mainContainer.setClickable(false);
            mainContainer.setFocusable(false);
            mainContainer.setFocusableInTouchMode(false);

            // Optimize for smooth scrolling
            mainContainer.setDrawingCacheEnabled(false);
            mainContainer.setWillNotDraw(false);

            // Left side container for 2 square images
            LinearLayout leftContainer = new LinearLayout(getContext());
            leftContainer.setOrientation(LinearLayout.VERTICAL);
            leftContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Ensure container doesn't interfere with touch events
            leftContainer.setClickable(false);
            leftContainer.setFocusable(false);
            leftContainer.setFocusableInTouchMode(false);

            // Top-left square image
            ImageView topLeftImage = createOptimizedImageView(items.get(0), imageSize, imageSize, 0);
            LinearLayout.LayoutParams topLeftParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            topLeftParams.setMargins(0, 0, 0, gap);
            topLeftImage.setLayoutParams(topLeftParams);
            leftContainer.addView(topLeftImage);
            android.util.Log.d("MediaBunchLayout", "Added top-left image for 3-image layout");

            // Bottom-left square image
            ImageView bottomLeftImage = createOptimizedImageView(items.get(1), imageSize, imageSize, 1);
            LinearLayout.LayoutParams bottomLeftParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            bottomLeftParams.setMargins(0, 0, 0, 0);
            bottomLeftImage.setLayoutParams(bottomLeftParams);
            leftContainer.addView(bottomLeftImage);
            android.util.Log.d("MediaBunchLayout", "Added bottom-left image for 3-image layout");

            mainContainer.addView(leftContainer);

            // Right side rectangle image (full height)
            int rightImageHeight = maxHeightPx - 2; // Full height minus gap
            ImageView rightImage = createOptimizedImageView(items.get(2), imageSize, rightImageHeight, 2);
            LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(imageSize, rightImageHeight);
            rightParams.setMargins(gap, 0, 0, 0); // Left margin for gap
            rightImage.setLayoutParams(rightParams);
            mainContainer.addView(rightImage);
            android.util.Log.d("MediaBunchLayout", "Added right image for 3-image layout");

            container.addView(mainContainer);

        } else {
            // Regular 2x2 grid for other counts
            for (int i = 0; i < visibleCount; i += 2) {
                // Create horizontal row
                LinearLayout row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Ensure container doesn't interfere with touch events
                row.setClickable(false);
                row.setFocusable(false);
                row.setFocusableInTouchMode(false);
                row.setGravity(android.view.Gravity.CENTER);

                // Optimize for smooth scrolling
                row.setDrawingCacheEnabled(false);
                row.setWillNotDraw(false);

                // Add first image in row
                if (i < visibleCount) {
                    // For 2 images, use full height but same width as 3 images
                    int imageWidth = imageSize;
                    int imageHeight = (visibleCount == 2) ? (maxHeightPx - 2) : imageSize; // Full height for 2 images

                    ImageView imageView1 = createOptimizedImageView(items.get(i), imageWidth, imageHeight, i);
                    LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                    imageParams1.setMargins(0, 0, gap, 0); // Right margin for gap
                    imageView1.setLayoutParams(imageParams1);
                    row.addView(imageView1);
                    android.util.Log.d("MediaBunchLayout", "Added first image in row " + (i/2) + " for " + visibleCount + " images");
                }

                // Add second image in row (if exists)
                if (i + 1 < visibleCount) {
                    // Check if this is the 4th position and we have more than 4 items
                    if (i + 1 == 3 && items.size() > 4) {
                        android.util.Log.d("MediaBunchLayout", "Creating 5+ images layout with " + items.size() + " total items");
                        // Create a FrameLayout container for the 4th image with overlay
                        FrameLayout imageContainer = new FrameLayout(getContext());

                        // Optimize for smooth scrolling
                        imageContainer.setDrawingCacheEnabled(false);
                        imageContainer.setWillNotDraw(false);

                        // Create the 4th image with dimmed effect
                        int imageWidth = imageSize;
                        int imageHeight = imageSize;
                        ImageView imageView4 = createOptimizedImageView(items.get(3), imageWidth, imageHeight, 3);
                        imageView4.setAlpha(0.5f); // Dim the 4th image

                        FrameLayout.LayoutParams imageParams4 = new FrameLayout.LayoutParams(imageWidth, imageHeight);
                        imageView4.setLayoutParams(imageParams4);
                        imageContainer.addView(imageView4);

                        // Add +N indicator overlay on top of the image
                        View plusIndicator = createPlusIndicatorOverlay(items.size() - 3);
                        plusIndicator.setVisibility(View.VISIBLE);
                        android.util.Log.d("MediaBunchLayout", "Created +N indicator for " + (items.size() - 3) + " remaining items");

                        FrameLayout.LayoutParams plusParams = new FrameLayout.LayoutParams(imageWidth, imageHeight);
                        plusIndicator.setLayoutParams(plusParams);
                        imageContainer.addView(plusIndicator);
                        android.util.Log.d("MediaBunchLayout", "Added +N indicator to FrameLayout container");

                        // Add the container to the row
                        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                        containerParams.setMargins(0, 0, 0, 0); // No margin for last item in row
                        imageContainer.setLayoutParams(containerParams);
                        row.addView(imageContainer);
                    } else {
                        // For 2 images, use full height but same width as 3 images
                        int imageWidth = imageSize;
                        int imageHeight = (visibleCount == 2) ? (maxHeightPx - 2) : imageSize; // Full height for 2 images

                        ImageView imageView2 = createOptimizedImageView(items.get(i + 1), imageWidth, imageHeight, i + 1);
                        LinearLayout.LayoutParams imageParams2 = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                        imageParams2.setMargins(0, 0, 0, 0); // No margin for last image in row
                        imageView2.setLayoutParams(imageParams2);
                        row.addView(imageView2);
                    }
                }

                container.addView(row);

                // Add gap between rows (except for last row)
                if (i + 2 < visibleCount) {
                    View spacer = new View(getContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        gap
                    ));
                    container.addView(spacer);
                }
            }
        }

        // Add caption at the bottom if any item has a caption
        String caption = getFirstCaption(items);
        if (caption != null && !caption.trim().isEmpty()) {
            TextView captionText = createCaptionText(caption);
            container.addView(captionText);
        }

        addView(container);
        android.util.Log.d("MediaBunchLayout", "addLinearGridLayout completed - Container added, Total child count: " + getChildCount());

        // Force visibility and layout to ensure bunching is displayed
        setVisibility(View.VISIBLE);
        post(() -> {
            requestLayout();
            invalidate();
        });
    }

    private ImageView createOptimizedImageView(MediaItem item, int width, int height, int position) {
        ImageView imageView = new ImageView(getContext());

        // Set transparent background for clean appearance
        imageView.setBackgroundColor(Color.TRANSPARENT);

        // Optimize for smooth scrolling - prevent blank screens
        imageView.setDrawingCacheEnabled(false);
        imageView.setWillNotCacheDrawing(false);
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Set dimensions
        imageView.setMinimumWidth(width);
        imageView.setMinimumHeight(height);
        imageView.setMaxWidth(width);
        imageView.setMaxHeight(height);
        imageView.setVisibility(View.VISIBLE);
        imageView.setAlpha(1.0f);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Performance optimizations to prevent blank screens
        imageView.setHasTransientState(false);
        imageView.setWillNotDraw(false);

        // No placeholder image - clean transparent appearance

        // Load the actual image using the callback
        if (onImageLoadListener != null) {
            onImageLoadListener.loadImage(imageView, item, position);
        }

        // Ensure the image respects the parent's rounded corners with 20dp radius
        imageView.setClipToOutline(true);
        imageView.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20 * getContext().getResources().getDisplayMetrics().density);
            }
        });

        // Ensure the ImageView can receive touch events properly
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setEnabled(true);

        // Prevent parent from intercepting touch events
        imageView.setDuplicateParentStateEnabled(false);

        // Simplified touch handling for better performance
        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && onMediaItemClickListener != null) {
                onMediaItemClickListener.onMediaItemClick(position, item);
                return true;
            }
            return false;
        });

        // Set click and long-click listeners if available
        if (onMediaItemClickListener != null) {
            imageView.setOnClickListener(v -> onMediaItemClickListener.onMediaItemClick(position, item));
            imageView.setOnLongClickListener(v -> {
                onMediaItemClickListener.onMediaItemLongClick(position, item);
                return true;
            });
        }


        return imageView;
    }
}
