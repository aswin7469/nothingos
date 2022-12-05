package com.android.settings.wifi.tether;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.hardware.display.WifiDisplayStatus;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public class WifiTetherSwitchBarController implements LifecycleObserver, OnStart, OnStop, DataSaverBackend.Listener, View.OnClickListener {
    private static final IntentFilter WIFI_INTENT_FILTER = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    final DataSaverBackend mDataSaverBackend;
    private final DisplayManager mDisplayManager;
    final ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback = new ConnectivityManager.OnStartTetheringCallback() { // from class: com.android.settings.wifi.tether.WifiTetherSwitchBarController.1
        public void onTetheringFailed() {
            super.onTetheringFailed();
            WifiTetherSwitchBarController.this.mSwitchBar.setChecked(false);
            WifiTetherSwitchBarController.this.updateWifiSwitch();
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settings.wifi.tether.WifiTetherSwitchBarController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
                WifiTetherSwitchBarController.this.handleWifiApStateChanged(intent.getIntExtra("wifi_state", 14));
            }
        }
    };
    private final Switch mSwitch;
    private final SettingsMainSwitchBar mSwitchBar;
    private boolean mSwitchClicked;
    private final WifiManager mWifiManager;

    @Override // com.android.settings.datausage.DataSaverBackend.Listener
    public void onAllowlistStatusChanged(int i, boolean z) {
    }

    @Override // com.android.settings.datausage.DataSaverBackend.Listener
    public void onDenylistStatusChanged(int i, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WifiTetherSwitchBarController(Context context, SettingsMainSwitchBar settingsMainSwitchBar) {
        this.mContext = context;
        this.mSwitchBar = settingsMainSwitchBar;
        this.mSwitch = settingsMainSwitchBar.getSwitch();
        this.mDataSaverBackend = new DataSaverBackend(context);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        this.mWifiManager = wifiManager;
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        settingsMainSwitchBar.setChecked(wifiManager.getWifiApState() == 13);
        updateWifiSwitch();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mDataSaverBackend.addListener(this);
        this.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.wifi.tether.WifiTetherSwitchBarController$$ExternalSyntheticLambda2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                WifiTetherSwitchBarController.this.lambda$onStart$0(compoundButton, z);
            }
        });
        this.mSwitch.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.settings.wifi.tether.WifiTetherSwitchBarController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$onStart$1;
                lambda$onStart$1 = WifiTetherSwitchBarController.this.lambda$onStart$1(view, motionEvent);
                return lambda$onStart$1;
            }
        });
        this.mSwitchBar.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.settings.wifi.tether.WifiTetherSwitchBarController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$onStart$2;
                lambda$onStart$2 = WifiTetherSwitchBarController.this.lambda$onStart$2(view, motionEvent);
                return lambda$onStart$2;
            }
        });
        this.mContext.registerReceiver(this.mReceiver, WIFI_INTENT_FILTER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(CompoundButton compoundButton, boolean z) {
        this.mSwitchBar.setEnabled(false);
        WifiDisplayStatus wifiDisplayStatus = this.mDisplayManager.getWifiDisplayStatus();
        if (wifiDisplayStatus.getScanState() == 1) {
            this.mDisplayManager.stopWifiDisplayScan();
        }
        if (wifiDisplayStatus.getActiveDisplayState() == 1) {
            this.mDisplayManager.disconnectWifiDisplay();
        }
        if (this.mSwitchClicked) {
            if (z) {
                startTether();
            } else {
                stopTether();
            }
            this.mSwitchClicked = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onStart$1(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mSwitchClicked = true;
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onStart$2(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            this.mSwitchClicked = true;
            return false;
        }
        return false;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mDataSaverBackend.remListener(this);
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (((Switch) view).isChecked()) {
            startTether();
        } else {
            stopTether();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopTether() {
        this.mSwitchBar.setEnabled(false);
        this.mConnectivityManager.stopTethering(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startTether() {
        this.mSwitchBar.setEnabled(false);
        this.mConnectivityManager.startTethering(0, true, this.mOnStartTetheringCallback, new Handler(Looper.getMainLooper()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWifiApStateChanged(int i) {
        Log.d("WifiTetherSettings", "Wifi ApState " + i + ", SwitchBar " + this.mSwitch.isChecked());
        switch (i) {
            case 10:
                this.mSwitchBar.setEnabled(false);
                return;
            case 11:
                updateWifiSwitch();
                return;
            case 12:
                this.mSwitchBar.setEnabled(false);
                return;
            case 13:
                updateWifiSwitch();
                return;
            default:
                this.mSwitch.setChecked(false);
                updateWifiSwitch();
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWifiSwitch() {
        this.mSwitchBar.setEnabled(!this.mDataSaverBackend.isDataSaverEnabled());
    }

    @Override // com.android.settings.datausage.DataSaverBackend.Listener
    public void onDataSaverChanged(boolean z) {
        updateWifiSwitch();
    }
}
