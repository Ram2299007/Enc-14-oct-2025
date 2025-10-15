package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;

import com.Appzia.enclosure.R;

public class GlobalPermissionPopup {
    
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int MAX_ALLOW_ATTEMPTS = 2; // Try Allow button up to 2 times before showing Settings
    private static int allowAttempts = 0;
    
    public static void showPermissionDialog(Activity activity, PermissionCallback callback) {
        showPermissionDialog(activity, callback, false);
    }
    
    public static void showPermissionDialog(Activity activity, PermissionCallback callback, boolean showSettings) {
        Log.d("GalleryPermission", "=== showPermissionDialog called ===");
        Log.d("GalleryPermission", "showSettings: " + showSettings);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.global_permission_popup, null);
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        
        // Remove default dialog background to hide the rectangle
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        
        // Get views
        Button notNowBtn = dialogView.findViewById(R.id.not_now);
        Button settingsBtn = dialogView.findViewById(R.id.settings);
        
        // Set button text based on whether to show settings or allow
        if (showSettings) {
            settingsBtn.setText("Settings");
        } else {
            settingsBtn.setText("Allow");
        }
        
        // Set click listeners
        notNowBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (callback != null) {
                callback.onPermissionDenied();
            }
        });
        
        settingsBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (showSettings) {
                // Open settings
                openAppSettings(activity);
                if (callback != null) {
                    callback.onSettingsClicked();
                }
            } else {
                // Request permission first
                requestPhotoPermission(activity, new PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        if (callback != null) {
                            callback.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        // Permission denied, show dialog again with Settings button
                        showPermissionDialog(activity, callback, true);
                    }

                    @Override
                    public void onSettingsClicked() {
                        // This won't be called in this flow
                    }
                });
            }
        });
        
        dialog.show();
    }
    
    public static boolean hasPhotoPermission(Context context) {
        boolean hasPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) 
                    == PackageManager.PERMISSION_GRANTED;
            Log.d("GalleryPermission", "Android 13+ - READ_MEDIA_IMAGES permission: " + hasPermission);
            
            // For Android 13+, also check if we have any level of media access
            if (!hasPermission) {
                // Check if we have limited access (this might be detected differently)
                // For now, let's assume if the permission check fails, we should still try to detect limited access
                Log.d("GalleryPermission", "READ_MEDIA_IMAGES failed, but checking for limited access...");
            }
        } else {
            hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    == PackageManager.PERMISSION_GRANTED;
            Log.d("GalleryPermission", "Android <13 - READ_EXTERNAL_STORAGE permission: " + hasPermission);
        }
        Log.d("GalleryPermission", "Final hasPhotoPermission result: " + hasPermission);
        return hasPermission;
    }
    
    public static boolean isPhotoPermissionPermanentlyDenied(Activity activity) {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        
        // Only consider permanently denied if:
        // 1. Permission is denied
        // 2. We can't show rationale (user selected "Don't ask again" or similar)
        // 3. We're absolutely sure there's no way to request it again
        boolean isDenied = ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
        boolean cannotShowRationale = !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        
        // Be very conservative - only show Settings if we're absolutely certain
        return isDenied && cannotShowRationale;
    }
    
    public static void requestPhotoPermission(Activity activity, PermissionCallback callback) {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        
        // Store callback for permission result handling
        permissionCallback = callback;
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
    }
    
    private static PermissionCallback permissionCallback;
    
    public static void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && permissionCallback != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                allowAttempts = 0; // Reset counter on success
                permissionCallback.onPermissionGranted();
            } else {
                permissionCallback.onPermissionDenied();
            }
            permissionCallback = null; // Clear the callback
        }
    }
    
    public static void handleGalleryClick(Activity activity, PermissionCallback callback) {
        if (hasPhotoPermission(activity)) {
            // Permission already granted, proceed with gallery
            allowAttempts = 0; // Reset counter on success
            if (callback != null) {
                callback.onPermissionGranted();
            }
        } else {
            // Permission not granted - try Allow first if we haven't exceeded max attempts
            if (allowAttempts >= MAX_ALLOW_ATTEMPTS || isPhotoPermissionPermanentlyDenied(activity)) {
                // Show Settings if we've tried Allow too many times OR it's permanently denied
                showPermissionDialog(activity, callback, true);
            } else {
                // Try Allow first - increment counter
                allowAttempts++;
                showPermissionDialog(activity, callback, false);
            }
        }
    }
    
    public static void handleGalleryClickWithLimitedAccess(Activity activity, PermissionCallback callback) {
        Log.d("GalleryPermission", "=== handleGalleryClickWithLimitedAccess called ===");
        
        boolean hasPermission = hasPhotoPermission(activity);
        boolean hasLimitedAccess = hasLimitedPhotoAccess(activity);
        
        Log.d("GalleryPermission", "hasPhotoPermission: " + hasPermission);
        Log.d("GalleryPermission", "hasLimitedAccess: " + hasLimitedAccess);
        
        if (hasPermission || hasLimitedAccess) {
            // Permission granted (full or limited) - show gallery directly (no popup)
            Log.d("GalleryPermission", "Permission granted - showing gallery directly (NO POPUP)");
            // Reset attempts counter when permission is granted
            allowAttempts = 0;
            if (callback != null) {
                callback.onPermissionGranted();
            }
        } else {
            // No permission - always show Allow button first, then Settings after multiple denials
            Log.d("GalleryPermission", "No permission - showing permission dialog");
            Log.d("GalleryPermission", "allowAttempts: " + allowAttempts + ", MAX_ALLOW_ATTEMPTS: " + MAX_ALLOW_ATTEMPTS);
            
            // Always try Allow first, only show Settings after multiple denials
            if (allowAttempts >= MAX_ALLOW_ATTEMPTS) {
                Log.d("GalleryPermission", "Multiple denials - showing Settings dialog");
                showPermissionDialog(activity, callback, true);
            } else {
                Log.d("GalleryPermission", "Showing Allow dialog (attempt " + (allowAttempts + 1) + ")");
                allowAttempts++;
                showPermissionDialog(activity, callback, false);
            }
        }
    }
    
    public static boolean hasLimitedPhotoAccess(Activity activity) {
        // Check if user has limited photo access (not full access)
        // For Android 13+, we need to distinguish between limited and full access

        Log.d("GalleryPermission", "=== hasLimitedPhotoAccess called ===");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+, check if we have READ_MEDIA_IMAGES permission
            boolean hasReadMediaImagesPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;

            if (!hasReadMediaImagesPermission) {
                // No READ_MEDIA_IMAGES permission at all
                Log.d("GalleryPermission", "No READ_MEDIA_IMAGES permission - NOT limited access");
                return false;
            }
            
            // We have READ_MEDIA_IMAGES permission, now check if it's limited or full
            try {
                android.content.ContentResolver resolver = activity.getContentResolver();
                android.database.Cursor cursor = resolver.query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{android.provider.MediaStore.Images.Media._ID},
                    null, null, null
                );

                if (cursor == null) {
                    Log.d("GalleryPermission", "Cursor is null - no access");
                    return false;
                }
                
                int totalImages = cursor.getCount();
                cursor.close();
                
                // If we can access some images but not all, it's limited access
                // For now, we'll consider it limited if we can access images but the count is small
                // This is a heuristic - you might need to adjust based on your specific needs
                boolean isLimitedAccess = totalImages > 0 && totalImages < 100; // Adjust threshold as needed
                
                Log.d("GalleryPermission", "Total images accessible: " + totalImages + ", isLimitedAccess: " + isLimitedAccess);
                return isLimitedAccess;

            } catch (Exception e) {
                Log.d("GalleryPermission", "MediaStore query failed: " + e.getMessage());
                return false;
            }
        } else {
            // For older versions, if permission is granted, assume it's full access
            boolean hasPermission = hasPhotoPermission(activity);
            Log.d("GalleryPermission", "Android <13 - hasPermission: " + hasPermission + " (assuming full access)");
            return false; // For older versions, don't show limited access text
        }
    }
    
    public static boolean isLimitedPhotoAccess(Activity activity) {
        // This method should detect if user has limited photo access
        // For now, we'll assume any granted permission is limited access
        // You can implement more sophisticated detection here based on your requirements
        return hasPhotoPermission(activity);
    }
    
    private static void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        SwipeNavigationHelper.startActivityWithSwipe(activity, intent);
    }
    
    public static void resetAllowAttempts() {
        allowAttempts = 0;
    }
    
    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
        void onSettingsClicked();
    }
}
