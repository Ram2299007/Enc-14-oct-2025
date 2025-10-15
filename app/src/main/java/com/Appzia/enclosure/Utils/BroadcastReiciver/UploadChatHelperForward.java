package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadChatHelperForward {
    private static final String TAG = "UploadChatHelper";
    private final Context mContext;
    public String userFTokenKey;
    private final String createdBy;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final Gson gson = new Gson();
    private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();
    private final List<String> pendingSnackbars = Collections.synchronizedList(new ArrayList<>());
    private boolean isShowingSnackbar = false;
    private static final String STORAGE_SUBDIR = "upload_files";
    private String xFilename;

    public UploadChatHelperForward(Context context, String createdBy, String userFTokenKey) {
        this.mContext = context;
        this.createdBy = createdBy;
        this.userFTokenKey = userFTokenKey;
        Log.d(TAG, "UploadChatHelper initialized. CreatedBy: " + createdBy + ", userFTokenKey: " + userFTokenKey);
    }

    public void uploadContent(messageModel model) {
        Log.d(TAG, "uploadContent called. DataType: " + model.getDataType() + ", ModelId: " + model.getModelId() + ", ReceiverUid: " + model.getReceiverUid());

        if (model == null) {
            Log.e(TAG, "Message model is null. Aborting upload.");
            showSnackbar("Failed to forward: Invalid message data");
            return;
        }

        if (model.getDataType() == null || model.getModelId() == null || model.getReceiverUid() == null) {
            Log.e(TAG, "Invalid model data: DataType=" + model.getDataType() + ", ModelId=" + model.getModelId() + ", ReceiverUid=" + model.getReceiverUid());
            showSnackbar("Failed to forward: Missing required fields");
            return;
        }

        try {
            String dataType = model.getDataType();
            Log.d(TAG, "Confirmed DataType: " + dataType);

            // Update xFilename if needed
            xFilename = model.getFileName();
            Log.d(TAG, "xFilename set to: " + xFilename);

            // Start upload
            String finalDataType = dataType;
            mainHandler.post(() -> {
                Log.d(TAG, "Posting startBackgroundService for ModelId: " + model.getModelId());
                startBackgroundService(finalDataType, model);
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to process upload for ModelId: " + model.getModelId() + ": " + e.getMessage(), e);
            showSnackbar("Failed to forward: " + e.getMessage());
        }
    }

    private void startBackgroundService(String uploadType, messageModel model) {
        Log.d(TAG, "startBackgroundService called. UploadType: " + uploadType + ", ModelId: " + model.getModelId());

        // Battery optimization check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            String packageName = mContext.getPackageName();
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                Log.w(TAG, "Battery optimization not ignored for package: " + packageName);
                Intent batteryIntent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                batteryIntent.setData(Uri.parse("package:" + packageName));
                try {
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, batteryIntent);
                    Log.d(TAG, "Battery optimization intent launched.");
                } catch (Exception e) {
                    Log.e(TAG, "Battery optimization intent failed: " + e.getMessage(), e);
                }
            } else {
                Log.d(TAG, "Battery optimization already ignored for package: " + packageName);
            }
        }

        // Prepare Intent for MessageUploadService
        Constant.setSfFunction(mContext);
        Intent serviceIntent = new Intent(mContext, MessageUploadServiceForward.class);
        String modelJson = gson.toJson(model);
        serviceIntent.putExtra("messageModel", modelJson);
        serviceIntent.putExtra("userFTokenKey", userFTokenKey);
        serviceIntent.putExtra("deviceType", "1");
        serviceIntent.putExtra("filePath", "");
        serviceIntent.putExtra("fullImageFilePath", "");
        serviceIntent.putExtra("thumbnailPath", "");
        Log.d(TAG, "Service intent prepared. ModelJson length: " + modelJson.length() + ", userFTokenKey: " + userFTokenKey);

        // Start the service
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(serviceIntent);
                Log.d(TAG, "Foreground service started for ModelId: " + model.getModelId());
            } else {
                mContext.startService(serviceIntent);
                Log.d(TAG, "Service started for ModelId: " + model.getModelId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to start service for ModelId: " + model.getModelId() + ": " + e.getMessage(), e);
            showSnackbar("Failed to start upload service: " + e.getMessage());
        }
    }

    private String getFileExtension(Uri uri) {
        Log.d(TAG, "Getting file extension for URI: " + uri);
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            Log.d(TAG, "Extension from URL: " + extension);
            return extension;
        }
        String mimeType = mContext.getContentResolver().getType(uri);
        extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        Log.d(TAG, "Extension from MIME type: " + extension);
        return extension != null ? extension : "";
    }

    private String safeString(String input) {
        return input != null ? input : "";
    }

    private void showSnackbar(String message) {
        Log.d(TAG, "Showing Snackbar: " + message);
        mainHandler.post(() -> {
            if (!(mContext instanceof Activity)) {
                Log.w(TAG, "Context is not an Activity, skipping Snackbar");
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                return;
            }
            pendingSnackbars.add(message);
            if (isShowingSnackbar) {
                Log.d(TAG, "Snackbar pending, queue size: " + pendingSnackbars.size());
                return;
            }
            displayNextSnackbar();
        });
    }

    private void displayNextSnackbar() {
        if (pendingSnackbars.isEmpty()) {
            isShowingSnackbar = false;
            Log.d(TAG, "No pending Snackbars");
            return;
        }
        isShowingSnackbar = true;
        String message = pendingSnackbars.remove(0);
        Log.d(TAG, "Displaying Snackbar: " + message);
        int duration = Snackbar.LENGTH_LONG;
        Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), message, duration);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                Log.d(TAG, "Snackbar dismissed");
                displayNextSnackbar();
            }
        });
        snackbar.show();
    }
}