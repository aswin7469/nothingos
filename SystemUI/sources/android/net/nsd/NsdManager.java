package android.net.nsd;

import android.app.compat.CompatChanges;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.nsd.INsdManagerCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import java.util.Objects;
import java.util.concurrent.Executor;

public final class NsdManager {
    public static final String ACTION_NSD_STATE_CHANGED = "android.net.nsd.STATE_CHANGED";
    public static final int DAEMON_CLEANUP = 18;
    public static final int DAEMON_STARTUP = 19;
    private static final boolean DBG = false;
    public static final int DISABLE = 21;
    public static final int DISCOVER_SERVICES = 1;
    public static final int DISCOVER_SERVICES_FAILED = 3;
    public static final int DISCOVER_SERVICES_STARTED = 2;
    public static final int ENABLE = 20;
    private static final SparseArray<String> EVENT_NAMES;
    public static final String EXTRA_NSD_STATE = "nsd_state";
    public static final int FAILURE_ALREADY_ACTIVE = 3;
    public static final int FAILURE_INTERNAL_ERROR = 0;
    public static final int FAILURE_MAX_LIMIT = 4;
    private static final int FIRST_LISTENER_KEY = 1;
    public static final int MDNS_SERVICE_EVENT = 22;
    public static final int NSD_STATE_DISABLED = 1;
    public static final int NSD_STATE_ENABLED = 2;
    public static final int PROTOCOL_DNS_SD = 1;
    public static final int REGISTER_CLIENT = 23;
    public static final int REGISTER_SERVICE = 9;
    public static final int REGISTER_SERVICE_FAILED = 10;
    public static final int REGISTER_SERVICE_SUCCEEDED = 11;
    public static final int RESOLVE_SERVICE = 15;
    public static final int RESOLVE_SERVICE_FAILED = 16;
    public static final int RESOLVE_SERVICE_SUCCEEDED = 17;
    public static final long RUN_NATIVE_NSD_ONLY_IF_LEGACY_APPS = 191844585;
    public static final int SERVICE_FOUND = 4;
    public static final int SERVICE_LOST = 5;
    public static final int STOP_DISCOVERY = 6;
    public static final int STOP_DISCOVERY_FAILED = 7;
    public static final int STOP_DISCOVERY_SUCCEEDED = 8;
    /* access modifiers changed from: private */
    public static final String TAG = "NsdManager";
    public static final int UNREGISTER_CLIENT = 24;
    public static final int UNREGISTER_SERVICE = 12;
    public static final int UNREGISTER_SERVICE_FAILED = 13;
    public static final int UNREGISTER_SERVICE_SUCCEEDED = 14;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final SparseArray<Executor> mExecutorMap = new SparseArray<>();
    /* access modifiers changed from: private */
    public final ServiceHandler mHandler;
    private int mListenerKey = 1;
    /* access modifiers changed from: private */
    public final SparseArray mListenerMap = new SparseArray();
    /* access modifiers changed from: private */
    public final Object mMapLock = new Object();
    private final ArrayMap<Integer, PerNetworkDiscoveryTracker> mPerNetworkDiscoveryMap = new ArrayMap<>();
    private final INsdServiceConnector mService;
    /* access modifiers changed from: private */
    public final SparseArray<NsdServiceInfo> mServiceMap = new SparseArray<>();

    public interface DiscoveryListener {
        void onDiscoveryStarted(String str);

        void onDiscoveryStopped(String str);

        void onServiceFound(NsdServiceInfo nsdServiceInfo);

        void onServiceLost(NsdServiceInfo nsdServiceInfo);

        void onStartDiscoveryFailed(String str, int i);

        void onStopDiscoveryFailed(String str, int i);
    }

    public interface RegistrationListener {
        void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i);

        void onServiceRegistered(NsdServiceInfo nsdServiceInfo);

        void onServiceUnregistered(NsdServiceInfo nsdServiceInfo);

        void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i);
    }

    public interface ResolveListener {
        void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i);

        void onServiceResolved(NsdServiceInfo nsdServiceInfo);
    }

    static {
        SparseArray<String> sparseArray = new SparseArray<>();
        EVENT_NAMES = sparseArray;
        sparseArray.put(1, "DISCOVER_SERVICES");
        sparseArray.put(2, "DISCOVER_SERVICES_STARTED");
        sparseArray.put(3, "DISCOVER_SERVICES_FAILED");
        sparseArray.put(4, "SERVICE_FOUND");
        sparseArray.put(5, "SERVICE_LOST");
        sparseArray.put(6, "STOP_DISCOVERY");
        sparseArray.put(7, "STOP_DISCOVERY_FAILED");
        sparseArray.put(8, "STOP_DISCOVERY_SUCCEEDED");
        sparseArray.put(9, "REGISTER_SERVICE");
        sparseArray.put(10, "REGISTER_SERVICE_FAILED");
        sparseArray.put(11, "REGISTER_SERVICE_SUCCEEDED");
        sparseArray.put(12, "UNREGISTER_SERVICE");
        sparseArray.put(13, "UNREGISTER_SERVICE_FAILED");
        sparseArray.put(14, "UNREGISTER_SERVICE_SUCCEEDED");
        sparseArray.put(15, "RESOLVE_SERVICE");
        sparseArray.put(16, "RESOLVE_SERVICE_FAILED");
        sparseArray.put(17, "RESOLVE_SERVICE_SUCCEEDED");
        sparseArray.put(18, "DAEMON_CLEANUP");
        sparseArray.put(19, "DAEMON_STARTUP");
        sparseArray.put(20, "ENABLE");
        sparseArray.put(21, "DISABLE");
        sparseArray.put(22, "MDNS_SERVICE_EVENT");
    }

    public static String nameOf(int i) {
        String str = EVENT_NAMES.get(i);
        return str == null ? Integer.toString(i) : str;
    }

    private class PerNetworkDiscoveryTracker {
        final Executor mBaseExecutor;
        final DiscoveryListener mBaseListener;
        final ConnectivityManager.NetworkCallback mNetworkCb;
        final ArrayMap<Network, DelegatingDiscoveryListener> mPerNetworkListeners;
        final int mProtocolType;
        final String mServiceType;
        /* access modifiers changed from: private */
        public boolean mStopRequested;

        public void start(NetworkRequest networkRequest) {
            ((ConnectivityManager) NsdManager.this.mContext.getSystemService(ConnectivityManager.class)).registerNetworkCallback(networkRequest, this.mNetworkCb, NsdManager.this.mHandler);
            NsdManager.this.mHandler.post(new NsdManager$PerNetworkDiscoveryTracker$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$start$1$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker */
        public /* synthetic */ void mo3831x4980c121() {
            this.mBaseExecutor.execute(new NsdManager$PerNetworkDiscoveryTracker$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$start$0$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker */
        public /* synthetic */ void mo3830x202c6be0() {
            this.mBaseListener.onDiscoveryStarted(this.mServiceType);
        }

        public void requestStop() {
            NsdManager.this.mHandler.post(new NsdManager$PerNetworkDiscoveryTracker$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$requestStop$3$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker */
        public /* synthetic */ void mo3829x364c732() {
            this.mStopRequested = true;
            ((ConnectivityManager) NsdManager.this.mContext.getSystemService(ConnectivityManager.class)).unregisterNetworkCallback(this.mNetworkCb);
            if (this.mPerNetworkListeners.size() == 0) {
                this.mBaseExecutor.execute(new NsdManager$PerNetworkDiscoveryTracker$$ExternalSyntheticLambda3(this));
                return;
            }
            for (int i = 0; i < this.mPerNetworkListeners.size(); i++) {
                NsdManager.this.stopServiceDiscovery(this.mPerNetworkListeners.valueAt(i));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$requestStop$2$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker */
        public /* synthetic */ void mo3828xda1071f1() {
            this.mBaseListener.onDiscoveryStopped(this.mServiceType);
        }

        private PerNetworkDiscoveryTracker(String str, int i, Executor executor, DiscoveryListener discoveryListener) {
            this.mPerNetworkListeners = new ArrayMap<>();
            this.mNetworkCb = new ConnectivityManager.NetworkCallback() {
                public void onAvailable(Network network) {
                    PerNetworkDiscoveryTracker perNetworkDiscoveryTracker = PerNetworkDiscoveryTracker.this;
                    DelegatingDiscoveryListener delegatingDiscoveryListener = new DelegatingDiscoveryListener(network, perNetworkDiscoveryTracker.mBaseListener, PerNetworkDiscoveryTracker.this.mBaseExecutor);
                    PerNetworkDiscoveryTracker.this.mPerNetworkListeners.put(network, delegatingDiscoveryListener);
                    NsdManager.this.discoverServices(PerNetworkDiscoveryTracker.this.mServiceType, PerNetworkDiscoveryTracker.this.mProtocolType, network, (Executor) new NsdManager$$ExternalSyntheticLambda0(), (DiscoveryListener) delegatingDiscoveryListener);
                }

                public void onLost(Network network) {
                    DelegatingDiscoveryListener delegatingDiscoveryListener = PerNetworkDiscoveryTracker.this.mPerNetworkListeners.get(network);
                    if (delegatingDiscoveryListener != null) {
                        delegatingDiscoveryListener.notifyAllServicesLost();
                        NsdManager.this.stopServiceDiscovery(delegatingDiscoveryListener);
                    }
                }
            };
            this.mServiceType = str;
            this.mProtocolType = i;
            this.mBaseExecutor = executor;
            this.mBaseListener = discoveryListener;
        }

        private class TrackedNsdInfo {
            /* access modifiers changed from: private */
            public final String mServiceName;
            /* access modifiers changed from: private */
            public final String mServiceType;

            TrackedNsdInfo(NsdServiceInfo nsdServiceInfo) {
                this.mServiceName = nsdServiceInfo.getServiceName();
                this.mServiceType = nsdServiceInfo.getServiceType();
            }

            public int hashCode() {
                return Objects.hash(this.mServiceName, this.mServiceType);
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof TrackedNsdInfo)) {
                    return false;
                }
                TrackedNsdInfo trackedNsdInfo = (TrackedNsdInfo) obj;
                if (!Objects.equals(this.mServiceName, trackedNsdInfo.mServiceName) || !Objects.equals(this.mServiceType, trackedNsdInfo.mServiceType)) {
                    return false;
                }
                return true;
            }
        }

        private class DelegatingDiscoveryListener implements DiscoveryListener {
            private final ArraySet<TrackedNsdInfo> mFoundInfo;
            private final Network mNetwork;
            private final DiscoveryListener mWrapped;
            private final Executor mWrappedExecutor;

            public void onDiscoveryStarted(String str) {
            }

            private DelegatingDiscoveryListener(Network network, DiscoveryListener discoveryListener, Executor executor) {
                this.mFoundInfo = new ArraySet<>();
                this.mNetwork = network;
                this.mWrapped = discoveryListener;
                this.mWrappedExecutor = executor;
            }

            /* access modifiers changed from: package-private */
            public void notifyAllServicesLost() {
                for (int i = 0; i < this.mFoundInfo.size(); i++) {
                    TrackedNsdInfo valueAt = this.mFoundInfo.valueAt(i);
                    NsdServiceInfo nsdServiceInfo = new NsdServiceInfo(valueAt.mServiceName, valueAt.mServiceType);
                    nsdServiceInfo.setNetwork(this.mNetwork);
                    this.mWrappedExecutor.execute(new C0130x95a94bbd(this, nsdServiceInfo));
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$notifyAllServicesLost$0$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener */
            public /* synthetic */ void mo3834x6a2b42b6(NsdServiceInfo nsdServiceInfo) {
                this.mWrapped.onServiceLost(nsdServiceInfo);
            }

            public void onStartDiscoveryFailed(String str, int i) {
                String r0 = NsdManager.TAG;
                Log.e(r0, "Failed to start discovery for " + str + " on " + this.mNetwork + " with code " + i);
                PerNetworkDiscoveryTracker.this.mPerNetworkListeners.remove(this.mNetwork);
            }

            public void onStopDiscoveryFailed(String str, int i) {
                String r0 = NsdManager.TAG;
                Log.e(r0, "Failed to stop discovery for " + str + " on " + this.mNetwork + " with code " + i);
                PerNetworkDiscoveryTracker.this.mPerNetworkListeners.remove(this.mNetwork);
                if (PerNetworkDiscoveryTracker.this.mStopRequested && PerNetworkDiscoveryTracker.this.mPerNetworkListeners.size() == 0) {
                    this.mWrappedExecutor.execute(new C0131x95a94bbe(this, str));
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStopDiscoveryFailed$1$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener */
            public /* synthetic */ void mo3838xcc9f8e69(String str) {
                this.mWrapped.onDiscoveryStopped(str);
            }

            public void onDiscoveryStopped(String str) {
                PerNetworkDiscoveryTracker.this.mPerNetworkListeners.remove(this.mNetwork);
                if (PerNetworkDiscoveryTracker.this.mStopRequested && PerNetworkDiscoveryTracker.this.mPerNetworkListeners.size() == 0) {
                    this.mWrappedExecutor.execute(new C0133x95a94bc0(this, str));
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDiscoveryStopped$2$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener */
            public /* synthetic */ void mo3835x8f61d8f4(String str) {
                this.mWrapped.onDiscoveryStopped(str);
            }

            public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                this.mFoundInfo.add(new TrackedNsdInfo(nsdServiceInfo));
                this.mWrappedExecutor.execute(new C0132x95a94bbf(this, nsdServiceInfo));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onServiceFound$3$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener */
            public /* synthetic */ void mo3836x8d6c7485(NsdServiceInfo nsdServiceInfo) {
                this.mWrapped.onServiceFound(nsdServiceInfo);
            }

            public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
                this.mFoundInfo.remove(new TrackedNsdInfo(nsdServiceInfo));
                this.mWrappedExecutor.execute(new C0129x95a94bbc(this, nsdServiceInfo));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onServiceLost$4$android-net-nsd-NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener */
            public /* synthetic */ void mo3837x79ccae7a(NsdServiceInfo nsdServiceInfo) {
                this.mWrapped.onServiceLost(nsdServiceInfo);
            }
        }
    }

    public NsdManager(Context context, INsdManager iNsdManager) {
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        ServiceHandler serviceHandler = new ServiceHandler(handlerThread.getLooper());
        this.mHandler = serviceHandler;
        try {
            INsdServiceConnector connect = iNsdManager.connect(new NsdCallbackImpl(serviceHandler));
            this.mService = connect;
            if (!CompatChanges.isChangeEnabled(RUN_NATIVE_NSD_ONLY_IF_LEGACY_APPS)) {
                try {
                    connect.startDaemon();
                } catch (RemoteException unused) {
                    Log.e(TAG, "Failed to proactively start daemon");
                }
            }
        } catch (RemoteException unused2) {
            throw new RuntimeException("Failed to connect to NsdService");
        }
    }

    private static class NsdCallbackImpl extends INsdManagerCallback.Stub {
        private final Handler mServHandler;

        NsdCallbackImpl(Handler handler) {
            this.mServHandler = handler;
        }

        private void sendInfo(int i, int i2, NsdServiceInfo nsdServiceInfo) {
            Handler handler = this.mServHandler;
            handler.sendMessage(handler.obtainMessage(i, 0, i2, nsdServiceInfo));
        }

        private void sendError(int i, int i2, int i3) {
            Handler handler = this.mServHandler;
            handler.sendMessage(handler.obtainMessage(i, i3, i2));
        }

        private void sendNoArg(int i, int i2) {
            Handler handler = this.mServHandler;
            handler.sendMessage(handler.obtainMessage(i, 0, i2));
        }

        public void onDiscoverServicesStarted(int i, NsdServiceInfo nsdServiceInfo) {
            sendInfo(2, i, nsdServiceInfo);
        }

        public void onDiscoverServicesFailed(int i, int i2) {
            sendError(3, i, i2);
        }

        public void onServiceFound(int i, NsdServiceInfo nsdServiceInfo) {
            sendInfo(4, i, nsdServiceInfo);
        }

        public void onServiceLost(int i, NsdServiceInfo nsdServiceInfo) {
            sendInfo(5, i, nsdServiceInfo);
        }

        public void onStopDiscoveryFailed(int i, int i2) {
            sendError(7, i, i2);
        }

        public void onStopDiscoverySucceeded(int i) {
            sendNoArg(8, i);
        }

        public void onRegisterServiceFailed(int i, int i2) {
            sendError(10, i, i2);
        }

        public void onRegisterServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) {
            sendInfo(11, i, nsdServiceInfo);
        }

        public void onUnregisterServiceFailed(int i, int i2) {
            sendError(13, i, i2);
        }

        public void onUnregisterServiceSucceeded(int i) {
            sendNoArg(14, i);
        }

        public void onResolveServiceFailed(int i, int i2) {
            sendError(16, i, i2);
        }

        public void onResolveServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) {
            sendInfo(17, i, nsdServiceInfo);
        }
    }

    class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Object obj;
            NsdServiceInfo nsdServiceInfo;
            Executor executor;
            int i = message.what;
            int i2 = message.arg1;
            int i3 = message.arg2;
            Object obj2 = message.obj;
            synchronized (NsdManager.this.mMapLock) {
                obj = NsdManager.this.mListenerMap.get(i3);
                nsdServiceInfo = (NsdServiceInfo) NsdManager.this.mServiceMap.get(i3);
                executor = (Executor) NsdManager.this.mExecutorMap.get(i3);
            }
            if (obj == null) {
                String r8 = NsdManager.TAG;
                Log.d(r8, "Stale key " + i3);
                return;
            }
            switch (i) {
                case 2:
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda0(obj, NsdManager.getNsdServiceInfoType((NsdServiceInfo) obj2)));
                    return;
                case 3:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda5(obj, nsdServiceInfo, i2));
                    return;
                case 4:
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda6(obj, obj2));
                    return;
                case 5:
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda7(obj, obj2));
                    return;
                case 7:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda8(obj, nsdServiceInfo, i2));
                    return;
                case 8:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda9(obj, nsdServiceInfo));
                    return;
                case 10:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda10(obj, nsdServiceInfo, i2));
                    return;
                case 11:
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda11(obj, obj2));
                    return;
                case 13:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda1(obj, nsdServiceInfo, i2));
                    return;
                case 14:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda2(obj, nsdServiceInfo));
                    return;
                case 16:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda3(obj, nsdServiceInfo, i2));
                    return;
                case 17:
                    NsdManager.this.removeListener(i3);
                    executor.execute(new NsdManager$ServiceHandler$$ExternalSyntheticLambda4(obj, obj2));
                    return;
                default:
                    String r82 = NsdManager.TAG;
                    Log.d(r82, "Ignored " + message);
                    return;
            }
        }
    }

    private int nextListenerKey() {
        int max = Math.max(1, this.mListenerKey + 1);
        this.mListenerKey = max;
        return max;
    }

    private int putListener(Object obj, Executor executor, NsdServiceInfo nsdServiceInfo) {
        int nextListenerKey;
        checkListener(obj);
        synchronized (this.mMapLock) {
            if (this.mListenerMap.indexOfValue(obj) == -1) {
                nextListenerKey = nextListenerKey();
                this.mListenerMap.put(nextListenerKey, obj);
                this.mServiceMap.put(nextListenerKey, nsdServiceInfo);
                this.mExecutorMap.put(nextListenerKey, executor);
            } else {
                throw new IllegalArgumentException("listener already in use");
            }
        }
        return nextListenerKey;
    }

    /* access modifiers changed from: private */
    public void removeListener(int i) {
        synchronized (this.mMapLock) {
            this.mListenerMap.remove(i);
            this.mServiceMap.remove(i);
            this.mExecutorMap.remove(i);
        }
    }

    private int getListenerKey(Object obj) {
        int keyAt;
        checkListener(obj);
        synchronized (this.mMapLock) {
            int indexOfValue = this.mListenerMap.indexOfValue(obj);
            if (indexOfValue != -1) {
                keyAt = this.mListenerMap.keyAt(indexOfValue);
            } else {
                throw new IllegalArgumentException("listener not registered");
            }
        }
        return keyAt;
    }

    /* access modifiers changed from: private */
    public static String getNsdServiceInfoType(NsdServiceInfo nsdServiceInfo) {
        return nsdServiceInfo == null ? "?" : nsdServiceInfo.getServiceType();
    }

    public void registerService(NsdServiceInfo nsdServiceInfo, int i, RegistrationListener registrationListener) {
        registerService(nsdServiceInfo, i, new NsdManager$$ExternalSyntheticLambda0(), registrationListener);
    }

    public void registerService(NsdServiceInfo nsdServiceInfo, int i, Executor executor, RegistrationListener registrationListener) {
        if (nsdServiceInfo.getPort() > 0) {
            checkServiceInfo(nsdServiceInfo);
            checkProtocol(i);
            try {
                this.mService.registerService(putListener(registrationListener, executor, nsdServiceInfo), nsdServiceInfo);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Invalid port number");
        }
    }

    public void unregisterService(RegistrationListener registrationListener) {
        try {
            this.mService.unregisterService(getListenerKey(registrationListener));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void discoverServices(String str, int i, DiscoveryListener discoveryListener) {
        Network network = null;
        discoverServices(str, i, (Network) null, (Executor) new NsdManager$$ExternalSyntheticLambda0(), discoveryListener);
    }

    public void discoverServices(String str, int i, Network network, Executor executor, DiscoveryListener discoveryListener) {
        if (!TextUtils.isEmpty(str)) {
            checkProtocol(i);
            NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.setServiceType(str);
            nsdServiceInfo.setNetwork(network);
            try {
                this.mService.discoverServices(putListener(discoveryListener, executor, nsdServiceInfo), nsdServiceInfo);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Service type cannot be empty");
        }
    }

    public void discoverServices(String str, int i, NetworkRequest networkRequest, Executor executor, DiscoveryListener discoveryListener) {
        if (!TextUtils.isEmpty(str)) {
            Objects.requireNonNull(networkRequest, "NetworkRequest cannot be null");
            checkProtocol(i);
            NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.setServiceType(str);
            int putListener = putListener(discoveryListener, executor, nsdServiceInfo);
            PerNetworkDiscoveryTracker perNetworkDiscoveryTracker = new PerNetworkDiscoveryTracker(str, i, executor, discoveryListener);
            synchronized (this.mPerNetworkDiscoveryMap) {
                this.mPerNetworkDiscoveryMap.put(Integer.valueOf(putListener), perNetworkDiscoveryTracker);
                perNetworkDiscoveryTracker.start(networkRequest);
            }
            return;
        }
        throw new IllegalArgumentException("Service type cannot be empty");
    }

    public void stopServiceDiscovery(DiscoveryListener discoveryListener) {
        int listenerKey = getListenerKey(discoveryListener);
        synchronized (this.mPerNetworkDiscoveryMap) {
            PerNetworkDiscoveryTracker perNetworkDiscoveryTracker = this.mPerNetworkDiscoveryMap.get(Integer.valueOf(listenerKey));
            if (perNetworkDiscoveryTracker != null) {
                perNetworkDiscoveryTracker.requestStop();
                return;
            }
            try {
                this.mService.stopDiscovery(listenerKey);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    public void resolveService(NsdServiceInfo nsdServiceInfo, ResolveListener resolveListener) {
        resolveService(nsdServiceInfo, new NsdManager$$ExternalSyntheticLambda0(), resolveListener);
    }

    public void resolveService(NsdServiceInfo nsdServiceInfo, Executor executor, ResolveListener resolveListener) {
        checkServiceInfo(nsdServiceInfo);
        try {
            this.mService.resolveService(putListener(resolveListener, executor, nsdServiceInfo), nsdServiceInfo);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    private static void checkListener(Object obj) {
        Objects.requireNonNull(obj, "listener cannot be null");
    }

    private static void checkProtocol(int i) {
        if (i != 1) {
            throw new IllegalArgumentException("Unsupported protocol");
        }
    }

    private static void checkServiceInfo(NsdServiceInfo nsdServiceInfo) {
        Objects.requireNonNull(nsdServiceInfo, "NsdServiceInfo cannot be null");
        if (TextUtils.isEmpty(nsdServiceInfo.getServiceName())) {
            throw new IllegalArgumentException("Service name cannot be empty");
        } else if (TextUtils.isEmpty(nsdServiceInfo.getServiceType())) {
            throw new IllegalArgumentException("Service type cannot be empty");
        }
    }
}
