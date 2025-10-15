package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.Appzia.enclosure.Model.selectionBunchModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;

/**
 * Helper class to break down large notification API methods
 * to comply with Google Play's 16 KB page size requirement
 */
public class NotificationApiHelper {
    private static final String TAG = "NotificationApiHelper";
    private static final String BASE_URL = "https://confidential.enclosureapp.com/";

    /**
     * Handles group notification API calls by breaking down the large method
     */
    public static void handleGroupNotification(Context mContext, String userName, String message,
                                               String senderId, String userName1, String profile,
                                               String sentTime, String deviceType, String uid,
                                               String message1, String time, String document,
                                               String dataType, String extension, String name,
                                               String phone, String micPhoto, String miceTiming,
                                               String userName2, String replytextData, String replyKey,
                                               String replyType, String replyOldData, String replyCrtPostion,
                                               String modelId, String receiverUid, String forwaredKey,
                                               String groupName, String docSize, String fileName,
                                               String thumbnail, String fileNameThumbnail, String caption,
                                               int notification, String currentDate, String senderTokenReplyPower,
                                               JSONArray group_members, long timestamp, ProgressBar progressBar,
                                               String imageWidth, String imageHeight, String aspectRatio, String selectionCount, ArrayList<selectionBunchModel> selectionBunch) {
        
        try {
            // Get access token
            String accessTokenKey = getAccessToken();
            
            // Create request JSON
            JSONObject requestJson = buildRequestJson(userName, message, senderId, userName1, profile,
                    sentTime, deviceType, uid, message1, time, document, dataType, extension, name,
                    phone, micPhoto, miceTiming, userName2, replytextData, replyKey, replyType,
                    replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey, groupName,
                    docSize, fileName, thumbnail, fileNameThumbnail, caption, notification,
                    currentDate, senderTokenReplyPower, group_members, timestamp, imageWidth,
                    imageHeight, aspectRatio, selectionCount,selectionBunch);

            // Process group members
            processGroupMembers(senderId, group_members);

            // Send notification
            sendNotificationRequest(mContext, requestJson);

        } catch (Exception e) {
            Log.e(TAG, "Error in handleGroupNotification: " + e.getMessage(), e);
        }
    }

    /**
     * Gets access token asynchronously
     */
    private static String getAccessToken() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> new Accesstoken().getAccessToke());
        return future.get();
    }

    /**
     * Builds the request JSON object
     */
    private static JSONObject buildRequestJson(String userName, String message, String senderId,
                                               String userName1, String profile, String sentTime,
                                               String deviceType, String uid, String message1,
                                               String time, String document, String dataType,
                                               String extension, String name, String phone,
                                               String micPhoto, String miceTiming, String userName2,
                                               String replytextData, String replyKey, String replyType,
                                               String replyOldData, String replyCrtPostion,
                                               String modelId, String receiverUid, String forwaredKey,
                                               String groupName, String docSize, String fileName,
                                               String thumbnail, String fileNameThumbnail,
                                               String caption, int notification, String currentDate,
                                               String senderTokenReplyPower, JSONArray group_members,
                                               long timestamp, String imageWidth, String imageHeight,
                                               String aspectRatio, String selectionCount, ArrayList<selectionBunchModel> selectionBunch) throws JSONException {
        
        String truncatedMessage = Webservice.truncateToWordss(message, 100);
        
        JSONObject requestJson = new JSONObject();
        requestJson.put("accessToken", senderTokenReplyPower);
        requestJson.put("accessTokenKey", senderTokenReplyPower);
        requestJson.put("userFcmToken", senderTokenReplyPower);
        requestJson.put("senderTokenReply", senderTokenReplyPower);
        requestJson.put("title", String.valueOf(userName));
        requestJson.put("body", String.valueOf(truncatedMessage));
        requestJson.put("receiverKey", String.valueOf(senderId));
        requestJson.put("user_name", String.valueOf(userName1));
        requestJson.put("photo", String.valueOf(profile));
        requestJson.put("currentDateTimeString", String.valueOf(sentTime));
        requestJson.put("deviceType", String.valueOf(deviceType));
        requestJson.put("bodyKey", Constant.chatting);
        requestJson.put("click_action", "OPEN_ACTIVITY_1");
        requestJson.put("icon", "notification_icon");
        requestJson.put("uid", String.valueOf(uid));
        requestJson.put("message", String.valueOf(truncatedMessage));
        requestJson.put("time", String.valueOf(time));
        requestJson.put("document", String.valueOf(document));
        requestJson.put("dataType", String.valueOf(dataType));
        requestJson.put("extension", String.valueOf(extension));
        requestJson.put("name", String.valueOf(name));
        requestJson.put("phone", String.valueOf(phone));
        requestJson.put("miceTiming", String.valueOf(miceTiming));
        requestJson.put("micPhoto", String.valueOf(micPhoto));
        requestJson.put("userName", String.valueOf(userName2));
        requestJson.put("replytextData", String.valueOf(replytextData));
        requestJson.put("replyKey", String.valueOf(replyKey));
        requestJson.put("replyType", String.valueOf(replyType));
        requestJson.put("replyOldData", String.valueOf(replyOldData));
        requestJson.put("replyCrtPostion", String.valueOf(replyCrtPostion));
        requestJson.put("modelId", String.valueOf(modelId));
        requestJson.put("receiverUid", String.valueOf(receiverUid));
        requestJson.put("forwaredKey", String.valueOf(forwaredKey));
        requestJson.put("groupName", String.valueOf(groupName));
        requestJson.put("docSize", String.valueOf(docSize));
        requestJson.put("fileName", String.valueOf(fileName));
        requestJson.put("thumbnail", String.valueOf(thumbnail));
        requestJson.put("fileNameThumbnail", String.valueOf(fileNameThumbnail));
        requestJson.put("caption", String.valueOf(caption));
        requestJson.put("notification", String.valueOf(notification));
        requestJson.put("currentDate", String.valueOf(currentDate));
        requestJson.put("group_members", group_members);
        requestJson.put("timestamp", timestamp);
        requestJson.put("imageWidthDp", imageWidth);
        requestJson.put("imageHeightDp", imageHeight);
        requestJson.put("aspectRatio", aspectRatio);
        requestJson.put("selectionCount", String.valueOf(selectionCount));

        if (selectionBunch != null && !selectionBunch.isEmpty()) {
            JSONArray selectionBunchArray = new JSONArray();
            for (selectionBunchModel bunchModel : selectionBunch) {
                if (bunchModel == null) continue;
                JSONObject bunchObject = new JSONObject();
                bunchObject.put("imgUrl", bunchModel.getImgUrl() != null ? bunchModel.getImgUrl() : "");
                bunchObject.put("fileName", bunchModel.getFileName() != null ? bunchModel.getFileName() : "");
                selectionBunchArray.put(bunchObject);
            }
            requestJson.put("selectionBunch", selectionBunchArray);
        } else {
            requestJson.put("selectionBunch", new JSONArray());
        }
 
        return requestJson;
    }

    /**
     * Processes group members for individual chatting
     */
    private static void processGroupMembers(String senderId, JSONArray group_members) {
        try {
            for (int i = 0; i < group_members.length(); i++) {
                JSONObject member = group_members.getJSONObject(i);
                String friendId = member.getString("friend_id");
                Log.d(TAG, "Processing friend ID: " + friendId);
                Webservice.get_individual_chattingUnion(senderId, friendId);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error processing group members: " + e.getMessage(), e);
        }
    }

    /**
     * Sends the notification request
     */
    private static void sendNotificationRequest(Context mContext, JSONObject requestJson) {
        try {
            // Validate access token before making the request
            String accessToken = requestJson.optString("accessToken");
            if (accessToken == null || accessToken.isEmpty()) {
                Log.e(TAG, "Invalid access token, skipping FCM notification");
                return;
            }
            
            Log.d(TAG, "Sending FCM notification with access token length: " + accessToken.length());
            
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setConnectTimeout(10000);
            client.setResponseTimeout(20000);
            client.setTimeout(30000);

            cz.msebera.android.httpclient.entity.StringEntity entity = 
                new cz.msebera.android.httpclient.entity.StringEntity(requestJson.toString(), "UTF-8");

            client.post(mContext, BASE_URL + "EmojiController/end_notification_api_group", 
                       entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "Notification API success: " + response.toString());
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, "Notification API failure: " + responseString, throwable);
                    
                    // Log specific error details for debugging
                    if (statusCode == 401) {
                        Log.e(TAG, "FCM Authentication failed - Check service account permissions and credentials");
                    } else if (statusCode == 403) {
                        Log.e(TAG, "FCM Permission denied - Service account may not have FCM access");
                    } else if (statusCode >= 500) {
                        Log.e(TAG, "FCM Server error - Retry later");
                    }
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(TAG, "Notification API failure with error response", throwable);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.e(TAG, "Notification API failure with array error response", throwable);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error sending notification request: " + e.getMessage(), e);
        }
    }
}
