package com.android.wifitrackerlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TransportInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.time.Clock;
import java.util.Objects;

public class BaseWifiTracker implements LifecycleObserver {
    private static boolean sVerboseLogging;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Received broadcast: " + action);
            }
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int unused = BaseWifiTracker.this.mWifiState = intent.getIntExtra("wifi_state", 1);
                if (BaseWifiTracker.this.mWifiState == 3) {
                    BaseWifiTracker.this.mScanner.start();
                } else {
                    BaseWifiTracker.this.mScanner.stop();
                }
                BaseWifiTracker.this.notifyOnWifiStateChanged();
                BaseWifiTracker.this.handleWifiStateChangedAction();
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                BaseWifiTracker.this.handleScanResultsAvailableAction(intent);
            } else if (WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION.equals(action)) {
                BaseWifiTracker.this.handleConfiguredNetworksChangedAction(intent);
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                BaseWifiTracker.this.handleNetworkStateChangedAction(intent);
            } else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
                BaseWifiTracker.this.handleRssiChangedAction();
            } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                BaseWifiTracker.this.handleDefaultSubscriptionChanged(intent.getIntExtra("subscription", -1));
            }
        }
    };
    protected final ConnectivityManager mConnectivityManager;
    protected final Context mContext;
    private final ConnectivityManager.NetworkCallback mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            boolean z = BaseWifiTracker.this.mIsWifiDefaultRoute;
            boolean z2 = BaseWifiTracker.this.mIsCellDefaultRoute;
            boolean z3 = true;
            BaseWifiTracker.this.mIsWifiDefaultRoute = networkCapabilities.hasTransport(1) || NonSdkApiWrapper.isVcnOverWifi(networkCapabilities);
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (baseWifiTracker.mIsWifiDefaultRoute || !networkCapabilities.hasTransport(0)) {
                z3 = false;
            }
            baseWifiTracker.mIsCellDefaultRoute = z3;
            if (BaseWifiTracker.this.mIsWifiDefaultRoute != z || BaseWifiTracker.this.mIsCellDefaultRoute != z2) {
                if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                    Log.v(BaseWifiTracker.this.mTag, "Wifi is the default route: " + BaseWifiTracker.this.mIsWifiDefaultRoute);
                    Log.v(BaseWifiTracker.this.mTag, "Cell is the default route: " + BaseWifiTracker.this.mIsCellDefaultRoute);
                }
                BaseWifiTracker.this.handleDefaultRouteChanged();
            }
        }

        public void onLost(Network network) {
            BaseWifiTracker.this.mIsWifiDefaultRoute = false;
            BaseWifiTracker.this.mIsCellDefaultRoute = false;
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Wifi is the default route: false");
                Log.v(BaseWifiTracker.this.mTag, "Cell is the default route: false");
            }
            BaseWifiTracker.this.handleDefaultRouteChanged();
        }
    };
    protected final WifiTrackerInjector mInjector;
    protected boolean mIsCellDefaultRoute;
    private boolean mIsInitialized = false;
    protected boolean mIsWifiDefaultRoute;
    protected boolean mIsWifiValidated;
    private final BaseWifiTrackerCallback mListener;
    protected final Handler mMainHandler;
    protected final long mMaxScanAgeMillis;
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (baseWifiTracker.isPrimaryWifiNetwork(baseWifiTracker.mConnectivityManager.getNetworkCapabilities(network))) {
                BaseWifiTracker.this.handleLinkPropertiesChanged(linkProperties);
            }
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (BaseWifiTracker.this.isPrimaryWifiNetwork(networkCapabilities)) {
                boolean z = BaseWifiTracker.this.mIsWifiValidated;
                BaseWifiTracker.this.mIsWifiValidated = networkCapabilities.hasCapability(16);
                if (BaseWifiTracker.isVerboseLoggingEnabled() && BaseWifiTracker.this.mIsWifiValidated != z) {
                    Log.v(BaseWifiTracker.this.mTag, "Is Wifi validated: " + BaseWifiTracker.this.mIsWifiValidated);
                }
                BaseWifiTracker.this.handleNetworkCapabilitiesChanged(networkCapabilities);
            }
        }

        public void onLost(Network network) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (baseWifiTracker.isPrimaryWifiNetwork(baseWifiTracker.mConnectivityManager.getNetworkCapabilities(network))) {
                BaseWifiTracker.this.mIsWifiValidated = false;
            }
        }
    };
    private final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
    protected final long mScanIntervalMillis;
    protected final ScanResultUpdater mScanResultUpdater;
    /* access modifiers changed from: private */
    public final Scanner mScanner;
    /* access modifiers changed from: private */
    public final String mTag;
    protected final WifiManager mWifiManager;
    /* access modifiers changed from: private */
    public int mWifiState = 1;
    protected final Handler mWorkerHandler;

    protected interface BaseWifiTrackerCallback {
        void onWifiStateChanged();
    }

    /* access modifiers changed from: protected */
    public void handleConfiguredNetworksChangedAction(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void handleDefaultRouteChanged() {
    }

    /* access modifiers changed from: protected */
    public void handleDefaultSubscriptionChanged(int i) {
    }

    /* access modifiers changed from: protected */
    public void handleLinkPropertiesChanged(LinkProperties linkProperties) {
    }

    /* access modifiers changed from: protected */
    public void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
    }

    /* access modifiers changed from: protected */
    public void handleNetworkStateChangedAction(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void handleOnStart() {
    }

    /* access modifiers changed from: protected */
    public void handleRssiChangedAction() {
    }

    /* access modifiers changed from: protected */
    public void handleScanResultsAvailableAction(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void handleWifiStateChangedAction() {
    }

    public static boolean isVerboseLoggingEnabled() {
        return sVerboseLogging;
    }

    public boolean isGbkSsidSupported() {
        return WifiTrackerInjector.isGbkSsidSupported();
    }

    /* access modifiers changed from: private */
    public boolean isPrimaryWifiNetwork(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return false;
        }
        TransportInfo transportInfo = networkCapabilities.getTransportInfo();
        if (!(transportInfo instanceof WifiInfo)) {
            return false;
        }
        return NonSdkApiWrapper.isPrimary((WifiInfo) transportInfo);
    }

    /* access modifiers changed from: protected */
    public void updateDefaultRouteInfo() {
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (networkCapabilities != null) {
            this.mIsWifiDefaultRoute = networkCapabilities.hasTransport(1);
            this.mIsCellDefaultRoute = networkCapabilities.hasTransport(0);
        } else {
            this.mIsWifiDefaultRoute = false;
            this.mIsCellDefaultRoute = false;
        }
        if (isVerboseLoggingEnabled()) {
            Log.v(this.mTag, "Wifi is the default route: " + this.mIsWifiDefaultRoute);
            Log.v(this.mTag, "Cell is the default route: " + this.mIsCellDefaultRoute);
        }
    }

    BaseWifiTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, BaseWifiTrackerCallback baseWifiTrackerCallback, String str) {
        long j3 = j;
        long j4 = j2;
        this.mInjector = wifiTrackerInjector;
        Lifecycle lifecycle2 = lifecycle;
        lifecycle.addObserver(this);
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mConnectivityManager = connectivityManager;
        this.mMainHandler = handler;
        this.mWorkerHandler = handler2;
        this.mMaxScanAgeMillis = j3;
        this.mScanIntervalMillis = j4;
        this.mListener = baseWifiTrackerCallback;
        this.mTag = str;
        this.mScanResultUpdater = new ScanResultUpdater(clock, j3 + j4);
        this.mScanner = new Scanner(handler2.getLooper());
        sVerboseLogging = wifiManager.isVerboseLoggingEnabled();
        updateDefaultRouteInfo();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mWorkerHandler.post(new BaseWifiTracker$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStart$0$com-android-wifitrackerlib-BaseWifiTracker  reason: not valid java name */
    public /* synthetic */ void m3369lambda$onStart$0$comandroidwifitrackerlibBaseWifiTracker() {
        updateDefaultRouteInfo();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, (String) null, this.mWorkerHandler);
        this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback, this.mWorkerHandler);
        NonSdkApiWrapper.registerSystemDefaultNetworkCallback(this.mConnectivityManager, this.mDefaultNetworkCallback, this.mWorkerHandler);
        handleOnStart();
        this.mIsInitialized = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mWorkerHandler.post(new BaseWifiTracker$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStop$1$com-android-wifitrackerlib-BaseWifiTracker  reason: not valid java name */
    public /* synthetic */ void m3370lambda$onStop$1$comandroidwifitrackerlibBaseWifiTracker() {
        this.mScanner.stop();
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mDefaultNetworkCallback);
    }

    /* access modifiers changed from: package-private */
    public boolean isInitialized() {
        return this.mIsInitialized;
    }

    public int getWifiState() {
        return this.mWifiState;
    }

    private class Scanner extends Handler {
        private static final int SCAN_RETRY_TIMES = 3;
        private boolean mIsActive;
        private int mRetry;

        private Scanner(Looper looper) {
            super(looper);
            this.mRetry = 0;
        }

        /* access modifiers changed from: private */
        public void start() {
            if (!this.mIsActive) {
                this.mIsActive = true;
                if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                    Log.v(BaseWifiTracker.this.mTag, "Scanner start");
                }
                postScan();
            }
        }

        /* access modifiers changed from: private */
        public void stop() {
            this.mIsActive = false;
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Scanner stop");
            }
            this.mRetry = 0;
            removeCallbacksAndMessages((Object) null);
        }

        /* access modifiers changed from: private */
        public void postScan() {
            if (BaseWifiTracker.this.mWifiManager.startScan()) {
                this.mRetry = 0;
            } else {
                int i = this.mRetry + 1;
                this.mRetry = i;
                if (i >= 3) {
                    if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                        Log.v(BaseWifiTracker.this.mTag, "Scanner failed to start scan " + this.mRetry + " times!");
                    }
                    this.mRetry = 0;
                    return;
                }
            }
            postDelayed(new BaseWifiTracker$Scanner$$ExternalSyntheticLambda0(this), BaseWifiTracker.this.mScanIntervalMillis);
        }
    }

    /* access modifiers changed from: private */
    public void notifyOnWifiStateChanged() {
        BaseWifiTrackerCallback baseWifiTrackerCallback = this.mListener;
        if (baseWifiTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(baseWifiTrackerCallback);
            handler.post(new BaseWifiTracker$$ExternalSyntheticLambda1(baseWifiTrackerCallback));
        }
    }
}
