package com.Appzia.enclosure.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

public class EndCallReceiverVoice extends BroadcastReceiver {
    private static final String TAG = "EndCallReceiverVoice";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent: " + intent.getAction());
        // Stop the CallServiceVoiceCall service
        Intent stopIntent = new Intent(context, CallServiceVoiceCall.class);
        context.stopService(stopIntent);
        // Notify CallVoiceActivity to end the call
        Intent broadcastIntent = new Intent("com.Appzia.enclosure.END_CALL_UI");
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}