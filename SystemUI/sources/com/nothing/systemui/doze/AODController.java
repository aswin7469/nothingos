package com.nothing.systemui.doze;

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
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AODController {
    public static final int AOD_DISPLAY_MODE_ALL_DAY = 0;
    public static final int AOD_DISPLAY_MODE_SCHEDULE = 1;
    public static final String AOD_SLEEP_MODE_END_TIME = "0700";
    public static final String AOD_SLEEP_MODE_START_TIME = "2300";
    public static final String KEY_AOD_DISPLAY_MODE = "aod_display_mode";
    public static final String KEY_AOD_END_TIME = "nt_aod_end_time";
    public static final String KEY_AOD_LIFT_WAKE_ENABLE = "wake_gesture_enabled";
    public static final String KEY_AOD_SHOW_BATTERY = "aod_show_battery";
    public static final String KEY_AOD_SHOW_DATE = "aod_show_date";
    public static final String KEY_AOD_SHOW_NOTIFICATION = "aod_show_notification";
    public static final String KEY_AOD_SHOW_WEATHER = "aod_show_weather";
    public static final String KEY_AOD_START_TIME = "nt_aod_start_time";
    public static final String KEY_AOD_TAP_WAKE_ENABLE = "tap_gesture";
    private static final String TAG = "AODController";
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_DISPLAY_MODE = Settings.Secure.getUriFor(KEY_AOD_DISPLAY_MODE);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_END_TIME = Settings.Secure.getUriFor(KEY_AOD_END_TIME);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_LIFT_WAKE_ENABLE = Settings.Secure.getUriFor(KEY_AOD_LIFT_WAKE_ENABLE);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_SHOW_BATTERY = Settings.Secure.getUriFor(KEY_AOD_SHOW_BATTERY);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_SHOW_DATE = Settings.Secure.getUriFor(KEY_AOD_SHOW_DATE);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_SHOW_NOTIFICATION = Settings.Secure.getUriFor(KEY_AOD_SHOW_NOTIFICATION);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_SHOW_WEATHER = Settings.Secure.getUriFor(KEY_AOD_SHOW_WEATHER);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_START_TIME = Settings.Secure.getUriFor(KEY_AOD_START_TIME);
    /* access modifiers changed from: private */
    public static final Uri URI_AOD_TAP_WAKE_ENABLE = Settings.Secure.getUriFor(KEY_AOD_TAP_WAKE_ENABLE);
    /* access modifiers changed from: private */
    public String mAODEndTime = AOD_SLEEP_MODE_START_TIME;
    /* access modifiers changed from: private */
    public String mAODStartTime = AOD_SLEEP_MODE_END_TIME;
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            boolean z2 = true;
            if (AODController.URI_AOD_DISPLAY_MODE.equals(uri)) {
                int i = Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_DISPLAY_MODE, 1);
                Log.d(AODController.TAG, "onChange: display mode = " + i + " uri = " + uri.toString());
                if (i != AODController.this.mDisplayMode) {
                    int unused = AODController.this.mDisplayMode = i;
                }
            } else if (AODController.URI_AOD_START_TIME.equals(uri)) {
                String string = Settings.Secure.getString(AODController.this.mContentResolver, AODController.KEY_AOD_START_TIME);
                Log.d(AODController.TAG, "onChange: start time = " + string);
                if (!string.equals(AODController.this.mAODStartTime)) {
                    String unused2 = AODController.this.mAODStartTime = string;
                }
            } else if (AODController.URI_AOD_END_TIME.equals(uri)) {
                String string2 = Settings.Secure.getString(AODController.this.mContentResolver, AODController.KEY_AOD_END_TIME);
                Log.d(AODController.TAG, "onChange: end time = " + string2);
                if (!string2.equals(AODController.this.mAODEndTime)) {
                    String unused3 = AODController.this.mAODEndTime = string2;
                }
            } else if (AODController.URI_AOD_SHOW_DATE.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_SHOW_DATE, 1) == 0) {
                    z2 = false;
                }
                if (AODController.this.mIsShowDate != z2) {
                    boolean unused4 = AODController.this.mIsShowDate = z2;
                }
            } else if (AODController.URI_AOD_SHOW_NOTIFICATION.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_SHOW_NOTIFICATION, 1) == 0) {
                    z2 = false;
                }
                if (AODController.this.mIsShowNotification != z2) {
                    boolean unused5 = AODController.this.mIsShowNotification = z2;
                }
            } else if (AODController.URI_AOD_SHOW_BATTERY.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_SHOW_BATTERY, 1) == 0) {
                    z2 = false;
                }
                if (AODController.this.mIsShowBattery != z2) {
                    boolean unused6 = AODController.this.mIsShowBattery = z2;
                }
            } else if (AODController.URI_AOD_SHOW_WEATHER.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_SHOW_WEATHER, 1) == 0) {
                    z2 = false;
                }
                if (AODController.this.mIsShowWeather != z2) {
                    boolean unused7 = AODController.this.mIsShowWeather = z2;
                }
            } else if (AODController.URI_AOD_LIFT_WAKE_ENABLE.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_LIFT_WAKE_ENABLE, 1) == 0) {
                    z2 = false;
                }
                Log.d(AODController.TAG, "onChange: liftWakeEnable = " + z2 + " mIsLiftWakeEnable = " + AODController.this.mIsLiftWakeEnable);
                if (AODController.this.mIsLiftWakeEnable != z2) {
                    boolean unused8 = AODController.this.mIsLiftWakeEnable = z2;
                }
            } else if (AODController.URI_AOD_TAP_WAKE_ENABLE.equals(uri)) {
                if (Settings.Secure.getInt(AODController.this.mContentResolver, AODController.KEY_AOD_TAP_WAKE_ENABLE, 1) == 0) {
                    z2 = false;
                }
                if (AODController.this.mIsTapWakeEnable != z2) {
                    boolean unused9 = AODController.this.mIsTapWakeEnable = z2;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mDisplayMode = 1;
    /* access modifiers changed from: private */
    public boolean mIsLiftWakeEnable = true;
    /* access modifiers changed from: private */
    public boolean mIsShowBattery = true;
    /* access modifiers changed from: private */
    public boolean mIsShowDate = true;
    /* access modifiers changed from: private */
    public boolean mIsShowNotification = true;
    /* access modifiers changed from: private */
    public boolean mIsShowWeather = true;
    /* access modifiers changed from: private */
    public boolean mIsTapWakeEnable = true;

    private void initValue() {
        boolean z = true;
        this.mDisplayMode = Settings.Secure.getInt(this.mContentResolver, KEY_AOD_DISPLAY_MODE, 1);
        this.mAODStartTime = Settings.Secure.getString(this.mContentResolver, KEY_AOD_START_TIME);
        this.mAODEndTime = Settings.Secure.getString(this.mContentResolver, KEY_AOD_END_TIME);
        this.mIsLiftWakeEnable = Settings.Secure.getInt(this.mContentResolver, KEY_AOD_LIFT_WAKE_ENABLE, 1) != 0;
        if (Settings.Secure.getInt(this.mContentResolver, KEY_AOD_TAP_WAKE_ENABLE, 1) == 0) {
            z = false;
        }
        this.mIsTapWakeEnable = z;
        if (TextUtils.isEmpty(this.mAODStartTime)) {
            this.mAODStartTime = AOD_SLEEP_MODE_END_TIME;
        }
        if (TextUtils.isEmpty(this.mAODEndTime)) {
            this.mAODEndTime = AOD_SLEEP_MODE_START_TIME;
        }
    }

    @Inject
    public AODController(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        registerContentObserver();
        initValue();
    }

    private void registerContentObserver() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_DISPLAY_MODE), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_START_TIME), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_END_TIME), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_SHOW_DATE), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_SHOW_BATTERY), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_SHOW_NOTIFICATION), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_SHOW_WEATHER), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_LIFT_WAKE_ENABLE), true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(KEY_AOD_TAP_WAKE_ENABLE), true, this.mContentObserver);
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
        Log.d(TAG, "checkNightMode startTime = " + time2 + ", endTime = " + time3);
        if (!time2.before(time3)) {
            time2.set(time2.toMillis(true) - 86400000);
            time3.set(time3.toMillis(true) - 1000);
            if (!time.before(time2) && !time.after(time3)) {
                z = true;
            }
            Time time4 = new Time();
            time4.set(time2.toMillis(true) + 86400000);
            Log.d(TAG, "checkNightMode now = " + time + " startTime = " + time2 + " result = " + z + " end time = " + time3 + ", startTimeInThisDay = " + time4);
            if (!time.before(time4)) {
                return true;
            }
        } else {
            Log.d(TAG, "checkNightMode now = " + time);
            if (!time.before(time2) && !time.after(time3)) {
                z = true;
            }
            Log.d(TAG, "now.before(startTime) =  " + time.before(time2) + " now.after(endTime)= " + time.after(time3) + " result = " + z);
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

    public boolean isSchedule() {
        return this.mDisplayMode == 1;
    }

    public String getAODStartTime() {
        return this.mAODStartTime;
    }

    public String getAODEndTime() {
        return this.mAODEndTime;
    }

    public boolean isShowDate() {
        return this.mIsShowDate;
    }

    public boolean isShowNotification() {
        return this.mIsShowNotification;
    }

    public boolean isShowBattery() {
        return this.mIsShowBattery;
    }

    public boolean isShowWeather() {
        return this.mIsShowWeather;
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
