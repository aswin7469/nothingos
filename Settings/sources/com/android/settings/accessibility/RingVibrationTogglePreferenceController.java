package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;

public class RingVibrationTogglePreferenceController extends VibrationTogglePreferenceController {
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

    public RingVibrationTogglePreferenceController(Context context, String str) {
        super(context, str, new RingVibrationPreferenceConfig(context));
    }
}
