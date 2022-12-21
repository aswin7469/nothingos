package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.net.NetworkSpecifier;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.util.CloseGuard;
import android.util.Log;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class WifiAwareSession implements AutoCloseable {
    private static final boolean DBG = false;
    private static final String TAG = "WifiAwareSession";
    private static final boolean VDBG = false;
    private final Binder mBinder;
    private final int mClientId;
    private final CloseGuard mCloseGuard;
    private final WeakReference<WifiAwareManager> mMgr;
    private boolean mTerminated = true;

    public WifiAwareSession(WifiAwareManager wifiAwareManager, Binder binder, int i) {
        CloseGuard closeGuard = new CloseGuard();
        this.mCloseGuard = closeGuard;
        this.mMgr = new WeakReference<>(wifiAwareManager);
        this.mBinder = binder;
        this.mClientId = i;
        this.mTerminated = false;
        closeGuard.open("close");
    }

    public void close() {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.w(TAG, "destroy: called post GC on WifiAwareManager");
            return;
        }
        wifiAwareManager.disconnect(this.mClientId, this.mBinder);
        this.mTerminated = true;
        this.mMgr.clear();
        this.mCloseGuard.close();
        Reference.reachabilityFence(this);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            if (!this.mTerminated) {
                close();
            }
        } finally {
            super.finalize();
        }
    }

    public int getClientId() {
        return this.mClientId;
    }

    public void publish(PublishConfig publishConfig, DiscoverySessionCallback discoverySessionCallback, Handler handler) {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.e(TAG, "publish: called post GC on WifiAwareManager");
        } else if (this.mTerminated) {
            Log.e(TAG, "publish: called after termination");
        } else {
            wifiAwareManager.publish(this.mClientId, handler == null ? Looper.getMainLooper() : handler.getLooper(), publishConfig, discoverySessionCallback);
        }
    }

    public void subscribe(SubscribeConfig subscribeConfig, DiscoverySessionCallback discoverySessionCallback, Handler handler) {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.e(TAG, "publish: called post GC on WifiAwareManager");
        } else if (this.mTerminated) {
            Log.e(TAG, "publish: called after termination");
        } else {
            wifiAwareManager.subscribe(this.mClientId, handler == null ? Looper.getMainLooper() : handler.getLooper(), subscribeConfig, discoverySessionCallback);
        }
    }

    @Deprecated
    public NetworkSpecifier createNetworkSpecifierOpen(int i, byte[] bArr) {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.e(TAG, "createNetworkSpecifierOpen: called post GC on WifiAwareManager");
            return null;
        } else if (!this.mTerminated) {
            return wifiAwareManager.createNetworkSpecifier(this.mClientId, i, bArr, (byte[]) null, (String) null);
        } else {
            Log.e(TAG, "createNetworkSpecifierOpen: called after termination");
            return null;
        }
    }

    @Deprecated
    public NetworkSpecifier createNetworkSpecifierPassphrase(int i, byte[] bArr, String str) {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.e(TAG, "createNetworkSpecifierPassphrase: called post GC on WifiAwareManager");
            return null;
        } else if (this.mTerminated) {
            Log.e(TAG, "createNetworkSpecifierPassphrase: called after termination");
            return null;
        } else if (WifiAwareUtils.validatePassphrase(str)) {
            return wifiAwareManager.createNetworkSpecifier(this.mClientId, i, bArr, (byte[]) null, str);
        } else {
            throw new IllegalArgumentException("Passphrase must meet length requirements");
        }
    }

    @SystemApi
    @Deprecated
    public NetworkSpecifier createNetworkSpecifierPmk(int i, byte[] bArr, byte[] bArr2) {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.e(TAG, "createNetworkSpecifierPmk: called post GC on WifiAwareManager");
            return null;
        } else if (this.mTerminated) {
            Log.e(TAG, "createNetworkSpecifierPmk: called after termination");
            return null;
        } else if (WifiAwareUtils.validatePmk(bArr2)) {
            return wifiAwareManager.createNetworkSpecifier(this.mClientId, i, bArr, bArr2, (String) null);
        } else {
            throw new IllegalArgumentException("PMK must 32 bytes");
        }
    }
}
