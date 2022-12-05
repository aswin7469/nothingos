package com.nt.ledsettings.manager;

import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.NtSettings;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
/* loaded from: classes4.dex */
public class BedTimeTaskManager implements AlarmManager.OnAlarmListener {
    private static final String ACTION_LED_BED_TIME_CHANGE = "com.nt.ledsettings.ACTION_LED_BED_TIME_CHANGE";
    private static final String EXTRA_OUT_BED_TIME = "outBedtime";
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final String TAG = "BedTimeTaskManager";
    private static BedTimeTaskManager stInstance;
    private AlarmManager mAlarmManager;
    private ContentObserver mContentObserver;
    private ContentResolver mContentResolver;
    private Context mContext;
    private int mOutBedtime = -1;

    private BedTimeTaskManager() {
    }

    private BedTimeTaskManager(Context context) {
        this.mContext = context;
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        initAlarm();
    }

    public static synchronized BedTimeTaskManager getInstance(Context context) {
        BedTimeTaskManager bedTimeTaskManager;
        synchronized (BedTimeTaskManager.class) {
            if (stInstance == null) {
                stInstance = new BedTimeTaskManager(context);
            }
            bedTimeTaskManager = stInstance;
        }
        return bedTimeTaskManager;
    }

    private void initAlarm() {
        this.mContentResolver = this.mContext.getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.nt.ledsettings.manager.BedTimeTaskManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                BedTimeTaskManager.this.cancel();
                if (BedTimeTaskManager.this.isBedTimeSwitchOpened()) {
                    BedTimeTaskManager.this.updateActivated(uri);
                    if (BedTimeTaskManager.this.canChangeSwitchUri(uri)) {
                        BedTimeTaskManager.this.isActivated();
                    }
                }
            }
        };
        registerLedSwitchContentDataChange();
        if (isBedTimeSwitchOpened()) {
            initSwitchState();
        }
    }

    public void initSwitchState() {
        isActivated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canChangeSwitchUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        return TextUtils.equals(uri.toString(), Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_START_TIME).toString()) || TextUtils.equals(uri.toString(), Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_END_TIME).toString()) || TextUtils.equals(uri.toString(), Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_WEEK).toString()) || TextUtils.equals(uri.toString(), Settings.Global.getUriFor(NtSettings.Global.LED_EFFECT_SCHEDULE_TIME).toString());
    }

    private void registerLedSwitchContentDataChange() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_START_TIME), true, this.mContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_END_TIME), true, this.mContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(NtSettings.Global.LED_BED_TIME_CUSTOM_WEEK), true, this.mContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(NtSettings.Global.LED_IN_BED_TIME), true, this.mContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(NtSettings.Global.LED_EFFECT_SCHEDULE_TIME), true, this.mContentObserver, -1);
    }

    public void checkNextAlarm() {
        long millis = getNextMillis();
        Log.d(TAG, "checkNextAlarm millis " + millis);
        if (millis == -1) {
            return;
        }
        this.mAlarmManager.setExact(0, millis, TAG, this, null);
    }

    static LocalDateTime getDateTimeAfter(LocalTime localTime, LocalDateTime compareTime) {
        LocalDateTime ldt = LocalDateTime.of(compareTime.getYear(), compareTime.getMonth(), compareTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        return ldt.isBefore(compareTime) ? ldt.plusDays(1L) : ldt;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0055, code lost:
        if (r5 != false) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long getNextMillis() {
        LocalDateTime ldtEndTime;
        if (!isBedTimeSwitchOpened()) {
            checkStateChange2Send(true);
            return -1L;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalTime startTime = getStartTime();
        LocalTime endTime = getEndTime();
        LocalDateTime ldtStartTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
        LocalDateTime ldtEndTime2 = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), endTime.getHour(), endTime.getMinute());
        if (!ldtEndTime2.isBefore(ldtStartTime)) {
            boolean isEqual = ldtEndTime2.isEqual(ldtStartTime);
            ldtEndTime = ldtEndTime2;
        }
        boolean isBefore = now.isBefore(ldtEndTime2);
        ldtEndTime = ldtEndTime2;
        if (!isBefore) {
            ldtEndTime = ldtEndTime2.plusDays(1L);
        }
        if (ldtStartTime.isBefore(ldtEndTime)) {
            if (now.isBefore(ldtStartTime)) {
                return ldtStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            if (now.isBefore(ldtEndTime)) {
                return ldtEndTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
        } else if (now.isBefore(ldtEndTime)) {
            return ldtEndTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            if (now.isBefore(ldtStartTime)) {
                return ldtStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
        }
        return ldtStartTime.plusDays(1L).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isActivated() {
        int day;
        LocalDateTime now = LocalDateTime.now();
        LocalTime startTime = getStartTime();
        LocalTime endTime = getEndTime();
        long startTimeTamp = (startTime.getHour() * 60) + startTime.getMinute();
        long endTimeTamp = (endTime.getHour() * 60) + endTime.getMinute();
        long nowTimeTamp = (now.getHour() * 60) + now.getMinute();
        boolean inBedTime = false;
        if (startTimeTamp >= endTimeTamp) {
            if (nowTimeTamp >= startTimeTamp) {
                day = now.getDayOfWeek().getValue();
                inBedTime = true;
            } else {
                day = now.getDayOfWeek().getValue() - 1;
                if (nowTimeTamp < endTimeTamp) {
                    inBedTime = true;
                }
            }
        } else {
            day = now.getDayOfWeek().getValue();
            if (nowTimeTamp >= startTimeTamp && nowTimeTamp < endTimeTamp) {
                inBedTime = true;
            }
        }
        boolean isSelectedInWeek = isSelectedInWeek(day);
        if (isSelectedInWeek) {
            setLedSwitchEnable(!inBedTime);
        }
        Log.d(TAG, "day " + day + " isSelectedInWeek " + isSelectedInWeek + " inBedTime " + inBedTime);
        return !inBedTime;
    }

    private void sendStateChangeNotification(boolean enabled) {
        Intent intent = new Intent(ACTION_LED_BED_TIME_CHANGE);
        intent.putExtra(EXTRA_OUT_BED_TIME, enabled);
        this.mContext.sendStickyBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBedTimeSwitchOpened() {
        return Settings.Global.getInt(this.mContentResolver, NtSettings.Global.LED_EFFECT_SCHEDULE_TIME, 1) == 1;
    }

    private void setLedSwitchEnable(boolean enable) {
        Settings.Global.putInt(this.mContentResolver, NtSettings.Global.LED_EFFECT_ENABLED, enable ? 1 : 0);
    }

    private int[] str2Array(String str) {
        int[] result = new int[2];
        if (str == null || str.length() == 0) {
            return result;
        }
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }

    private LocalTime getStartTime() {
        int[] time = str2Array(Settings.Global.getString(this.mContentResolver, NtSettings.Global.LED_BED_TIME_CUSTOM_START_TIME));
        return LocalTime.of(time[0], time[1]);
    }

    private LocalTime getEndTime() {
        int[] time = str2Array(Settings.Global.getString(this.mContentResolver, NtSettings.Global.LED_BED_TIME_CUSTOM_END_TIME));
        return LocalTime.of(time[0], time[1]);
    }

    private int[] getSelectedWeek() {
        return str2WeekArray(Settings.Global.getString(this.mContentResolver, NtSettings.Global.LED_BED_TIME_CUSTOM_WEEK));
    }

    private boolean isSelectedInWeek(int dayOfWeek) {
        int[] weeks = getSelectedWeek();
        if (dayOfWeek > weeks.length) {
            return false;
        }
        if (dayOfWeek == 0) {
            dayOfWeek = 1;
        }
        return weeks[dayOfWeek + (-1)] == 1;
    }

    private int[] str2WeekArray(String str) {
        int[] result = new int[7];
        if (str == null || str.length() == 0) {
            return result;
        }
        char[] split = str.toCharArray();
        for (int i = 0; i < split.length; i++) {
            result[i] = Integer.parseInt(String.valueOf(split[i]));
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancel() {
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager == null) {
            return;
        }
        alarmManager.cancel(this);
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public void onAlarm() {
        checkNextAlarm();
        boolean isActivated = isActivated();
        Log.d(TAG, "onAlarm isActivated " + isActivated);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivated(Uri uri) {
        checkNextAlarm();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void checkStateChange2Send(boolean isActivated) {
        int i = this.mOutBedtime;
        if (i < 0) {
            sendStateChangeNotification(isActivated);
            this.mOutBedtime = isActivated;
        } else if (i != 1 || isActivated == 0) {
            sendStateChangeNotification(isActivated);
            this.mOutBedtime = isActivated ? 1 : 0;
        }
    }
}
