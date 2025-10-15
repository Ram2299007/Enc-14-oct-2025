package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;

/**
 * Utility class to optimize image loading and RecyclerView performance
 * for chat adapters to prevent image quality issues during scrolling
 */
public class ImageLoadingOptimizer {

    private static final String TAG = "ImageLoadingOptimizer";
    
    /**
     * Configure Glide for optimal performance in chat applications
     */
    public static void configureGlide(Context context) {
        try {
            GlideBuilder builder = new GlideBuilder();
            
            // Calculate optimal memory cache size (20% of available memory)
            MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                    .setMemoryCacheScreens(2)
                    .build();
            int memoryCacheSize = calculator.getMemoryCacheSize();
            builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
            
            // Configure disk cache (100MB for chat images)
            int diskCacheSize = 100 * 1024 * 1024; // 100MB
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSize));
            
            // Set default request options for better quality
            builder.setDefaultRequestOptions(
                new com.bumptech.glide.request.RequestOptions()
                    .format(com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888)
                    .dontAnimate()
                    .timeout(15000)
            );
            
            // Apply the configuration
            Glide.init(context, builder);
            
            Log.d(TAG, "Glide configured successfully with memory cache: " + 
                  (memoryCacheSize / (1024 * 1024)) + "MB, disk cache: " + 
                  (diskCacheSize / (1024 * 1024)) + "MB");
                  
        } catch (Exception e) {
            Log.e(TAG, "Failed to configure Glide", e);
        }
    }
    
    /**
     * Optimize RecyclerView for better image loading performance
     */
    public static void optimizeRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        
        try {
            // Increase item view cache size for better performance
            recyclerView.setItemViewCacheSize(100); // Increased from 50
            
            // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height
            
            // Enable drawing cache for better performance
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(android.view.View.DRAWING_CACHE_QUALITY_HIGH);
            
            // Disable item animations for better performance during scrolling
            recyclerView.setItemAnimator(null);
            
            // Set scroll listener to optimize image loading during scroll
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // Resume image loading when scrolling stops
                        Glide.with(recyclerView.getContext()).resumeRequests();
                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        // Pause image loading during fast scrolling to improve performance
                        Glide.with(recyclerView.getContext()).pauseRequests();
                    }
                }
            });
            
            Log.d(TAG, "RecyclerView optimized successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to optimize RecyclerView", e);
        }
    }
    
    /**
     * Clear Glide cache to free memory
     */
    public static void clearImageCache(Context context) {
        try {
            Glide.get(context).clearMemory();
            new Thread(() -> {
                Glide.get(context).clearDiskCache();
            }).start();
            Log.d(TAG, "Image cache cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear image cache", e);
        }
    }
    
    /**
     * Preload images for better scrolling performance
     */
    public static void preloadImages(Context context, String[] imageUrls, int width, int height) {
        try {
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    Glide.with(context)
                        .load(url)
                        .override(width, height)
                        .preload();
                }
            }
            Log.d(TAG, "Preloaded " + imageUrls.length + " images");
        } catch (Exception e) {
            Log.e(TAG, "Failed to preload images", e);
        }
    }
}
