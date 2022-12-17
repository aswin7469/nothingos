package com.android.settings.wifi.tether;

import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.text.BidiFormatter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.wifi.tether.WifiTetherSoftApManager;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;
import com.android.settingslib.wifi.WifiUtils;
import java.util.List;

public class WifiTetherPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop {
    private boolean mIsWifiTetherable;
    private boolean mIsWifiTetheringAllow;
    Preference mPreference;
    /* access modifiers changed from: private */
    public int mSoftApState;
    private WifiManager mWifiManager;
    WifiTetherSoftApManager mWifiTetherSoftApManager;

    public String getPreferenceKey() {
        return "wifi_tether";
    }

    public WifiTetherPreferenceController(Context context, Lifecycle lifecycle) {
        this(context, lifecycle, (WifiManager) context.getSystemService(WifiManager.class), (TetheringManager) context.getSystemService(TetheringManager.class), true, WifiEnterpriseRestrictionUtils.isWifiTetheringAllowed(context));
    }

    WifiTetherPreferenceController(Context context, Lifecycle lifecycle, WifiManager wifiManager, TetheringManager tetheringManager, boolean z, boolean z2) {
        super(context);
        String[] tetherableWifiRegexs = tetheringManager.getTetherableWifiRegexs();
        if (!(tetherableWifiRegexs == null || tetherableWifiRegexs.length == 0)) {
            this.mIsWifiTetherable = true;
        }
        this.mIsWifiTetheringAllow = z2;
        if (z2) {
            this.mWifiManager = wifiManager;
            if (lifecycle != null) {
                lifecycle.addObserver(this);
            }
            if (z) {
                initWifiTetherSoftApManager();
            }
        }
    }

    public boolean isAvailable() {
        return this.mIsWifiTetherable && !Utils.isMonkeyRunning();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("wifi_tether");
        this.mPreference = findPreference;
        if (findPreference != null && !this.mIsWifiTetheringAllow && findPreference.isEnabled()) {
            this.mPreference.setEnabled(false);
            this.mPreference.setSummary(R$string.not_allowed_by_ent);
        }
    }

    public void onStart() {
        WifiTetherSoftApManager wifiTetherSoftApManager;
        if (this.mPreference != null && (wifiTetherSoftApManager = this.mWifiTetherSoftApManager) != null) {
            wifiTetherSoftApManager.registerSoftApCallback();
        }
    }

    public void onStop() {
        WifiTetherSoftApManager wifiTetherSoftApManager;
        if (this.mPreference != null && (wifiTetherSoftApManager = this.mWifiTetherSoftApManager) != null) {
            wifiTetherSoftApManager.unRegisterSoftApCallback();
        }
    }

    /* access modifiers changed from: package-private */
    public void initWifiTetherSoftApManager() {
        this.mWifiTetherSoftApManager = new WifiTetherSoftApManager(this.mWifiManager, new WifiTetherSoftApManager.WifiTetherSoftApCallback() {
            public void onStateChanged(int i, int i2) {
                WifiTetherPreferenceController.this.mSoftApState = i;
                WifiTetherPreferenceController.this.handleWifiApStateChanged(i, i2);
            }

            public void onConnectedClientsChanged(List<WifiClient> list) {
                WifiTetherPreferenceController wifiTetherPreferenceController = WifiTetherPreferenceController.this;
                if (wifiTetherPreferenceController.mPreference != null && wifiTetherPreferenceController.mSoftApState == 13) {
                    WifiTetherPreferenceController wifiTetherPreferenceController2 = WifiTetherPreferenceController.this;
                    wifiTetherPreferenceController2.mPreference.setSummary((CharSequence) WifiUtils.getWifiTetherSummaryForConnectedDevices(wifiTetherPreferenceController2.mContext, list.size()));
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void handleWifiApStateChanged(int i, int i2) {
        switch (i) {
            case 10:
                this.mPreference.setSummary(R$string.wifi_tether_stopping);
                return;
            case 11:
                this.mPreference.setSummary(R$string.wifi_hotspot_off_subtext);
                return;
            case 12:
                this.mPreference.setSummary(R$string.wifi_tether_starting);
                return;
            case 13:
                updateConfigSummary(this.mWifiManager.getSoftApConfiguration());
                return;
            default:
                if (i2 == 1) {
                    this.mPreference.setSummary(R$string.wifi_sap_no_channel_error);
                    return;
                } else {
                    this.mPreference.setSummary(R$string.wifi_error);
                    return;
                }
        }
    }

    private void updateConfigSummary(SoftApConfiguration softApConfiguration) {
        if (softApConfiguration != null) {
            this.mPreference.setSummary((CharSequence) this.mContext.getString(R$string.wifi_tether_enabled_subtext, new Object[]{BidiFormatter.getInstance().unicodeWrap(softApConfiguration.getSsid())}));
        }
    }
}
