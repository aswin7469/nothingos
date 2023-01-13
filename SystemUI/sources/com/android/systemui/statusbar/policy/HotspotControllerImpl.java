package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.util.ConcurrentUtils;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.HotspotController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@SysUISingleton
public class HotspotControllerImpl implements HotspotController, WifiManager.SoftApCallback {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "HotspotController";
    private final ArrayList<HotspotController.Callback> mCallbacks = new ArrayList<>();
    private final Context mContext;
    /* access modifiers changed from: private */
    public volatile boolean mHasTetherableWifiRegexs = true;
    private int mHotspotState;
    /* access modifiers changed from: private */
    public volatile boolean mIsTetheringSupported = true;
    private final Handler mMainHandler;
    private volatile int mNumConnectedDevices;
    private TetheringManager.TetheringEventCallback mTetheringCallback = new TetheringManager.TetheringEventCallback() {
        public void onTetheringSupported(boolean z) {
            if (HotspotControllerImpl.this.mIsTetheringSupported != z) {
                boolean unused = HotspotControllerImpl.this.mIsTetheringSupported = z;
                HotspotControllerImpl.this.fireHotspotAvailabilityChanged();
            }
        }

        public void onTetherableInterfaceRegexpsChanged(TetheringManager.TetheringInterfaceRegexps tetheringInterfaceRegexps) {
            boolean z = tetheringInterfaceRegexps.getTetherableWifiRegexs().size() != 0;
            if (HotspotControllerImpl.this.mHasTetherableWifiRegexs != z) {
                boolean unused = HotspotControllerImpl.this.mHasTetherableWifiRegexs = z;
                HotspotControllerImpl.this.fireHotspotAvailabilityChanged();
            }
        }
    };
    private final TetheringManager mTetheringManager;
    private boolean mWaitingForTerminalState;
    private final WifiManager mWifiManager;

    private static String stateToString(int i) {
        switch (i) {
            case 10:
                return "DISABLING";
            case 11:
                return "DISABLED";
            case 12:
                return "ENABLING";
            case 13:
                return "ENABLED";
            case 14:
                return "FAILED";
            default:
                return null;
        }
    }

    public int getHotspotWifiStandard() {
        return 1;
    }

    @Inject
    public HotspotControllerImpl(Context context, @Main Handler handler, @Background Handler handler2, DumpManager dumpManager) {
        this.mContext = context;
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mTetheringManager = tetheringManager;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mMainHandler = handler;
        tetheringManager.registerTetheringEventCallback(new HandlerExecutor(handler2), this.mTetheringCallback);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public boolean isHotspotSupported() {
        return this.mIsTetheringSupported && this.mHasTetherableWifiRegexs && UserManager.get(this.mContext).isUserAdmin(ActivityManager.getCurrentUser());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("HotspotController state:");
        printWriter.print("  available=");
        printWriter.println(isHotspotSupported());
        printWriter.print("  mHotspotState=");
        printWriter.println(stateToString(this.mHotspotState));
        printWriter.print("  mNumConnectedDevices=");
        printWriter.println(this.mNumConnectedDevices);
        printWriter.print("  mWaitingForTerminalState=");
        printWriter.println(this.mWaitingForTerminalState);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0050, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addCallback(com.android.systemui.statusbar.policy.HotspotController.Callback r5) {
        /*
            r4 = this;
            java.lang.String r0 = "addCallback "
            java.util.ArrayList<com.android.systemui.statusbar.policy.HotspotController$Callback> r1 = r4.mCallbacks
            monitor-enter(r1)
            if (r5 == 0) goto L_0x0051
            java.util.ArrayList<com.android.systemui.statusbar.policy.HotspotController$Callback> r2 = r4.mCallbacks     // Catch:{ all -> 0x0053 }
            boolean r2 = r2.contains(r5)     // Catch:{ all -> 0x0053 }
            if (r2 == 0) goto L_0x0010
            goto L_0x0051
        L_0x0010:
            boolean r2 = DEBUG     // Catch:{ all -> 0x0053 }
            if (r2 == 0) goto L_0x0026
            java.lang.String r2 = "HotspotController"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0053 }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0053 }
            java.lang.StringBuilder r0 = r3.append((java.lang.Object) r5)     // Catch:{ all -> 0x0053 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0053 }
            android.util.Log.d(r2, r0)     // Catch:{ all -> 0x0053 }
        L_0x0026:
            java.util.ArrayList<com.android.systemui.statusbar.policy.HotspotController$Callback> r0 = r4.mCallbacks     // Catch:{ all -> 0x0053 }
            r0.add(r5)     // Catch:{ all -> 0x0053 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x004f
            java.util.ArrayList<com.android.systemui.statusbar.policy.HotspotController$Callback> r0 = r4.mCallbacks     // Catch:{ all -> 0x0053 }
            int r0 = r0.size()     // Catch:{ all -> 0x0053 }
            r2 = 1
            if (r0 != r2) goto L_0x0045
            android.net.wifi.WifiManager r5 = r4.mWifiManager     // Catch:{ all -> 0x0053 }
            android.os.HandlerExecutor r0 = new android.os.HandlerExecutor     // Catch:{ all -> 0x0053 }
            android.os.Handler r2 = r4.mMainHandler     // Catch:{ all -> 0x0053 }
            r0.<init>(r2)     // Catch:{ all -> 0x0053 }
            r5.registerSoftApCallback(r0, r4)     // Catch:{ all -> 0x0053 }
            goto L_0x004f
        L_0x0045:
            android.os.Handler r0 = r4.mMainHandler     // Catch:{ all -> 0x0053 }
            com.android.systemui.statusbar.policy.HotspotControllerImpl$$ExternalSyntheticLambda0 r2 = new com.android.systemui.statusbar.policy.HotspotControllerImpl$$ExternalSyntheticLambda0     // Catch:{ all -> 0x0053 }
            r2.<init>(r4, r5)     // Catch:{ all -> 0x0053 }
            r0.post(r2)     // Catch:{ all -> 0x0053 }
        L_0x004f:
            monitor-exit(r1)     // Catch:{ all -> 0x0053 }
            return
        L_0x0051:
            monitor-exit(r1)     // Catch:{ all -> 0x0053 }
            return
        L_0x0053:
            r4 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0053 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.HotspotControllerImpl.addCallback(com.android.systemui.statusbar.policy.HotspotController$Callback):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCallback$0$com-android-systemui-statusbar-policy-HotspotControllerImpl */
    public /* synthetic */ void mo45819xf425eaad(HotspotController.Callback callback) {
        callback.onHotspotChanged(isHotspotEnabled(), this.mNumConnectedDevices, getHotspotWifiStandard());
    }

    public void removeCallback(HotspotController.Callback callback) {
        WifiManager wifiManager;
        if (callback != null) {
            if (DEBUG) {
                Log.d(TAG, "removeCallback " + callback);
            }
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove((Object) callback);
                if (this.mCallbacks.isEmpty() && (wifiManager = this.mWifiManager) != null) {
                    wifiManager.unregisterSoftApCallback(this);
                }
            }
        }
    }

    public boolean isHotspotEnabled() {
        return this.mHotspotState == 13;
    }

    public boolean isHotspotTransient() {
        return this.mWaitingForTerminalState || this.mHotspotState == 12;
    }

    public void setHotspotEnabled(boolean z) {
        if (this.mWaitingForTerminalState) {
            if (DEBUG) {
                Log.d(TAG, "Ignoring setHotspotEnabled; waiting for terminal state.");
            }
        } else if (z) {
            this.mWaitingForTerminalState = true;
            if (DEBUG) {
                Log.d(TAG, "Starting tethering");
            }
            this.mTetheringManager.startTethering(new TetheringManager.TetheringRequest.Builder(0).setShouldShowEntitlementUi(false).build(), ConcurrentUtils.DIRECT_EXECUTOR, (TetheringManager.StartTetheringCallback) new TetheringManager.StartTetheringCallback() {
                public void onTetheringFailed(int i) {
                    if (HotspotControllerImpl.DEBUG) {
                        Log.d(HotspotControllerImpl.TAG, "onTetheringFailed");
                    }
                    HotspotControllerImpl.this.maybeResetSoftApState();
                    HotspotControllerImpl.this.fireHotspotChangedCallback();
                }
            });
        } else {
            this.mTetheringManager.stopTethering(0);
        }
    }

    public int getNumConnectedDevices() {
        return this.mNumConnectedDevices;
    }

    /* access modifiers changed from: private */
    public void fireHotspotChangedCallback() {
        ArrayList<HotspotController.Callback> arrayList;
        synchronized (this.mCallbacks) {
            arrayList = new ArrayList<>(this.mCallbacks);
        }
        for (HotspotController.Callback onHotspotChanged : arrayList) {
            onHotspotChanged.onHotspotChanged(isHotspotEnabled(), this.mNumConnectedDevices, getHotspotWifiStandard());
        }
    }

    /* access modifiers changed from: private */
    public void fireHotspotAvailabilityChanged() {
        ArrayList<HotspotController.Callback> arrayList;
        synchronized (this.mCallbacks) {
            arrayList = new ArrayList<>(this.mCallbacks);
        }
        for (HotspotController.Callback onHotspotAvailabilityChanged : arrayList) {
            onHotspotAvailabilityChanged.onHotspotAvailabilityChanged(isHotspotSupported());
        }
    }

    public void onStateChanged(int i, int i2) {
        this.mHotspotState = i;
        maybeResetSoftApState();
        if (!isHotspotEnabled()) {
            this.mNumConnectedDevices = 0;
        }
        fireHotspotChangedCallback();
    }

    /* access modifiers changed from: private */
    public void maybeResetSoftApState() {
        if (this.mWaitingForTerminalState) {
            int i = this.mHotspotState;
            if (!(i == 11 || i == 13)) {
                if (i == 14) {
                    this.mTetheringManager.stopTethering(0);
                } else {
                    return;
                }
            }
            this.mWaitingForTerminalState = false;
        }
    }

    public void onConnectedClientsChanged(List<WifiClient> list) {
        this.mNumConnectedDevices = list.size();
        fireHotspotChangedCallback();
    }
}
