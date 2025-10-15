package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.text.TextUtils;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class MessageUploadService extends Service {
    private static final String TAG = "MessageUploadService";
    private static final String CHANNEL_ID = "UploadChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String CREATE_INDIVIDUAL_CHATTING = Webservice.BASE_URL + "create_individual_chatting";
    private StorageReference storageReference;
    private String modelId;
    private Handler mainHandler;
    private ExecutorService executor;
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    // Retry helper for eventual consistency issues when fetching download URLs immediately after upload
    private void getDownloadUrlWithRetry(StorageReference ref,
                                         int attempt,
                                         int maxAttempts,
                                         long delayMs,
                                         com.google.android.gms.tasks.OnSuccessListener<Uri> onSuccess,
                                         com.google.android.gms.tasks.OnFailureListener onFailure) {
        ref.getDownloadUrl()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(e -> {
                    boolean notFound = (e instanceof StorageException) &&
                            ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND;
                    if (notFound && attempt < maxAttempts) {
                        long nextDelay = Math.min(delayMs * 2, 2000); // cap delay to 2s
                        Log.w(TAG, "getDownloadUrl retry " + attempt + "/" + maxAttempts + " after " + delayMs + "ms for ref: " + ref.getPath());
                        mainHandler.postDelayed(() ->
                                getDownloadUrlWithRetry(ref, attempt + 1, maxAttempts, nextDelay, onSuccess, onFailure), delayMs);
                    } else {
                        onFailure.onFailure(e);
                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        storageReference = FirebaseStorage.getInstance().getReference(Constant.CHAT);
        mainHandler = new Handler(getMainLooper());
        executor = Executors.newSingleThreadExecutor();
        createNotificationChannel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ImageUpload", "=== MessageUploadService onStartCommand ===");
        if (intent == null) {
            Log.e("ImageUpload", "Intent is null, stopping service");
            showNotification("Upload failed: Null intent", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        // Extract Intent extras
        String uploadType = intent.getStringExtra("uploadType");
        String senderId = intent.getStringExtra("uid");
        String receiverUid = intent.getStringExtra("receiverUid");
        String message = intent.getStringExtra("message");
        String dataType = intent.getStringExtra("dataType");
        modelId = intent.getStringExtra("modelId");
        String userName = intent.getStringExtra("userName");
        String sentTime = intent.getStringExtra("time");
        String extension = intent.getStringExtra("extension");
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String micPhoto = intent.getStringExtra("micPhoto");
        String miceTiming = intent.getStringExtra("miceTiming");
        String filePath = intent.getStringExtra("filePath");
        String fullImageFilePath = intent.getStringExtra("fullImageFilePath");
        String thumbnailPath = intent.getStringExtra("thumbnailPath");
        String fileNameThumbnail = intent.getStringExtra("fileNameThumbnail");
        String fileName = intent.getStringExtra("fileName");
        String userFTokenKey = intent.getStringExtra("userFTokenKey");
        String deviceType = intent.getStringExtra("deviceType");
        String replytextData = intent.getStringExtra("replytextData");
        String replyKey = intent.getStringExtra("replyKey");
        String replyType = intent.getStringExtra("replyType");
        String replyOldData = intent.getStringExtra("replyOldData");
        String replyCrtPostion = intent.getStringExtra("replyCrtPostion");
        String forwaredKey = intent.getStringExtra("forwaredKey");
        String groupName = intent.getStringExtra("groupName");
        String caption = intent.getStringExtra("caption");
        int notification = intent.getIntExtra("notification", 1);
        String currentDate = intent.getStringExtra("currentDate");
        String docSize = intent.getStringExtra("docSize");
        String thumbnail = intent.getStringExtra("thumbnail");
        String emojiModelJson = intent.getStringExtra("emojiModelJson");
        String emojiCount = intent.getStringExtra("emojiCount");
        String selectionCount = intent.getStringExtra("selectionCount");
        Log.d("SelectionCount", "MessageUploadService: received selectionCount=" + selectionCount);
        
        // Get selectionBunch from intent
        String selectionBunchJson = intent.getStringExtra("selectionBunch");
        Log.d("SelectionBunch", "MessageUploadService: received selectionBunch=" + selectionBunchJson);
        
        //   imageWidthDp,imageHeightDp,aspectRatio
        long timestamp = intent.getLongExtra("timestamp", 0);
        String imageWidthDp = intent.getStringExtra("imageWidthDp");
        String imageHeightDp = intent.getStringExtra("imageHeightDp");
        String aspectRatio = intent.getStringExtra("aspectRatio");
        long fileSize = intent.getLongExtra("fileSize", 0);

        boolean selectionBunchPreUploaded = intent.getBooleanExtra("selectionBunchPreUploaded", false);
        ArrayList<String> selectionBunchFirebaseUrls = intent.getStringArrayListExtra("selectionBunchFirebaseUrls");

        // Parse selectionBunch
        ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> selectionBunch = new ArrayList<>();
        if (selectionBunchJson != null && !selectionBunchJson.isEmpty()) {
            try {
                Type selectionBunchListType = new TypeToken<ArrayList<com.Appzia.enclosure.Model.selectionBunchModel>>() {
                }.getType();
                selectionBunch = gson.fromJson(selectionBunchJson, selectionBunchListType);
                if (selectionBunch == null) {
                    selectionBunch = new ArrayList<>();
                }
                Log.d("SelectionBunch", "Parsed selectionBunch with " + selectionBunch.size() + " items");
            } catch (Exception e) {
                Log.e("SelectionBunch", "Error parsing selectionBunch: " + e.getMessage(), e);
                selectionBunch = new ArrayList<>();
            }
        }

        if (selectionBunchPreUploaded && selectionBunchFirebaseUrls != null && !selectionBunchFirebaseUrls.isEmpty()) {
            for (int i = 0; i < selectionBunch.size() && i < selectionBunchFirebaseUrls.size(); i++) {
                String url = selectionBunchFirebaseUrls.get(i);
                if (url != null && !url.isEmpty()) {
                    selectionBunch.get(i).setImgUrl(url);
                }
            }
        }

        Log.d("ImageUpload", "Received Intent: uploadType=" + uploadType + ", modelId=" + modelId + ", filePath=" + filePath + ", fullImageFilePath=" + fullImageFilePath);
        if (TextUtils.isEmpty(uploadType)) {
            Log.e(TAG, "Upload type is null/empty. Stopping service.");
            showNotification("Upload failed: Invalid upload type", 0);
            stopSelf();
            return START_NOT_STICKY;
        }
        Log.d("ImageUpload", "File path: " + filePath + ", exists: " + (filePath != null && !filePath.isEmpty() ? new File(filePath).exists() : "null or empty"));
        Log.d("ImageUpload", "Full image file path: " + fullImageFilePath + ", exists: " + (fullImageFilePath != null && !fullImageFilePath.isEmpty() ? new File(fullImageFilePath).exists() : "null or empty"));
        Log.d("ImageUpload", "Thumbnail path: " + thumbnailPath + ", exists: " + (thumbnailPath != null && !thumbnailPath.isEmpty() ? new File(thumbnailPath).exists() : "null or empty"));
        Log.d("ImageUpload", "filename " + fileName);
        
        // Enhanced file validation for image uploads
        if (!TextUtils.isEmpty(uploadType) && (uploadType.equals(Constant.img) || uploadType.equals(Constant.camera))) {
            if ((filePath == null || filePath.isEmpty()) && (fullImageFilePath == null || fullImageFilePath.isEmpty())) {
                Log.e("ImageUpload", "Image upload failed: No valid file path provided");
                showNotification("Upload failed: No image file found", 0);
                stopSelf();
                return START_NOT_STICKY;
            }
            
            // Check if at least one file exists
            boolean hasValidFile = false;
            if (filePath != null && !filePath.isEmpty() && new File(filePath).exists()) {
                hasValidFile = true;
                Log.d("ImageUpload", "Using filePath: " + filePath);
            } else if (fullImageFilePath != null && !fullImageFilePath.isEmpty() && new File(fullImageFilePath).exists()) {
                hasValidFile = true;
                Log.d("ImageUpload", "Using fullImageFilePath: " + fullImageFilePath);
            }
            
            if (!hasValidFile) {
                Log.e("ImageUpload", "Image upload failed: No valid image file found");
                showNotification("Upload failed: Image file not found", 0);
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        // Parse emojiModel
        Type emojiListType = new TypeToken<ArrayList<emojiModel>>() {
        }.getType();
        ArrayList<emojiModel> emojiModels = gson.fromJson(emojiModelJson, emojiListType);
        if (emojiModels == null) {
            emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));
        }
        
        // Validate required fields
        if (senderId == null || receiverUid == null || modelId == null) {
            Log.e(TAG, "Missing required fields: senderId=" + senderId + ", receiverUid=" + receiverUid + ", modelId=" + modelId);
            showNotification("Upload failed: Missing required fields", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        Log.e("RTYUIOPCCC", "Caption :" + caption);
        Log.e("RTYUIOPCCC", "Caption :" + message);

        // Initialize messageModel
        messageModel msgModel;
        if (!TextUtils.isEmpty(dataType) && dataType.equals(Constant.Text)) {
            msgModel = new messageModel(
                    senderId, message, sentTime, "", dataType, extension, name, phone,
                    micPhoto, miceTiming, userName, replytextData, replyKey, replyType,
                    replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey,
                    groupName, docSize != null ? docSize : getFormattedFileSize(fileSize),
                    fileName, thumbnail, fileNameThumbnail, "", notification,
                    currentDate, emojiModels, emojiCount, timestamp, imageWidthDp, imageHeightDp, aspectRatio, selectionCount != null ? selectionCount : "1", selectionBunch
            );
        } else if (!TextUtils.isEmpty(dataType) && dataType.equals(Constant.camera)) {

            if (!TextUtils.isEmpty(replyCrtPostion)) {
                msgModel = new messageModel(
                        senderId, message, sentTime, "", Constant.img, extension, name, phone,
                        micPhoto, miceTiming, userName, replytextData, replyKey, replyType,
                        replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey,
                        groupName, docSize != null ? docSize : getFormattedFileSize(fileSize),
                        fileName, thumbnail, fileNameThumbnail, caption, notification,
                        currentDate, emojiModels, emojiCount, timestamp, imageWidthDp, imageHeightDp, aspectRatio, selectionCount != null ? selectionCount : "1", selectionBunch
                );
            } else {
                msgModel = new messageModel(
                        senderId, message, sentTime, "", Constant.img, extension, name, phone,
                        micPhoto, miceTiming, userName, replytextData, replyKey, replyType,
                        replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey,
                        groupName, docSize != null ? docSize : getFormattedFileSize(fileSize),
                        fileName, thumbnail, fileNameThumbnail, caption, notification,
                        currentDate, emojiModels, emojiCount, timestamp, imageWidthDp, imageHeightDp, aspectRatio, selectionCount != null ? selectionCount : "1", selectionBunch
                );
            }

        } else {

            if (!TextUtils.isEmpty(replyCrtPostion)) {
                msgModel = new messageModel(
                        senderId, message, sentTime, fileName, dataType, extension, name, phone,
                        micPhoto, miceTiming, userName, replytextData, replyKey, replyType,
                        replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey,
                        groupName, docSize != null ? docSize : getFormattedFileSize(fileSize),
                        fileName, thumbnail, fileNameThumbnail, caption, notification,
                        currentDate, emojiModels, emojiCount, timestamp, imageWidthDp, imageHeightDp, aspectRatio, selectionCount != null ? selectionCount : "1", selectionBunch
                );
            }else {
                msgModel = new messageModel(
                        senderId, message, sentTime, fileName, dataType, extension, name, phone,
                        micPhoto, miceTiming, userName, replytextData, replyKey, replyType,
                        replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey,
                        groupName, docSize != null ? docSize : getFormattedFileSize(fileSize),
                        fileName, thumbnail, fileNameThumbnail, caption, notification,
                        currentDate, emojiModels, emojiCount, timestamp, imageWidthDp, imageHeightDp, aspectRatio, selectionCount != null ? selectionCount : "1", selectionBunch
                );
            }
        }

        // Debug selectionBunch URLs after model creation
        if (msgModel.getSelectionBunch() != null && !msgModel.getSelectionBunch().isEmpty()) {
            Log.d("SelectionBunch", "Model selectionBunch has " + msgModel.getSelectionBunch().size() + " items:");
            for (int i = 0; i < msgModel.getSelectionBunch().size(); i++) {
                com.Appzia.enclosure.Model.selectionBunchModel bunch = msgModel.getSelectionBunch().get(i);
                Log.d("SelectionBunch", "Item " + i + ": fileName=" + bunch.getFileName() + ", imgUrl=" + bunch.getImgUrl());
            }
        } else {
            Log.d("SelectionBunch", "Model selectionBunch is null or empty");
        }
        
        // Additional debug info
        Log.d("SelectionBunch", "MessageUploadService debug info:");
        Log.d("SelectionBunch", "  - selectionBunchJson from intent: " + (selectionBunchJson != null ? selectionBunchJson.substring(0, Math.min(100, selectionBunchJson.length())) + "..." : "null"));
        Log.d("SelectionBunch", "  - selectionBunchPreUploaded: " + selectionBunchPreUploaded);
        Log.d("SelectionBunch", "  - selectionBunchFirebaseUrls: " + (selectionBunchFirebaseUrls != null ? selectionBunchFirebaseUrls.size() + " items" : "null"));

        // Start foreground notification
        int notificationId = modelId.hashCode();
        Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Message")
                .setContentText("Starting upload...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
        startForeground(notificationId, notification2);

        // Validate file paths
        File fileToUpload = null;
        if (!selectionBunchPreUploaded && !uploadType.equals(Constant.Text) && !uploadType.equals(Constant.contact)) {
            // Enhanced file selection logic with better error handling
            File selectedFile = null;
            
            // Try fullImageFilePath first (usually higher quality)
            if (fullImageFilePath != null && !fullImageFilePath.isEmpty()) {
                File fullImageFile = new File(fullImageFilePath);
                if (fullImageFile.exists() && fullImageFile.length() > 0) {
                    selectedFile = fullImageFile;
                    Log.d("ImageUpload", "Selected fullImageFilePath: " + fullImageFilePath + ", size: " + fullImageFile.length());
                } else {
                    Log.w("ImageUpload", "FullImageFilePath exists but is empty or invalid: " + fullImageFilePath);
                }
            }
            
            // Fallback to filePath if fullImageFilePath is not available
            if (selectedFile == null && filePath != null && !filePath.isEmpty()) {
                File regularFile = new File(filePath);
                if (regularFile.exists() && regularFile.length() > 0) {
                    selectedFile = regularFile;
                    Log.d("ImageUpload", "Selected filePath: " + filePath + ", size: " + regularFile.length());
                } else {
                    Log.w("ImageUpload", "FilePath exists but is empty or invalid: " + filePath);
                }
            }
            
            fileToUpload = selectedFile;
            
            if (fileToUpload == null) {
                Log.e("ImageUpload", "No valid file found. fullImageFilePath=" + fullImageFilePath + ", filePath=" + filePath);
                showNotification("Upload failed: No valid image file found", 0);
                stopSelf();
                return START_NOT_STICKY;
            }
            
            Log.d("ImageUpload", "Final selected file: " + fileToUpload.getAbsolutePath() + ", size: " + fileToUpload.length());
            if (!fileToUpload.canRead()) {
                Log.e("ImageUpload", "File is not readable: " + fileToUpload.getAbsolutePath());
                showNotification("Upload failed: File is not readable", 0);
                stopSelf();
                return START_NOT_STICKY;
            }
            if (fileToUpload.length() > 200 * 1024 * 1024) {
                Log.e("ImageUpload", "File exceeds 200MB: " + fileToUpload.getAbsolutePath());
                showNotification("Upload failed: File exceeds 200MB limit", 0);
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        // Handle upload
        File finalFileToUpload = fileToUpload;
        executor.execute(() -> {
            try {
                if (selectionBunchPreUploaded) {
                    uploadToServer(msgModel, null, null, null, userFTokenKey, deviceType, userFTokenKey);
                } else if (uploadType.equals(Constant.Text)) {
                    // Handle text messages - skip Firebase upload and go directly to server
                    Log.d(TAG, "Handling text upload, skipping Firebase");
                    uploadToServer(msgModel, null, null, null, userFTokenKey, deviceType, userFTokenKey);
                } else if (uploadType.equals(Constant.contact)) {
                    // Handle contact messages - skip Firebase upload and go directly to server
                    Log.d(TAG, "Handling contact upload, skipping Firebase");
                    uploadToServer(msgModel, null, null, null, userFTokenKey, deviceType, userFTokenKey);
                } else if (uploadType.equals(Constant.img) || uploadType.equals(Constant.camera) || uploadType.equals(Constant.doc) ||
                        uploadType.equals(Constant.voiceAudio)) {
                    uploadFileToFirebase(finalFileToUpload, uploadType, msgModel, null, filePath, fileName, userFTokenKey, deviceType, userFTokenKey);
                } else if (uploadType.equals(Constant.video)) {
                    File videoFile = new File(filePath);
                    File thumbnailFile = thumbnailPath != null ? new File(thumbnailPath) : null;
                    if (!videoFile.exists() || videoFile.length() == 0) {
                        throw new IllegalStateException("Video file not found or empty: " + videoFile.getAbsolutePath());
                    }
                    if (thumbnailFile == null || !thumbnailFile.exists() || thumbnailFile.length() == 0) {
                        throw new IllegalStateException("Thumbnail not found or empty: " + (thumbnailPath != null ? thumbnailPath : "null"));
                    }
                    uploadFileToFirebase(videoFile, uploadType, msgModel, thumbnailFile, filePath, fileName, userFTokenKey, deviceType, userFTokenKey);
                } else {
                    throw new IllegalStateException("Unknown uploadType: " + uploadType);
                }
            } catch (Exception e) {
                Log.e(TAG, "Upload error: " + e.getMessage(), e);
                showNotification("Upload failed: " + e.getMessage(), 0);
                if (finalFileToUpload != null && finalFileToUpload.exists()) {
                    //  finalFileToUpload.delete();
                }
                stopSelf();
            }
        });

        return START_STICKY;
    }

    private void uploadFileToFirebase(File file, String uploadType, messageModel model, @Nullable File thumbnailFile,
                                      String originalFilePath, String originalFileName, String userFTokenKey, String deviceType, String fTokenKey) {
        if (!file.exists() || file.length() == 0) {
            Log.e(TAG, "File does not exist or is empty: " + file.getAbsolutePath());
            showNotification("Upload failed: File not found or empty", 0);
            stopSelf();
            return;
        }

        // Generate unique filename with userId prefix to avoid conflicts
        String fileName = model.getUid() + "_" + (originalFileName != null && !originalFileName.isEmpty()
                ? originalFileName : System.currentTimeMillis() + "." + model.getExtension());
        StorageReference fileRef = storageReference.child(fileName);

        // Check if file already exists in Firebase Storage
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // File exists, use the existing URL
            Log.d(TAG, "File already exists in Firebase Storage: " + fileName + ", URL: " + uri.toString());
            String downloadUrl = uri.toString();
            model.setDocument(downloadUrl);
            // Ensure dimension metadata is propagated whenever we already have the file stored
            // Preserve existing image metadata if present; otherwise leave as-is
            if (uploadType.equals(Constant.video) && thumbnailFile != null) {
                String thumbnailName = model.getUid() + "_" + (model.getFileNameThumbnail() != null && !model.getFileNameThumbnail().isEmpty()
                        ? model.getFileNameThumbnail() : "thumbnail_" + System.currentTimeMillis() + ".jpg");
                StorageReference thumbnailRef = storageReference.child(thumbnailName);
                // Check if thumbnail already exists
                thumbnailRef.getDownloadUrl().addOnSuccessListener(thumbUri -> {
                    // Thumbnail exists, use existing URL
                    Log.d(TAG, "Thumbnail already exists: " + thumbnailName + ", URL: " + thumbUri.toString());
                    model.setThumbnail(thumbUri.toString());
                    uploadToServer(model, fileName, thumbnailName, null, userFTokenKey, deviceType, fTokenKey);
                }).addOnFailureListener(e -> {
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        // Thumbnail does not exist, upload it
                        Log.d(TAG, "Thumbnail does not exist, uploading: " + thumbnailName);
                        if (!thumbnailFile.exists()) {
                            Log.e(TAG, "Thumbnail file does not exist: " + thumbnailFile.getAbsolutePath());
                            showNotification("Upload failed: Thumbnail not found", 0);
                            //    if (file.exists()) file.delete();
                            stopSelf();
                            return;
                        }
                        thumbnailRef.putFile(Uri.fromFile(thumbnailFile))
                                .addOnSuccessListener(thumbSnapshot ->
                                        getDownloadUrlWithRetry(thumbnailRef, 1, 5, 300,
                                                thumbUri -> {
                                                    model.setThumbnail(thumbUri.toString());
                                                    uploadToServer(model, fileName, thumbnailName, null, userFTokenKey, deviceType, fTokenKey);
                                                },
                                                downloadError -> {
                                                    Log.e(TAG, "Failed to get thumbnail download URL: " + downloadError.getMessage());
                                                    showNotification("Upload failed: Could not get thumbnail URL", 0);
                                                    stopSelf();
                                                }))
                                .addOnFailureListener(uploadError -> {
                                    Log.e(TAG, "Thumbnail upload failed: " + uploadError.getMessage());
                                    showNotification("Upload failed: " + uploadError.getMessage(), 0);
                                    // if (file.exists()) file.delete();
                                    //    if (thumbnailFile.exists()) thumbnailFile.delete();
                                    stopSelf();
                                });
                    } else {
                        Log.e(TAG, "Error checking thumbnail existence: " + e.getMessage());
                        showNotification("Upload failed: " + e.getMessage(), 0);
                        //   if (file.exists()) file.delete();
                        //  if (thumbnailFile.exists()) thumbnailFile.delete();
                        stopSelf();
                    }
                });
            } else {
                uploadToServer(model, fileName, null, null, userFTokenKey, deviceType, fTokenKey);
            }
        }).addOnFailureListener(e -> {
            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                // File does not exist, proceed with upload
                Log.d(TAG, "File does not exist in Firebase Storage, uploading: " + fileName);
                showNotification("Uploading " + uploadType + "...", 0);
                fileRef.putFile(Uri.fromFile(file))
                        .addOnProgressListener(snapshot -> {
                            int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            showNotification("Uploading " + uploadType + ": " + progress + "%", progress);
                            sendProgressBroadcast(model.getModelId(), progress);
                        })
                        .addOnSuccessListener(taskSnapshot ->
                                getDownloadUrlWithRetry(fileRef, 1, 5, 300,
                                        uri -> {
                            String downloadUrl = uri.toString();
                            model.setDocument(downloadUrl);
                            // Preserve existing image metadata if already set
                            if (uploadType.equals(Constant.video) && thumbnailFile != null) {
                                String thumbnailName = model.getUid() + "_" + (model.getFileNameThumbnail() != null && !model.getFileNameThumbnail().isEmpty()
                                        ? model.getFileNameThumbnail() : "thumbnail_" + System.currentTimeMillis() + ".jpg");
                                StorageReference thumbnailRef = storageReference.child(thumbnailName);
                                thumbnailRef.getDownloadUrl().addOnSuccessListener(thumbUri -> {
                                    // Thumbnail exists, use existing URL
                                    Log.d(TAG, "Thumbnail already exists: " + thumbnailName + ", URL: " + thumbUri.toString());
                                    model.setThumbnail(thumbUri.toString());
                                    uploadToServer(model, fileName, thumbnailName, originalFilePath, userFTokenKey, deviceType, fTokenKey);
                                }).addOnFailureListener(thumbCheckError -> {
                                    if (thumbCheckError instanceof StorageException && ((StorageException) thumbCheckError).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                        // Thumbnail does not exist, upload it
                                        if (!thumbnailFile.exists()) {
                                            Log.e(TAG, "Thumbnail file does not exist: " + thumbnailFile.getAbsolutePath());
                                            showNotification("Upload failed: Thumbnail not found", 0);
                                            //   if (file.exists()) file.delete();
                                            stopSelf();
                                            return;
                                        }
                                        thumbnailRef.putFile(Uri.fromFile(thumbnailFile))
                                                .addOnSuccessListener(thumbSnapshot ->
                                                        getDownloadUrlWithRetry(thumbnailRef, 1, 5, 300,
                                                                thumbUri -> {
                                                                    model.setThumbnail(thumbUri.toString());
                                                                    uploadToServer(model, fileName, thumbnailName, originalFilePath, userFTokenKey, deviceType, fTokenKey);
                                                                },
                                                                thumbDownloadError -> {
                                                                    Log.e(TAG, "Failed to get thumbnail download URL: " + thumbDownloadError.getMessage());
                                                                    showNotification("Upload failed: Could not get thumbnail URL", 0);
                                                                    stopSelf();
                                                                }))
                                                .addOnFailureListener(thumbUploadError -> {
                                                    Log.e(TAG, "Thumbnail upload failed: " + thumbUploadError.getMessage());
                                                    showNotification("Upload failed: " + thumbUploadError.getMessage(), 0);
                                                    //  if (file.exists()) file.delete();
                                                    //  if (thumbnailFile.exists())
                                                    //  thumbnailFile.delete();
                                                    stopSelf();
                                                });
                                    } else {
                                        Log.e(TAG, "Error checking thumbnail existence: " + thumbCheckError.getMessage());
                                        showNotification("Upload failed: " + thumbCheckError.getMessage(), 0);
                                        // if (file.exists()) file.delete();
                                        //      if (thumbnailFile.exists()) thumbnailFile.delete();
                                        stopSelf();
                                    }
                                });
                            } else {
                uploadToServer(model, fileName, null, originalFilePath, userFTokenKey, deviceType, fTokenKey);
                            }
                        },
                                        downloadError -> {
                                            Log.e(TAG, "Failed to get file download URL: " + downloadError.getMessage());
                                            showNotification("Upload failed: Could not get file URL", 0);
                                            stopSelf();
                                        }))
                        .addOnFailureListener(uploadError -> {
                            Log.e(TAG, "File upload failed: " + uploadError.getMessage());
                            showNotification("Upload failed: " + uploadError.getMessage(), 0);
                            //     if (file.exists()) file.delete();
                            stopSelf();
                        });
            } else {
                Log.e(TAG, "Error checking file existence: " + e.getMessage());
                showNotification("Upload failed: " + e.getMessage(), 0);
                // if (file.exists()) file.delete();
                stopSelf();
            }
        });
    }

    private void uploadToServer(messageModel model, @Nullable String fileName, @Nullable String thumbnailName,
                                @Nullable String filePath, String userFTokenKey, String deviceType, String fTokenKey) {
        // Check if selectionBunch has URLs (indicating pre-uploaded files)
        boolean selectionBunchPreUploaded = model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty();
        Log.d("SelectionBunch", "uploadToServer: selectionBunch exists: " + selectionBunchPreUploaded);
        
        if (selectionBunchPreUploaded) {
            // Check if any selectionBunch item has a URL
            boolean hasUrls = false;
            for (com.Appzia.enclosure.Model.selectionBunchModel bunch : model.getSelectionBunch()) {
                if (bunch.getImgUrl() != null && !bunch.getImgUrl().isEmpty()) {
                    hasUrls = true;
                    Log.d("SelectionBunch", "Found URL in selectionBunch: " + bunch.getImgUrl());
                    break;
                }
            }
            selectionBunchPreUploaded = hasUrls;
            Log.d("SelectionBunch", "selectionBunchPreUploaded after URL check: " + selectionBunchPreUploaded);
            
            // Set document to first URL if available and document is empty
            if (hasUrls && (model.getDocument() == null || model.getDocument().isEmpty() || !model.getDocument().startsWith("http"))) {
                String firstUrl = model.getSelectionBunch().get(0).getImgUrl();
                if (firstUrl != null && !firstUrl.isEmpty()) {
                    model.setDocument(firstUrl);
                    Log.d("SelectionBunch", "Set document to first URL: " + firstUrl);
                }
            }
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uid", safeString(model.getUid()))
                .addFormDataPart("friend_id", safeString(model.getReceiverUid()))
                .addFormDataPart("message", safeString(model.getMessage()))
                .addFormDataPart("user_name", safeString(model.getUserName()))
                .addFormDataPart("notification", String.valueOf(1))
                .addFormDataPart("dataType", safeString(model.getDataType()))
                .addFormDataPart("model_id", safeString(model.getModelId()))
                .addFormDataPart("sent_time", safeString(model.getTime()))
                .addFormDataPart("extension", safeString(model.getExtension()))
                .addFormDataPart("name", safeString(model.getName()))
                .addFormDataPart("phone", safeString(model.getPhone()))
                .addFormDataPart("micPhoto", safeString(model.getMicPhoto()))
                .addFormDataPart("miceTiming", safeString(model.getMiceTiming()))
                .addFormDataPart("fTokenKey", safeString(fTokenKey));

        // Handle upload_docs based on file existence in Firebase Storage
        if (model.getDataType().equals(Constant.Text) || model.getDataType().equals(Constant.contact)) {
            builder.addFormDataPart("upload_docs", "");
        } else if (model.getDocument() != null && !model.getDocument().isEmpty()) {
            // File exists in Firebase Storage, send the URL
            Log.d(TAG, "File exists, sending Firebase URL for upload_docs: " + model.getDocument());
            builder.addFormDataPart("upload_docs", model.getDocument());
        } else if (filePath != null && !filePath.isEmpty() && !selectionBunchPreUploaded) {
            // File does not exist, upload the local file
            File file = new File(filePath);
            if (file.exists() && file.length() <= 200 * 1024 * 1024) {
                String mimeType = getMimeType(filePath);
                Log.d(TAG, "Uploading local file for upload_docs: " + file.getAbsolutePath() + ", mimeType: " + mimeType);
                RequestBody fileBody = RequestBody.create(
                        MediaType.parse(mimeType != null ? mimeType : "application/octet-stream"), file);
                ProgressRequestBody progressRequestBody = new ProgressRequestBody(fileBody, progress -> {
                    showNotification("Uploading to server: " + progress + "%", progress);
                    sendProgressBroadcast(model.getModelId(), progress);
                });
                builder.addFormDataPart("upload_docs", file.getName(), progressRequestBody);
            } else {
                Log.e(TAG, "File does not exist or exceeds 200MB: " + filePath);
                builder.addFormDataPart("upload_docs", "");
                showNotification("Upload failed: File does not exist or exceeds 200MB", 0);
                stopSelf();
                return;
            }
        } else {
            Log.w(TAG, "No file to upload and no Firebase URL available");
            builder.addFormDataPart("upload_docs", "");
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(CREATE_INDIVIDUAL_CHATTING)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e(TAG, "Network error: " + e.getMessage());
                //  showNotification("Upload failed: Network error - " + e.getMessage(), 0);
                //  if (filePath != null) new File(filePath).delete();
                stopSelf();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d(TAG, "Server response: " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server request failed: HTTP " + response.code());
                    showNotification("Upload failed: HTTP " + response.code(), 0);
                    //  if (filePath != null) new File(filePath).delete();
                    stopSelf();
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    int status = jsonResponse.getInt("error_code");

                    if (status == 200) {
                        JSONObject data = jsonResponse.getJSONObject("data");


                        String senderRoom = model.getUid() + model.getReceiverUid();
                        String receiverRoom = model.getReceiverUid() + model.getUid();
                        
                        Log.d("FirebaseStructure", "SenderRoom: " + senderRoom + ", ReceiverRoom: " + receiverRoom);
                        Log.d("FirebaseStructure", "ModelID: " + model.getModelId() + ", UID: " + model.getUid() + ", ReceiverUID: " + model.getReceiverUid());

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        
                        // Debug: Check model state before creating maps
                        Log.d("SelectionBunch", "=== DEBUGGING SELECTIONBUNCH BEFORE DATABASE UPDATE ===");
                        Log.d("SelectionBunch", "Model selectionBunch is null: " + (model.getSelectionBunch() == null));
                        Log.d("SelectionBunch", "Model selectionBunch is empty: " + (model.getSelectionBunch() != null && model.getSelectionBunch().isEmpty()));
                        Log.d("SelectionBunch", "Model selectionCount: " + model.getSelectionCount());
                        if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                            Log.d("SelectionBunch", "Model selectionBunch size: " + model.getSelectionBunch().size());
                            for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                                com.Appzia.enclosure.Model.selectionBunchModel bunch = model.getSelectionBunch().get(i);
                                Log.d("SelectionBunch", "Model selectionBunch[" + i + "]: fileName='" + bunch.getFileName() + "', imgUrl='" + bunch.getImgUrl() + "'");
                            }
                        }
                        
                        Map<String, Object> senderMap = model.toMap(); // 🔹 model ला Map मध्ये बदल
                        senderMap.put("timestamp", ServerValue.TIMESTAMP); // ✅ server timestamp लाव

                        // Update selectionBunch with actual Firebase Storage URLs if available
                        if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                            ArrayList<Map<String, Object>> selectionBunchMap = new ArrayList<>();
                            for (com.Appzia.enclosure.Model.selectionBunchModel bunch : model.getSelectionBunch()) {
                                Map<String, Object> bunchMap = bunch.toMap();
                                // Only add imgUrl if it's not already set (preserve individual URLs)
                                if (bunchMap.get("imgUrl") == null || String.valueOf(bunchMap.get("imgUrl")).isEmpty()) {
                                    Log.w("SelectionBunch", "Missing imgUrl for " + bunch.getFileName() + " - this should not happen with pre-uploaded selectionBunch");
                                } else {
                                    Log.d("SelectionBunch", "Preserving existing imgUrl for " + bunch.getFileName() + ": " + bunchMap.get("imgUrl"));
                                }
                                selectionBunchMap.add(bunchMap);
                            }
                            senderMap.put("selectionBunch", selectionBunchMap);
                            Log.d("SelectionBunch", "Updated senderMap with selectionBunch: " + selectionBunchMap.size() + " items");
                        } else {
                            Log.d("SelectionBunch", "No selectionBunch found in model");
                        }

                        Map<String, Object> receiverMap = model.toMap();
                        receiverMap.put("timestamp", ServerValue.TIMESTAMP);
                        
                        // Update selectionBunch with actual Firebase Storage URLs if available
                        if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                            ArrayList<Map<String, Object>> selectionBunchMap = new ArrayList<>();
                            for (com.Appzia.enclosure.Model.selectionBunchModel bunch : model.getSelectionBunch()) {
                                Map<String, Object> bunchMap = bunch.toMap();
                                // Only add imgUrl if it's not already set (preserve individual URLs)
                                if (bunchMap.get("imgUrl") == null || String.valueOf(bunchMap.get("imgUrl")).isEmpty()) {
                                    Log.w("SelectionBunch", "Missing imgUrl for " + bunch.getFileName() + " - this should not happen with pre-uploaded selectionBunch");
                                } else {
                                    Log.d("SelectionBunch", "Preserving existing imgUrl for " + bunch.getFileName() + ": " + bunchMap.get("imgUrl"));
                                }
                                selectionBunchMap.add(bunchMap);
                            }
                            receiverMap.put("selectionBunch", selectionBunchMap);
                            Log.d("SelectionBunch", "Updated receiverMap with selectionBunch: " + selectionBunchMap.size() + " items");
                        } else {
                            Log.d("SelectionBunch", "No selectionBunch found in model for receiver");
                        }

                        // Debug: Log what's being sent to database
                        Log.d("SelectionBunch", "=== FINAL DATABASE UPDATE DEBUG ===");
                        Log.d("SelectionBunch", "SenderMap contains selectionBunch: " + senderMap.containsKey("selectionBunch"));
                        Log.d("SelectionBunch", "ReceiverMap contains selectionBunch: " + receiverMap.containsKey("selectionBunch"));
                        if (senderMap.containsKey("selectionBunch")) {
                            Object selectionBunchObj = senderMap.get("selectionBunch");
                            Log.d("SelectionBunch", "SenderMap selectionBunch type: " + (selectionBunchObj != null ? selectionBunchObj.getClass().getSimpleName() : "null"));
                            Log.d("SelectionBunch", "SenderMap selectionBunch value: " + selectionBunchObj);
                        }
                        if (receiverMap.containsKey("selectionBunch")) {
                            Object selectionBunchObj = receiverMap.get("selectionBunch");
                            Log.d("SelectionBunch", "ReceiverMap selectionBunch type: " + (selectionBunchObj != null ? selectionBunchObj.getClass().getSimpleName() : "null"));
                            Log.d("SelectionBunch", "ReceiverMap selectionBunch value: " + selectionBunchObj);
                        }
                        
                        Map<String, Object> updates = new HashMap<>();
                        updates.put(Constant.CHAT + "/" + senderRoom + "/" + model.getModelId(), senderMap);
                        updates.put(Constant.CHAT + "/" + receiverRoom + "/" + model.getModelId(), receiverMap);


                        database.updateChildren(updates).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String pushKey = database.child(Constant.chattingSocket).child(model.getReceiverUid()).push().getKey();
                                database.child(Constant.chattingSocket).child(model.getReceiverUid()).setValue(pushKey)
                                        .addOnSuccessListener(unused -> {
                                            Intent intent = new Intent("MESSAGE_UPLOADED");
                                            intent.putExtra("modelId", model.getModelId());
                                            LocalBroadcastManager.getInstance(MessageUploadService.this).sendBroadcast(intent);


                                            if (userFTokenKey != null) {
                                                showNotification("Upload successful", 100);

                                                Constant.getSfFuncion(getApplicationContext());
                                                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                                                if (!model.getReceiverUid().equals(uid)) {
                                                    mainHandler.postDelayed(() ->
                                                            sendPushNotification(model, userFTokenKey, deviceType,
                                                                    model.getUid(), model.getUserName(), model.getTime()), 500);
                                                    Log.e(TAG, "sendPushNotification: " + "CLICKED");
                                                } else {
                                                    Log.e(TAG, "sendPushNotification: " + "NOT CLICKED");
                                                }

                                            }
                                            // if (filePath != null) new File(filePath).delete();
                                            if (thumbnailName != null)
                                                new File(thumbnailName).delete();
                                            stopSelf();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Firebase push key sync failed: " + e.getMessage());
                                            // showNotification("Firebase push key sync failed: " + e.getMessage(), 0);
                                            //  if (filePath != null) new File(filePath).delete();
                                            stopSelf();
                                        });
                            } else {
                                Log.e(TAG, "Firebase sync failed: " + task.getException().getMessage());
                                //  showNotification("Firebase sync failed: " + task.getException().getMessage(), 0);
                                //  if (filePath != null) new File(filePath).delete();
                                stopSelf();
                            }
                        });
                    } else if (status == 205) {
                        String errorMessage = jsonResponse.optString("message", "Unknown error");
//                            new Handler(Looper.getMainLooper()).post(() -> {
//                                Toast.makeText(MessageUploadService.this, errorMessage, Toast.LENGTH_SHORT).show();
//                            });
                        stopSelf(); // Stop the service here

                    } else {
                        String errorMessage = jsonResponse.optString("message", "Unknown error");
                        Log.e(TAG, "Server error: " + errorMessage);
                        //  showNotification("Failed to upload message: " + errorMessage, 0);
                        //  if (filePath != null) new File(filePath).delete();
                        stopSelf();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Invalid server response: " + e.getMessage());
                    //showNotification("Upload failed: Invalid server response - " + e.getMessage(), 0);
                    //   if (filePath != null) new File(filePath).delete();
                    stopSelf();
                }
            }
        });
    }

    private String safeString(String input) {
        return input != null ? input : "";
    }

    private String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "mp4":
                return "video/mp4";
            case "pdf":
                return "application/pdf";
            case "mp3":
                return "audio/mpeg";
            default:
                return null;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Upload Notifications",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(String message, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("File Upload")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(progress < 100)
                .setProgress(100, progress, progress == 0);

        Notification notification = builder.build();
        // Safely get a notification ID
        int notificationId;
        if (modelId != null && !modelId.isEmpty()) {
            notificationId = modelId.hashCode();
        } else {
            notificationId = NOTIFICATION_ID; // Use the constant NOTIFICATION_ID (which is 1) as a fallback
        }
        startForeground(notificationId, notification);
    }

    private void sendProgressBroadcast(String modelId, int progress) {
        Intent intent = new Intent("UPLOAD_PROGRESS");
        intent.putExtra("modelId", modelId);
        intent.putExtra("progress", progress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendPushNotification(messageModel model, String userFTokenKey, String deviceType,
                                      String senderId, String userName, String sentTime) {
        String messageBody;
        if (model.getDataType().equals(Constant.Text)) {
            messageBody = model.getMessage();
        } else if (model.getDataType().equals(Constant.img)) {
            messageBody = "You have a new Image";
        } else if (model.getDataType().equals(Constant.contact)) {
            messageBody = "You have a new Contact";
        } else if (model.getDataType().equals(Constant.voiceAudio)) {
            messageBody = "You have a new Audio";
        } else if (model.getDataType().equals(Constant.video)) {
            messageBody = "You have a new Video";
        } else {
            messageBody = "You have a new File";
        }
        Log.d(TAG, "Sending push notification to: " + userFTokenKey + ", body: " + messageBody);
        Webservice.end_notification_api(
                this, userFTokenKey, userName, messageBody, senderId, userName,
                Constant.getSF.getString(Constant.profilePic, ""), sentTime, deviceType,
                model.getUid(), model.getMessage(), model.getTime(), model.getDocument(),
                model.getDataType(), model.getExtension(), model.getName(), model.getPhone(),
                model.getMicPhoto(), model.getMiceTiming(), model.getUserName(),
                model.getReplytextData(), model.getReplyKey(), model.getReplyType(),
                model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(),
                model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(),
                model.getDocSize(), model.getFileName(), model.getThumbnail(),
                model.getFileNameThumbnail(), model.getCaption(), model.getNotification(),
                model.getCurrentDate(),
                model.getSelectionCount());
    }

    private String getFormattedFileSize(long size) {
        if (size <= 0) return "";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    public static class ProgressRequestBody extends RequestBody {
        private final RequestBody requestBody;
        private final ProgressListener progressListener;

        public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(@NonNull BufferedSink sink) throws IOException {
            long contentLength = contentLength();
            Sink forwardingSink = new ForwardingSink(sink) {
                long bytesWritten = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    bytesWritten += byteCount;
                    if (contentLength > 0) {
                        int progress = (int) (100 * bytesWritten / contentLength);
                        progressListener.onProgress(progress);
                    }
                }
            };
            BufferedSink bufferedSink = Okio.buffer(forwardingSink);
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        public interface ProgressListener {
            void onProgress(int progress);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}