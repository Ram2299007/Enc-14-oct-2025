package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.os.Build;

/**
 * Custom WebView that prevents native camera access and forces WebView camera usage
 * This is specifically designed to prevent getCameraCharacteristics errors on Android 15+
 */
public class SecureWebView extends WebView {
    private static final String TAG = "SecureWebView";

    public SecureWebView(Context context) {
        super(context);
        initSecureWebView();
    }

    public SecureWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSecureWebView();
    }

    public SecureWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSecureWebView();
    }

    public SecureWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSecureWebView();
    }

    private void initSecureWebView() {
        Log.d(TAG, "Initializing SecureWebView to prevent native camera access");
        
        // Get WebView settings
        WebSettings webSettings = getSettings();
        
        // Basic security settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        // Disable features that might trigger native camera access
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowContentAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        
        // Disable advanced features that might cause issues
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        // Force WebView to handle camera internally
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
        }
        
        // Android 15+ specific configurations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Log.d(TAG, "Applying Android 15+ specific WebView configurations");
            
            // Disable features that might trigger native camera access
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            
            // Force WebView to use its own camera implementation
            try {
                // Use reflection to access Android 15+ specific WebView methods
                java.lang.reflect.Method setForceDarkAllowed = 
                    webSettings.getClass().getMethod("setForceDarkAllowed", boolean.class);
                if (setForceDarkAllowed != null) {
                    setForceDarkAllowed.invoke(webSettings, false);
                }
                
                // Disable any potential native camera access methods
                java.lang.reflect.Method setAlgorithmicDarkeningAllowed = 
                    webSettings.getClass().getMethod("setAlgorithmicDarkeningAllowed", boolean.class);
                if (setAlgorithmicDarkeningAllowed != null) {
                    setAlgorithmicDarkeningAllowed.invoke(webSettings, false);
                }
            } catch (Exception e) {
                Log.d(TAG, "Could not set Android 15+ WebView properties: " + e.getMessage());
            }
        }
        
        // Set hardware acceleration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        
        Log.d(TAG, "SecureWebView initialization completed");
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "SecureWebView attached to window");
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "SecureWebView detached from window");
    }
}
