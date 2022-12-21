package com.android.settingslib.utils;

import android.content.Context;
import android.icu.text.DisplayContext;
import android.icu.text.MeasureFormat;
import android.icu.text.MessageFormat;
import android.icu.text.NumberFormat;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.icu.util.ULocale;
import android.text.SpannableStringBuilder;
import android.text.style.TtsSpan;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StringUtil {
    private static final int LIMITED_TIME_UNIT_COUNT = 2;
    public static final int SECONDS_PER_DAY = 86400;
    public static final int SECONDS_PER_HOUR = 3600;
    public static final int SECONDS_PER_MINUTE = 60;

    public static CharSequence formatElapsedTime(Context context, double d, boolean z, boolean z2) {
        int i;
        int i2;
        int i3;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int floor = (int) Math.floor(d / 1000.0d);
        if (!z) {
            floor += 30;
        }
        if (floor >= 86400) {
            i = floor / SECONDS_PER_DAY;
            floor -= SECONDS_PER_DAY * i;
        } else {
            i = 0;
        }
        if (floor >= 3600) {
            i2 = floor / 3600;
            floor -= i2 * 3600;
        } else {
            i2 = 0;
        }
        if (floor >= 60) {
            i3 = floor / 60;
            floor -= i3 * 60;
        } else {
            i3 = 0;
        }
        ArrayList arrayList = new ArrayList(4);
        if (i > 0) {
            arrayList.add(new Measure(Integer.valueOf(i), MeasureUnit.DAY));
        }
        if (i2 > 0) {
            arrayList.add(new Measure(Integer.valueOf(i2), MeasureUnit.HOUR));
        }
        if (i3 > 0) {
            arrayList.add(new Measure(Integer.valueOf(i3), MeasureUnit.MINUTE));
        }
        if (z && floor > 0) {
            arrayList.add(new Measure(Integer.valueOf(floor), MeasureUnit.SECOND));
        }
        if (arrayList.size() == 0) {
            arrayList.add(new Measure(0, z ? MeasureUnit.SECOND : MeasureUnit.MINUTE));
        }
        if (z2 && arrayList.size() > 2) {
            arrayList.subList(2, arrayList.size()).clear();
        }
        Measure[] measureArr = (Measure[]) arrayList.toArray(new Measure[arrayList.size()]);
        spannableStringBuilder.append(MeasureFormat.getInstance(context.getResources().getConfiguration().locale, MeasureFormat.FormatWidth.SHORT).formatMeasures(measureArr));
        if (measureArr.length == 1 && MeasureUnit.MINUTE.equals(measureArr[0].getUnit())) {
            spannableStringBuilder.setSpan(new TtsSpan.MeasureBuilder().setNumber((long) i3).setUnit("minute").build(), 0, spannableStringBuilder.length(), 33);
        }
        return spannableStringBuilder;
    }

    public static CharSequence formatRelativeTime(Context context, double d, boolean z, RelativeDateTimeFormatter.Style style) {
        RelativeDateTimeFormatter.RelativeUnit relativeUnit;
        int i;
        int floor = (int) Math.floor(d / 1000.0d);
        if (z && floor < 120) {
            return context.getResources().getString(C1757R.string.time_unit_just_now);
        }
        if (floor < 7200) {
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.MINUTES;
            i = (floor + 30) / 60;
        } else if (floor < 172800) {
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.HOURS;
            i = (floor + 1800) / 3600;
        } else {
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.DAYS;
            i = (floor + 43200) / SECONDS_PER_DAY;
        }
        return RelativeDateTimeFormatter.getInstance(ULocale.forLocale(context.getResources().getConfiguration().locale), (NumberFormat) null, style, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE).format((double) i, RelativeDateTimeFormatter.Direction.LAST, relativeUnit);
    }

    @Deprecated
    public static CharSequence formatRelativeTime(Context context, double d, boolean z) {
        return formatRelativeTime(context, d, z, RelativeDateTimeFormatter.Style.LONG);
    }

    public static String getIcuPluralsString(Context context, int i, int i2) {
        MessageFormat messageFormat = new MessageFormat(context.getResources().getString(i2), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(i));
        return messageFormat.format(hashMap);
    }

    public static String getIcuPluralsString(Context context, Map<String, Object> map, int i) {
        return new MessageFormat(context.getResources().getString(i), Locale.getDefault()).format(map);
    }
}
