package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class PrimaryMonoPreferenceController extends TogglePreferenceController {
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

    public PrimaryMonoPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), "master_mono", 0, -2) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putIntForUser(this.mContext.getContentResolver(), "master_mono", z ? 1 : 0, -2);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }
}
