package com.Appzia.enclosure.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("STOP_MEDIA_PLAYER".equals(action)) {
            MediaPlayer player = MediaPlayerSingleton.getInstance().getMediaPlayer();
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                MediaPlayerSingleton.getInstance().setMediaPlayer(null);
            }
        }
    }
}
