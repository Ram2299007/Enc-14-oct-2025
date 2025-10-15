package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import java.io.File;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.Priority;

import jp.wasabeef.glide.transformations.BlurTransformation;

import com.Appzia.enclosure.Model.messageModel;

/**
 * Specialized utility for handling blur effects in chat images
 * Prevents black images and pixelation while maintaining smooth scrolling
 */
public class BlurImageOptimizer {

    private static final String TAG = "BlurImageOptimizer";

    /**
     * Load image with optimized blur that prevents black images and pixelation
     */
    public static void loadImageWithSafeBlur(Context context, String imageSource, RequestOptions requestOptions,
                                             ImageView targetImageView, ViewGroup parentLayout, int position,
                                             Object model, View videoIcon) {

        // Validate image source before proceeding
        if (!isValidImageSource(imageSource)) {
            Log.e(TAG, "Invalid image source, skipping blur optimization: " + imageSource);
            return;
        }

        float density = context.getResources().getDisplayMetrics().density;
        int orientation = context.getResources().getConfiguration().orientation;

        // Calculate dimensions based on actual image dimensions from model
        int finalWidthPx, finalHeightPx;
        if (model instanceof messageModel) {
            messageModel msgModel = (messageModel) model;
            finalWidthPx = calculateOptimalWidth(context, msgModel.getImageWidth(), msgModel.getImageHeight(), msgModel.getAspectRatio());
            finalHeightPx = calculateOptimalHeight(context, msgModel.getImageWidth(), msgModel.getImageHeight(), msgModel.getAspectRatio());
        } else {
            // Fallback to default dimensions if model is not available
            finalWidthPx = (int) (210 * density);
            finalHeightPx = (int) (250 * density);
        }

        // Create blur request with safety measures
        RequestBuilder<Drawable> blurRequest = createSafeBlurRequest(context, imageSource, requestOptions, position, finalWidthPx, finalHeightPx);

        // Set ImageView dimensions
        setImageViewDimensions(targetImageView, finalWidthPx, finalHeightPx);
        adjustParentLayout(parentLayout);

        // Create main request
        RequestBuilder<Drawable> mainRequest = createMainRequest(context, imageSource, requestOptions, position);

        // Single quality setting for all images - 75% quality
        videoIcon.setVisibility(View.VISIBLE);
        mainRequest = mainRequest
                .override(finalWidthPx, finalHeightPx)
                .encodeQuality(75) // Single 75% quality for all images
                .priority(Priority.HIGH)
                .thumbnail(blurRequest);

        // Execute with error handling
        executeRequestWithFallback(mainRequest, context, imageSource, requestOptions, position, finalWidthPx, finalHeightPx, targetImageView);
    }

    /**
     * Create a safe blur request that prevents black images and scrolling artifacts
     */
    private static RequestBuilder<Drawable> createSafeBlurRequest(Context context, String imageSource,
                                                                  RequestOptions requestOptions, int position,
                                                                  int width, int height) {
        return Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position + "_safe_blur"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888) // High quality format prevents artifacts
                        .encodeQuality(90) // High quality encoding for blur thumbnails
                        .override(width / 2, height / 2) // Increased resolution for better blur quality
                        .dontAnimate()
                        .timeout(8000))
                .transform(new BlurTransformation(6, 1)); // Gentle blur (6 radius, 1 sampling)
    }

    /**
     * Create an optimized blur request for low quality mode - prevents scrolling artifacts
     */
    private static RequestBuilder<Drawable> createOptimizedBlurRequest(Context context, String imageSource,
                                                                       RequestOptions requestOptions, int position,
                                                                       int width, int height) {
        return Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position + "_opt_blur"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .encodeQuality(85) // Better quality encoding for low quality mode
                        .override((int) (width * 0.7f), (int) (height * 0.7f)) // Increased resolution for better quality during scrolling
                        .dontAnimate()
                        .timeout(10000))
                .transform(new BlurTransformation(8, 2)); // Medium blur (8 radius, 2 sampling)
    }


    /**
     * Create main request for high quality images - optimized for scrolling performance
     */
    private static RequestBuilder<Drawable> createMainRequest(Context context, String imageSource,
                                                              RequestOptions requestOptions, int position) {
        return Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position + "_main"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .encodeQuality(95) // High quality encoding for main images
                        .dontAnimate()
                        .timeout(15000))
                ;
    }

    /**
     * Set ImageView dimensions
     */
    private static void setImageViewDimensions(ImageView imageView, int width, int height) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        imageView.setLayoutParams(params);
    }

    /**
     * Adjust parent layout dimensions
     */
    private static void adjustParentLayout(ViewGroup parentLayout) {
        if (parentLayout != null) {
            ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
            if (parentParams != null) {
                parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentLayout.setLayoutParams(parentParams);
            }
        }
    }


    /**
     * Execute request with fallback to prevent black images
     */
    private static void executeRequestWithFallback(RequestBuilder<Drawable> request, Context context,
                                                   String imageSource, RequestOptions requestOptions,
                                                   int position, int width, int height, ImageView targetImageView) {
        request.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e(TAG, "Blur image load failed for: " + imageSource, e);

                // Fallback to non-blurred image to prevent black images
                loadFallbackImage(context, imageSource, requestOptions, position, width, height, targetImageView);
                return true; // Prevent default error handling
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d(TAG, "Blur image loaded successfully: " + imageSource);
                return false;
            }
        }).into(targetImageView);
    }

    /**
     * Load fallback image without blur to prevent black images
     */
    private static void loadFallbackImage(Context context, String imageSource, RequestOptions requestOptions,
                                          int position, int width, int height, ImageView targetImageView) {
        Log.d(TAG, "Loading fallback image for: " + imageSource);

        // Validate image source - check if it's a valid file path
        if (imageSource == null || imageSource.isEmpty()) {
            Log.e(TAG, "Invalid image source: null or empty");
            return;
        }

        // Check if the path is a directory (ends with /)
        if (imageSource.endsWith("/")) {
            Log.e(TAG, "Cannot load directory as image: " + imageSource);
            return;
        }

        // Check if the file exists and is not a directory
        File imageFile = new File(imageSource);
        if (!imageFile.exists() || imageFile.isDirectory()) {
            Log.e(TAG, "Image file does not exist or is a directory: " + imageSource);
            return;
        }

        // Use Handler to post the fallback request to main thread to avoid callback issues
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Glide.with(context)
                        .load(imageSource)
                        .apply(requestOptions
                                .signature(new ObjectKey(position + "_fallback"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(width, height)
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .encodeQuality(90) // High quality encoding for fallback images
                                .dontAnimate()
                                .timeout(12000))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e(TAG, "Fallback image also failed for: " + imageSource, e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d(TAG, "Fallback image loaded successfully for: " + imageSource);
                                return false;
                            }
                        })
                        .into(targetImageView);
            } catch (Exception e) {
                Log.e(TAG, "Error loading fallback image: " + imageSource, e);
            }
        });
    }

    /**
     * Calculate optimal width based on image dimensions and aspect ratio
     */
    private static int calculateOptimalWidth(Context context, String widthStr, String heightStr, String aspectRatioStr) {
        try {
            // Check for null or empty strings
            if (widthStr == null || widthStr.isEmpty() || heightStr == null || heightStr.isEmpty() ||
                    aspectRatioStr == null || aspectRatioStr.isEmpty()) {
                Log.w("BlurImageOptimizer", "Empty or null dimension strings, using default width");
                return (int) (200 * context.getResources().getDisplayMetrics().density);
            }

            float density = context.getResources().getDisplayMetrics().density;
            int orientation = context.getResources().getConfiguration().orientation;

            // Parse dimensions with additional safety checks
            float imageWidthDp = 0f;
            float imageHeightDp = 0f;
            float aspectRatio = 1f;

            try {
                imageWidthDp = Float.parseFloat(widthStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid width string: " + widthStr + ", using default");
                imageWidthDp = 200f;
            }

            try {
                imageHeightDp = Float.parseFloat(heightStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid height string: " + heightStr + ", using default");
                imageHeightDp = 200f;
            }

            try {
                aspectRatio = Float.parseFloat(aspectRatioStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid aspect ratio string: " + aspectRatioStr + ", using default");
                aspectRatio = 1f;
            }

            // Define maximum dimensions in dp
            final float MAX_WIDTH_DP = 210f;
            final float MAX_HEIGHT_DP = 250f;

            int maxWidthPx = (int) (MAX_WIDTH_DP * density);
            int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

            int finalWidthPx, finalHeightPx;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Landscape: Prioritize width for wide images
                finalWidthPx = maxWidthPx;
                finalHeightPx = (int) (maxWidthPx / aspectRatio);
                if (finalHeightPx > maxHeightPx) {
                    finalHeightPx = maxHeightPx;
                    finalWidthPx = (int) (maxHeightPx * aspectRatio);
                }
            } else {
                // Portrait: Prioritize height for wide images
                finalHeightPx = maxHeightPx;
                finalWidthPx = (int) (maxHeightPx * aspectRatio);
                if (finalWidthPx > maxWidthPx) {
                    finalWidthPx = maxWidthPx;
                    finalHeightPx = (int) (maxWidthPx / aspectRatio);
                }
            }

            int result = Math.min(finalWidthPx, maxWidthPx);
            Log.d(TAG, "calculateOptimalWidth - imageWidthDp: " + imageWidthDp + ", imageHeightDp: " + imageHeightDp + ", aspectRatio: " + aspectRatio + ", orientation: " + orientation + ", finalWidthPx: " + finalWidthPx + ", result: " + result);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Error calculating optimal width, using default", e);
            return (int) (210 * context.getResources().getDisplayMetrics().density);
        }
    }

    /**
     * Calculate optimal height based on image dimensions and aspect ratio
     */
    private static int calculateOptimalHeight(Context context, String widthStr, String heightStr, String aspectRatioStr) {
        try {
            // Check for null or empty strings
            if (widthStr == null || widthStr.isEmpty() || heightStr == null || heightStr.isEmpty() ||
                    aspectRatioStr == null || aspectRatioStr.isEmpty()) {
                Log.w("BlurImageOptimizer", "Empty or null dimension strings, using default height");
                return (int) (200 * context.getResources().getDisplayMetrics().density);
            }

            float density = context.getResources().getDisplayMetrics().density;
            int orientation = context.getResources().getConfiguration().orientation;

            // Parse dimensions with additional safety checks
            float imageWidthDp = 0f;
            float imageHeightDp = 0f;
            float aspectRatio = 1f;

            try {
                imageWidthDp = Float.parseFloat(widthStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid width string: " + widthStr + ", using default");
                imageWidthDp = 200f;
            }

            try {
                imageHeightDp = Float.parseFloat(heightStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid height string: " + heightStr + ", using default");
                imageHeightDp = 200f;
            }

            try {
                aspectRatio = Float.parseFloat(aspectRatioStr);
            } catch (NumberFormatException e) {
                Log.w("BlurImageOptimizer", "Invalid aspect ratio string: " + aspectRatioStr + ", using default");
                aspectRatio = 1f;
            }

            // Define maximum dimensions in dp
            final float MAX_WIDTH_DP = 210f;
            final float MAX_HEIGHT_DP = 250f;

            int maxWidthPx = (int) (MAX_WIDTH_DP * density);
            int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

            int finalWidthPx, finalHeightPx;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Landscape: Prioritize width for wide images
                finalWidthPx = maxWidthPx;
                finalHeightPx = (int) (maxWidthPx / aspectRatio);
                if (finalHeightPx > maxHeightPx) {
                    finalHeightPx = maxHeightPx;
                    finalWidthPx = (int) (maxHeightPx * aspectRatio);
                }
            } else {
                // Portrait: Prioritize height for wide images
                finalHeightPx = maxHeightPx;
                finalWidthPx = (int) (maxHeightPx * aspectRatio);
                if (finalWidthPx > maxWidthPx) {
                    finalWidthPx = maxWidthPx;
                    finalHeightPx = (int) (maxWidthPx / aspectRatio);
                }
            }

            int result = Math.min(finalHeightPx, maxHeightPx);
            Log.d(TAG, "calculateOptimalHeight - imageWidthDp: " + imageWidthDp + ", imageHeightDp: " + imageHeightDp + ", aspectRatio: " + aspectRatio + ", orientation: " + orientation + ", finalHeightPx: " + finalHeightPx + ", result: " + result);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Error calculating optimal height, using default", e);
            return (int) (250 * context.getResources().getDisplayMetrics().density);
        }
    }

    /**
     * Validate if the image source is valid for loading
     */
    private static boolean isValidImageSource(String imageSource) {
        if (imageSource == null || imageSource.isEmpty()) {
            Log.e(TAG, "Image source is null or empty");
            return false;
        }

        // Check if the path is a directory (ends with /)
        if (imageSource.endsWith("/")) {
            Log.e(TAG, "Image source is a directory: " + imageSource);
            return false;
        }

        // Check if it's a network URL (http/https)
        if (imageSource.startsWith("http://") || imageSource.startsWith("https://")) {
            Log.d(TAG, "Valid network URL: " + imageSource);
            return true;
        }

        // Check if it's a content URI
        if (imageSource.startsWith("content://")) {
            Log.d(TAG, "Valid content URI: " + imageSource);
            return true;
        }

        // Check if it's an asset path
        if (imageSource.startsWith("file:///android_asset/")) {
            Log.d(TAG, "Valid asset path: " + imageSource);
            return true;
        }

        // For local file paths, check if the file exists and is not a directory
        try {
            File imageFile = new File(imageSource);
            if (!imageFile.exists()) {
                Log.e(TAG, "Image file does not exist: " + imageSource);
                return false;
            }
            if (imageFile.isDirectory()) {
                Log.e(TAG, "Image source is a directory: " + imageSource);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error validating image source: " + imageSource, e);
            return false;
        }
        return true;
    }

    /**
     * Clear blur cache to free memory
     */
    public static void clearBlurCache(Context context) {
        try {
            // Clear memory cache for blur images
            Glide.get(context).clearMemory();

            // Clear disk cache in background
            new Thread(() -> {
                Glide.get(context).clearDiskCache();
            }).start();

            Log.d(TAG, "Blur cache cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear blur cache", e);
        }
    }
}
