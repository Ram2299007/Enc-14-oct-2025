package com.Appzia.enclosure.Utils;

import android.util.Log;

import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;

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

public class FcmNotificationSenderMisscall {

    private String userFcmToken;
    private String title;
    private String body;
    private String userName;
    private String photo;
    private String device_type;

    private static final String PROJECT_ID = "enclosure-30573";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
    private JSONObject notificationJson;

    public FcmNotificationSenderMisscall(String userFcmToken, String title, String body, String photo, String userName, String device_type) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.photo = photo;
        this.userName = userName;
        this.device_type = device_type;
    }

    public void SendNotifications() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject messageObject = new JSONObject();

                    if (device_type.equals("1")) {
                        // Android
                        JSONObject extraData = createExtraData();

                        messageObject.put("token", userFcmToken);
                        messageObject.put("data", extraData);

                        notificationJson = new JSONObject();
                        notificationJson.put("message", messageObject);

                    } else if (device_type.equals("2")) {
                        // iOS
                        JSONObject notification = new JSONObject();
                        notification.put("title", title);
                        notification.put("body", body);
                        notification.put("sound", "default");

                        JSONObject extraData = createExtraData();

                        messageObject.put("token", userFcmToken);
                        messageObject.put("notification", notification);
                        messageObject.put("data", extraData);

                        notificationJson = new JSONObject();
                        notificationJson.put("message", messageObject);
                    }

                    sendFcmNotification();

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
        extraData.put("bodyKey", body);
        extraData.put("icon", "notification_icon");
        extraData.put("photo", photo);
        extraData.put("device_type", device_type);
        return extraData;
    }

    private void sendFcmNotification() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), notificationJson.toString());

        Accesstoken accessToken = new Accesstoken();
        String accessTokenKey = accessToken.getAccessToke();

        Log.d("accessTokenKey", "sendFcmNotification: "+notificationJson.toString());


        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessTokenKey)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Notification sent successfully. Response: " + response.body().string());
            } else {
                System.out.println("Failed to send notification. Response code: " + response.code() + ", Response: " + response.body().string());
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
