package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.MainApplication;

/**
 * Base activity class that provides simple swipe navigation
 * Extend this class instead of AppCompatActivity for automatic swipe animations
 */
public abstract class SwipeBaseActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Register activity for lifecycle management
        if (getApplication() instanceof MainApplication) {
            ((MainApplication) getApplication()).registerActivity(this);
        }
        
        // Setup swipe gestures after content is set
        setupSwipeGestures();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister activity
        if (getApplication() instanceof MainApplication) {
            ((MainApplication) getApplication()).unregisterActivity(this);
        }
    }
    
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // Apply simple swipe animation
        overridePendingTransition(
            R.anim.slide_in_right_swipe,
            R.anim.slide_out_left_swipe
        );
    }
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        // Apply simple swipe animation
        overridePendingTransition(
            R.anim.slide_in_right_swipe,
            R.anim.slide_out_left_swipe
        );
    }
    
    @Override
    public void finish() {
        super.finish();
        // Apply simple back swipe animation
        overridePendingTransition(
            R.anim.slide_in_left_swipe,
            R.anim.slide_out_right_swipe
        );
    }
    
    @Override
    public void onBackPressed() {
        // Use swipe back animation
        SwipeNavigationHelper.handleBackPress(this);
    }
    
    /**
     * Setup swipe gestures for this activity
     * Override this method to customize gesture behavior
     */
    protected void setupSwipeGestures() {
        SwipeNavigationHelper.setupSwipeGestures(this);
    }
    
    /**
     * Navigate to next activity with swipe
     */
    protected void navigateTo(Class<?> nextActivityClass) {
        SwipeNavigationHelper.QuickNav.next(this, nextActivityClass);
    }
    
    /**
     * Navigate to next activity with swipe and finish current
     */
    protected void navigateToAndFinish(Class<?> nextActivityClass) {
        SwipeNavigationHelper.QuickNav.nextAndFinish(this, nextActivityClass);
    }
    
    /**
     * Navigate back with swipe
     */
    protected void navigateBack() {
        SwipeNavigationHelper.QuickNav.back(this);
    }
    
    /**
     * Navigate back to specific activity with swipe
     */
    protected void navigateBackTo(Class<?> targetActivityClass) {
        SwipeNavigationHelper.QuickNav.backTo(this, targetActivityClass);
    }
}
