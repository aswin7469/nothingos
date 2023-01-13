package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.quicksettings.IQSService;
import android.service.quicksettings.Tile;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.TileServices */
public class TileServices extends IQSService.Stub {
    static final int DEFAULT_MAX_BOUND = 3;
    static final int REDUCED_MAX_BOUND = 1;
    private static final Comparator<TileServiceManager> SERVICE_SORT = new Comparator<TileServiceManager>() {
        public int compare(TileServiceManager tileServiceManager, TileServiceManager tileServiceManager2) {
            return -Integer.compare(tileServiceManager.getBindPriority(), tileServiceManager2.getBindPriority());
        }
    };
    private static final String TAG = "TileServices";
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private final Provider<Handler> mHandlerProvider;
    /* access modifiers changed from: private */
    public final QSTileHost mHost;
    private final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public final Handler mMainHandler;
    private int mMaxBound = 3;
    private final CommandQueue.Callbacks mRequestListeningCallback;
    private final ArrayMap<CustomTile, TileServiceManager> mServices = new ArrayMap<>();
    private final ArrayMap<ComponentName, CustomTile> mTiles = new ArrayMap<>();
    private final ArrayMap<IBinder, CustomTile> mTokenMap = new ArrayMap<>();
    private final UserTracker mUserTracker;

    @Inject
    public TileServices(QSTileHost qSTileHost, @Main Provider<Handler> provider, BroadcastDispatcher broadcastDispatcher, UserTracker userTracker, KeyguardStateController keyguardStateController, CommandQueue commandQueue) {
        C23822 r0 = new CommandQueue.Callbacks() {
            /* access modifiers changed from: package-private */
            /* renamed from: lambda$requestTileServiceListeningState$0$com-android-systemui-qs-external-TileServices$2 */
            public /* synthetic */ void mo36687x1d2c1004(ComponentName componentName) {
                TileServices.this.requestListening(componentName);
            }

            public void requestTileServiceListeningState(ComponentName componentName) {
                TileServices.this.mMainHandler.post(new TileServices$2$$ExternalSyntheticLambda0(this, componentName));
            }
        };
        this.mRequestListeningCallback = r0;
        this.mHost = qSTileHost;
        this.mKeyguardStateController = keyguardStateController;
        this.mContext = qSTileHost.getContext();
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mHandlerProvider = provider;
        this.mMainHandler = provider.get();
        this.mUserTracker = userTracker;
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) r0);
    }

    public Context getContext() {
        return this.mContext;
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public TileServiceManager getTileWrapper(CustomTile customTile) {
        ComponentName component = customTile.getComponent();
        TileServiceManager onCreateTileService = onCreateTileService(component, this.mBroadcastDispatcher);
        synchronized (this.mServices) {
            this.mServices.put(customTile, onCreateTileService);
            this.mTiles.put(component, customTile);
            this.mTokenMap.put(onCreateTileService.getToken(), customTile);
        }
        onCreateTileService.startLifecycleManagerAndAddTile();
        return onCreateTileService;
    }

    /* access modifiers changed from: protected */
    public TileServiceManager onCreateTileService(ComponentName componentName, BroadcastDispatcher broadcastDispatcher) {
        return new TileServiceManager(this, this.mHandlerProvider.get(), componentName, broadcastDispatcher, this.mUserTracker);
    }

    public void freeService(CustomTile customTile, TileServiceManager tileServiceManager) {
        synchronized (this.mServices) {
            tileServiceManager.setBindAllowed(false);
            tileServiceManager.handleDestroy();
            this.mServices.remove(customTile);
            this.mTokenMap.remove(tileServiceManager.getToken());
            this.mTiles.remove(customTile.getComponent());
            this.mMainHandler.post(new TileServices$$ExternalSyntheticLambda1(this, customTile.getComponent().getClassName()));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$freeService$0$com-android-systemui-qs-external-TileServices */
    public /* synthetic */ void mo36676xe19e37(String str) {
        this.mHost.getIconController().removeAllIconsForSlot(str);
    }

    public void setMemoryPressure(boolean z) {
        this.mMaxBound = z ? 1 : 3;
        recalculateBindAllowance();
    }

    public void recalculateBindAllowance() {
        ArrayList arrayList;
        synchronized (this.mServices) {
            arrayList = new ArrayList(this.mServices.values());
        }
        int size = arrayList.size();
        if (size > this.mMaxBound) {
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                ((TileServiceManager) arrayList.get(i)).calculateBindPriority(currentTimeMillis);
            }
            Collections.sort(arrayList, SERVICE_SORT);
        }
        int i2 = 0;
        while (i2 < this.mMaxBound && i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(true);
            i2++;
        }
        while (i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(false);
            i2++;
        }
    }

    private void verifyCaller(CustomTile customTile) {
        try {
            if (Binder.getCallingUid() != this.mContext.getPackageManager().getPackageUidAsUser(customTile.getComponent().getPackageName(), Binder.getCallingUserHandle().getIdentifier())) {
                throw new SecurityException("Component outside caller's uid");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new SecurityException((Throwable) e);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:17|18|19|20|21|22) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0056 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestListening(android.content.ComponentName r5) {
        /*
            r4 = this;
            java.lang.String r0 = "No TileServiceManager found in requestListening for tile "
            java.lang.String r1 = "Couldn't find tile for "
            android.util.ArrayMap<com.android.systemui.qs.external.CustomTile, com.android.systemui.qs.external.TileServiceManager> r2 = r4.mServices
            monitor-enter(r2)
            com.android.systemui.qs.external.CustomTile r3 = r4.getTileForComponent(r5)     // Catch:{ all -> 0x0058 }
            if (r3 != 0) goto L_0x0021
            java.lang.String r4 = "TileServices"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0058 }
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0058 }
            java.lang.StringBuilder r5 = r0.append((java.lang.Object) r5)     // Catch:{ all -> 0x0058 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0058 }
            android.util.Log.d(r4, r5)     // Catch:{ all -> 0x0058 }
            monitor-exit(r2)     // Catch:{ all -> 0x0058 }
            return
        L_0x0021:
            android.util.ArrayMap<com.android.systemui.qs.external.CustomTile, com.android.systemui.qs.external.TileServiceManager> r4 = r4.mServices     // Catch:{ all -> 0x0058 }
            java.lang.Object r4 = r4.get(r3)     // Catch:{ all -> 0x0058 }
            com.android.systemui.qs.external.TileServiceManager r4 = (com.android.systemui.p012qs.external.TileServiceManager) r4     // Catch:{ all -> 0x0058 }
            if (r4 != 0) goto L_0x0043
            java.lang.String r4 = "TileServices"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0058 }
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0058 }
            java.lang.String r0 = r3.getTileSpec()     // Catch:{ all -> 0x0058 }
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r0)     // Catch:{ all -> 0x0058 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0058 }
            android.util.Log.e(r4, r5)     // Catch:{ all -> 0x0058 }
            monitor-exit(r2)     // Catch:{ all -> 0x0058 }
            return
        L_0x0043:
            boolean r5 = r4.isActiveTile()     // Catch:{ all -> 0x0058 }
            if (r5 != 0) goto L_0x004b
            monitor-exit(r2)     // Catch:{ all -> 0x0058 }
            return
        L_0x004b:
            r5 = 1
            r4.setBindRequested(r5)     // Catch:{ all -> 0x0058 }
            android.service.quicksettings.IQSTileService r4 = r4.getTileService()     // Catch:{ RemoteException -> 0x0056 }
            r4.onStartListening()     // Catch:{ RemoteException -> 0x0056 }
        L_0x0056:
            monitor-exit(r2)     // Catch:{ all -> 0x0058 }
            return
        L_0x0058:
            r4 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0058 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.external.TileServices.requestListening(android.content.ComponentName):void");
    }

    public void updateQsTile(Tile tile, IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null) {
                    if (tileServiceManager.isLifecycleStarted()) {
                        tileServiceManager.clearPendingBind();
                        tileServiceManager.setLastUpdate(System.currentTimeMillis());
                        tileForToken.updateTileState(tile);
                        tileForToken.refreshState();
                        return;
                    }
                }
                Log.e(TAG, "TileServiceManager not started for " + tileForToken.getComponent(), new IllegalStateException());
            }
        }
    }

    public void onStartSuccessful(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null) {
                    if (tileServiceManager.isLifecycleStarted()) {
                        tileServiceManager.clearPendingBind();
                        tileForToken.refreshState();
                        return;
                    }
                }
                Log.e(TAG, "TileServiceManager not started for " + tileForToken.getComponent(), new IllegalStateException());
            }
        }
    }

    public void onShowDialog(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.onDialogShown();
            this.mHost.forceCollapsePanels();
            ((TileServiceManager) Objects.requireNonNull(this.mServices.get(tileForToken))).setShowingDialog(true);
        }
    }

    public void onDialogHidden(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            ((TileServiceManager) Objects.requireNonNull(this.mServices.get(tileForToken))).setShowingDialog(false);
            tileForToken.onDialogHidden();
        }
    }

    public void onStartActivity(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            this.mHost.forceCollapsePanels();
        }
    }

    public void updateStatusIcon(IBinder iBinder, Icon icon, String str) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            try {
                final ComponentName component = tileForToken.getComponent();
                String packageName = component.getPackageName();
                UserHandle callingUserHandle = getCallingUserHandle();
                if (this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, callingUserHandle.getIdentifier()).applicationInfo.isSystemApp()) {
                    final StatusBarIcon statusBarIcon = icon != null ? new StatusBarIcon(callingUserHandle, packageName, icon, 0, 0, str) : null;
                    this.mMainHandler.post(new Runnable() {
                        public void run() {
                            StatusBarIconController iconController = TileServices.this.mHost.getIconController();
                            iconController.setIcon(component.getClassName(), statusBarIcon);
                            iconController.setExternalIcon(component.getClassName());
                        }
                    });
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    public Tile getTile(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken == null) {
            return null;
        }
        verifyCaller(tileForToken);
        return tileForToken.getQsTile();
    }

    public void startUnlockAndRun(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.startUnlockAndRun();
        }
    }

    public boolean isLocked() {
        return this.mKeyguardStateController.isShowing();
    }

    public boolean isSecure() {
        return this.mKeyguardStateController.isMethodSecure() && this.mKeyguardStateController.isShowing();
    }

    private CustomTile getTileForToken(IBinder iBinder) {
        CustomTile customTile;
        synchronized (this.mServices) {
            customTile = this.mTokenMap.get(iBinder);
        }
        return customTile;
    }

    private CustomTile getTileForComponent(ComponentName componentName) {
        CustomTile customTile;
        synchronized (this.mServices) {
            customTile = this.mTiles.get(componentName);
        }
        return customTile;
    }

    public void destroy() {
        synchronized (this.mServices) {
            this.mServices.values().forEach(new TileServices$$ExternalSyntheticLambda0());
        }
        this.mCommandQueue.removeCallback(this.mRequestListeningCallback);
    }
}
