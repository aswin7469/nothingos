package com.nothing.systemui.biometrics;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import android.provider.Settings;
import com.android.systemui.dagger.SysUISingleton;
import com.nothing.systemui.util.NTLogUtil;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.inject.Inject;

@SysUISingleton
public class NTColorController {
    private static final String COLOR_TEMPERATURE = "current_screen_color_temperature";
    public static final int DISPLAY_CLOSE = 1;
    public static final int DISPLAY_NOT_SAVE = -1;
    public static final int DISPLAY_OPEN = 0;
    public static final String KEYGUARD_QS_EYE_PROTECT_STATUS = "keyguard_qs_eye_protect_status";
    private static final String TAG = "FpColorController";
    /* access modifiers changed from: private */
    public final ColorDisplayManager mColorDisplayManager;
    protected ContentResolver mContentResolver;
    private Context mContext;
    private boolean mIsColorControlled = false;
    private boolean mIsDaltonizerReset = false;
    private boolean mIsInversionHasReset;
    private boolean mIsNightDisplayReset = false;
    private final int mTemperatureDefault;
    private final BroadcastReceiver mTimeChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (NTColorController.this.mColorDisplayManager.getNightDisplayAutoMode() != 0 && NTColorController.this.isNightModeCustomActive()) {
                NTColorController.this.resetNightMode();
            }
        }
    };
    private int savedColorTemperature = -1;
    private int savedInversion;

    @Inject
    public NTColorController(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mColorDisplayManager = (ColorDisplayManager) this.mContext.getSystemService(ColorDisplayManager.class);
        int integer = this.mContext.getResources().getInteger(17694809);
        this.mTemperatureDefault = integer;
        NTLogUtil.m1682i(TAG, "mTemperatureDefault=" + integer);
    }

    public boolean isColorControlled() {
        NTLogUtil.m1682i(TAG, "isColorControlled=" + this.mIsColorControlled);
        return this.mIsColorControlled;
    }

    public void resetDisplaySettingsIfNeeded() {
        this.mIsColorControlled = true;
        resetNightMode();
        resetColorTemp();
        resetInversion();
        resetDaltonizer();
    }

    public void restoreOnDestroy() {
        this.mIsColorControlled = false;
        restoreColorTemp();
        restoreDaltonizer();
    }

    public void restoreDisplaySettingsIfNeeded() {
        NTLogUtil.m1682i(TAG, "restoreDisplaySettingsIfNeeded getNightDisplayAutoMode===" + this.mColorDisplayManager.getNightDisplayAutoMode());
        this.mIsColorControlled = false;
        restoreNightMode();
        restoreColorTemp();
        restoreInversion();
        restoreDaltonizer();
    }

    private void resetColorTemp() {
        int i = Settings.Global.getInt(this.mContentResolver, COLOR_TEMPERATURE, this.mTemperatureDefault);
        int i2 = this.mTemperatureDefault;
        if (i != i2) {
            this.savedColorTemperature = i;
            Settings.Global.putInt(this.mContentResolver, COLOR_TEMPERATURE, i2);
        }
    }

    private void restoreColorTemp() {
        int i = this.savedColorTemperature;
        if (!(i == this.mTemperatureDefault || i == -1)) {
            Settings.Global.putInt(this.mContentResolver, COLOR_TEMPERATURE, i);
        }
        this.savedColorTemperature = -1;
    }

    private void resetDaltonizer() {
        if (!this.mIsDaltonizerReset && Settings.Secure.getInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", 0) == 1) {
            Settings.Secure.putInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", 0);
            this.mIsDaltonizerReset = true;
        }
    }

    private void restoreDaltonizer() {
        if (this.mIsDaltonizerReset) {
            Settings.Secure.putInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", 1);
            this.mIsDaltonizerReset = false;
        }
    }

    public void resetInversion() {
        NTLogUtil.m1682i(TAG, "resetInversion  mIsInversionHasReset=" + this.mIsInversionHasReset);
        synchronized (this) {
            if (!this.mIsInversionHasReset) {
                int i = Settings.Secure.getInt(this.mContentResolver, "accessibility_display_inversion_enabled", 0);
                this.savedInversion = i;
                if (i == 1) {
                    Settings.Secure.putInt(this.mContentResolver, "accessibility_display_inversion_enabled", 0);
                }
                NTLogUtil.m1682i(TAG, "resetInversion  savedInversion=" + this.savedInversion);
                this.mIsInversionHasReset = true;
            }
        }
    }

    public void restoreInversion() {
        NTLogUtil.m1682i(TAG, "restoreInversion  mIsInversionHasReset=" + this.mIsInversionHasReset);
        synchronized (this) {
            if (this.mIsInversionHasReset) {
                NTLogUtil.m1682i(TAG, "restoreInversion  savedInversion=" + this.savedInversion);
                int i = this.savedInversion;
                if (i != 0) {
                    Settings.Secure.putInt(this.mContentResolver, "accessibility_display_inversion_enabled", i);
                }
                this.mIsInversionHasReset = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void resetNightMode() {
        int i = Settings.Secure.getInt(this.mContentResolver, "night_display_activated", 0);
        NTLogUtil.m1682i(TAG, "resetNightMode settings: " + i);
        if (i == 1) {
            this.mColorDisplayManager.setNightDisplayActivatedImmediately(false);
            this.mIsNightDisplayReset = true;
        }
    }

    private void restoreNightMode() {
        NTLogUtil.m1682i(TAG, "saveStatus:" + getKeyguardQsEyeStatus() + " mIsNightDisplayReset: " + this.mIsNightDisplayReset);
        boolean z = true;
        if (getKeyguardQsEyeStatus() != -1) {
            ColorDisplayManager colorDisplayManager = this.mColorDisplayManager;
            if (getKeyguardQsEyeStatus() != 0) {
                z = false;
            }
            colorDisplayManager.setNightDisplayActivated(z);
            saveKeyguardQsEyeStatus(-1);
        } else if (this.mIsNightDisplayReset) {
            this.mColorDisplayManager.setNightDisplayActivatedImmediately(true);
            this.mIsNightDisplayReset = false;
        }
    }

    /* access modifiers changed from: private */
    public boolean isNightModeCustomActive() {
        LocalTime nightDisplayCustomEndTime = this.mColorDisplayManager.getNightDisplayCustomEndTime();
        LocalTime nightDisplayCustomStartTime = this.mColorDisplayManager.getNightDisplayCustomStartTime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTimeAfter = getDateTimeAfter(nightDisplayCustomEndTime, getDateTimeBefore(nightDisplayCustomStartTime, now));
        NTLogUtil.m1682i(TAG, "isNightModeCustomActive startTime=" + nightDisplayCustomStartTime + ", endTime=" + nightDisplayCustomEndTime);
        return now.isBefore(dateTimeAfter);
    }

    public void registerTimeChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        this.mContext.registerReceiver(this.mTimeChangedReceiver, intentFilter);
    }

    public void unRegisterReceiver() {
        this.mContext.unregisterReceiver(this.mTimeChangedReceiver);
    }

    private LocalDateTime getDateTimeAfter(LocalTime localTime, LocalDateTime localDateTime) {
        LocalDateTime of = LocalDateTime.m913of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        return of.isBefore(localDateTime) ? of.plusDays(1) : of;
    }

    private LocalDateTime getDateTimeBefore(LocalTime localTime, LocalDateTime localDateTime) {
        LocalDateTime of = LocalDateTime.m913of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        return of.isAfter(localDateTime) ? of.minusDays(1) : of;
    }

    public void saveKeyguardQsEyeStatus(int i) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), KEYGUARD_QS_EYE_PROTECT_STATUS, i);
    }

    public int getKeyguardQsEyeStatus() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), KEYGUARD_QS_EYE_PROTECT_STATUS, -1);
    }
}
