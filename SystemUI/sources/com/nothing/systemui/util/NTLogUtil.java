package com.nothing.systemui.util;

import android.util.Log;

public class NTLogUtil {
    public static final String TAG = "SystemUI";
    private static String sSeparator = "-->";

    /* renamed from: v */
    public static void m1683v(String str, String str2) {
        Log.v(TAG, str + sSeparator + str2);
    }

    /* renamed from: d */
    public static void m1680d(String str, String str2) {
        Log.d(TAG, str + sSeparator + str2);
    }

    /* renamed from: i */
    public static void m1682i(String str, String str2) {
        Log.i(TAG, str + sSeparator + str2);
    }

    /* renamed from: w */
    public static void m1684w(String str, String str2) {
        Log.w(TAG, str + sSeparator + str2);
    }

    /* renamed from: e */
    public static void m1681e(String str, String str2) {
        Log.e(TAG, str + sSeparator + str2);
    }

    public static String getCallStack(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("----getCallStack total depth = " + i + ", print depth = " + stackTrace.length + "----\n");
        int i2 = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (i2 >= i) {
                break;
            }
            sb.append("    " + i2 + ": " + stackTraceElement.toString() + "\n");
            i2++;
        }
        return sb.toString();
    }
}
