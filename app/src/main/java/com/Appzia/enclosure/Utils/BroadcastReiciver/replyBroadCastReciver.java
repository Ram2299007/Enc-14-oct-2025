package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class replyBroadCastReciver extends BroadcastReceiver {
    String TAG = "PowerIntentDebug";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e("ReplyReceiver", "Received null intent or action");
            return;
        }

        // Verify the action matches the expected action
        int notificationId = intent.getIntExtra("notificationId", 0);
        String expectedAction = "com.Appzia.enclosure.REPLY_ACTION_" + notificationId;
        if (!intent.getAction().equals(expectedAction)) {
            Log.e("ReplyReceiver", "Invalid action: " + intent.getAction() + ", expected: " + expectedAction);
            return;
        }

        // Extract RemoteInput reply
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput == null) {
            Log.e("ReplyReceiver", "No RemoteInput found in intent");
            return;
        }

        CharSequence replyText = remoteInput.getCharSequence("key_text_reply_" + notificationId);
        if (replyText == null || replyText.toString().trim().isEmpty()) {
            Log.e("ReplyReceiver", "Empty or null reply text");
            return;
        }

        // Extract intent extras
        String uidPower = intent.getStringExtra("uidPower");
        String dataTypePower = intent.getStringExtra("dataTypePower");
        String extensionPower = intent.getStringExtra("extensionPower");
        String namePower = intent.getStringExtra("namepower");
        String phonePower = intent.getStringExtra("phonePower");
        String miceTimingPower = intent.getStringExtra("miceTimingPower");
        String receiverUidPower = intent.getStringExtra("receiverUidPower");
        String userFcmTokenPower = intent.getStringExtra("userFcmTokenPower");
        String senderTokenReplyPower = intent.getStringExtra("senderTokenReplyPower");
        String replyKeyPower = intent.getStringExtra("replyKeyPower");
        String replyTypePower = intent.getStringExtra("replyTypePower");
        String replyOldDataPower = intent.getStringExtra("replyOldDataPower");
        String replyCrtPostionPower = intent.getStringExtra("replyCrtPostionPower");
        String modelIdPower = intent.getStringExtra("modelIdPower");
        String forwaredKeyPower = intent.getStringExtra("forwaredKeyPower");
        String groupNamePower = intent.getStringExtra("groupNamePower");
        String docSizePower = intent.getStringExtra("docSizePower");
        String fileNamePower = intent.getStringExtra("fileNamePower");
        String thumbnailPower = intent.getStringExtra("thumbnailPower");
        String fileNameThumbnailPower = intent.getStringExtra("fileNameThumbnailPower");
        String captionPower = intent.getStringExtra("captionPower");
        String notificationPower = intent.getStringExtra("notificationPower");
        String currentDatePower = intent.getStringExtra("currentDatePower");
        String receiverKey = intent.getStringExtra("receiverKey");



        // Log all values at once
        Log.d(TAG, "Intent extras: " +
                "\nuidPower=" + uidPower +
                "\ndataTypePower=" + dataTypePower +
                "\nextensionPower=" + extensionPower +
                "\nnamePower=" + namePower +
                "\nphonePower=" + phonePower +
                "\nmiceTimingPower=" + miceTimingPower +
                "\nreceiverUidPower=" + receiverUidPower +
                "\nuserFcmTokenPower=" + userFcmTokenPower +
                "\nsenderTokenReplyPower=" + senderTokenReplyPower +
                "\nreplyKeyPower=" + replyKeyPower +
                "\nreplyTypePower=" + replyTypePower +
                "\nreplyOldDataPower=" + replyOldDataPower +
                "\nreplyCrtPostionPower=" + replyCrtPostionPower +
                "\nmodelIdPower=" + modelIdPower +
                "\nforwaredKeyPower=" + forwaredKeyPower +
                "\ngroupNamePower=" + groupNamePower +
                "\ndocSizePower=" + docSizePower +
                "\nfileNamePower=" + fileNamePower +
                "\nthumbnailPower=" + thumbnailPower +
                "\nfileNameThumbnailPower=" + fileNameThumbnailPower +
                "\ncaptionPower=" + captionPower +
                "\nnotificationPower=" + notificationPower +
                "\ncurrentDatePower=" + currentDatePower +
                "\nreceiverKey=" + receiverKey
        );
        // Log extracted data for debugging
        Log.d("ReplyReceiver", "Received reply: " + replyText + ", uidPower: " + uidPower +
                ", receiverUidPower: " + receiverUidPower + ", userFcmTokenPower: " + userFcmTokenPower);

        // Initialize Firebase and other variables
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        File dummyFile = null;

        // Get user photo from SharedPreferences
        String photo = Constant.getSF.getString(Constant.full_name, "");

        // Format current timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(new Date());

        // Determine sender and receiver UIDs
        Constant.getSfFuncion(context);
        String finalUid = Constant.getSF.getString(Constant.UID_KEY, "");
        // Null-safe comparison and fallback
        String safeUidPower = uidPower == null ? "" : uidPower;
        String safeReceiverUidPower = receiverUidPower == null ? "" : receiverUidPower;
        String finalRecId;
        if (!safeUidPower.isEmpty() && safeUidPower.equals(finalUid)) {
            finalRecId = safeReceiverUidPower;
        } else if (!safeUidPower.isEmpty()) {
            finalRecId = safeUidPower;
        } else {
            // If uidPower is missing, fall back to receiverUidPower
            finalRecId = safeReceiverUidPower;
        }

        if (finalRecId == null || finalRecId.trim().isEmpty()) {
            Log.e("ReplyReceiver", "Missing both uidPower and receiverUidPower; cannot determine recipient. Aborting.");
            return;
        }

        // Create sender and receiver rooms
        String senderRoom = finalUid + receiverKey;
        String receiverRoom = receiverKey + finalUid;
        String messagePower = replyText.toString();
        String userNamePower = Constant.getSF.getString(Constant.full_name, "");

        // Log additional details
        Log.d("ReplyReceiver", "senderRoom: " + senderRoom + ", receiverRoom: " + receiverRoom +
                ", messagePower: " + messagePower + ", finalUid: " + finalUid + ", finalRecId: " + finalRecId +"receiverKey "+receiverKey +"userFcmTokenPower :"+userFcmTokenPower);

        // Create message model
        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));
        messageModel model = new messageModel(
                finalUid, messagePower, currentDateTimeString, "", Constant.Text, "", "", "", "", "",
                userNamePower, "", "", "", "", "", database.getReference().push().getKey(),
                finalRecId, "", "", "", "", "", "", "", 1, Constant.getCurrentDate(),
                emojiModels, "", Constant.getCurrentTimestamp(),"", "", "", "1"
        );

        // Use UploadChatHelper to send a TEXT message reply
        try {
            String modelId = database.getReference().push().getKey();

            UploadChatHelper uploadHelper = new UploadChatHelper(context, null, null, finalUid, userFcmTokenPower);
            uploadHelper.uploadContent(
                    Constant.Text,   // uploadType
                    null,            // uri
                    messagePower,    // captionText/message
                    modelId,         // modelId
                    null,            // savedThumbnail
                    null,            // fileThumbName
                    null,            // fileName
                    null,            // contactName
                    null,            // contactPhone
                    null,            // audioTime
                    null,            // audioName
                    null,            // extension
                    receiverKey,      // receiverUid
                    replyCrtPostionPower, // replyCrtPostion
                    replyKeyPower,        // replyKey
                    replyOldDataPower,    // replyOldData
                    replyTypePower,       // replyType
                    "",                  // replytextData (not available here)
                    dataTypePower,        // replydataType
                    fileNamePower,        // replyfilename
                    forwaredKeyPower,     // forwaredKey
                    null,                 // imageWidthDp
                    null,                 // imageHeightDp
                    null                  // aspectRatio
            );

            // Dismiss the notification
            androidx.core.app.NotificationManagerCompat notificationManager = androidx.core.app.NotificationManagerCompat.from(context);
            notificationManager.cancel(notificationId);
            Log.d("ReplyReceiver", "Notification dismissed for ID: " + notificationId);
        } catch (Exception e) {
            Log.e("ReplyReceiver", "Error processing reply via UploadChatHelper: " + e.getMessage(), e);
        }
    }
}
