package com.nt.settings.utils;

import android.app.ActivityManager;
import android.text.TextUtils;
/* loaded from: classes2.dex */
public class MonkeyUtils {
    private static String[] sPkgs = {"com.byd.agingtestapplication", "com.byd.ntmonkey"};

    public static boolean isNeedIntercept(String str) {
        if (!TextUtils.isEmpty(str) && ActivityManager.isUserAMonkey()) {
            for (String str2 : sPkgs) {
                if (TextUtils.equals(str2, str)) {
                    return true;
                }
            }
        }
        return false;
    }
}
