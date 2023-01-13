package com.android.settingslib.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.provider.Settings;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ConnectivitySubsystemsRecoveryManager {
    private static final long RESTART_TIMEOUT_MS = 15000;
    private static final String TAG = "ConnectivitySubsystemsRecoveryManager";
    private final BroadcastReceiver mApmMonitor = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RecoveryAvailableListener access$000 = ConnectivitySubsystemsRecoveryManager.this.mRecoveryAvailableListener;
            if (access$000 != null) {
                access$000.onRecoveryAvailableChangeListener(ConnectivitySubsystemsRecoveryManager.this.isRecoveryAvailable());
            }
        }
    };
    private boolean mApmMonitorRegistered = false;
    private final Context mContext;
    /* access modifiers changed from: private */
    public RecoveryStatusCallback mCurrentRecoveryCallback = null;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public RecoveryAvailableListener mRecoveryAvailableListener = null;
    private final MobileTelephonyCallback mTelephonyCallback = new MobileTelephonyCallback();
    private TelephonyManager mTelephonyManager = null;
    /* access modifiers changed from: private */
    public boolean mTelephonyRestartInProgress = false;
    private WifiManager mWifiManager = null;
    /* access modifiers changed from: private */
    public boolean mWifiRestartInProgress = false;
    private final WifiManager.SubsystemRestartTrackingCallback mWifiSubsystemRestartTrackingCallback = new WifiManager.SubsystemRestartTrackingCallback() {
        public void onSubsystemRestarting() {
        }

        public void onSubsystemRestarted() {
            boolean unused = ConnectivitySubsystemsRecoveryManager.this.mWifiRestartInProgress = false;
            ConnectivitySubsystemsRecoveryManager.this.stopTrackingWifiRestart();
            ConnectivitySubsystemsRecoveryManager.this.checkIfAllSubsystemsRestartsAreDone();
        }
    };

    public interface RecoveryAvailableListener {
        void onRecoveryAvailableChangeListener(boolean z);
    }

    public interface RecoveryStatusCallback {
        void onSubsystemRestartOperationBegin();

        void onSubsystemRestartOperationEnd();
    }

    private class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.RadioPowerStateListener {
        private MobileTelephonyCallback() {
        }

        public void onRadioPowerStateChanged(int i) {
            if (!ConnectivitySubsystemsRecoveryManager.this.mTelephonyRestartInProgress || ConnectivitySubsystemsRecoveryManager.this.mCurrentRecoveryCallback == null) {
                ConnectivitySubsystemsRecoveryManager.this.stopTrackingTelephonyRestart();
            }
            if (i == 1) {
                boolean unused = ConnectivitySubsystemsRecoveryManager.this.mTelephonyRestartInProgress = false;
                ConnectivitySubsystemsRecoveryManager.this.stopTrackingTelephonyRestart();
                ConnectivitySubsystemsRecoveryManager.this.checkIfAllSubsystemsRestartsAreDone();
            }
        }
    }

    public ConnectivitySubsystemsRecoveryManager(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = new Handler(handler.getLooper());
        if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
            this.mWifiManager = wifiManager;
            if (wifiManager == null) {
                Log.e(TAG, "WifiManager not available!?");
            }
        }
        if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            this.mTelephonyManager = telephonyManager;
            if (telephonyManager == null) {
                Log.e(TAG, "TelephonyManager not available!?");
            }
        }
    }

    public void setRecoveryAvailableListener(RecoveryAvailableListener recoveryAvailableListener) {
        this.mHandler.post(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda0(this, recoveryAvailableListener));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setRecoveryAvailableListener$0$com-android-settingslib-connectivity-ConnectivitySubsystemsRecoveryManager */
    public /* synthetic */ void mo28446x8f9ba31(RecoveryAvailableListener recoveryAvailableListener) {
        this.mRecoveryAvailableListener = recoveryAvailableListener;
        startTrackingRecoveryAvailability();
    }

    public void clearRecoveryAvailableListener() {
        this.mHandler.post(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$clearRecoveryAvailableListener$1$com-android-settingslib-connectivity-ConnectivitySubsystemsRecoveryManager */
    public /* synthetic */ void mo28445xcb7cf47b() {
        this.mRecoveryAvailableListener = null;
        stopTrackingRecoveryAvailability();
    }

    private boolean isApmEnabled() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private boolean isWifiEnabled() {
        WifiManager wifiManager = this.mWifiManager;
        return wifiManager != null && (wifiManager.isWifiEnabled() || this.mWifiManager.isWifiApEnabled());
    }

    public boolean isRecoveryAvailable() {
        if (!isApmEnabled()) {
            return true;
        }
        return isWifiEnabled();
    }

    private void startTrackingRecoveryAvailability() {
        if (!this.mApmMonitorRegistered) {
            this.mContext.registerReceiver(this.mApmMonitor, new IntentFilter("android.intent.action.AIRPLANE_MODE"), (String) null, this.mHandler);
            this.mApmMonitorRegistered = true;
        }
    }

    private void stopTrackingRecoveryAvailability() {
        if (this.mApmMonitorRegistered) {
            this.mContext.unregisterReceiver(this.mApmMonitor);
            this.mApmMonitorRegistered = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void startTrackingWifiRestart() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.registerSubsystemRestartTrackingCallback(new HandlerExecutor(this.mHandler), this.mWifiSubsystemRestartTrackingCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void stopTrackingWifiRestart() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.unregisterSubsystemRestartTrackingCallback(this.mWifiSubsystemRestartTrackingCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void startTrackingTelephonyRestart() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            telephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mHandler), this.mTelephonyCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void stopTrackingTelephonyRestart() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            telephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
        }
    }

    /* access modifiers changed from: private */
    public void checkIfAllSubsystemsRestartsAreDone() {
        RecoveryStatusCallback recoveryStatusCallback;
        if (!this.mWifiRestartInProgress && !this.mTelephonyRestartInProgress && (recoveryStatusCallback = this.mCurrentRecoveryCallback) != null) {
            recoveryStatusCallback.onSubsystemRestartOperationEnd();
            this.mCurrentRecoveryCallback = null;
        }
    }

    public void triggerSubsystemRestart(String str, RecoveryStatusCallback recoveryStatusCallback) {
        this.mHandler.post(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda3(this, recoveryStatusCallback));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$triggerSubsystemRestart$3$com-android-settingslib-connectivity-ConnectivitySubsystemsRecoveryManager */
    public /* synthetic */ void mo28448xb18e63b6(RecoveryStatusCallback recoveryStatusCallback) {
        boolean z;
        if (this.mWifiRestartInProgress) {
            Log.e(TAG, "Wifi restart still in progress");
        } else if (this.mTelephonyRestartInProgress) {
            Log.e(TAG, "Telephony restart still in progress");
        } else {
            boolean z2 = true;
            if (isWifiEnabled()) {
                this.mWifiManager.restartWifiSubsystem();
                this.mWifiRestartInProgress = true;
                startTrackingWifiRestart();
                z = true;
            } else {
                z = false;
            }
            if (this.mTelephonyManager == null || isApmEnabled() || !this.mTelephonyManager.rebootRadio()) {
                z2 = z;
            } else {
                this.mTelephonyRestartInProgress = true;
                startTrackingTelephonyRestart();
            }
            if (z2) {
                this.mCurrentRecoveryCallback = recoveryStatusCallback;
                recoveryStatusCallback.onSubsystemRestartOperationBegin();
                this.mHandler.postDelayed(new ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda2(this), RESTART_TIMEOUT_MS);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$triggerSubsystemRestart$2$com-android-settingslib-connectivity-ConnectivitySubsystemsRecoveryManager */
    public /* synthetic */ void mo28447x6f773657() {
        stopTrackingWifiRestart();
        stopTrackingTelephonyRestart();
        this.mWifiRestartInProgress = false;
        this.mTelephonyRestartInProgress = false;
        RecoveryStatusCallback recoveryStatusCallback = this.mCurrentRecoveryCallback;
        if (recoveryStatusCallback != null) {
            recoveryStatusCallback.onSubsystemRestartOperationEnd();
            this.mCurrentRecoveryCallback = null;
        }
    }
}
