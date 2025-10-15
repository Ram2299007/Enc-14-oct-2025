package com.Appzia.enclosure.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeCalculator {

    public static String getTimeDifference(String timeStr1, String timeStr2) {
        // ✅ Only hours and minutes, NO SECONDS
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            Date time1 = format.parse(timeStr1);
            Date time2 = format.parse(timeStr2);

            long diffInMillis = time2.getTime() - time1.getTime();

            if (diffInMillis < 0) {
                return "0m";
            }

            long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;

            if (hours > 0 && minutes > 0) {
                return String.format("%dh %dm", hours, minutes);
            } else if (hours > 0) {
                return String.format("%dh", hours);
            } else {
                return String.format("%dm", minutes);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "0m";
        }
    }
}
