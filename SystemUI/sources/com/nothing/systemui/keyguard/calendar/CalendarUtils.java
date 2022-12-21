package com.nothing.systemui.keyguard.calendar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import java.util.Calendar;
import java.util.Locale;

public class CalendarUtils {
    private static final long ONE_DAY_TIME_IN_MILLS = 86400000;
    private static final int OPEN_CALENDAR_EVENT_REQUEST_CODE = 101;

    public static void enterCalendarEventInterface(Context context, RemoteViews remoteViews, int i, CalendarSimpleData calendarSimpleData) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(calendarSimpleData.getId() + "").build());
        intent.putExtra("beginTime", calendarSimpleData.getStartTime());
        intent.putExtra("endTime", calendarSimpleData.getEndTime());
        intent.setFlags(268468224);
        remoteViews.setOnClickPendingIntent(i, PendingIntent.getActivity(context, 101, intent, 201326592));
    }

    public static String formatTime(long j, String str) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), str), Locale.getDefault()).format(Long.valueOf(j));
    }

    public static boolean isSameYear(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        int i = instance.get(1);
        instance.setTimeInMillis(j2);
        if (i == instance.get(1)) {
            return true;
        }
        return false;
    }

    public static boolean isSameDay(long j, long j2) {
        if (!isSameYear(j, j2)) {
            return false;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        int i = instance.get(6);
        instance.setTimeInMillis(j2);
        if (i == instance.get(6)) {
            return true;
        }
        return false;
    }

    public static boolean isNextDay(long j, long j2) {
        return isSameDay(j + ONE_DAY_TIME_IN_MILLS, j2);
    }

    public static boolean isPreDay(long j, long j2) {
        return isSameDay(j2 + ONE_DAY_TIME_IN_MILLS, j);
    }
}
