package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.Appzia.enclosure.Model.messageModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationSenderChatting {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;
    String receiverKey;
    String user_name;
    String photo;
    String currentDateTimeString;
    String deviceType;
    messageModel model;
    JSONObject notificationJson;
    private static final String PROJECT_ID = "enclosure-30573";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    public FcmNotificationSenderChatting(String userFcmToken, String title, String body, Context mContext, Activity mActivity, String receiverKey, String user_name, String photo, String currentDateTimeString, String deviceType, messageModel model) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.receiverKey = receiverKey;
        this.user_name = user_name;
        this.photo = photo;
        this.currentDateTimeString = currentDateTimeString;
        this.deviceType = deviceType;
        this.model = model;

    }

    public void SendNotifications() {


        Log.d("PowerData", "uidPower: " + model.getUid());
        Log.d("PowerData", "messagePower: " + model.getMessage());
        Log.d("PowerData", "timePower: " + model.getTime());
        Log.d("PowerData", "documentPower: " + model.getDocument());
        Log.d("PowerData", "dataTypePower: " + model.getDataType());
        Log.d("PowerData", "extensionPower: " + model.getExtension());
        Log.d("PowerData", "namepower: " + model.getName());
        Log.d("PowerData", "phonePower: " + model.getPhone());
        Log.d("PowerData", "micPhotoPower: " + model.getMicPhoto());
        Log.d("PowerData", "miceTimingPower: " + model.getMiceTiming());
        Log.d("PowerData", "userNamePower: " + model.getUserName());
        Log.d("PowerData", "replytextDataPower: " + model.getReplytextData());
        Log.d("PowerData", "replyKeyPower: " + model.getReplyKey());
        Log.d("PowerData", "replyTypePower: " + model.getReplyType());
        Log.d("PowerData", "replyOldDataPower: " + model.getReplyOldData());
        Log.d("PowerData", "replyCrtPostionPower: " + model.getReplyCrtPostion());
        Log.d("PowerData", "modelIdPower: " + model.getModelId());
        Log.d("PowerData", "receiverUidPower: " + model.getReceiverUid());
        Log.d("PowerData", "forwaredKeyPower: " + model.getForwaredKey());
        Log.d("PowerData", "groupNamePower: " + model.getGroupName());
        Log.d("PowerData", "docSizePower: " + model.getDocSize());
        Log.d("PowerData", "fileNamePower: " + model.getFileName());
        Log.d("PowerData", "thumbnailPower: " + model.getThumbnail());
        Log.d("PowerData", "fileNameThumbnailPower: " + model.getFileNameThumbnail());
        Log.d("PowerData", "captionPower: " + model.getCaption());
        Log.d("PowerData", "notificationPower: " + model.getNotification());
        Log.d("PowerData", "currentDatePower: " + model.getCurrentDate());


        Log.d("PowerData", "title: " + title);
        Log.d("PowerData", "body: " + body);
        Log.d("PowerData", "icon: " + "notification_icon");
        Log.d("PowerData", "nameKey: " + title);
        Log.d("PowerData", "msgKey: " + body);
        Log.d("PowerData", "currentDateTimeString: " + currentDateTimeString);
        Log.d("PowerData", "photo: " + photo);
        Log.d("PowerData", "friendUidKey: " + receiverKey);
        Log.d("PowerData", "device_type: " + deviceType);
        Log.d("PowerData", "user_nameKey: " + user_name);


        try {
            JSONObject messageObject = new JSONObject();

            if (deviceType.equals("1")) {
                // todo android
                notificationJson = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("bodyKey", Constant.chatting);
                    data.put("title", title);
                    data.put("body", body);
                    data.put("click_action", "OPEN_ACTIVITY_1"); // This can be used in your app's logic
                    data.put("icon", "notification_icon");
                    data.put("nameKey", title);
                    data.put("msgKey", body);
                    data.put("currentDateTimeString", currentDateTimeString);
                    data.put("photo", photo);
                    data.put("friendUidKey", receiverKey);
                    data.put("device_type", deviceType);
                    data.put("user_nameKey", user_name);

                    /// Power - Reply
                    data.put("uidPower", String.valueOf(model.getUid()));
                    data.put("messagePower", String.valueOf(model.getMessage()));
                    data.put("timePower", String.valueOf(model.getTime()));
                    data.put("documentPower", String.valueOf(model.getDocument()));
                    data.put("dataTypePower", String.valueOf(model.getDataType()));
                    data.put("extensionPower", String.valueOf(model.getExtension()));
                    data.put("namepower", String.valueOf(model.getName()));
                    data.put("phonePower", String.valueOf(model.getPhone()));
                    data.put("micPhotoPower", String.valueOf(model.getMicPhoto()));
                    data.put("miceTimingPower", String.valueOf(model.getMiceTiming()));
                    data.put("userNamePower", String.valueOf(model.getUserName()));
                    data.put("replytextDataPower", String.valueOf(model.getReplytextData()));
                    data.put("replyKeyPower", String.valueOf(model.getReplyKey()));
                    data.put("replyTypePower", String.valueOf(model.getReplyType()));
                    data.put("replyOldDataPower", String.valueOf(model.getReplyOldData()));
                    data.put("replyCrtPostionPower", String.valueOf(model.getReplyCrtPostion()));
                    data.put("modelIdPower", String.valueOf(model.getModelId()));
                    data.put("receiverUidPower", String.valueOf(model.getReceiverUid()));
                    data.put("forwaredKeyPower", String.valueOf(model.getForwaredKey()));
                    data.put("groupNamePower", String.valueOf(model.getGroupName()));
                    data.put("docSizePower", String.valueOf(model.getDocSize()));
                    data.put("fileNamePower", String.valueOf(model.getFileName()));
                    data.put("thumbnailPower", String.valueOf(model.getThumbnail()));
                    data.put("fileNameThumbnailPower", String.valueOf(model.getFileNameThumbnail()));
                    data.put("captionPower", String.valueOf(model.getCaption()));
                    data.put("notificationPower", String.valueOf(model.getNotification()));
                    data.put("currentDatePower", String.valueOf(model.getCurrentDate()));
                    data.put("userFcmTokenPower", String.valueOf(userFcmToken));

                    messageObject = new JSONObject();
                    messageObject.put("token", userFcmToken);
                    messageObject.put("data", data);

                    notificationJson.put("message", messageObject);

                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception
                }


            } else if (deviceType.equals("2")) {
                // todo ios

                notificationJson = new JSONObject();

                try {
                    // Create the notification payload
                    JSONObject notification = new JSONObject();
                    notification.put("title", title);
                    notification.put("body", body);

                    JSONObject data = new JSONObject();
                    data.put("bodyKey", Constant.chatting);
                    data.put("title", title);
                    data.put("body", body);
                    data.put("click_action", "OPEN_ACTIVITY_1"); // Custom action
                    data.put("icon", "notification_icon");
                    data.put("nameKey", title);
                    data.put("msgKey", body);
                    data.put("currentDateTimeString", currentDateTimeString);
                    data.put("photo", photo);
                    data.put("friendUidKey", receiverKey);
                    data.put("device_type", deviceType);
                    data.put("user_nameKey", user_name);


                    data.put("uidPower", String.valueOf(model.getUid()));
                    data.put("messagePower", String.valueOf(model.getMessage()));
                    data.put("timePower", String.valueOf(model.getTime()));
                    data.put("documentPower", String.valueOf(model.getDocument()));
                    data.put("dataTypePower", String.valueOf(model.getDataType()));
                    data.put("extensionPower", String.valueOf(model.getExtension()));
                    data.put("namepower", String.valueOf(model.getName()));
                    data.put("phonePower", String.valueOf(model.getPhone()));
                    data.put("micPhotoPower", String.valueOf(model.getMicPhoto()));
                    data.put("miceTimingPower", String.valueOf(model.getMiceTiming()));
                    data.put("userNamePower", String.valueOf(model.getUserName()));
                    data.put("replytextDataPower", String.valueOf(model.getReplytextData()));
                    data.put("replyKeyPower", String.valueOf(model.getReplyKey()));
                    data.put("replyTypePower", String.valueOf(model.getReplyType()));
                    data.put("replyOldDataPower", String.valueOf(model.getReplyOldData()));
                    data.put("replyCrtPostionPower", String.valueOf(model.getReplyCrtPostion()));
                    data.put("modelIdPower", String.valueOf(model.getModelId()));
                    data.put("receiverUidPower", String.valueOf(model.getReceiverUid()));
                    data.put("forwaredKeyPower", String.valueOf(model.getForwaredKey()));
                    data.put("groupNamePower", String.valueOf(model.getGroupName()));
                    data.put("docSizePower", String.valueOf(model.getDocSize()));
                    data.put("fileNamePower", String.valueOf(model.getFileName()));
                    data.put("thumbnailPower", String.valueOf(model.getThumbnail()));
                    data.put("fileNameThumbnailPower", String.valueOf(model.getFileNameThumbnail()));
                    data.put("captionPower", String.valueOf(model.getCaption()));
                    data.put("notificationPower", String.valueOf(model.getNotification()));
                    data.put("currentDatePower", String.valueOf(model.getCurrentDate()));
                    data.put("userFcmToken", String.valueOf(userFcmToken));

                    // Create the message object
                    messageObject = new JSONObject();
                    messageObject.put("token", userFcmToken);
                    messageObject.put("notification", notification); // Include notification field
                    messageObject.put("data", data); // Include custom data field

                    // Wrap the message object
                    notificationJson.put("message", messageObject);

                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception
                }


            } else {
            }


//            OkHttpClient client = new OkHttpClient();
//         //   RequestBody body = RequestBody.create(notificationJson.toString(), MediaType.parse("application/json; charset=utf-8"));
//            Accesstoken accessToken = new Accesstoken();
//            String accessTokenKey = accessToken.getAccessToke();
//            okhttp3.Request request = new Request.Builder()
//                    .url(FCM_URL)
//                    .post(body)
//
//                    .addHeader("Authorization", "Bearer " + accessTokenKey)
//                    .build();
//            Response response = null;
//            Log.d("TAG", "accesstokenkey: "+accessTokenKey);
//            Log.d("TAG", "accesstokenkey: "+notificationJson.toString());
//
//            response = client.newCall(request).execute();
//            System.out.println("Response *: " + response.body().string());
        } catch (Exception e) {

        }

    }
}
