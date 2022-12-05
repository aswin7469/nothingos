package com.android.settings.accessibility;

import android.os.Vibrator;
import com.android.settings.R;
/* loaded from: classes.dex */
public class RingVibrationPreferenceFragment extends VibrationPreferenceFragment {
    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1620;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected int getPreviewVibrationAudioAttributesUsage() {
        return 6;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected String getVibrationIntensitySetting() {
        return "ring_vibration_intensity";
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment, com.android.settings.core.InstrumentedPreferenceFragment
    protected int getPreferenceScreenResId() {
        return R.xml.accessibility_ring_vibration_settings;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected String getVibrationEnabledSetting() {
        return AccessibilitySettings.isRampingRingerEnabled(getContext()) ? "apply_ramping_ringer" : "vibrate_when_ringing";
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected int getDefaultVibrationIntensity() {
        return ((Vibrator) getContext().getSystemService(Vibrator.class)).getDefaultRingVibrationIntensity();
    }
}
