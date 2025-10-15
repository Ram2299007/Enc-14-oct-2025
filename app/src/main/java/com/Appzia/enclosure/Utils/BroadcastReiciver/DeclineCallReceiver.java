package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationSenderMisscall;
import com.Appzia.enclosure.Utils.MainApplication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeclineCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract extras
        String userName = intent.getStringExtra("caller_name");
        String roomvoice = intent.getStringExtra("roomVoice");
        String receiverId = intent.getStringExtra("receiverId");
        String userFcmToken = intent.getStringExtra("userFcmToken");
        String title = intent.getStringExtra("title");
        String photo = intent.getStringExtra("photo");
        String device_type = intent.getStringExtra("device_type");
        int notificationId = intent.getIntExtra("notificationId", 100);

        // Stop ringtone
        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }

        // Send missed call notification
        FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(
                userFcmToken, title, Constant.voiceMissCall, photo, userName, device_type);
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

//        Webservice.create_group_callingMissCall(
//                Constant.getSF.getString(Constant.UID_KEY, ""), roomvoice, "", dateStr, currentTime, "2", currentTime, "1");


        // todo for voice
        try {
            Constant.getSfFuncion(context);
            String uid = Constant.getSF.getString(Constant.UID_KEY,"");

            String deleteroom = uid + roomvoice;

            String pushKey = FirebaseDatabase.getInstance().getReference().child("declineVoiceKey").child(deleteroom).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("declineVoiceKey").child(deleteroom).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // Cancel notification
        NotificationManagerCompat.from(context).cancel(notificationId);
    }
}