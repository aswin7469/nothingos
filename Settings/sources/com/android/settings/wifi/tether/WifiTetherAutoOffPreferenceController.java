package com.android.settings.wifi.tether;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;

public class WifiTetherAutoOffPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private String TAG = "WifiTetherAutoOffPreferenceController";
    private SwitchPreference mPreference = null;
    private final WifiManager mWifiManager;

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

    public WifiTetherAutoOffPreferenceController(Context context, String str) {
        super(context, str);
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateDisplay() {
        SwitchPreference switchPreference = this.mPreference;
        if (switchPreference != null) {
            updateState(switchPreference);
        } else {
            Log.e(this.TAG, "updateDisplay Failed, cannot find switch preference");
        }
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) preference).setChecked(this.mWifiManager.getSoftApConfiguration().isAutoShutdownEnabled());
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return this.mWifiManager.setSoftApConfiguration(new SoftApConfiguration.Builder(this.mWifiManager.getSoftApConfiguration()).setAutoShutdownEnabled(((Boolean) obj).booleanValue()).build());
    }
}
