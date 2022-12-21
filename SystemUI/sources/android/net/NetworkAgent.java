package android.net;

import android.annotation.SystemApi;
import android.app.StatsManager;
import android.content.Context;
import android.net.INetworkAgent;
import android.net.NetworkInfo;
import android.net.NetworkScore;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.data.EpsBearerQosSessionAttributes;
import android.telephony.data.NrQosSessionAttributes;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@SystemApi
public abstract class NetworkAgent {
    private static final int BASE = 200;
    private static final long BW_REFRESH_MIN_WIN_MS = 500;
    public static final int CMD_ADD_KEEPALIVE_PACKET_FILTER = 216;
    public static final int CMD_DSCP_POLICY_STATUS = 228;
    public static final int CMD_NETWORK_CREATED = 222;
    public static final int CMD_NETWORK_DESTROYED = 223;
    public static final int CMD_PREVENT_AUTOMATIC_RECONNECT = 215;
    public static final int CMD_REGISTER_QOS_CALLBACK = 220;
    public static final int CMD_REMOVE_KEEPALIVE_PACKET_FILTER = 217;
    public static final int CMD_REPORT_NETWORK_STATUS = 207;
    public static final int CMD_REQUEST_BANDWIDTH_UPDATE = 210;
    public static final int CMD_SAVE_ACCEPT_UNVALIDATED = 209;
    public static final int CMD_SET_SIGNAL_STRENGTH_THRESHOLDS = 214;
    public static final int CMD_START_SOCKET_KEEPALIVE = 211;
    public static final int CMD_STOP_SOCKET_KEEPALIVE = 212;
    public static final int CMD_SUSPECT_BAD = 200;
    public static final int CMD_UNREGISTER_QOS_CALLBACK = 221;
    private static final boolean DBG = true;
    public static final int DSCP_POLICY_STATUS_DELETED = 4;
    public static final int DSCP_POLICY_STATUS_INSUFFICIENT_PROCESSING_RESOURCES = 3;
    public static final int DSCP_POLICY_STATUS_POLICY_NOT_FOUND = 5;
    public static final int DSCP_POLICY_STATUS_REQUESTED_CLASSIFIER_NOT_SUPPORTED = 2;
    public static final int DSCP_POLICY_STATUS_REQUEST_DECLINED = 1;
    public static final int DSCP_POLICY_STATUS_SUCCESS = 0;
    public static final int EVENT_ADD_DSCP_POLICY = 225;
    private static final int EVENT_AGENT_CONNECTED = 218;
    private static final int EVENT_AGENT_DISCONNECTED = 219;
    public static final int EVENT_LINGER_DURATION_CHANGED = 224;
    public static final int EVENT_NETWORK_CAPABILITIES_CHANGED = 202;
    public static final int EVENT_NETWORK_INFO_CHANGED = 201;
    public static final int EVENT_NETWORK_PROPERTIES_CHANGED = 203;
    public static final int EVENT_NETWORK_SCORE_CHANGED = 204;
    public static final int EVENT_REMOVE_ALL_DSCP_POLICIES = 227;
    public static final int EVENT_REMOVE_DSCP_POLICY = 226;
    public static final int EVENT_SET_EXPLICITLY_SELECTED = 208;
    public static final int EVENT_SOCKET_KEEPALIVE = 213;
    public static final int EVENT_TEARDOWN_DELAY_CHANGED = 206;
    public static final int EVENT_UNDERLYING_NETWORKS_CHANGED = 205;
    public static final int EVENT_UNREGISTER_AFTER_REPLACEMENT = 229;
    public static final int INVALID_NETWORK = 2;
    public static final int MAX_TEARDOWN_DELAY_MS = 5000;
    public static final int MIN_LINGER_TIMER_MS = 2000;
    public static final String REDIRECT_URL_KEY = "redirect URL";
    public static final int VALIDATION_STATUS_NOT_VALID = 2;
    public static final int VALIDATION_STATUS_VALID = 1;
    public static final int VALID_NETWORK = 1;
    private static final boolean VDBG = false;
    public static final int WIFI_BASE_SCORE = 60;
    /* access modifiers changed from: private */
    public final String LOG_TAG;
    /* access modifiers changed from: private */
    public AtomicBoolean mBandwidthUpdatePending;
    /* access modifiers changed from: private */
    public boolean mBandwidthUpdateScheduled;
    private final Handler mHandler;
    private volatile InitialConfiguration mInitialConfiguration;
    /* access modifiers changed from: private */
    public volatile long mLastBwRefreshTime;
    private volatile Network mNetwork;
    private NetworkInfo mNetworkInfo;
    /* access modifiers changed from: private */
    public final ArrayList<RegistryAction> mPreConnectedQueue;
    private final Object mRegisterLock;
    /* access modifiers changed from: private */
    public volatile INetworkAgentRegistry mRegistry;
    public final int providerId;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DscpPolicyStatus {
    }

    private interface RegistryAction {
        void execute(INetworkAgentRegistry iNetworkAgentRegistry) throws RemoteException;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ValidationStatus {
    }

    /* access modifiers changed from: protected */
    public void addKeepalivePacketFilter(Message message) {
    }

    /* access modifiers changed from: protected */
    public void networkStatus(int i, String str) {
    }

    public void onDscpPolicyStatusUpdated(int i, int i2) {
    }

    public void onNetworkCreated() {
    }

    public void onNetworkDestroyed() {
    }

    public void onQosCallbackRegistered(int i, QosFilter qosFilter) {
    }

    public void onQosCallbackUnregistered(int i) {
    }

    /* access modifiers changed from: protected */
    public void pollLceData() {
    }

    /* access modifiers changed from: protected */
    public void preventAutomaticReconnect() {
    }

    /* access modifiers changed from: protected */
    public void removeKeepalivePacketFilter(Message message) {
    }

    /* access modifiers changed from: protected */
    public void saveAcceptUnvalidated(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void setSignalStrengthThresholds(int[] iArr) {
    }

    /* access modifiers changed from: protected */
    public void unwanted() {
    }

    private static NetworkInfo getLegacyNetworkInfo(NetworkAgentConfig networkAgentConfig) {
        NetworkInfo networkInfo = new NetworkInfo(networkAgentConfig.legacyType, networkAgentConfig.legacySubType, networkAgentConfig.legacyTypeName, networkAgentConfig.legacySubTypeName);
        networkInfo.setIsAvailable(true);
        networkInfo.setDetailedState(NetworkInfo.DetailedState.CONNECTING, (String) null, networkAgentConfig.getLegacyExtraInfo());
        return networkInfo;
    }

    public NetworkAgent(Context context, Looper looper, String str, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, int i, NetworkAgentConfig networkAgentConfig, NetworkProvider networkProvider) {
        this(context, looper, str, networkCapabilities, linkProperties, new NetworkScore.Builder().setLegacyInt(i).build(), networkAgentConfig, networkProvider);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public NetworkAgent(Context context, Looper looper, String str, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, NetworkProvider networkProvider) {
        this(looper, context, str, networkCapabilities, linkProperties, networkScore, networkAgentConfig, networkProvider == null ? -1 : networkProvider.getProviderId(), getLegacyNetworkInfo(networkAgentConfig));
    }

    private static class InitialConfiguration {
        public final NetworkCapabilities capabilities;
        public final NetworkAgentConfig config;
        public final Context context;
        public final NetworkInfo info;
        public final LinkProperties properties;
        public final NetworkScore score;

        InitialConfiguration(Context context2, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, NetworkInfo networkInfo) {
            this.context = context2;
            this.capabilities = networkCapabilities;
            this.properties = linkProperties;
            this.score = networkScore;
            this.config = networkAgentConfig;
            this.info = networkInfo;
        }
    }

    private NetworkAgent(Looper looper, Context context, String str, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, int i, NetworkInfo networkInfo) {
        NetworkCapabilities networkCapabilities2 = networkCapabilities;
        LinkProperties linkProperties2 = linkProperties;
        NetworkInfo networkInfo2 = networkInfo;
        this.mPreConnectedQueue = new ArrayList<>();
        this.mLastBwRefreshTime = 0;
        this.mBandwidthUpdateScheduled = false;
        this.mBandwidthUpdatePending = new AtomicBoolean(false);
        this.mRegisterLock = new Object();
        Looper looper2 = looper;
        this.mHandler = new NetworkAgentHandler(looper);
        this.LOG_TAG = str;
        this.mNetworkInfo = new NetworkInfo(networkInfo2);
        this.providerId = i;
        if (networkInfo2 == null || networkCapabilities2 == null || linkProperties2 == null) {
            throw new IllegalArgumentException();
        }
        this.mInitialConfiguration = new InitialConfiguration(context, new NetworkCapabilities(networkCapabilities, 0), new LinkProperties(linkProperties), networkScore, networkAgentConfig, networkInfo);
    }

    private class NetworkAgentHandler extends Handler {
        NetworkAgentHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 200) {
                Uri uri = null;
                if (i == 207) {
                    String string = ((Bundle) message.obj).getString(NetworkAgent.REDIRECT_URL_KEY);
                    if (string != null) {
                        try {
                            uri = Uri.parse(string);
                        } catch (Exception e) {
                            Log.wtf(NetworkAgent.this.LOG_TAG, "Surprising URI : " + string, e);
                        }
                    }
                    NetworkAgent.this.onValidationStatus(message.arg1, uri);
                } else if (i != 228) {
                    boolean z = true;
                    switch (i) {
                        case 209:
                            NetworkAgent networkAgent = NetworkAgent.this;
                            if (message.arg1 == 0) {
                                z = false;
                            }
                            networkAgent.onSaveAcceptUnvalidated(z);
                            return;
                        case 210:
                            long currentTimeMillis = System.currentTimeMillis();
                            if (currentTimeMillis >= NetworkAgent.this.mLastBwRefreshTime + 500) {
                                NetworkAgent.this.mBandwidthUpdateScheduled = false;
                                if (!NetworkAgent.this.mBandwidthUpdatePending.getAndSet(true)) {
                                    NetworkAgent.this.onBandwidthUpdateRequested();
                                    return;
                                }
                                return;
                            } else if (!NetworkAgent.this.mBandwidthUpdateScheduled) {
                                NetworkAgent.this.mBandwidthUpdateScheduled = sendEmptyMessageDelayed(210, ((NetworkAgent.this.mLastBwRefreshTime + 500) - currentTimeMillis) + 1);
                                return;
                            } else {
                                return;
                            }
                        case 211:
                            NetworkAgent.this.onStartSocketKeepalive(message.arg1, Duration.ofSeconds((long) message.arg2), (KeepalivePacketData) message.obj);
                            return;
                        case 212:
                            NetworkAgent.this.onStopSocketKeepalive(message.arg1);
                            return;
                        default:
                            switch (i) {
                                case 214:
                                    NetworkAgent.this.onSignalStrengthThresholdsUpdated((int[]) message.obj);
                                    return;
                                case 215:
                                    NetworkAgent.this.onAutomaticReconnectDisabled();
                                    return;
                                case 216:
                                    NetworkAgent.this.onAddKeepalivePacketFilter(message.arg1, (KeepalivePacketData) message.obj);
                                    return;
                                case 217:
                                    NetworkAgent.this.onRemoveKeepalivePacketFilter(message.arg1);
                                    return;
                                case 218:
                                    if (NetworkAgent.this.mRegistry != null) {
                                        NetworkAgent.this.log("Received new connection while already connected!");
                                        return;
                                    }
                                    synchronized (NetworkAgent.this.mPreConnectedQueue) {
                                        INetworkAgentRegistry iNetworkAgentRegistry = (INetworkAgentRegistry) message.obj;
                                        NetworkAgent.this.mRegistry = iNetworkAgentRegistry;
                                        Iterator it = NetworkAgent.this.mPreConnectedQueue.iterator();
                                        while (it.hasNext()) {
                                            try {
                                                ((RegistryAction) it.next()).execute(iNetworkAgentRegistry);
                                            } catch (RemoteException e2) {
                                                Log.wtf(NetworkAgent.this.LOG_TAG, "Communication error with registry", e2);
                                            }
                                        }
                                        NetworkAgent.this.mPreConnectedQueue.clear();
                                    }
                                    return;
                                case 219:
                                    NetworkAgent.this.log("NetworkAgent channel lost");
                                    NetworkAgent.this.onNetworkUnwanted();
                                    synchronized (NetworkAgent.this.mPreConnectedQueue) {
                                        NetworkAgent.this.mRegistry = null;
                                    }
                                    return;
                                case 220:
                                    NetworkAgent.this.onQosCallbackRegistered(message.arg1, (QosFilter) message.obj);
                                    return;
                                case 221:
                                    NetworkAgent.this.onQosCallbackUnregistered(message.arg1);
                                    return;
                                case 222:
                                    NetworkAgent.this.onNetworkCreated();
                                    return;
                                case 223:
                                    NetworkAgent.this.onNetworkDestroyed();
                                    return;
                                default:
                                    return;
                            }
                    }
                } else {
                    NetworkAgent.this.onDscpPolicyStatusUpdated(message.arg1, message.arg2);
                }
            } else {
                NetworkAgent.this.log("Unhandled Message " + message);
            }
        }
    }

    public Network register() {
        synchronized (this.mRegisterLock) {
            if (this.mNetwork == null) {
                this.mNetwork = ((ConnectivityManager) this.mInitialConfiguration.context.getSystemService("connectivity")).registerNetworkAgent(new NetworkAgentBinder(this.mHandler), new NetworkInfo(this.mInitialConfiguration.info), this.mInitialConfiguration.properties, this.mInitialConfiguration.capabilities, this.mInitialConfiguration.score, this.mInitialConfiguration.config, this.providerId);
                this.mInitialConfiguration = null;
            } else {
                throw new IllegalStateException("Agent already registered");
            }
        }
        return this.mNetwork;
    }

    private static class NetworkAgentBinder extends INetworkAgent.Stub {
        private static final String LOG_TAG = "NetworkAgentBinder";
        private final Handler mHandler;

        private NetworkAgentBinder(Handler handler) {
            this.mHandler = handler;
        }

        public void onRegistered(INetworkAgentRegistry iNetworkAgentRegistry) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(218, iNetworkAgentRegistry));
        }

        public void onDisconnected() {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(219));
        }

        public void onBandwidthUpdateRequested() {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(210));
        }

        public void onValidationStatusChanged(int i, String str) {
            Bundle bundle = new Bundle();
            bundle.putString(NetworkAgent.REDIRECT_URL_KEY, str);
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(207, i, 0, bundle));
        }

        public void onSaveAcceptUnvalidated(boolean z) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(209, z ? 1 : 0, 0));
        }

        public void onStartNattSocketKeepalive(int i, int i2, NattKeepalivePacketData nattKeepalivePacketData) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(211, i, i2, nattKeepalivePacketData));
        }

        public void onStartTcpSocketKeepalive(int i, int i2, TcpKeepalivePacketData tcpKeepalivePacketData) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(211, i, i2, tcpKeepalivePacketData));
        }

        public void onStopSocketKeepalive(int i) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(212, i, 0));
        }

        public void onSignalStrengthThresholdsUpdated(int[] iArr) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(214, iArr));
        }

        public void onPreventAutomaticReconnect() {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(215));
        }

        public void onAddNattKeepalivePacketFilter(int i, NattKeepalivePacketData nattKeepalivePacketData) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(216, i, 0, nattKeepalivePacketData));
        }

        public void onAddTcpKeepalivePacketFilter(int i, TcpKeepalivePacketData tcpKeepalivePacketData) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(216, i, 0, tcpKeepalivePacketData));
        }

        public void onRemoveKeepalivePacketFilter(int i) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(217, i, 0));
        }

        public void onQosFilterCallbackRegistered(int i, QosFilterParcelable qosFilterParcelable) {
            if (qosFilterParcelable.getQosFilter() != null) {
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(220, i, 0, qosFilterParcelable.getQosFilter()));
                return;
            }
            Log.wtf(LOG_TAG, "onQosFilterCallbackRegistered: qos filter is null.");
        }

        public void onQosCallbackUnregistered(int i) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(221, i, 0, (Object) null));
        }

        public void onNetworkCreated() {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(222));
        }

        public void onNetworkDestroyed() {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(223));
        }

        public void onDscpPolicyStatusUpdated(int i, int i2) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(228, i, i2));
        }
    }

    public INetworkAgent registerForTest(Network network) {
        log("Registering NetworkAgent for test");
        synchronized (this.mRegisterLock) {
            this.mNetwork = network;
            this.mInitialConfiguration = null;
        }
        return new NetworkAgentBinder(this.mHandler);
    }

    public boolean waitForIdle(long j) {
        ConditionVariable conditionVariable = new ConditionVariable(false);
        this.mHandler.post(new NetworkAgent$$ExternalSyntheticLambda7(conditionVariable));
        return conditionVariable.block(j);
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    private void queueOrSendMessage(RegistryAction registryAction) {
        synchronized (this.mPreConnectedQueue) {
            if (this.mRegistry != null) {
                try {
                    registryAction.execute(this.mRegistry);
                } catch (RemoteException e) {
                    Log.wtf(this.LOG_TAG, "Error executing registry action", e);
                }
            } else {
                this.mPreConnectedQueue.add(registryAction);
            }
        }
    }

    public final void sendLinkProperties(LinkProperties linkProperties) {
        Objects.requireNonNull(linkProperties);
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda17(new LinkProperties(linkProperties)));
    }

    public final void setUnderlyingNetworks(List<Network> list) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda13(list != null ? new ArrayList(list) : null));
    }

    public void markConnected() {
        this.mNetworkInfo.setDetailedState(NetworkInfo.DetailedState.CONNECTED, (String) null, this.mNetworkInfo.getExtraInfo());
        queueOrSendNetworkInfo(this.mNetworkInfo);
    }

    public void unregister() {
        this.mNetworkInfo.setDetailedState(NetworkInfo.DetailedState.DISCONNECTED, (String) null, (String) null);
        queueOrSendNetworkInfo(this.mNetworkInfo);
    }

    public void setTeardownDelayMillis(int i) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda9(i));
    }

    public void unregisterAfterReplacement(int i) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda14(i));
    }

    @SystemApi
    @Deprecated
    public void setLegacySubtype(int i, String str) {
        this.mNetworkInfo.setSubtype(i, str);
        queueOrSendNetworkInfo(this.mNetworkInfo);
    }

    @Deprecated
    public void setLegacyExtraInfo(String str) {
        this.mNetworkInfo.setExtraInfo(str);
        queueOrSendNetworkInfo(this.mNetworkInfo);
    }

    public final void sendNetworkInfo(NetworkInfo networkInfo) {
        queueOrSendNetworkInfo(networkInfo);
    }

    private void queueOrSendNetworkInfo(NetworkInfo networkInfo) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda8(new NetworkInfo(networkInfo)));
    }

    public final void sendNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        Objects.requireNonNull(networkCapabilities);
        this.mBandwidthUpdatePending.set(false);
        this.mLastBwRefreshTime = System.currentTimeMillis();
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda6(new NetworkCapabilities(networkCapabilities, 0)));
    }

    public final void sendNetworkScore(NetworkScore networkScore) {
        Objects.requireNonNull(networkScore);
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda11(networkScore));
    }

    public final void sendNetworkScore(int i) {
        sendNetworkScore(new NetworkScore.Builder().setLegacyInt(i).build());
    }

    public void explicitlySelected(boolean z) {
        explicitlySelected(true, z);
    }

    public void explicitlySelected(boolean z, boolean z2) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda2(z, z2));
    }

    public void onNetworkUnwanted() {
        unwanted();
    }

    @SystemApi
    public void onBandwidthUpdateRequested() {
        pollLceData();
    }

    public void onValidationStatus(int i, Uri uri) {
        networkStatus(i, uri == null ? "" : uri.toString());
    }

    public void onSaveAcceptUnvalidated(boolean z) {
        saveAcceptUnvalidated(z);
    }

    public void onStartSocketKeepalive(int i, Duration duration, KeepalivePacketData keepalivePacketData) {
        long seconds = duration.getSeconds();
        if (seconds < 10 || seconds > 3600) {
            throw new IllegalArgumentException("Interval needs to be comprised between 10 and 3600 but was " + seconds);
        }
        Message obtainMessage = this.mHandler.obtainMessage(211, i, (int) seconds, keepalivePacketData);
        startSocketKeepalive(obtainMessage);
        obtainMessage.recycle();
    }

    /* access modifiers changed from: protected */
    public void startSocketKeepalive(Message message) {
        onSocketKeepaliveEvent(message.arg1, -30);
    }

    public void onStopSocketKeepalive(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(212, i, 0, (Object) null);
        stopSocketKeepalive(obtainMessage);
        obtainMessage.recycle();
    }

    /* access modifiers changed from: protected */
    public void stopSocketKeepalive(Message message) {
        onSocketKeepaliveEvent(message.arg1, -30);
    }

    public final void sendSocketKeepaliveEvent(int i, int i2) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda5(i, i2));
    }

    public void onSocketKeepaliveEvent(int i, int i2) {
        sendSocketKeepaliveEvent(i, i2);
    }

    public void onAddKeepalivePacketFilter(int i, KeepalivePacketData keepalivePacketData) {
        Message obtainMessage = this.mHandler.obtainMessage(216, i, 0, keepalivePacketData);
        addKeepalivePacketFilter(obtainMessage);
        obtainMessage.recycle();
    }

    public void onRemoveKeepalivePacketFilter(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(217, i, 0, (Object) null);
        removeKeepalivePacketFilter(obtainMessage);
        obtainMessage.recycle();
    }

    public void onSignalStrengthThresholdsUpdated(int[] iArr) {
        setSignalStrengthThresholds(iArr);
    }

    public void onAutomaticReconnectDisabled() {
        preventAutomaticReconnect();
    }

    public final void sendQosSessionAvailable(int i, int i2, QosSessionAttributes qosSessionAttributes) {
        Objects.requireNonNull(qosSessionAttributes, "The attributes must be non-null");
        if (qosSessionAttributes instanceof EpsBearerQosSessionAttributes) {
            queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda15(i, i2, qosSessionAttributes));
        } else if (qosSessionAttributes instanceof NrQosSessionAttributes) {
            queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda16(i, i2, qosSessionAttributes));
        }
    }

    public final void sendQosSessionLost(int i, int i2, int i3) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda1(i, i2, i3));
    }

    public final void sendQosCallbackError(int i, int i2) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda10(i, i2));
    }

    public void setLingerDuration(Duration duration) {
        Objects.requireNonNull(duration);
        long millis = duration.toMillis();
        if (millis < StatsManager.DEFAULT_TIMEOUT_MILLIS || millis > 2147483647L) {
            throw new IllegalArgumentException("Duration must be within [2000,2147483647]ms");
        }
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda0(millis));
    }

    public void sendAddDscpPolicy(DscpPolicy dscpPolicy) {
        Objects.requireNonNull(dscpPolicy);
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda3(dscpPolicy));
    }

    public void sendRemoveDscpPolicy(int i) {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda4(i));
    }

    public void sendRemoveAllDscpPolicies() {
        queueOrSendMessage(new NetworkAgent$$ExternalSyntheticLambda12());
    }

    /* access modifiers changed from: protected */
    public void log(String str) {
        String str2 = this.LOG_TAG;
        Log.d(str2, "NetworkAgent: " + str);
    }
}
