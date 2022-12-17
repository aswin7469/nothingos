package com.android.settings.nfc;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$bool;
import com.android.settings.core.BasePreferenceController;

public class NfcDetectionPointController extends BasePreferenceController {
    private boolean mEnabled = this.mContext.getResources().getBoolean(R$bool.config_nfc_detection_point);

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

    public NfcDetectionPointController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return !this.mEnabled ? 3 : 0;
    }

    public void setConfig(boolean z) {
        this.mEnabled = z;
    }
}
