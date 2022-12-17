package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.RestrictedPreference;

public class LocationServicesBluetoothScanningPreferenceController extends BasePreferenceController {
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

    public LocationServicesBluetoothScanningPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        ((RestrictedPreference) preference).checkRestrictionAndSetDisabled("no_config_location");
        refreshSummary(preference);
    }

    public CharSequence getSummary() {
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "ble_scan_always_enabled", 0) == 1) {
            z = true;
        }
        return this.mContext.getString(z ? R$string.scanning_status_text_on : R$string.scanning_status_text_off);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_location_scanning) ? 0 : 3;
    }
}
