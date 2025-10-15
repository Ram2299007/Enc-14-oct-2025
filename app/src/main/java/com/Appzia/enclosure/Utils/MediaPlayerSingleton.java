package com.Appzia.enclosure.Utils;

import android.media.MediaPlayer;

public class MediaPlayerSingleton {
    private static MediaPlayerSingleton instance;
    private MediaPlayer mediaPlayer;

    private MediaPlayerSingleton() {}

    public static synchronized MediaPlayerSingleton getInstance() {
        if (instance == null) {
            instance = new MediaPlayerSingleton();
        }
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
