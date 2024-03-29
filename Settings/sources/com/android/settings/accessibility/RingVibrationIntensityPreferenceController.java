package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;

public class RingVibrationIntensityPreferenceController extends VibrationIntensityPreferenceController {
    public int getAvailabilityStatus() {
        return 0;
    }

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

    public RingVibrationIntensityPreferenceController(Context context, String str) {
        super(context, str, new RingVibrationPreferenceConfig(context));
    }

    protected RingVibrationIntensityPreferenceController(Context context, String str, int i) {
        super(context, str, new RingVibrationPreferenceConfig(context), i);
    }
}
