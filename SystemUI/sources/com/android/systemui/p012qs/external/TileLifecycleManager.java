package com.android.systemui.p012qs.external;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quicksettings.IQSService;
import android.service.quicksettings.IQSTileService;
import android.util.ArraySet;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* renamed from: com.android.systemui.qs.external.TileLifecycleManager */
public class TileLifecycleManager extends BroadcastReceiver implements IQSTileService, ServiceConnection, IBinder.DeathRecipient {
    public static final boolean DEBUG = false;
    private static final int DEFAULT_BIND_RETRY_DELAY = 1000;
    private static final int MAX_BIND_RETRIES = 5;
    private static final int META_DATA_QUERY_FLAGS = 794752;
    private static final int MSG_ON_ADDED = 0;
    private static final int MSG_ON_CLICK = 2;
    private static final int MSG_ON_REMOVED = 1;
    private static final int MSG_ON_UNLOCK_COMPLETE = 3;
    private static final String TAG = "TileLifecycleManager";
    private static final String TILES = "tiles_prefs";
    private int mBindRetryDelay = 1000;
    private int mBindTryCount;
    /* access modifiers changed from: private */
    public boolean mBound;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private TileChangeListener mChangeListener;
    private IBinder mClickBinder;
    private final Context mContext;
    private final Handler mHandler;
    private final Intent mIntent;
    private boolean mIsBound;
    private boolean mListening;
    private final PackageManagerAdapter mPackageManagerAdapter;
    private AtomicBoolean mPackageReceiverRegistered = new AtomicBoolean(false);
    private Set<Integer> mQueuedMessages = new ArraySet();
    private final IBinder mToken;
    private boolean mUnbindImmediate;
    private final UserHandle mUser;
    private AtomicBoolean mUserReceiverRegistered = new AtomicBoolean(false);
    private QSTileServiceWrapper mWrapper;

    @AssistedFactory
    /* renamed from: com.android.systemui.qs.external.TileLifecycleManager$Factory */
    public interface Factory {
        TileLifecycleManager create(Intent intent, UserHandle userHandle);
    }

    /* renamed from: com.android.systemui.qs.external.TileLifecycleManager$TileChangeListener */
    public interface TileChangeListener {
        void onTileChanged(ComponentName componentName);
    }

    @AssistedInject
    TileLifecycleManager(@Main Handler handler, Context context, IQSService iQSService, PackageManagerAdapter packageManagerAdapter, BroadcastDispatcher broadcastDispatcher, @Assisted Intent intent, @Assisted UserHandle userHandle) {
        Binder binder = new Binder();
        this.mToken = binder;
        this.mContext = context;
        this.mHandler = handler;
        this.mIntent = intent;
        intent.putExtra(NotificationCompat.CATEGORY_SERVICE, iQSService.asBinder());
        intent.putExtra("token", binder);
        this.mUser = userHandle;
        this.mPackageManagerAdapter = packageManagerAdapter;
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    public ComponentName getComponent() {
        return this.mIntent.getComponent();
    }

    public boolean hasPendingClick() {
        boolean contains;
        synchronized (this.mQueuedMessages) {
            contains = this.mQueuedMessages.contains(2);
        }
        return contains;
    }

    public void setBindRetryDelay(int i) {
        this.mBindRetryDelay = i;
    }

    public boolean isActiveTile() {
        try {
            ServiceInfo serviceInfo = this.mPackageManagerAdapter.getServiceInfo(this.mIntent.getComponent(), META_DATA_QUERY_FLAGS);
            if (serviceInfo.metaData == null || !serviceInfo.metaData.getBoolean("android.service.quicksettings.ACTIVE_TILE", false)) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public boolean isToggleableTile() {
        try {
            ServiceInfo serviceInfo = this.mPackageManagerAdapter.getServiceInfo(this.mIntent.getComponent(), META_DATA_QUERY_FLAGS);
            if (serviceInfo.metaData == null || !serviceInfo.metaData.getBoolean("android.service.quicksettings.TOGGLEABLE_TILE", false)) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public void flushMessagesAndUnbind() {
        this.mUnbindImmediate = true;
        setBindService(true);
    }

    public void setBindService(boolean z) {
        if (!this.mBound || !this.mUnbindImmediate) {
            this.mBound = z;
            if (!z) {
                this.mBindTryCount = 0;
                freeWrapper();
                if (this.mIsBound) {
                    try {
                        this.mContext.unbindService(this);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to unbind service " + this.mIntent.getComponent().flattenToShortString(), e);
                    }
                    this.mIsBound = false;
                }
            } else if (this.mBindTryCount == 5) {
                startPackageListening();
            } else if (checkComponentState()) {
                this.mBindTryCount++;
                try {
                    boolean bindServiceAsUser = this.mContext.bindServiceAsUser(this.mIntent, this, 34603041, this.mUser);
                    this.mIsBound = bindServiceAsUser;
                    if (!bindServiceAsUser) {
                        this.mContext.unbindService(this);
                    }
                } catch (SecurityException e2) {
                    Log.e(TAG, "Failed to bind to service", e2);
                    this.mIsBound = false;
                }
            }
        } else {
            this.mUnbindImmediate = false;
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mBindTryCount = 0;
        QSTileServiceWrapper qSTileServiceWrapper = new QSTileServiceWrapper(IQSTileService.Stub.asInterface(iBinder));
        try {
            iBinder.linkToDeath(this, 0);
        } catch (RemoteException unused) {
        }
        this.mWrapper = qSTileServiceWrapper;
        handlePendingMessages();
    }

    public void onServiceDisconnected(ComponentName componentName) {
        handleDeath();
    }

    private void handlePendingMessages() {
        ArraySet arraySet;
        synchronized (this.mQueuedMessages) {
            arraySet = new ArraySet(this.mQueuedMessages);
            this.mQueuedMessages.clear();
        }
        if (arraySet.contains(0)) {
            onTileAdded();
        }
        if (this.mListening) {
            onStartListening();
        }
        if (arraySet.contains(2)) {
            if (!this.mListening) {
                Log.w(TAG, "Managed to get click on non-listening state...");
            } else {
                onClick(this.mClickBinder);
            }
        }
        if (arraySet.contains(3)) {
            if (!this.mListening) {
                Log.w(TAG, "Managed to get unlock on non-listening state...");
            } else {
                onUnlockComplete();
            }
        }
        if (arraySet.contains(1)) {
            if (this.mListening) {
                Log.w(TAG, "Managed to get remove in listening state...");
                onStopListening();
            }
            onTileRemoved();
        }
        if (this.mUnbindImmediate) {
            this.mUnbindImmediate = false;
            setBindService(false);
        }
    }

    public void handleDestroy() {
        if (this.mPackageReceiverRegistered.get() || this.mUserReceiverRegistered.get()) {
            stopPackageListening();
        }
        this.mChangeListener = null;
    }

    private void handleDeath() {
        if (this.mWrapper != null) {
            freeWrapper();
            this.mIsBound = false;
            if (this.mBound && checkComponentState()) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (TileLifecycleManager.this.mBound) {
                            TileLifecycleManager.this.setBindService(true);
                        }
                    }
                }, (long) this.mBindRetryDelay);
            }
        }
    }

    private boolean checkComponentState() {
        if (isPackageAvailable() && isComponentAvailable()) {
            return true;
        }
        startPackageListening();
        return false;
    }

    private void startPackageListening() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        try {
            this.mPackageReceiverRegistered.set(true);
            this.mContext.registerReceiverAsUser(this, this.mUser, intentFilter, (String) null, this.mHandler, 2);
        } catch (Exception e) {
            this.mPackageReceiverRegistered.set(false);
            Log.e(TAG, "Could not register package receiver", e);
        }
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.USER_UNLOCKED");
        try {
            this.mUserReceiverRegistered.set(true);
            this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter2, this.mHandler, this.mUser);
        } catch (Exception e2) {
            this.mUserReceiverRegistered.set(false);
            Log.e(TAG, "Could not register unlock receiver", e2);
        }
    }

    private void stopPackageListening() {
        if (this.mUserReceiverRegistered.compareAndSet(true, false)) {
            this.mBroadcastDispatcher.unregisterReceiver(this);
        }
        if (this.mPackageReceiverRegistered.compareAndSet(true, false)) {
            this.mContext.unregisterReceiver(this);
        }
    }

    public void setTileChangeListener(TileChangeListener tileChangeListener) {
        this.mChangeListener = tileChangeListener;
    }

    public void onReceive(Context context, Intent intent) {
        TileChangeListener tileChangeListener;
        if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || Objects.equals(intent.getData().getEncodedSchemeSpecificPart(), this.mIntent.getComponent().getPackageName())) {
            if ("android.intent.action.PACKAGE_CHANGED".equals(intent.getAction()) && (tileChangeListener = this.mChangeListener) != null) {
                tileChangeListener.onTileChanged(this.mIntent.getComponent());
            }
            stopPackageListening();
            if (this.mBound) {
                setBindService(true);
            }
        }
    }

    private boolean isComponentAvailable() {
        this.mIntent.getComponent().getPackageName();
        try {
            if (this.mPackageManagerAdapter.getServiceInfo(this.mIntent.getComponent(), 0, this.mUser.getIdentifier()) != null) {
                return true;
            }
            return false;
        } catch (RemoteException unused) {
            return false;
        }
    }

    private boolean isPackageAvailable() {
        String packageName = this.mIntent.getComponent().getPackageName();
        try {
            this.mPackageManagerAdapter.getPackageInfoAsUser(packageName, 0, this.mUser.getIdentifier());
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d(TAG, "Package not available: " + packageName);
            return false;
        }
    }

    private void queueMessage(int i) {
        synchronized (this.mQueuedMessages) {
            this.mQueuedMessages.add(Integer.valueOf(i));
        }
    }

    public void onTileAdded() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper == null || !qSTileServiceWrapper.onTileAdded()) {
            queueMessage(0);
            handleDeath();
        }
    }

    public void onTileRemoved() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper == null || !qSTileServiceWrapper.onTileRemoved()) {
            queueMessage(1);
            handleDeath();
        }
    }

    public void onStartListening() {
        this.mListening = true;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null && !qSTileServiceWrapper.onStartListening()) {
            handleDeath();
        }
    }

    public void onStopListening() {
        this.mListening = false;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null && !qSTileServiceWrapper.onStopListening()) {
            handleDeath();
        }
    }

    public void onClick(IBinder iBinder) {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper == null || !qSTileServiceWrapper.onClick(iBinder)) {
            this.mClickBinder = iBinder;
            queueMessage(2);
            handleDeath();
        }
    }

    public void onUnlockComplete() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper == null || !qSTileServiceWrapper.onUnlockComplete()) {
            queueMessage(3);
            handleDeath();
        }
    }

    public IBinder asBinder() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            return qSTileServiceWrapper.asBinder();
        }
        return null;
    }

    public void binderDied() {
        handleDeath();
    }

    public IBinder getToken() {
        return this.mToken;
    }

    private void freeWrapper() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            try {
                qSTileServiceWrapper.asBinder().unlinkToDeath(this, 0);
            } catch (NoSuchElementException unused) {
                Log.w(TAG, "Trying to unlink not linked recipient for component" + this.mIntent.getComponent().flattenToShortString());
            }
            this.mWrapper = null;
        }
    }

    public static boolean isTileAdded(Context context, ComponentName componentName) {
        return context.getSharedPreferences(TILES, 0).getBoolean(componentName.flattenToString(), false);
    }

    public static void setTileAdded(Context context, ComponentName componentName, boolean z) {
        context.getSharedPreferences(TILES, 0).edit().putBoolean(componentName.flattenToString(), z).commit();
    }
}
