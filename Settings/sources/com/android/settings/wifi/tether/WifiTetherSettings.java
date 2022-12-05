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
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import com.android.settingslib.TetherUtil;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class WifiTetherSettings extends RestrictedDashboardFragment implements WifiTetherBasePreferenceController.OnTetherConfigUpdateListener {
    static final String KEY_WIFI_TETHER_AUTO_OFF = "wifi_tether_auto_turn_off";
    static final String KEY_WIFI_TETHER_NETWORK_AP_BAND = "wifi_tether_network_ap_band";
    static final String KEY_WIFI_TETHER_NETWORK_NAME = "wifi_tether_network_name";
    static final String KEY_WIFI_TETHER_NETWORK_PASSWORD = "wifi_tether_network_password";
    private static final IntentFilter TETHER_STATE_CHANGE_FILTER;
    private WifiTetherApBandPreferenceController mApBandPreferenceController;
    private WifiTetherPasswordPreferenceController mPasswordPreferenceController;
    private boolean mRestartWifiApAfterConfigChange;
    private WifiTetherSSIDPreferenceController mSSIDPreferenceController;
    private WifiTetherSecurityPreferenceController mSecurityPreferenceController;
    private WifiTetherSwitchBarController mSwitchBarController;
    TetherChangeReceiver mTetherChangeReceiver;
    private boolean mUnavailable;
    private WifiManager mWifiManager;
    private boolean wasApBandPrefUpdated = false;
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.wifi_tether_settings) { // from class: com.android.settings.wifi.tether.WifiTetherSettings.1
        @Override // com.android.settings.search.BaseSearchIndexProvider, com.android.settingslib.search.Indexable$SearchIndexProvider
        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            if (!TetherUtil.isTetherAvailable(context)) {
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_NAME);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_PASSWORD);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_AUTO_OFF);
                nonIndexableKeys.add(WifiTetherSettings.KEY_WIFI_TETHER_NETWORK_AP_BAND);
            }
            nonIndexableKeys.add("wifi_tether_settings_screen");
            return nonIndexableKeys;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public boolean isPageSearchEnabled(Context context) {
            return !FeatureFlagUtils.isEnabled(context, "settings_tether_all_in_one");
        }

        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return WifiTetherSettings.buildPreferenceControllers(context, null);
        }
    };
    private static boolean mWasApBand6GHzSelected = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "WifiTetherSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1014;
    }

    static {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.TETHER_STATE_CHANGED");
        TETHER_STATE_CHANGE_FILTER = intentFilter;
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
    }

    public WifiTetherSettings() {
        super("no_config_tethering");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WifiTetherSSIDPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherSecurityPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherPasswordPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherApBandPreferenceController(context, onTetherConfigUpdateListener));
        arrayList.add(new WifiTetherAutoOffPreferenceController(context, KEY_WIFI_TETHER_AUTO_OFF));
        return arrayList;
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setIfOnlyAvailableForAdmins(true);
        if (isUiRestricted()) {
            this.mUnavailable = true;
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mTetherChangeReceiver = new TetherChangeReceiver();
        this.mSSIDPreferenceController = (WifiTetherSSIDPreferenceController) use(WifiTetherSSIDPreferenceController.class);
        this.mSecurityPreferenceController = (WifiTetherSecurityPreferenceController) use(WifiTetherSecurityPreferenceController.class);
        this.mPasswordPreferenceController = (WifiTetherPasswordPreferenceController) use(WifiTetherPasswordPreferenceController.class);
        this.mApBandPreferenceController = (WifiTetherApBandPreferenceController) use(WifiTetherApBandPreferenceController.class);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mUnavailable) {
            return;
        }
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R.string.use_wifi_hotsopt_main_switch_title));
        this.mSwitchBarController = new WifiTetherSwitchBarController(settingsActivity, switchBar);
        getSettingsLifecycle().addObserver(this.mSwitchBarController);
        switchBar.show();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.mUnavailable) {
            if (!isUiRestrictedByOnlyAdmin()) {
                getEmptyTextView().setText(R.string.tethering_settings_not_available);
            }
            getPreferenceScreen().removeAll();
            return;
        }
        Context context = getContext();
        if (context == null) {
            return;
        }
        context.registerReceiver(this.mTetherChangeReceiver, TETHER_STATE_CHANGE_FILTER);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        Context context;
        super.onStop();
        if (!this.mUnavailable && (context = getContext()) != null) {
            context.unregisterReceiver(this.mTetherChangeReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.wifi_tether_settings;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, new WifiTetherBasePreferenceController.OnTetherConfigUpdateListener() { // from class: com.android.settings.wifi.tether.WifiTetherSettings$$ExternalSyntheticLambda0
            @Override // com.android.settings.wifi.tether.WifiTetherBasePreferenceController.OnTetherConfigUpdateListener
            public final void onTetherConfigUpdated(AbstractPreferenceController abstractPreferenceController) {
                WifiTetherSettings.this.onTetherConfigUpdated(abstractPreferenceController);
            }
        });
    }

    @Override // com.android.settings.wifi.tether.WifiTetherBasePreferenceController.OnTetherConfigUpdateListener
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
        if (this.mApBandPreferenceController.getBandIndex() == 5 && !mWasApBand6GHzSelected) {
            this.mSecurityPreferenceController.updateDisplay();
            mWasApBand6GHzSelected = true;
            this.mWifiManager.setSoftApConfiguration(buildNewConfig());
        } else if (this.mApBandPreferenceController.getBandIndex() != 5 && mWasApBand6GHzSelected) {
            this.mSecurityPreferenceController.updateDisplay();
            mWasApBand6GHzSelected = false;
        }
        if (abstractPreferenceController instanceof WifiTetherSecurityPreferenceController) {
            reConfigInitialExpandedChildCount();
        }
    }

    private SoftApConfiguration buildNewConfig() {
        SoftApConfiguration.Builder builder = new SoftApConfiguration.Builder();
        int securityType = this.mSecurityPreferenceController.getSecurityType();
        builder.setSsid(this.mSSIDPreferenceController.getSSID());
        if (securityType == 0 || securityType == 4) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void startTether() {
        this.mRestartWifiApAfterConfigChange = false;
        this.mSwitchBarController.startTether();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayWithNewConfig() {
        ((WifiTetherSSIDPreferenceController) use(WifiTetherSSIDPreferenceController.class)).updateDisplay();
        ((WifiTetherSecurityPreferenceController) use(WifiTetherSecurityPreferenceController.class)).updateDisplay();
        ((WifiTetherPasswordPreferenceController) use(WifiTetherPasswordPreferenceController.class)).updateDisplay();
        ((WifiTetherApBandPreferenceController) use(WifiTetherApBandPreferenceController.class)).updateDisplay();
    }

    private void reConfigInitialExpandedChildCount() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.setInitialExpandedChildrenCount(getInitialExpandedChildCount());
        }
    }

    /* loaded from: classes.dex */
    class TetherChangeReceiver extends BroadcastReceiver {
        TetherChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("WifiTetherSettings", "updating display config due to receiving broadcast action " + action + ", config changed " + WifiTetherSettings.this.mRestartWifiApAfterConfigChange);
            if (WifiTetherSettings.this.mRestartWifiApAfterConfigChange) {
                WifiTetherSettings.this.updateDisplayWithNewConfig();
            }
            if (action.equals("android.net.conn.TETHER_STATE_CHANGED")) {
                if (WifiTetherSettings.this.mWifiManager.getWifiApState() != 11 || !WifiTetherSettings.this.mRestartWifiApAfterConfigChange) {
                    return;
                }
                WifiTetherSettings.this.startTether();
            } else if (!action.equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
            } else {
                int intExtra = intent.getIntExtra("wifi_state", 0);
                if (intExtra == 11 && WifiTetherSettings.this.mRestartWifiApAfterConfigChange) {
                    WifiTetherSettings.this.startTether();
                }
                if (intExtra != 11 && intExtra != 13) {
                    return;
                }
                WifiTetherSettings.this.mSSIDPreferenceController.updateDisplay();
            }
        }
    }
}
