package com.Appzia.enclosure.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            
            // Retrieve the notification data
            String title = extras.getString("title");
            String message = extras.getString("message");

            Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
            // ... retrieve other data fields as needed
            
            // Process the notification data
            // ...
        }
    }
}
