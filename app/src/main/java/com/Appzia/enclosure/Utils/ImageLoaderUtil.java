package com.Appzia.enclosure.Utils;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Utility class for safely loading images with Picasso
 * Prevents crashes from empty or null image URLs
 */
public class ImageLoaderUtil {
    private static final String TAG = "ImageLoaderUtil";

    /**
     * Safely load an image with a placeholder
     * @param photoUrl The URL to load (can be null or empty)
     * @param imageView The ImageView to load into
     * @param placeholderResId The placeholder drawable resource ID
     */
    public static void safeLoadImage(String photoUrl, ImageView imageView, int placeholderResId) {
        try {
            if (photoUrl != null && !photoUrl.trim().isEmpty()) {
                Picasso.get().load(photoUrl).placeholder(placeholderResId).into(imageView);
            } else {
                // Load placeholder if photo URL is null or empty
                Picasso.get().load(placeholderResId).into(imageView);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            // Fallback to placeholder on any error
            try {
                Picasso.get().load(placeholderResId).into(imageView);
            } catch (Exception fallbackException) {
                Log.e(TAG, "Error loading placeholder: " + fallbackException.getMessage());
            }
        }
    }

    /**
     * Safely load an image with placeholder and error handling
     * @param photoUrl The URL to load (can be null or empty)
     * @param imageView The ImageView to load into
     * @param placeholderResId The placeholder drawable resource ID
     * @param errorResId The error drawable resource ID
     */
    public static void safeLoadImage(String photoUrl, ImageView imageView, int placeholderResId, int errorResId) {
        try {
            if (photoUrl != null && !photoUrl.trim().isEmpty()) {
                Picasso.get().load(photoUrl).placeholder(placeholderResId).error(errorResId).into(imageView);
            } else {
                // Load placeholder if photo URL is null or empty
                Picasso.get().load(placeholderResId).into(imageView);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            // Fallback to placeholder on any error
            try {
                Picasso.get().load(placeholderResId).into(imageView);
            } catch (Exception fallbackException) {
                Log.e(TAG, "Error loading placeholder: " + fallbackException.getMessage());
            }
        }
    }

    /**
     * Safely load an image without placeholder
     * @param photoUrl The URL to load (can be null or empty)
     * @param imageView The ImageView to load into
     */
    public static void safeLoadImage(String photoUrl, ImageView imageView) {
        try {
            if (photoUrl != null && !photoUrl.trim().isEmpty()) {
                Picasso.get().load(photoUrl).into(imageView);
            }
            // If URL is null or empty, do nothing (keep current image)
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
        }
    }
}
