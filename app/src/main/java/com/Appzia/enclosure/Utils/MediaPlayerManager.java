package com.Appzia.enclosure.Utils;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MediaPlayerManager {
    private static List<MediaPlayer> activeMediaPlayers = new ArrayList<>();

    public static void registerMediaPlayer(MediaPlayer mediaPlayer) {
        activeMediaPlayers.add(mediaPlayer);
    }

    public static void unregisterMediaPlayer(MediaPlayer mediaPlayer) {
        activeMediaPlayers.remove(mediaPlayer);
    }

    public static void stopAllMediaPlayers() {
        for (MediaPlayer mediaPlayer : activeMediaPlayers) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        activeMediaPlayers.clear();
    }
}
