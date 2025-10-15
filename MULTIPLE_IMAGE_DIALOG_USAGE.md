# Multiple Image Dialog Usage Guide

This guide shows how to use the new `MultipleImageDialog` instead of the `multiple_show_image_screen` activity.

## Basic Usage

### 1. Filename-based Positioning (Recommended)

```java
import com.Appzia.enclosure.Utils.MultipleImageDialogHelper;
import com.Appzia.enclosure.Dialogs.MultipleImageDialog;

// In your Activity or Fragment
List<String> imageList = Arrays.asList(
    "image1.jpg|https://example.com/image1.jpg",
    "image2.jpg|https://example.com/image2.jpg",
    "image3.jpg|https://example.com/image3.jpg",
    "image4.jpg|https://example.com/image4.jpg"
);

// Show dialog with filename-based positioning
// - Finds the specific image by filename and scrolls to it
// - If filename not found, falls back to smart positioning
MultipleImageDialogHelper.showDialogWithFilename(
    getSupportFragmentManager(),
    imageList,
    "image2.jpg", // Filename to scroll to
    "your_view_holder_type_key"
);
```

### 2. WhatsApp-like Smart Positioning

```java
// Show dialog with WhatsApp-like smart positioning
// - For 1-4 images: shows the clicked image
// - For more than 4 images: shows from position 0
MultipleImageDialogHelper.showDialogWithSmartPositioning(
    getSupportFragmentManager(),
    imageList,
    clickedImagePosition, // Position of the clicked image
    "your_view_holder_type_key"
);
```

### 3. Simple Usage (Default Behavior)

```java
// Show dialog with default behavior
MultipleImageDialogHelper.showDialog(
    getSupportFragmentManager(), // or getChildFragmentManager() if in Fragment
    imageList,
    0, // current position
    "your_view_holder_type_key"
);
```

### 4. With Custom Image Click Handler

```java
import com.Appzia.enclosure.Dialogs.MultipleImageDialog;

// Create custom click listener
MultipleImageDialog.OnImageClickListener clickListener = new MultipleImageDialog.OnImageClickListener() {
    @Override
    public void onImageClick(String imagePath, int position) {
        // Custom behavior when image is clicked
        Log.d("CustomClick", "Image clicked: " + imagePath + " at position: " + position);
        
        // You can still open the individual image screen if needed
        Intent intent = new Intent(context, show_image_Screen.class);
        // ... set up intent extras
        startActivity(intent);
    }
};

// Show dialog with custom click handler
MultipleImageDialogHelper.showDialog(
    getSupportFragmentManager(),
    imageList,
    0,
    "your_view_holder_type_key",
    clickListener
);
```

### 3. Direct Dialog Creation (Advanced)

```java
import com.Appzia.enclosure.Dialogs.MultipleImageDialog;

// Create dialog instance directly
MultipleImageDialog dialog = MultipleImageDialog.newInstance(
    imageList,
    currentPosition,
    viewHolderTypeKey
);

// Set custom click listener
dialog.setOnImageClickListener(new MultipleImageDialog.OnImageClickListener() {
    @Override
    public void onImageClick(String imagePath, int position) {
        // Custom behavior
    }
});

// Show dialog
dialog.show(getSupportFragmentManager(), "MultipleImageDialog");
```

## Migration from Activity

### Before (Activity):
```java
Intent intent = new Intent(context, multiple_show_image_screen.class);
intent.putStringArrayListExtra("imageList", new ArrayList<>(imageList));
intent.putExtra("currentPosition", currentPosition);
intent.putExtra("viewHolderTypeKey", viewHolderTypeKey);
context.startActivity(intent);
```

### After (Dialog):
```java
MultipleImageDialogHelper.showDialog(
    getSupportFragmentManager(),
    imageList,
    currentPosition,
    viewHolderTypeKey
);
```

## Features

- **Full Screen Dialog**: Takes up the entire screen like the original activity
- **Same Functionality**: All features from the original activity are preserved
- **Performance Optimized**: Uses the same optimizations as the original
- **Customizable**: Easy to customize click behavior
- **Memory Efficient**: Better memory management than activities
- **WhatsApp-like Animations**: Smooth slide-in/slide-out transitions
- **Smart Positioning**: Intelligent image positioning based on count
- **Click Animations**: Visual feedback on image clicks
- **Smooth Scrolling**: Optimized RecyclerView with smooth animations

## Animation Features

### 1. Dialog Entrance/Exit Animations
- **Slide from bottom**: Dialog slides up from bottom with fade effect
- **Slide to bottom**: Dialog slides down to bottom when dismissed
- **Smooth transitions**: 300ms entrance, 250ms exit with proper interpolators

### 2. Image Click Animations
- **Scale animation**: Images scale down (0.95x) and back up on click
- **Visual feedback**: 100ms scale down, 100ms scale up
- **Smooth timing**: Decelerate in, accelerate out for natural feel

### 3. Smart Positioning Logic
- **Filename-based**: Finds specific image by filename and scrolls to it
- **1-4 images**: Shows the clicked image (img1 → shows img1, img2 → shows img2, etc.)
- **5+ images**: Always shows from position 0 (like WhatsApp)
- **Smooth scrolling**: Automatically scrolls to target position with animation
- **Fallback support**: If filename not found, falls back to smart positioning

### 4. Enhanced UX
- **No scrollbars**: Clean appearance without visible scrollbars
- **Optimized padding**: Proper spacing for better visual hierarchy
- **Performance**: Hardware acceleration and drawing cache optimizations

## Dialog vs Activity Benefits

1. **Memory**: Dialogs use less memory than activities
2. **Performance**: Faster to show/hide
3. **Context**: Maintains parent context better
4. **Lifecycle**: Simpler lifecycle management
5. **Animations**: Better transition animations

## Notes

- The dialog uses the same layout and adapter as the original activity
- All image loading optimizations are preserved
- The dialog automatically handles back button presses
- Status bar color is set to match the app theme
- The dialog is dismissible by tapping the back button
