package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.IIntResultListener;
import android.net.ITetheringConnector;
import android.net.ITetheringEventCallback;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@SystemApi
public class TetheringManager {
    @Deprecated
    public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
    public static final int CONNECTIVITY_SCOPE_GLOBAL = 1;
    public static final int CONNECTIVITY_SCOPE_LOCAL = 2;
    private static final long CONNECTOR_POLL_INTERVAL_MILLIS = 200;
    private static final int DEFAULT_TIMEOUT_MS = 60000;
    public static final String EXTRA_ACTIVE_LOCAL_ONLY = "android.net.extra.ACTIVE_LOCAL_ONLY";
    public static final String EXTRA_ACTIVE_TETHER = "tetherArray";
    public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
    public static final String EXTRA_ERRORED_TETHER = "erroredArray";
    public static final int MAX_TETHERING_TYPE = 6;
    private static final String TAG = "TetheringManager";
    public static final int TETHERING_BLUETOOTH = 2;
    public static final int TETHERING_ETHERNET = 5;
    public static final int TETHERING_INVALID = -1;
    public static final int TETHERING_NCM = 4;
    public static final int TETHERING_USB = 1;
    public static final int TETHERING_WIFI = 0;
    public static final int TETHERING_WIFI_P2P = 3;
    public static final int TETHERING_WIGIG = 6;
    public static final int TETHER_ERROR_DHCPSERVER_ERROR = 12;
    public static final int TETHER_ERROR_DISABLE_FORWARDING_ERROR = 9;
    public static final int TETHER_ERROR_ENABLE_FORWARDING_ERROR = 8;
    public static final int TETHER_ERROR_ENTITLEMENT_UNKNOWN = 13;
    public static final int TETHER_ERROR_IFACE_CFG_ERROR = 10;
    public static final int TETHER_ERROR_INTERNAL_ERROR = 5;
    public static final int TETHER_ERROR_NO_ACCESS_TETHERING_PERMISSION = 15;
    public static final int TETHER_ERROR_NO_CHANGE_TETHERING_PERMISSION = 14;
    public static final int TETHER_ERROR_NO_ERROR = 0;
    public static final int TETHER_ERROR_PROVISIONING_FAILED = 11;
    public static final int TETHER_ERROR_SERVICE_UNAVAIL = 2;
    public static final int TETHER_ERROR_TETHER_IFACE_ERROR = 6;
    public static final int TETHER_ERROR_UNAVAIL_IFACE = 4;
    public static final int TETHER_ERROR_UNKNOWN_IFACE = 1;
    public static final int TETHER_ERROR_UNKNOWN_TYPE = 16;
    public static final int TETHER_ERROR_UNSUPPORTED = 3;
    public static final int TETHER_ERROR_UNTETHER_IFACE_ERROR = 7;
    public static final int TETHER_HARDWARE_OFFLOAD_FAILED = 2;
    public static final int TETHER_HARDWARE_OFFLOAD_STARTED = 1;
    public static final int TETHER_HARDWARE_OFFLOAD_STOPPED = 0;
    private final TetheringCallbackInternal mCallback;
    private ITetheringConnector mConnector;
    private final Supplier<IBinder> mConnectorSupplier;
    private final List<ConnectorConsumer> mConnectorWaitQueue = new ArrayList();
    private final Context mContext;
    /* access modifiers changed from: private */
    public volatile TetherStatesParcel mTetherStatesParcel;
    /* access modifiers changed from: private */
    public volatile TetheringConfigurationParcel mTetheringConfiguration;
    private final ArrayMap<TetheringEventCallback, ITetheringEventCallback> mTetheringEventCallbacks = new ArrayMap<>();

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectivityScope {
    }

    private interface ConnectorConsumer {
        void onConnectorAvailable(ITetheringConnector iTetheringConnector) throws RemoteException;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntitlementResult {
    }

    public interface OnTetheringEntitlementResultListener {
        void onTetheringEntitlementResult(int i);
    }

    private interface RequestHelper {
        void runRequest(ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener);
    }

    public interface StartTetheringCallback {
        void onTetheringFailed(int i) {
        }

        void onTetheringStarted() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StartTetheringError {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TetherOffloadStatus {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface TetheredInterfaceCallback {
        void onAvailable(String str);

        void onUnavailable();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface TetheredInterfaceRequest {
        void release();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TetheringIfaceError {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TetheringType {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public TetheringManager(Context context, Supplier<IBinder> supplier) {
        this.mContext = context;
        this.mCallback = new TetheringCallbackInternal(this);
        this.mConnectorSupplier = supplier;
        String opPackageName = context.getOpPackageName();
        IBinder iBinder = supplier.get();
        if (iBinder != null) {
            this.mConnector = ITetheringConnector.Stub.asInterface(iBinder);
        } else {
            startPollingForConnector();
        }
        String str = TAG;
        Log.i(str, "registerTetheringEventCallback:" + opPackageName);
        getConnector(new TetheringManager$$ExternalSyntheticLambda8(this, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1944lambda$new$0$androidnetTetheringManager(String str, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.registerTetheringEventCallback(this.mCallback, str);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "unregisterTetheringEventCallback:" + opPackageName);
        if (this.mConnector == null) {
            Log.wtf(str, "null connector in finalize!");
        }
        getConnector(new TetheringManager$$ExternalSyntheticLambda9(this, opPackageName));
        super.finalize();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$finalize$1$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1942lambda$finalize$1$androidnetTetheringManager(String str, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.unregisterTetheringEventCallback(this.mCallback, str);
    }

    private void startPollingForConnector() {
        new Thread((Runnable) new TetheringManager$$ExternalSyntheticLambda10(this)).start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startPollingForConnector$2$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1946lambda$startPollingForConnector$2$androidnetTetheringManager() {
        IBinder iBinder;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException unused) {
            }
            iBinder = this.mConnectorSupplier.get();
        } while (iBinder == null);
        onTetheringConnected(ITetheringConnector.Stub.asInterface(iBinder));
    }

    private void onTetheringConnected(ITetheringConnector iTetheringConnector) {
        ArrayList<ConnectorConsumer> arrayList;
        while (true) {
            synchronized (this.mConnectorWaitQueue) {
                arrayList = new ArrayList<>(this.mConnectorWaitQueue);
                this.mConnectorWaitQueue.clear();
            }
            for (ConnectorConsumer onConnectorAvailable : arrayList) {
                try {
                    onConnectorAvailable.onConnectorAvailable(iTetheringConnector);
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error processing request for the tethering connector", e);
                }
            }
            synchronized (this.mConnectorWaitQueue) {
                if (this.mConnectorWaitQueue.size() == 0) {
                    this.mConnector = iTetheringConnector;
                    return;
                }
            }
        }
        while (true) {
        }
    }

    /* access modifiers changed from: private */
    public void getConnector(ConnectorConsumer connectorConsumer) {
        synchronized (this.mConnectorWaitQueue) {
            ITetheringConnector iTetheringConnector = this.mConnector;
            if (iTetheringConnector == null) {
                this.mConnectorWaitQueue.add(connectorConsumer);
                return;
            }
            try {
                connectorConsumer.onConnectorAvailable(iTetheringConnector);
            } catch (RemoteException e) {
                throw new IllegalStateException((Throwable) e);
            }
        }
    }

    private class RequestDispatcher {
        private final IIntResultListener mListener = new IIntResultListener.Stub() {
            public void onResult(int i) {
                RequestDispatcher.this.mRemoteResult = i;
                RequestDispatcher.this.mWaiting.open();
            }
        };
        public volatile int mRemoteResult;
        /* access modifiers changed from: private */
        public final ConditionVariable mWaiting = new ConditionVariable();

        RequestDispatcher() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$waitForResult$0$android-net-TetheringManager$RequestDispatcher */
        public /* synthetic */ void mo3510xf198a361(RequestHelper requestHelper, ITetheringConnector iTetheringConnector) throws RemoteException {
            requestHelper.runRequest(iTetheringConnector, this.mListener);
        }

        /* access modifiers changed from: package-private */
        public int waitForResult(RequestHelper requestHelper) {
            TetheringManager.this.getConnector(new TetheringManager$RequestDispatcher$$ExternalSyntheticLambda0(this, requestHelper));
            if (this.mWaiting.block(60000)) {
                TetheringManager.throwIfPermissionFailure(this.mRemoteResult);
                return this.mRemoteResult;
            }
            throw new IllegalStateException("Callback timeout");
        }
    }

    /* access modifiers changed from: private */
    public static void throwIfPermissionFailure(int i) {
        if (i == 14) {
            throw new SecurityException("No android.permission.TETHER_PRIVILEGED or android.permission.WRITE_SETTINGS permission");
        } else if (i == 15) {
            throw new SecurityException("No android.permission.ACCESS_NETWORK_STATE permission");
        }
    }

    private static class TetheringCallbackInternal extends ITetheringEventCallback.Stub {
        private volatile int mError = 0;
        private final WeakReference<TetheringManager> mTetheringMgrRef;
        private final ConditionVariable mWaitForCallback = new ConditionVariable();

        public void onOffloadStatusChanged(int i) {
        }

        public void onSupportedTetheringTypes(long j) {
        }

        public void onTetherClientsChanged(List<TetheredClient> list) {
        }

        public void onUpstreamChanged(Network network) {
        }

        TetheringCallbackInternal(TetheringManager tetheringManager) {
            this.mTetheringMgrRef = new WeakReference<>(tetheringManager);
        }

        public void onCallbackStarted(TetheringCallbackStartedParcel tetheringCallbackStartedParcel) {
            TetheringManager tetheringManager = this.mTetheringMgrRef.get();
            if (tetheringManager != null) {
                tetheringManager.mTetheringConfiguration = tetheringCallbackStartedParcel.config;
                tetheringManager.mTetherStatesParcel = tetheringCallbackStartedParcel.states;
                this.mWaitForCallback.open();
            }
        }

        public void onCallbackStopped(int i) {
            if (this.mTetheringMgrRef.get() != null) {
                this.mError = i;
                this.mWaitForCallback.open();
            }
        }

        public void onConfigurationChanged(TetheringConfigurationParcel tetheringConfigurationParcel) {
            TetheringManager tetheringManager = this.mTetheringMgrRef.get();
            if (tetheringManager != null) {
                tetheringManager.mTetheringConfiguration = tetheringConfigurationParcel;
            }
        }

        public void onTetherStatesChanged(TetherStatesParcel tetherStatesParcel) {
            TetheringManager tetheringManager = this.mTetheringMgrRef.get();
            if (tetheringManager != null) {
                tetheringManager.mTetherStatesParcel = tetherStatesParcel;
            }
        }

        public void waitForStarted() {
            this.mWaitForCallback.block(60000);
            TetheringManager.throwIfPermissionFailure(this.mError);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public int tether(String str) {
        String opPackageName = this.mContext.getOpPackageName();
        String str2 = TAG;
        Log.i(str2, "tether caller:" + opPackageName);
        return new RequestDispatcher().waitForResult(new TetheringManager$$ExternalSyntheticLambda12(this, str, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$tether$3$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1950lambda$tether$3$androidnetTetheringManager(String str, String str2, ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        try {
            iTetheringConnector.tether(str, str2, getAttributionTag(), iIntResultListener);
        } catch (RemoteException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    private String getAttributionTag() {
        return this.mContext.getAttributionTag();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public int untether(String str) {
        String opPackageName = this.mContext.getOpPackageName();
        String str2 = TAG;
        Log.i(str2, "untether caller:" + opPackageName);
        return new RequestDispatcher().waitForResult(new TetheringManager$$ExternalSyntheticLambda13(this, str, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$untether$4$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1951lambda$untether$4$androidnetTetheringManager(String str, String str2, ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        try {
            iTetheringConnector.untether(str, str2, getAttributionTag(), iIntResultListener);
        } catch (RemoteException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public int setUsbTethering(boolean z) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "setUsbTethering caller:" + opPackageName);
        return new RequestDispatcher().waitForResult(new TetheringManager$$ExternalSyntheticLambda4(this, z, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setUsbTethering$5$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1945lambda$setUsbTethering$5$androidnetTetheringManager(boolean z, String str, ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        try {
            iTetheringConnector.setUsbTethering(z, str, getAttributionTag(), iIntResultListener);
        } catch (RemoteException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    public static class TetheringRequest {
        private final TetheringRequestParcel mRequestParcel;

        /* access modifiers changed from: private */
        public static boolean checkConnectivityScope(int i, int i2) {
            return i2 == 1 || i == 1 || i == 5 || i == 4;
        }

        public static int getDefaultConnectivityScope(int i) {
            return i != 4 ? 1 : 2;
        }

        private TetheringRequest(TetheringRequestParcel tetheringRequestParcel) {
            this.mRequestParcel = tetheringRequestParcel;
        }

        public static class Builder {
            private final TetheringRequestParcel mBuilderParcel;

            public Builder(int i) {
                TetheringRequestParcel tetheringRequestParcel = new TetheringRequestParcel();
                this.mBuilderParcel = tetheringRequestParcel;
                tetheringRequestParcel.tetheringType = i;
                tetheringRequestParcel.localIPv4Address = null;
                tetheringRequestParcel.staticClientAddress = null;
                tetheringRequestParcel.exemptFromEntitlementCheck = false;
                tetheringRequestParcel.showProvisioningUi = true;
                tetheringRequestParcel.connectivityScope = TetheringRequest.getDefaultConnectivityScope(i);
            }

            public Builder setStaticIpv4Addresses(LinkAddress linkAddress, LinkAddress linkAddress2) {
                Objects.requireNonNull(linkAddress);
                Objects.requireNonNull(linkAddress2);
                if (TetheringRequest.checkStaticAddressConfiguration(linkAddress, linkAddress2)) {
                    this.mBuilderParcel.localIPv4Address = linkAddress;
                    this.mBuilderParcel.staticClientAddress = linkAddress2;
                    return this;
                }
                throw new IllegalArgumentException("Invalid server or client addresses");
            }

            public Builder setExemptFromEntitlementCheck(boolean z) {
                this.mBuilderParcel.exemptFromEntitlementCheck = z;
                return this;
            }

            public Builder setShouldShowEntitlementUi(boolean z) {
                this.mBuilderParcel.showProvisioningUi = z;
                return this;
            }

            public Builder setConnectivityScope(int i) {
                if (TetheringRequest.checkConnectivityScope(this.mBuilderParcel.tetheringType, i)) {
                    this.mBuilderParcel.connectivityScope = i;
                    return this;
                }
                throw new IllegalArgumentException("Invalid connectivity scope " + i);
            }

            public TetheringRequest build() {
                return new TetheringRequest(this.mBuilderParcel);
            }
        }

        public LinkAddress getLocalIpv4Address() {
            return this.mRequestParcel.localIPv4Address;
        }

        public LinkAddress getClientStaticIpv4Address() {
            return this.mRequestParcel.staticClientAddress;
        }

        public int getTetheringType() {
            return this.mRequestParcel.tetheringType;
        }

        public int getConnectivityScope() {
            return this.mRequestParcel.connectivityScope;
        }

        public boolean isExemptFromEntitlementCheck() {
            return this.mRequestParcel.exemptFromEntitlementCheck;
        }

        public boolean getShouldShowEntitlementUi() {
            return this.mRequestParcel.showProvisioningUi;
        }

        public static boolean checkStaticAddressConfiguration(LinkAddress linkAddress, LinkAddress linkAddress2) {
            return linkAddress.getPrefixLength() == linkAddress2.getPrefixLength() && linkAddress.isIpv4() && linkAddress2.isIpv4() && new IpPrefix(linkAddress.toString()).equals(new IpPrefix(linkAddress2.toString()));
        }

        public TetheringRequestParcel getParcel() {
            return this.mRequestParcel;
        }

        public String toString() {
            return "TetheringRequest [ type= " + this.mRequestParcel.tetheringType + ", localIPv4Address= " + this.mRequestParcel.localIPv4Address + ", staticClientAddress= " + this.mRequestParcel.staticClientAddress + ", exemptFromEntitlementCheck= " + this.mRequestParcel.exemptFromEntitlementCheck + ", showProvisioningUi= " + this.mRequestParcel.showProvisioningUi + " ]";
        }
    }

    public void startTethering(TetheringRequest tetheringRequest, final Executor executor, final StartTetheringCallback startTetheringCallback) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "startTethering caller:" + opPackageName);
        getConnector(new TetheringManager$$ExternalSyntheticLambda1(this, tetheringRequest, opPackageName, new IIntResultListener.Stub() {
            public void onResult(int i) {
                executor.execute(new TetheringManager$1$$ExternalSyntheticLambda0(i, startTetheringCallback));
            }

            static /* synthetic */ void lambda$onResult$0(int i, StartTetheringCallback startTetheringCallback) {
                if (i == 0) {
                    startTetheringCallback.onTetheringStarted();
                } else {
                    startTetheringCallback.onTetheringFailed(i);
                }
            }
        }));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startTethering$6$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1947lambda$startTethering$6$androidnetTetheringManager(TetheringRequest tetheringRequest, String str, IIntResultListener iIntResultListener, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.startTethering(tetheringRequest.getParcel(), str, getAttributionTag(), iIntResultListener);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void startTethering(int i, Executor executor, StartTetheringCallback startTetheringCallback) {
        startTethering(new TetheringRequest.Builder(i).build(), executor, startTetheringCallback);
    }

    public void stopTethering(int i) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "stopTethering caller:" + opPackageName);
        getConnector(new TetheringManager$$ExternalSyntheticLambda7(this, i, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$stopTethering$7$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1949lambda$stopTethering$7$androidnetTetheringManager(int i, String str, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.stopTethering(i, str, getAttributionTag(), new IIntResultListener.Stub() {
            public void onResult(int i) {
            }
        });
    }

    public void requestLatestTetheringEntitlementResult(int i, boolean z, final Executor executor, final OnTetheringEntitlementResultListener onTetheringEntitlementResultListener) {
        if (onTetheringEntitlementResultListener != null) {
            requestLatestTetheringEntitlementResult(i, new ResultReceiver((Handler) null) {
                /* access modifiers changed from: protected */
                public void onReceiveResult(int i, Bundle bundle) {
                    executor.execute(new TetheringManager$3$$ExternalSyntheticLambda0(onTetheringEntitlementResultListener, i));
                }
            }, z);
            return;
        }
        throw new IllegalArgumentException("OnTetheringEntitlementResultListener cannot be null.");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void requestLatestTetheringEntitlementResult(int i, ResultReceiver resultReceiver, boolean z) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "getLatestTetheringEntitlementResult caller:" + opPackageName);
        getConnector(new TetheringManager$$ExternalSyntheticLambda0(this, i, resultReceiver, z, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$requestLatestTetheringEntitlementResult$8$android-net-TetheringManager */
    public /* synthetic */ void mo3484x602116e8(int i, ResultReceiver resultReceiver, boolean z, String str, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.requestLatestTetheringEntitlementResult(i, resultReceiver, z, str, getAttributionTag());
    }

    public interface TetheringEventCallback {
        void onClientsChanged(Collection<TetheredClient> collection) {
        }

        void onError(String str, int i) {
        }

        void onLocalOnlyInterfacesChanged(List<String> list) {
        }

        void onOffloadStatusChanged(int i) {
        }

        void onSupportedTetheringTypes(Set<Integer> set) {
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        @Deprecated
        void onTetherableInterfaceRegexpsChanged(TetheringInterfaceRegexps tetheringInterfaceRegexps) {
        }

        void onTetherableInterfacesChanged(List<String> list) {
        }

        void onTetheredInterfacesChanged(List<String> list) {
        }

        void onTetheringSupported(boolean z) {
        }

        void onUpstreamChanged(Network network) {
        }

        void onTetherableInterfacesChanged(Set<TetheringInterface> set) {
            onTetherableInterfacesChanged((List<String>) TetheringManager.toIfaces((Collection<TetheringInterface>) set));
        }

        void onTetheredInterfacesChanged(Set<TetheringInterface> set) {
            onTetheredInterfacesChanged((List<String>) TetheringManager.toIfaces((Collection<TetheringInterface>) set));
        }

        void onLocalOnlyInterfacesChanged(Set<TetheringInterface> set) {
            onLocalOnlyInterfacesChanged((List<String>) TetheringManager.toIfaces((Collection<TetheringInterface>) set));
        }

        void onError(TetheringInterface tetheringInterface, int i) {
            onError(tetheringInterface.getInterface(), i);
        }
    }

    public static ArrayList<String> toIfaces(Collection<TetheringInterface> collection) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (TetheringInterface tetheringInterface : collection) {
            arrayList.add(tetheringInterface.getInterface());
        }
        return arrayList;
    }

    private static String[] toIfaces(TetheringInterface[] tetheringInterfaceArr) {
        String[] strArr = new String[tetheringInterfaceArr.length];
        for (int i = 0; i < tetheringInterfaceArr.length; i++) {
            strArr[i] = tetheringInterfaceArr[i].getInterface();
        }
        return strArr;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public static class TetheringInterfaceRegexps {
        private final String[] mTetherableBluetoothRegexs;
        private final String[] mTetherableUsbRegexs;
        private final String[] mTetherableWifiRegexs;

        public TetheringInterfaceRegexps(String[] strArr, String[] strArr2, String[] strArr3) {
            this.mTetherableBluetoothRegexs = (String[]) strArr.clone();
            this.mTetherableUsbRegexs = (String[]) strArr2.clone();
            this.mTetherableWifiRegexs = (String[]) strArr3.clone();
        }

        public List<String> getTetherableBluetoothRegexs() {
            return Collections.unmodifiableList(Arrays.asList(this.mTetherableBluetoothRegexs));
        }

        public List<String> getTetherableUsbRegexs() {
            return Collections.unmodifiableList(Arrays.asList(this.mTetherableUsbRegexs));
        }

        public List<String> getTetherableWifiRegexs() {
            return Collections.unmodifiableList(Arrays.asList(this.mTetherableWifiRegexs));
        }

        public int hashCode() {
            return Objects.hash(this.mTetherableBluetoothRegexs, this.mTetherableUsbRegexs, this.mTetherableWifiRegexs);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TetheringInterfaceRegexps)) {
                return false;
            }
            TetheringInterfaceRegexps tetheringInterfaceRegexps = (TetheringInterfaceRegexps) obj;
            if (!Arrays.equals((Object[]) this.mTetherableBluetoothRegexs, (Object[]) tetheringInterfaceRegexps.mTetherableBluetoothRegexs) || !Arrays.equals((Object[]) this.mTetherableUsbRegexs, (Object[]) tetheringInterfaceRegexps.mTetherableUsbRegexs) || !Arrays.equals((Object[]) this.mTetherableWifiRegexs, (Object[]) tetheringInterfaceRegexps.mTetherableWifiRegexs)) {
                return false;
            }
            return true;
        }
    }

    public void registerTetheringEventCallback(final Executor executor, final TetheringEventCallback tetheringEventCallback) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "registerTetheringEventCallback caller:" + opPackageName);
        synchronized (this.mTetheringEventCallbacks) {
            if (!this.mTetheringEventCallbacks.containsKey(tetheringEventCallback)) {
                C01124 r2 = new ITetheringEventCallback.Stub() {
                    private final HashMap<TetheringInterface, Integer> mErrorStates = new HashMap<>();
                    private TetheringInterface[] mLastLocalOnlyInterfaces = null;
                    private TetheringInterface[] mLastTetherableInterfaces = null;
                    private TetheringInterface[] mLastTetheredInterfaces = null;

                    public void onUpstreamChanged(Network network) throws RemoteException {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda3(tetheringEventCallback, network));
                    }

                    private synchronized void sendErrorCallbacks(TetherStatesParcel tetherStatesParcel) {
                        for (int i = 0; i < tetherStatesParcel.erroredIfaceList.length; i++) {
                            TetheringInterface tetheringInterface = tetherStatesParcel.erroredIfaceList[i];
                            Integer num = this.mErrorStates.get(tetheringInterface);
                            int i2 = tetherStatesParcel.lastErrorList[i];
                            if (i2 != 0 && !Objects.equals(num, Integer.valueOf(i2))) {
                                tetheringEventCallback.onError(tetheringInterface, i2);
                            }
                            this.mErrorStates.put(tetheringInterface, Integer.valueOf(i2));
                        }
                    }

                    private synchronized void maybeSendTetherableIfacesChangedCallback(TetherStatesParcel tetherStatesParcel) {
                        if (!Arrays.equals((Object[]) this.mLastTetherableInterfaces, (Object[]) tetherStatesParcel.availableList)) {
                            this.mLastTetherableInterfaces = (TetheringInterface[]) tetherStatesParcel.availableList.clone();
                            tetheringEventCallback.onTetherableInterfacesChanged((Set<TetheringInterface>) Collections.unmodifiableSet(new ArraySet(this.mLastTetherableInterfaces)));
                        }
                    }

                    private synchronized void maybeSendTetheredIfacesChangedCallback(TetherStatesParcel tetherStatesParcel) {
                        if (!Arrays.equals((Object[]) this.mLastTetheredInterfaces, (Object[]) tetherStatesParcel.tetheredList)) {
                            this.mLastTetheredInterfaces = (TetheringInterface[]) tetherStatesParcel.tetheredList.clone();
                            tetheringEventCallback.onTetheredInterfacesChanged((Set<TetheringInterface>) Collections.unmodifiableSet(new ArraySet(this.mLastTetheredInterfaces)));
                        }
                    }

                    private synchronized void maybeSendLocalOnlyIfacesChangedCallback(TetherStatesParcel tetherStatesParcel) {
                        if (!Arrays.equals((Object[]) this.mLastLocalOnlyInterfaces, (Object[]) tetherStatesParcel.localOnlyList)) {
                            this.mLastLocalOnlyInterfaces = (TetheringInterface[]) tetherStatesParcel.localOnlyList.clone();
                            tetheringEventCallback.onLocalOnlyInterfacesChanged((Set<TetheringInterface>) Collections.unmodifiableSet(new ArraySet(this.mLastLocalOnlyInterfaces)));
                        }
                    }

                    public void onCallbackStarted(TetheringCallbackStartedParcel tetheringCallbackStartedParcel) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda6(this, tetheringEventCallback, tetheringCallbackStartedParcel));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onCallbackStarted$1$android-net-TetheringManager$4  reason: not valid java name */
                    public /* synthetic */ void m1952lambda$onCallbackStarted$1$androidnetTetheringManager$4(TetheringEventCallback tetheringEventCallback, TetheringCallbackStartedParcel tetheringCallbackStartedParcel) {
                        tetheringEventCallback.onSupportedTetheringTypes(TetheringManager.unpackBits(tetheringCallbackStartedParcel.supportedTypes));
                        tetheringEventCallback.onTetheringSupported(tetheringCallbackStartedParcel.supportedTypes != 0);
                        tetheringEventCallback.onUpstreamChanged(tetheringCallbackStartedParcel.upstreamNetwork);
                        sendErrorCallbacks(tetheringCallbackStartedParcel.states);
                        m1953lambda$onConfigurationChanged$4$androidnetTetheringManager$4(tetheringCallbackStartedParcel.config);
                        maybeSendTetherableIfacesChangedCallback(tetheringCallbackStartedParcel.states);
                        maybeSendTetheredIfacesChangedCallback(tetheringCallbackStartedParcel.states);
                        maybeSendLocalOnlyIfacesChangedCallback(tetheringCallbackStartedParcel.states);
                        tetheringEventCallback.onClientsChanged(tetheringCallbackStartedParcel.tetheredClients);
                        tetheringEventCallback.onOffloadStatusChanged(tetheringCallbackStartedParcel.offloadStatus);
                    }

                    public void onCallbackStopped(int i) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda2(i));
                    }

                    public void onSupportedTetheringTypes(long j) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda1(tetheringEventCallback, j));
                    }

                    /* access modifiers changed from: private */
                    /* renamed from: sendRegexpsChanged */
                    public void m1953lambda$onConfigurationChanged$4$androidnetTetheringManager$4(TetheringConfigurationParcel tetheringConfigurationParcel) {
                        tetheringEventCallback.onTetherableInterfaceRegexpsChanged(new TetheringInterfaceRegexps(tetheringConfigurationParcel.tetherableBluetoothRegexs, tetheringConfigurationParcel.tetherableUsbRegexs, tetheringConfigurationParcel.tetherableWifiRegexs));
                    }

                    public void onConfigurationChanged(TetheringConfigurationParcel tetheringConfigurationParcel) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda7(this, tetheringConfigurationParcel));
                    }

                    public void onTetherStatesChanged(TetherStatesParcel tetherStatesParcel) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda4(this, tetherStatesParcel));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onTetherStatesChanged$5$android-net-TetheringManager$4  reason: not valid java name */
                    public /* synthetic */ void m1954lambda$onTetherStatesChanged$5$androidnetTetheringManager$4(TetherStatesParcel tetherStatesParcel) {
                        sendErrorCallbacks(tetherStatesParcel);
                        maybeSendTetherableIfacesChangedCallback(tetherStatesParcel);
                        maybeSendTetheredIfacesChangedCallback(tetherStatesParcel);
                        maybeSendLocalOnlyIfacesChangedCallback(tetherStatesParcel);
                    }

                    public void onTetherClientsChanged(List<TetheredClient> list) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda5(tetheringEventCallback, list));
                    }

                    public void onOffloadStatusChanged(int i) {
                        executor.execute(new TetheringManager$4$$ExternalSyntheticLambda0(tetheringEventCallback, i));
                    }
                };
                getConnector(new TetheringManager$$ExternalSyntheticLambda11(r2, opPackageName));
                this.mTetheringEventCallbacks.put(tetheringEventCallback, r2);
            } else {
                throw new IllegalArgumentException("callback was already registered.");
            }
        }
    }

    public static ArraySet<Integer> unpackBits(long j) {
        ArraySet<Integer> arraySet = new ArraySet<>(Long.bitCount(j));
        int i = 0;
        while (j != 0) {
            if ((j & 1) == 1) {
                arraySet.add(Integer.valueOf(i));
            }
            j >>>= 1;
            i++;
        }
        return arraySet;
    }

    public void unregisterTetheringEventCallback(TetheringEventCallback tetheringEventCallback) {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "unregisterTetheringEventCallback caller:" + opPackageName);
        synchronized (this.mTetheringEventCallbacks) {
            ITetheringEventCallback remove = this.mTetheringEventCallbacks.remove(tetheringEventCallback);
            if (remove != null) {
                getConnector(new TetheringManager$$ExternalSyntheticLambda2(remove, opPackageName));
            } else {
                throw new IllegalArgumentException("callback was not registered.");
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getLastTetherError(String str) {
        this.mCallback.waitForStarted();
        if (this.mTetherStatesParcel == null) {
            return 0;
        }
        int i = 0;
        for (TetheringInterface tetheringInterface : this.mTetherStatesParcel.erroredIfaceList) {
            if (str.equals(tetheringInterface.getInterface())) {
                return this.mTetherStatesParcel.lastErrorList[i];
            }
            i++;
        }
        return 0;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetherableUsbRegexs() {
        this.mCallback.waitForStarted();
        return this.mTetheringConfiguration.tetherableUsbRegexs;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetherableWifiRegexs() {
        this.mCallback.waitForStarted();
        return this.mTetheringConfiguration.tetherableWifiRegexs;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetherableBluetoothRegexs() {
        this.mCallback.waitForStarted();
        return this.mTetheringConfiguration.tetherableBluetoothRegexs;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetherableIfaces() {
        this.mCallback.waitForStarted();
        if (this.mTetherStatesParcel == null) {
            return new String[0];
        }
        return toIfaces(this.mTetherStatesParcel.availableList);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetheredIfaces() {
        this.mCallback.waitForStarted();
        if (this.mTetherStatesParcel == null) {
            return new String[0];
        }
        return toIfaces(this.mTetherStatesParcel.tetheredList);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String[] getTetheringErroredIfaces() {
        this.mCallback.waitForStarted();
        if (this.mTetherStatesParcel == null) {
            return new String[0];
        }
        return toIfaces(this.mTetherStatesParcel.erroredIfaceList);
    }

    @Deprecated
    public String[] getTetheredDhcpRanges() {
        this.mCallback.waitForStarted();
        return this.mTetheringConfiguration.legacyDhcpRanges;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean isTetheringSupported() {
        return isTetheringSupported(this.mContext.getOpPackageName());
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean isTetheringSupported(String str) {
        return new RequestDispatcher().waitForResult(new TetheringManager$$ExternalSyntheticLambda6(this, str)) == 0;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$isTetheringSupported$11$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1943lambda$isTetheringSupported$11$androidnetTetheringManager(String str, ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        try {
            iTetheringConnector.isTetheringSupported(str, getAttributionTag(), iIntResultListener);
        } catch (RemoteException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    public void stopAllTethering() {
        String opPackageName = this.mContext.getOpPackageName();
        String str = TAG;
        Log.i(str, "stopAllTethering caller:" + opPackageName);
        getConnector(new TetheringManager$$ExternalSyntheticLambda5(this, opPackageName));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$stopAllTethering$12$android-net-TetheringManager  reason: not valid java name */
    public /* synthetic */ void m1948lambda$stopAllTethering$12$androidnetTetheringManager(String str, ITetheringConnector iTetheringConnector) throws RemoteException {
        iTetheringConnector.stopAllTethering(str, getAttributionTag(), new IIntResultListener.Stub() {
            public void onResult(int i) {
            }
        });
    }

    public void setPreferTestNetworks(boolean z) {
        String str = TAG;
        Log.i(str, "setPreferTestNetworks caller: " + this.mContext.getOpPackageName());
        new RequestDispatcher().waitForResult(new TetheringManager$$ExternalSyntheticLambda3(z));
    }

    static /* synthetic */ void lambda$setPreferTestNetworks$13(boolean z, ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        try {
            iTetheringConnector.setPreferTestNetworks(z, iIntResultListener);
        } catch (RemoteException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }
}
