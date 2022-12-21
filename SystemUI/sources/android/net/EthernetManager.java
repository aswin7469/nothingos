package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.IEthernetServiceListener;
import android.net.INetworkInterfaceOutcomeReceiver;
import android.net.ITetheredInterfaceCallback;
import android.net.connectivity.com.android.modules.utils.BackgroundThread;
import android.os.OutcomeReceiver;
import android.os.RemoteException;
import android.util.ArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;

@SystemApi
public class EthernetManager {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int ETHERNET_STATE_DISABLED = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int ETHERNET_STATE_ENABLED = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int ROLE_CLIENT = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int ROLE_NONE = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int ROLE_SERVER = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int STATE_ABSENT = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int STATE_LINK_DOWN = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int STATE_LINK_UP = 2;
    private static final String TAG = "EthernetManager";
    private final ArrayMap<InterfaceStateListener, IEthernetServiceListener> mIfaceServiceListeners = new ArrayMap<>();
    final Object mListenerLock = new Object();
    private final IEthernetManager mService;
    private final ArrayMap<IntConsumer, IEthernetServiceListener> mStateServiceListeners = new ArrayMap<>();

    @Retention(RetentionPolicy.SOURCE)
    public @interface InterfaceState {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface InterfaceStateListener {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Role {
    }

    public interface TetheredInterfaceCallback {
        void onAvailable(String str);

        void onUnavailable();
    }

    public interface Listener extends InterfaceStateListener {
        void onAvailabilityChanged(String str, boolean z);

        void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) {
            onAvailabilityChanged(str, i >= 2);
        }
    }

    public EthernetManager(Context context, IEthernetManager iEthernetManager) {
        this.mService = iEthernetManager;
    }

    public IpConfiguration getConfiguration(String str) {
        try {
            return this.mService.getConfiguration(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setConfiguration(String str, IpConfiguration ipConfiguration) {
        try {
            this.mService.setConfiguration(str, ipConfiguration);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isAvailable() {
        return getAvailableInterfaces().length > 0;
    }

    public boolean isAvailable(String str) {
        try {
            return this.mService.isAvailable(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addListener(Listener listener) {
        addListener(listener, BackgroundThread.getExecutor());
    }

    public void addListener(Listener listener, Executor executor) {
        addInterfaceStateListener(executor, listener);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addInterfaceStateListener(final Executor executor, final InterfaceStateListener interfaceStateListener) {
        if (interfaceStateListener == null || executor == null) {
            throw new NullPointerException("listener and executor must not be null");
        }
        C00551 r0 = new IEthernetServiceListener.Stub() {
            public void onEthernetStateChanged(int i) {
            }

            public void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) {
                executor.execute(new EthernetManager$1$$ExternalSyntheticLambda0(interfaceStateListener, str, i, i2, ipConfiguration));
            }
        };
        synchronized (this.mListenerLock) {
            addServiceListener(r0);
            this.mIfaceServiceListeners.put(interfaceStateListener, r0);
        }
    }

    private void addServiceListener(IEthernetServiceListener iEthernetServiceListener) {
        try {
            this.mService.addListener(iEthernetServiceListener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getAvailableInterfaces() {
        try {
            return this.mService.getAvailableInterfaces();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void removeInterfaceStateListener(InterfaceStateListener interfaceStateListener) {
        Objects.requireNonNull(interfaceStateListener);
        synchronized (this.mListenerLock) {
            maybeRemoveServiceListener(this.mIfaceServiceListeners.remove(interfaceStateListener));
        }
    }

    private void maybeRemoveServiceListener(IEthernetServiceListener iEthernetServiceListener) {
        if (iEthernetServiceListener != null) {
            try {
                this.mService.removeListener(iEthernetServiceListener);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void removeListener(Listener listener) {
        if (listener != null) {
            removeInterfaceStateListener(listener);
            return;
        }
        throw new IllegalArgumentException("listener must not be null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setIncludeTestInterfaces(boolean z) {
        try {
            this.mService.setIncludeTestInterfaces(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static class TetheredInterfaceRequest {
        private final ITetheredInterfaceCallback mCb;
        private final IEthernetManager mService;

        private TetheredInterfaceRequest(IEthernetManager iEthernetManager, ITetheredInterfaceCallback iTetheredInterfaceCallback) {
            this.mService = iEthernetManager;
            this.mCb = iTetheredInterfaceCallback;
        }

        public void release() {
            try {
                this.mService.releaseTetheredInterface(this.mCb);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    public TetheredInterfaceRequest requestTetheredInterface(final Executor executor, final TetheredInterfaceCallback tetheredInterfaceCallback) {
        Objects.requireNonNull(tetheredInterfaceCallback, "Callback must be non-null");
        Objects.requireNonNull(executor, "Executor must be non-null");
        C00562 r0 = new ITetheredInterfaceCallback.Stub() {
            public void onAvailable(String str) {
                executor.execute(new EthernetManager$2$$ExternalSyntheticLambda0(tetheredInterfaceCallback, str));
            }

            public void onUnavailable() {
                executor.execute(new EthernetManager$2$$ExternalSyntheticLambda1(tetheredInterfaceCallback));
            }
        };
        try {
            this.mService.requestTetheredInterface(r0);
            return new TetheredInterfaceRequest(this.mService, r0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static final class NetworkInterfaceOutcomeReceiver extends INetworkInterfaceOutcomeReceiver.Stub {
        private final OutcomeReceiver<String, EthernetNetworkManagementException> mCallback;
        private final Executor mExecutor;

        NetworkInterfaceOutcomeReceiver(Executor executor, OutcomeReceiver<String, EthernetNetworkManagementException> outcomeReceiver) {
            Objects.requireNonNull(executor, "Pass a non-null executor");
            Objects.requireNonNull(outcomeReceiver, "Pass a non-null callback");
            this.mExecutor = executor;
            this.mCallback = outcomeReceiver;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onResult$0$android-net-EthernetManager$NetworkInterfaceOutcomeReceiver */
        public /* synthetic */ void mo2139x4c07e8db(String str) {
            this.mCallback.onResult(str);
        }

        public void onResult(String str) {
            this.mExecutor.execute(new C0058xe536caf7(this, str));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onError$1$android-net-EthernetManager$NetworkInterfaceOutcomeReceiver */
        public /* synthetic */ void mo2138x83454e8f(EthernetNetworkManagementException ethernetNetworkManagementException) {
            this.mCallback.onError(ethernetNetworkManagementException);
        }

        public void onError(EthernetNetworkManagementException ethernetNetworkManagementException) {
            this.mExecutor.execute(new C0059xe536caf8(this, ethernetNetworkManagementException));
        }
    }

    private NetworkInterfaceOutcomeReceiver makeNetworkInterfaceOutcomeReceiver(Executor executor, OutcomeReceiver<String, EthernetNetworkManagementException> outcomeReceiver) {
        if (outcomeReceiver != null) {
            Objects.requireNonNull(executor, "Pass a non-null executor, or a null callback");
        }
        if (outcomeReceiver == null) {
            return null;
        }
        return new NetworkInterfaceOutcomeReceiver(executor, outcomeReceiver);
    }

    @SystemApi
    public void updateConfiguration(String str, EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest, Executor executor, OutcomeReceiver<String, EthernetNetworkManagementException> outcomeReceiver) {
        Objects.requireNonNull(str, "iface must be non-null");
        Objects.requireNonNull(ethernetNetworkUpdateRequest, "request must be non-null");
        try {
            this.mService.updateConfiguration(str, ethernetNetworkUpdateRequest, makeNetworkInterfaceOutcomeReceiver(executor, outcomeReceiver));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void enableInterface(String str, Executor executor, OutcomeReceiver<String, EthernetNetworkManagementException> outcomeReceiver) {
        Objects.requireNonNull(str, "iface must be non-null");
        try {
            this.mService.connectNetwork(str, makeNetworkInterfaceOutcomeReceiver(executor, outcomeReceiver));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void disableInterface(String str, Executor executor, OutcomeReceiver<String, EthernetNetworkManagementException> outcomeReceiver) {
        Objects.requireNonNull(str, "iface must be non-null");
        try {
            this.mService.disconnectNetwork(str, makeNetworkInterfaceOutcomeReceiver(executor, outcomeReceiver));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setEthernetEnabled(boolean z) {
        try {
            this.mService.setEthernetEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addEthernetStateListener(final Executor executor, final IntConsumer intConsumer) {
        Objects.requireNonNull(executor);
        Objects.requireNonNull(intConsumer);
        C00573 r0 = new IEthernetServiceListener.Stub() {
            public void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) {
            }

            public void onEthernetStateChanged(int i) {
                executor.execute(new EthernetManager$3$$ExternalSyntheticLambda0(intConsumer, i));
            }
        };
        synchronized (this.mListenerLock) {
            addServiceListener(r0);
            this.mStateServiceListeners.put(intConsumer, r0);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void removeEthernetStateListener(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        synchronized (this.mListenerLock) {
            maybeRemoveServiceListener(this.mStateServiceListeners.remove(intConsumer));
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public List<String> getInterfaceList() {
        try {
            return this.mService.getInterfaceList();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }
}
