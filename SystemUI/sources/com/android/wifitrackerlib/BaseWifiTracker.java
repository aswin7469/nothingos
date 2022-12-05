package com.android.wifitrackerlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkKey;
import android.net.NetworkRequest;
import android.net.NetworkScoreManager;
import android.net.ScoredNetwork;
import android.net.TransportInfo;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.wifitrackerlib.BaseWifiTracker;
import java.time.Clock;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public class BaseWifiTracker implements LifecycleObserver {
    private static boolean sVerboseLogging;
    protected final ConnectivityManager mConnectivityManager;
    protected final Context mContext;
    protected boolean mIsCellDefaultRoute;
    private boolean mIsStarted;
    protected boolean mIsWifiDefaultRoute;
    protected boolean mIsWifiValidated;
    private final BaseWifiTrackerCallback mListener;
    protected final Handler mMainHandler;
    protected final long mMaxScanAgeMillis;
    protected final NetworkScoreManager mNetworkScoreManager;
    protected final long mScanIntervalMillis;
    protected final ScanResultUpdater mScanResultUpdater;
    private final Scanner mScanner;
    private final String mTag;
    protected final WifiManager mWifiManager;
    protected final WifiNetworkScoreCache mWifiNetworkScoreCache;
    protected final Handler mWorkerHandler;
    private int mCachedWifiState = 4;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.wifitrackerlib.BaseWifiTracker.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            String action = intent.getAction();
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                String str = BaseWifiTracker.this.mTag;
                Log.v(str, "Received broadcast: " + action);
            }
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                if (intent.getIntExtra("wifi_state", 4) == 3) {
                    BaseWifiTracker.this.mCachedWifiState = 3;
                    BaseWifiTracker.this.mScanner.start();
                } else {
                    BaseWifiTracker.this.mCachedWifiState = 1;
                    BaseWifiTracker.this.mScanner.stop();
                }
                BaseWifiTracker.this.notifyOnWifiStateChanged();
                BaseWifiTracker.this.handleWifiStateChangedAction();
            } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
                NetworkScoreManager networkScoreManager = baseWifiTracker.mNetworkScoreManager;
                Stream<R> map = baseWifiTracker.mWifiManager.getScanResults().stream().map(BaseWifiTracker$1$$ExternalSyntheticLambda0.INSTANCE);
                final Set set = BaseWifiTracker.this.mRequestedScoreKeys;
                Objects.requireNonNull(set);
                networkScoreManager.requestScores((Collection) map.filter(new Predicate() { // from class: com.android.wifitrackerlib.BaseWifiTracker$1$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return set.add((NetworkKey) obj);
                    }
                }).collect(Collectors.toList()));
                BaseWifiTracker.this.handleScanResultsAvailableAction(intent);
            } else if ("com.qualcomm.qti.net.wifi.PARTIAL_SCAN_RESULTS".equals(action)) {
                BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
                NetworkScoreManager networkScoreManager2 = baseWifiTracker2.mNetworkScoreManager;
                Stream<R> map2 = baseWifiTracker2.mWifiManager.getScanResults().stream().map(BaseWifiTracker$1$$ExternalSyntheticLambda0.INSTANCE);
                final Set set2 = BaseWifiTracker.this.mRequestedScoreKeys;
                Objects.requireNonNull(set2);
                networkScoreManager2.requestScores((Collection) map2.filter(new Predicate() { // from class: com.android.wifitrackerlib.BaseWifiTracker$1$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return set2.add((NetworkKey) obj);
                    }
                }).collect(Collectors.toList()));
                BaseWifiTracker.this.handleScanResultsAvailableAction(intent);
            } else if ("android.net.wifi.CONFIGURED_NETWORKS_CHANGE".equals(action)) {
                BaseWifiTracker.this.handleConfiguredNetworksChangedAction(intent);
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                BaseWifiTracker.this.handleNetworkStateChangedAction(intent);
            } else if ("android.net.wifi.RSSI_CHANGED".equals(action)) {
                BaseWifiTracker.this.handleRssiChangedAction();
            } else if (!"android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
            } else {
                BaseWifiTracker.this.handleDefaultSubscriptionChanged(intent.getIntExtra("subscription", -1));
            }
        }
    };
    private final Set<NetworkKey> mRequestedScoreKeys = new HashSet();
    private final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.wifitrackerlib.BaseWifiTracker.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.isPrimaryWifiNetwork(baseWifiTracker.mConnectivityManager.getNetworkCapabilities(network))) {
                return;
            }
            BaseWifiTracker.this.handleLinkPropertiesChanged(linkProperties);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            if (!BaseWifiTracker.this.isPrimaryWifiNetwork(networkCapabilities)) {
                return;
            }
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            boolean z = baseWifiTracker.mIsWifiValidated;
            baseWifiTracker.mIsWifiValidated = networkCapabilities.hasCapability(16);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
                if (baseWifiTracker2.mIsWifiValidated != z) {
                    String str = baseWifiTracker2.mTag;
                    Log.v(str, "Is Wifi validated: " + BaseWifiTracker.this.mIsWifiValidated);
                }
            }
            BaseWifiTracker.this.handleNetworkCapabilitiesChanged(networkCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.isPrimaryWifiNetwork(baseWifiTracker.mConnectivityManager.getNetworkCapabilities(network))) {
                return;
            }
            BaseWifiTracker.this.mIsWifiValidated = false;
        }
    };
    private final ConnectivityManager.NetworkCallback mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.wifitrackerlib.BaseWifiTracker.3
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            boolean z = true;
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            boolean z2 = baseWifiTracker.mIsWifiDefaultRoute;
            boolean z3 = baseWifiTracker.mIsCellDefaultRoute;
            VcnTransportInfo transportInfo = networkCapabilities.getTransportInfo();
            BaseWifiTracker.this.mIsWifiDefaultRoute = networkCapabilities.hasTransport(1) || ((transportInfo == null || !(transportInfo instanceof VcnTransportInfo) || transportInfo.getWifiInfo() == null) ? false : true);
            BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
            if (baseWifiTracker2.mIsWifiDefaultRoute || !networkCapabilities.hasTransport(0)) {
                z = false;
            }
            baseWifiTracker2.mIsCellDefaultRoute = z;
            BaseWifiTracker baseWifiTracker3 = BaseWifiTracker.this;
            if (baseWifiTracker3.mIsWifiDefaultRoute == z2 && baseWifiTracker3.mIsCellDefaultRoute == z3) {
                return;
            }
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Wifi is the default route: " + BaseWifiTracker.this.mIsWifiDefaultRoute);
                Log.v(BaseWifiTracker.this.mTag, "Cell is the default route: " + BaseWifiTracker.this.mIsCellDefaultRoute);
            }
            BaseWifiTracker.this.handleDefaultRouteChanged();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            if (!BaseWifiTracker.this.mIsStarted) {
                BaseWifiTracker.this.mIsStarted = true;
                BaseWifiTracker.this.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            baseWifiTracker.mIsWifiDefaultRoute = false;
            baseWifiTracker.mIsCellDefaultRoute = false;
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Wifi is the default route: false");
                Log.v(BaseWifiTracker.this.mTag, "Cell is the default route: false");
            }
            BaseWifiTracker.this.handleDefaultRouteChanged();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public interface BaseWifiTrackerCallback {
        void onWifiStateChanged();
    }

    protected void handleConfiguredNetworksChangedAction(Intent intent) {
    }

    protected void handleDefaultRouteChanged() {
    }

    protected void handleDefaultSubscriptionChanged(int i) {
    }

    protected void handleLinkPropertiesChanged(LinkProperties linkProperties) {
    }

    protected void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
    }

    protected void handleNetworkScoreCacheUpdated() {
    }

    protected void handleNetworkStateChangedAction(Intent intent) {
    }

    protected void handleOnStart() {
    }

    protected void handleRssiChangedAction() {
    }

    protected void handleScanResultsAvailableAction(Intent intent) {
    }

    protected void handleWifiStateChangedAction() {
    }

    public static boolean isVerboseLoggingEnabled() {
        return sVerboseLogging;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPrimaryWifiNetwork(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return false;
        }
        TransportInfo transportInfo = networkCapabilities.getTransportInfo();
        if (transportInfo instanceof WifiInfo) {
            return ((WifiInfo) transportInfo).isPrimary();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseWifiTracker(Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2, Clock clock, long j, long j2, BaseWifiTrackerCallback baseWifiTrackerCallback, String str) {
        lifecycle.addObserver(this);
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mConnectivityManager = connectivityManager;
        this.mNetworkScoreManager = networkScoreManager;
        this.mMainHandler = handler;
        this.mWorkerHandler = handler2;
        this.mMaxScanAgeMillis = j;
        this.mScanIntervalMillis = j2;
        this.mListener = baseWifiTrackerCallback;
        this.mTag = str;
        this.mScanResultUpdater = new ScanResultUpdater(clock, j + j2);
        this.mWifiNetworkScoreCache = new WifiNetworkScoreCache(context, new WifiNetworkScoreCache.CacheListener(handler2) { // from class: com.android.wifitrackerlib.BaseWifiTracker.4
            public void networkCacheUpdated(List<ScoredNetwork> list) {
                BaseWifiTracker.this.handleNetworkScoreCacheUpdated();
            }
        });
        this.mScanner = new Scanner(handler2.getLooper());
        sVerboseLogging = wifiManager.isVerboseLoggingEnabled();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("com.qualcomm.qti.net.wifi.PARTIAL_SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.CONFIGURED_NETWORKS_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.RSSI_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, null, this.mWorkerHandler);
        this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback, this.mWorkerHandler);
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mWorkerHandler);
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
            String str = this.mTag;
            Log.v(str, "Wifi is the default route: " + this.mIsWifiDefaultRoute);
            String str2 = this.mTag;
            Log.v(str2, "Cell is the default route: " + this.mIsCellDefaultRoute);
        }
        this.mNetworkScoreManager.registerNetworkScoreCache(1, this.mWifiNetworkScoreCache, 2);
        this.mWorkerHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BaseWifiTracker.this.lambda$onResume$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$0() {
        if (!this.mIsStarted) {
            this.mIsStarted = true;
            handleOnStart();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Handler handler = this.mWorkerHandler;
        final Scanner scanner = this.mScanner;
        Objects.requireNonNull(scanner);
        handler.post(new Runnable() { // from class: com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BaseWifiTracker.Scanner.access$500(BaseWifiTracker.Scanner.this);
            }
        });
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mDefaultNetworkCallback);
        this.mNetworkScoreManager.unregisterNetworkScoreCache(1, this.mWifiNetworkScoreCache);
        Handler handler2 = this.mWorkerHandler;
        final Set<NetworkKey> set = this.mRequestedScoreKeys;
        Objects.requireNonNull(set);
        handler2.post(new Runnable() { // from class: com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                set.clear();
            }
        });
        this.mIsStarted = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class Scanner extends Handler {
        private boolean mIsActive;
        private int mRetry;

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void access$500(Scanner scanner) {
            scanner.stop();
        }

        private Scanner(Looper looper) {
            super(looper);
            this.mRetry = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start() {
            if (!this.mIsActive) {
                this.mIsActive = true;
                if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                    Log.v(BaseWifiTracker.this.mTag, "Scanner start");
                }
                postScan();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            this.mIsActive = false;
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v(BaseWifiTracker.this.mTag, "Scanner stop");
            }
            this.mRetry = 0;
            removeCallbacksAndMessages(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postScan() {
            if (BaseWifiTracker.this.mWifiManager.startScan()) {
                this.mRetry = 0;
            } else {
                int i = this.mRetry + 1;
                this.mRetry = i;
                if (i >= 3) {
                    if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                        String str = BaseWifiTracker.this.mTag;
                        Log.v(str, "Scanner failed to start scan " + this.mRetry + " times!");
                    }
                    this.mRetry = 0;
                    return;
                }
            }
            postDelayed(new Runnable() { // from class: com.android.wifitrackerlib.BaseWifiTracker$Scanner$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BaseWifiTracker.Scanner.this.postScan();
                }
            }, BaseWifiTracker.this.mScanIntervalMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnWifiStateChanged() {
        final BaseWifiTrackerCallback baseWifiTrackerCallback = this.mListener;
        if (baseWifiTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(baseWifiTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BaseWifiTracker.BaseWifiTrackerCallback.this.onWifiStateChanged();
                }
            });
        }
    }
}
