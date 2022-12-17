package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.accessibility.AlarmVibrationIntensityPreferenceController;

public class AlarmVibrationTogglePreferenceController extends VibrationTogglePreferenceController {
    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AlarmVibrationTogglePreferenceController(Context context, String str) {
        super(context, str, new AlarmVibrationIntensityPreferenceController.AlarmVibrationPreferenceConfig(context));
    }
}
