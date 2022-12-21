package com.nothing.systemui.keyguard.calendar;

import android.database.Cursor;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public class CalendarSimpleData {
    public static final int EVENT_STATUS_END = 3;
    public static final int EVENT_STATUS_NOW = 2;
    public static final int EVENT_STATUS_TO_BEGIN = 1;
    public static final int EVENT_STATUS_TO_SCHEDULE = 0;
    private static final String TAG = "CalendarSimpleData";
    private final long mEndTime;
    private final long mId;
    private final String mLocation;
    private final long mStartTime;
    private final String mTitle;

    public CalendarSimpleData(long j, String str, long j2, long j3, String str2) {
        this.mId = j;
        this.mTitle = str;
        this.mStartTime = j2;
        this.mEndTime = j3;
        this.mLocation = str2;
    }

    public long getToBeginTime() {
        return TimeUnit.MILLISECONDS.toMinutes(this.mStartTime) - TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
    }

    public int getEventStatus() {
        long toBeginTime = getToBeginTime();
        if (toBeginTime > 20) {
            return 0;
        }
        if (toBeginTime > 0) {
            return 1;
        }
        return toBeginTime > -10 ? 2 : 3;
    }

    public boolean isEventVisible() {
        int eventStatus = getEventStatus();
        return 1 == eventStatus || 2 == eventStatus;
    }

    public long getId() {
        return this.mId;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public static CalendarSimpleData buildDataFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        long j = cursor.getLong(cursor.getColumnIndex("event_id"));
        String string = cursor.getString(cursor.getColumnIndex("title"));
        String string2 = cursor.getString(cursor.getColumnIndex("eventLocation"));
        long j2 = cursor.getLong(cursor.getColumnIndex("begin"));
        long j3 = cursor.getLong(cursor.getColumnIndex("end"));
        Log.d(TAG, "Next calendar event is loaded. id = " + j + ", title = " + string + ", location = " + string2 + ", dtStart = " + j2 + ", dtEnd = " + j3);
        return new CalendarSimpleData(j, string, j2, j3, string2);
    }
}
