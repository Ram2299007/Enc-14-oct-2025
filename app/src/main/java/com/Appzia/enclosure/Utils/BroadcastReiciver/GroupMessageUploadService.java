package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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

public class GroupMessageUploadService extends Service {
    private static final String TAG = "GroupMessageUploadService";
    private static final String CHANNEL_ID = "group_message_upload_channel";
    private static final String CREATE_GROUP_CHATTING = Webservice.BASE_URL + "create_group_chatting";
    private ExecutorService executor;
    private Handler mainHandler;
    private String modelId;
    private int notificationId;
    private NotificationManager notificationManager;
    private CancelReceiver cancelReceiver;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        storageReference = FirebaseStorage.getInstance().getReference().child(Constant.CHAT);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.GROUPCHAT);
        createNotificationChannel();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "User not authenticated for Realtime Database operations");
        } else {
            Log.d(TAG, "Authenticated user: " + auth.getCurrentUser().getUid());
            auth.getCurrentUser().getIdToken(true).addOnFailureListener(e ->
                    Log.e(TAG, "Failed to refresh auth token: " + e.getMessage()));
        }

        cancelReceiver = new CancelReceiver();
        IntentFilter filter = new IntentFilter("CANCEL_GROUP_UPLOAD");
        registerReceiver(cancelReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    // Retry helper for eventual consistency when fetching download URLs after upload
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
                        long nextDelay = Math.min(delayMs * 2, 2000); // cap at 2s
                        Log.w(TAG, "getDownloadUrl retry " + attempt + "/" + maxAttempts + " after " + delayMs + "ms for ref: " + ref.getPath());
                        mainHandler.postDelayed(() ->
                                getDownloadUrlWithRetry(ref, attempt + 1, maxAttempts, nextDelay, onSuccess, onFailure), delayMs);
                    } else {
                        onFailure.onFailure(e);
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.e(TAG, "Intent is null, stopping service");
            showNotification("Upload failed: Null intent", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        String uploadType = intent.getStringExtra("uploadType");
        String senderId = intent.getStringExtra("uid");
        String groupId = intent.getStringExtra("group_id");
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
        String createdBy = intent.getStringExtra("created_by");
        String userFTokenKey = intent.getStringExtra("userFTokenKey");
        
        // Set fallback values for empty required parameters
        if (userName == null || userName.isEmpty()) {
            userName = "User"; // Default user name
        }
        if (userFTokenKey == null || userFTokenKey.isEmpty()) {
            userFTokenKey = "default_token"; // Default token
        }
        
        // Make final for lambda expressions
        final String finalUserFTokenKey = userFTokenKey;
        String deviceType = intent.getStringExtra("deviceType");
        String replytextData = intent.getStringExtra("replytextData");
        String replyKey = intent.getStringExtra("replyKey");
        String replyType = intent.getStringExtra("replyType");
        String replyOldData = intent.getStringExtra("replyOldData");
        String replyCrtPostion = intent.getStringExtra("replyCrtPostion");
        String forwaredKey = intent.getStringExtra("forwaredKey");
        String groupName = intent.getStringExtra("groupName");
        String caption = intent.getStringExtra("caption");
        ArrayList<String> selectionBunchFilePaths = intent.getStringArrayListExtra("selectionBunchFilePaths");
        String selectionBunchJson = intent.getStringExtra("selectionBunchJson");
        boolean selectionBunchPreUploaded = intent.getBooleanExtra("selectionBunchPreUploaded", false);
        ArrayList<String> selectionBunchFirebaseUrls = intent.getStringArrayListExtra("selectionBunchFirebaseUrls");
        int notification = intent.getIntExtra("notification", 1);
        String currentDate = intent.getStringExtra("currentDate");
        String emojiModelJson = intent.getStringExtra("emojiModelJson");
        String emojiCount = intent.getStringExtra("emojiCount");
        String selectionCount = intent.getStringExtra("selectionCount");
        Log.d("SelectionCount", "GroupMessageUploadService: received selectionCount=" + selectionCount);
        //    serviceIntent.putExtra("imageWidthDp", imageWidthDp);
        //        serviceIntent.putExtra("imageHeightDp", imageHeightDp);
        //        serviceIntent.putExtra("aspectRatio", aspectRatio);
        long timestamp;
        try {
            timestamp = intent.getLongExtra("timestamp", 0);
        } catch (ClassCastException e) {
            // Handle case where timestamp is passed as Integer instead of Long
            Integer timestampInt = intent.getIntExtra("timestamp", 0);
            timestamp = timestampInt.longValue();
            Log.d(TAG, "Converted timestamp from Integer to Long: " + timestamp);
        }
        String docSize = intent.getStringExtra("docSize");
        String imageWidthDp = intent.getStringExtra("imageWidthDp");
        String imageHeightDp = intent.getStringExtra("imageHeightDp");
        String aspectRatio = intent.getStringExtra("aspectRatio");
        long fileSize = intent.getIntExtra("fileSize", 0);

        // Debug logs for Intent extras
        Log.d(TAG, "Received Intent: uploadType=" + uploadType + ", senderId=" + senderId + ", groupId=" + groupId +
                ", message=" + message + ", modelId=" + modelId + ", dataType=" + dataType);
        Log.d(TAG, "Received Intent: userName=" + userName + ", userFTokenKey=" + userFTokenKey);
        Log.d(TAG, "filePath: " + filePath);
        Log.d(TAG, "fullImageFilePath: " + fullImageFilePath);
        Log.d(TAG, "thumbnailPath: " + thumbnailPath);
        Log.d(TAG, "fileSize: " + fileSize);
        Log.d(TAG, "fileName: " + fileName);
        Log.d(TAG, "groupId: " + groupId + ", modelId: " + modelId);

        Gson gson = new Gson();
        ArrayList<emojiModel> emojiModels;
        try {
            Type emojiListType = new TypeToken<ArrayList<emojiModel>>() {}.getType();
            emojiModels = gson.fromJson(emojiModelJson, emojiListType);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse emojiModelJson with TypeToken, using fallback: " + e.getMessage());
            emojiModels = null;
        }
        if (emojiModels == null) {
            emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));
        }

        // Debug all Intent extras to help identify missing groupId
        Log.d(TAG, "All Intent extras:");
        for (String key : intent.getExtras().keySet()) {
            Object value = intent.getExtras().get(key);
            Log.d(TAG, "  " + key + " = " + value + " (type: " + (value != null ? value.getClass().getSimpleName() : "null") + ")");
        }
        
        // Try to get groupId from alternative sources if it's null
        if (groupId == null) {
            Log.w(TAG, "groupId is null, trying alternative sources...");
            groupId = intent.getStringExtra("grpIdKey");
            if (groupId == null) {
                groupId = intent.getStringExtra("receiverUid");
                Log.d(TAG, "Tried receiverUid as groupId: " + groupId);
            }
            if (groupId == null) {
                Log.e(TAG, "Could not find groupId from any source!");
            } else {
                Log.d(TAG, "Found groupId from alternative source: " + groupId);
            }
        }
        
        if (senderId == null || groupId == null || modelId == null) {
            Log.e(TAG, "Missing required fields: senderId=" + senderId + ", groupId=" + groupId + ", modelId=" + modelId);
            showNotification("Upload failed: Missing required fields", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        group_messageModel groupModel;
        if (dataType.equals(Constant.doc)) {
            groupModel = new group_messageModel(
                    senderId, message, sentTime, "", fileName, extension, name, phone,
                    miceTiming, micPhoto, createdBy, userName, modelId, groupId,
                    docSize, fileName, "", fileNameThumbnail, caption, currentDate,imageWidthDp,imageHeightDp,aspectRatio, selectionCount != null ? selectionCount : "1"
            );
        } else if (dataType.equals(Constant.voiceAudio)) {
            Constant.getSfFuncion(getApplicationContext());
            groupModel = new group_messageModel(
                    senderId, message, sentTime, "", dataType, extension, name, phone,
                    miceTiming, Constant.getSF.getString(Constant.profilePic, ""), createdBy, userName, modelId, groupId,
                    docSize, fileName, "", fileNameThumbnail, caption, currentDate,imageWidthDp,imageHeightDp,aspectRatio, selectionCount != null ? selectionCount : "1"
            );
        } else if (dataType.equals(Constant.Text)) {
            groupModel = new group_messageModel(
                    senderId, caption, sentTime, "", dataType, extension, name, phone,
                    miceTiming, micPhoto, createdBy, userName, modelId, groupId,
                    docSize, fileName, "", fileNameThumbnail, "", currentDate,imageWidthDp,imageHeightDp,aspectRatio, "1"
            );
        } else {
            groupModel = new group_messageModel(
                    senderId, message, sentTime, "", dataType, extension, name, phone,
                    miceTiming, micPhoto, createdBy, userName, modelId, groupId,
                    docSize, fileName, "", fileNameThumbnail, caption, currentDate,imageWidthDp,imageHeightDp,aspectRatio, selectionCount != null ? selectionCount : "1"
            );
        }

        messageModel msgModel = new messageModel();
        msgModel.setUid(senderId);
        msgModel.setMessage(message);
        msgModel.setTime(sentTime);
        msgModel.setDocument("");
        msgModel.setDataType(dataType);
        msgModel.setExtension(extension);
        msgModel.setName(name);
        msgModel.setPhone(phone);
        msgModel.setMiceTiming(miceTiming);
        msgModel.setMicPhoto(micPhoto);
        msgModel.setUserName(userName);
        msgModel.setModelId(modelId);
        msgModel.setReceiverUid(groupId);
        msgModel.setDocSize(docSize);
        msgModel.setFileName(fileName);
        msgModel.setThumbnail("");
        msgModel.setFileNameThumbnail(fileNameThumbnail);
        msgModel.setCaption(caption);
        msgModel.setCurrentDate(currentDate);
        msgModel.setEmojiModel(emojiModels);
        msgModel.setEmojiCount(emojiCount);
        msgModel.setTimestamp(timestamp);
        msgModel.setReplytextData(replytextData);
        msgModel.setReplyKey(replyKey);
        msgModel.setReplyType(replyType);
        msgModel.setReplyOldData(replyOldData);
        msgModel.setReplyCrtPostion(replyCrtPostion);
        msgModel.setForwaredKey(forwaredKey);
        msgModel.setGroupName(groupName);
        msgModel.setNotification(notification);
        msgModel.setImageWidth(imageWidthDp);
        msgModel.setImageHeight(imageHeightDp);
        msgModel.setAspectRatio(aspectRatio);
        msgModel.setSelectionCount(selectionCount != null ? selectionCount : "1");

        // Attach selectionBunch from JSON if provided (ensures Firebase write includes it)
        try {
            if (selectionBunchJson != null && !selectionBunchJson.isEmpty()) {
                Type bunchType = new TypeToken<ArrayList<selectionBunchModel>>() {}.getType();
                ArrayList<selectionBunchModel> bunch = new Gson().fromJson(selectionBunchJson, bunchType);
                if (bunch != null && !bunch.isEmpty()) {
                    groupModel.setSelectionBunch(new ArrayList<>(bunch));
                    msgModel.setSelectionBunch(new ArrayList<>(bunch));
                    Log.d("SelectionBunch", "Service attached selectionBunch size=" + bunch.size());
                } else {
                    Log.d("SelectionBunch", "selectionBunchJson parsed but empty");
                }
            } else {
                Log.d("SelectionBunch", "No selectionBunchJson provided");
            }
        } catch (Exception e) {
            Log.e("SelectionBunch", "Failed to parse selectionBunchJson: " + e.getMessage(), e);
        }




        messagemodel2 model2 = new messagemodel2(
                msgModel.getUid(),
                msgModel.getMessage(),
                msgModel.getTime(),
                msgModel.getDocument(),
                msgModel.getDataType(),
                msgModel.getExtension(),
                msgModel.getName(),
                msgModel.getPhone(),
                msgModel.getMicPhoto(),
                msgModel.getMiceTiming(),
                msgModel.getUserName(),
                msgModel.getReplytextData(),
                msgModel.getReplyKey(),
                msgModel.getReplyType(),
                msgModel.getReplyOldData(),
                msgModel.getReplyCrtPostion(),
                msgModel.getModelId(),
                msgModel.getReceiverUid(),
                msgModel.getForwaredKey(),
                msgModel.getGroupName(),
                msgModel.getDocSize(),
                msgModel.getFileName(),
                msgModel.getThumbnail(),
                msgModel.getFileNameThumbnail(),
                msgModel.getCaption(),
                msgModel.getNotification(),
                msgModel.getCurrentDate(),
                msgModel.getEmojiModel(),
                msgModel.getEmojiCount(),
                msgModel.getTimestamp(),
                0,msgModel.getImageWidth(),msgModel.getImageHeight(),msgModel.getAspectRatio(),
                msgModel.getSelectionCount()
        );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//        try {
//            new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//        } catch (Exception e) {
//            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//        }

        notificationId = modelId.hashCode();
        Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Group Message")
                .setContentText("Starting upload...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(notificationId, notification2, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(notificationId, notification2);
        }

        String selectedFilePath = fileSize > 200 * 1024 * 1024 ? fullImageFilePath : filePath;
        Log.d(TAG, "selectedFilePath: " + selectedFilePath);

        executor.execute(() -> {
            Log.d(TAG, "Executing upload for uploadType: " + uploadType);
            if (selectionBunchPreUploaded && selectionBunchFirebaseUrls != null && !selectionBunchFirebaseUrls.isEmpty()) {
                performSelectionBunchUpload(selectionBunchFirebaseUrls, groupModel, msgModel, finalUserFTokenKey, deviceType);
            } else if (selectionBunchFilePaths != null && !selectionBunchFilePaths.isEmpty()) {
                uploadSelectionBunchFiles(selectionBunchFilePaths, groupModel, msgModel, finalUserFTokenKey, deviceType);
            } else if (uploadType.equals(Constant.Text)) {
                handleTextUpload(groupModel, msgModel, finalUserFTokenKey, deviceType);
            } else if (uploadType.equals(Constant.contact)) {
                handleContactUpload(groupModel, msgModel, finalUserFTokenKey, deviceType);
            } else if (uploadType.equals(Constant.video)) {
                if (selectedFilePath == null || !validateFile(selectedFilePath, 200 * 1024 * 1024)) {
                    Log.e(TAG, "Invalid video path: " + selectedFilePath);
                    showNotification("Upload failed: Invalid video file", 0);
                    stopSelf();
                    return;
                }
                if (thumbnailPath == null || !validateFile(thumbnailPath, 200 * 1024 * 1024)) {
                    Log.e(TAG, "Invalid thumbnail path: " + thumbnailPath);
                    showNotification("Upload failed: Invalid thumbnail file", 0);
                    stopSelf();
                    return;
                }
                uploadVideoToFirebaseStorage(selectedFilePath, thumbnailPath, fileName, fileNameThumbnail, groupModel, msgModel, finalUserFTokenKey, deviceType);
            } else if (uploadType.equals(Constant.img) || uploadType.equals(Constant.doc) ||
                    uploadType.equals(Constant.camera) || uploadType.equals(Constant.voiceAudio)) {
                if (selectedFilePath == null || !validateFile(selectedFilePath, 200 * 1024 * 1024)) {
                    Log.e(TAG, "Invalid file path: " + selectedFilePath);
                    showNotification("Upload failed: Invalid file path", 0);
                    stopSelf();
                    return;
                }
                uploadToFirebaseStorage(selectedFilePath, fileName, groupModel, msgModel, finalUserFTokenKey, deviceType, dataType);
            } else {
                Log.e(TAG, "Invalid upload type: " + uploadType);
                showNotification("Upload failed: Invalid upload type", 0);
                stopSelf();
            }
        });

        return START_STICKY;
    }

    private void handleTextUpload(group_messageModel groupModel, messageModel msgModel,
                                  String userFTokenKey, String deviceType) {
        Log.d(TAG, "Handling text upload, skipping Firebase");
        performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
    }

    private boolean validateFile(String path, long maxSize) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Invalid file path: " + path);
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG, "File does not exist: " + path);
            return false;
        }
        if (file.length() > maxSize) {
            Log.e(TAG, "File size exceeds limit: " + file.length() + " > " + maxSize);
            return false;
        }
        return true;
    }

    private void writeToRealtimeDatabase(group_messageModel groupModel, messageModel msgModel) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.e(TAG, "User not authenticated for database write");
            showNotification("Failed to save message: User not authenticated", 0);
            return;
        }

        String chatKey = (msgModel.getUid() + msgModel.getReceiverUid()).replaceAll("[.#$\\[\\]]", "_");
        DatabaseReference messageRef = databaseReference.child(chatKey).child(msgModel.getModelId());
        messageRef.setValue(groupModel.toMap())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully wrote to Realtime Database at: " +
                            Constant.GROUPCHAT + "/" + chatKey + "/" + msgModel.getModelId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to write to Realtime Database: " + e.getMessage(), e);
                    showNotification("Failed to save message: " + e.getMessage(), 0);
                });
    }

    private void uploadVideoToFirebaseStorage(String filePath, String thumbnailPath, String fileName, String fileNameThumbnail,
                                              group_messageModel groupModel, messageModel msgModel,
                                              String userFTokenKey, String deviceType) {
        File videoFile = new File(filePath);
        File thumbnailFile = new File(thumbnailPath);

        if (!videoFile.exists() || videoFile.length() > 200 * 1024 * 1024 || !thumbnailFile.exists()) {
            Log.e(TAG, "Video or thumbnail invalid: videoExists=" + videoFile.exists() + ", videoSize=" + videoFile.length() + ", thumbnailExists=" + thumbnailFile.exists());
            showNotification("Upload failed: Video or thumbnail file does not exist or video exceeds 200MB", 0);
            stopSelf();
            return;
        }

        // Use a strong unique pattern to avoid collisions across users/groups/messages
        // <uid>_<groupId>_<modelId>_<sanitizedOriginalName or generated>
        String baseVideoName = (fileName != null && !fileName.isEmpty()) ? fileName.replaceAll("[^A-Za-z0-9._-]", "_") : (System.currentTimeMillis() + "." + msgModel.getExtension());
        String baseThumbName = (fileNameThumbnail != null && !fileNameThumbnail.isEmpty()) ? fileNameThumbnail.replaceAll("[^A-Za-z0-9._-]", "_") : ("thumb_" + System.currentTimeMillis() + ".jpg");
        String videoFileName = msgModel.getUid() + "_" + msgModel.getReceiverUid() + "_" + msgModel.getModelId() + "_" + baseVideoName;
        String thumbFileName = msgModel.getUid() + "_" + msgModel.getReceiverUid() + "_" + msgModel.getModelId() + "_" + baseThumbName;
        StorageReference videoRef = storageReference.child(videoFileName);
        StorageReference thumbnailRef = storageReference.child(thumbFileName);

        // Check if video file already exists
        videoRef.getDownloadUrl().addOnSuccessListener(videoDownloadUri -> {
            // Video exists, use existing URL
            Log.d(TAG, "Video already exists in Firebase Storage: " + videoFileName + ", URL: " + videoDownloadUri);
            groupModel.setDocument(videoDownloadUri.toString());
            msgModel.setDocument(videoDownloadUri.toString());

            // Check if thumbnail exists
            thumbnailRef.getDownloadUrl().addOnSuccessListener(thumbnailDownloadUri -> {
                // Thumbnail exists, use existing URL
                Log.d(TAG, "Thumbnail already exists: " + thumbFileName + ", URL: " + thumbnailDownloadUri);
                groupModel.setThumbnail(thumbnailDownloadUri.toString());
                msgModel.setThumbnail(thumbnailDownloadUri.toString());
                String baseThumbOnly = thumbFileName.substring(thumbFileName.lastIndexOf('_') + 1);
                groupModel.setFileNameThumbnail(baseThumbOnly);
                msgModel.setFileNameThumbnail(baseThumbOnly);
                performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
            }).addOnFailureListener(e -> {
                if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    // Thumbnail does not exist, upload it
                    Log.d(TAG, "Thumbnail does not exist, uploading: " + thumbFileName);
                    Uri thumbnailUri = Uri.fromFile(thumbnailFile);
                    UploadTask thumbnailUploadTask = thumbnailRef.putFile(thumbnailUri);
                    thumbnailUploadTask.addOnProgressListener(thumbSnapshot -> {
                        double thumbProgress = (100.0 * thumbSnapshot.getBytesTransferred()) / thumbSnapshot.getTotalByteCount();
                        showNotification("Uploading thumbnail to Firebase: " + (int) thumbProgress + "%", (int) thumbProgress);
                        Intent progressIntent = new Intent("GROUP_UPLOAD_PROGRESS");
                        progressIntent.putExtra("modelId", modelId);
                        progressIntent.putExtra("progress", (int) thumbProgress);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(progressIntent);
                    }).addOnSuccessListener(thumbSnapshot -> {
                        getDownloadUrlWithRetry(thumbnailRef, 1, 5, 300,
                                thumbnailDownloadUri -> {
                                    groupModel.setThumbnail(thumbnailDownloadUri.toString());
                                    msgModel.setThumbnail(thumbnailDownloadUri.toString());
                                    // Use the original fileNameThumbnail from intent (without Firebase prefix)
                                    groupModel.setFileNameThumbnail(fileNameThumbnail);
                                    msgModel.setFileNameThumbnail(fileNameThumbnail);
                                    Log.d(TAG, "Thumbnail uploaded: " + thumbnailDownloadUri);
                                    performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
                                },
                                thumbUrlError -> {
                                    Log.e(TAG, "Thumbnail URL fetch failed: " + thumbUrlError.getMessage(), thumbUrlError);
                                    showNotification("Thumbnail upload failed: " + thumbUrlError.getMessage(), 0);
                                    stopSelf();
                                });
                    }).addOnFailureListener(thumbUploadError -> {
                        Log.e(TAG, "Thumbnail upload failed: " + thumbUploadError.getMessage(), thumbUploadError);
                        showNotification("Thumbnail upload failed: " + thumbUploadError.getMessage(), 0);
                        stopSelf();
                    });
                    mainHandler.postDelayed(() -> {
                        if (thumbnailUploadTask.isInProgress()) {
                            thumbnailUploadTask.cancel();
                            Log.e(TAG, "Thumbnail upload timed out");
                        }
                    }, 60_000);
                } else {
                    Log.e(TAG, "Thumbnail check failed: " + e.getMessage(), e);
                    showNotification("Thumbnail check failed: " + e.getMessage(), 0);
                    stopSelf();
                }
            });
        }).addOnFailureListener(e -> {
            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                // Video does not exist, upload it
                Log.d(TAG, "Video does not exist in Firebase Storage, uploading: " + videoFileName);
                Uri videoUri = Uri.fromFile(videoFile);
                UploadTask videoUploadTask = videoRef.putFile(videoUri);
                videoUploadTask.addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    showNotification("Uploading video to Firebase: " + (int) progress + "%", (int) progress);
                    Intent progressIntent = new Intent("GROUP_UPLOAD_PROGRESS");
                    progressIntent.putExtra("modelId", modelId);
                    progressIntent.putExtra("progress", (int) progress);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(progressIntent);
                }).addOnSuccessListener(taskSnapshot -> {
                    getDownloadUrlWithRetry(videoRef, 1, 5, 300,
                            videoDownloadUri -> {
                                groupModel.setDocument(videoDownloadUri.toString());
                                msgModel.setDocument(videoDownloadUri.toString());
                                Log.d(TAG, "Video uploaded successfully: " + videoDownloadUri);

                                // Check if thumbnail exists
                                thumbnailRef.getDownloadUrl().addOnSuccessListener(thumbnailDownloadUri -> {
                                    // Thumbnail exists, use existing URL
                                    Log.d(TAG, "Thumbnail already exists: " + thumbFileName + ", URL: " + thumbnailDownloadUri);
                                    groupModel.setThumbnail(thumbnailDownloadUri.toString());
                                    msgModel.setThumbnail(thumbnailDownloadUri.toString());
                                    // Use the original fileNameThumbnail from intent (without Firebase prefix)
                                    groupModel.setFileNameThumbnail(fileNameThumbnail);
                                    msgModel.setFileNameThumbnail(fileNameThumbnail);
                                    performUpload(groupModel, msgModel, filePath, userFTokenKey, deviceType);
                                }).addOnFailureListener(thumbError -> {
                                    if (thumbError instanceof StorageException && ((StorageException) thumbError).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                        // Thumbnail does not exist, upload it
                                        Log.d(TAG, "Thumbnail does not exist, uploading: " + thumbFileName);
                                        Uri thumbnailUri = Uri.fromFile(thumbnailFile);
                                        UploadTask thumbnailUploadTask = thumbnailRef.putFile(thumbnailUri);
                                        thumbnailUploadTask.addOnProgressListener(thumbSnapshot -> {
                                            double thumbProgress = (100.0 * thumbSnapshot.getBytesTransferred()) / thumbSnapshot.getTotalByteCount();
                                            showNotification("Uploading thumbnail to Firebase: " + (int) thumbProgress + "%", (int) thumbProgress);
                                            Intent progressIntent = new Intent("GROUP_UPLOAD_PROGRESS");
                                            progressIntent.putExtra("modelId", modelId);
                                            progressIntent.putExtra("progress", (int) thumbProgress);
                                            LocalBroadcastManager.getInstance(this).sendBroadcast(progressIntent);
                                        }).addOnSuccessListener(thumbSnapshot -> {
                                            getDownloadUrlWithRetry(thumbnailRef, 1, 5, 300,
                                                    thumbnailDownloadUri -> {
                                                        groupModel.setThumbnail(thumbnailDownloadUri.toString());
                                                        msgModel.setThumbnail(thumbnailDownloadUri.toString());
                                                        // Use the original fileNameThumbnail from intent (without Firebase prefix)
                                                        groupModel.setFileNameThumbnail(fileNameThumbnail);
                                                        msgModel.setFileNameThumbnail(fileNameThumbnail);
                                                        Log.d(TAG, "Thumbnail uploaded: " + thumbnailDownloadUri);
                                                        performUpload(groupModel, msgModel, filePath, userFTokenKey, deviceType);
                                                    },
                                                    thumbUrlError -> {
                                                        Log.e(TAG, "Thumbnail URL fetch failed: " + thumbUrlError.getMessage(), thumbUrlError);
                                                        showNotification("Thumbnail upload failed: " + thumbUrlError.getMessage(), 0);
                                                        stopSelf();
                                                    });
                                        }).addOnFailureListener(thumbUploadError -> {
                                            Log.e(TAG, "Thumbnail upload failed: " + thumbUploadError.getMessage(), thumbUploadError);
                                            showNotification("Thumbnail upload failed: " + thumbUploadError.getMessage(), 0);
                                            stopSelf();
                                        });
                                        mainHandler.postDelayed(() -> {
                                            if (thumbnailUploadTask.isInProgress()) {
                                                thumbnailUploadTask.cancel();
                                                Log.e(TAG, "Thumbnail upload timed out");
                                            }
                                        }, 60_000);
                                    } else {
                                        Log.e(TAG, "Thumbnail check failed: " + thumbError.getMessage(), thumbError);
                                        showNotification("Thumbnail check failed: " + thumbError.getMessage(), 0);
                                        stopSelf();
                                    }
                                });
                            }, videoUrlError -> {
                                Log.e(TAG, "Video URL fetch failed: " + videoUrlError.getMessage(), videoUrlError);
                                showNotification("Video upload failed: " + videoUrlError.getMessage(), 0);
                                stopSelf();
                            });
                }).addOnFailureListener(videoUploadError -> {
                    Log.e(TAG, "Video upload failed: " + videoUploadError.getMessage(), videoUploadError);
                    showNotification("Video upload failed: " + videoUploadError.getMessage(), 0);
                    stopSelf();
                });
                mainHandler.postDelayed(() -> {
                    if (videoUploadTask.isInProgress()) {
                        videoUploadTask.cancel();
                        Log.e(TAG, "Video upload timed out");
                    }
                }, 60_000);
            } else {
                Log.e(TAG, "Video check failed: " + e.getMessage(), e);
                showNotification("Video check failed: " + e.getMessage(), 0);
                stopSelf();
            }
        });
    }

    private void performSelectionBunchUpload(ArrayList<String> firebaseUrls,
                                             group_messageModel groupModel,
                                             messageModel msgModel,
                                             String userFTokenKey,
                                             String deviceType) {
        Log.d(TAG, "performSelectionBunchUpload: Using pre-uploaded URLs count=" + firebaseUrls.size());
        if (firebaseUrls.isEmpty()) {
            Log.w(TAG, "performSelectionBunchUpload: URL list empty, falling back to single upload");
            uploadToFirebaseStorage(msgModel.getDocument(), groupModel.getFileName(), groupModel, msgModel, userFTokenKey, deviceType, msgModel.getDataType());
            return;
        }

        ArrayList<selectionBunchModel> selectionBunch = groupModel.getSelectionBunch();
        ArrayList<selectionBunchModel> msgSelectionBunch = msgModel.getSelectionBunch();
        if (selectionBunch == null) {
            selectionBunch = new ArrayList<>();
            groupModel.setSelectionBunch(selectionBunch);
        }
        if (selectionBunch.isEmpty() && msgSelectionBunch != null && !msgSelectionBunch.isEmpty()) {
            for (selectionBunchModel bunchModel : msgSelectionBunch) {
                selectionBunch.add(bunchModel);
            }
        }
        if (msgSelectionBunch == null) {
            msgSelectionBunch = new ArrayList<>();
            msgModel.setSelectionBunch(msgSelectionBunch);
        }
        if (msgSelectionBunch.isEmpty() && selectionBunch != null && !selectionBunch.isEmpty()) {
            for (selectionBunchModel bunchModel : selectionBunch) {
                msgSelectionBunch.add(bunchModel);
            }
        }

        for (int i = 0; i < selectionBunch.size() && i < firebaseUrls.size(); i++) {
            String url = firebaseUrls.get(i);
            selectionBunchModel bunchModel = selectionBunch.get(i);
            if (bunchModel.getFileName() == null || bunchModel.getFileName().isEmpty()) {
                String fallbackName = extractFileNameFromUrl(url, i);
                bunchModel.setFileName(fallbackName);
            }
            bunchModel.setImgUrl(url);
        }
        for (int i = 0; i < msgSelectionBunch.size() && i < firebaseUrls.size(); i++) {
            String url = firebaseUrls.get(i);
            selectionBunchModel bunchModel = msgSelectionBunch.get(i);
            if (bunchModel.getFileName() == null || bunchModel.getFileName().isEmpty()) {
                bunchModel.setFileName(extractFileNameFromUrl(url, i));
            }
            bunchModel.setImgUrl(url);
        }

        groupModel.setDocument(firebaseUrls.get(0));
        msgModel.setDocument(firebaseUrls.get(0));
        String selectionCount = String.valueOf(firebaseUrls.size());
        groupModel.setSelectionCount(selectionCount);
        msgModel.setSelectionCount(selectionCount);

        performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
    }

    private void uploadSelectionBunchFiles(ArrayList<String> localPaths,
                                           group_messageModel groupModel,
                                           messageModel msgModel,
                                           String userFTokenKey,
                                           String deviceType) {
        Log.d(TAG, "uploadSelectionBunchFiles: uploading " + localPaths.size() + " files");
        if (localPaths.isEmpty()) {
            Log.w(TAG, "uploadSelectionBunchFiles: localPaths empty, aborting");
            return;
        }

        ArrayList<selectionBunchModel> selectionBunch = groupModel.getSelectionBunch();
        ArrayList<selectionBunchModel> msgSelectionBunch = msgModel.getSelectionBunch();
        if (selectionBunch == null) {
            selectionBunch = new ArrayList<>();
            groupModel.setSelectionBunch(selectionBunch);
        }
        ensureSelectionBunchSize(selectionBunch, localPaths.size());
        if (msgSelectionBunch == null) {
            msgSelectionBunch = new ArrayList<>();
            msgModel.setSelectionBunch(msgSelectionBunch);
        }
        ensureSelectionBunchModelSize(msgSelectionBunch, localPaths.size());

        ArrayList<String> uploadedUrls = new ArrayList<>();

        uploadSelectionBunchFileAtIndex(localPaths, 0, uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
    }

    private void uploadSelectionBunchFileAtIndex(ArrayList<String> localPaths,
                                                 int index,
                                                 ArrayList<String> uploadedUrls,
                                                 group_messageModel groupModel,
                                                 messageModel msgModel,
                                                 String userFTokenKey,
                                                 String deviceType) {
        if (index >= localPaths.size()) {
            Log.d(TAG, "uploadSelectionBunchFileAtIndex: completed uploads, urls size=" + uploadedUrls.size());
            if (!uploadedUrls.isEmpty()) {
                performSelectionBunchUpload(uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
            }
            return;
        }

        String path = localPaths.get(index);
        File file = new File(path);
        if (!file.exists() || file.length() == 0) {
            Log.w(TAG, "uploadSelectionBunchFileAtIndex: skipping missing file " + path);
            uploadedUrls.add("");
            uploadSelectionBunchFileAtIndex(localPaths, index + 1, uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
            return;
        }

        String extension = "";
        int dot = file.getName().lastIndexOf('.');
        if (dot != -1) {
            extension = file.getName().substring(dot);
        }

        String uniqueName = msgModel.getUid() + "_" + msgModel.getReceiverUid() + "_" + msgModel.getModelId() + "_" + System.currentTimeMillis() + "_" + index + extension;
        StorageReference ref = storageReference.child(uniqueName);
        Uri fileUri = Uri.fromFile(file);

        ref.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> getDownloadUrlWithRetry(ref, 1, 5, 300,
                        uri -> {
                            String url = uri.toString();
                            uploadedUrls.add(url);
                            updateSelectionBunchEntry(index, url, groupModel, msgModel);
                            uploadSelectionBunchFileAtIndex(localPaths, index + 1, uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
                        },
                        error -> {
                            Log.e(TAG, "Failed to get selectionBunch URL: " + error.getMessage());
                            uploadedUrls.add("");
                            updateSelectionBunchEntry(index, "", groupModel, msgModel);
                            uploadSelectionBunchFileAtIndex(localPaths, index + 1, uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
                        }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Selection bunch file upload failed: " + e.getMessage());
                    uploadedUrls.add("");
                    updateSelectionBunchEntry(index, "", groupModel, msgModel);
                    uploadSelectionBunchFileAtIndex(localPaths, index + 1, uploadedUrls, groupModel, msgModel, userFTokenKey, deviceType);
                });
    }

    private void updateSelectionBunchEntry(int index,
                                           String url,
                                           group_messageModel groupModel,
                                           messageModel msgModel) {
        if (groupModel.getSelectionBunch() != null && index < groupModel.getSelectionBunch().size()) {
            selectionBunchModel bunchModel = groupModel.getSelectionBunch().get(index);
            if (bunchModel.getFileName() == null || bunchModel.getFileName().isEmpty()) {
                String fallbackName = extractFileNameFromUrl(url, index);
                bunchModel.setFileName(fallbackName);
            }
            bunchModel.setImgUrl(url);
        } else if (groupModel.getSelectionBunch() != null) {
            selectionBunchModel bunchModel = new selectionBunchModel(url, extractFileNameFromUrl(url, index));
            groupModel.getSelectionBunch().add(bunchModel);
        }
        if (msgModel.getSelectionBunch() != null && index < msgModel.getSelectionBunch().size()) {
            selectionBunchModel bunchModel = msgModel.getSelectionBunch().get(index);
            if (bunchModel.getFileName() == null || bunchModel.getFileName().isEmpty()) {
                bunchModel.setFileName(extractFileNameFromUrl(url, index));
            }
            bunchModel.setImgUrl(url);
        } else if (msgModel.getSelectionBunch() != null) {
            selectionBunchModel bunchModel = new selectionBunchModel();
            bunchModel.setImgUrl(url);
            bunchModel.setFileName(extractFileNameFromUrl(url, index));
            msgModel.getSelectionBunch().add(bunchModel);
        }
    }

    private void ensureSelectionBunchSize(ArrayList<selectionBunchModel> selectionBunch, int expectedSize) {
        while (selectionBunch.size() < expectedSize) {
            selectionBunchModel bunchModel = new selectionBunchModel("", "");
            selectionBunch.add(bunchModel);
        }
    }

    private void ensureSelectionBunchModelSize(ArrayList<selectionBunchModel> list, int expectedSize) {
        while (list.size() < expectedSize) {
            selectionBunchModel model = new selectionBunchModel();
            model.setImgUrl("");
            model.setFileName("");
            list.add(model);
        }
    }

    private void uploadToFirebaseStorage(String filePath, String fileName, group_messageModel groupModel, messageModel msgModel,
                                         String userFTokenKey, String deviceType, String dataType) {
        if (filePath == null || filePath.isEmpty()) {
            Log.e(TAG, "Invalid file path: " + filePath);
            showNotification("Upload failed: Invalid file path", 0);
            stopSelf();
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || file.length() > 200 * 1024 * 1024) {
            Log.e(TAG, "File invalid: exists=" + file.exists() + ", size=" + file.length());
            showNotification("Upload failed: File does not exist or exceeds 200MB", 0);
            stopSelf();
            return;
        }

        // Use a strong unique pattern to avoid collisions across users/groups/messages
        // <uid>_<groupId>_<modelId>_<sanitizedOriginalName or generated>
        String baseName = (fileName != null && !fileName.isEmpty()) ? fileName.replaceAll("[^A-Za-z0-9._-]", "_") : (System.currentTimeMillis() + "." + msgModel.getExtension());
        String uniqueFileName = msgModel.getUid() + "_" + msgModel.getReceiverUid() + "_" + msgModel.getModelId() + "_" + baseName;
        StorageReference fileRef = storageReference.child(uniqueFileName);

        // Check if file already exists
        fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
            // File exists, use existing URL
            Log.d(TAG, "File already exists in Firebase Storage: " + uniqueFileName + ", URL: " + downloadUri);
            groupModel.setDocument(downloadUri.toString());
            msgModel.setDocument(downloadUri.toString());
            performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
        }).addOnFailureListener(e -> {
            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                // File does not exist, upload it
                Log.d(TAG, "File does not exist in Firebase Storage, uploading: " + uniqueFileName);
                Uri fileUri = Uri.fromFile(file);
                UploadTask uploadTask = fileRef.putFile(fileUri);
                uploadTask.addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    showNotification("Uploading to Firebase: " + (int) progress + "%", (int) progress);
                    Intent progressIntent = new Intent("GROUP_UPLOAD_PROGRESS");
                    progressIntent.putExtra("modelId", modelId);
                    progressIntent.putExtra("progress", (int) progress);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(progressIntent);
                }).addOnSuccessListener(taskSnapshot -> {
                    getDownloadUrlWithRetry(fileRef, 1, 5, 300,
                            downloadUri -> {
                                groupModel.setDocument(downloadUri.toString());
                                msgModel.setDocument(downloadUri.toString());
                                Log.d(TAG, "File uploaded successfully: " + downloadUri);
                                performUpload(groupModel, msgModel, filePath, userFTokenKey, deviceType);
                            },
                            urlError -> {
                                Log.e(TAG, "File URL fetch failed: " + urlError.getMessage(), urlError);
                                showNotification("File upload failed: " + urlError.getMessage(), 0);
                                stopSelf();
                            });
                }).addOnFailureListener(uploadError -> {
                    Log.e(TAG, "File upload failed: " + uploadError.getMessage(), uploadError);
                    showNotification("File upload failed: " + uploadError.getMessage(), 0);
                    stopSelf();
                });
                mainHandler.postDelayed(() -> {
                    if (uploadTask.isInProgress()) {
                        uploadTask.cancel();
                        Log.e(TAG, "File upload timed out");
                    }
                }, 60_000);
            } else {
                Log.e(TAG, "File check failed: " + e.getMessage(), e);
                showNotification("File check failed: " + e.getMessage(), 0);
                stopSelf();
            }
        });
    }

    private void handleContactUpload(group_messageModel groupModel, messageModel msgModel,
                                     String userFTokenKey, String deviceType) {
        Log.d(TAG, "Handling contact upload, skipping Firebase");
        performUpload(groupModel, msgModel, null, userFTokenKey, deviceType);
    }

    private void performUpload(group_messageModel groupModel, messageModel msgModel, String filePath,
                               String userFTokenKey, String deviceType) {
        Log.d(TAG, "Performing server upload, document: " + msgModel.getDocument() + ", thumbnail: " + msgModel.getThumbnail());

        String msgdata = msgModel.getMessage();
        if (msgModel.getDataType().equals(Constant.Text)) {
            msgdata = groupModel.getMessage();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uid", safeString(msgModel.getUid()))
                .addFormDataPart("group_id", safeString(msgModel.getReceiverUid()))
                .addFormDataPart("message", safeString(msgdata))
                .addFormDataPart("user_name", safeString(msgModel.getUserName()))
                .addFormDataPart("notification", String.valueOf(msgModel.getNotification()))
                .addFormDataPart("dataType", safeString(msgModel.getDataType()))
                .addFormDataPart("model_id", safeString(msgModel.getModelId()))
                .addFormDataPart("sent_time", safeString(msgModel.getTime()))
                .addFormDataPart("extension", safeString(msgModel.getExtension()))
                .addFormDataPart("name", safeString(msgModel.getName()))
                .addFormDataPart("phone", safeString(msgModel.getPhone()))
                .addFormDataPart("micPhoto", safeString(msgModel.getMicPhoto()))
                .addFormDataPart("miceTiming", safeString(msgModel.getMiceTiming()))
                .addFormDataPart("created_by", safeString(groupModel.getCreatedBy()))
                .addFormDataPart("document", safeString(msgModel.getDocument()))
                .addFormDataPart("thumbnail", safeString(msgModel.getThumbnail()))
                .addFormDataPart("fileNameThumbnail", safeString(msgModel.getFileNameThumbnail()))
                .addFormDataPart("selection_count", safeString(msgModel.getSelectionCount()))
                .addFormDataPart("fTokenKey", safeString(userFTokenKey));

        Log.d(TAG, "Server upload parameters - selection_count: " + safeString(msgModel.getSelectionCount()));
        Log.d(TAG, "Server upload parameters - uid: " + safeString(msgModel.getUid()));
        Log.d(TAG, "Server upload parameters - group_id: " + safeString(msgModel.getReceiverUid()));
        Log.d(TAG, "Server upload parameters - message: " + safeString(msgdata));
        Log.d(TAG, "Server upload parameters - user_name: " + safeString(msgModel.getUserName()));
        Log.d(TAG, "Server upload parameters - dataType: " + safeString(msgModel.getDataType()));
        Log.d(TAG, "Server upload parameters - model_id: " + safeString(msgModel.getModelId()));
        Log.d(TAG, "Server upload parameters - fTokenKey: " + safeString(userFTokenKey));

        // Handle upload_docs based on file existence in Firebase Storage
        if (msgModel.getDataType().equals(Constant.Text) || msgModel.getDataType().equals(Constant.contact)) {
            builder.addFormDataPart("upload_docs", "");
        } else if (msgModel.getDocument() != null && !msgModel.getDocument().isEmpty()) {
            // File exists, send Firebase URL
            Log.d(TAG, "File exists, sending Firebase URL for upload_docs: " + msgModel.getDocument());
            builder.addFormDataPart("upload_docs", msgModel.getDocument());
        } else if (filePath != null && !filePath.isEmpty()) {
            // File does not exist, upload local file
            File file = new File(filePath);
            if (file.exists() && file.length() <= 200 * 1024 * 1024) {
                String mimeType = getMimeType(filePath);
                Log.d(TAG, "Uploading local file for upload_docs: " + file.getAbsolutePath() + ", mimeType: " + mimeType);
                RequestBody fileBody = RequestBody.create(
                        MediaType.parse(mimeType != null ? mimeType : "application/octet-stream"), file);
                ProgressRequestBody progressRequestBody = new ProgressRequestBody(fileBody, progress -> {
                    showNotification("Uploading to Server: " + progress + "%", progress);
                    Intent progressIntent = new Intent("GROUP_UPLOAD_PROGRESS");
                    progressIntent.putExtra("modelId", msgModel.getModelId());
                    progressIntent.putExtra("progress", progress);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(progressIntent);
                });
                builder.addFormDataPart("upload_docs", file.getName(), progressRequestBody);
            } else {
                Log.e(TAG, "Server upload file invalid: " + filePath);
                showNotification("Upload failed: File does not exist or exceeds 200MB", 0);
                stopSelf();
                return;
            }
        } else {
            Log.w(TAG, "No file to upload and no Firebase URL available");
            builder.addFormDataPart("upload_docs", "");
        }

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(CREATE_GROUP_CHATTING)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    mainHandler.post(() -> {
                        Log.e(TAG, "Server upload failed: " + e.getMessage(), e);
                        showNotification("Server upload failed: " + e.getMessage(), 0);
                        stopSelf();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    mainHandler.post(() -> {
                        try {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                Log.d(TAG, "Server response: " + responseBody);
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                int errorCode = jsonResponse.getInt("error_code");

                                if (errorCode == 200) {
                                    Log.d(TAG, "Server upload successful");

                                    Intent progressIntent = new Intent("GROUP_MESSAGE_UPLOADED");
                                    progressIntent.putExtra("modelId", modelId);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(progressIntent);

                                    mainHandler.postDelayed(() -> {
                                        writeToRealtimeDatabase(groupModel, msgModel);

                                        Constant.getSfFuncion(GroupMessageUploadService.this);
                                        String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");
                                        if (!sleepKey.equals(Constant.sleepKey)) {
                                            try {
                                                Constant.setSfFunction(GroupMessageUploadService.this);
                                                Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
                                                Constant.setSF.apply();
                                                Constant.getSfFuncion(GroupMessageUploadService.this);
                                                String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");
                                                String profilePic = Constant.getSF.getString(Constant.profilePic, "");
                                                JSONObject data = jsonResponse.getJSONObject("data");
                                                JSONArray groupMembers = data.getJSONArray("group_members");


                                                String notificationMessage;
                                                if (msgModel.getDataType().equals(Constant.Text)) {
                                                    notificationMessage = msgModel.getMessage();
                                                } else if (msgModel.getDataType().equals(Constant.img)) {
                                                    notificationMessage = "You have a new Image";
                                                } else if (msgModel.getDataType().equals(Constant.contact)) {
                                                    notificationMessage = "You have a new Contact";
                                                } else if (msgModel.getDataType().equals(Constant.voiceAudio)) {
                                                    notificationMessage = "You have a new Audio";
                                                } else if (msgModel.getDataType().equals(Constant.video)) {
                                                    notificationMessage = "You have a new Video";
                                                } else {
                                                    notificationMessage = "You have a new File";
                                                }

                                                Constant.getSfFuncion(getApplicationContext());
                                                String recIdMy = Constant.getSF.getString(Constant.UID_KEY,"");
                                                Log.e("recIdMy", "recIdMy: "+recIdMy );
                                                Log.e("recIdMy", "msgModel.getUid(): "+msgModel.getUid());
                                                Log.e("recIdMy", "msgModel.getReceiverUid(): "+msgModel.getReceiverUid());

                                                if (msgModel.getDataType().equals(Constant.doc)) {
                                                    Webservice.end_notification_api_group(
                                                            GroupMessageUploadService.this,
                                                            msgModel.getUserName(),
                                                            notificationMessage,
                                                            msgModel.getUid(),
                                                            msgModel.getUserName(),
                                                            profilePic,
                                                            msgModel.getTime(),
                                                            "1",
                                                            msgModel.getUid(),
                                                            msgModel.getMessage(),
                                                            msgModel.getTime(),
                                                            msgModel.getDocument(),
                                                            msgModel.getFileName(),
                                                            msgModel.getExtension(),
                                                            msgModel.getName(),
                                                            msgModel.getPhone(),
                                                            msgModel.getMicPhoto(),
                                                            msgModel.getMiceTiming(),
                                                            msgModel.getUserName(),
                                                            msgModel.getReplytextData(),
                                                            msgModel.getReplyKey(),
                                                            msgModel.getReplyType(),
                                                            msgModel.getReplyOldData(),
                                                            msgModel.getReplyCrtPostion(),
                                                            msgModel.getModelId(),
                                                            msgModel.getReceiverUid(),
                                                            msgModel.getForwaredKey(),
                                                            msgModel.getGroupName(),
                                                            msgModel.getDocSize(),
                                                            msgModel.getFileName(),
                                                            msgModel.getThumbnail(),
                                                            msgModel.getFileNameThumbnail(),
                                                            msgModel.getCaption(),
                                                            msgModel.getNotification(),
                                                            msgModel.getCurrentDate(),
                                                            fcm,
                                                            groupMembers,
                                                            msgModel.getTimestamp(),
                                                            null,
                                                            msgModel.getImageWidth(),
                                                            msgModel.getImageHeight(),
                                                            msgModel.getAspectRatio(),
                                                            msgModel.getSelectionCount(),msgModel.getSelectionBunch()
                                                    );
                                                } else if (msgModel.getDataType().equals(Constant.voiceAudio)) {
                                                    Constant.getSfFuncion(getApplicationContext());
                                                    Webservice.end_notification_api_group(
                                                            GroupMessageUploadService.this,
                                                            msgModel.getUserName(),
                                                            notificationMessage,
                                                            msgModel.getUid(),
                                                            msgModel.getUserName(),
                                                            profilePic,
                                                            msgModel.getTime(),
                                                            "1",
                                                            msgModel.getUid(),
                                                            msgModel.getMessage(),
                                                            msgModel.getTime(),
                                                            msgModel.getDocument(),
                                                            msgModel.getDataType(),
                                                            msgModel.getExtension(),
                                                            msgModel.getName(),
                                                            msgModel.getPhone(),
                                                            Constant.getSF.getString(Constant.profilePic, ""),
                                                            msgModel.getMiceTiming(),
                                                            msgModel.getUserName(),
                                                            msgModel.getReplytextData(),
                                                            msgModel.getReplyKey(),
                                                            msgModel.getReplyType(),
                                                            msgModel.getReplyOldData(),
                                                            msgModel.getReplyCrtPostion(),
                                                            msgModel.getModelId(),
                                                            msgModel.getReceiverUid(),
                                                            msgModel.getForwaredKey(),
                                                            msgModel.getGroupName(),
                                                            msgModel.getDocSize(),
                                                            msgModel.getFileName(),
                                                            msgModel.getThumbnail(),
                                                            msgModel.getFileNameThumbnail(),
                                                            msgModel.getCaption(),
                                                            msgModel.getNotification(),
                                                            msgModel.getCurrentDate(),
                                                            fcm,
                                                            groupMembers,
                                                            msgModel.getTimestamp(),
                                                            null,
                                                            msgModel.getImageWidth(), msgModel.getImageHeight(), msgModel.getAspectRatio(), msgModel.getSelectionCount(), msgModel.getSelectionBunch());
                                                } else if (msgModel.getDataType().equals(Constant.Text)) {
                                                    Webservice.end_notification_api_group(
                                                            GroupMessageUploadService.this,
                                                            msgModel.getUserName(),
                                                            msgModel.getCaption(),
                                                            msgModel.getUid(),
                                                            msgModel.getUserName(),
                                                            profilePic,
                                                            msgModel.getTime(),
                                                            "1",
                                                            msgModel.getUid(),
                                                            msgModel.getCaption(),
                                                            msgModel.getTime(),
                                                            msgModel.getDocument(),
                                                            msgModel.getDataType(),
                                                            msgModel.getExtension(),
                                                            msgModel.getName(),
                                                            msgModel.getPhone(),
                                                            msgModel.getMicPhoto(),
                                                            msgModel.getMiceTiming(),
                                                            msgModel.getUserName(),
                                                            msgModel.getReplytextData(),
                                                            msgModel.getReplyKey(),
                                                            msgModel.getReplyType(),
                                                            msgModel.getReplyOldData(),
                                                            msgModel.getReplyCrtPostion(),
                                                            msgModel.getModelId(),
                                                            msgModel.getReceiverUid(),
                                                            msgModel.getForwaredKey(),
                                                            msgModel.getGroupName(),
                                                            msgModel.getDocSize(),
                                                            msgModel.getFileName(),
                                                            msgModel.getThumbnail(),
                                                            msgModel.getFileNameThumbnail(),
                                                            "",
                                                            msgModel.getNotification(),
                                                            msgModel.getCurrentDate(),
                                                            fcm,
                                                            groupMembers,
                                                            msgModel.getTimestamp(),
                                                            null,
                                                            msgModel.getImageWidth(), msgModel.getImageHeight(), msgModel.getAspectRatio(), msgModel.getSelectionCount(), msgModel.getSelectionBunch());
                                                } else {
                                                    Webservice.end_notification_api_group(
                                                            GroupMessageUploadService.this,
                                                            msgModel.getUserName(),
                                                            notificationMessage,
                                                            msgModel.getUid(),
                                                            msgModel.getUserName(),
                                                            profilePic,
                                                            msgModel.getTime(),
                                                            "1",
                                                            msgModel.getUid(),
                                                            msgModel.getMessage(),
                                                            msgModel.getTime(),
                                                            msgModel.getDocument(),
                                                            msgModel.getDataType(),
                                                            msgModel.getExtension(),
                                                            msgModel.getName(),
                                                            msgModel.getPhone(),
                                                            msgModel.getMicPhoto(),
                                                            msgModel.getMiceTiming(),
                                                            msgModel.getUserName(),
                                                            msgModel.getReplytextData(),
                                                            msgModel.getReplyKey(),
                                                            msgModel.getReplyType(),
                                                            msgModel.getReplyOldData(),
                                                            msgModel.getReplyCrtPostion(),
                                                            msgModel.getModelId(),
                                                            msgModel.getReceiverUid(),
                                                            msgModel.getForwaredKey(),
                                                            msgModel.getGroupName(),
                                                            msgModel.getDocSize(),
                                                            msgModel.getFileName(),
                                                            msgModel.getThumbnail(),
                                                            msgModel.getFileNameThumbnail(),
                                                            msgModel.getCaption(),
                                                            msgModel.getNotification(),
                                                            msgModel.getCurrentDate(),
                                                            fcm,
                                                            groupMembers,
                                                            msgModel.getTimestamp(),
                                                            null,
                                                            msgModel.getImageWidth(), msgModel.getImageHeight(), msgModel.getAspectRatio(), msgModel.getSelectionCount(), msgModel.getSelectionBunch());
                                                }
                                            } catch (JSONException e) {
                                                Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                                                showNotification("Failed to process server response: " + e.getMessage(), 0);
                                            } catch (Exception e) {
                                                Log.e(TAG, "Error sending notification: " + e.getMessage(), e);
                                                showNotification("Failed to send notification: " + e.getMessage(), 0);
                                            }
                                        }
                                        showNotification("Upload successful", 100);
                                        Intent successIntent = new Intent("GROUP_UPLOAD_SUCCESS");
                                        successIntent.putExtra("modelId", msgModel.getModelId());
                                        LocalBroadcastManager.getInstance(GroupMessageUploadService.this).sendBroadcast(successIntent);
                                    }, 500);
                                } else {
                                    String message = jsonResponse.optString("message", "Unknown error");
                                    Log.e(TAG, "Server upload failed: error_code=" + errorCode + ", message=" + message);
                                    showNotification("Server upload failed: " + message, 0);
                                }
                            } else {
                                Log.e(TAG, "Server upload failed: " + response.message());
                                showNotification("Server upload failed: " + response.message(), 0);
                            }
                        } catch (JSONException | IOException e) {
                            Log.e(TAG, "Failed to parse server response: " + e.getMessage(), e);
                            showNotification("Server response parsing failed: " + e.getMessage(), 0);
                        } finally {
                            stopSelf();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error during server upload", e);
            showNotification("Upload failed: " + e.getMessage(), 0);
            stopSelf();
        }
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    private String getMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private String safeString(String value) {
        return value != null ? value : "";
    }

    private void showNotification(String message, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Group Message Upload")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(progress < 100);

        if (progress > 0 && progress < 100) {
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }

        notificationManager.notify(notificationId, builder.build());

        if (progress == 100) {
            mainHandler.postDelayed(() -> {
                notificationManager.cancel(notificationId);
                Log.d(TAG, "Notification dismissed for modelId: " + modelId);
            }, 2000);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Group Message Upload",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cancelReceiver != null) {
            unregisterReceiver(cancelReceiver);
        }
        executor.shutdown();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class CancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("CANCEL_GROUP_UPLOAD".equals(intent.getAction()) && intent.getStringExtra("modelId").equals(modelId)) {
                Log.d(TAG, "Upload cancelled for modelId: " + modelId);
                showNotification("Upload cancelled", 0);
                stopSelf();
            }
        }
    }

    private static class ProgressRequestBody extends RequestBody {
        private final RequestBody requestBody;
        private final ProgressListener progressListener;

        ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
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
        public void writeTo(BufferedSink sink) throws IOException {
            long contentLength = contentLength();
            Sink forwardingSink = new ForwardingSink(sink) {
                long bytesWritten = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    bytesWritten += byteCount;
                    int progress = (int) (100 * bytesWritten / contentLength);
                    progressListener.onProgress(progress);
                }
            };
            BufferedSink bufferedSink = Okio.buffer(forwardingSink);
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        interface ProgressListener {
            void onProgress(int progress);
        }
    }

    private String extractFileNameFromUrl(String url, int index) {
        if (url == null || url.isEmpty()) {
            return "image_" + index + ".jpg";
        }
        int lastSlash = url.lastIndexOf('/') + 1;
        String name = lastSlash > 0 && lastSlash < url.length() ? url.substring(lastSlash) : "image_" + index;
        int query = name.indexOf('?');
        if (query != -1) {
            name = name.substring(0, query);
        }
        return name.isEmpty() ? "image_" + index + ".jpg" : name;
    }
}