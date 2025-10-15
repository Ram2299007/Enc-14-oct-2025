package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageUploadServiceForward extends Service {
    private static final String TAG = "MessageUploadServiceForward";
    private static final String CHANNEL_ID = "UploadChannel";
    private static final String CREATE_INDIVIDUAL_CHATTING = Webservice.BASE_URL + "create_individual_chatting";
    private String modelId;
    private Handler mainHandler;
    private ExecutorService executor;
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service onCreate called");
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
        Log.d(TAG, "onStartCommand called. StartId: " + startId);

        if (intent == null) {
            Log.e(TAG, "Intent is null. Stopping service.");
            showNotification("Upload failed: Null intent", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        String messageModelJson = intent.getStringExtra("messageModel");
        String userFTokenKey = intent.getStringExtra("userFTokenKey");
        String deviceType = intent.getStringExtra("deviceType");
        Log.d(TAG, "Intent extras: messageModelJson length=" + (messageModelJson != null ? messageModelJson.length() : "null") +
                ", userFTokenKey=" + userFTokenKey + ", deviceType=" + deviceType);

        if (messageModelJson == null) {
            Log.e(TAG, "Missing messageModel JSON. Stopping service.");
            showNotification("Upload failed: Missing messageModel", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        messageModel msgModel = gson.fromJson(messageModelJson, messageModel.class);
        if (msgModel == null) {
            Log.e(TAG, "Failed to deserialize messageModel. Stopping service.");
            showNotification("Upload failed: Invalid messageModel", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        modelId = msgModel.getModelId();
        Log.d(TAG, "Message model deserialized. ModelId: " + modelId + ", SenderUid: " + msgModel.getUid() +
                ", ReceiverUid: " + msgModel.getReceiverUid());

        if (msgModel.getUid() == null || msgModel.getReceiverUid() == null || modelId == null) {
            Log.e(TAG, "Missing required fields: senderId=" + msgModel.getUid() + ", receiverUid=" + msgModel.getReceiverUid() +
                    ", modelId=" + modelId + ". Stopping service.");
            showNotification("Upload failed: Missing required fields", 0);
            stopSelf();
            return START_NOT_STICKY;
        }

        int notificationId = modelId.hashCode();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Forwarding Message")
                .setContentText("Starting forwarding...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
        startForeground(notificationId, notification);
        Log.d(TAG, "Foreground notification started. NotificationId: " + notificationId);

        executor.execute(() -> {
            Log.d(TAG, "Executing upload task for ModelId: " + modelId);
            try {
                uploadToServer(msgModel, null, null, userFTokenKey, deviceType, userFTokenKey);
            } catch (Exception e) {
                Log.e(TAG, "Upload error for ModelId: " + modelId + ": " + e.getMessage(), e);
                showNotification("Forward failed: " + e.getMessage(), 0);
                stopSelf();
            }
        });

        return START_STICKY;
    }

    private void uploadToServer(messageModel model, @Nullable String fileName, @Nullable String thumbnailName,
                                String userFTokenKey, String deviceType, String fTokenKey) {
        Log.d(TAG, "uploadToServer called for ModelId: " + model.getModelId());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uid", safeString(model.getUid()))
                .addFormDataPart("friend_id", safeString(model.getReceiverUid()))
                .addFormDataPart("message", safeString(model.getMessage()))
                .addFormDataPart("user_name", safeString(model.getUserName()))
                .addFormDataPart("notification", String.valueOf(model.getNotification()))
                .addFormDataPart("dataType", safeString(model.getDataType()))
                .addFormDataPart("model_id", safeString(model.getModelId()))
                .addFormDataPart("sent_time", safeString(model.getTime()))
                .addFormDataPart("extension", safeString(model.getExtension()))
                .addFormDataPart("name", safeString(model.getName()))
                .addFormDataPart("phone", safeString(model.getPhone()))
                .addFormDataPart("micPhoto", safeString(model.getMicPhoto()))
                .addFormDataPart("miceTiming", safeString(model.getMiceTiming()))
                .addFormDataPart("fTokenKey", safeString(fTokenKey))
                .addFormDataPart("upload_docs", "");

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(CREATE_INDIVIDUAL_CHATTING)
                .post(requestBody)
                .build();
        Log.d(TAG, "HTTP request prepared for ModelId: " + model.getModelId());

        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.d(TAG, "Server response for ModelId: " + model.getModelId() + ": " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server request failed for ModelId: " + model.getModelId() + ": HTTP " + response.code());
                    showNotification("Forward failed: HTTP " + response.code(), 0);
                    stopSelf();
                    return;
                }

                JSONObject jsonResponse = new JSONObject(responseBody);
                int status = jsonResponse.getInt("error_code");
                Log.d(TAG, "Server response status for ModelId: " + model.getModelId() + ": " + status);

                if (status == 200) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                //    String totalMsgLimit = data.getString("total_msg_limit");
                    String responseUserName = data.getString("user_name");
                   // Log.d(TAG, "Success response for ModelId: " + model.getModelId() + ", TotalMsgLimit: " + totalMsgLimit);

                    String senderRoom = model.getUid() + model.getReceiverUid();
                    String receiverRoom = model.getReceiverUid() + model.getUid();
                    Log.d(TAG, "Firebase rooms: SenderRoom=" + senderRoom + ", ReceiverRoom=" + receiverRoom);

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> senderMap = model.toMap(); // 🔹 model ला Map मध्ये बदल
                    senderMap.put("timestamp", ServerValue.TIMESTAMP); // ✅ server timestamp लाव

                    Map<String, Object> receiverMap = model.toMap();
                    receiverMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put(Constant.CHAT + "/" + senderRoom + "/" + model.getModelId(), senderMap);
                    updates.put(Constant.CHAT + "/" + receiverRoom + "/" + model.getModelId(), receiverMap);

                    database.updateChildren(updates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Firebase update successful for ModelId: " + model.getModelId());
                            String pushKey = database.child(Constant.chattingSocket).child(model.getReceiverUid()).push().getKey();
                            database.child(Constant.chattingSocket).child(model.getReceiverUid()).setValue(pushKey)
                                    .addOnSuccessListener(unused -> {
                                        Log.d(TAG, "Firebase push key set for ModelId: " + model.getModelId());
                                        Intent intent = new Intent("MESSAGE_UPLOADED");
                                        intent.putExtra("modelId", model.getModelId());
                                        LocalBroadcastManager.getInstance(MessageUploadServiceForward.this).sendBroadcast(intent);
                                        Log.d(TAG, "Broadcast sent for ModelId: " + model.getModelId());

                                        ///TODO STORE IN A SQL LITE DATABASE



                                        if (userFTokenKey != null) {
                                            showNotification("Forward successful", 100);
                                            Constant.getSfFuncion(getApplicationContext());
                                            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                                            if (!model.getReceiverUid().equals(uid)) {
                                                mainHandler.postDelayed(() -> {
                                                    Log.d(TAG, "Sending push notification for ModelId: " + model.getModelId());
                                                    sendPushNotification(model, userFTokenKey, deviceType,
                                                            model.getUid(), model.getUserName(), model.getTime());
                                                }, 500);
                                            }
                                        }
                                        stopSelf();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Firebase push key sync failed for ModelId: " + model.getModelId() + ": " + e.getMessage());
                                        showNotification("Firebase push key sync failed: " + e.getMessage(), 0);
                                        stopSelf();
                                    });
                        } else {
                            Log.e(TAG, "Firebase sync failed for ModelId: " + model.getModelId() + ": " + task.getException().getMessage());
                            showNotification("Firebase sync failed: " + task.getException().getMessage(), 0);
                            stopSelf();
                        }
                    });
                } else {
                    String errorMessage = jsonResponse.optString("message", "Unknown error");
                    Log.e(TAG, "Server error for ModelId: " + model.getModelId() + ": " + errorMessage);
                    showNotification("Failed to forward message: " + errorMessage, 0);
                    stopSelf();
                }
                return; // Success, exit retry loop
            } catch (IOException | JSONException e) {
                retryCount++;
                Log.e(TAG, "Attempt " + retryCount + " failed for ModelId: " + model.getModelId() + ": " + e.getMessage());
                if (retryCount >= maxRetries) {
                    Log.e(TAG, "Max retries reached for ModelId: " + model.getModelId() + ": " + e.getMessage());
                    showNotification("Forward failed: " + e.getMessage(), 0);
                    stopSelf();
                }
                try {
                    Thread.sleep(1000 * retryCount); // Exponential backoff
                } catch (InterruptedException ie) {
                    Log.e(TAG, "Retry sleep interrupted: " + ie.getMessage());
                }
            }
        }
    }

    private String safeString(String input) {
        return input != null ? input : "";
    }

    private void createNotificationChannel() {
        Log.d(TAG, "Creating notification channel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Upload Notifications",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created");
            } else {
                Log.e(TAG, "NotificationManager is null");
            }
        }
    }

    private void showNotification(String message, int progress) {
        Log.d(TAG, "Showing notification for ModelId: " + modelId + ", Message: " + message + ", Progress: " + progress);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Message Forward")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(progress < 100)
                .setProgress(100, progress, progress == 0);

        Notification notification = builder.build();
        startForeground(modelId.hashCode(), notification);
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
                model.getCurrentDate(),model.getSelectionCount()
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service onDestroy called");
        if (!executor.isShutdown()) {
            executor.shutdown();
            Log.d(TAG, "Executor shutdown");
        }
    }
}