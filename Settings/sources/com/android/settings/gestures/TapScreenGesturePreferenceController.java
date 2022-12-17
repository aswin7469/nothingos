package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;

public class TapScreenGesturePreferenceController extends GesturePreferenceController {
    private static final String PREF_KEY_VIDEO = "gesture_tap_screen_video";
    private AmbientDisplayConfiguration mAmbientConfig;
    private final int mUserId = UserHandle.myUserId();

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public String getVideoPrefKey() {
        return PREF_KEY_VIDEO;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TapScreenGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    public TapScreenGesturePreferenceController setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mAmbientConfig = ambientDisplayConfiguration;
        return this;
    }

    public int getAvailabilityStatus() {
        return !getAmbientConfig().tapSensorAvailable() ? 3 : 0;
    }

    public CharSequence getSummary() {
        return super.getSummary();
    }

    public boolean isChecked() {
        return getAmbientConfig().tapGestureEnabled(this.mUserId);
    }

    public boolean setChecked(boolean z) {
        boolean putInt = Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_tap_gesture", z ? 1 : 0);
        SystemProperties.set("persist.sys.tap_gesture", z ? "1" : "0");
        return putInt;
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (this.mAmbientConfig == null) {
            this.mAmbientConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mAmbientConfig;
    }
}
