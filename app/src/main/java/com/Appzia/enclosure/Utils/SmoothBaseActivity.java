package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.MainApplication;

/**
 * Base activity class that provides smooth navigation transitions
 * Extend this class instead of AppCompatActivity for automatic smooth animations
 */
public abstract class SmoothBaseActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register activity for lifecycle management
        if (getApplication() instanceof MainApplication) {
            ((MainApplication) getApplication()).registerActivity(this);
        }
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
        // Apply default smooth slide animation
        overridePendingTransition(
            R.anim.slide_in_from_right_smooth,
            R.anim.slide_out_to_left_smooth
        );
    }
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        // Apply default smooth slide animation
        overridePendingTransition(
            R.anim.slide_in_from_right_smooth,
            R.anim.slide_out_to_left_smooth
        );
    }
    
    @Override
    public void finish() {
        super.finish();
        // Apply default smooth back animation
        overridePendingTransition(
            R.anim.slide_in_from_left_smooth,
            R.anim.slide_out_to_right_smooth
        );
    }
    
    @Override
    public void onBackPressed() {
        // Use smooth back animation
        SmoothNavigationHelper.handleBackPress(this);
    }
    
    /**
     * Start activity with custom animation
     */
    public void startActivityWithAnimation(Intent intent, int animationType) {
        SmoothNavigationHelper.startActivityWithAnimation(this, intent, animationType);
    }
    
    /**
     * Start activity with custom animation and finish current
     */
    public void startActivityWithAnimation(Intent intent, int animationType, boolean finishCurrent) {
        SmoothNavigationHelper.startActivityWithAnimation(this, intent, animationType, finishCurrent);
    }
    
    /**
     * Start activity with slide from right animation
     */
    public void startActivityWithSlideFromRight(Intent intent) {
        SmoothNavigationHelper.startActivityWithSlideFromRight(this, intent);
    }
    
    /**
     * Start activity with slide from right animation and finish current
     */
    public void startActivityWithSlideFromRight(Intent intent, boolean finishCurrent) {
        SmoothNavigationHelper.startActivityWithSlideFromRight(this, intent, finishCurrent);
    }
    
    /**
     * Start activity with slide from left animation
     */
    public void startActivityWithSlideFromLeft(Intent intent) {
        SmoothNavigationHelper.startActivityWithSlideFromLeft(this, intent);
    }
    
    /**
     * Start activity with slide from left animation and finish current
     */
    public void startActivityWithSlideFromLeft(Intent intent, boolean finishCurrent) {
        SmoothNavigationHelper.startActivityWithSlideFromLeft(this, intent, finishCurrent);
    }
    
    /**
     * Start activity with fade animation
     */
    public void startActivityWithFade(Intent intent) {
        SmoothNavigationHelper.startActivityWithFade(this, intent);
    }
    
    /**
     * Start activity with fade animation and finish current
     */
    public void startActivityWithFade(Intent intent, boolean finishCurrent) {
        SmoothNavigationHelper.startActivityWithFade(this, intent, finishCurrent);
    }
    
    /**
     * Finish with smooth slide to right animation
     */
    public void finishWithSlideToRight() {
        SmoothNavigationHelper.finishWithSlideToRight(this);
    }
    
    /**
     * Finish with smooth fade animation
     */
    public void finishWithFade() {
        SmoothNavigationHelper.finishWithFade(this);
    }
}
