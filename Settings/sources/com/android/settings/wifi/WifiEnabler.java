package com.android.settings.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Pair;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class WifiEnabler implements SwitchWidgetController.OnSwitchChangeListener {
    private AtomicBoolean mConnected;
    private final ConnectivityManager mConnectivityManager;
    private Context mContext;
    private final IntentFilter mIntentFilter;
    private final AtomicBoolean mIsSwitchChecked;
    private final AtomicBoolean mIsWifiEnabled;
    private boolean mListeningToOnSwitchChange;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final BroadcastReceiver mReceiver;
    private boolean mStateMachineEvent;
    private final SwitchWidgetController mSwitchWidget;
    private final WifiManager mWifiManager;

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStateChanged(NetworkInfo.DetailedState detailedState) {
    }

    private void setSwitchBarChecked(boolean z) {
    }

    public WifiEnabler(Context context, SwitchWidgetController switchWidgetController, MetricsFeatureProvider metricsFeatureProvider) {
        this(context, switchWidgetController, metricsFeatureProvider, (ConnectivityManager) context.getSystemService("connectivity"));
    }

    WifiEnabler(Context context, SwitchWidgetController switchWidgetController, MetricsFeatureProvider metricsFeatureProvider, ConnectivityManager connectivityManager) {
        this.mListeningToOnSwitchChange = false;
        this.mConnected = new AtomicBoolean(false);
        this.mIsSwitchChecked = new AtomicBoolean(false);
        this.mIsWifiEnabled = new AtomicBoolean(false);
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.settings.wifi.WifiEnabler.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                    WifiEnabler.this.handleWifiStateChanged(intent.getIntExtra("wifi_state", 4));
                } else if ("android.net.wifi.supplicant.STATE_CHANGE".equals(action)) {
                    if (WifiEnabler.this.mConnected.get()) {
                        return;
                    }
                    WifiEnabler.this.handleStateChanged(android.net.wifi.WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra("newState")));
                } else if (!"android.net.wifi.STATE_CHANGE".equals(action)) {
                } else {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    WifiEnabler.this.mConnected.set(networkInfo.isConnected());
                    WifiEnabler.this.handleStateChanged(networkInfo.getDetailedState());
                }
            }
        };
        this.mContext = context;
        this.mSwitchWidget = switchWidgetController;
        switchWidgetController.setListener(this);
        this.mMetricsFeatureProvider = metricsFeatureProvider;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mConnectivityManager = connectivityManager;
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        setupSwitchController();
    }

    public void setupSwitchController() {
        this.mWifiManager.allowConnectOnPartialScanResults(true);
        int wifiState = this.mWifiManager.getWifiState();
        this.mIsWifiEnabled.set(wifiState == 3);
        this.mIsSwitchChecked.set(this.mIsWifiEnabled.get());
        this.mSwitchWidget.setChecked(this.mIsWifiEnabled.get());
        handleWifiStateChanged(wifiState);
        if (!this.mListeningToOnSwitchChange) {
            this.mSwitchWidget.startListening();
            this.mListeningToOnSwitchChange = true;
        }
        this.mSwitchWidget.setupView();
    }

    public void teardownSwitchController() {
        if (this.mListeningToOnSwitchChange) {
            this.mSwitchWidget.stopListening();
            this.mListeningToOnSwitchChange = false;
        }
        this.mSwitchWidget.teardownView();
        this.mWifiManager.allowConnectOnPartialScanResults(false);
    }

    public void resume(Context context) {
        this.mContext = context;
        context.registerReceiver(this.mReceiver, this.mIntentFilter);
        if (!this.mListeningToOnSwitchChange) {
            this.mIsSwitchChecked.set(this.mWifiManager.isWifiEnabled());
            this.mSwitchWidget.setChecked(this.mIsSwitchChecked.get());
            this.mSwitchWidget.startListening();
            this.mListeningToOnSwitchChange = true;
        }
    }

    public void pause() {
        this.mContext.unregisterReceiver(this.mReceiver);
        if (this.mListeningToOnSwitchChange) {
            this.mSwitchWidget.stopListening();
            this.mListeningToOnSwitchChange = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWifiStateChanged(int i) {
        if (i != 0) {
            if (i == 1) {
                this.mIsWifiEnabled.set(false);
                setSwitchBarChecked(false);
            } else if (i != 2) {
                if (i == 3) {
                    this.mIsWifiEnabled.set(true);
                    setSwitchBarChecked(true);
                } else {
                    setSwitchBarChecked(false);
                    this.mSwitchWidget.setEnabled(true);
                }
            }
        }
        if (i == 3 || i == 1) {
            if (this.mIsWifiEnabled.get() != this.mIsSwitchChecked.get()) {
                new Thread(new Runnable() { // from class: com.android.settings.wifi.WifiEnabler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        WifiEnabler.this.lambda$handleWifiStateChanged$0();
                    }
                }).start();
            }
            this.mSwitchWidget.setEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleWifiStateChanged$0() {
        this.mWifiManager.setWifiEnabled(this.mIsSwitchChecked.get());
    }

    @Override // com.android.settings.widget.SwitchWidgetController.OnSwitchChangeListener
    public boolean onSwitchToggled(final boolean z) {
        if (this.mStateMachineEvent) {
            return true;
        }
        if (z && !WirelessUtils.isRadioAllowed(this.mContext, "wifi")) {
            Toast.makeText(this.mContext, R.string.wifi_in_airplane_mode, 0).show();
            this.mSwitchWidget.setChecked(false);
            return false;
        }
        if (z) {
            this.mMetricsFeatureProvider.action(this.mContext, 139, new Pair[0]);
        } else {
            this.mMetricsFeatureProvider.action(this.mContext, 138, this.mConnected.get());
        }
        if (this.mIsWifiEnabled.get() == this.mIsSwitchChecked.get() && this.mIsSwitchChecked.get() != z) {
            new Thread(new Runnable() { // from class: com.android.settings.wifi.WifiEnabler$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    WifiEnabler.this.lambda$onSwitchToggled$1(z);
                }
            }).start();
        }
        this.mIsSwitchChecked.set(z);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSwitchToggled$1(boolean z) {
        this.mWifiManager.setWifiEnabled(z);
    }
}
