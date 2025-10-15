package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.Notification;
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
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;

import java.util.concurrent.ExecutionException;

public class CallServiceVideoCall extends Service {
    private static final String TAG = "CallServiceVideoCall";
    private static final String PREFS_NAME = "CallPrefs";
    private static final String CHANNEL_ID = "VideoCallChannel";
    private static final String CHANNEL_NAME = "Video Call Notifications";
    private static final int NOTIFICATION_ID = 529;
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
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_IS_SENDER = "isSender";
    public static final String ACTION_START_TIMER = "com.Appzia.enclosure.START_TIMER.VIDEO";
    public static final String ACTION_END_CALL = "com.Appzia.enclosure.END_CALL_VIDEO";
    public static final String ACTION_TIMER_UPDATE = "com.Appzia.enclosure.TIMER_UPDATE_VIDEO";
    public static final String ACTION_REQUEST_DURATION = "com.Appzia.enclosure.REQUEST_DURATION_VIDEO";
    private static final String CALL_DURATION_KEY = "call_duration_video";

    private String callerImageUrl = "";
    private String callerName = "Video Call";
    private String myRoom;
    private String username;
    private String incoming;
    private String createdBy;
    private String roomFlagKey;
    private String photoReceiver;
    private String nameReceiver;
    private String roomId;
    private String receiverId;
    private boolean iAmSender;
    private long callStartTime;
    private NotificationCompat.Builder notificationBuilder;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;
    private long callDurationSeconds = 0;
    private boolean isTimerRunning = false;
    private AudioRecord audioRecord;
    private SharedPreferences prefs;
    // Removed native camera imports

    private final BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Received broadcast: " + action);
            if (ACTION_REQUEST_DURATION.equals(action)) {
                broadcastTimerUpdate();
            } else if (ACTION_START_TIMER.equals(action)) {
                startCallTimer();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: Service created");
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REQUEST_DURATION);
        filter.addAction(ACTION_START_TIMER);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, filter);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    android.app.NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Notifications for ongoing video calls");
            channel.setSound(null, null);
            android.app.NotificationManager manager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        Log.d(TAG, "onStartCommand: Intent=" + (intent != null ? intent.getAction() : "null"));

        if (intent != null && ACTION_END_CALL.equals(intent.getAction())) {
            stopCall();
            stopMicrophoneUsage();
            // Removed stopCameraPreview();
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        // Retrieve all call data from SharedPreferences
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        myRoom = callDataPrefs.getString(KEY_MY_ROOM, null);
        username = callDataPrefs.getString(KEY_USERNAME, null);
        incoming = callDataPrefs.getString(KEY_INCOMING, null);
        createdBy = callDataPrefs.getString(KEY_CREATED_BY, null);
        roomFlagKey = callDataPrefs.getString(KEY_ROOM_FLAG_KEY, null);
        photoReceiver = callDataPrefs.getString(KEY_PHOTO_RECEIVER, null);
        nameReceiver = callDataPrefs.getString(KEY_NAME_RECEIVER, null);
        callStartTime = callDataPrefs.getLong(KEY_CALL_START_TIME, System.currentTimeMillis());
        roomId = callDataPrefs.getString(KEY_ROOM_ID, null);
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, null);
        iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);

        // Log retrieved data to verify
        Log.d(TAG, "Retrieved from SharedPreferences: " +
                "myRoom=" + myRoom + ", username=" + username + ", incoming=" + incoming +
                ", createdBy=" + createdBy + ", roomFlagKey=" + roomFlagKey +
                ", photoReceiver=" + photoReceiver + ", nameReceiver=" + nameReceiver +
                ", callStartTime=" + callStartTime + ", roomId=" + roomId +
                ", receiverId=" + receiverId + ", iAmSender=" + iAmSender);

        // Set callerName and callerImageUrl with defaults if needed
        callerName = (nameReceiver == null || nameReceiver.isEmpty()) ? "Video Call" : nameReceiver;
        callerImageUrl = (photoReceiver == null || photoReceiver.isEmpty()) ? "" : photoReceiver;

        // Save call duration and start time
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CALL_DURATION_KEY, 0);
        editor.putLong("call_start_time", callStartTime);
        editor.apply();
        callDurationSeconds = 0;

        Log.d(TAG, "Starting service with callerName: " + callerName + ", callerImageUrl: " + callerImageUrl);
        buildCallNotification(null);
        startCallTimer();
        loadNetworkImage();
        triggerMicrophoneUsage();
        // Removed startCameraPreview();
        return START_STICKY;
    }

    private void buildCallNotification(Bitmap networkBitmap) {
        Person.Builder personBuilder = new Person.Builder().setName(callerName);
        if (networkBitmap != null) {
            personBuilder.setIcon(IconCompat.createWithBitmap(networkBitmap));
            Log.d(TAG, "Using circular network image for Person icon");
        } else {
            personBuilder.setIcon(IconCompat.createWithResource(this, R.drawable.deflarge));
            Log.d(TAG, "Using fallback icon for Person");
        }
        Person caller = personBuilder.build();

        Intent intent = new Intent(this, MainActivityVideoCall.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent endCallIntent = new Intent(this, CallServiceVideoCall.class);
        endCallIntent.setAction(ACTION_END_CALL);
        PendingIntent endCallPendingIntent = PendingIntent.getService(this, 100, endCallIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.CallStyle callStyle = NotificationCompat.CallStyle.forOngoingCall(caller, endCallPendingIntent);

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(callerName + " " + formatCallDuration(callDurationSeconds))
                .setStyle(callStyle)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setWhen(callStartTime)
                .setUsesChronometer(true)
                .setContentIntent(contentIntent);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Notification permission missing");
                stopSelf();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(NOTIFICATION_ID, notificationBuilder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL | ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA | ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE);
            } else {
                startForeground(NOTIFICATION_ID, notificationBuilder.build());
            }
            Log.d(TAG, "Notification posted with ID: " + NOTIFICATION_ID + ", duration=" + formatCallDuration(callDurationSeconds));
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException starting foreground service", e);
            stopSelf();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error starting foreground service", e);
            stopSelf();
        }
    }

    private void startCallTimer() {
        if (isTimerRunning) {
            Log.d(TAG, "startCallTimer: Timer already running, skipping start");
            return;
        }

        isTimerRunning = true;
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                callDurationSeconds++;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(CALL_DURATION_KEY, callDurationSeconds);
                editor.apply();
                Log.d(TAG, "Timer tick: callDurationSeconds=" + callDurationSeconds + ", formatted=" + formatCallDuration(callDurationSeconds));
                broadcastTimerUpdate();
                if (notificationBuilder != null) {
                    try {
                        notificationBuilder.setContentTitle(callerName + " " + formatCallDuration(callDurationSeconds));
                        android.app.NotificationManager manager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (manager != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    ActivityCompat.checkSelfPermission(CallServiceVideoCall.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                Log.e(TAG, "Notification permission missing during update");
                                stopCall();
                                return;
                            }
                            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
                            Log.d(TAG, "Notification updated: " + callerName + " " + formatCallDuration(callDurationSeconds));
                        } else {
                            Log.e(TAG, "NotificationManager is null during update");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to update notification", e);
                    }
                } else {
                    Log.w(TAG, "notificationBuilder is null, rebuilding notification");
                    buildCallNotification(null);
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
        Log.d(TAG, "Call timer started with callStartTime=" + callStartTime);
    }

    private void broadcastTimerUpdate() {
        Intent intent = new Intent(ACTION_TIMER_UPDATE);
        intent.putExtra("duration_seconds", callDurationSeconds);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "Broadcasted timer update: " + callDurationSeconds + " seconds");
    }

    private void stopCall() {
        Log.d(TAG, "stopCall: Ending call and resetting state");
        if (isTimerRunning && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
            isTimerRunning = false;
            Log.d(TAG, "Timer stopped");
        }
        // Clear SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("call_start_time", 0);
        editor.putLong(CALL_DURATION_KEY, 0);
        editor.apply();

        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor callDataEditor = callDataPrefs.edit();
        callDataEditor.putString(KEY_MY_ROOM, "");
        callDataEditor.putString(KEY_USERNAME, "");
        callDataEditor.putString(KEY_INCOMING, "");
        callDataEditor.putString(KEY_CREATED_BY, "");
        callDataEditor.putString(KEY_ROOM_FLAG_KEY, "");
        callDataEditor.putString(KEY_PHOTO_RECEIVER, "");
        callDataEditor.putString(KEY_NAME_RECEIVER, "");
        callDataEditor.putLong(KEY_CALL_START_TIME, 0);
        callDataEditor.putString(KEY_ROOM_ID, "");
        callDataEditor.putString(KEY_RECEIVER_ID, "");
        callDataEditor.putBoolean(KEY_IS_SENDER, false);
        callDataEditor.apply();

        stopMicrophoneUsage();
        // Removed stopCameraPreview();
        stopForeground(true);
        stopSelf();

        Intent broadcastIntent = new Intent("com.Appzia.enclosure.END_CALL_UI_VIDEO");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.d(TAG, "Call ended and service stopped");
    }

    private String formatCallDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void loadNetworkImage() {
        if (callerImageUrl.isEmpty()) {
            Log.w(TAG, "callerImageUrl is empty, using fallback icon");
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

    private void triggerMicrophoneUsage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "RECORD_AUDIO permission not granted");
            return;
        }

        int bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        try {
            audioRecord.startRecording();
            byte[] buffer = new byte[bufferSize];
            audioRecord.read(buffer, 0, buffer.length);
        } catch (Exception e) {
            Log.e(TAG, "Error starting microphone", e);
            audioRecord = null;
        }
    }

    private void stopMicrophoneUsage() {
        if (audioRecord != null) {
            try {
                audioRecord.stop();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping microphone", e);
            } finally {
                try {
                    audioRecord.release();
                } catch (Exception e) {
                    Log.e(TAG, "Error releasing microphone", e);
                }
                audioRecord = null;
            }
        }
    }

    // Removed startCameraPreview();

    // Removed stopCameraPreview();

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: Restarting service with call data");
        if (callStartTime > 0) {
            Intent restartIntent = new Intent(this, CallServiceVideoCall.class);
            startService(restartIntent);
            Log.d(TAG, "onTaskRemoved: Restarted service with callStartTime=" + callStartTime);
        } else {
            Log.d(TAG, "onTaskRemoved: No active call, not restarting");
        }
        super.onTaskRemoved(rootIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Cleaning up service");
        timerHandler.removeCallbacksAndMessages(null);
        isTimerRunning = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceReceiver);
        stopMicrophoneUsage();
        // Removed stopCameraPreview();

//        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = callDataPrefs.edit();
//        editor.putString(KEY_MY_ROOM, "");
//        editor.putString(KEY_USERNAME, "");
//        editor.putString(KEY_INCOMING, "");
//        editor.putString(KEY_CREATED_BY, "");
//        editor.putString(KEY_ROOM_FLAG_KEY, "");
//        editor.putString(KEY_PHOTO_RECEIVER, "");
//        editor.putString(KEY_NAME_RECEIVER, "");
//        editor.putLong(KEY_CALL_START_TIME, 0);
//        editor.putString(KEY_ROOM_ID, "");
//        editor.putString(KEY_RECEIVER_ID, "");
//        editor.putBoolean(KEY_IS_SENDER, false);
//        editor.apply();

        Log.d(TAG, "onDestroy: Reset timer and destroyed service");
        super.onDestroy();
    }
}