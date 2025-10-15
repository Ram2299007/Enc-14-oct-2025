package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

/**
 * Helper class for managing camera permissions and WebView camera compatibility
 * Modified to use WebView camera instead of native camera for video calls
 */
public class CameraPermissionHelper {
    private static final String TAG = "CameraPermissionHelper";

    /**
     * Check if required permissions are granted
     */
    public static boolean hasRequiredPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check if camera is available for WebView usage
     */
    public static boolean isCameraAvailable(Context context) {
        if (!hasRequiredPermissions(context)) {
            Log.w(TAG, "Camera permissions not granted");
            return false;
        }

        // For WebView camera, we only need to check permissions
        // The actual camera access will be handled by the WebView's getUserMedia API
        return true;
    }

    /**
     * Check if camera is available with retry logic for WebView
     */
    public static boolean isCameraAvailableWithRetry(Context context, int maxRetries, long delayMs) {
        if (!hasRequiredPermissions(context)) {
            Log.w(TAG, "Camera permissions not granted");
            return false;
        }

        // For WebView camera, we only need to check permissions
        // The actual camera access will be handled by the WebView's getUserMedia API
        return true;
    }

    /**
     * Get camera information for debugging (WebView compatible)
     */
    public static String getCameraInfo(Context context) {
        StringBuilder info = new StringBuilder();
        
        info.append("Device: ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n");
        info.append("Android Version: ").append(Build.VERSION.RELEASE).append(" (API ").append(Build.VERSION.SDK_INT).append(")\n");
        info.append("Camera Access: WebView getUserMedia API\n");
        info.append("Permissions: ").append(hasRequiredPermissions(context) ? "Granted" : "Not Granted").append("\n");
        
        return info.toString();
    }

    /**
     * Check if device supports specific camera features (WebView compatible)
     */
    public static boolean supportsCameraFeature(Context context, String feature) {
        // For WebView camera, features are determined by the browser's getUserMedia implementation
        // We can only check if permissions are granted
        return hasRequiredPermissions(context);
    }

    /**
     * Show helpful error message based on permission status
     */
    public static void showPermissionError(Context context) {
        if (!hasRequiredPermissions(context)) {
            Toast.makeText(context, 
                "Camera and microphone permissions are required for video calls. Please enable them in Settings > Apps > " + 
                context.getString(android.R.string.unknownName) + " > Permissions", 
                Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check if this is a problematic device (Vivo, Oppo, etc.)
     */
    public static boolean isKnownProblematicDevice() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        String model = Build.MODEL.toLowerCase();
        
        // Known problematic manufacturers
        if (manufacturer.contains("vivo") || 
            manufacturer.contains("oppo") || 
            manufacturer.contains("xiaomi") || 
            manufacturer.contains("oneplus")) {
            Log.w(TAG, "Detected potentially problematic device: " + Build.MANUFACTURER + " " + Build.MODEL);
            return true;
        }
        
        return false;
    }

    /**
     * Get recommended camera constraints for the device (WebView compatible)
     */
    public static String getRecommendedConstraints() {
        if (isKnownProblematicDevice()) {
            // Use more conservative constraints for problematic devices
            return "{\"video\":{\"width\":{\"ideal\":480,\"min\":320,\"max\":640},\"height\":{\"ideal\":360,\"min\":240,\"max\":480},\"facingMode\":\"user\",\"frameRate\":{\"ideal\":24,\"min\":15,\"max\":30}},\"audio\":{\"echoCancellation\":true,\"noiseSuppression\":true,\"autoGainControl\":true}}";
        } else {
            // Use standard constraints for other devices
            return "{\"video\":{\"width\":{\"ideal\":640,\"min\":320,\"max\":1280},\"height\":{\"ideal\":360,\"min\":240,\"max\":720},\"facingMode\":\"user\",\"frameRate\":{\"ideal\":30,\"min\":15,\"max\":60}},\"audio\":{\"echoCancellation\":true,\"noiseSuppression\":true,\"autoGainControl\":true}}";
        }
    }

    /**
     * Wait for camera service to be ready (WebView compatible)
     */
    public static void waitForCameraService(Context context, long timeoutMs) {
        // For WebView camera, we don't need to wait for native camera service
        // The WebView will handle camera initialization through getUserMedia
        Log.d(TAG, "WebView camera doesn't require waiting for native camera service");
    }

    /**
     * Check if WebView camera is supported on this device
     */
    public static boolean isWebViewCameraSupported(Context context) {
        // WebView camera support is generally available on Android 4.4+ (API 19+)
        // and getUserMedia is supported on Android 5.0+ (API 21+)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Get WebView camera constraints for optimal performance
     */
    public static String getWebViewCameraConstraints() {
        return "{\"video\":{\"width\":{\"ideal\":640,\"min\":320,\"max\":1280},\"height\":{\"ideal\":360,\"min\":240,\"max\":720},\"facingMode\":\"user\",\"frameRate\":{\"ideal\":30,\"min\":15,\"max\":60}},\"audio\":{\"echoCancellation\":true,\"noiseSuppression\":true,\"autoGainControl\":true}}";
    }
}
