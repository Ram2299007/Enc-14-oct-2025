package com.Appzia.enclosure.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.activities.CallActivity;

import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService;

public class CustomJitsiService extends JitsiMeetOngoingConferenceService {

//    @Override
//    protected Notification buildOngoingConferenceNotification(Context context, String conferenceTitle) {
//        // Create intent to bring user back to app
//        Intent intent = new Intent(context, CallActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        return new NotificationCompat.Builder(context, "VideoCallChannel")
//                .setSmallIcon(R.drawable.notification_icon) // ✅ your app’s icon here
//                .setContentTitle("Enclosure Video Call")
//                .setContentText("Ongoing call: " + conferenceTitle)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setCategory(NotificationCompat.CATEGORY_CALL)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true)
//                .build();

}
