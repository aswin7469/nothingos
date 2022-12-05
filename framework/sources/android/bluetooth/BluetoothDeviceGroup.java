package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothDeviceGroup;
import android.bluetooth.IBluetoothGroupCallback;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.AttributionSource;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BluetoothDeviceGroup implements BluetoothProfile {
    public static final int ACCESS_DENIED = 5;
    public static final int ACCESS_GRANTED = 2;
    public static final int ACCESS_RELEASED = 1;
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.group.profile.action.CONNECTION_STATE_CHANGED";
    public static final int ALL_DEVICES_GRANTED_ACCESS = 2;
    public static final int APP_ID_MAX = 15;
    public static final int APP_ID_MIN = 0;
    public static final int APP_REGISTRATION_FAILED = 1;
    public static final int APP_REGISTRATION_SUCCESSFUL = 0;
    private static final boolean DBG = true;
    public static final int DISCOVERY_COMPLETED = 3;
    public static final int DISCOVERY_NOT_STARTED_INVALID_PARAMS = 5;
    public static final int DISCOVERY_STARTED_BY_APPL = 0;
    public static final int DISCOVERY_STARTED_GROUP_PROP_CHANGED = 2;
    public static final int DISCOVERY_STOPPED_BY_APPL = 1;
    public static final int DISCOVERY_STOPPED_BY_TIMEOUT = 4;
    public static final int EXCLUSIVE_ACCESS_RELEASED = 0;
    public static final int EXCLUSIVE_ACCESS_RELEASED_BY_TIMEOUT = 1;
    public static final int GROUP_DISCOVERY_STARTED = 0;
    public static final int GROUP_DISCOVERY_STOPPED = 1;
    public static final int GROUP_ID_MAX = 15;
    public static final int GROUP_ID_MIN = 0;
    public static final int INVALID_ACCESS_REQ_PARAMS = 6;
    public static final int INVALID_APP_ID = 16;
    public static final int INVALID_GROUP_ID = 16;
    public static final int SOME_GRANTED_ACCESS_REASON_DISCONNECTION = 4;
    public static final int SOME_GRANTED_ACCESS_REASON_TIMEOUT = 3;
    private static final String TAG = "BluetoothDeviceGroup";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private int mAppId;
    private final AttributionSource mAttributionSource;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback;
    private BluetoothGroupCallback mCallback;
    private Handler mHandler;
    private final BluetoothProfileConnector<IBluetoothDeviceGroup> mProfileConnector;
    private boolean mAppRegistered = false;
    private final IBluetoothGroupCallback.Stub mBluetoothGroupCallback = new IBluetoothGroupCallback.Stub() { // from class: android.bluetooth.BluetoothDeviceGroup.3
        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupClientAppRegistered(final int status, final int appId) {
            Log.d(BluetoothDeviceGroup.TAG, "onGroupClientAppRegistered() - status=" + status + " appId = " + appId);
            if (status != 0) {
                BluetoothDeviceGroup.this.mAppRegistered = false;
            }
            BluetoothDeviceGroup.this.mAppId = appId;
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.1
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onGroupClientAppRegistered(status, appId);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupClientAppUnregistered(int status) {
            Log.d(BluetoothDeviceGroup.TAG, "onGroupClientAppUnregistered() - status=" + status + " mAppId=" + BluetoothDeviceGroup.this.mAppId);
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onConnectionStateChanged(final int state, final BluetoothDevice device) {
            Log.d(BluetoothDeviceGroup.TAG, "onConnectionStateChanged() - state = " + state + " device = " + device);
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.2
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onConnectionStateChanged(state, device);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onNewGroupFound(final int groupId, final BluetoothDevice device, final ParcelUuid uuid) {
            Log.d(BluetoothDeviceGroup.TAG, "onNewGroupFound() - appId = " + BluetoothDeviceGroup.this.mAppId + ", groupId = " + groupId + ", device: " + device + ", Including service UUID: " + uuid.toString());
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.3
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onNewGroupFound(groupId, device, uuid.getUuid());
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupDiscoveryStatusChanged(final int groupId, final int status, final int reason) {
            Log.d(BluetoothDeviceGroup.TAG, "onGroupDiscoveryStatusChanged() - appId = " + BluetoothDeviceGroup.this.mAppId + ", groupId = " + groupId + ", status: " + status + ", reason: " + reason);
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.4
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onGroupDiscoveryStatusChanged(groupId, status, reason);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupDeviceFound(final int groupId, final BluetoothDevice device) {
            Log.d(BluetoothDeviceGroup.TAG, "onGroupDeviceFound() - appId = " + BluetoothDeviceGroup.this.mAppId + ", device = " + device);
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.5
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onGroupDeviceFound(groupId, device);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessChanged(final int groupId, final int value, final int status, final List<BluetoothDevice> devices) {
            Log.d(BluetoothDeviceGroup.TAG, "onExclusiveAccessChanged() - appId = " + BluetoothDeviceGroup.this.mAppId + ", groupId = " + groupId + ", value = " + value + " accessStatus = " + status + ", devices: " + devices);
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.6
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onExclusiveAccessChanged(groupId, value, status, devices);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessStatusFetched(int groupId, int accessValue) {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessAvailable(final int groupId, final BluetoothDevice device) {
            Log.d(BluetoothDeviceGroup.TAG, "onExclusiveAccessAvailable() - appId = " + BluetoothDeviceGroup.this.mAppId + ", groupId = " + groupId + ", device: " + device);
            BluetoothDeviceGroup.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothDeviceGroup.3.7
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGroupCallback callback = BluetoothDeviceGroup.this.mCallback;
                    if (callback != null) {
                        callback.onExclusiveAccessAvailable(groupId, device);
                    }
                }
            });
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothDeviceGroup(Context context, BluetoothProfile.ServiceListener listener) {
        BluetoothProfileConnector<IBluetoothDeviceGroup> bluetoothProfileConnector = new BluetoothProfileConnector(this, 24, TAG, IBluetoothDeviceGroup.class.getName()) { // from class: android.bluetooth.BluetoothDeviceGroup.1
            @Override // android.bluetooth.BluetoothProfileConnector
            /* renamed from: getServiceInterface */
            public IBluetoothDeviceGroup mo604getServiceInterface(IBinder service) {
                return IBluetoothDeviceGroup.Stub.asInterface(Binder.allowBlocking(service));
            }
        };
        this.mProfileConnector = bluetoothProfileConnector;
        IBluetoothStateChangeCallback.Stub stub = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothDeviceGroup.2
            @Override // android.bluetooth.IBluetoothStateChangeCallback
            public void onBluetoothStateChange(boolean up) {
                if (!up) {
                    BluetoothDeviceGroup.this.mAppRegistered = false;
                }
            }
        };
        this.mBluetoothStateChangeCallback = stub;
        bluetoothProfileConnector.connect(context, listener);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mAdapter = defaultAdapter;
        this.mAttributionSource = defaultAdapter.getAttributionSource();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(stub);
            } catch (RemoteException re) {
                Log.e(TAG, "", re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mAppRegistered = false;
        IBluetoothDeviceGroup service = getService();
        if (service != null) {
            try {
                service.unregisterGroupClientApp(this.mAppId, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        }
        this.mProfileConnector.disconnect();
    }

    private IBluetoothDeviceGroup getService() {
        return this.mProfileConnector.getService();
    }

    public void finalize() {
        close();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        return null;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        return null;
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        return 0;
    }

    public boolean registerGroupClientApp(BluetoothGroupCallback callbacks, Handler handler) {
        log("registerGroupClientApp() mAppRegistered = " + this.mAppRegistered);
        if (this.mAppRegistered) {
            Log.e(TAG, "App already registered.");
            return false;
        }
        this.mHandler = handler;
        this.mCallback = callbacks;
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy not attached to Profile Service. Can't register App.");
            return false;
        }
        this.mAppRegistered = true;
        try {
            UUID uuid = UUID.randomUUID();
            service.registerGroupClientApp(new ParcelUuid(uuid), this.mBluetoothGroupCallback, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
        }
        return true;
    }

    public boolean startGroupDiscovery(int groupId) {
        log("startGroupDiscovery() : groupId = " + groupId);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't start group discovery");
            return false;
        }
        try {
            UUID.randomUUID();
            service.startGroupDiscovery(this.mAppId, groupId, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    public boolean stopGroupDiscovery(int groupId) {
        log("stopGroupDiscovery() : groupId = " + groupId);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't Stop group discovery");
            return false;
        }
        try {
            service.stopGroupDiscovery(this.mAppId, groupId, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    public List<DeviceGroup> getDiscoveredGroups() {
        return getDiscoveredGroups(false);
    }

    public List<DeviceGroup> getDiscoveredGroups(boolean mPublicAddr) {
        log("getDiscoveredGroups()");
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return null;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't fetch Groups.");
            return null;
        }
        try {
            List<DeviceGroup> groups = service.getDiscoveredGroups(mPublicAddr, this.mAttributionSource);
            return groups;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return null;
        }
    }

    public DeviceGroup getGroup(int groupId) {
        return getGroup(groupId, false);
    }

    public DeviceGroup getGroup(int groupId, boolean mPublicAddr) {
        log("getGroup() : groupId = " + groupId);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return null;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't fetch Group.");
            return null;
        }
        try {
            DeviceGroup group = service.getDeviceGroup(groupId, mPublicAddr, this.mAttributionSource);
            return group;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return null;
        }
    }

    public int getRemoteDeviceGroupId(BluetoothDevice device, ParcelUuid uuid) {
        return getRemoteDeviceGroupId(device, uuid, false);
    }

    public int getRemoteDeviceGroupId(BluetoothDevice device, ParcelUuid uuid, boolean mPublicAddr) {
        log("getRemoteDeviceGroupId() : device = " + device);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return 16;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service.Can't get group id for device.");
            return 16;
        }
        try {
            return service.getRemoteDeviceGroupId(device, uuid, mPublicAddr, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 16;
        }
    }

    public boolean isGroupDiscoveryInProgress(int groupId) {
        log("isGroupDiscoveryInProgress() : groupId = " + groupId);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service.Can't get discovery status.");
            return false;
        }
        try {
            return service.isGroupDiscoveryInProgress(groupId, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean setExclusiveAccess(int groupId, List<BluetoothDevice> devices, int value) {
        log("setExclusiveAccess() : groupId = " + groupId + ", access value: " + value);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't proceed.");
            return false;
        }
        try {
            service.setExclusiveAccess(this.mAppId, groupId, devices, value, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    public boolean getExclusiveAccessStatus(int groupId, List<BluetoothDevice> devices) {
        log("getExclusiveAccessStatus() : groupId = " + groupId);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't get exclusive access status.");
            return false;
        }
        try {
            service.getExclusiveAccessStatus(this.mAppId, groupId, devices, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    public boolean connect(BluetoothDevice device) {
        log("connect : device = " + device);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't connect.");
            return false;
        }
        try {
            service.connect(this.mAppId, device, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    public boolean disconnect(BluetoothDevice device) {
        log("disconnect : device = " + device);
        if (!this.mAppRegistered) {
            Log.e(TAG, "App not registered for Group operations. Register App using registerGroupClientApp");
            return false;
        }
        IBluetoothDeviceGroup service = getService();
        if (service == null) {
            Log.e(TAG, "Proxy is not attached to Profile Service. Can't disconnect");
            return false;
        }
        try {
            service.disconnect(this.mAppId, device, this.mAttributionSource);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOrQueueCallback(Runnable cb) {
        Handler handler = this.mHandler;
        if (handler == null) {
            try {
                cb.run();
                return;
            } catch (Exception ex) {
                Log.w(TAG, "Unhandled exception in callback", ex);
                return;
            }
        }
        handler.post(cb);
    }
}
