package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.Appzia.enclosure.Utils.MediaPlayerManager;

import java.io.IOException;

public class AudioPlaybackServiceGroup extends Service {
    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateProgressRunnable;
    private String audioUrl;
    private String profileImageUrl;
    String grpIdKey;
    private String songTitle;
    private String nameKey;
    private String captionKey;
    private boolean isPlaying = false;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "AudioPlaybackChannel";
    public static final String ACTION_PLAY = "com.Appzia.enclosure.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.Appzia.enclosure.ACTION_PAUSE";
    public static final String ACTION_SEEK = "com.Appzia.enclosure.ACTION_SEEK";
    public static final String ACTION_COMPLETED = "com.Appzia.enclosure.ACTION_COMPLETED";
    public static final String ACTION_UPDATE_PROGRESS = "com.Appzia.enclosure.ACTION_UPDATE_PROGRESS";

    private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                Log.d("AudioPlaybackServiceGroup", "Received action: " + intent.getAction());
                switch (intent.getAction()) {
                    case ACTION_PLAY:
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            isPlaying = true;
                            updateMediaSessionState();
                            updateNotification();
                            startProgressUpdate();
                            sendProgressBroadcast();
                        }
                        break;
                    case ACTION_PAUSE:
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            isPlaying = false;
                            updateMediaSessionState();
                            updateNotification();
                            stopProgressUpdate();
                            sendProgressBroadcast();
                        }
                        break;
                    case ACTION_SEEK:
                        int seekPosition = intent.getIntExtra("seekPosition", 0);
                        if (mediaPlayer != null) {
                            mediaPlayer.seekTo(seekPosition);
                            updateMediaSessionState();
                            sendProgressBroadcast();
                        }
                        break;
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_SEEK);
        registerReceiver(controlReceiver, filter, RECEIVER_EXPORTED);
        Log.d("AudioPlaybackServiceGroup", "Service created");

        mediaSession = new MediaSessionCompat(this, "AudioPlaybackServiceGroup");
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    isPlaying = true;
                    updateMediaSessionState();
                    updateNotification();
                    startProgressUpdate();
                    sendProgressBroadcast();
                }
            }

            @Override
            public void onPause() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying = false;
                    updateMediaSessionState();
                    updateNotification();
                    stopProgressUpdate();
                    sendProgressBroadcast();
                }
            }
        });
        mediaSession.setActive(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AudioPlaybackServiceGroup", "onStartCommand called");
        if (intent == null || intent.getStringExtra("audioUrl") == null) {
            Log.e("AudioPlaybackServiceGroup", "Intent or audioUrl is null");
            // Toast.makeText(this, "Invalid audio URL", Toast.LENGTH_SHORT).show();
            try {
                stopSelf();
            } catch (Exception e) {

            }
            return START_NOT_STICKY;
        }

        audioUrl = intent.getStringExtra("audioUrl");
        profileImageUrl = intent.getStringExtra("profileImageUrl");
        songTitle = intent.getStringExtra("songTitle");
        grpIdKey = intent.getStringExtra("grpIdKey");
        captionKey = intent.getStringExtra("captionKey");
        nameKey = intent.getStringExtra("nameKey");

        Log.d("AudioPlaybackServiceGroup", "Audio URL: " + audioUrl);

        try {
            Notification notification = createNotification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
            } else {
                startForeground(NOTIFICATION_ID, notification);
            }
            Log.d("AudioPlaybackServiceGroup", "startForeground called successfully");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Failed to start foreground: " + e.getMessage(), e);
            // Toast.makeText(this, "Failed to start service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            try {
                stopSelf();
            } catch (Exception ex) {

            }
            return START_NOT_STICKY;
        }

        setupMediaPlayer();

        return START_STICKY;
    }

    private void setupMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                isPlaying = true;
                updateMediaSessionState();
                updateNotification();
                startProgressUpdate();
                MediaPlayerManager.registerMediaPlayer(mediaPlayer);
                sendProgressBroadcast();
                Log.d("AudioPlaybackServiceGroup", "MediaPlayer started");
                // Send play broadcast
                Intent playIntent = new Intent(ACTION_PLAY);
                sendBroadcast(playIntent);
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                updateMediaSessionState();
                updateNotification();
                stopProgressUpdate();
                mediaPlayer.seekTo(0);
                sendProgressBroadcast();
                Log.d("AudioPlaybackServiceGroup", "MediaPlayer completed");
                // Remove notification and stop service
                stopForeground(true);
                stopSelf();
                // Send completion broadcast
                Intent completeIntent = new Intent(ACTION_COMPLETED);
                sendBroadcast(completeIntent);
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("AudioPlaybackServiceGroup", "MediaPlayer error: what=" + what + ", extra=" + extra);
                // Toast.makeText(AudioPlaybackServiceGroup.this, "Error playing audio: what=" + what + ", extra=" + extra, Toast.LENGTH_LONG).show();
                try {
                    stopSelf();
                } catch (Exception e) {

                }
                return true;
            });
        } catch (IOException e) {
            Log.e("AudioPlaybackServiceGroup", "Error setting up MediaPlayer: " + e.getMessage(), e);
            //  Toast.makeText(this, "Error loading audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            try {
                stopSelf();
            } catch (Exception ex) {

            }
        }
    }

    private void updateMediaSessionState() {
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .setState(isPlaying ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED,
                        mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0, 1.0f);
        mediaSession.setPlaybackState(stateBuilder.build());

        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songTitle != null ? songTitle : "Audio Message")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Unknown Artist")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer != null ? mediaPlayer.getDuration() : 0);
        mediaSession.setMetadata(metadataBuilder.build());
    }

    private Notification createNotification() {
        Log.d("AudioPlaybackServiceGroup", "Creating notification for song: " + songTitle);

        Intent playIntent = new Intent(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(
                this, 0, playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent pauseIntent = new Intent(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(
                this, 0, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent contentIntent = new Intent(this, chattingScreen.class);
        contentIntent.putExtra("friendUidKey",  grpIdKey); // <-- Add this!
        contentIntent.putExtra("nameKey",  nameKey); // <-- Add this!
        contentIntent.putExtra("captionKey",  captionKey); // <-- Add this!

        contentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                this, 0, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(songTitle != null ? songTitle : "Audio Message")
                .setContentText("Audio Message")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setStyle(new MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0))
                .addAction(new NotificationCompat.Action(
                        isPlaying ? R.drawable.pause : R.drawable.play,
                        isPlaying ? "Pause" : "Play",
                        isPlaying ? pausePendingIntent : playPendingIntent));

        return builder.build();
    }

    private void updateNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("AudioPlaybackServiceGroup", "Notification permission missing");
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, createNotification());
            Log.d("AudioPlaybackServiceGroup", "Notification updated");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Failed to update notification: " + e.getMessage(), e);
        }
    }

    private void startProgressUpdate() {
        stopProgressUpdate();
        updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    sendProgressBroadcast();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateProgressRunnable);
    }

    private void stopProgressUpdate() {
        if (updateProgressRunnable != null) {
            handler.removeCallbacks(updateProgressRunnable);
        }
    }

    private void sendProgressBroadcast() {
        if (mediaPlayer != null) {
            Intent intent = new Intent(ACTION_UPDATE_PROGRESS);
            intent.putExtra("currentPosition", mediaPlayer.getCurrentPosition());
            intent.putExtra("duration", mediaPlayer.getDuration());
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AudioPlaybackServiceGroup", "onDestroy called - cleaning up resources");
        cleanupAndStop();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("AudioPlaybackServiceGroup", "onTaskRemoved called - app removed from recent tasks");
        
        // Force cleanup when app is removed from recent tasks
        Log.d("AudioPlaybackServiceGroup", "App removed from recent tasks - forcing cleanup");
        cleanupAndStop();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d("AudioPlaybackServiceGroup", "onTrimMemory called with level: " + level + " - cleaning up");
            cleanupAndStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("AudioPlaybackServiceGroup", "onLowMemory called - cleaning up");
        cleanupAndStop();
    }

    private void cleanupAndStop() {
        Log.d("AudioPlaybackServiceGroup", "Starting comprehensive cleanup...");
        Log.d("AudioPlaybackServiceGroup", "Current thread: " + Thread.currentThread().getName());
        Log.d("AudioPlaybackServiceGroup", "Service instance: " + this.hashCode());
        
        // Stop and release MediaPlayer
        if (mediaPlayer != null) {
            try {
                Log.d("AudioPlaybackServiceGroup", "MediaPlayer exists, isPlaying: " + mediaPlayer.isPlaying());
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    Log.d("AudioPlaybackServiceGroup", "MediaPlayer stopped");
                }
                mediaPlayer.release();
                Log.d("AudioPlaybackServiceGroup", "MediaPlayer released");
            } catch (Exception e) {
                Log.e("AudioPlaybackServiceGroup", "Error stopping MediaPlayer: " + e.getMessage(), e);
            } finally {
                mediaPlayer = null;
            }
        } else {
            Log.d("AudioPlaybackServiceGroup", "MediaPlayer is null");
        }

        // Release MediaSession
        if (mediaSession != null) {
            try {
                mediaSession.release();
                Log.d("AudioPlaybackServiceGroup", "MediaSession released");
            } catch (Exception e) {
                Log.e("AudioPlaybackServiceGroup", "Error releasing MediaSession: " + e.getMessage());
            } finally {
                mediaSession = null;
            }
        }

        // Stop progress updates
        stopProgressUpdate();
        Log.d("AudioPlaybackServiceGroup", "Progress updates stopped");

        // Unregister broadcast receiver
        try {
            unregisterReceiver(controlReceiver);
            Log.d("AudioPlaybackServiceGroup", "Broadcast receiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.d("AudioPlaybackServiceGroup", "Broadcast receiver already unregistered");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error unregistering receiver: " + e.getMessage());
        }

        // Remove notification and stop foreground
        try {
            Log.d("AudioPlaybackServiceGroup", "Attempting to stop foreground service...");
            stopForeground(true);
            Log.d("AudioPlaybackServiceGroup", "Foreground service stopped");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error stopping foreground: " + e.getMessage(), e);
        }

        // Cancel notification
        try {
            Log.d("AudioPlaybackServiceGroup", "Attempting to cancel notification...");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NOTIFICATION_ID);
            Log.d("AudioPlaybackServiceGroup", "Notification cancelled");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error cancelling notification: " + e.getMessage(), e);
        }

        // Stop the service
        try {
            stopSelf();
            Log.d("AudioPlaybackServiceGroup", "Service stopped");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error stopping service: " + e.getMessage());
        }

        Log.d("AudioPlaybackServiceGroup", "Comprehensive cleanup completed");
    }

    private boolean isAppBeingKilled() {
        // Check if the app is being killed by checking recent tasks
        try {
            android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
            if (am != null) {
                java.util.List<android.app.ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
                if (processes != null) {
                    for (android.app.ActivityManager.RunningAppProcessInfo process : processes) {
                        if (process.processName.equals(getPackageName())) {
                            // App is still running
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error checking app state: " + e.getMessage());
        }
        return true;
    }

    // Method to handle app kill scenarios
    public void handleAppKill() {
        Log.d("AudioPlaybackServiceGroup", "App kill detected - stopping service immediately");
        cleanupAndStop();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("AudioPlaybackServiceGroup", "onUnbind called");
        // Don't stop the service here, let it continue playing in background
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Method to manually stop the service (can be called from activities)
    public static void stopService(Context context) {
        try {
            Intent intent = new Intent(context, AudioPlaybackServiceGroup.class);
            context.stopService(intent);
            Log.d("AudioPlaybackServiceGroup", "Service stop requested from context");
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error stopping service: " + e.getMessage());
        }
    }

    // Force stop method for immediate cleanup
    public void forceStop() {
        Log.d("AudioPlaybackServiceGroup", "Force stop called - immediate cleanup");
        cleanupAndStop();
    }

    // Check if service is running
    public static boolean isServiceRunning(Context context) {
        try {
            android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                for (android.app.ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
                    if (AudioPlaybackServiceGroup.class.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("AudioPlaybackServiceGroup", "Error checking service status: " + e.getMessage());
        }
        return false;
    }
}