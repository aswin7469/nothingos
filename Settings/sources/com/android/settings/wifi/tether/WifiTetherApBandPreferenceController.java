package com.android.settings.wifi.tether;

import android.content.Context;
import android.net.wifi.SoftApConfiguration;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import java.util.ArrayList;

public class WifiTetherApBandPreferenceController extends WifiTetherBasePreferenceController {
    private boolean m5GHzSupported;
    private boolean m6GHzSupported;
    private String[] mBandEntries;
    private int mBandIndex;
    private String[] mBandSummaries;
    private final Context mContext;
    private String mCountryCode;

    public WifiTetherApBandPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context, onTetherConfigUpdateListener);
        this.mContext = context;
        syncBandSupportAndCountryCode();
        updatePreferenceEntries();
    }

    public void updateDisplay() {
        SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
        syncBandSupportAndCountryCode();
        if (softApConfiguration == null) {
            this.mBandIndex = 1;
            Log.d("WifiTetherApBandPref", "Updating band index to BAND_2GHZ because no config");
        } else if (is5GhzBandSupported() || is6GhzBandSupported()) {
            if (softApConfiguration.getBands().length != 2) {
                this.mBandIndex = validateSelection(softApConfiguration.getBand());
            } else if (softApConfiguration.getSecurityType() == 4) {
                this.mWifiManager.setSoftApConfiguration(new SoftApConfiguration.Builder(softApConfiguration).setBand(1).build());
                this.mBandIndex = 1;
                Log.d("WifiTetherApBandPref", "Dual band not supported with OWE, updating band index to 2GHz");
            } else {
                this.mBandIndex = 16;
            }
            Log.d("WifiTetherApBandPref", "Updating band index to " + this.mBandIndex);
        } else {
            this.mWifiManager.setSoftApConfiguration(new SoftApConfiguration.Builder(softApConfiguration).setBand(1).build());
            this.mBandIndex = 1;
            Log.d("WifiTetherApBandPref", "5Ghz/6Ghz not supported, updating band index to 2GHz");
        }
        ListPreference listPreference = (ListPreference) this.mPreference;
        listPreference.setEntries((CharSequence[]) this.mBandSummaries);
        listPreference.setEntryValues(this.mBandEntries);
        if (is5GhzBandSupported() || is6GhzBandSupported()) {
            listPreference.setValue(Integer.toString(this.mBandIndex));
            listPreference.setSummary(getConfigSummary());
            return;
        }
        listPreference.setEnabled(false);
        listPreference.setSummary(R$string.wifi_ap_choose_2G);
    }

    /* access modifiers changed from: package-private */
    public String getConfigSummary() {
        int i = this.mBandIndex;
        if (i == 1) {
            return this.mBandSummaries[0];
        }
        if (i == 3 || i == 5 || i == 16) {
            return this.mBandSummaries[((ListPreference) this.mPreference).findIndexOfValue(String.valueOf(i))];
        }
        return this.mContext.getString(R$string.wifi_ap_prefer_5G);
    }

    public String getPreferenceKey() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_tether_all_in_one") ? "wifi_tether_network_ap_band_2" : "wifi_tether_network_ap_band";
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        syncBandSupportAndCountryCode();
        this.mBandIndex = validateSelection(Integer.parseInt((String) obj));
        Log.d("WifiTetherApBandPref", "Band preference changed, updating band index to " + this.mBandIndex);
        preference.setSummary((CharSequence) getConfigSummary());
        this.mListener.onTetherConfigUpdated(this);
        return true;
    }

    private int validateSelection(int i) {
        if (i == 3) {
            if (!is5GhzBandSupported()) {
                return 1;
            }
        } else if ((i & 4) != 0) {
            if (!is6GhzBandSupported()) {
                return 1;
            }
            return 5;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceEntries() {
        SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
        this.mContext.getResources();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.add(String.valueOf(1));
        arrayList2.add(this.mContext.getString(R$string.wifi_ap_choose_2G));
        if (is5GhzBandSupported()) {
            arrayList.add(String.valueOf(3));
            arrayList2.add(this.mContext.getString(R$string.wifi_ap_prefer_5G));
        }
        if (is6GhzBandSupported()) {
            arrayList.add(String.valueOf(5));
            arrayList2.add(this.mContext.getString(R$string.wifi_ap_prefer_6G));
        }
        if (is5GhzBandSupported() && ((this.mWifiManager.isBridgedApConcurrencySupported() || isVendorLegacyDualBandSupported()) && softApConfiguration != null && softApConfiguration.getSecurityType() != 4 && this.mWifiManager.isConcurrentBandSupported())) {
            arrayList.add(String.valueOf(16));
            arrayList2.add(this.mContext.getString(R$string.wifi_ap_choose_vendor_dual_band));
        }
        this.mBandEntries = (String[]) arrayList.toArray(new String[arrayList.size()]);
        this.mBandSummaries = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
    }

    private void syncBandSupportAndCountryCode() {
        this.m5GHzSupported = this.mWifiManager.is5GHzBandSupported();
        this.m6GHzSupported = false;
        this.mCountryCode = this.mWifiManager.getCountryCode();
        Log.d("WifiTetherApBandPref", "syncBandSupportAndCountryCode m5GHzSupported:" + this.m5GHzSupported + ",mCountryCode:" + this.mCountryCode);
    }

    private boolean is5GhzBandSupported() {
        return this.m5GHzSupported && this.mCountryCode != null;
    }

    private boolean is6GhzBandSupported() {
        return this.m6GHzSupported && this.mCountryCode != null;
    }

    public int getBandIndex() {
        return this.mBandIndex;
    }

    private boolean isVendorLegacyDualBandSupported() {
        return this.mContext.getResources().getBoolean(17891835);
    }
}
