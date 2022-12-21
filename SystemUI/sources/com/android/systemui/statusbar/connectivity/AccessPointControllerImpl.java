package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IndentingPrintWriter;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.p026io.PrintWriter;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class AccessPointControllerImpl implements AccessPointController, WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String EXTRA_START_CONNECT_SSID = "wifi_start_connect_ssid";
    private static final int[] ICONS = WifiIcons.WIFI_FULL_ICONS;
    private static final String TAG = "AccessPointController";
    private final ArrayList<AccessPointController.AccessPointCallback> mCallbacks = new ArrayList<>();
    private final WifiEntry.ConnectCallback mConnectCallback = new WifiEntry.ConnectCallback() {
        public void onConnectResult(int i) {
            if (i == 0) {
                if (AccessPointControllerImpl.DEBUG) {
                    Log.d(AccessPointControllerImpl.TAG, "connect success");
                }
            } else if (AccessPointControllerImpl.DEBUG) {
                Log.d(AccessPointControllerImpl.TAG, "connect failure reason=" + i);
            }
        }
    };
    private int mCurrentUser;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final Executor mMainExecutor;
    private final UserManager mUserManager;
    private final UserTracker mUserTracker;
    private WifiPickerTracker mWifiPickerTracker;
    private WifiPickerTrackerFactory mWifiPickerTrackerFactory;

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public AccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        this.mUserManager = userManager;
        this.mUserTracker = userTracker;
        this.mCurrentUser = userTracker.getUserId();
        this.mMainExecutor = executor;
        this.mWifiPickerTrackerFactory = wifiPickerTrackerFactory;
        executor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-connectivity-AccessPointControllerImpl */
    public /* synthetic */ void mo39282x4d42dfc9() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void init() {
        if (this.mWifiPickerTracker == null) {
            this.mWifiPickerTracker = this.mWifiPickerTrackerFactory.create(getLifecycle(), this);
        }
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda0(this));
        super.finalize();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$finalize$1$com-android-systemui-statusbar-connectivity-AccessPointControllerImpl */
    public /* synthetic */ void mo39281xdbd89022() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public boolean canConfigWifi() {
        if (!this.mWifiPickerTrackerFactory.isSupported()) {
            return false;
        }
        return !this.mUserManager.hasUserRestriction("no_config_wifi", new UserHandle(this.mCurrentUser));
    }

    public boolean canConfigMobileData() {
        return !this.mUserManager.hasUserRestriction("no_config_mobile_networks", UserHandle.of(this.mCurrentUser)) && this.mUserTracker.getUserInfo().isAdmin();
    }

    /* access modifiers changed from: package-private */
    public void onUserSwitched(int i) {
        this.mCurrentUser = i;
    }

    public void addAccessPointCallback(AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null && !this.mCallbacks.contains(accessPointCallback)) {
            if (DEBUG) {
                Log.d(TAG, "addCallback " + accessPointCallback);
            }
            this.mCallbacks.add(accessPointCallback);
            if (this.mCallbacks.size() == 1) {
                this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda3(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addAccessPointCallback$2$com-android-systemui-statusbar-connectivity-AccessPointControllerImpl */
    public /* synthetic */ void mo39280xce7d1f15() {
        this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
    }

    public void removeAccessPointCallback(AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null) {
            if (DEBUG) {
                Log.d(TAG, "removeCallback " + accessPointCallback);
            }
            this.mCallbacks.remove((Object) accessPointCallback);
            if (this.mCallbacks.isEmpty()) {
                this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda1(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeAccessPointCallback$3$com-android-systemui-statusbar-connectivity-AccessPointControllerImpl */
    public /* synthetic */ void mo39283x2005fe79() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void scanForAccessPoints() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker == null) {
            fireAccessPointsCallback(Collections.emptyList());
            return;
        }
        List<WifiEntry> wifiEntries = wifiPickerTracker.getWifiEntries();
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null) {
            wifiEntries.add(0, connectedWifiEntry);
        }
        fireAccessPointsCallback(wifiEntries);
    }

    public MergedCarrierEntry getMergedCarrierEntry() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker != null) {
            return wifiPickerTracker.getMergedCarrierEntry();
        }
        fireAccessPointsCallback(Collections.emptyList());
        return null;
    }

    public int getIcon(WifiEntry wifiEntry) {
        return ICONS[Math.max(0, wifiEntry.getLevel())];
    }

    public boolean connect(WifiEntry wifiEntry) {
        if (wifiEntry == null) {
            return false;
        }
        if (DEBUG) {
            if (wifiEntry.getWifiConfiguration() != null) {
                Log.d(TAG, "connect networkId=" + wifiEntry.getWifiConfiguration().networkId);
            } else {
                Log.d(TAG, "connect to unsaved network " + wifiEntry.getTitle());
            }
        }
        if (wifiEntry.isSaved()) {
            wifiEntry.connect(this.mConnectCallback);
        } else if (wifiEntry.getSecurity() != 0) {
            Intent intent = new Intent("android.settings.WIFI_SETTINGS");
            intent.putExtra(EXTRA_START_CONNECT_SSID, wifiEntry.getSsid());
            intent.addFlags(268435456);
            fireSettingsIntentCallback(intent);
            return true;
        } else {
            wifiEntry.connect(this.mConnectCallback);
        }
        return false;
    }

    private void fireSettingsIntentCallback(Intent intent) {
        Iterator<AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onSettingsActivityTriggered(intent);
        }
    }

    private void fireAccessPointsCallback(List<WifiEntry> list) {
        Iterator<AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onAccessPointsChanged(list);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.println("AccessPointControllerImpl:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Callbacks: " + Arrays.toString(this.mCallbacks.toArray()));
        indentingPrintWriter.println("WifiPickerTracker: " + this.mWifiPickerTracker.toString());
        if (this.mWifiPickerTracker != null && !this.mCallbacks.isEmpty()) {
            indentingPrintWriter.println("Connected: " + this.mWifiPickerTracker.getConnectedWifiEntry());
            indentingPrintWriter.println("Other wifi entries: " + Arrays.toString(this.mWifiPickerTracker.getWifiEntries().toArray()));
        } else if (this.mWifiPickerTracker != null) {
            indentingPrintWriter.println("WifiPickerTracker not started, cannot get reliable entries");
        }
        indentingPrintWriter.decreaseIndent();
    }

    public void onWifiStateChanged() {
        scanForAccessPoints();
    }

    public void onWifiEntriesChanged() {
        scanForAccessPoints();
    }

    @SysUISingleton
    public static class WifiPickerTrackerFactory {
        private static final long MAX_SCAN_AGE_MILLIS = 15000;
        private static final long SCAN_INTERVAL_MILLIS = 10000;
        private final Clock mClock = new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        private final ConnectivityManager mConnectivityManager;
        private final Context mContext;
        private final Handler mMainHandler;
        private final WifiManager mWifiManager;
        private final Handler mWorkerHandler;

        /* JADX WARNING: type inference failed for: r0v0, types: [java.time.Clock, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory$1] */
        @Inject
        public WifiPickerTrackerFactory(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, @Main Handler handler, @Background Handler handler2) {
            this.mContext = context;
            this.mWifiManager = wifiManager;
            this.mConnectivityManager = connectivityManager;
            this.mMainHandler = handler;
            this.mWorkerHandler = handler2;
        }

        /* access modifiers changed from: private */
        public boolean isSupported() {
            return this.mWifiManager != null;
        }

        public WifiPickerTracker create(Lifecycle lifecycle, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
            if (this.mWifiManager == null) {
                return null;
            }
            return new WifiPickerTracker(lifecycle, this.mContext, this.mWifiManager, this.mConnectivityManager, this.mMainHandler, this.mWorkerHandler, this.mClock, MAX_SCAN_AGE_MILLIS, SCAN_INTERVAL_MILLIS, wifiPickerTrackerCallback);
        }
    }
}
