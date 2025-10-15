package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.core.util.Pair;

import com.Appzia.enclosure.R;

import java.util.List;

/**
 * Enhanced Navigation Helper with Shared Element Transitions
 * Provides both traditional smooth animations and modern shared element transitions
 * Automatically uses shared element transitions on Android 5.0+ and falls back to smooth animations
 */
public class SmoothNavigationHelper {
    
    // Traditional animation types
    public static final int SLIDE_FROM_RIGHT = 1;
    public static final int SLIDE_FROM_LEFT = 2;
    public static final int FADE_IN_OUT = 3;
    public static final int SLIDE_FROM_BOTTOM = 4;
    
    // Shared element transition types
    public static final int SHARED_ELEMENT_TRANSITION = 5;
    public static final int EXPLODE_TRANSITION = 6;
    public static final int FADE_TRANSITION = 7;
    public static final int SLIDE_TRANSITION = 8;
    public static final int COMBINED_TRANSITION = 9;
    
    // Global setting for shared element transitions
    private static boolean useSharedElementTransitions = true;
    
    /**
     * Start activity with smooth slide animation from right
     */
    public static void startActivityWithSlideFromRight(Activity currentActivity, Intent intent) {
        startActivityWithSlideFromRight(currentActivity, intent, false);
    }
    
    /**
     * Start activity with smooth slide animation from right
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithSlideFromRight(Activity currentActivity, Intent intent, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(
            R.anim.slide_in_from_right_smooth,
            R.anim.slide_out_to_left_smooth
        );
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Start activity with smooth slide animation from left
     */
    public static void startActivityWithSlideFromLeft(Activity currentActivity, Intent intent) {
        startActivityWithSlideFromLeft(currentActivity, intent, false);
    }
    
    /**
     * Start activity with smooth slide animation from left
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithSlideFromLeft(Activity currentActivity, Intent intent, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(
            R.anim.slide_in_from_left_smooth,
            R.anim.slide_out_to_right_smooth
        );
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Start activity with smooth fade animation
     */
    public static void startActivityWithFade(Activity currentActivity, Intent intent) {
        startActivityWithFade(currentActivity, intent, false);
    }
    
    /**
     * Start activity with smooth fade animation
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithFade(Activity currentActivity, Intent intent, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(
            R.anim.fade_in_smooth,
            R.anim.fade_out_smooth
        );
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Start activity with custom animation type
     */
    public static void startActivityWithAnimation(Activity currentActivity, Intent intent, int animationType) {
        startActivityWithAnimation(currentActivity, intent, animationType, false);
    }
    
    /**
     * Start activity with custom animation type
     * @param finishCurrent true to finish current activity after starting new one
     */
    public static void startActivityWithAnimation(Activity currentActivity, Intent intent, int animationType, boolean finishCurrent) {
        currentActivity.startActivity(intent);
        
        switch (animationType) {
            case SLIDE_FROM_RIGHT:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_right_smooth,
                    R.anim.slide_out_to_left_smooth
                );
                break;
            case SLIDE_FROM_LEFT:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_left_smooth,
                    R.anim.slide_out_to_right_smooth
                );
                break;
            case FADE_IN_OUT:
                currentActivity.overridePendingTransition(
                    R.anim.fade_in_smooth,
                    R.anim.fade_out_smooth
                );
                break;
            case SLIDE_FROM_BOTTOM:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_bottom,
                    R.anim.slide_out_top
                );
                break;
            default:
                // Default to slide from right
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_right_smooth,
                    R.anim.slide_out_to_left_smooth
                );
                break;
        }
        
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
    
    /**
     * Finish activity with smooth back animation
     */
    public static void finishWithSlideToRight(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(
            R.anim.slide_in_from_left_smooth,
            R.anim.slide_out_to_right_smooth
        );
    }
    
    /**
     * Finish activity with smooth fade out animation
     */
    public static void finishWithFade(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(
            R.anim.fade_in_smooth,
            R.anim.fade_out_smooth
        );
    }
    
    /**
     * Start activity for result with smooth animation
     */
    public static void startActivityForResultWithAnimation(Activity currentActivity, Intent intent, int requestCode, int animationType) {
        currentActivity.startActivityForResult(intent, requestCode);
        
        switch (animationType) {
            case SLIDE_FROM_RIGHT:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_right_smooth,
                    R.anim.slide_out_to_left_smooth
                );
                break;
            case SLIDE_FROM_LEFT:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_left_smooth,
                    R.anim.slide_out_to_right_smooth
                );
                break;
            case FADE_IN_OUT:
                currentActivity.overridePendingTransition(
                    R.anim.fade_in_smooth,
                    R.anim.fade_out_smooth
                );
                break;
            default:
                currentActivity.overridePendingTransition(
                    R.anim.slide_in_from_right_smooth,
                    R.anim.slide_out_to_left_smooth
                );
                break;
        }
    }
    
    /**
     * Handle back press with smooth animation
     */
    public static boolean handleBackPress(Activity activity) {
        finishWithSlideToRight(activity);
        return true;
    }
    
    /**
     * Set up smooth back press handling for an activity
     * Call this in onCreate() after setContentView()
     */
    public static void setupSmoothBackPress(Activity activity) {
        // This will be handled by overriding onBackPressed in each activity
        // or using OnBackPressedDispatcher for newer Android versions
    }
    
    // ==================== SHARED ELEMENT TRANSITION METHODS ====================
    
    /**
     * Enable or disable shared element transitions globally
     */
    public static void setUseSharedElementTransitions(boolean useSharedElementTransitions) {
        SmoothNavigationHelper.useSharedElementTransitions = useSharedElementTransitions;
    }
    
    /**
     * Check if shared element transitions are enabled
     */
    public static boolean isSharedElementTransitionsEnabled() {
        return useSharedElementTransitions && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    
    /**
     * Start activity with shared element transition
     */
    public static void startActivityWithSharedElement(Activity currentActivity, Intent intent, 
                                                     View sharedElement, String transitionName) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithSharedElement(currentActivity, intent, sharedElement, transitionName);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with shared element transition and finish current
     */
    public static void startActivityWithSharedElementAndFinish(Activity currentActivity, Intent intent, 
                                                              View sharedElement, String transitionName) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithSharedElementAndFinish(currentActivity, intent, sharedElement, transitionName);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent, true);
        }
    }
    
    /**
     * Start activity with multiple shared elements
     */
    public static void startActivityWithSharedElements(Activity currentActivity, Intent intent, 
                                                      List<Pair<View, String>> sharedElements) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithSharedElements(currentActivity, intent, sharedElements);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with multiple shared elements and finish current
     */
    public static void startActivityWithSharedElementsAndFinish(Activity currentActivity, Intent intent, 
                                                               List<Pair<View, String>> sharedElements) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithSharedElementsAndFinish(currentActivity, intent, sharedElements);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent, true);
        }
    }
    
    /**
     * Start activity with explode transition
     */
    public static void startActivityWithExplode(Activity currentActivity, Intent intent) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithExplode(currentActivity, intent);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with fade transition
     */
    public static void startActivityWithFadeTransition(Activity currentActivity, Intent intent) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithFade(currentActivity, intent);
        } else {
            startActivityWithFade(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with slide transition
     */
    public static void startActivityWithSlideTransition(Activity currentActivity, Intent intent, int gravity) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithSlide(currentActivity, intent, gravity);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with combined transition
     */
    public static void startActivityWithCombinedTransition(Activity currentActivity, Intent intent) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithCombinedTransition(currentActivity, intent);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with smart transition (automatically chooses best transition)
     */
    public static void startActivityWithSmartTransition(Activity currentActivity, Intent intent) {
        if (isSharedElementTransitionsEnabled()) {
            // Use explode transition as default for modern shared element experience
            SharedElementTransitionHelper.startActivityWithExplode(currentActivity, intent);
        } else {
            startActivityWithSlideFromRight(currentActivity, intent);
        }
    }
    
    /**
     * Start activity with smart transition and finish current
     */
    public static void startActivityWithSmartTransitionAndFinish(Activity currentActivity, Intent intent) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.startActivityWithExplode(currentActivity, intent);
            currentActivity.finish();
        } else {
            startActivityWithSlideFromRight(currentActivity, intent, true);
        }
    }
    
    /**
     * Finish activity with shared element transition
     */
    public static void finishWithSharedElement(Activity activity) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.finishWithSharedElement(activity);
        } else {
            finishWithSlideToRight(activity);
        }
    }
    
    /**
     * Create shared element pair
     */
    public static Pair<View, String> createSharedElementPair(View view, String transitionName) {
        return SharedElementTransitionHelper.createSharedElementPair(view, transitionName);
    }
    
    /**
     * Create list of shared element pairs
     */
    public static List<Pair<View, String>> createSharedElementPairs(View[] views, String[] transitionNames) {
        return SharedElementTransitionHelper.createSharedElementPairs(views, transitionNames);
    }
    
    /**
     * Setup activity for shared element transitions
     */
    public static void setupActivityForSharedElements(Activity activity) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.setupActivityForSharedElements(activity);
        }
    }
    
    /**
     * Setup activity for shared elements with custom duration
     */
    public static void setupActivityForSharedElements(Activity activity, int duration) {
        if (isSharedElementTransitionsEnabled()) {
            SharedElementTransitionHelper.setupActivityForSharedElements(activity, duration);
        }
    }
}
