package com.Appzia.enclosure.Utils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.Appzia.enclosure.activities.ConnectingActivity;

public class EndCallReceiverVideo extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Stop your video call service
        Intent stopIntent = new Intent(context, CallServiceVideoCall.class);
        context.stopService(stopIntent);


//        if (ConnectingActivity.instance != null) {
//            ConnectingActivity.instance.triggerEndCallButton();
//        } else {
//
//            MainApplication.finishActivitiesByClass(ConnectingActivity.class);
//        }

    }
}
