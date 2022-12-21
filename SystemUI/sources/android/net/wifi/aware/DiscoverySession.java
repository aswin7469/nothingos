package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.net.NetworkSpecifier;
import android.util.CloseGuard;
import android.util.Log;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class DiscoverySession implements AutoCloseable {
    private static final boolean DBG = false;
    private static final int MAX_SEND_RETRY_COUNT = 5;
    private static final String TAG = "DiscoverySession";
    private static final boolean VDBG = false;
    protected final int mClientId;
    private final CloseGuard mCloseGuard;
    protected WeakReference<WifiAwareManager> mMgr;
    protected final int mSessionId;
    protected boolean mTerminated = false;

    public static int getMaxSendRetryCount() {
        return 5;
    }

    public DiscoverySession(WifiAwareManager wifiAwareManager, int i, int i2) {
        CloseGuard closeGuard = new CloseGuard();
        this.mCloseGuard = closeGuard;
        this.mMgr = new WeakReference<>(wifiAwareManager);
        this.mClientId = i;
        this.mSessionId = i2;
        closeGuard.open("close");
    }

    public void close() {
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.w(TAG, "destroy: called post GC on WifiAwareManager");
            return;
        }
        wifiAwareManager.terminateSession(this.mClientId, this.mSessionId);
        this.mTerminated = true;
        this.mMgr.clear();
        this.mCloseGuard.close();
        Reference.reachabilityFence(this);
    }

    public void setTerminated() {
        if (this.mTerminated) {
            Log.w(TAG, "terminate: already terminated.");
            return;
        }
        this.mTerminated = true;
        this.mMgr.clear();
        this.mCloseGuard.close();
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

    public int getSessionId() {
        return this.mSessionId;
    }

    public void sendMessage(PeerHandle peerHandle, int i, byte[] bArr, int i2) {
        if (this.mTerminated) {
            Log.w(TAG, "sendMessage: called on terminated session");
            return;
        }
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.w(TAG, "sendMessage: called post GC on WifiAwareManager");
        } else {
            wifiAwareManager.sendMessage(this.mClientId, this.mSessionId, peerHandle, bArr, i, i2);
        }
    }

    public void sendMessage(PeerHandle peerHandle, int i, byte[] bArr) {
        sendMessage(peerHandle, i, bArr, 0);
    }

    @Deprecated
    public NetworkSpecifier createNetworkSpecifierOpen(PeerHandle peerHandle) {
        if (this.mTerminated) {
            Log.w(TAG, "createNetworkSpecifierOpen: called on terminated session");
            return null;
        }
        WifiAwareManager wifiAwareManager = this.mMgr.get();
        if (wifiAwareManager == null) {
            Log.w(TAG, "createNetworkSpecifierOpen: called post GC on WifiAwareManager");
            return null;
        }
        return wifiAwareManager.createNetworkSpecifier(this.mClientId, (this instanceof SubscribeDiscoverySession) ^ true ? 1 : 0, this.mSessionId, peerHandle, (byte[]) null, (String) null);
    }

    @Deprecated
    public NetworkSpecifier createNetworkSpecifierPassphrase(PeerHandle peerHandle, String str) {
        if (!WifiAwareUtils.validatePassphrase(str)) {
            throw new IllegalArgumentException("Passphrase must meet length requirements");
        } else if (this.mTerminated) {
            Log.w(TAG, "createNetworkSpecifierPassphrase: called on terminated session");
            return null;
        } else {
            WifiAwareManager wifiAwareManager = this.mMgr.get();
            if (wifiAwareManager == null) {
                Log.w(TAG, "createNetworkSpecifierPassphrase: called post GC on WifiAwareManager");
                return null;
            }
            return wifiAwareManager.createNetworkSpecifier(this.mClientId, (this instanceof SubscribeDiscoverySession) ^ true ? 1 : 0, this.mSessionId, peerHandle, (byte[]) null, str);
        }
    }

    @SystemApi
    @Deprecated
    public NetworkSpecifier createNetworkSpecifierPmk(PeerHandle peerHandle, byte[] bArr) {
        if (!WifiAwareUtils.validatePmk(bArr)) {
            throw new IllegalArgumentException("PMK must 32 bytes");
        } else if (this.mTerminated) {
            Log.w(TAG, "createNetworkSpecifierPmk: called on terminated session");
            return null;
        } else {
            WifiAwareManager wifiAwareManager = this.mMgr.get();
            if (wifiAwareManager == null) {
                Log.w(TAG, "createNetworkSpecifierPmk: called post GC on WifiAwareManager");
                return null;
            }
            return wifiAwareManager.createNetworkSpecifier(this.mClientId, (this instanceof SubscribeDiscoverySession) ^ true ? 1 : 0, this.mSessionId, peerHandle, bArr, (String) null);
        }
    }
}
