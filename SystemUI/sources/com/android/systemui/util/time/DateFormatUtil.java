package com.android.systemui.util.time;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.DateFormat;
import javax.inject.Inject;

public class DateFormatUtil {
    private final Context mContext;

    @Inject
    public DateFormatUtil(Context context) {
        this.mContext = context;
    }

    public boolean is24HourFormat() {
        return DateFormat.is24HourFormat(this.mContext, ActivityManager.getCurrentUser());
    }
}
