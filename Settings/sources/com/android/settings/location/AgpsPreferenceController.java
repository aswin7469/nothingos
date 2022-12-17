package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;

public class AgpsPreferenceController extends LocationBasePreferenceController {
    private static final String KEY_ASSISTED_GPS = "assisted_gps";
    private CheckBoxPreference mAgpsPreference;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_ASSISTED_GPS;
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

    public void onLocationModeChanged(int i, boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AgpsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_agps_enabled) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mAgpsPreference = (CheckBoxPreference) preferenceScreen.findPreference(KEY_ASSISTED_GPS);
    }

    public void updateState(Preference preference) {
        CheckBoxPreference checkBoxPreference = this.mAgpsPreference;
        if (checkBoxPreference != null) {
            boolean z = false;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), "assisted_gps_enabled", 0) == 1) {
                z = true;
            }
            checkBoxPreference.setChecked(z);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY_ASSISTED_GPS.equals(preference.getKey())) {
            return false;
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "assisted_gps_enabled", this.mAgpsPreference.isChecked() ? 1 : 0);
        return true;
    }
}
