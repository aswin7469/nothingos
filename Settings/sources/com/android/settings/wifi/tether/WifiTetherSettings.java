package com.android.settings.wifi.tether;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.FeatureFlagUtils;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import com.android.settingslib.TetherUtil;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;
import java.util.ArrayList;
import java.util.List;

public class WifiTetherSettings extends RestrictedDashboardFragment implements WifiTetherBasePreferenceController.OnTetherConfigUpdateListener {
    static final String KEY_WIFI_TETHER_AUTO_OFF = "wifi_tether_auto_turn_off";
    static final String KEY_WIFI_TETHER_NETWORK_AP_BAND = "wifi_tether_network_ap_band";
    static final String KEY_WIFI_TETHER_NETWORK_NAME = "wifi_tether_network_name";
    static final String KEY_WIFI_TETHER_NETWORK_PASSWORD = "wifi_tether_network_password";
    static final String KEY_WIFI_TETHER_SECURITY = "wifi_tether_security";
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new SearchIndexProvider(R$xml.wifi_tether_settings);
    private static final IntentFilter TETHER_STATE_CHANGE_FILTER = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
    private static boolean mWasApBand6GHzSelected = false;
    private WifiTetherApBandPreferenceController mApBandPreferenceController;
    private WifiTetherPasswordPreferenceController mPasswordPreferenceController;
    /* access modifiers changed from: private */
    public boolean mRestartWifiApAfterConfigChange;
    private WifiTetherSSIDPreferenceController mSSIDPreferenceController;
    private WifiTetherSecurityPreferenceController mSecurityPreferenceController;
    private WifiTetherSwitchBarController mSwitchBarController;
    TetherChangeReceiver mTetherChangeReceiver;
    private boolean mUnavailable;
    private WifiManager mWifiManager;
    private WifiRestriction mWifiRestriction = new WifiRestriction();
    private boolean wasApBandPrefUpdated = false;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WifiTetherSettings";
    }

    public int getMetricsCategory() {
        return 1014;
    }

    public WifiTetherSettings() {
        super("no_config_tethering");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean z = true;
        setIfOnlyAvailableForAdmins(true);
        if (!isUiRestricted() && this.mWifiRestriction.isHotspotAvailable(getContext())) {
            z = false;
        }
        this.mUnavailable = z;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mTetherChangeReceiver = new TetherChangeReceiver();
        this.mSSIDPreferenceController = (WifiTetherSSIDPreferenceController) use(WifiTetherSSIDPreferenceController.class);
        this.mSecurityPreferenceController = (WifiTetherSecurityPreferenceController) use(WifiTetherSecurityPreferenceController.class);
        this.mPasswordPreferenceController = (WifiTetherPasswordPreferenceController) use(WifiTetherPasswordPreferenceController.class);
        this.mApBandPreferenceController = (WifiTetherApBandPreferenceController) use(WifiTetherApBandPreferenceController.class);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (!this.mUnavailable) {
            SettingsActivity settingsActivity = (SettingsActivity) getActivity();
            SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
            switchBar.setTitle(getContext().getString(R$string.use_wifi_hotsopt_main_switch_title));
            this.mSwitchBarController = new WifiTetherSwitchBarController(settingsActivity, switchBar);
            getSettingsLifecycle().addObserver(this.mSwitchBarController);
            switchBar.show();
        }
    }

    public void onStart() {
        super.onStart();
        if (!this.mWifiRestriction.isHotspotAvailable(getContext())) {
            getEmptyTextView().setText(R$string.not_allowed_by_ent);
            getPreferenceScreen().removeAll();
        } else if (this.mUnavailable) {
            if (!isUiRestrictedByOnlyAdmin()) {
                getEmptyTextView().setText(R$string.tethering_settings_not_available);
            }
            getPreferenceScreen().removeAll();
        } else {
            Context context = getContext();
            if (context != null) {
                context.registerReceiver(this.mTetherChangeReceiver, TETHER_STATE_CHANGE_FILTER, 2);
            }
        }
    }

    public void onStop() {
        Context context;
        super.onStop();
        if (!this.mUnavailable && (context = getContext()) != null) {
            context.unregisterReceiver(this.mTetherChangeReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.wifi_tether_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, new WifiTetherSettings$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WifiTetherSSIDPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherSecurityPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherPasswordPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherApBandPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherAutoOffPreferenceController(context, KEY_WIFI_TETHER_AUTO_OFF));
        return arrayList;
    }

    public void onTetherConfigUpdated(AbstractPreferenceController abstractPreferenceController) {
        SoftApConfiguration buildNewConfig = buildNewConfig();
        this.mPasswordPreferenceController.setSecurityType(buildNewConfig.getSecurityType());
        if (this.mWifiManager.getWifiApState() == 13) {
            Log.d("TetheringSettings", "Wifi AP config changed while enabled, stop and restart");
            this.mRestartWifiApAfterConfigChange = true;
            this.mSwitchBarController.stopTether();
        }
        this.mWifiManager.setSoftApConfiguration(buildNewConfig);
        ((WifiTetherAutoOffPreferenceController) use(WifiTetherAutoOffPreferenceController.class)).updateDisplay();
        if (this.mSecurityPreferenceController.isOweDualSapSupported()) {
            if (buildNewConfig.getSecurityType() == 4 && this.mApBandPreferenceController.getBandIndex() == 16) {
                this.mApBandPreferenceController.updatePreferenceEntries();
                this.mApBandPreferenceController.updateDisplay();
                this.wasApBandPrefUpdated = true;
            } else if (this.wasApBandPrefUpdated && buildNewConfig.getSecurityType() != 4) {
                this.mApBandPreferenceController.updatePreferenceEntries();
                this.mApBandPreferenceController.updateDisplay();
                this.wasApBandPrefUpdated = false;
            }
        }
        if ((this.mApBandPreferenceController.getBandIndex() & 4) != 0 && !mWasApBand6GHzSelected) {
            this.mSecurityPreferenceController.updateDisplay();
            mWasApBand6GHzSelected = true;
            SoftApConfiguration buildNewConfig2 = buildNewConfig();
            this.mPasswordPreferenceController.setSecurityType(buildNewConfig2.getSecurityType());
            this.mWifiManager.setSoftApConfiguration(buildNewConfig2);
        } else if ((this.mApBandPreferenceController.getBandIndex() & 4) == 0 && mWasApBand6GHzSelected) {
            this.mSecurityPreferenceController.updateDisplay();
            mWasApBand6GHzSelected = false;
        }
    }

    private SoftApConfiguration buildNewConfig() {
        SoftApConfiguration.Builder builder = new SoftApConfiguration.Builder();
        int securityType = this.mSecurityPreferenceController.getSecurityType();
        builder.setSsid(this.mSSIDPreferenceController.getSSID());
        if ((this.mApBandPreferenceController.getBandIndex() & 4) != 0 && securityType == 4) {
            securityType = 5;
        }
        if (securityType == 0 || securityType == 4 || securityType == 5) {
            builder.setPassphrase((String) null, securityType);
        } else {
            builder.setPassphrase(this.mPasswordPreferenceController.getPasswordValidated(securityType), securityType);
        }
        if (this.mApBandPreferenceController.getBandIndex() != 16) {
            builder.setBand(this.mApBandPreferenceController.getBandIndex());
        } else if (securityType == 4) {
            builder.setBand(1);
        } else {
            builder.setBands(new int[]{1, 2});
        }
        return builder.build();
    }

    /* access modifiers changed from: private */
    public void startTether() {
        this.mRestartWifiApAfterConfigChange = false;
        this.mSwitchBarController.startTether();
    }

    /* access modifiers changed from: private */
    public void updateDisplayWithNewConfig() {
        ((WifiTetherSSIDPreferenceController) use(WifiTetherSSIDPreferenceController.class)).updateDisplay();
        ((WifiTetherSecurityPreferenceController) use(WifiTetherSecurityPreferenceController.class)).updateDisplay();
        ((WifiTetherPasswordPreferenceController) use(WifiTetherPasswordPreferenceController.class)).updateDisplay();
        ((WifiTetherApBandPreferenceController) use(WifiTetherApBandPreferenceController.class)).updateDisplay();
    }

    static class SearchIndexProvider extends BaseSearchIndexProvider {
        private final WifiRestriction mWifiRestriction;

        SearchIndexProvider(int i) {
            super(i);
            this.mWifiRestriction = new WifiRestriction();
        }

        SearchIndexProvider(int i, WifiRestriction wifiRestriction) {
            super(i);
            this.mWifiRestriction = wifiRestriction;
        }

        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            if (!this.mWifiRestriction.isTetherAvailable(context) || !this.mWifiRestriction.isHotspotAvailable(context)) {
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_NAME);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_SECURITY);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_PASSWORD);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_AUTO_OFF);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_AP_BAND);
            }
            nonIndexableKeys.add("wifi_tether_settings_screen");
            return nonIndexableKeys;
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return !FeatureFlagUtils.isEnabled(context, "settings_tether_all_in_one");
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return WifiTetherSettings.buildPreferenceControllers(context, (WifiTetherBasePreferenceController.OnTetherConfigUpdateListener) null);
        }
    }

    static class WifiRestriction {
        WifiRestriction() {
        }

        public boolean isTetherAvailable(Context context) {
            if (context == null) {
                return true;
            }
            return TetherUtil.isTetherAvailable(context);
        }

        public boolean isHotspotAvailable(Context context) {
            if (context == null) {
                return true;
            }
            return WifiEnterpriseRestrictionUtils.isWifiTetheringAllowed(context);
        }
    }

    class TetherChangeReceiver extends BroadcastReceiver {
        TetherChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("WifiTetherSettings", "updating display config due to receiving broadcast action " + action);
            WifiTetherSettings.this.updateDisplayWithNewConfig();
            if (action.equals("android.net.wifi.WIFI_AP_STATE_CHANGED") && intent.getIntExtra("wifi_state", 0) == 11 && WifiTetherSettings.this.mRestartWifiApAfterConfigChange) {
                WifiTetherSettings.this.startTether();
            }
        }
    }
}
