package com.nothing.experience.utils;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import okhttp3.HttpUrl;

public class DateFormatUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static Map<String, ThreadLocal<SimpleDateFormat>> formatMaps = new HashMap();

    private static synchronized SimpleDateFormat getDateFormat(final String str, final Locale locale) {
        SimpleDateFormat simpleDateFormat;
        synchronized (DateFormatUtils.class) {
            ThreadLocal threadLocal = formatMaps.get(str);
            if (threadLocal == null) {
                threadLocal = new ThreadLocal<SimpleDateFormat>() {
                    /* access modifiers changed from: protected */
                    public SimpleDateFormat initialValue() {
                        try {
                            if (locale == null) {
                                return new SimpleDateFormat(str, Locale.getDefault());
                            }
                            return new SimpleDateFormat(str, locale);
                        } catch (Exception e) {
                            LogUtil.printStackTrace(e);
                            return null;
                        }
                    }
                };
                if (threadLocal.get() != null) {
                    formatMaps.put(str, threadLocal);
                }
            }
            simpleDateFormat = (SimpleDateFormat) threadLocal.get();
        }
        return simpleDateFormat;
    }

    public static String formatTime(long j, String str) {
        if (TextUtils.isEmpty(str)) {
            str = YYYY_MM_DD_HH_MM_SS_SSS;
        }
        SimpleDateFormat dateFormat = getDateFormat(str, Locale.getDefault());
        if (dateFormat == null) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        try {
            return dateFormat.format(Long.valueOf(j));
        } catch (IllegalArgumentException e) {
            LogUtil.printStackTrace(e);
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
    }

    public static String formatTime(long j) {
        return formatTime(j, (String) null);
    }

    public static String formatDate(Date date) {
        return formatDate(date, YYYY_MM_DD_HH_MM_SS_SSS);
    }

    public static String formatDate(Date date, String str) {
        return formatDate(date, str, Locale.getDefault());
    }

    public static String formatDate(Date date, Locale locale) {
        return formatDate(date, YYYY_MM_DD_HH_MM_SS_SSS, locale);
    }

    public static String formatDate(Date date, String str, Locale locale) {
        if (TextUtils.isEmpty(str)) {
            str = YYYY_MM_DD_HH_MM_SS_SSS;
        }
        SimpleDateFormat dateFormat = getDateFormat(str, locale);
        if (dateFormat == null) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        try {
            return dateFormat.format(date);
        } catch (IllegalArgumentException e) {
            LogUtil.printStackTrace(e);
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
    }

    public static boolean isDateValid(Date date) {
        try {
            return date.after(getDateFormat(YYYY_MM_DD_HH_MM_SS_SSS, Locale.getDefault()).parse("2020-10-15 10:24:00.000"));
        } catch (ParseException e) {
            LogUtil.printStackTrace(e);
            return false;
        }
    }

    public static boolean isDateValid(long j) {
        try {
            Date parse = getDateFormat(YYYY_MM_DD_HH_MM_SS_SSS, Locale.getDefault()).parse("2020-10-15 10:24:00.000");
            if (parse != null && parse.getTime() < j) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            LogUtil.printStackTrace(e);
            return false;
        }
    }

    public static boolean hasLower(String str) {
        if (str != null && !HttpUrl.FRAGMENT_ENCODE_SET.equals(str)) {
            if (str.contains("{") && str.contains("}")) {
                return true;
            }
            if (!str.contains("[") || !str.contains("]")) {
                return false;
            }
            return true;
        }
        return false;
    }
}
