package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;

import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.chattingScreen;
//import com.Appzia.enclosure.Screens.videoCallingTwo;
import com.Appzia.enclosure.Utils.BroadcastReiciver.DeclineCallReceiver;
import com.Appzia.enclosure.Utils.BroadcastReiciver.DeclineVideoCallReceiver;
import com.Appzia.enclosure.Utils.BroadcastReiciver.replyBroadCastReciver;


import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.Appzia.enclosure.activities.MainActivityVoiceCall;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    FirebaseDatabase database;
    //todo This is for chatting and 100 for video and voice call
    private static int notificationId = 200;
    private Handler handler;
    private Runnable timerRunnable;


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Constant.getSfFuncion(this);
        String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


        //when user in sleep mode then do not desturb functionlity will on
        if (sleepKey.equals(Constant.sleepKey)) {

        } else {

            // vibration
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] vibrationPattern = {0, 500, 200, 500};


            database = FirebaseDatabase.getInstance();


            Bitmap iconLarge = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.notiiconmodern);


            String userName = remoteMessage.getData().get("name");
            String message = remoteMessage.getData().get("msgKey");
            String meetingId = remoteMessage.getData().get("meetingId");
            String phone = remoteMessage.getData().get("phone");
            String photo = remoteMessage.getData().get("photo");
            String sampleToken = remoteMessage.getData().get("token");
            String senderId = remoteMessage.getData().get("uid");
            String receiverId = remoteMessage.getData().get("receiverId");
            String bodyKey = remoteMessage.getData().get("bodyKey");
            String receiverKey = remoteMessage.getData().get("friendUidKey");
            String user_nameKey = remoteMessage.getData().get("user_nameKey");
            String currentDateTimeString = remoteMessage.getData().get("currentDateTimeString");
            String device_type = remoteMessage.getData().get("device_type");
            String title = remoteMessage.getData().get("title");
            String userFcmToken = remoteMessage.getData().get("userFcmToken");

            String usernameWeb = remoteMessage.getData().get("username");
            String createdByWeb = remoteMessage.getData().get("createdBy");
            String incomingWeb = remoteMessage.getData().get("incoming");

            /// power - reply

            String uidPower = remoteMessage.getData().get("uidPower");
            String messagePower = remoteMessage.getData().get("messagePower");
            String timePower = remoteMessage.getData().get("timePower");
            String documentPower = remoteMessage.getData().get("documentPower");
            String dataTypePower = remoteMessage.getData().get("dataTypePower");
            String extensionPower = remoteMessage.getData().get("extensionPower");
            String namePower = remoteMessage.getData().get("namepower");
            String phonePower = remoteMessage.getData().get("phonePower");
            String micPhotoPower = remoteMessage.getData().get("micPhotoPower");
            String miceTimingPower = remoteMessage.getData().get("miceTimingPower");
            String userNamePower = remoteMessage.getData().get("userNamePower");
            String replytextDataPower = remoteMessage.getData().get("replytextDataPower");
            String replyKeyPower = remoteMessage.getData().get("replyKeyPower");
            String replyTypePower = remoteMessage.getData().get("replyTypePower");
            String replyOldDataPower = remoteMessage.getData().get("replyOldDataPower");
            String replyCrtPostionPower = remoteMessage.getData().get("replyCrtPostionPower");
            String modelIdPower = remoteMessage.getData().get("modelIdPower");
            String receiverUidPower = remoteMessage.getData().get("receiverUidPower");
            String forwaredKeyPower = remoteMessage.getData().get("forwaredKeyPower");
            String groupNamePower = remoteMessage.getData().get("groupNamePower");
            String docSizePower = remoteMessage.getData().get("docSizePower");
            String fileNamePower = remoteMessage.getData().get("fileNamePower");
            String thumbnailPower = remoteMessage.getData().get("thumbnailPower");
            String fileNameThumbnailPower = remoteMessage.getData().get("fileNameThumbnailPower");
            String captionPower = remoteMessage.getData().get("captionPower");
            String notificationPower = remoteMessage.getData().get("notificationPower");
            String currentDatePower = remoteMessage.getData().get("currentDatePower");
            String userFcmTokenPower = remoteMessage.getData().get("userFcmTokenPower");
            String myFcmOwn = remoteMessage.getData().get("myFcmOwn");
            String senderTokenReplyPower = remoteMessage.getData().get("senderTokenReplyPower");
            String roomId = remoteMessage.getData().get("roomId");
            String selectionCount = remoteMessage.getData().get("selectionCount");


            // Keep optional media-related fields as-is if missing

            // Logging the data with Tag "ForceWord"
            Log.d("ForceWord2025", "uidPower: " + uidPower);
            Log.d("ForceWord2025", "messagePower: " + messagePower);
            Log.d("ForceWord2025", "timePower: " + timePower);
            Log.d("ForceWord2025", "documentPower: " + documentPower);
            Log.d("ForceWord2025", "dataTypePower: " + dataTypePower);
            Log.d("ForceWord2025", "extensionPower: " + extensionPower);
            Log.d("ForceWord2025", "namePower: " + namePower);
            Log.d("ForceWord2025", "phonePower: " + phonePower);
            Log.d("ForceWord2025", "micPhotoPower: " + micPhotoPower);
            Log.d("ForceWord2025", "miceTimingPower: " + miceTimingPower);
            Log.d("ForceWord2025", "userNamePower: " + userNamePower);
            Log.d("ForceWord2025", "replytextDataPower: " + replytextDataPower);
            Log.d("ForceWord2025", "replyKeyPower: " + replyKeyPower);
            Log.d("ForceWord2025", "replyTypePower: " + replyTypePower);
            Log.d("ForceWord2025", "replyOldDataPower: " + replyOldDataPower);
            Log.d("ForceWord2025", "replyCrtPostionPower: " + replyCrtPostionPower);
            Log.d("ForceWord2025", "modelIdPower: " + modelIdPower);
            Log.d("ForceWord2025", "receiverUidPower: " + receiverUidPower);
            Log.d("ForceWord2025", "forwaredKeyPower: " + forwaredKeyPower);
            Log.d("ForceWord2025", "groupNamePower: " + groupNamePower);
            Log.d("ForceWord2025", "docSizePower: " + docSizePower);
            Log.d("ForceWord2025", "fileNamePower: " + fileNamePower);
            Log.d("ForceWord2025", "thumbnailPower: " + thumbnailPower);
            Log.d("ForceWord2025", "fileNameThumbnailPower: " + fileNameThumbnailPower);
            Log.d("ForceWord2025", "captionPower: " + captionPower);
            Log.d("ForceWord2025", "notificationPower: " + notificationPower);
            Log.d("ForceWord2025", "currentDatePower: " + currentDatePower);
            Log.d("ForceWord2025", "userFcmTokenPower: " + userFcmTokenPower);
            Log.d("ForceWord2025", "userFcmTokenPower: " + userFcmToken);
            Log.d("ForceWord2025", "receiverKey: " + receiverKey);
            Log.d("ForceWord2025", "userFcmToken: " + userFcmToken);
            Log.d("ForceWord2025", "userFcmTokenPower: " + userFcmTokenPower);
            Log.d("ForceWord2025", "myFcmOwn: " + myFcmOwn);
            Log.d("ForceWord2025", "selectionCount: " + selectionCount);


            Log.d("TAG", "onMessageReceived: " + title);


            if (bodyKey != null) {

                Log.d("bodyKey", bodyKey);
                if (bodyKey.equals(Constant.voicecall)) {
                    // Signal for notification arrived
                    try {
                        database.getReference().child("NOTIARRIVED").child("status").child(receiverId).setValue("TRUE")
                                .addOnSuccessListener(unused -> {
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Constant.getSfFuncion(this);

                    // Handle call disconnection and missed call notification
                    try {
                        DatabaseReference ref = database.getReference()
                                .child("removeCallNotification")
                                .child(Constant.getSF.getString(Constant.UID_KEY, ""));

                        ChildEventListener listener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                ref.removeValue().addOnSuccessListener(unused -> {
                                    Log.d("xys", "onDataChangecsacsasca: pointedcssddc");

                                    // Stop and release ringtone
                                    if (MainApplication.player != null && MainApplication.player.isPlaying()) {
                                        MainApplication.player.stop();
                                        MainApplication.player.release();
                                        MainApplication.player = null;
                                    }

                                    // Send missed call notification
                                    FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(
                                            userFcmToken, title, Constant.voiceMissCall, photo, userName, device_type);
                                    fmisscall.SendNotifications();

                                    // Record missed call
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date2 = new Date();
                                    String date = dateFormat.format(date2);

                                    Calendar calendar = Calendar.getInstance();
                                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                                    String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

                                    Log.d("currentTime", currentTime);
                                    Log.d("receiverIddytdyxwaux", receiverId + Constant.getSF.getString(Constant.UID_KEY, ""));

                                    Webservice.create_group_callingMissCall(
                                            Constant.getSF.getString(Constant.UID_KEY, ""), senderId, "", date, currentTime, "2", currentTime, "1");

                                    // Cancel notification
                                    NotificationManagerCompat.from(FirebaseMessagingService.this).cancel(100);

                                    // Remove listener to avoid repeated calls
                                    ref.removeEventListener(this);
                                });
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        };

                        ref.addChildEventListener(listener);

                    } catch (Exception e) {
                        Log.e("NotificationDebug", "Error setting up removeCallNotification listener: " + e.getMessage());
                    }


                    Constant.getSfFuncion(this);

                    String voiceRadioKey = Constant.getSF.getString(Constant.voiceRadioKey, "");
                    Log.d("TAG", "onMessageReceived: " + voiceRadioKey);
                    if (voiceRadioKey.equals(Constant.voiceRadioKey)) {
                        // Load sender's photo
                        Bitmap largeIcon = null;
                        try {
                            URL url = new URL(photo);
                            largeIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        } catch (IOException e) {
                            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.deflarge);
                            if (largeIcon == null) {
                                largeIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(largeIcon);
                                canvas.drawColor(0xFFCCCCCC);
                                Log.w("NotificationDebug", "Profile fallback bitmap created (gray square)");
                            }
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        }

                        // Get sender name from DatabaseHelper
                        String originalName = userName;
                        Log.w("receiverKeyacwcaww", "receiverKeydd" + receiverKey);
                        try {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                            get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverKey);
                            if (dataModel.getFull_name() != null) {
                                originalName = dataModel.getFull_name();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "DatabaseHelper error: " + e.getMessage());
                        }
                        assert originalName != null;
                        if(originalName.isEmpty()){
                            originalName ="Unknown";
                        }

                        // Create Person for sender
                        Person sender = new Person.Builder()
                                .setName(originalName)
                                .setIcon(IconCompat.createWithBitmap(largeIcon))
                                .build();

                        // Accept intent (to ConnectingVoiceActivity)
                        Intent acceptIntent = new Intent(this, MainActivityVoiceCall.class);
                        acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        acceptIntent.putExtra("meetingId", meetingId);
                        acceptIntent.putExtra("phone", phone);
                        acceptIntent.putExtra("photoReceiver", photo);
                        acceptIntent.putExtra("nameReceiver", originalName);
                        acceptIntent.putExtra("token", sampleToken);
                        acceptIntent.putExtra("senderId", receiverId);
                        acceptIntent.putExtra("receiverId", senderId);
                        acceptIntent.putExtra("device_type", device_type);
                        acceptIntent.putExtra("acceptKey", "acceptKey");
                        acceptIntent.putExtra("stop_ringtone", true);
                        acceptIntent.putExtra("fromNoti", "fromNoti");
                        acceptIntent.putExtra("username", usernameWeb);
                        acceptIntent.putExtra("createdBy", createdByWeb);
                        acceptIntent.putExtra("incoming", incomingWeb);
                        acceptIntent.putExtra("roomId", roomId);
                        acceptIntent.putExtra(Constant.tapOnNotification, Constant.tapOnNotification);

                        PendingIntent acceptPendingIntent = PendingIntent.getActivity(
                                this, 1, acceptIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));

                        // Decline intent (to BroadcastReceiver)
                        Intent declineIntent = new Intent(this, DeclineCallReceiver.class);
                        declineIntent.putExtra("stop_ringtone", true);
                        declineIntent.putExtra("caller_name", originalName);
                        declineIntent.putExtra("declineVoiceKey", "declineVoiceKey");
                        declineIntent.putExtra("roomVoice", senderId);
                        declineIntent.putExtra("receiverId", receiverId);
                        declineIntent.putExtra("userFcmToken", userFcmToken);
                        declineIntent.putExtra("title", title);
                        declineIntent.putExtra("photo", photo);
                        declineIntent.putExtra("userName", originalName);
                        declineIntent.putExtra("device_type", device_type);
                        declineIntent.putExtra("notificationId", 100);

                        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                                this, 2, declineIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));

                        // Full-screen intent (to FullScreenVoiceIncoming)
                        Intent fullScreenIntent = new Intent(this, FullScreenVoiceIncoming.class);
                        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        fullScreenIntent.putExtra("meetingId", meetingId);
                        fullScreenIntent.putExtra("phone", phone);
                        fullScreenIntent.putExtra("photoReceiver", photo);
                        fullScreenIntent.putExtra("nameReceiver", originalName);
                        fullScreenIntent.putExtra("token", sampleToken);
                        fullScreenIntent.putExtra("senderId", receiverId);
                        fullScreenIntent.putExtra("receiverId", senderId);
                        fullScreenIntent.putExtra("device_type", device_type);
                        fullScreenIntent.putExtra("acceptKey", "acceptKey");
                        fullScreenIntent.putExtra("stop_ringtone", true);
                        fullScreenIntent.putExtra("fromNoti", "fromNoti");
                        fullScreenIntent.putExtra("username", usernameWeb);
                        fullScreenIntent.putExtra("createdBy", createdByWeb);
                        fullScreenIntent.putExtra("incoming", incomingWeb);
                        fullScreenIntent.putExtra("roomId", roomId);
                        fullScreenIntent.putExtra(Constant.tapOnNotification, Constant.tapOnNotification);

                        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                                this, 1111, fullScreenIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));

                        // Build CallStyle notification
                        NotificationCompat.CallStyle callStyle = NotificationCompat.CallStyle.forIncomingCall(sender, declinePendingIntent, acceptPendingIntent)
                                .setIsVideo(false);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "VOICE CALL")
                                .setSmallIcon(R.drawable.notification_icon)
                                .setLargeIcon(largeIcon)
                                .setContentTitle("Incoming Voice Call from " + originalName)
                                .setContentText("Voice Call")
                                .setStyle(callStyle)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setFullScreenIntent(fullScreenPendingIntent, true)
                                .setAutoCancel(true)
                                .setOngoing(true);

                        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);

                        // Play ringtone
                        Uri notification = Settings.System.DEFAULT_RINGTONE_URI;
                        MainApplication.player = MediaPlayer.create(this, notification);
                        MainApplication.player.start();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("VOICE CALL", "VOICE CALL", NotificationManager.IMPORTANCE_HIGH);
                            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                            mNotificationManager.createNotificationChannel(channel);
                            builder.setChannelId("VOICE CALL");
                        }

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            mNotificationManager.notify(100, builder.build());
                            Log.d("chat007", "voice");
                        } else {
                            Log.e("NotificationDebug", "Permission missing for notification ID 100");
                        }
                    }
                } else
                    if (bodyKey.equals(Constant.videocall)) {

                    try {
                        database.getReference().child("NOTIARRIVED").child("status").child(receiverId).setValue("TRUE")
                                .addOnSuccessListener(unused -> {
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Constant.getSfFuncion(this);

                    // Handle call disconnection and missed call notification
                    try {
                        DatabaseReference refVideo = database.getReference()
                                .child("removeVideoCallNotification")
                                .child(Constant.getSF.getString(Constant.UID_KEY, ""));

                        ChildEventListener videoCallListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                refVideo.removeValue().addOnSuccessListener(unused -> {
                                    Log.d("xys", "onDataChange: pointedcssddc");

                                    // Stop and release ringtone
                                    if (MainApplication.player != null && MainApplication.player.isPlaying()) {
                                        MainApplication.player.stop();
                                        MainApplication.player.release();
                                        MainApplication.player = null;
                                    }

                                    // Send missed call notification
                                    Log.d("*****", "onSuccess: " + photo);
                                    FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(
                                            userFcmToken, title, Constant.videoMissCall, photo, userName, device_type);
                                    fmisscall.SendNotifications();

                                    // Record missed call
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date2 = new Date();
                                    String date = dateFormat.format(date2);

                                    Calendar calendar = Calendar.getInstance();
                                    String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                                    String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

                                    Log.d("currentTime", currentTime);
                                    Log.d("receiverIddytdyxwaux", receiverId);

                                    Webservice.create_group_callingMissCall(
                                            Constant.getSF.getString(Constant.UID_KEY, ""), senderId, "", date, currentTime, "2", currentTime, "2");

                                    // Cancel notification
                                    NotificationManagerCompat.from(FirebaseMessagingService.this).cancel(100);

                                    // Remove listener to avoid repeated calls
                                    refVideo.removeEventListener(this);
                                });
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        };

                        refVideo.addChildEventListener(videoCallListener);

                    } catch (Exception e) {
                        Log.e("NotificationDebug", "Error setting up removeVideoCallNotification listener: " + e.getMessage());
                    }


                    String videoRadioKey = Constant.getSF.getString(Constant.videoRadioKey, "");
                    if (videoRadioKey.equals(Constant.videoRadioKey)) {
                        // Load sender's photo
                        Bitmap largeIcon = null;
                        try {
                            URL url = new URL(photo);
                            largeIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        } catch (IOException e) {
                            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.deflarge);
                            if (largeIcon == null) {
                                largeIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(largeIcon);
                                canvas.drawColor(0xFFCCCCCC);
                                Log.w("NotificationDebug", "Profile fallback bitmap created (gray square)");
                            }
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        }

                        String originalName = userName;
                        Log.w("receiverKeyacwcaww", "receiverKeydd" + receiverKey);
                        try {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                            get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverKey);
                            if (dataModel.getFull_name() != null) {
                                originalName = dataModel.getFull_name();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "DatabaseHelper error: " + e.getMessage());
                        }
                        assert originalName != null;
                        if(originalName.isEmpty()){
                            originalName ="Unknown";
                        }

                        Person sender = new Person.Builder()
                                .setName(originalName)
                                .setIcon(IconCompat.createWithBitmap(largeIcon))
                                .build();

                        // Accept intent (to ConnectingActivity)
                        Intent acceptIntent = new Intent(this, MainActivityVideoCall.class);
                        acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        acceptIntent.putExtra("meetingId", meetingId);
                        acceptIntent.putExtra("phone", phone);
                        acceptIntent.putExtra("photoReceiver", photo);
                        acceptIntent.putExtra("nameReceiver", originalName);
                        acceptIntent.putExtra("token", sampleToken);
                        acceptIntent.putExtra("senderId", receiverId);
                        acceptIntent.putExtra("receiverId", senderId);
                        acceptIntent.putExtra("device_type", device_type);
                        acceptIntent.putExtra("acceptKey", "acceptKey");
                        acceptIntent.putExtra("stop_ringtone", true);
                        acceptIntent.putExtra("fromNoti", "fromNoti");
                        acceptIntent.putExtra("username", usernameWeb);
                        acceptIntent.putExtra("createdBy", createdByWeb);
                        acceptIntent.putExtra("incoming", incomingWeb);
                        acceptIntent.putExtra("roomId", roomId);
                        acceptIntent.putExtra(Constant.tapOnNotification, Constant.tapOnNotification);
                        PendingIntent acceptPendingIntent = PendingIntent.getActivity(
                                this, 1, acceptIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));
                        // Decline intent (to BroadcastReceiver)
                        Intent declineIntent = new Intent(this, DeclineVideoCallReceiver.class);
                        declineIntent.putExtra("stop_ringtone", true);
                        declineIntent.putExtra("caller_name", originalName);
                        declineIntent.putExtra("declineVideoKey", "declineVideoKey");
                        declineIntent.putExtra("roomVideo", senderId);
                        declineIntent.putExtra("receiverId", receiverId);
                        declineIntent.putExtra("userFcmToken", userFcmToken);
                        declineIntent.putExtra("title", title);
                        declineIntent.putExtra("photo", photo);
                        declineIntent.putExtra("userName", originalName);
                        declineIntent.putExtra("device_type", device_type);
                        declineIntent.putExtra("notificationId", 100);

                        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                                this, 2, declineIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));

                        // Full-screen intent (to FullScreenVideoIncoming)
                        Intent fullScreenIntent = new Intent(this, FullScreenVideoIncoming.class);
                        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        fullScreenIntent.putExtra("meetingId", meetingId);
                        fullScreenIntent.putExtra("phone", phone);
                        fullScreenIntent.putExtra("photoReceiver", photo);
                        fullScreenIntent.putExtra("nameReceiver", originalName);
                        fullScreenIntent.putExtra("token", sampleToken);
                        fullScreenIntent.putExtra("senderId", receiverId);
                        fullScreenIntent.putExtra("receiverId", senderId);
                        fullScreenIntent.putExtra("device_type", device_type);
                        fullScreenIntent.putExtra("acceptKey", "acceptKey");
                        fullScreenIntent.putExtra("stop_ringtone", true);
                        fullScreenIntent.putExtra("fromNoti", "fromNoti");
                        fullScreenIntent.putExtra("username", usernameWeb);
                        fullScreenIntent.putExtra("createdBy", createdByWeb);
                        fullScreenIntent.putExtra("incoming", incomingWeb);
                        fullScreenIntent.putExtra("roomId", roomId);
                        fullScreenIntent.putExtra(Constant.tapOnNotification, Constant.tapOnNotification);

                        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                                this, 1112, fullScreenIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0));

                        // Build-cur CallStyle notification
                        NotificationCompat.CallStyle callStyle = NotificationCompat.CallStyle.forIncomingCall(sender, declinePendingIntent, acceptPendingIntent)
                                .setIsVideo(true);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "VIDEO CALL")
                                .setSmallIcon(R.drawable.notification_icon)
                                .setLargeIcon(largeIcon)
                                .setContentTitle("Incoming Video Call from " + originalName)
                                .setContentText("Video Call")
                                .setStyle(callStyle)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setFullScreenIntent(fullScreenPendingIntent, true)
                                .setAutoCancel(true)
                                .setOngoing(true);

                        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);

                        // Play ringtone
                        Uri notification = Settings.System.DEFAULT_RINGTONE_URI;
                        MainApplication.player = MediaPlayer.create(this, notification);
                        MainApplication.player.start();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("VIDEO CALL", "VIDEO CALL", NotificationManager.IMPORTANCE_HIGH);
                            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                            mNotificationManager.createNotificationChannel(channel);
                            builder.setChannelId("VIDEO CALL");
                        }

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            mNotificationManager.notify(100, builder.build());
                            Log.d("chat007", "video");
                        } else {
                            Log.e("NotificationDebug", "Permission missing for notification ID 100");
                        }
                    }
                } else
                    if (bodyKey.equals(Constant.chatting)) {

                    Log.d("activated###", "DeActivated###: ");
                    Log.d("chat007", "User :" + user_nameKey);
                    Log.d("chat007", "message :" + message);

                    // Create notification channel
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("MESSAGE", "Chat Notifications", NotificationManager.IMPORTANCE_HIGH);
                        channel.setDescription("Chat message notifications");
                        channel.setShowBadge(true);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    // Check for notification permission
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("NotificationDebug", "Notification permission missing");
                        return;
                    }


                    try {
                        if (chattingScreen.isChatScreenActive
                                && receiverKey != null
                                && receiverKey.equals(chattingScreen.isChatScreenActiveUid)) {
                            Log.d("NotificationDebug", "Chat in foreground for " + receiverKey + " → suppressing notification");
                            return; // Do not build/post notification
                        }
                    } catch (Exception ignored) { /* defensive */ }


                    // Truncate message to avoid issues with long text
                    String truncatedMessage = message.length() > 500 ? message.substring(0, 500) + "..." : message;
                    Log.d("NotificationDebug", "Message length: " + message.length() + ", Truncated to: " + truncatedMessage.length());

                    // Unique notification ID and shortcut ID
                    int notificationId = uidPower.hashCode(); // Consistent ID for same user
                    String shortcutId = "chat_" + uidPower; // Unique shortcut ID for the user

                    // Prepare conversation for SmartReply
                    List<TextMessage> conversation = new ArrayList<>();
                    try {
                        conversation.add(TextMessage.createForRemoteUser(truncatedMessage, System.currentTimeMillis(), user_nameKey));
                    } catch (Exception e) {
                        Log.e("NotificationDebug", "Failed to create TextMessage for SmartReply: " + e.getMessage());
                        return;
                    }

                    // Generate smart replies
                    SmartReplyGenerator smartReply = SmartReply.getClient();
                    smartReply.suggestReplies(conversation)
                            .addOnSuccessListener(result -> {
                                List<String> suggestions = new ArrayList<>();
                                if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                                    for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                        suggestions.add(suggestion.getText());
                                    }
                                    Log.d("SmartReplyDebug", "Smart replies for " + user_nameKey + ": " + suggestions);
                                } else {
                                    Log.w("SmartReplyDebug", "Smart reply status: " + result.getStatus());
                                }

                                // Fallback replies if none generated
                                if (suggestions.isEmpty()) {
                                    suggestions.add("Okay");
                                    suggestions.add("👍");
                                    suggestions.add("Reply later");
                                    Log.d("SmartReplyDebug", "Using fallback replies: " + suggestions);
                                }

                                // Load profile image and build notification
                                loadProfileImageFromUrl(this, photo, bitmap -> {
                                    buildChatNotification(this, user_nameKey, truncatedMessage, suggestions, bitmap, notificationId, shortcutId, receiverKey, device_type, uidPower, messagePower, timePower, documentPower, dataTypePower, extensionPower, namePower, phonePower, micPhotoPower, miceTimingPower, userNamePower, replytextDataPower, replyKeyPower, replyTypePower, replyOldDataPower, replyCrtPostionPower, modelIdPower, receiverUidPower, forwaredKeyPower, groupNamePower, docSizePower, fileNamePower, thumbnailPower, fileNameThumbnailPower, captionPower, notificationPower, currentDatePower, userFcmTokenPower, senderTokenReplyPower, myFcmOwn, selectionCount);
                                });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("SmartReplyDebug", "Smart reply failed: " + e.getMessage());
                                List<String> fallback = List.of("Okay", "👍", "Reply later");

                                // Load profile image with fallback replies
                                loadProfileImageFromUrl(this, photo, bitmap -> {
                                    buildChatNotification(this, user_nameKey, truncatedMessage, fallback, bitmap, notificationId, shortcutId, receiverKey, device_type, uidPower, messagePower, timePower, documentPower, dataTypePower, extensionPower, namePower, phonePower, micPhotoPower, miceTimingPower, userNamePower, replytextDataPower, replyKeyPower, replyTypePower, replyOldDataPower, replyCrtPostionPower, modelIdPower, receiverUidPower, forwaredKeyPower, groupNamePower, docSizePower, fileNamePower, thumbnailPower, fileNameThumbnailPower, captionPower, notificationPower, currentDatePower, userFcmTokenPower, senderTokenReplyPower, myFcmOwn, selectionCount);
                                });
                            });

                } else
                    if (bodyKey.equals(Constant.videoMissCall)) {
                    Constant.getSfFuncion(this);

                    String voiceRadioKey = Constant.getSF.getString(Constant.voiceRadioKey, "");
                    if (voiceRadioKey.equals(Constant.voiceRadioKey)) {
                        // Load sender's photo for large icon
                        Bitmap largeIcon = null;
                        try {
                            URL url = new URL(photo);
                            largeIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        } catch (IOException e) {
                            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.deflarge);
                            if (largeIcon == null) {
                                largeIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(largeIcon);
                                canvas.drawColor(0xFFCCCCCC);
                                Log.w("NotificationDebug", "Profile fallback bitmap created (gray square)");
                            }
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        }



                        String originalName = userName;
                        Log.w("receiverKeyacwcaww", "receiverKeydd" + receiverKey);
                        try {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                            get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverKey);
                            if (dataModel.getFull_name() != null) {
                                originalName = dataModel.getFull_name();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "DatabaseHelper error: " + e.getMessage());
                        }
                        assert originalName != null;
                        if(originalName.isEmpty()){
                            originalName ="Unknown";
                        }
                        // Create Person for sender
                        Person sender = new Person.Builder()
                                .setName(originalName)
                                .setIcon(IconCompat.createWithBitmap(largeIcon))
                                .build();

                        // Create MessagingStyle for group-like notification
                        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(sender)
                                .setGroupConversation(true)
                                .setConversationTitle("Missed Call")
                                .addMessage("Video Call", System.currentTimeMillis(), sender);

                        // Intent to open MainActivityOld when tapped
                        Intent rejectIntent = new Intent(this, MainActivityOld.class);
                        rejectIntent.putExtra("videoCallKey", "videoCallKey");
                        rejectIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent rejectPendingIntent = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                                ? PendingIntent.getActivity(this, 1, rejectIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT)
                                : PendingIntent.getActivity(this, 1, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Build individual notification with default small icon
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "VIDEO_CALL_MISS_CALL")
                                .setSmallIcon(R.drawable.miss_call_svg) // Default notification icon
                                .setLargeIcon(largeIcon)
                                .setStyle(messagingStyle)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setContentIntent(rejectPendingIntent)
                                .setAutoCancel(true)
                                .setGroup("missed_call_group") // Group notifications
                                .setGroupSummary(false);

                        // Build group summary notification with default small icon
                        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this, "VIDEO_CALL_MISS_CALL")
                                .setSmallIcon(R.drawable.miss_call_svg) // Default notification icon
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .setBigContentTitle("Missed Call")
                                        .addLine(originalName + ": Video Call"))
                                .setGroup("missed_call_group")
                                .setGroupSummary(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setAutoCancel(true);

                        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);

                        // Stop ringtone if active
                        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
                            MainApplication.player.stop();
                            MainApplication.player.release();
                            MainApplication.player = null;
                        }

                        // Create notification channel
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    "VIDEO_CALL_MISS_CALL",
                                    "Video Call Notifications",
                                    NotificationManager.IMPORTANCE_HIGH);
                            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                            mNotificationManager.createNotificationChannel(channel);
                        }

                        // Post notifications if permission is granted
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            // Use a unique ID based on user to replace previous notification
                            int notificationId = ("missed_video_call_" + originalName).hashCode();
                            mNotificationManager.notify(notificationId, builder.build());
                            mNotificationManager.notify(100, summaryBuilder.build()); // Group summary ID
                            Log.d("chat007", "Video miss call notification posted for " + originalName);
                        } else {
                            Log.e("NotificationDebug", "Permission missing for notification ID");
                        }
                    }
                } else if (bodyKey.equals(Constant.voiceMissCall)) {
                    Constant.getSfFuncion(this);

                    String voiceRadioKey = Constant.getSF.getString(Constant.voiceRadioKey, "");
                    if (voiceRadioKey.equals(Constant.voiceRadioKey)) {
                        // Load sender's photo for large icon
                        Bitmap largeIcon = null;
                        try {
                            URL url = new URL(photo);
                            largeIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        } catch (IOException e) {
                            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.deflarge);
                            if (largeIcon == null) {
                                largeIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(largeIcon);
                                canvas.drawColor(0xFFCCCCCC);
                                Log.w("NotificationDebug", "Profile fallback bitmap created (gray square)");
                            }
                            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
                        }


                        String originalName = userName;
                        Log.w("receiverKeyacwcaww", "receiverKeydd" + receiverKey);
                        try {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                            get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverKey);
                            if (dataModel.getFull_name() != null) {
                                originalName = dataModel.getFull_name();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "DatabaseHelper error: " + e.getMessage());
                        }

                        assert originalName != null;
                        if(originalName.isEmpty()){
                            originalName ="Unknown";
                        }

                        // Create Person for sender
                        Person sender = new Person.Builder()
                                .setName(originalName)
                                .setIcon(IconCompat.createWithBitmap(largeIcon))
                                .build();

                        // Create MessagingStyle for group-like notification
                        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(sender)
                                .setGroupConversation(true)
                                .setConversationTitle("Missed Call")
                                .addMessage("Voice Call", System.currentTimeMillis(), sender);

                        // Intent to open MainActivityOld when tapped
                        Intent rejectIntent = new Intent(this, MainActivityOld.class);
                        rejectIntent.putExtra("voiceCallKey", "voiceCallKey");
                        rejectIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent rejectPendingIntent = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                                ? PendingIntent.getActivity(this, 1, rejectIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT)
                                : PendingIntent.getActivity(this, 1, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Build individual notification with default small icon
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "VOICE_CALL_MISS_CALL")
                                .setSmallIcon(R.drawable.miss_call_svg) // Default notification icon
                                .setLargeIcon(largeIcon)
                                .setStyle(messagingStyle)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setContentIntent(rejectPendingIntent)
                                .setAutoCancel(true)
                                .setGroup("missed_call_group") // Group notifications
                                .setGroupSummary(false);

                        // Build group summary notification with default small icon
                        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this, "VOICE_CALL_MISS_CALL")
                                .setSmallIcon(R.drawable.miss_call_svg) // Default notification icon
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .setBigContentTitle("Missed Call")
                                        .addLine(originalName + ": Voice Call"))
                                .setGroup("missed_call_group")
                                .setGroupSummary(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setAutoCancel(true);

                        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);

                        // Stop ringtone if active
                        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
                            MainApplication.player.stop();
                            MainApplication.player.release();
                            MainApplication.player = null;
                        }

                        // Create notification channel
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    "VOICE_CALL_MISS_CALL",
                                    "Voice Call Notifications",
                                    NotificationManager.IMPORTANCE_HIGH);
                            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                            mNotificationManager.createNotificationChannel(channel);
                        }

                        // Post notifications if permission is granted
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            // Use a unique ID based on user to replace previous notification
                            int notificationId = ("missed_call_" + originalName).hashCode();
                            mNotificationManager.notify(notificationId, builder.build());
                            mNotificationManager.notify(100, summaryBuilder.build()); // Group summary ID
                            Log.d("chat007", "Voice miss call notification posted for " + originalName);
                        } else {
                            Log.e("NotificationDebug", "Permission missing for notification ID");
                        }
                    }
                }
            }
        }


    }

//    public Bitmap getCircularBitmap(Bitmap bitmap) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setFilterBitmap(true);
//        paint.setDither(true);
//
//        // Draw a circle
//        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
//
//        // Set the bitmap to be drawn inside the circle
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, 0, 0, paint);
//
//        return output;
//    }


    private void startNotificationTimer(RemoteViews collapseview, RemoteViews expandview, NotificationCompat.Builder builder, NotificationManagerCompat notificationManager) {
        // Get saved time from SharedPreferences
        String savedTime = Constant.getSF.getString("lastTimeKey", "00:00");
        long savedMillis = parseElapsedTime(savedTime);
        long startTime = SystemClock.elapsedRealtime() - savedMillis;

        handler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                String time = formatElapsedTime(elapsedTime);
                // Update SharedPreferences
                Constant.setSfFunction(FirebaseMessagingService.this);
                Constant.setSF.putString("lastTimeKey", time).apply();
                // Update RemoteViews
                collapseview.setTextViewText(R.id.notification_time, time);
                expandview.setTextViewText(R.id.notification_time, time);
                // Rebuild notification
                builder.setCustomContentView(collapseview);
                builder.setCustomBigContentView(expandview);
                if (ActivityCompat.checkSelfPermission(FirebaseMessagingService.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManager.notify(100, builder.build());
                Log.d("CallNotification", "Updated notification time: " + time);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timerRunnable);
    }

    private String formatElapsedTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private long parseElapsedTime(String timeStr) {
        String[] parts = timeStr.split(":");
        if (parts.length != 2) return 0;
        try {
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return (minutes * 60 + seconds) * 1000L;
        } catch (NumberFormatException e) {
            Log.e("CallNotification", "Error parsing time: " + timeStr, e);
            return 0;
        }
    }

    interface OnImageLoadedListener {
        void onLoaded(Bitmap bitmap);
    }

    private void loadProfileImageFromUrl(Context context, String url, OnImageLoadedListener listener) {
        Log.d("NotificationDebug", "Loading profile image from URL: " + url);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .override(128, 128)
                .centerCrop()
                .timeout(10000)
                .error(R.drawable.deflarge)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d("NotificationDebug", "Profile image loaded: " + resource.getWidth() + "x" + resource.getHeight());
                        listener.onLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.e("NotificationDebug", "Profile image load failed for URL: " + url);
                        Bitmap fallback = BitmapFactory.decodeResource(context.getResources(), R.drawable.deflarge);
                        if (fallback == null) {
                            fallback = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(fallback);
                            canvas.drawColor(0xFFCCCCCC); // Gray fallback
                        }
                        listener.onLoaded(getCircularBitmap(fallback));
                    }
                });
    }

    // Helper method to create circular bitmap
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.w("NotificationDebug", "Input bitmap is null, returning null");
            return null;
        }
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, size, size);
        float radius = size / 2f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0); // Transparent background
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private Bitmap getCircularBitmap2(Bitmap bitmap) {
        if (bitmap == null) {
            Log.w("NotificationDebug", "Input bitmap is null, returning null");
            return null;
        }

        // Ensure the input bitmap has an alpha channel
        Bitmap inputBitmap = bitmap;
        if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            inputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (inputBitmap == null) {
                Log.w("NotificationDebug", "Failed to copy bitmap to ARGB_8888, returning null");
                return null;
            }
        }

        int size = Math.min(inputBitmap.getWidth(), inputBitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Create a shader from the input bitmap
        BitmapShader shader = new BitmapShader(inputBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // Adjust the shader's matrix to fit the bitmap into the circular area
        float scale = (float) size / Math.min(inputBitmap.getWidth(), inputBitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        // Center the bitmap
        matrix.postTranslate(
                (size - inputBitmap.getWidth() * scale) / 2,
                (size - inputBitmap.getHeight() * scale) / 2
        );
        shader.setLocalMatrix(matrix);

        // Draw the circle with the shader
        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        // Clean up if we copied the bitmap
        if (inputBitmap != bitmap) {
            inputBitmap.recycle();
        }

        return output;
    }


    // Helper method to build and display the notification
    // Updated buildChatNotification method
    private void buildChatNotification(Context context, String userName, String message, List<String> replies, Bitmap largeIcon, int notificationId, String shortcutId, String receiverKey, String device_type, String uidPower, String messagePower, String timePower, String documentPower, String dataTypePower, String extensionPower, String namePower, String phonePower, String micPhotoPower, String miceTimingPower, String userNamePower, String replytextDataPower, String replyKeyPower, String replyTypePower, String replyOldDataPower, String replyCrtPostionPower, String modelIdPower, String receiverUidPower, String forwaredKeyPower, String groupNamePower, String docSizePower, String fileNamePower, String thumbnailPower, String fileNameThumbnailPower, String captionPower, String notificationPower, String currentDatePower, String userFcmTokenPower, String senderTokenReplyPower, String myFcmOwn, String selectionCount) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Fallback bitmap for profile image
        if (largeIcon == null) {
            largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.deflarge);
            if (largeIcon == null) {
                largeIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(largeIcon);
                canvas.drawColor(0xFFCCCCCC);
                Log.w("NotificationDebug", "Profile fallback bitmap created (gray square)" + receiverKey);
            }
            largeIcon = getCircularBitmap(Bitmap.createScaledBitmap(largeIcon, 128, 128, false));
        }


        String displayMessage;

// Check if multiple selections
        boolean isMultiple = !selectionCount.equals("1");

// Determine message type
        switch (message) {
            case "You have a new Image":
                displayMessage = "📷 " + (isMultiple ? selectionCount + " Photos" : "Photo");
                break;
            case "You have a new Contact":
                displayMessage = "👤 " + (isMultiple ? selectionCount + " Contacts" : "Contact");
                break;
            case "You have a new Audio":
                displayMessage = "🎙️ " + (isMultiple ? selectionCount + " Audios" : "Audio");
                break;
            case "You have a new File":
                displayMessage = "📄 " + (isMultiple ? selectionCount + " Files" : "File");
                break;
            case "You have a new Video":
                displayMessage = "📹 " + (isMultiple ? selectionCount + " Videos" : "Video");
                break;
            default:
                displayMessage = message;
                break;
        }


        // Get sender name from DatabaseHelper
        String originalName = userName;
        Log.w("receiverKeyacwcaww", "receiverKeydd" + receiverKey);
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverKey);
            if (dataModel.getFull_name() != null) {
                originalName = dataModel.getFull_name();
            }
        } catch (Exception e) {
            Log.e("Error", "DatabaseHelper error: " + e.getMessage());
        }
        assert originalName != null;
        if(originalName.isEmpty()){
            originalName ="Unknown";
        }
        // Create Person for the sender
        Person sender = new Person.Builder()
                .setName(originalName)
                .setIcon(IconCompat.createWithBitmap(largeIcon))
                .build();

        // Create dynamic shortcut
        try {
            createDynamicShortcut(context, shortcutId, originalName, sender, largeIcon, receiverKey);
        } catch (Exception e) {
            Log.e("NotificationDebug", "Failed to create dynamic shortcut: " + e.getMessage());
        }

        // Load "Me" profile image asynchronously
        Constant.getSfFuncion(context); // Ensure this is thread-safe
        String meProfileUrl = Constant.getSF.getString(Constant.profilePic, "");
        Log.d("TAG", "buildChatNotification: " + meProfileUrl);

        // Use ExecutorService for async image loading
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Bitmap finalLargeIcon = largeIcon;
        String finalOriginalName = originalName;
        executor.execute(() -> {
            Bitmap meIcon;
            try {
                meIcon = Glide.with(context)
                        .asBitmap()
                        .load(meProfileUrl)
                        .apply(new RequestOptions().override(128, 128))
                        .submit()
                        .get();
                meIcon = getCircularBitmap(meIcon);
            } catch (Exception e) {
                Log.e("NotificationDebug", "Failed to load Me profile image: " + e.getMessage());
                meIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.deflarge);
                if (meIcon == null) {
                    meIcon = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(meIcon);
                    canvas.drawColor(0xFFCCCCCC);
                }
                meIcon = getCircularBitmap(Bitmap.createScaledBitmap(meIcon, 128, 128, false));
            }

            // Prepare intent for navigating to chat screen
            Intent resultIntent = new Intent(context, chattingScreen.class);
            resultIntent.putExtra("friendUidKey", receiverKey);
            resultIntent.putExtra("nameKey", userName);
            resultIntent.putExtra("device_type", device_type);
            resultIntent.putExtra("notificationId", notificationId);
            resultIntent.putExtra("FROM_NOTI_KEY", "FROM_NOTI_KEY");

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(resultIntent);

            PendingIntent contentIntent = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    ? stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE)
                    : stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set up RemoteInput for single reply action
            String replyKey = "key_text_reply_" + notificationId;
            RemoteInput remoteInput = new RemoteInput.Builder(replyKey)
                    .setLabel("Reply to " + finalOriginalName)
                    .setChoices(replies.toArray(new CharSequence[0]))
                    .build();

            // Set up reply Intent
            Intent replyIntent = new Intent(context, replyBroadCastReciver.class);
            replyIntent.setAction("com.Appzia.enclosure.REPLY_ACTION_" + notificationId);
            replyIntent.putExtra("recipientName", finalOriginalName);
            replyIntent.putExtra("replyKey", replyKey);
            replyIntent.putExtra("notificationId", notificationId);
            replyIntent.putExtra("uidPower", uidPower);
            replyIntent.putExtra("messagePower", messagePower);
            replyIntent.putExtra("timePower", timePower);
            replyIntent.putExtra("documentPower", documentPower);
            replyIntent.putExtra("dataTypePower", dataTypePower);
            replyIntent.putExtra("extensionPower", extensionPower);
            replyIntent.putExtra("namepower", namePower);
            replyIntent.putExtra("phonePower", phonePower);
            replyIntent.putExtra("micPhotoPower", micPhotoPower);
            replyIntent.putExtra("miceTimingPower", miceTimingPower);
            replyIntent.putExtra("userNamePower", userNamePower);
            replyIntent.putExtra("replytextDataPower", replytextDataPower);
            replyIntent.putExtra("replyKeyPower", replyKeyPower);
            replyIntent.putExtra("replyTypePower", replyTypePower);
            replyIntent.putExtra("replyOldDataPower", replyOldDataPower);
            replyIntent.putExtra("replyCrtPostionPower", replyCrtPostionPower);
            replyIntent.putExtra("modelIdPower", modelIdPower);
            replyIntent.putExtra("receiverUidPower", receiverUidPower);
            replyIntent.putExtra("forwaredKeyPower", forwaredKeyPower);
            replyIntent.putExtra("groupNamePower", groupNamePower);
            replyIntent.putExtra("docSizePower", docSizePower);
            replyIntent.putExtra("fileNamePower", fileNamePower);
            replyIntent.putExtra("thumbnailPower", thumbnailPower);
            replyIntent.putExtra("fileNameThumbnailPower", fileNameThumbnailPower);
            replyIntent.putExtra("captionPower", captionPower);
            replyIntent.putExtra("notificationPower", notificationPower);
            replyIntent.putExtra("currentDatePower", currentDatePower);
            replyIntent.putExtra("userFcmTokenPower", myFcmOwn);
            replyIntent.putExtra("senderTokenReplyPower", senderTokenReplyPower);
            replyIntent.putExtra("receiverKey", receiverKey);


            PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    replyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
            );

            NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                    R.drawable.reply_svg_black,
                    "Reply",
                    replyPendingIntent
            ).addRemoteInput(remoteInput).build();

            Person me = new Person.Builder()
                    .setName("You")
                    .setIcon(IconCompat.createWithBitmap(meIcon))
                    .setImportant(true)
                    .build();


            // --- WhatsApp-like MessagingStyle ---
            NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(me)
                    .setConversationTitle(finalOriginalName)
                    .addMessage(displayMessage, System.currentTimeMillis(), sender);

// --- Build notification ---
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MESSAGE")
                    .setSmallIcon(R.drawable.notification_icon) // App logo bottom-right of avatar
                    .setLargeIcon(finalLargeIcon)                // Big circular profile image
                    .setStyle(style)                             // Key line for WhatsApp look
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setShortcutId(shortcutId)
                    .setAutoCancel(true)
                    .addAction(replyAction)
                    .setContentIntent(contentIntent);



            // Post the notification
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(notificationId, builder.build());
                Log.d("NotificationDebug", "Posted/Updated notification for " + finalOriginalName + " with ID " + notificationId + ", ShortcutId: " + shortcutId + ", Message: " + displayMessage + ", Replies: " + replies);
            } else {
                Log.e("NotificationDebug", "Notification permission missing for ID " + notificationId);
            }
        });
    }

    private void createDynamicShortcut(Context context, String shortcutId, String shortLabel, Person person, Bitmap iconBitmap, String receiverKey) {
        // Check if shortcut already exists to avoid duplicates
        for (ShortcutInfoCompat shortcut : ShortcutManagerCompat.getDynamicShortcuts(context)) {
            if (shortcut.getId().equals(shortcutId)) {
                Log.d("NotificationDebug", "Shortcut already exists: " + shortcutId);
                return;
            }
        }

        // Create intent for the chat screen
        Intent intent = new Intent(context, chattingScreen.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("friendUidKey", receiverKey);
        intent.putExtra("nameKey", person.getName());
        intent.putExtra("FROM_NOTI_KEY", "FROM_NOTI_KEY");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Build the shortcut
        ShortcutInfoCompat.Builder shortcutBuilder = new ShortcutInfoCompat.Builder(context, shortcutId)
                .setLongLived(true)
                .setIntent(intent)
                .setShortLabel(shortLabel)
                .setCategories(Collections.singleton("android.shortcut.conversation")); // Mark as conversation shortcut

        if (iconBitmap != null) {
            Bitmap circularBitmap = getCircularBitmap2(iconBitmap); // Use the reliable method
            if (circularBitmap != null) {
                IconCompat icon = IconCompat.createWithBitmap(circularBitmap);
                shortcutBuilder.setIcon(icon);
            } else {
                Log.w("NotificationDebug", "Failed to create circular bitmap, shortcut created without icon");
            }
        } else {
            Log.w("NotificationDebug", "Icon bitmap is null, shortcut created without icon");
        }

        try {
            ShortcutInfoCompat shortcut = shortcutBuilder.build();
            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);
            Log.d("NotificationDebug", "Dynamic shortcut created: " + shortcutId);
        } catch (Exception e) {
            Log.e("NotificationDebug", "Failed to push dynamic shortcut: " + e.getMessage());
            throw e; // Re-throw for debugging; consider removing in production
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
