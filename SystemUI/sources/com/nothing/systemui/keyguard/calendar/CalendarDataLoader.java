package com.nothing.systemui.keyguard.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.HandlerThread;
import android.provider.CalendarContract;
import java.util.Date;

public class CalendarDataLoader {
    public static final Uri CALENDAR_EVENTS_URL = CalendarContract.Instances.CONTENT_URI;
    private static final String CALENDAR_EVENT_SELECTOR = "isPrimary = ? and allDay =? and begin > ?";
    private static final String CALENDAR_EVENT_SORT_ORDER = "begin ASC LIMIT 1";
    private static final String[] EVENT_PROJECTS = {"event_id", "title", "begin", "end", "eventLocation"};
    private static final String TAG = "CalendarDataLoader";
    private static final long TEN_MIN = 600000;
    private ContentResolver mContentResolver;
    private HandlerThread mWorker;

    private CalendarDataLoader() {
        this.mContentResolver = null;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
    }

    public static CalendarDataLoader getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final CalendarDataLoader INSTANCE = new CalendarDataLoader();

        private InstanceHolder() {
        }
    }

    public CalendarSimpleData queryCalendarEvent(Context context) {
        this.mContentResolver = context.getContentResolver();
        long time = new Date(System.currentTimeMillis() - TEN_MIN).getTime();
        Uri.Builder buildUpon = CALENDAR_EVENTS_URL.buildUpon();
        ContentUris.appendId(buildUpon, time);
        ContentUris.appendId(buildUpon, Long.MAX_VALUE);
        Uri build = buildUpon.build();
        Bundle bundle = new Bundle();
        bundle.putInt("android:query-arg-limit", 1);
        bundle.putInt("android:query-arg-sort-direction", 0);
        bundle.putString("android:query-arg-sql-selection", CALENDAR_EVENT_SELECTOR);
        bundle.putStringArray("android:query-arg-sort-columns", new String[]{"begin"});
        bundle.putStringArray("android:query-arg-sql-selection-args", new String[]{"1", "0", time + ""});
        Cursor query = this.mContentResolver.query(build, EVENT_PROJECTS, bundle, (CancellationSignal) null);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    CalendarSimpleData buildDataFromCursor = CalendarSimpleData.buildDataFromCursor(query);
                    if (query != null) {
                        query.close();
                    }
                    return buildDataFromCursor;
                }
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        if (query != null) {
            query.close();
        }
        return null;
        throw th;
    }
}
