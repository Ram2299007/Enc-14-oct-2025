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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Activity class with built-in shared element transitions
 * Extend this instead of AppCompatActivity for automatic shared element support
 */
public abstract class SharedElementBaseActivity extends AppCompatActivity {

    private static final int DEFAULT_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup shared element transitions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSharedElementTransitions();
        }
    }

    /**
     * Setup shared element transitions for this activity
     */
    private void setupSharedElementTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Enable transitions
            getWindow().requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);

            // Setup shared element transitions
            TransitionSet transitionSet = new TransitionSet();

            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(DEFAULT_DURATION);
            changeBounds.setInterpolator(new DecelerateInterpolator());

            ChangeTransform changeTransform = new ChangeTransform();
            changeTransform.setDuration(DEFAULT_DURATION);
            changeTransform.setInterpolator(new DecelerateInterpolator());

            ChangeImageTransform changeImageTransform = new ChangeImageTransform();
            changeImageTransform.setDuration(DEFAULT_DURATION);
            changeImageTransform.setInterpolator(new DecelerateInterpolator());

            ChangeClipBounds changeClipBounds = new ChangeClipBounds();
            changeClipBounds.setDuration(DEFAULT_DURATION);
            changeClipBounds.setInterpolator(new DecelerateInterpolator());

            transitionSet.addTransition(changeBounds);
            transitionSet.addTransition(changeTransform);
            transitionSet.addTransition(changeImageTransform);
            transitionSet.addTransition(changeClipBounds);

            getWindow().setSharedElementEnterTransition(transitionSet);
            getWindow().setSharedElementExitTransition(transitionSet);

            // Setup content transitions
            setupContentTransitions();
        }
    }

    /**
     * Setup content transitions (for non-shared elements)
     */
    private void setupContentTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Explode transition for content
            Explode explode = new Explode();
            explode.setDuration(DEFAULT_DURATION);
            explode.setInterpolator(new DecelerateInterpolator());

            getWindow().setEnterTransition(explode);
            getWindow().setExitTransition(explode);
        }
    }

    /**
     * Start activity with shared element transition
     */
    public void startActivityWithSharedElement(Intent intent, View sharedElement, String transitionName) {
        SharedElementTransitionHelper.startActivityWithSharedElement(this, intent, sharedElement, transitionName);
    }

    /**
     * Start activity with shared element transition and finish current
     */
    public void startActivityWithSharedElementAndFinish(Intent intent, View sharedElement, String transitionName) {
        SharedElementTransitionHelper.startActivityWithSharedElementAndFinish(this, intent, sharedElement, transitionName);
    }

    /**
     * Start activity with multiple shared elements
     */
    public void startActivityWithSharedElements(Intent intent, List<Pair<View, String>> sharedElements) {
        SharedElementTransitionHelper.startActivityWithSharedElements(this, intent, sharedElements);
    }

    /**
     * Start activity with multiple shared elements and finish current
     */
    public void startActivityWithSharedElementsAndFinish(Intent intent, List<Pair<View, String>> sharedElements) {
        SharedElementTransitionHelper.startActivityWithSharedElementsAndFinish(this, intent, sharedElements);
    }

    /**
     * Start activity with explode transition
     */
    public void startActivityWithExplode(Intent intent) {
        SharedElementTransitionHelper.startActivityWithExplode(this, intent);
    }

    /**
     * Start activity with fade transition
     */
    public void startActivityWithFade(Intent intent) {
        SharedElementTransitionHelper.startActivityWithFade(this, intent);
    }

    /**
     * Start activity with slide transition
     */
    public void startActivityWithSlide(Intent intent, int gravity) {
        SharedElementTransitionHelper.startActivityWithSlide(this, intent, gravity);
    }

    /**
     * Start activity with combined transition
     */
    public void startActivityWithCombinedTransition(Intent intent) {
        SharedElementTransitionHelper.startActivityWithCombinedTransition(this, intent);
    }

    /**
     * Start activity with custom transition type
     */
    public void startActivityWithTransition(Intent intent, int transitionType) {
        SharedElementTransitionHelper.startActivityWithTransition(this, intent, transitionType);
    }

    /**
     * Finish activity with shared element transition
     */
    public void finishWithSharedElement() {
        SharedElementTransitionHelper.finishWithSharedElement(this);
    }

    /**
     * Create shared element pair
     */
    public Pair<View, String> createSharedElementPair(View view, String transitionName) {
        return SharedElementTransitionHelper.createSharedElementPair(view, transitionName);
    }

    /**
     * Create list of shared element pairs
     */
    public List<Pair<View, String>> createSharedElementPairs(View[] views, String[] transitionNames) {
        return SharedElementTransitionHelper.createSharedElementPairs(views, transitionNames);
    }

    /**
     * Setup custom shared element transition
     */
    public void setupCustomSharedElementTransition(int duration) {
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

            getWindow().setSharedElementEnterTransition(transitionSet);
            getWindow().setSharedElementExitTransition(transitionSet);
        }
    }

    /**
     * Setup custom content transition
     */
    public void setupCustomContentTransition(int transitionType, int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = null;

            switch (transitionType) {
                case SharedElementTransitionHelper.EXPLODE_TRANSITION:
                    transition = new Explode();
                    break;
                case SharedElementTransitionHelper.FADE_TRANSITION:
                    transition = new Fade();
                    break;
                case SharedElementTransitionHelper.SLIDE_TRANSITION:
                    transition = new Slide(Gravity.END);
                    break;
            }

            if (transition != null) {
                transition.setDuration(duration);
                transition.setInterpolator(new DecelerateInterpolator());

                getWindow().setEnterTransition(transition);
                getWindow().setExitTransition(transition);
            }
        }
    }
}
