package com.android.settings.applications.specialaccess.vrlistener;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$bool;
import com.android.settings.core.BasePreferenceController;

public class VrListenerScreenPreferenceController extends BasePreferenceController {
    private final ActivityManager mActivityManager = ((ActivityManager) this.mContext.getSystemService(ActivityManager.class));

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

    public VrListenerScreenPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(R$bool.config_show_enabled_vr_listeners) || this.mActivityManager.isLowRamDevice()) ? 3 : 0;
    }
}
