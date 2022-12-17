package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class LocationIndicatorsPreferenceController extends TogglePreferenceController {
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

    public LocationIndicatorsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return DeviceConfig.getBoolean("privacy", "location_indicators_enabled", false);
    }

    public boolean setChecked(boolean z) {
        DeviceConfig.setProperty("privacy", "location_indicators_enabled", Boolean.toString(z), true);
        return true;
    }

    public int getAvailabilityStatus() {
        if (!DeviceConfig.getBoolean("privacy", "location_indicator_settings_enabled", false)) {
            return 3;
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.location")) {
            return 0;
        }
        return 3;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_location;
    }
}
