package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.Appzia.enclosure.R;

/**
 * Simple Swipe Navigation Helper
 * Provides clean, fast swipe-style transitions throughout the app
 */
public class SwipeNavigationHelper {
    
    /**
     * Start activity with simple swipe from right animation
     * Use this for forward navigation
     */
    public static void startActivityWithSwipe(Activity currentActivity, Intent intent) {
        startActivityWithSwipe(currentActivity, intent, false);
    }
    
    /**
     * Start activity with simple swipe from right animation
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithSwipe(Activity currentActivity, Intent intent, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(
            R.anim.slide_in_right_swipe,
            R.anim.slide_out_left_swipe
        );
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Start activity with swipe from left animation
     * Use this for back navigation or returning to previous screens
     */
    public static void startActivityWithBackSwipe(Activity currentActivity, Intent intent) {
        startActivityWithBackSwipe(currentActivity, intent, false);
    }
    
    /**
     * Start activity with swipe from left animation
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithBackSwipe(Activity currentActivity, Intent intent, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(
            R.anim.slide_in_left_swipe,
            R.anim.slide_out_right_swipe
        );
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Finish activity with simple swipe to right animation
     * Use this for back navigation
     */
    public static void finishWithSwipe(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(
            R.anim.slide_in_left_swipe,
            R.anim.slide_out_right_swipe
        );
    }
    
    /**
     * Start activity for result with swipe animation
     */
    public static void startActivityForResultWithSwipe(Activity currentActivity, Intent intent, int requestCode) {
        currentActivity.startActivityForResult(intent, requestCode);
        currentActivity.overridePendingTransition(
            R.anim.slide_in_right_swipe,
            R.anim.slide_out_left_swipe
        );
    }
    
    /**
     * Handle back press with swipe animation
     */
    public static boolean handleBackPress(Activity activity) {
        finishWithSwipe(activity);
        return true;
    }
    
    /**
     * Setup swipe gesture detector for an activity
     * Call this in onCreate() after setContentView()
     */
    public static void setupSwipeGestures(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content);
        if (rootView != null) {
            setupSwipeGestures(activity, rootView);
        }
    }
    
    /**
     * Setup swipe gesture detector for a specific view
     */
    public static void setupSwipeGestures(Activity activity, View view) {
        final GestureDetector gestureDetector = new GestureDetector(activity, new SwipeGestureListener(activity));
        
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    
    /**
     * Simple gesture listener for swipe navigation
     */
    private static class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        
        private final Activity activity;
        
        public SwipeGestureListener(Activity activity) {
            this.activity = activity;
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null) return false;
            
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            
            // Check if it's a horizontal swipe
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe right - go back
                        onSwipeRight();
                    } else {
                        // Swipe left - forward navigation (optional)
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }
        
        private void onSwipeRight() {
            // Swipe right to go back
            if (!activity.isFinishing()) {
                finishWithSwipe(activity);
            }
        }
        
        private void onSwipeLeft() {
            // Swipe left for forward navigation (can be customized per activity)
            // This is optional and can be overridden in specific activities
        }
    }
    
    /**
     * Quick navigation methods for common use cases
     */
    public static class QuickNav {
        
        /**
         * Navigate to next screen with swipe
         */
        public static void next(Activity currentActivity, Class<?> nextActivityClass) {
            Intent intent = new Intent(currentActivity, nextActivityClass);
            startActivityWithSwipe(currentActivity, intent);
        }
        
        /**
         * Navigate to next screen with swipe and finish current
         */
        public static void nextAndFinish(Activity currentActivity, Class<?> nextActivityClass) {
            Intent intent = new Intent(currentActivity, nextActivityClass);
            startActivityWithSwipe(currentActivity, intent, true);
        }
        
        /**
         * Go back with swipe animation
         */
        public static void back(Activity activity) {
            finishWithSwipe(activity);
        }
        
        /**
         * Navigate back to specific activity with swipe
         */
        public static void backTo(Activity currentActivity, Class<?> targetActivityClass) {
            Intent intent = new Intent(currentActivity, targetActivityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityWithBackSwipe(currentActivity, intent, true);
        }
    }
}
