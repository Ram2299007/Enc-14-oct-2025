package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationSenderMisscall;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeclineVideoCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract extras
        String userName = intent.getStringExtra("caller_name");
        String roomVideo = intent.getStringExtra("roomVideo");
        String receiverId = intent.getStringExtra("receiverId");
        String userFcmToken = intent.getStringExtra("userFcmToken");
        String title = intent.getStringExtra("title");
        String photo = intent.getStringExtra("photo");
        String device_type = intent.getStringExtra("device_type");
        int notificationId = intent.getIntExtra("notificationId", 101); // Using 101 to avoid conflict with voice call

        // Stop ringtone
        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }

        // Send missed call notification
        FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(
                userFcmToken, title, Constant.videoMissCall, photo, userName, device_type);
        fmisscall.SendNotifications();

        // Record missed call
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

        Log.d("currentTime", currentTime);
        Log.d("receiverIddytdyxwaux", receiverId + Constant.getSF.getString(Constant.UID_KEY, ""));


        try {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Constant.getSfFuncion(context);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String deleteroom = uid + roomVideo;
            String pushKey = database.getReference().child("declineVideoKey").child(deleteroom).push().getKey();
            database.getReference().child("declineVideoKey").child(deleteroom).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("TAG", "HDUWAIFIWAHIFHAWHIAWHIWAOW "+"SUCCESS");

                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        NotificationManagerCompat.from(context).cancel(notificationId);
    }
}