package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.dream.DreamBackend;

public class DreamComplicationPreferenceController extends TogglePreferenceController {
    private final DreamBackend mBackend;

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

    public DreamComplicationPreferenceController(Context context, String str) {
        super(context, str);
        this.mBackend = DreamBackend.getInstance(context);
    }

    public int getAvailabilityStatus() {
        return this.mBackend.getSupportedComplications().isEmpty() ? 2 : 0;
    }

    public boolean isChecked() {
        return this.mBackend.getEnabledComplications().containsAll(this.mBackend.getSupportedComplications());
    }

    public boolean setChecked(boolean z) {
        for (Integer intValue : this.mBackend.getSupportedComplications()) {
            this.mBackend.setComplicationEnabled(intValue.intValue(), z);
        }
        return true;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }
}
