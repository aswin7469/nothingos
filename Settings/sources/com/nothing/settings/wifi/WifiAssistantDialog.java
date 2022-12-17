package com.nothing.settings.wifi;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.R$string;
import com.nothing.experience.AppTracking;

public class WifiAssistantDialog extends AlertActivity implements DialogInterface.OnClickListener {
    private String mAction;
    private AppTracking mAppTracker;
    private ConnectivityManager mCM;
    private ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public Network mNetwork;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private String mNetworkName;
    private WifiConfiguration mWifiConfig;
    private WifiManager mWifiManager;

    public void onCreate(Bundle bundle) {
        WifiAssistantDialog.super.onCreate(bundle);
        Log.e("WifiAssistantDialog", "onCreate");
        Intent intent = getIntent();
        if (intent == null) {
            Log.e("WifiAssistantDialog", "Unexpected intent " + intent + ", exiting");
            finish();
            return;
        }
        this.mContentResolver = getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                Settings.System.putString(WifiAssistantDialog.this.mContentResolver, "assistant_avoid_bad_wifi", (String) null);
                Log.e("WifiAssistantDialog", "onChange---finish()");
                WifiAssistantDialog.this.finish();
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("dismiss_no_internet_dialog"), false, this.mContentObserver);
        String action = intent.getAction();
        this.mAction = action;
        if ("android.net.wifi.action.SWITCH_TO_CELLULARDATA".equals(action)) {
            this.mNetwork = (Network) intent.getParcelableExtra("android.net.wifi.extra.CURRENT_NETWORK");
        }
        if ("android.net.wifi.action.SWITCH_TO_WIFI".equals(this.mAction)) {
            this.mWifiConfig = (WifiConfiguration) intent.getParcelableExtra("wifiConfiguration");
        }
        if ("android.net.wifi.action.SWITCH_TO_CELLULARDATA".equals(this.mAction) && this.mNetwork == null) {
            Log.e("WifiAssistantDialog", "Can't determine network from intent extra mNetwork, exiting");
            finish();
        } else if (!"android.net.wifi.action.SWITCH_TO_WIFI".equals(this.mAction) || this.mWifiConfig != null) {
            NetworkRequest build = new NetworkRequest.Builder().clearCapabilities().build();
            this.mNetworkCallback = new ConnectivityManager.NetworkCallback() {
                public void onLost(Network network) {
                    if (WifiAssistantDialog.this.mNetwork != null && WifiAssistantDialog.this.mNetwork.equals(network)) {
                        Log.d("WifiAssistantDialog", "Network " + WifiAssistantDialog.this.mNetwork + " disconnected");
                        WifiAssistantDialog.this.finish();
                    }
                }

                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                    if (WifiAssistantDialog.this.mNetwork != null && WifiAssistantDialog.this.mNetwork.equals(network) && networkCapabilities.hasCapability(16)) {
                        Log.d("WifiAssistantDialog", "Network " + WifiAssistantDialog.this.mNetwork + " validated");
                        WifiAssistantDialog.this.finish();
                    }
                }
            };
            this.mWifiManager = (WifiManager) getSystemService("wifi");
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
            this.mCM = connectivityManager;
            connectivityManager.registerNetworkCallback(build, this.mNetworkCallback);
            String ssid = this.mWifiManager.getConnectionInfo().getSSID();
            this.mNetworkName = ssid;
            if (ssid == null) {
                this.mNetworkName = "Wi-Fi";
            }
            createDialog();
        } else {
            Log.e("WifiAssistantDialog", "Can't determine network from intent extra mWifiConfig, exiting");
            finish();
        }
    }

    private void createDialog() {
        Log.e("WifiAssistantDialog", "createDialog");
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = getString(R$string.wifi_assistant_dialog_title);
        alertParams.mMessage = getString(R$string.wifi_assistant_dialog_message, new Object[]{this.mNetworkName});
        alertParams.mPositiveButtonText = getString(R$string.r_angle_adjust_change_btn_text);
        alertParams.mNegativeButtonText = getString(R$string.wifi_assistant_dialog_keep_connect);
        alertParams.mPositiveButtonListener = this;
        alertParams.mNegativeButtonListener = this;
        Log.e("WifiAssistantDialog", "setupAlert");
        setupAlert();
        setTrackingEvent("wifiqualityreminder_dialog", 1);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ContentObserver contentObserver = this.mContentObserver;
        if (contentObserver != null) {
            this.mContentResolver.unregisterContentObserver(contentObserver);
        }
        ConnectivityManager.NetworkCallback networkCallback = this.mNetworkCallback;
        if (networkCallback != null) {
            this.mCM.unregisterNetworkCallback(networkCallback);
            this.mNetworkCallback = null;
        }
        WifiAssistantDialog.super.onDestroy();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i != -2 && i != -1) {
            return;
        }
        if ("android.net.wifi.action.SWITCH_TO_CELLULARDATA".equals(this.mAction)) {
            if (i == -1) {
                Settings.System.putString(this.mContentResolver, "assistant_avoid_bad_wifi", "1");
                this.mCM.setAvoidUnvalidated(this.mNetwork);
                setTrackingEvent("wifiqualityreminder_dialogswitch", 1);
            } else if (i == -2) {
                Settings.System.putString(this.mContentResolver, "assistant_avoid_bad_wifi", "0");
                setTrackingEvent("wifiqualityreminder_dialogskeep", 0);
            }
        } else if (!"android.net.wifi.action.SWITCH_TO_WIFI".equals(this.mAction)) {
        } else {
            if (i == -1) {
                Settings.System.putString(this.mContentResolver, "assistant_avoid_bad_wifi", "1");
                this.mWifiManager.connect(this.mWifiConfig, (WifiManager.ActionListener) null);
                setTrackingEvent("wifiqualityreminder_dialogswitch", 1);
            } else if (i == -2) {
                Settings.System.putString(this.mContentResolver, "assistant_avoid_bad_wifi", "0");
                setTrackingEvent("wifiqualityreminder_dialogskeep", 0);
            }
        }
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [android.content.Context, com.nothing.settings.wifi.WifiAssistantDialog] */
    public void setTrackingEvent(String str, int i) {
        if (this.mAppTracker == null) {
            this.mAppTracker = AppTracking.getInstance(this);
        }
        if (this.mAppTracker != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(str, i);
            this.mAppTracker.logProductEvent("Settings_networkassistant", bundle);
        }
    }
}
