package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.Appzia.enclosure.Dialogs.MultipleImageDialog;

import java.util.List;

/**
 * Helper class to easily show the MultipleImageDialog
 */
public class MultipleImageDialogHelper {
    
    private static final String TAG = "MultipleImageDialogHelper";
    
    /**
     * Show the multiple image dialog with the provided parameters
     * 
     * @param fragmentManager The FragmentManager to show the dialog
     * @param imageList List of image paths/URLs
     * @param currentPosition Current position to scroll to
     * @param viewHolderTypeKey Type key for view holder
     * @param imageClickListener Optional click listener for images
     */
    public static void showDialog(FragmentManager fragmentManager, 
                                 List<String> imageList, 
                                 int currentPosition, 
                                 String viewHolderTypeKey,
                                 MultipleImageDialog.OnImageClickListener imageClickListener) {
        
        if (fragmentManager == null) {
            Log.e(TAG, "FragmentManager is null, cannot show dialog");
            return;
        }
        
        if (imageList == null || imageList.isEmpty()) {
            Log.w(TAG, "Image list is null or empty, cannot show dialog");
            return;
        }
        
        try {
            // Create dialog instance
            MultipleImageDialog dialog = MultipleImageDialog.newInstance(
                imageList, 
                currentPosition, 
                viewHolderTypeKey
            );
            
            // Set click listener if provided
            if (imageClickListener != null) {
                dialog.setOnImageClickListener(imageClickListener);
            }
            
            // Show dialog
            dialog.show(fragmentManager, "MultipleImageDialog");
            
            Log.d(TAG, "Dialog shown successfully with " + imageList.size() + " images");
            
        } catch (Exception e) {
            Log.e(TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }
    
    /**
     * Show the multiple image dialog with default parameters
     * 
     * @param fragmentManager The FragmentManager to show the dialog
     * @param imageList List of image paths/URLs
     * @param currentPosition Current position to scroll to
     * @param viewHolderTypeKey Type key for view holder
     */
    public static void showDialog(FragmentManager fragmentManager, 
                                 List<String> imageList, 
                                 int currentPosition, 
                                 String viewHolderTypeKey) {
        showDialog(fragmentManager, imageList, currentPosition, viewHolderTypeKey, null);
    }
    
    /**
     * Show the multiple image dialog with default position (0)
     * 
     * @param fragmentManager The FragmentManager to show the dialog
     * @param imageList List of image paths/URLs
     * @param viewHolderTypeKey Type key for view holder
     */
    public static void showDialog(FragmentManager fragmentManager, 
                                 List<String> imageList, 
                                 String viewHolderTypeKey) {
        showDialog(fragmentManager, imageList, 0, viewHolderTypeKey, null);
    }
    
    /**
     * Show dialog with WhatsApp-like smart positioning
     * - For 1-4 images: shows the clicked image
     * - For more than 4 images: shows from position 0
     * 
     * @param fragmentManager The FragmentManager to show the dialog
     * @param imageList List of image paths/URLs
     * @param clickedPosition Position of the clicked image
     * @param viewHolderTypeKey Type key for view holder
     */
    public static void showDialogWithSmartPositioning(FragmentManager fragmentManager, 
                                                     List<String> imageList, 
                                                     int clickedPosition, 
                                                     String viewHolderTypeKey) {
        int imageCount = imageList.size();
        int targetPosition;
        
        if (imageCount <= 4) {
            // For 1-4 images, show the clicked image
            targetPosition = Math.max(0, Math.min(clickedPosition, imageCount - 1));
        } else {
            // For more than 4 images, always start from position 0
            targetPosition = 0;
        }
        
        showDialog(fragmentManager, imageList, targetPosition, viewHolderTypeKey, null);
    }
    
    /**
     * Show dialog with filename-based positioning
     * - Finds the specific image by filename and scrolls to it
     * - If filename not found, falls back to smart positioning
     * 
     * @param fragmentManager The FragmentManager to show the dialog
     * @param imageList List of image paths/URLs
     * @param targetFilename The filename to scroll to
     * @param viewHolderTypeKey Type key for view holder
     */
    public static void showDialogWithFilename(FragmentManager fragmentManager, 
                                            List<String> imageList, 
                                            String targetFilename, 
                                            String viewHolderTypeKey) {
        Log.d(TAG, "=== SHOWING DIALOG WITH FILENAME ===");
        Log.d(TAG, "Target filename: " + targetFilename);
        Log.d(TAG, "Image list size: " + (imageList != null ? imageList.size() : 0));
        Log.d(TAG, "View holder type key: " + viewHolderTypeKey);
        
        if (fragmentManager == null) {
            Log.e(TAG, "❌ FragmentManager is null, cannot show dialog");
            return;
        }
        
        if (imageList == null || imageList.isEmpty()) {
            Log.w(TAG, "❌ Image list is null or empty, cannot show dialog");
            return;
        }
        
        try {
            // Create dialog instance with filename
            MultipleImageDialog dialog = MultipleImageDialog.newInstanceWithFilename(
                imageList, 
                targetFilename, 
                viewHolderTypeKey
            );
            
            // Show dialog
            dialog.show(fragmentManager, "MultipleImageDialog");
            
            Log.d(TAG, "✅ Dialog shown successfully with filename targeting: " + targetFilename);
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error showing dialog: " + e.getMessage(), e);
        }
    }
}
