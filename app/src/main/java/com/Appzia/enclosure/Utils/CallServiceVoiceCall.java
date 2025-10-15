package com.Appzia.enclosure.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.activities.MainActivityVoiceCall; // Import MainActivityVoiceCall
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

public class CallServiceVoiceCall extends Service {
    private static final String TAG = "CallServiceVoiceCall";
    private static final String PREFS_NAME = "CallPrefs";
    private static final String CALL_DURATION_KEY = "call_duration";
    private static final String CHANNEL_ID = "VoiceCallChannel";
    private static final String CHANNEL_NAME = "Voice Call Notifications";
    private static final int NOTIFICATION_ID = 1;
    public static final String PREFS_CALL_DATA = "CallDataPrefs";
    public static final String KEY_MY_ROOM = "myRoom";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_INCOMING = "incoming";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_ROOM_FLAG_KEY = "roomFlagKey";
    public static final String KEY_PHOTO_RECEIVER = "photoReceiver";
    public static final String KEY_NAME_RECEIVER = "nameReceiver";
    public static final String KEY_CALL_START_TIME = "callStartTime";
    public static final String KEY_ROOM_ID = "roomId";
    public static final String PEER_ID = "PEER_ID";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_IS_SENDER = "isSender";
    private static final int END_CALL_REQUEST_CODE = 100;
    public static final String ACTION_END_CALL = "com.Appzia.enclosure.END_CALL";
    public static final String ACTION_TIMER_UPDATE = "com.Appzia.enclosure.TIMER_UPDATE";
    public static final String ACTION_REQUEST_DURATION = "com.Appzia.enclosure.REQUEST_DURATION";
    public static final String ACTION_START_TIMER = "com.Appzia.enclosure.START_TIMER";
    public static final String ACTION_UPDATE_CALLER_INFO = "com.Appzia.enclosure.UPDATE_CALLER_INFO";

    private SharedPreferences prefs;
    private NotificationCompat.Builder notificationBuilder;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;
    private long callDurationSeconds = 0;
    private boolean isTimerRunning = false;
    private String callerName = "";
    private String callerImageUrl = "";

    // Variables to hold call data, retrieved from SharedPreferences
    private String myRoom;
    private String username;
    private String incoming;
    private String createdBy;
    private String roomFlagKey;
    private String photoReceiver;
    private String nameReceiver;
    private long callStartTime;
    private String roomId;
    private String receiverId;
    private boolean iAmSender;


    private final BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Received broadcast: " + action);
            if (ACTION_REQUEST_DURATION.equals(action)) {
                broadcastTimerUpdate();
            } else if (ACTION_START_TIMER.equals(action)) {
                startCallTimer();
            } else if (ACTION_UPDATE_CALLER_INFO.equals(action)) {
                updateCallerInfo();
            }
        }
    };

    private final BroadcastReceiver phoneStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                Log.d(TAG, "Phone state changed: " + state);
                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && isVoipCallActive()) {
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d(TAG, "Incoming call from: " + incomingNumber);
                    rejectTelephonyCall();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Constant.getSfFuncion(this);
        callerName = Constant.getSF.getString(Constant.callName, "Voice Call");
        callerImageUrl = Constant.getSF.getString(Constant.photoReceiver, "");
        Log.d(TAG, "Service created with callerName: " + callerName + ", callerImageUrl: " + callerImageUrl);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REQUEST_DURATION);
        filter.addAction(ACTION_START_TIMER);
        filter.addAction(ACTION_UPDATE_CALLER_INFO);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, filter);
        IntentFilter phoneStateFilter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(phoneStateReceiver, phoneStateFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(phoneStateReceiver, phoneStateFilter);
        }
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for ongoing voice calls");
            channel.setSound(null, null);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created: " + CHANNEL_ID);
            } else {
                Log.e(TAG, "NotificationManager is null");
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called with action: " + (intent != null ? intent.getAction() : "null"));

        // Retrieve all call data from SharedPreferences
        SharedPreferences callDataPrefs = getSharedPreferences(MainActivityVoiceCall.PREFS_CALL_DATA, Context.MODE_PRIVATE);
        myRoom = callDataPrefs.getString(MainActivityVoiceCall.KEY_MY_ROOM, null);
        username = callDataPrefs.getString(MainActivityVoiceCall.KEY_USERNAME, null);
        incoming = callDataPrefs.getString(MainActivityVoiceCall.KEY_INCOMING, null);
        createdBy = callDataPrefs.getString(MainActivityVoiceCall.KEY_CREATED_BY, null);
        roomFlagKey = callDataPrefs.getString(MainActivityVoiceCall.KEY_ROOM_FLAG_KEY, null);
        photoReceiver = callDataPrefs.getString(MainActivityVoiceCall.KEY_PHOTO_RECEIVER, null);
        nameReceiver = callDataPrefs.getString(MainActivityVoiceCall.KEY_NAME_RECEIVER, null);
        callStartTime = callDataPrefs.getLong(MainActivityVoiceCall.KEY_CALL_START_TIME, System.currentTimeMillis());
        roomId = callDataPrefs.getString(MainActivityVoiceCall.KEY_ROOM_ID, null);
        receiverId = callDataPrefs.getString(MainActivityVoiceCall.KEY_RECEIVER_ID, null);
        iAmSender = callDataPrefs.getBoolean(MainActivityVoiceCall.KEY_IS_SENDER, false);


        // Log retrieved data to verify
        Log.d(TAG, "Retrieved from SharedPreferencesVoice: " +
                "myRoom=" + myRoom + ", username=" + username + ", incoming=" + incoming +
                ", createdBy=" + createdBy + ", roomFlagKey=" + roomFlagKey +
                ", photoReceiver=" + photoReceiver + ", nameReceiver=" + nameReceiver +
                ", callStartTime=" + callStartTime + ", roomId=" + roomId +
                ", receiverId=" + receiverId + ", iAmSender=" + iAmSender);


        if (intent != null && ACTION_END_CALL.equals(intent.getAction())) {
            stopCall();
            return START_NOT_STICKY;
        } else if (intent != null && ACTION_UPDATE_CALLER_INFO.equals(intent.getAction())) {
            updateCallerInfo();
            return START_STICKY;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CALL_DURATION_KEY, 0);
        editor.putLong("call_start_time", callStartTime); // Use retrieved callStartTime
        editor.apply();
        callDurationSeconds = 0;

        // Use retrieved nameReceiver and photoReceiver for notification, applying defaults if needed
        callerName = (nameReceiver == null || nameReceiver.isEmpty()) ? "Voice Call" : nameReceiver;
        callerImageUrl = (photoReceiver == null || photoReceiver.isEmpty()) ? "" : photoReceiver;


        Log.d(TAG, "Starting service with callerName: " + callerName + ", callerImageUrl: " + callerImageUrl);
        loadNetworkImage();
        startCallTimer();
        return START_STICKY;
    }

    private void updateCallerInfo() {
        // Retrieve updated caller info from Constant.getSF (which is populated by MainActivityVoiceCall)
        callerName = Constant.getSF.getString(Constant.callName, "Voice Call");
        callerImageUrl = Constant.getSF.getString(Constant.photoReceiver, "");
        Log.d(TAG, "Updating caller info: name=" + callerName + ", photoUrl=" + callerImageUrl);
        loadNetworkImage();
    }

    private void buildCallNotification(Bitmap networkBitmap) {
        Person.Builder personBuilder = new Person.Builder().setName(callerName);
        if (networkBitmap != null) {
            personBuilder.setIcon(IconCompat.createWithBitmap(networkBitmap));
            Log.d(TAG, "Using network image for notification");
        } else {
            personBuilder.setIcon(IconCompat.createWithResource(this, R.drawable.deflarge));
            Log.d(TAG, "Using fallback icon for notification");
        }
        Person caller = personBuilder.build();

        Intent intent = new Intent(this, MainActivityVoiceCall.class);
        intent.putExtra("iAmBroadcast",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent endCallIntent = new Intent(this, CallServiceVoiceCall.class);
        endCallIntent.setAction(ACTION_END_CALL);
        PendingIntent endCallPendingIntent = PendingIntent.getService(
                this, END_CALL_REQUEST_CODE, endCallIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.CallStyle callStyle = NotificationCompat.CallStyle.forOngoingCall(caller, endCallPendingIntent);

        long startTime = prefs.getLong("call_start_time", System.currentTimeMillis());
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setStyle(callStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setWhen(startTime)
                .setUsesChronometer(true)
                .setContentIntent(contentIntent);

        updateNotificationTitle();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Notification permission missing");
                stopSelf();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(NOTIFICATION_ID, notificationBuilder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL | ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE);
            } else {
                startForeground(NOTIFICATION_ID, notificationBuilder.build());
            }
            Log.d(TAG, "Foreground notification started with ID: " + NOTIFICATION_ID);
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException starting foreground service", e);
            stopSelf();
        }
    }

    private void updateNotificationTitle() {
        if (notificationBuilder != null) {
            String formattedDuration = isTimerRunning ? formatCallDuration(callDurationSeconds) : "00:00";
            notificationBuilder.setContentTitle(callerName + " - " + formattedDuration);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Notification permission missing during update");
                        return;
                    }
                    manager.notify(NOTIFICATION_ID, notificationBuilder.build());
                    Log.d(TAG, "Notification updated with title: " + callerName + " - " + formattedDuration);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to update notification", e);
                }
            }
        }
    }

    private void loadNetworkImage() {
        if (callerImageUrl == null || callerImageUrl.isEmpty()) {
            Log.w(TAG, "callerImageUrl is empty or null, using fallback icon");
            timerHandler.post(() -> buildCallNotification(null));
            return;
        }

        new Thread(() -> {
            try {
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
                FutureTarget<Bitmap> futureTarget;
                if (callerImageUrl.startsWith("file:///android_asset/")) {
                    String assetPath = callerImageUrl.replace("file:///android_asset/", "");
                    futureTarget = Glide.with(getApplicationContext())
                            .asBitmap()
                            .load("file:///android_asset/" + assetPath)
                            .transform(new CircleCrop())
                            .submit(size, size);
                } else {
                    futureTarget = Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(callerImageUrl)
                            .transform(new CircleCrop())
                            .submit(size, size);
                }
                Bitmap bitmap = futureTarget.get();
                timerHandler.post(() -> buildCallNotification(bitmap));
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error loading network image: " + callerImageUrl, e);
                timerHandler.post(() -> buildCallNotification(null));
            }
        }).start();
    }

    private void startCallTimer() {
        if (isTimerRunning) {
            Log.d(TAG, "Timer already running");
            return;
        }

        isTimerRunning = true;
        callDurationSeconds = 0;
        long startTime = prefs.getLong("call_start_time", System.currentTimeMillis());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("call_start_time", startTime);
        editor.apply();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                callDurationSeconds++; // Increment by 1 second
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(CALL_DURATION_KEY, callDurationSeconds);
                editor.apply();
                Log.d(TAG, "Timer tick: " + formatCallDuration(callDurationSeconds));
                broadcastTimerUpdate();
                updateNotificationTitle();
                timerHandler.postDelayed(this, 1000); // Update every 1 second
            }
        };
        timerHandler.post(timerRunnable);
        Log.d(TAG, "Call timer started with 1-second update interval");
    }

    private void broadcastTimerUpdate() {
        Intent intent = new Intent(ACTION_TIMER_UPDATE);
        intent.putExtra("duration_seconds", callDurationSeconds);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "Broadcasted timer update: " + callDurationSeconds + " seconds");
    }

    private boolean isVoipCallActive() {
        return isTimerRunning;
    }

    private void rejectTelephonyCall() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing ANSWER_PHONE_CALLS permission");
            notifyUserToRejectCall();
            return;
        }

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> clazz = Class.forName(tm.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            Object iTelephony = method.invoke(tm);
            Method endCallMethod = iTelephony.getClass().getDeclaredMethod("endCall");
            endCallMethod.invoke(iTelephony);
            Log.d(TAG, "Successfully rejected incoming telephony call");
        } catch (Exception e) {
            Log.e(TAG, "Failed to reject telephony call", e);
            notifyUserToRejectCall();
        }
    }

    private void notifyUserToRejectCall() {
        if (notificationBuilder != null) {
            notificationBuilder.setContentText("Incoming call detected. Please reject it to continue VoIP call.");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Notification permission missing");
                        return;
                    }
                    manager.notify(NOTIFICATION_ID, notificationBuilder.build());
                    Log.d(TAG, "Notified user to reject incoming call");
                } catch (Exception e) {
                    Log.e(TAG, "Failed to notify user to reject call", e);
                }
            }
        }
    }

    private void stopCall() {
        if (isTimerRunning && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
            isTimerRunning = false;
            Log.d(TAG, "Timer stopped");
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CALL_DURATION_KEY, callDurationSeconds);
        editor.apply();

        stopForeground(true);
        Intent broadcastIntent = new Intent("com.Appzia.enclosure.END_CALL");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.d(TAG, "Call ended and service stopped, broadcast sent: com.Appzia.enclosure.END_CALL");
        stopSelf();
    }

    private String formatCallDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacksAndMessages(null);
        isTimerRunning = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceReceiver);
        try {
            unregisterReceiver(phoneStateReceiver);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Phone state receiver was not registered", e);
        }
        Log.d(TAG, "Service destroyed");

//        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = callDataPrefs.edit();
//        editor.putString(KEY_MY_ROOM, "");
//        editor.putString(KEY_USERNAME, "");
//        editor.putString(KEY_INCOMING, "");
//        editor.putString(KEY_CREATED_BY, "");
//        editor.putString(KEY_ROOM_FLAG_KEY, "");
//        editor.putString(KEY_PHOTO_RECEIVER, ""); // Save original photo
//        editor.putString(KEY_NAME_RECEIVER, "");   // Save original name
//        editor.putLong(KEY_CALL_START_TIME, 0);
//        editor.putString(KEY_ROOM_ID, "");
//        editor.putString(PEER_ID, "");
//        editor.putString(KEY_RECEIVER_ID, "");
//        editor.putBoolean(KEY_IS_SENDER, false);
//        editor.apply();
    }
}