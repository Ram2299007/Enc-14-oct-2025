package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationsSender {

    private String userFcmToken;
    private String title;
    private String body;
    private Context mContext;
    private Activity mActivity;
    private String userName;
    private String meetingId;
    private String phone;
    private String photo;
    private String sampleToken;
    private String uid;
    private String callerId;
    private String device_type;
    private String username;
    private String createdBy;
    private String incoming;
    private  String roomId;
    private static final String PROJECT_ID = "enclosure-30573";

    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    private JSONObject notificationJson;

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity, String userName, String meetingId, String phone, String photo, String sampleToken, String uid, String callerId, String device_type, String username, String createdBy, String incoming, String roomId) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.userName = userName;
        this.meetingId = meetingId;
        this.phone = phone;
        this.photo = photo;
        this.sampleToken = sampleToken;
        this.uid = uid;
        this.callerId = callerId;
        this.device_type = device_type;
        this.username = username;
        this.createdBy = createdBy;
        this.incoming = incoming;
        this.roomId = roomId;
    }

    public void SendNotifications() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject mainObj = new JSONObject();
                    JSONObject messageObject = new JSONObject();
                    mainObj.put("to", userFcmToken);

                    if (device_type.equals("1")) {
                        // Android device
                        notificationJson = new JSONObject();

                        JSONObject extraData = createExtraData();

                        messageObject.put("token", userFcmToken);
                        messageObject.put("data", extraData);

                        notificationJson.put("message", messageObject);

                    } else if (device_type.equals("2")) {
                        // iOS device
                        notificationJson = new JSONObject();

                        JSONObject notification = new JSONObject();
                        notification.put("title", title);
                        notification.put("body", body);
                        notification.put("sound", "default");

                        JSONObject extraData = createExtraData();

                        messageObject.put("token", userFcmToken);
                        messageObject.put("notification", notification);
                        messageObject.put("data", extraData);

                        notificationJson.put("message", messageObject);
                    }

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), notificationJson.toString());

                    Accesstoken accessToken = new Accesstoken();
                    String accessTokenKey = accessToken.getAccessToke();

                    Request request = new Request.Builder()
                            .url(FCM_URL)
                            .post(requestBody)
                            .addHeader("Authorization", "Bearer " + accessTokenKey)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.body() != null) {
                        System.out.println("Response: " + response.body().string());
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JSONObject createExtraData() throws JSONException {
        JSONObject extraData = new JSONObject();
        extraData.put("name", userName);
        extraData.put("title", title);
        extraData.put("body", body);
        extraData.put("icon", "notification_icon");

        if (body.equals(Constant.voicecall)) {
            extraData.put("click_action", "OPEN_VOICE_CALL");
        } else if (body.equals(Constant.videocall)) {
            extraData.put("click_action", "OPEN_VIDEO_CALL");
        }

        extraData.put("meetingId", meetingId);
        extraData.put("phone", phone);
        extraData.put("photo", photo);
        extraData.put("token", sampleToken);
        extraData.put("uid", uid);
        extraData.put("receiverId", callerId);
        extraData.put("device_type", device_type);
        extraData.put("userFcmToken", userFcmToken);
        extraData.put("username", username);
        extraData.put("createdBy", createdBy);
        extraData.put("incoming", incoming);
        extraData.put("bodyKey", body);
        extraData.put("roomId", roomId);

        return extraData;
    }
}
