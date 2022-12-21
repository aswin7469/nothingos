package com.nothing.systemui.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.nothing.experience.AppTracking;

public class SystemUIEventUtils {
    public static final String EVENT_NAME_OS_ACTIVE = "OS_Active";
    public static final String EVENT_NAME_OS_BATTERY = "OS_Battery";
    public static final String EVENT_PROPERTY_KEY_BATTERY_SHARE_TIME = "batteryshare_time";
    public static final String EVENT_PROPERTY_KEY_UNLOCK_FAIL = "unlock_fail";
    public static final String EVENT_PROPERTY_KEY_UNLOCK_SUCCESS = "unlock_success";
    public static final String EVENT_PROPERTY_KEY_WIRELESS_CHARGING_TIME = "wrielesscharging_time";
    public static final int EVENT_PROPERTY_VALUE_FACE_FAIL = 5;
    public static final int EVENT_PROPERTY_VALUE_FACE_SUCCESS = 5;
    public static final int EVENT_PROPERTY_VALUE_FINGERPRINT_FAIL = 4;
    public static final int EVENT_PROPERTY_VALUE_FINGERPRINT_SUCCESS = 4;
    public static final int EVENT_PROPERTY_VALUE_NO_LOCK_SUCCESS = 0;
    public static final int EVENT_PROPERTY_VALUE_PASSWORD_FAIL = 2;
    public static final int EVENT_PROPERTY_VALUE_PASSWORD_SUCCESS = 2;
    public static final int EVENT_PROPERTY_VALUE_PATTERN_FAIL = 3;
    public static final int EVENT_PROPERTY_VALUE_PATTERN_SUCCESS = 3;
    public static final int EVENT_PROPERTY_VALUE_SWIPE_SUCCESS = 1;
    public static final int EVENT_PROPERTY_VALUE_SWITCH_OFF = 0;
    public static final int EVENT_PROPERTY_VALUE_SWITCH_ON = 1;
    private static final String TAG = "SystemUIMobEventUtils";
    private static AppTracking sAppTracking;

    private static void onProductEvent(Context context, String str, Bundle bundle) {
        if (sAppTracking == null) {
            sAppTracking = AppTracking.getInstance(context);
        }
        AppTracking appTracking = sAppTracking;
        if (appTracking != null) {
            appTracking.logProductEvent(str, bundle);
        }
    }

    public static void collectUnLockResults(Context context, String str, int i) {
        Log.i(TAG, "onEvent name =OS_Active,key = " + str + ", value = " + i);
        Bundle bundle = new Bundle();
        bundle.putInt(str, i);
        onProductEvent(context, EVENT_NAME_OS_ACTIVE, bundle);
    }

    public static void collectIntResults(Context context, String str, String str2, int i) {
        Log.i(TAG, "event name = " + str + ",key = " + str2 + ", value = " + i);
        Bundle bundle = new Bundle();
        bundle.putInt(str2, i);
        onProductEvent(context, str, bundle);
    }

    public static void collectWirelessChargingTime(Context context, int i) {
        collectIntResults(context, EVENT_NAME_OS_BATTERY, EVENT_PROPERTY_KEY_WIRELESS_CHARGING_TIME, i);
    }

    public static void collectBatteryShareTime(Context context, int i) {
        collectIntResults(context, EVENT_NAME_OS_BATTERY, EVENT_PROPERTY_KEY_BATTERY_SHARE_TIME, i);
    }
}
