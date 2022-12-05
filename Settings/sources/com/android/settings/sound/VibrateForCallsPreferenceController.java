package com.android.settings.sound;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes.dex */
public class VibrateForCallsPreferenceController extends BasePreferenceController {
    private static final int OFF = 0;
    private static final int ON = 1;
    static final String RAMPING_RINGER_ENABLED = "ramping_ringer_enabled";
    private int mDefaultRingVibrationIntensity;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrateForCallsPreferenceController(Context context, String str) {
        super(context, str);
        this.mDefaultRingVibrationIntensity = ((Vibrator) context.getSystemService(Vibrator.class)).getDefaultRingVibrationIntensity();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (!Utils.isVoiceCapable(this.mContext) || DeviceConfig.getBoolean("telephony", RAMPING_RINGER_ENABLED, false)) ? 3 : 0;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "apply_ramping_ringer", 0) == 1) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_ramping_ringer);
        }
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) == 1) {
            return this.mContext.getText(R.string.vibrate_when_ringing_option_always_vibrate);
        }
        if (Settings.System.getInt(this.mContext.getContentResolver(), "ring_vibration_intensity", 0) == this.mDefaultRingVibrationIntensity) {
            return this.mContext.getText(R.string.nt_vibrate_with_ringtone);
        }
        return this.mContext.getText(R.string.vibrate_when_ringing_option_never_vibrate);
    }
}
