package com.Appzia.enclosure.Screens;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.Appzia.enclosure.R;

public class NativeNotification extends AppCompatActivity {
    private static final String MESSAGES_CHANNEL_ID = "notification_channel_id";
    private static final int NOTIFICATION_ID = 123456;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;
    private static final String SHORTCUT_ID = "conversation_shortcut";
    private static final String TAG = "NativeNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_notification);

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            } else {
                triggerNotification();
            }
        } else {
            triggerNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                triggerNotification();
            } else {
                Log.w(TAG, "Notification permission denied");
                Toast.makeText(this, "Notifications are disabled. Enable them in settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void triggerNotification() {
        // Load a Bitmap from a drawable resource
        Bitmap notificationIcon = BitmapFactory.decodeResource(getResources(), R.drawable.deflarge);
        if (notificationIcon == null) {
            notificationIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon);
            if (notificationIcon == null) {
                Log.e(TAG, "Fallback notification icon not found");
                return;
            } else {
                Log.w(TAG, "User profile bitmap not found, using fallback icon");
            }
        }

        // Create a Person for the shortcut and notification
        Person person = createPerson("Olivia", notificationIcon);

        // Create a dynamic shortcut
        try {
            createDynamicShortcut(this, SHORTCUT_ID, "Olivia", person, notificationIcon);
        } catch (Exception e) {
            Log.e(TAG, "Failed to create dynamic shortcut", e);
        }

        // Call the notification method
        pushMessageNotification(this, notificationIcon, SHORTCUT_ID);
    }

    private void pushMessageNotification(Context context, Bitmap notificationIcon, String shortcutId) {
        createNotificationChannelIfNeeded();

        PendingIntent pendingIntent = createPendingIntent();
        NotificationCompat.MessagingStyle chatMessageStyle = createMessageNotificationStyle(
                "Olivia",
                notificationIcon,
                "Hello! How are you?"
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MESSAGES_CHANNEL_ID)
                .setStyle(chatMessageStyle)
                .setShortcutId(shortcutId) // Associate notification with shortcut
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MESSAGES_CHANNEL_ID,
                    "Messages",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for message notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            } else {
                Log.e(TAG, "Failed to get NotificationManager");
            }
        }
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, NativeNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        return PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );
    }

    private Person createPerson(String userName, Bitmap userProfileBitmap) {
        Person.Builder personBuilder = new Person.Builder().setName(userName);
        IconCompat icon = IconCompat.createWithBitmap(userProfileBitmap);
        personBuilder.setIcon(icon);
        return personBuilder.build();
    }

    private NotificationCompat.MessagingStyle createMessageNotificationStyle(String userName, Bitmap userProfileBitmap, String text) {
        Person person = createPerson(userName, userProfileBitmap);
        NotificationCompat.MessagingStyle chatMessageStyle = new NotificationCompat.MessagingStyle(person);
        NotificationCompat.MessagingStyle.Message notificationMessage = new NotificationCompat.MessagingStyle.Message(
                text,
                System.currentTimeMillis(),
                person
        );
        chatMessageStyle.addMessage(notificationMessage);
        return chatMessageStyle;
    }

    private void createDynamicShortcut(Context context, String shortcutId, String shortLabel, Person person, Bitmap notificationIcon) {
        // Check if shortcut already exists to avoid duplicates
        for (ShortcutInfoCompat shortcut : ShortcutManagerCompat.getDynamicShortcuts(context)) {
            if (shortcut.getId().equals(shortcutId)) {
                Log.d(TAG, "Shortcut already exists: " + shortcutId);
                return;
            }
        }

        Intent intent = createActivityClassIntent();
        ShortcutInfoCompat.Builder shortcutBuilder = new ShortcutInfoCompat.Builder(context, shortcutId)
                .setLongLived(true)
                .setIntent(intent)
                .setShortLabel(shortLabel)
                .setPerson(person);

        if (notificationIcon != null) {
            IconCompat icon = IconCompat.createWithBitmap(notificationIcon);
            shortcutBuilder.setIcon(icon);
        } else {
            Log.w(TAG, "Notification icon is null, shortcut created without icon");
        }

        try {
            ShortcutInfoCompat shortcut = shortcutBuilder.build();
            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);
            Log.d(TAG, "Dynamic shortcut created: " + shortcutId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to push dynamic shortcut", e);
            throw e; // Re-throw for debugging; consider removing in production
        }
    }

    private Intent createActivityClassIntent() {
        Intent intent = new Intent(this, NativeNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("conversation_id", "123"); // Example deep link data
        return intent;
    }
}