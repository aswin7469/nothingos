package com.android.settings.network;

import android.content.Context;
import android.net.wifi.WifiManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.GenericSwitchController;
import com.android.settings.wifi.WifiEnabler;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;

public class WifiSwitchPreferenceController extends AbstractPreferenceController implements LifecycleObserver {
    boolean mIsChangeWifiStateAllowed;
    private RestrictedSwitchPreference mPreference;
    WifiEnabler mWifiEnabler;

    public String getPreferenceKey() {
        return "main_toggle_wifi";
    }

    public boolean isAvailable() {
        return true;
    }

    public WifiSwitchPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
            this.mIsChangeWifiStateAllowed = WifiEnterpriseRestrictionUtils.isChangeWifiStateAllowed(context);
            return;
        }
        throw new IllegalArgumentException("Lifecycle must be set");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = restrictedSwitchPreference;
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setChecked(isWifiEnabled());
            if (!this.mIsChangeWifiStateAllowed) {
                this.mPreference.setEnabled(false);
                this.mPreference.setSummary(R$string.not_allowed_by_ent);
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
        if (restrictedSwitchPreference != null && this.mIsChangeWifiStateAllowed) {
            this.mWifiEnabler = new WifiEnabler(this.mContext, new GenericSwitchController(restrictedSwitchPreference), FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.teardownSwitchController();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.resume(this.mContext);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.pause();
        }
    }

    private boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) this.mContext.getSystemService(WifiManager.class);
        if (wifiManager == null) {
            return false;
        }
        return wifiManager.isWifiEnabled();
    }
}
