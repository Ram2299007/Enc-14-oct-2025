package com.Appzia.enclosure.Utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Accesstoken {

    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";
    private static final Log log = LogFactory.getLog(Accesstoken.class);


    public String getAccessToke() {
        try {
            android.util.Log.d("TAG", "Starting to fetch access token...");
            
            String jsonString = "{\n" +
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
                    "}\n";

            android.util.Log.d("TAG", "Service account JSON created successfully");
            
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            android.util.Log.d("TAG", "InputStream created from JSON string");
            
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList(firebaseMessagingScope));
            
            android.util.Log.d("TAG", "GoogleCredentials created and scoped successfully");

            android.util.Log.d("TAG", "Refreshing credentials if expired...");
            googleCredentials.refreshIfExpired(); // safer than refresh()
            android.util.Log.d("TAG", "Credentials refreshed successfully");

            if (googleCredentials.getAccessToken() != null) {
                String tokenValue = googleCredentials.getAccessToken().getTokenValue();
                android.util.Log.d("TAG", "Access token obtained successfully, length: " + (tokenValue != null ? tokenValue.length() : 0));
                return tokenValue;
            } else {
                android.util.Log.e("TAG", "Failed to fetch access token: token is null");
                return null;
            }


        } catch (IOException e) {
            android.util.Log.e("TAG", "getAccessToke IOException: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            android.util.Log.e("TAG", "getAccessToke Exception: " + e.getMessage(), e);
            return null;
        }
    }
}
