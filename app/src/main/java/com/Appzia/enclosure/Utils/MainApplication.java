package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;


import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainApplication extends Application {

  public static MediaPlayer player;
  private static final List<Activity> activityList = new ArrayList<>();
  public static final String CHANNEL_ID = "AudioPlaybackChannel";
  private static MainApplication instance;
  public static boolean isFirstLaunch = true;
  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;


    FirebaseApp.initializeApp(this);
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


    createNotificationChannel();
    createNotificationChannel2();

  }

  public static MainApplication getInstance() {
    return instance;
  }
  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "Call Channel";
      String description = "Channel for ongoing call notifications";
      int importance = NotificationManager.IMPORTANCE_LOW;
      NotificationChannel channel = new NotificationChannel("ONGOING_CALL_ID", name, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(channel);
      }
    }
  }

  private void createNotificationChannel2() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
              CHANNEL_ID,
              "Audio Playback",
              NotificationManager.IMPORTANCE_LOW // Use LOW to avoid sound/vibration
      );
      channel.setDescription("Channel for audio playback notifications");
      NotificationManager manager = getSystemService(NotificationManager.class);
      if (manager != null) {
        manager.createNotificationChannel(channel);
      } else {
        // Log error if NotificationManager is null
        android.util.Log.e("MyApplication", "Failed to get NotificationManager");
      }
    }
  }

  // ==== Activity Management ====

  public static void registerActivity(Activity activity) {
    activityList.add(activity);
  }

  public static void unregisterActivity(Activity activity) {
    activityList.remove(activity);
  }

  public static void finishAllActivities() {
    for (Activity activity : new ArrayList<>(activityList)) {
      if (!activity.isFinishing()) {
        activity.finish();
      }
    }
    activityList.clear();
  }

  public static void finishActivitiesByClass(Class<?>... classes) {
    for (Class<?> cls : classes) {
      Iterator<Activity> iterator = activityList.iterator();
      while (iterator.hasNext()) {
        Activity activity = iterator.next();
        if (activity.getClass().equals(cls) && !activity.isFinishing()) {
          activity.finish();
          iterator.remove();
        }
      }
    }
  }
}