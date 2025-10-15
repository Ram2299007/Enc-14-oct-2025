package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d("PowerButton", "Screen turned OFF");
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Log.d("PowerButton", "Screen turned ON");
        } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
            String reason = intent.getStringExtra("reason");
            if ("globalactions".equals(reason)) {
                Log.d("PowerButton", "Power button long press");
            }
        }
    }
}