package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBleBroadcastAudioScanAssistCallback;
import android.bluetooth.IBluetoothSyncHelper;
import android.bluetooth.le.ScanResult;
import android.content.AttributionSource;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class BluetoothSyncHelper implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.bc.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothSyncHelper";
    private Context mContext;
    private final BluetoothProfileConnector<IBluetoothSyncHelper> mProfileConnector;
    private Map<BluetoothDevice, BleBroadcastAudioScanAssistManager> sBleAssistManagers;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Map<BleBroadcastAudioScanAssistCallback, IBleBroadcastAudioScanAssistCallback> mAppCallbackWrappers = new IdentityHashMap();
    private final AttributionSource mAttributionSource = this.mBluetoothAdapter.getAttributionSource();

    void close() {
        this.mProfileConnector.disconnect();
        this.mAppCallbackWrappers.clear();
    }

    IBluetoothSyncHelper getService() {
        return this.mProfileConnector.getService();
    }

    static boolean isSupported() {
        log("BluetoothSyncHelper: isSupported returns true");
        return true;
    }

    BluetoothSyncHelper(Context context, BluetoothProfile.ServiceListener listener) {
        this.sBleAssistManagers = null;
        this.mContext = null;
        BluetoothProfileConnector<IBluetoothSyncHelper> bluetoothProfileConnector = new BluetoothProfileConnector(this, 27, TAG, IBluetoothSyncHelper.class.getName()) { // from class: android.bluetooth.BluetoothSyncHelper.1
            @Override // android.bluetooth.BluetoothProfileConnector
            /* renamed from: getServiceInterface */
            public IBluetoothSyncHelper mo604getServiceInterface(IBinder service) {
                return IBluetoothSyncHelper.Stub.asInterface(Binder.allowBlocking(service));
            }
        };
        this.mProfileConnector = bluetoothProfileConnector;
        bluetoothProfileConnector.connect(context, listener);
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(BluetoothManager.class);
        this.sBleAssistManagers = new IdentityHashMap();
        this.mContext = context;
    }

    public BleBroadcastAudioScanAssistManager getBleBroadcastAudioScanAssistManager(BluetoothDevice device, BleBroadcastAudioScanAssistCallback callback) {
        if (!isSupported()) {
            Log.e(TAG, "Broadcast scan assistance not supported");
            return null;
        }
        BleBroadcastAudioScanAssistManager assistMgr = null;
        Map<BluetoothDevice, BleBroadcastAudioScanAssistManager> map = this.sBleAssistManagers;
        if (map != null) {
            BleBroadcastAudioScanAssistManager assistMgr2 = map.get(device);
            assistMgr = assistMgr2;
        }
        if (assistMgr == null) {
            assistMgr = new BleBroadcastAudioScanAssistManager(this, device, callback);
        } else {
            log("calling registerAppCb only");
        }
        registerAppCallback(device, callback);
        return assistMgr;
    }

    public boolean connect(BluetoothDevice device) {
        log("connect(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.connect(device, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public boolean disconnect(BluetoothDevice device) {
        log("disconnect(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.disconnect(device, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getConnectedDevices(this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        log("getDevicesMatchingStates()");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getDevicesMatchingConnectionStates(states, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        log("getState(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getConnectionState(device, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    public int getConnectionPolicy(BluetoothDevice device) {
        log("getConnectionPolicy(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getConnectionPolicy(device, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean setConnectionPolicy(BluetoothDevice device, int connectionPolicy) {
        log("setConnectionPolicy(" + device + ", " + connectionPolicy + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    if (connectionPolicy != 0 && connectionPolicy != 100) {
                        return false;
                    }
                    return service.setConnectionPolicy(device, connectionPolicy, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    private IBleBroadcastAudioScanAssistCallback wrap(final BleBroadcastAudioScanAssistCallback callback, final Handler handler) {
        return new IBleBroadcastAudioScanAssistCallback.Stub() { // from class: android.bluetooth.BluetoothSyncHelper.2
            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastSourceFound(final ScanResult scanres) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastSourceFound for scanres:" + scanres);
                        callback.onBleBroadcastSourceFound(scanres);
                    }
                });
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceSelected(final BluetoothDevice device, final int status, final List<BleBroadcastSourceChannel> broadcastSourceChannels) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastSourceSelected for status:" + status);
                        callback.onBleBroadcastSourceSelected(device, status, broadcastSourceChannels);
                    }
                });
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceAdded(final BluetoothDevice rcvr, final byte srcId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastAudioSourceAdded for " + rcvr + "srcId:" + ((int) srcId) + "status:" + status);
                        callback.onBleBroadcastAudioSourceAdded(rcvr, srcId, status);
                    }
                });
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceUpdated(final BluetoothDevice rcvr, final byte srcId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.4
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastAudioSourceUpdated for " + rcvr + "srcId:" + ((int) srcId) + "status:" + status);
                        callback.onBleBroadcastAudioSourceUpdated(rcvr, srcId, status);
                    }
                });
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastPinUpdated(final BluetoothDevice rcvr, final byte srcId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.5
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastPinUpdated for " + rcvr + "srcId:" + ((int) srcId) + "status:" + status);
                        callback.onBleBroadcastPinUpdated(rcvr, srcId, status);
                    }
                });
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceRemoved(final BluetoothDevice rcvr, final byte srcId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.BluetoothSyncHelper.2.6
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothSyncHelper.log("calling onBleBroadcastAudioSourceRemoved for " + rcvr + "srcId:" + ((int) srcId) + "status:" + status);
                        callback.onBleBroadcastAudioSourceRemoved(rcvr, srcId, status);
                    }
                });
            }
        };
    }

    boolean startScanOffload(BluetoothDevice device, boolean isGroupOp) {
        log("startScanOffload(" + device + ", isGroupOp: " + isGroupOp + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.startScanOffload(device, isGroupOp, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    boolean stopScanOffload(BluetoothDevice device, boolean isGroupOp) {
        log("stopScanOffload(" + device + ", isGroupOp: " + isGroupOp + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.stopScanOffload(device, isGroupOp, this.mAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean searchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource aAttributionSource) {
        log("searchforLeAudioBroadcasters(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.searchforLeAudioBroadcasters(device, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopSearchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource aAttributionSource) {
        log("stopSearchforLeAudioBroadcasters(" + device + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.stopSearchforLeAudioBroadcasters(device, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean selectBroadcastSource(BluetoothDevice device, ScanResult scanRes, boolean isGroupOp, AttributionSource aAttributionSource) {
        log("selectBroadcastSource(" + device + ": groupop" + isGroupOp + ")");
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.selectBroadcastSource(device, scanRes, isGroupOp, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerAppCallback(BluetoothDevice device, BleBroadcastAudioScanAssistCallback appCallback) {
        log("registerAppCallback device :" + device + "appCB: " + appCallback);
        Handler handler = new Handler(Looper.getMainLooper());
        IBleBroadcastAudioScanAssistCallback wrapped = wrap(appCallback, handler);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    service.registerAppCallback(device, wrapped, this.mAttributionSource);
                    Map<BleBroadcastAudioScanAssistCallback, IBleBroadcastAudioScanAssistCallback> map = this.mAppCallbackWrappers;
                    if (map != null) {
                        map.put(appCallback, wrapped);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterAppCallback(BluetoothDevice device, BleBroadcastAudioScanAssistCallback appCallback) {
        log("unregisterAppCallback: device" + device + "appCB:" + appCallback);
        IBluetoothSyncHelper service = getService();
        IBleBroadcastAudioScanAssistCallback cb = this.mAppCallbackWrappers.get(device);
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    service.unregisterAppCallback(device, cb, this.mAttributionSource);
                    this.mAppCallbackWrappers.remove(appCallback);
                    return;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addBroadcastSource(BluetoothDevice sinkDevice, BleBroadcastSourceInfo srcInfo, boolean isGroupOp, AttributionSource aAttributionSource) {
        log("addBroadcastSource  for :" + sinkDevice + "SourceInfo: " + srcInfo + "isGroupOp: " + isGroupOp);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(sinkDevice)) {
                    return service.addBroadcastSource(sinkDevice, srcInfo, isGroupOp, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service != null) {
            return false;
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateBroadcastSource(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean isGroupOp, AttributionSource aAttributionSource) {
        log("updateBroadcastSource for :" + device + "SourceInfo: " + srcInfo + "isGroupOp: " + isGroupOp);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.updateBroadcastSource(device, srcInfo, isGroupOp, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service != null) {
            return false;
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setBroadcastCode(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean isGroupOp, AttributionSource aAttributionSource) {
        log("setBroadcastCode for :" + device);
        log("SourceInfo: " + srcInfo + "isGroupOp: " + isGroupOp);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.setBroadcastCode(device, srcInfo, isGroupOp, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service != null) {
            return false;
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeBroadcastSource(BluetoothDevice device, byte sourceId, boolean isGroupOp, AttributionSource aAttributionSource) {
        log("removeBroadcastSource for :" + device + "SourceId: " + ((int) sourceId) + "isGroupOp: " + isGroupOp);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.removeBroadcastSource(device, sourceId, isGroupOp, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service != null) {
            return false;
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<BleBroadcastSourceInfo> getAllBroadcastSourceInformation(BluetoothDevice device, AttributionSource aAttributionSource) {
        log("GetAllBroadcastReceiverStates for :" + device);
        IBluetoothSyncHelper service = getService();
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getAllBroadcastSourceInformation(device, aAttributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return null;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return null;
    }

    private boolean isEnabled() {
        return this.mBluetoothAdapter.getState() == 12;
    }

    private boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        BleBroadcastSourceInfo.BASS_Debug(TAG, msg);
    }
}
