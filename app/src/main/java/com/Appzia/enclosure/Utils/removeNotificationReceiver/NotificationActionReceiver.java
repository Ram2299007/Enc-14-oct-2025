package com.Appzia.enclosure.Utils.removeNotificationReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Cancel the notification
        if (notificationManager != null) {
            notificationManager.cancel(100); // Use the same ID you used when creating the notification
        }
    }
}
