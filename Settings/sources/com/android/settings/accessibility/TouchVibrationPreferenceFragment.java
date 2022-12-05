package com.android.settings.accessibility;

import android.os.Vibrator;
import com.android.settings.R;
/* loaded from: classes.dex */
public class TouchVibrationPreferenceFragment extends VibrationPreferenceFragment {
    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1294;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected int getPreviewVibrationAudioAttributesUsage() {
        return 13;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected String getVibrationEnabledSetting() {
        return "haptic_feedback_enabled";
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected String getVibrationIntensitySetting() {
        return "haptic_feedback_intensity";
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment, com.android.settings.core.InstrumentedPreferenceFragment
    protected int getPreferenceScreenResId() {
        return R.xml.accessibility_touch_vibration_settings;
    }

    @Override // com.android.settings.accessibility.VibrationPreferenceFragment
    protected int getDefaultVibrationIntensity() {
        return ((Vibrator) getContext().getSystemService(Vibrator.class)).getDefaultHapticFeedbackIntensity();
    }
}
