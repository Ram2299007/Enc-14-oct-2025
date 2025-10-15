package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.Appzia.enclosure.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Shared Element Transition Helper
 * Provides beautiful shared element transitions throughout the app
 * Replaces traditional slide/fade animations with modern shared element transitions
 */
public class SharedElementTransitionHelper {

    // Transition types
    public static final int SHARED_ELEMENT_TRANSITION = 1;
    public static final int EXPLODE_TRANSITION = 2;
    public static final int FADE_TRANSITION = 3;
    public static final int SLIDE_TRANSITION = 4;
    public static final int COMBINED_TRANSITION = 5;

    // Transition durations
    private static final int DEFAULT_DURATION = 300;
    private static final int FAST_DURATION = 200;
    private static final int SLOW_DURATION = 500;

    /**
     * Start activity with shared element transition
     */
    public static void startActivityWithSharedElement(Activity currentActivity, Intent intent,
                                                      View sharedElement, String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    currentActivity, sharedElement, transitionName).toBundle();
            currentActivity.startActivity(intent, options);
        } else {
            // Fallback to smooth navigation for older versions
            SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
        }
    }

    /**
     * Start activity with multiple shared elements
     */
    public static void startActivityWithSharedElements(Activity currentActivity, Intent intent,
                                                       List<Pair<View, String>> sharedElements) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String>[] pairs = new Pair[sharedElements.size()];
            for (int i = 0; i < sharedElements.size(); i++) {
                pairs[i] = sharedElements.get(i);
            }

            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    currentActivity, pairs).toBundle();
            currentActivity.startActivity(intent, options);
        } else {
            // Fallback to smooth navigation for older versions
            SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
        }
    }

    /**
     * Start activity with shared element and finish current
     */
    public static void startActivityWithSharedElementAndFinish(Activity currentActivity, Intent intent,
                                                               View sharedElement, String transitionName) {
        startActivityWithSharedElement(currentActivity, intent, sharedElement, transitionName);
        currentActivity.finish();
    }

    /**
     * Start activity with shared elements and finish current
     */
    public static void startActivityWithSharedElementsAndFinish(Activity currentActivity, Intent intent,
                                                                List<Pair<View, String>> sharedElements) {
        startActivityWithSharedElements(currentActivity, intent, sharedElements);
        currentActivity.finish();
    }

    /**
     * Start activity with explode transition
     */
    public static void startActivityWithExplode(Activity currentActivity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupExplodeTransition(currentActivity);
            currentActivity.startActivity(intent);
        } else {
            SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
        }
    }

    /**
     * Start activity with fade transition
     */
    public static void startActivityWithFade(Activity currentActivity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupFadeTransition(currentActivity);
            currentActivity.startActivity(intent);
        } else {
            SmoothNavigationHelper.startActivityWithFade(currentActivity, intent);
        }
    }

    /**
     * Start activity with slide transition
     */
    public static void startActivityWithSlide(Activity currentActivity, Intent intent, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSlideTransition(currentActivity, gravity);
            currentActivity.startActivity(intent);
        } else {
            SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
        }
    }

    /**
     * Start activity with combined transition (explode + fade)
     */
    public static void startActivityWithCombinedTransition(Activity currentActivity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupCombinedTransition(currentActivity);
            currentActivity.startActivity(intent);
        } else {
            SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
        }
    }

    /**
     * Start activity with custom transition type
     */
    public static void startActivityWithTransition(Activity currentActivity, Intent intent, int transitionType) {
        switch (transitionType) {
            case EXPLODE_TRANSITION:
                startActivityWithExplode(currentActivity, intent);
                break;
            case FADE_TRANSITION:
                startActivityWithFade(currentActivity, intent);
                break;
            case SLIDE_TRANSITION:
                startActivityWithSlide(currentActivity, intent, Gravity.END);
                break;
            case COMBINED_TRANSITION:
                startActivityWithCombinedTransition(currentActivity, intent);
                break;
            default:
                SmoothNavigationHelper.startActivityWithSlideFromRight(currentActivity, intent);
                break;
        }
    }

    /**
     * Setup explode transition for activity
     */
    private static void setupExplodeTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(DEFAULT_DURATION);
            explode.setInterpolator(new DecelerateInterpolator());

            activity.getWindow().setExitTransition(explode);
            activity.getWindow().setEnterTransition(explode);
        }
    }

    /**
     * Setup fade transition for activity
     */
    private static void setupFadeTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(DEFAULT_DURATION);
            fade.setInterpolator(new DecelerateInterpolator());

            activity.getWindow().setExitTransition(fade);
            activity.getWindow().setEnterTransition(fade);
        }
    }

    /**
     * Setup slide transition for activity
     */
    private static void setupSlideTransition(Activity activity, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(gravity);
            slide.setDuration(DEFAULT_DURATION);
            slide.setInterpolator(new DecelerateInterpolator());

            activity.getWindow().setExitTransition(slide);
            activity.getWindow().setEnterTransition(slide);
        }
    }

    /**
     * Setup combined transition for activity
     */
    private static void setupCombinedTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();

            Explode explode = new Explode();
            explode.setDuration(DEFAULT_DURATION);
            explode.setInterpolator(new DecelerateInterpolator());

            Fade fade = new Fade();
            fade.setDuration(DEFAULT_DURATION);
            fade.setInterpolator(new DecelerateInterpolator());

            transitionSet.addTransition(explode);
            transitionSet.addTransition(fade);

            activity.getWindow().setExitTransition(transitionSet);
            activity.getWindow().setEnterTransition(transitionSet);
        }
    }

    /**
     * Setup shared element transition for activity
     */
    public static void setupSharedElementTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();

            // Change bounds for size changes
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(DEFAULT_DURATION);
            changeBounds.setInterpolator(new DecelerateInterpolator());

            // Change transform for rotation/scale changes
            ChangeTransform changeTransform = new ChangeTransform();
            changeTransform.setDuration(DEFAULT_DURATION);
            changeTransform.setInterpolator(new DecelerateInterpolator());

            // Change image transform for image-specific changes
            ChangeImageTransform changeImageTransform = new ChangeImageTransform();
            changeImageTransform.setDuration(DEFAULT_DURATION);
            changeImageTransform.setInterpolator(new DecelerateInterpolator());

            // Change clip bounds for clipping changes
            ChangeClipBounds changeClipBounds = new ChangeClipBounds();
            changeClipBounds.setDuration(DEFAULT_DURATION);
            changeClipBounds.setInterpolator(new DecelerateInterpolator());

            transitionSet.addTransition(changeBounds);
            transitionSet.addTransition(changeTransform);
            transitionSet.addTransition(changeImageTransform);
            transitionSet.addTransition(changeClipBounds);

            activity.getWindow().setSharedElementEnterTransition(transitionSet);
            activity.getWindow().setSharedElementExitTransition(transitionSet);
        }
    }

    /**
     * Setup shared element transition with custom duration
     */
    public static void setupSharedElementTransition(Activity activity, int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();

            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(duration);
            changeBounds.setInterpolator(new DecelerateInterpolator());

            ChangeTransform changeTransform = new ChangeTransform();
            changeTransform.setDuration(duration);
            changeTransform.setInterpolator(new DecelerateInterpolator());

            ChangeImageTransform changeImageTransform = new ChangeImageTransform();
            changeImageTransform.setDuration(duration);
            changeImageTransform.setInterpolator(new DecelerateInterpolator());

            ChangeClipBounds changeClipBounds = new ChangeClipBounds();
            changeClipBounds.setDuration(duration);
            changeClipBounds.setInterpolator(new DecelerateInterpolator());

            transitionSet.addTransition(changeBounds);
            transitionSet.addTransition(changeTransform);
            transitionSet.addTransition(changeImageTransform);
            transitionSet.addTransition(changeClipBounds);

            activity.getWindow().setSharedElementEnterTransition(transitionSet);
            activity.getWindow().setSharedElementExitTransition(transitionSet);
        }
    }

    /**
     * Finish activity with shared element transition
     */
    public static void finishWithSharedElement(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAfterTransition();
        } else {
            SmoothNavigationHelper.finishWithSlideToRight(activity);
        }
    }

    /**
     * Create shared element pair for multiple elements
     */
    public static Pair<View, String> createSharedElementPair(View view, String transitionName) {
        return new Pair<>(view, transitionName);
    }

    /**
     * Create list of shared element pairs
     */
    public static List<Pair<View, String>> createSharedElementPairs(View[] views, String[] transitionNames) {
        List<Pair<View, String>> pairs = new ArrayList<>();
        for (int i = 0; i < views.length && i < transitionNames.length; i++) {
            pairs.add(createSharedElementPair(views[i], transitionNames[i]));
        }
        return pairs;
    }

    /**
     * Setup activity for shared element transitions
     */
    public static void setupActivityForSharedElements(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Enable transitions
            activity.getWindow().requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS);
            activity.getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);

            // Setup shared element transitions
            setupSharedElementTransition(activity);
        }
    }

    /**
     * Setup activity for shared elements with custom duration
     */
    public static void setupActivityForSharedElements(Activity activity, int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Enable transitions
            activity.getWindow().requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS);
            activity.getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);

            // Setup shared element transitions with custom duration
            setupSharedElementTransition(activity, duration);
        }
    }
}
