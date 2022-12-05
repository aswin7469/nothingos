package com.nothingos.systemui.doze;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
/* loaded from: classes2.dex */
public final class AODController {
    private ContentResolver mContentResolver;
    private Context mContext;
    private static final Uri URI_AOD_DISPLAY_MODE = Settings.Secure.getUriFor("aod_display_mode");
    private static final Uri URI_AOD_START_TIME = Settings.Secure.getUriFor("nt_aod_start_time");
    private static final Uri URI_AOD_END_TIME = Settings.Secure.getUriFor("nt_aod_end_time");
    private static final Uri URI_AOD_SHOW_DATE = Settings.Secure.getUriFor("aod_show_date");
    private static final Uri URI_AOD_SHOW_NOTIFICATION = Settings.Secure.getUriFor("aod_show_notification");
    private static final Uri URI_AOD_SHOW_BATTERY = Settings.Secure.getUriFor("aod_show_battery");
    private static final Uri URI_AOD_SHOW_WEATHER = Settings.Secure.getUriFor("aod_show_weather");
    private static final Uri URI_AOD_LIFT_WAKE_ENABLE = Settings.Secure.getUriFor("wake_gesture_enabled");
    private static final Uri URI_AOD_TAP_WAKE_ENABLE = Settings.Secure.getUriFor("tap_gesture");
    private String mAODStartTime = "0700";
    private String mAODEndTime = "2300";
    private int mDisplayMode = 0;
    private boolean mIsShowDate = true;
    private boolean mIsShowBattery = true;
    private boolean mIsShowNotification = true;
    private boolean mIsShowWeather = true;
    private boolean mIsLiftWakeEnable = true;
    private boolean mIsTapWakeEnable = true;
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) { // from class: com.nothingos.systemui.doze.AODController.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            boolean z2 = false;
            if (AODController.URI_AOD_DISPLAY_MODE.equals(uri)) {
                int i = Settings.Secure.getInt(AODController.this.mContentResolver, "aod_display_mode", 0);
                Log.d("AODController", "onChange: display mode = " + i + " uri = " + uri.toString());
                if (i == AODController.this.mDisplayMode) {
                    return;
                }
                AODController.this.mDisplayMode = i;
            } else if (AODController.URI_AOD_START_TIME.equals(uri)) {
                String string = Settings.Secure.getString(AODController.this.mContentResolver, "nt_aod_start_time");
                Log.d("AODController", "onChange: start time = " + string);
                if (string.equals(AODController.this.mAODStartTime)) {
                    return;
                }
                AODController.this.mAODStartTime = string;
            } else if (AODController.URI_AOD_END_TIME.equals(uri)) {
                String string2 = Settings.Secure.getString(AODController.this.mContentResolver, "nt_aod_end_time");
                Log.d("AODController", "onChange: end time = " + string2);
                if (string2.equals(AODController.this.mAODEndTime)) {
                    return;
                }
                AODController.this.mAODEndTime = string2;
            } else if (AODController.URI_AOD_SHOW_DATE.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "aod_show_date", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsShowDate == z2) {
                    return;
                }
                AODController.this.mIsShowDate = z2;
            } else if (AODController.URI_AOD_SHOW_NOTIFICATION.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "aod_show_notification", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsShowNotification == z2) {
                    return;
                }
                AODController.this.mIsShowNotification = z2;
            } else if (AODController.URI_AOD_SHOW_BATTERY.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "aod_show_battery", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsShowBattery == z2) {
                    return;
                }
                AODController.this.mIsShowBattery = z2;
            } else if (AODController.URI_AOD_SHOW_WEATHER.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "aod_show_weather", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsShowWeather == z2) {
                    return;
                }
                AODController.this.mIsShowWeather = z2;
            } else if (AODController.URI_AOD_LIFT_WAKE_ENABLE.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "wake_gesture_enabled", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsLiftWakeEnable == z2) {
                    return;
                }
                AODController.this.mIsLiftWakeEnable = z2;
            } else if (!AODController.URI_AOD_TAP_WAKE_ENABLE.equals(uri)) {
            } else {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, "tap_gesture", 1) != 0) {
                    z2 = true;
                }
                if (AODController.this.mIsTapWakeEnable == z2) {
                    return;
                }
                AODController.this.mIsTapWakeEnable = z2;
            }
        }
    };

    private void initValue() {
        boolean z = false;
        this.mDisplayMode = Settings.Secure.getInt(this.mContentResolver, "aod_display_mode", 0);
        this.mAODStartTime = Settings.Secure.getString(this.mContentResolver, "nt_aod_start_time");
        this.mAODEndTime = Settings.Secure.getString(this.mContentResolver, "nt_aod_end_time");
        this.mIsLiftWakeEnable = Settings.Secure.getInt(this.mContentResolver, "wake_gesture_enabled", 1) != 0;
        if (Settings.Secure.getInt(this.mContentResolver, "tap_gesture", 1) != 0) {
            z = true;
        }
        this.mIsTapWakeEnable = z;
        if (TextUtils.isEmpty(this.mAODStartTime)) {
            this.mAODStartTime = "0700";
        }
        if (TextUtils.isEmpty(this.mAODEndTime)) {
            this.mAODEndTime = "2300";
        }
    }

    public AODController(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        registerContentObserver();
        initValue();
    }

    private void registerContentObserver() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("aod_display_mode"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("nt_aod_start_time"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("nt_aod_end_time"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("aod_show_date"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("aod_show_battery"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("aod_show_notification"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("aod_show_weather"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("wake_gesture_enabled"), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("tap_gesture"), true, this.mContentObserver);
    }

    public boolean isCurrentInTimeScope(int i, int i2, int i3, int i4) {
        long currentTimeMillis = System.currentTimeMillis();
        Time time = new Time();
        time.set(currentTimeMillis);
        Time time2 = new Time();
        time2.set(currentTimeMillis);
        time2.hour = i;
        time2.minute = i2;
        boolean z = false;
        time2.second = 0;
        Time time3 = new Time();
        time3.set(currentTimeMillis);
        time3.hour = i3;
        time3.minute = i4;
        time3.second = 0;
        Log.d("AODController", "checkNightMode startTime = " + time2 + ", endTime = " + time3);
        if (!time2.before(time3)) {
            time2.set(time2.toMillis(true) - 86400000);
            time3.set(time3.toMillis(true) - 1000);
            if (!time.before(time2) && !time.after(time3)) {
                z = true;
            }
            Time time4 = new Time();
            time4.set(time2.toMillis(true) + 86400000);
            Log.d("AODController", "checkNightMode now = " + time + " startTime = " + time2 + " result = " + z + " end time = " + time3 + ", startTimeInThisDay = " + time4);
            if (!time.before(time4)) {
                return true;
            }
        } else {
            Log.d("AODController", "checkNightMode now = " + time);
            if (!time.before(time2) && !time.after(time3)) {
                z = true;
            }
            Log.d("AODController", "now.before(startTime) =  " + time.before(time2) + "    now.after(endTime)= " + time.after(time3) + " result = " + z);
        }
        return z;
    }

    public boolean checkNightMode() {
        if (isAllDay()) {
            return false;
        }
        return isCurrentInTimeScope(Integer.valueOf(this.mAODEndTime.substring(0, 2)).intValue(), Integer.valueOf(this.mAODEndTime.substring(2, 4)).intValue(), Integer.valueOf(this.mAODStartTime.substring(0, 2)).intValue(), Integer.valueOf(this.mAODStartTime.substring(2, 4)).intValue());
    }

    public boolean isAllDay() {
        return this.mDisplayMode == 0;
    }

    public String getAODStartTime() {
        return this.mAODStartTime;
    }

    public String getAODEndTime() {
        return this.mAODEndTime;
    }

    public boolean isShowNotification() {
        return this.mIsShowNotification;
    }

    public boolean isLiftWakeEnable() {
        return this.mIsLiftWakeEnable;
    }

    public boolean isTapWakeEnable() {
        return this.mIsTapWakeEnable;
    }

    public boolean shouldShowAODView() {
        return new AmbientDisplayConfiguration(this.mContext).alwaysOnEnabled(-2) && !checkNightMode();
    }
}
