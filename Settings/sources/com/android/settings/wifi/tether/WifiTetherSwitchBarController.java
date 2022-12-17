package com.android.settings.wifi.tether;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Switch;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.nothing.p006ui.support.NtCustSwitch;

public class WifiTetherSwitchBarController implements LifecycleObserver, OnStart, OnStop, OnResume, DataSaverBackend.Listener, OnMainSwitchChangeListener {
    private static final IntentFilter WIFI_INTENT_FILTER = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    DataSaverBackend mDataSaverBackend;
    final ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback = new ConnectivityManager.OnStartTetheringCallback() {
        public void onTetheringFailed() {
            WifiTetherSwitchBarController.super.onTetheringFailed();
            Log.e("WifiTetherSBC", "Failed to start Wi-Fi Tethering.");
            WifiTetherSwitchBarController wifiTetherSwitchBarController = WifiTetherSwitchBarController.this;
            wifiTetherSwitchBarController.handleWifiApStateChanged(wifiTetherSwitchBarController.mWifiManager.getWifiApState());
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
                WifiTetherSwitchBarController.this.handleWifiApStateChanged(intent.getIntExtra("wifi_state", 14));
            }
        }
    };
    private final NtCustSwitch mSwitch;
    private final SettingsMainSwitchBar mSwitchBar;
    /* access modifiers changed from: private */
    public final WifiManager mWifiManager;

    public void onAllowlistStatusChanged(int i, boolean z) {
    }

    public void onDenylistStatusChanged(int i, boolean z) {
    }

    WifiTetherSwitchBarController(Context context, SettingsMainSwitchBar settingsMainSwitchBar) {
        this.mContext = context;
        this.mSwitchBar = settingsMainSwitchBar;
        this.mSwitch = settingsMainSwitchBar.getSwitch();
        this.mDataSaverBackend = new DataSaverBackend(context);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        this.mWifiManager = wifiManager;
        settingsMainSwitchBar.setChecked(wifiManager.getWifiApState() == 13);
        updateWifiSwitch();
    }

    public void onStart() {
        this.mDataSaverBackend.addListener(this);
        this.mSwitchBar.addOnSwitchChangeListener(this);
        this.mContext.registerReceiver(this.mReceiver, WIFI_INTENT_FILTER, 2);
    }

    public void onStop() {
        this.mDataSaverBackend.remListener(this);
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (switchR.isEnabled()) {
            if (z) {
                startTether();
            } else {
                stopTether();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void stopTether() {
        if (isWifiApActivated()) {
            this.mSwitchBar.setEnabled(false);
            this.mConnectivityManager.stopTethering(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void startTether() {
        if (!isWifiApActivated()) {
            this.mSwitchBar.setEnabled(false);
            this.mConnectivityManager.startTethering(0, true, this.mOnStartTetheringCallback, new Handler(Looper.getMainLooper()));
        }
    }

    private boolean isWifiApActivated() {
        int wifiApState = this.mWifiManager.getWifiApState();
        return wifiApState == 13 || wifiApState == 12;
    }

    /* access modifiers changed from: package-private */
    public void handleWifiApStateChanged(int i) {
        if (i != 12 && i != 10) {
            boolean z = i == 13;
            if (this.mSwitch.isChecked() != z) {
                this.mSwitch.setChecked(z);
            }
            updateWifiSwitch();
        }
    }

    private void updateWifiSwitch() {
        this.mSwitchBar.setEnabled(!this.mDataSaverBackend.isDataSaverEnabled());
    }

    public void onDataSaverChanged(boolean z) {
        updateWifiSwitch();
    }

    public void onResume() {
        handleWifiApStateChanged(this.mWifiManager.getWifiApState());
    }
}
