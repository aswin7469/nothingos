package com.nothing.settings.fuelgauge;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.NtFeaturesUtils;

public class BatterySleepPreferenceController extends TogglePreferenceController {
    private static final String POWER_SLEEP_TIGHT = "power_sleep_tight";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public int getAvailabilityStatus() {
        return NtFeaturesUtils.isSupport(new int[]{9}) ? 0 : 2;
    }

    public BatterySleepPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.System.getInt(this.mContext.getContentResolver(), POWER_SLEEP_TIGHT, 1) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), POWER_SLEEP_TIGHT, z ? 1 : 0);
    }
}
