package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import com.android.settings.core.BasePreferenceController;

public class NightDisplayTopIntroPreferenceController extends BasePreferenceController {
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

    public NightDisplayTopIntroPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return ColorDisplayManager.isNightDisplayAvailable(this.mContext) ? 1 : 3;
    }
}
