package com.Appzia.enclosure.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    private ConnectivityListener listener;

    public void setConnectivityListener(ConnectivityListener listener) {
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();
            if (listener != null) {
                listener.onConnectivityChanged(isConnected);
            }
        }
    }

    public interface ConnectivityListener {
        void onConnectivityChanged(boolean isConnected);
    }


}
