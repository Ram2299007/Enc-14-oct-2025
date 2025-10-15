package com.Appzia.enclosure.Utils;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class FcmSenderV1 {

    private static final String PROJECT_ID = "enclosure-30573";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String SERVICE_ACCOUNT_KEY_PATH = "app/service-account.json";

    public static void main(String[] args) {
        try {
            String token = "f_iO5rSFRiWqFbCa0RgkiA:APA91bGEilB7s15Eyzvurgsp_-dx4v_5TukaRvrFihIz9IMlQGS0Ywghhm1wKrlHF66Itg4LxXbWhiUC8_444tZWTAPgC0H2JbNQLOHDm8ZRmovlevuUwbAwUOt1sh2upJocljlLe9Lg"; // Replace with the device token
            String message = "Hello from FCM v1 API!";

            sendFcmNotification(token, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendFcmNotification(String targetToken, String messageBody) throws IOException, JSONException {

        Log.d("csac", "sendFcmNotification: ");
        // Load the credentials from the service account file
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(Files.newInputStream(Paths.get(SERVICE_ACCOUNT_KEY_PATH)))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();

        String accessToken = googleCredentials.getAccessToken().getTokenValue();

        OkHttpClient client = new OkHttpClient();

        JSONObject messageJson = new JSONObject();
        JSONObject notificationJson = new JSONObject();
        JSONObject dataJson = new JSONObject();

        // Construct the notification payload
        notificationJson.put("title", "Your Title");
        notificationJson.put("body", messageBody);

        dataJson.put("key1", "value1"); // Custom data payload (optional)

        messageJson.put("notification", notificationJson);
        messageJson.put("data", dataJson);
        messageJson.put("token", targetToken);

        JSONObject json = new JSONObject();
        json.put("message", messageJson);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        // Make the HTTP request with Authorization header
        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("Response: " + response.body().string());
    }
}
