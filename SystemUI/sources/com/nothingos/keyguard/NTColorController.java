package com.nothingos.keyguard;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.provider.Settings;
import android.util.Log;
/* loaded from: classes2.dex */
public class NTColorController {
    private ColorDisplayManager mColorDisplayManager;
    protected ContentResolver mContentResolver;
    private Context mContext;
    private boolean mIsInversionHasReset;
    private int mTemperatureDefault;
    private int savedColorTemperature;
    private int savedDaltonizer;
    private int savedInversion;
    private int savedNightDisplayMode;

    public NTColorController(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mColorDisplayManager = (ColorDisplayManager) this.mContext.getSystemService(ColorDisplayManager.class);
        this.mTemperatureDefault = this.mContext.getResources().getInteger(17694801);
        Log.i("FpColorController", "mTemperatureDefault=" + this.mTemperatureDefault);
    }

    public void resetDisplaySettingsIfNeeded() {
        int i = Settings.Secure.getInt(this.mContentResolver, "night_display_activated", 0);
        this.savedNightDisplayMode = i;
        if (i == 1) {
            Settings.Global.putInt(this.mContentResolver, "night_display_activated", 0);
            this.mColorDisplayManager.setNightDisplayActivated(false);
            Log.i("FpColorController", "setNightDisplayActivated False");
        }
        int i2 = Settings.Global.getInt(this.mContentResolver, "current_screen_color_temperature", this.mTemperatureDefault);
        this.savedColorTemperature = i2;
        int i3 = this.mTemperatureDefault;
        if (i2 != i3) {
            Settings.Global.putInt(this.mContentResolver, "current_screen_color_temperature", i3);
        }
        resetInversion();
        this.savedDaltonizer = Settings.Secure.getInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", 0);
        if (this.savedInversion == 1) {
            Settings.Secure.putInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", 0);
        }
        Log.i("FpColorController", "resetDisplaySettingsIfNeeded isNightDisplayMode=" + this.savedNightDisplayMode + ", savedColorTemperature=" + this.savedColorTemperature + ", savedInversion=" + this.savedInversion + ", savedDaltonizer=" + this.savedDaltonizer);
    }

    public void restoreDisplaySettingsIfNeeded() {
        Log.i("FpColorController", "restoreDisplaySettingsIfNeeded isNightDisplayMode=" + this.savedNightDisplayMode + ", savedColorTemperature=" + this.savedColorTemperature + ", savedInversion=" + this.savedInversion + ", savedDaltonizer=" + this.savedDaltonizer);
        int i = this.savedNightDisplayMode;
        if (i != 0) {
            Settings.Global.putInt(this.mContentResolver, "night_display_activated", i);
            this.mColorDisplayManager.setNightDisplayActivated(true);
            Log.i("FpColorController", "setNightDisplayActivated TRUE");
        }
        int i2 = this.savedColorTemperature;
        if (i2 != this.mTemperatureDefault) {
            Settings.Global.putInt(this.mContentResolver, "current_screen_color_temperature", i2);
        }
        restoreInversion();
        int i3 = this.savedDaltonizer;
        if (i3 != 0) {
            Settings.Secure.putInt(this.mContentResolver, "accessibility_display_daltonizer_enabled", i3);
        }
    }

    public void resetInversion() {
        Log.i("FpColorController", "resetInversion  mIsInversionHasReset=" + this.mIsInversionHasReset);
        synchronized (this) {
            if (this.mIsInversionHasReset) {
                return;
            }
            int i = Settings.Secure.getInt(this.mContentResolver, "accessibility_display_inversion_enabled", 0);
            this.savedInversion = i;
            if (i == 1) {
                Settings.Secure.putInt(this.mContentResolver, "accessibility_display_inversion_enabled", 0);
            }
            Log.i("FpColorController", "resetInversion  savedInversion=" + this.savedInversion);
            this.mIsInversionHasReset = true;
        }
    }

    public void restoreInversion() {
        Log.i("FpColorController", "restoreInversion  mIsInversionHasReset=" + this.mIsInversionHasReset);
        synchronized (this) {
            if (!this.mIsInversionHasReset) {
                return;
            }
            Log.i("FpColorController", "restoreInversion  savedInversion=" + this.savedInversion);
            int i = this.savedInversion;
            if (i != 0) {
                Settings.Secure.putInt(this.mContentResolver, "accessibility_display_inversion_enabled", i);
            }
            this.mIsInversionHasReset = false;
        }
    }
}
