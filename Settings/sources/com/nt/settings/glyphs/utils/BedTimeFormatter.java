package com.nt.settings.glyphs.utils;

import android.content.Context;
import android.text.TextUtils;
import com.android.settings.R;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
/* loaded from: classes2.dex */
public class BedTimeFormatter {
    private final int[] ID_DAY_OF_WEEKS = {R.string.nt_glyphs_monday, R.string.nt_glyphs_tuesday, R.string.nt_glyphs_wednesday, R.string.nt_glyphs_thursday, R.string.nt_glyphs_friday, R.string.nt_glyphs_saturday, R.string.nt_glyphs_sunday};
    private DateFormat mTimeFormatter;

    public BedTimeFormatter(Context context) {
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        this.mTimeFormatter = timeFormat;
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public String getFormattedTimeString(LocalTime localTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(this.mTimeFormatter.getTimeZone());
        calendar.set(11, localTime.getHour());
        calendar.set(12, localTime.getMinute());
        calendar.set(13, 0);
        calendar.set(14, 0);
        return this.mTimeFormatter.format(calendar.getTime());
    }

    public int[] str2Array(String str) {
        int[] iArr = new int[2];
        if (str != null && str.length() != 0) {
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                iArr[i] = Integer.parseInt(split[i]);
            }
        }
        return iArr;
    }

    public String getWeekSelectedTitle(Context context, String str) {
        if ("0000000".equals(str) || TextUtils.isEmpty(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_title_unchecked);
        }
        if ("1111100".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_weekays);
        }
        if ("1111111".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_everyday);
        }
        if ("0000011".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_weekends);
        }
        char[] charArray = str.toCharArray();
        int i = -1;
        String str2 = "";
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < charArray.length; i4++) {
            if ('1' == charArray[i4]) {
                i2++;
                str2 = str2.length() == 0 ? context.getString(this.ID_DAY_OF_WEEKS[i4]) : str2 + " & " + context.getString(this.ID_DAY_OF_WEEKS[i4]);
                i3 = i4;
            } else {
                i = i4;
            }
        }
        if (i2 <= 1) {
            return context.getString(this.ID_DAY_OF_WEEKS[i3]);
        }
        return i2 == 2 ? str2 : i2 < 6 ? context.getString(R.string.nt_glyphs_week_repeat_title_selectdays, Integer.valueOf(i2)) : context.getString(R.string.nt_glyphs_week_repeat_title_six_day, context.getString(this.ID_DAY_OF_WEEKS[i]));
    }

    public String getWeekSelectedSummary(Context context, String str) {
        if ("0000000".equals(str) || TextUtils.isEmpty(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_unchecked);
        }
        if ("1111100".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_weekays);
        }
        if ("1111111".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_everyday);
        }
        if ("0000011".equals(str)) {
            return context.getString(R.string.nt_glyphs_week_repeat_weekends);
        }
        char[] charArray = str.toCharArray();
        int i = -1;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < charArray.length; i4++) {
            if ('1' == charArray[i4]) {
                i2++;
                i3 = i4;
            } else {
                i = i4;
            }
        }
        if (i2 <= 1) {
            return context.getString(this.ID_DAY_OF_WEEKS[i3]);
        }
        return i2 < 6 ? context.getString(R.string.nt_glyphs_week_repeat_selectdays) : context.getString(R.string.nt_glyphs_week_repeat_six_day, context.getString(this.ID_DAY_OF_WEEKS[i]));
    }
}
