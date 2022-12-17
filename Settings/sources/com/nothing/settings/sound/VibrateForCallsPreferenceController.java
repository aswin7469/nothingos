package com.nothing.settings.sound;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class VibrateForCallsPreferenceController extends BasePreferenceController {
    private static final String APPLY_RAMPING_RINGER = "apply_ramping_ringer";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f266ON = 1;
    static final String RAMPING_RINGER_ENABLED = "ramping_ringer_enabled";
    private static final String RING_VIBRATION_INTENSITY = "ring_vibration_intensity";
    private static final String VIBRATE_WHEN_RINGING = "vibrate_when_ringing";
    private int mDefaultRingVibrationIntensity;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrateForCallsPreferenceController(Context context, String str) {
        super(context, str);
        this.mDefaultRingVibrationIntensity = loadDefaultIntensity(context, 17694800);
    }

    private int loadDefaultIntensity(Context context, int i) {
        if (context != null) {
            return context.getResources().getInteger(i);
        }
        return 2;
    }

    public int getAvailabilityStatus() {
        if (!Utils.isVoiceCapable(this.mContext) || DeviceConfig.getBoolean("telephony", RAMPING_RINGER_ENABLED, false)) {
            return 3;
        }
        return 0;
    }

    public CharSequence getSummary() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, APPLY_RAMPING_RINGER, 0) == 1) {
            return this.mContext.getText(R$string.vibrate_when_ringing_option_ramping_ringer);
        }
        if (Settings.System.getInt(contentResolver, VIBRATE_WHEN_RINGING, 0) == 1) {
            return this.mContext.getText(R$string.vibrate_when_ringing_option_always_vibrate);
        }
        if (Settings.System.getInt(contentResolver, RING_VIBRATION_INTENSITY, 0) == this.mDefaultRingVibrationIntensity) {
            return this.mContext.getText(R$string.nt_vibrate_with_ringtone);
        }
        return this.mContext.getText(R$string.vibrate_when_ringing_option_never_vibrate);
    }
}
