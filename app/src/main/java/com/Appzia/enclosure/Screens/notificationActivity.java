package com.Appzia.enclosure.Screens;

import android.os.Bundle;

import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.databinding.ActivityNotificationBinding;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class notificationActivity extends AppCompatActivity {
    ActivityNotificationBinding binding;
    private static final String PROJECT_ID = "enclosure-30573";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String JSON_STRING = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"enclosure-30573\",\n" +
            "  \"private_key_id\": \"0214c5bb83d50e5d11d72ba8d3e4ebba7d313677\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD04bnvGLULC8tn\\nU6wBT6ymn3axN5l3UTyaoNjGGH9CNK0Qx1r5tsreXqfw/iDsy5p/Wsjc5WGWBcrI\\nxIsMb820tM1v1Gscv8LxcyHwovlPYguseFLsWJ+Nc/TnsVS34Ykuf11iWYWVJBXF\\n1K1MGuDhjuIB+5GosOpw72yrZYVVJhWppiu00YX/193IFxZgScF45DydWZ8Hu3Q3\\n83ZWT3Q7IWJGcwpApBLjW6Cb9ccG9yrGVYkDvq5FpqVrlRBxfRtKFLZQnGuJyuZE\\nxAQYMrWE12Hhqrz1zrivJkuS4mX8AniYqKI2rUpAenlpn4bFmmNXFv9FCDfULCOz\\nOB8VzLz5AgMBAAECggEABFCAeM3z9/Xakj950FbEW/XYntaz8CjmQHMvs+Mf8DKt\\nZJZJQWJka0vk+ZdV99YT1W/sCg2gjTFyQ9ydS+LMZQVVI/CXfTIuZRf6M8XV+VK+\\nPJOszQKNYm316qnH1wA07TTL7b0AtYKlP48NCUI6pBQNt1XkrcGFipKCqk0SRFBr\\n+MiF8qr+fjrOEwt12q6sOYlHEHfAAsFGq4yJgnHudVPklcxIFYiW6JgmvDSTFHXV\\nYXQNHhZ+zlicdlE+dwu2mPfvn9dJgRf4Enjl3araA03Ga39uCq21ii0D8AqgwtPp\\nxguJ5wmcBbf1b7hHIDG0P0uTdCbTMi44qW8uvwbp4QKBgQD/6vSc2CjQtFaqir0C\\ni5OSDKBa1h3TKBRKcmPsbG3OPTsX+a3u1PN9hDBRaaX9/TeEuDQpC0t2WAs7df1i\\n1Q1WqUbDnsBA70imBkUuV7THogxLT5vbtx8FryTPt7GA1nRxUZct0kc2TG+lm1Kl\\nnKxIhyS/ULRckusg7AADdszfvwKBgQD09dz/q2DppH3hiUeM2jYhx0a0dVD5bwj9\\niujf3eKhbR6fpwj56OFC7dzTQYp5laqMMw9dIaA/uR4LAoRKOKDFXSOLR65YCxse\\nmCV8NDZ2RsHWb6cTCA2nytIDTsw1hqljEwuN1bnxz6+rrIQeiuOpE2KQa9dAuPVL\\nkznXb6ERRwKBgQDnShO1RO7uYG4LR8Q27qp6TosGTYk683gTKHsCi6RZxqEHtBHs\\nTe2ZvMRmb9MjT5zDiC8sARc8Z6oPHT3Z+q9JaUeZOHqMtTW1RulzTrUFz4DI97Pm\\nyQNyga4FRQFZbXhjidfWA7t0aXRl+ZCiOIzEJ8+gUHIRUH7MjD4e41mZxQKBgBEx\\nkKmBZfQAT7Wc5SDF0Dbevd+8vEpFuOPS9DWCZX3fIt8h4kdoSSdherZ5SzbtgmME\\n0nc+/Ph8DdfH/XEYOHCh8PS9u0cCwIyNMVReddQnc0OR4rA7SHoWilchGMRJB2qk\\n05LJBZwrb7ElEsDyDri3W5u3dgxc7xq24sB0XWHRAoGAaGIdVSYh/9UJEorTNTAl\\n/pWGF0f2eqcNu/zzWxSboAYEu8IXsVj42nb2C4wkBC2IDVXvqez70Y2eCDYwu1Uj\\npr7ohx6rJVssvjI2jzQKCa0KRR8W9WBzkC9fyspnBEJzpZyLz+UC6dkV7pA7vEyl\\njVn2aZOkuUaPlkdoVzF8VO0=\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"firebase-adminsdk-nulab@enclosure-30573.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"118076563992961353315\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-nulab%40enclosure-30573.iam.gserviceaccount.com\",\n" +
            "  \"universe_domain\": \"googleapis.com\"\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, binding.getRoot());


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


        InputStream stream = new ByteArrayInputStream(JSON_STRING.getBytes(StandardCharsets.UTF_8));

        // Load credentials
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(stream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));

        // Check if credentials are loaded successfully
        if (googleCredentials == null) {
            throw new RuntimeException("Failed to load GoogleCredentials");
        }

        // Request an access token
        googleCredentials.refreshIfExpired();
        AccessToken accessToken = googleCredentials.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("Failed to obtain access token");
        }

        String tokenValue = accessToken.getTokenValue();
        System.out.println("Access Token: " + tokenValue);


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
                .addHeader("Authorization", "Bearer ya29.c.c0ASRK0GY3_4yHanVM7Xs54u-HpEUuV0xGmTipboTFE3kdjGiD8y2GpLzWJ0Oi2RRwAqnoQx1VJb_SJk7oHa95h3fZQ-qDpaXeWpO-UglMq2DjSpYzgL4in6DgzS02kKozw5xyBQuxiqmui4fBJJ-8gL-Q1OR1xanNKFGjQe8P59iQhsGq74o0IhCbhKisvd1qupU4EvNh-ie7DIKbcKkLKe9QF4fRkAHVmP5X6u05FKb7Y2REbQaxye7woCqwpE1a7vi7gBJPsfJSG6u6rnSLbm3NZAWdBYn31FotGY_43sYz7MPMXld5pEQDtvftqKx9tfPXvKa1B4ZqeW8loQuxVfco9rIx-0v_5cEmp2ig8SU2o7FxwQhBrooT384CW5YQMlvZ0w_fRXw5Jdrtv8Sc18JWskt-q1qUwobwzh9UvhoYqnt6VuRpanVisfmtBRMueeFh7tvwbUvo9h0VQrcXcvjwkF_bBMlRjF7VWIajg3rIcWZ0f1Zo_UXMWb2kgZ_rlck6970fBMSZn1lj8ohw2lztZBvoaB3WlbgMqqX8plmzwh-FJyoJY6mzrvwy9RZW5Q0vdZpiuUhOUX55V73dU4iaoua8v5lgSR2RfVa9bkfBy4s576m9ulc8rY1ZJfrRWYwbjUiy8oRyB3iRgrQmayqavJmfco6j2hqfMqVl7eomcZXQIx72rZ8zvg5vBycjthJmcsc8Bkb-VfIFrX3q7oreBpn0gyrlgot9uBn7aJVpqSsQr_yzqoZOdgwO5Rzr22O_cMmknxtWqmMnqa4tZilVyX4lvQBr9QU47dwJZtJmoIfFgzgZ-UcdY22aVkhkkXprvbjj6rYeRyJMxbBQb_hzo_rJmQer4Q0SBvbaj3bQOVz5v-BZpmvjhQvXoUacVppge0M5M66u2RORWrBRk59qw5ynY-wg5BOv-rsdQ3tu_flMueUQvVqjj_v7J6R29p7Ys3a9s_zy5lQmZ-ShWvy7B9mOf19g080Fdpc9uy0h9YVX2m8pjai")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("Response: " + response.body().string());
    }
}