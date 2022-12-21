package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiManager;
import android.net.wifi.aware.IWifiAwareDiscoverySessionCallback;
import android.net.wifi.aware.IWifiAwareEventCallback;
import android.net.wifi.aware.TlvBufferUtils;
import android.net.wifi.util.HexEncoding;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.Collections;
import java.util.List;

public class WifiAwareManager {
    public static final String ACTION_WIFI_AWARE_RESOURCE_CHANGED = "android.net.wifi.aware.action.WIFI_AWARE_RESOURCE_CHANGED";
    public static final String ACTION_WIFI_AWARE_STATE_CHANGED = "android.net.wifi.aware.action.WIFI_AWARE_STATE_CHANGED";
    private static final boolean DBG = false;
    public static final String EXTRA_AWARE_RESOURCES = "android.net.wifi.aware.extra.AWARE_RESOURCES";
    private static final String TAG = "WifiAwareManager";
    private static final boolean VDBG = false;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_INITIATOR = 0;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_RESPONDER = 1;
    public static final int WIFI_AWARE_DISCOVERY_LOST_REASON_PEER_NOT_VISIBLE = 1;
    public static final int WIFI_AWARE_DISCOVERY_LOST_REASON_UNKNOWN = 0;
    private final Context mContext;
    private final Object mLock = new Object();
    private final IWifiAwareManager mService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DataPathRole {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DiscoveryLostReasonCode {
    }

    public WifiAwareManager(Context context, IWifiAwareManager iWifiAwareManager) {
        this.mContext = context;
        this.mService = iWifiAwareManager;
    }

    public boolean isAvailable() {
        try {
            return this.mService.isUsageEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isDeviceAttached() {
        try {
            return this.mService.isDeviceAttached();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSetChannelOnDataPathSupported() {
        try {
            return this.mService.isSetChannelOnDataPathSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void enableInstantCommunicationMode(boolean z) {
        if (SdkLevel.isAtLeastS()) {
            try {
                this.mService.enableInstantCommunicationMode(this.mContext.getOpPackageName(), z);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean isInstantCommunicationModeEnabled() {
        if (SdkLevel.isAtLeastS()) {
            try {
                return this.mService.isInstantCommunicationModeEnabled();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Characteristics getCharacteristics() {
        try {
            return this.mService.getCharacteristics();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public AwareResources getAvailableAwareResources() {
        try {
            return this.mService.getAvailableAwareResources();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void attach(AttachCallback attachCallback, Handler handler) {
        attach(handler, (ConfigRequest) null, attachCallback, (IdentityChangedListener) null);
    }

    public void attach(AttachCallback attachCallback, IdentityChangedListener identityChangedListener, Handler handler) {
        attach(handler, (ConfigRequest) null, attachCallback, identityChangedListener);
    }

    public void attach(Handler handler, ConfigRequest configRequest, AttachCallback attachCallback, IdentityChangedListener identityChangedListener) {
        if (attachCallback != null) {
            synchronized (this.mLock) {
                Looper mainLooper = handler == null ? Looper.getMainLooper() : handler.getLooper();
                try {
                    Binder binder = new Binder();
                    Bundle bundle = new Bundle();
                    if (SdkLevel.isAtLeastS()) {
                        bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                    }
                    this.mService.connect(binder, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), new WifiAwareEventCallbackProxy(this, mainLooper, binder, attachCallback, identityChangedListener), configRequest, identityChangedListener != null, bundle);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return;
        }
        throw new IllegalArgumentException("Null callback provided");
    }

    public void disconnect(int i, Binder binder) {
        try {
            this.mService.disconnect(i, binder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void publish(int i, Looper looper, PublishConfig publishConfig, DiscoverySessionCallback discoverySessionCallback) {
        if (discoverySessionCallback != null) {
            try {
                Bundle bundle = new Bundle();
                if (SdkLevel.isAtLeastS()) {
                    bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                }
                this.mService.publish(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), i, publishConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, true, discoverySessionCallback, i), bundle);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Null callback provided");
        }
    }

    public void updatePublish(int i, int i2, PublishConfig publishConfig) {
        try {
            this.mService.updatePublish(i, i2, publishConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void subscribe(int i, Looper looper, SubscribeConfig subscribeConfig, DiscoverySessionCallback discoverySessionCallback) {
        if (discoverySessionCallback != null) {
            try {
                Bundle bundle = new Bundle();
                if (SdkLevel.isAtLeastS()) {
                    bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                }
                this.mService.subscribe(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), i, subscribeConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, false, discoverySessionCallback, i), bundle);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Null callback provided");
        }
    }

    public void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) {
        try {
            this.mService.updateSubscribe(i, i2, subscribeConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void terminateSession(int i, int i2) {
        try {
            this.mService.terminateSession(i, i2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendMessage(int i, int i2, PeerHandle peerHandle, byte[] bArr, int i3, int i4) {
        if (peerHandle != null) {
            try {
                this.mService.sendMessage(i, i2, peerHandle.peerId, bArr, i3, i4);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("sendMessage: invalid peerHandle - must be non-null");
        }
    }

    public void requestMacAddresses(int i, int[] iArr, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) {
        try {
            this.mService.requestMacAddresses(i, iArr, iWifiAwareMacAddressProvider);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NetworkSpecifier createNetworkSpecifier(int i, int i2, int i3, PeerHandle peerHandle, byte[] bArr, String str) {
        int i4 = i2;
        PeerHandle peerHandle2 = peerHandle;
        if (WifiAwareUtils.isLegacyVersion(this.mContext, 29)) {
            int i5 = 1;
            if (i4 != 0 && i4 != 1) {
                throw new IllegalArgumentException("createNetworkSpecifier: Invalid 'role' argument when creating a network specifier");
            } else if ((i4 == 0 || !WifiAwareUtils.isLegacyVersion(this.mContext, 28)) && peerHandle2 == null) {
                throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer handle - cannot be null");
            } else {
                int i6 = 0;
                if (peerHandle2 != null) {
                    i5 = 0;
                }
                if (peerHandle2 != null) {
                    i6 = peerHandle2.peerId;
                }
                return new WifiAwareNetworkSpecifier(i5, i2, i, i3, i6, (byte[]) null, bArr, str, 0, -1);
            }
        } else {
            throw new UnsupportedOperationException("API deprecated - use WifiAwareNetworkSpecifier.Builder");
        }
    }

    public NetworkSpecifier createNetworkSpecifier(int i, int i2, byte[] bArr, byte[] bArr2, String str) {
        int i3 = i2;
        byte[] bArr3 = bArr;
        if (i3 != 0 && i3 != 1) {
            throw new IllegalArgumentException("createNetworkSpecifier: Invalid 'role' argument when creating a network specifier");
        } else if ((i3 == 0 || !WifiAwareUtils.isLegacyVersion(this.mContext, 28)) && bArr3 == null) {
            throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer MAC - cannot be null");
        } else if (bArr3 == null || bArr3.length == 6) {
            return new WifiAwareNetworkSpecifier(bArr3 == null ? 3 : 2, i2, i, 0, 0, bArr, bArr2, str, 0, -1);
        } else {
            throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer MAC address");
        }
    }

    private static class WifiAwareEventCallbackProxy extends IWifiAwareEventCallback.Stub {
        private static final int CALLBACK_ATTACH_TERMINATE = 3;
        private static final int CALLBACK_CONNECT_FAIL = 1;
        private static final int CALLBACK_CONNECT_SUCCESS = 0;
        private static final int CALLBACK_IDENTITY_CHANGED = 2;
        /* access modifiers changed from: private */
        public final WeakReference<WifiAwareManager> mAwareManager;
        /* access modifiers changed from: private */
        public final Binder mBinder;
        private final Handler mHandler;
        private final Looper mLooper;

        WifiAwareEventCallbackProxy(WifiAwareManager wifiAwareManager, Looper looper, Binder binder, final AttachCallback attachCallback, final IdentityChangedListener identityChangedListener) {
            this.mAwareManager = new WeakReference<>(wifiAwareManager);
            this.mLooper = looper;
            this.mBinder = binder;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message message) {
                    WifiAwareManager wifiAwareManager = (WifiAwareManager) WifiAwareEventCallbackProxy.this.mAwareManager.get();
                    if (wifiAwareManager == null) {
                        Log.w(WifiAwareManager.TAG, "WifiAwareEventCallbackProxy: handleMessage post GC");
                        return;
                    }
                    int i = message.what;
                    if (i == 0) {
                        attachCallback.onAttached(new WifiAwareSession(wifiAwareManager, WifiAwareEventCallbackProxy.this.mBinder, message.arg1));
                    } else if (i == 1) {
                        WifiAwareEventCallbackProxy.this.mAwareManager.clear();
                        attachCallback.onAttachFailed();
                    } else if (i == 2) {
                        IdentityChangedListener identityChangedListener = identityChangedListener;
                        if (identityChangedListener == null) {
                            Log.e(WifiAwareManager.TAG, "CALLBACK_IDENTITY_CHANGED: null listener.");
                        } else {
                            identityChangedListener.onIdentityChanged((byte[]) message.obj);
                        }
                    } else if (i == 3) {
                        WifiAwareEventCallbackProxy.this.mAwareManager.clear();
                        attachCallback.onAwareSessionTerminated();
                    }
                }
            };
        }

        public void onConnectSuccess(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(0);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onConnectFail(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(1);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onIdentityChanged(byte[] bArr) {
            Message obtainMessage = this.mHandler.obtainMessage(2);
            obtainMessage.obj = bArr;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onAttachTerminate() {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
        }
    }

    private static class WifiAwareDiscoverySessionCallbackProxy extends IWifiAwareDiscoverySessionCallback.Stub {
        private static final int CALLBACK_MATCH = 4;
        private static final int CALLBACK_MATCH_EXPIRED = 9;
        private static final int CALLBACK_MATCH_WITH_DISTANCE = 8;
        private static final int CALLBACK_MESSAGE_RECEIVED = 7;
        private static final int CALLBACK_MESSAGE_SEND_FAIL = 6;
        private static final int CALLBACK_MESSAGE_SEND_SUCCESS = 5;
        private static final int CALLBACK_SESSION_CONFIG_FAIL = 2;
        private static final int CALLBACK_SESSION_CONFIG_SUCCESS = 1;
        private static final int CALLBACK_SESSION_STARTED = 0;
        private static final int CALLBACK_SESSION_TERMINATED = 3;
        private static final String MESSAGE_BUNDLE_KEY_CIPHER_SUITE = "key_cipher_suite";
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE = "message";
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE2 = "message2";
        private static final String MESSAGE_BUNDLE_KEY_SCID = "key_scid";
        /* access modifiers changed from: private */
        public final WeakReference<WifiAwareManager> mAwareManager;
        private final int mClientId;
        private final Handler mHandler;
        private final boolean mIsPublish;
        /* access modifiers changed from: private */
        public final DiscoverySessionCallback mOriginalCallback;
        /* access modifiers changed from: private */
        public DiscoverySession mSession;

        WifiAwareDiscoverySessionCallbackProxy(WifiAwareManager wifiAwareManager, Looper looper, boolean z, DiscoverySessionCallback discoverySessionCallback, int i) {
            this.mAwareManager = new WeakReference<>(wifiAwareManager);
            this.mIsPublish = z;
            this.mOriginalCallback = discoverySessionCallback;
            this.mClientId = i;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message message) {
                    List<byte[]> list;
                    if (WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.get() == null) {
                        Log.w(WifiAwareManager.TAG, "WifiAwareDiscoverySessionCallbackProxy: handleMessage post GC");
                        return;
                    }
                    switch (message.what) {
                        case 0:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionStarted(message.arg1);
                            return;
                        case 1:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigUpdated();
                            return;
                        case 2:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigFailed();
                            if (WifiAwareDiscoverySessionCallbackProxy.this.mSession == null) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.clear();
                                return;
                            }
                            return;
                        case 3:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionTerminated(message.arg1);
                            return;
                        case 4:
                        case 8:
                            Bundle data = message.getData();
                            byte[] byteArray = data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE2);
                            try {
                                list = new TlvBufferUtils.TlvIterable(0, 1, byteArray).toList();
                            } catch (BufferOverflowException e) {
                                List<byte[]> emptyList = Collections.emptyList();
                                Log.e(WifiAwareManager.TAG, "onServiceDiscovered: invalid match filter byte array '" + new String(HexEncoding.encode(byteArray)) + "' - cannot be parsed: e=" + e);
                                list = emptyList;
                            }
                            if (message.what == 4) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscovered(new PeerHandle(message.arg1), data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE), list);
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscovered(new ServiceDiscoveryInfo(new PeerHandle(message.arg1), data.getInt(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_CIPHER_SUITE), data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE), list, data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_SCID)));
                                return;
                            }
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscoveredWithinRange(new PeerHandle(message.arg1), message.getData().getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE), list, message.arg2);
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscoveredWithinRange(new ServiceDiscoveryInfo(new PeerHandle(message.arg1), data.getInt(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_CIPHER_SUITE), data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE), list, data.getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_SCID)), message.arg2);
                            return;
                        case 5:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendSucceeded(message.arg1);
                            return;
                        case 6:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendFailed(message.arg1);
                            return;
                        case 7:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageReceived(new PeerHandle(message.arg1), (byte[]) message.obj);
                            return;
                        case 9:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceLost(new PeerHandle(message.arg1), 1);
                            return;
                        default:
                            return;
                    }
                }
            };
        }

        public void onSessionStarted(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(0);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onSessionConfigSuccess() {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        }

        public void onSessionConfigFail(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(2);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onSessionTerminated(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(3);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        private void onMatchCommon(int i, int i2, byte[] bArr, byte[] bArr2, int i3, int i4, byte[] bArr3) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(MESSAGE_BUNDLE_KEY_MESSAGE, bArr);
            bundle.putByteArray(MESSAGE_BUNDLE_KEY_MESSAGE2, bArr2);
            bundle.putInt(MESSAGE_BUNDLE_KEY_CIPHER_SUITE, i4);
            bundle.putByteArray(MESSAGE_BUNDLE_KEY_SCID, bArr3);
            Message obtainMessage = this.mHandler.obtainMessage(i);
            obtainMessage.arg1 = i2;
            obtainMessage.arg2 = i3;
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onMatch(int i, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) {
            onMatchCommon(4, i, bArr, bArr2, 0, i2, bArr3);
        }

        public void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) {
            onMatchCommon(8, i, bArr, bArr2, i2, i3, bArr3);
        }

        public void onMatchExpired(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(9);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onMessageSendSuccess(int i) {
            Message obtainMessage = this.mHandler.obtainMessage(5);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onMessageSendFail(int i, int i2) {
            Message obtainMessage = this.mHandler.obtainMessage(6);
            obtainMessage.arg1 = i;
            obtainMessage.arg2 = i2;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onMessageReceived(int i, byte[] bArr) {
            Message obtainMessage = this.mHandler.obtainMessage(7);
            obtainMessage.arg1 = i;
            obtainMessage.obj = bArr;
            this.mHandler.sendMessage(obtainMessage);
        }

        public void onProxySessionStarted(int i) {
            if (this.mSession == null) {
                WifiAwareManager wifiAwareManager = this.mAwareManager.get();
                if (wifiAwareManager == null) {
                    Log.w(WifiAwareManager.TAG, "onProxySessionStarted: mgr GC'd");
                } else if (this.mIsPublish) {
                    PublishDiscoverySession publishDiscoverySession = new PublishDiscoverySession(wifiAwareManager, this.mClientId, i);
                    this.mSession = publishDiscoverySession;
                    this.mOriginalCallback.onPublishStarted(publishDiscoverySession);
                } else {
                    SubscribeDiscoverySession subscribeDiscoverySession = new SubscribeDiscoverySession(wifiAwareManager, this.mClientId, i);
                    this.mSession = subscribeDiscoverySession;
                    this.mOriginalCallback.onSubscribeStarted(subscribeDiscoverySession);
                }
            } else {
                Log.e(WifiAwareManager.TAG, "onSessionStarted: sessionId=" + i + ": session already created!?");
                throw new IllegalStateException("onSessionStarted: sessionId=" + i + ": session already created!?");
            }
        }

        public void onProxySessionTerminated(int i) {
            DiscoverySession discoverySession = this.mSession;
            if (discoverySession != null) {
                discoverySession.setTerminated();
                this.mSession = null;
            } else {
                Log.w(WifiAwareManager.TAG, "Proxy: onSessionTerminated called but mSession is null!?");
            }
            this.mAwareManager.clear();
            this.mOriginalCallback.onSessionTerminated();
        }
    }

    @SystemApi
    public void setAwareParams(AwareParams awareParams) {
        try {
            this.mService.setAwareParams(awareParams);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
