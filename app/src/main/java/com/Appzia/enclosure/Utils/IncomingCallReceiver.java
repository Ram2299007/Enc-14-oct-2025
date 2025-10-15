package com.Appzia.enclosure.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IncomingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle incoming call event

//        String callerName = intent.getStringExtra("caller_name");
//        Bitmap callerPhoto = intent.getParcelableExtra("caller_photo");
//
//        // Start the CallService with the necessary information
//        Intent serviceIntent = new Intent(context, CallService.class);
//        serviceIntent.putExtra("caller_name", callerName);
//        serviceIntent.putExtra("caller_photo", callerPhoto);
//        context.startService(serviceIntent);
    }
}
