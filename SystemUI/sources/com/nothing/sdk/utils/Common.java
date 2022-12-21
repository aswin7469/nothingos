package com.nothing.sdk.utils;

import android.content.Context;

public class Common {
    public static int APP_TARGET_SDK_VERSION = 0;
    public static final int NOTHING_SDK_VERSION = 130101;

    public static int getSDKVersion() {
        return NOTHING_SDK_VERSION;
    }

    public static boolean isSDKAvaliable(Context context) {
        return getAppTargetSDKVersion(context) >= 130101;
    }

    public static int getAppTargetSDKVersion(Context context) {
        if (APP_TARGET_SDK_VERSION == 0) {
            APP_TARGET_SDK_VERSION = context.getApplicationInfo().metaData.getInt("targetNothingSDKVersion");
        }
        return APP_TARGET_SDK_VERSION;
    }
}
