package com.Appzia.enclosure.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class PendingIntentHelper {
    private static final String TAG = "PendingIntentHelper";
    // Add this inside your PendingIntentHelper class or a Constants class
    private static final int FLAG_ALLOW_BACKGROUND_ACTIVITY_STARTS = 0x04000000;

    public static PendingIntent getActivityPendingIntent(
            Context context,
            int requestCode,
            Intent intent,
            boolean immutable,
            boolean allowBgLaunch,
            int... additionalFlags // Use varargs for flexibility
    ) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;

        // Clear potential mutable/immutable bits first
        flags &= ~(PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_MUTABLE);

        // Apply either immutable or mutable, not both
        if (immutable) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        } else {
            flags |= PendingIntent.FLAG_MUTABLE;
        }

        // ✅ Add background launch flag if API 34+ and requested
        if (Build.VERSION.SDK_INT >= 34 && allowBgLaunch) {
            flags |= FLAG_ALLOW_BACKGROUND_ACTIVITY_STARTS;
            Log.d(TAG, "Applied background start flag for SDK 34+");
        }

        for (int f : additionalFlags) {
            flags |= f;
        }

        Log.d(TAG, "Final flags for PendingIntent.getActivity: " + Integer.toHexString(flags));

        return PendingIntent.getActivity(context, requestCode, intent, flags);
    }


    public static PendingIntent getPendingIntent1(Context context, Intent resultIntent) {
        return getActivityPendingIntent(
                context,
                1,
                resultIntent,
                true,  // ✅ true = only FLAG_IMMUTABLE
                false,
                Intent.FLAG_ACTIVITY_NEW_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getPendingIntent2(Context context, Intent resultIntent2) {
        return getActivityPendingIntent(
                context,
                2,
                resultIntent2,
                true,
                false,
                Intent.FLAG_ACTIVITY_NEW_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getPendingIntent3(Context context, Intent resultIntent3) {
        return getActivityPendingIntent(
                context,
                3,
                resultIntent3,
                true,
                true,  // ✅ Background start allowed
                Intent.FLAG_ACTIVITY_NEW_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getRejectPendingIntent(Context context, Intent rejectIntent) {
        return getActivityPendingIntent(
                context,
                4,
                rejectIntent,
                true,
                true, // ✅ allow background start
                Intent.FLAG_ACTIVITY_NEW_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getBroadcastPendingIntent(
            Context context,
            int requestCode,
            Intent intent,
            boolean immutable,
            int... additionalFlags
    ) {
        int baseFlags = 0;
        for (int flag : additionalFlags) {
            // Strip out MUTABLE/IMMUTABLE if accidentally passed
            if (flag != PendingIntent.FLAG_MUTABLE && flag != PendingIntent.FLAG_IMMUTABLE) {
                baseFlags |= flag;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            baseFlags |= immutable ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_MUTABLE;
        }

        return PendingIntent.getBroadcast(context, requestCode, intent, baseFlags);
    }

}
