package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothVcp;
import android.content.AttributionSource;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
/* loaded from: classes.dex */
public final class BluetoothVcp implements BluetoothProfile {
    public static final int A2DP = 1;
    public static final String ACTION_CONNECTION_MODE_CHANGED = "android.bluetooth.vcp.profile.action.CONNECTION_MODE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.vcp.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_MUTE_CHANGED = "android.bluetooth.vcp.profile.action.MUTE_CHANGED";
    public static final String ACTION_VOLUME_CHANGED = "android.bluetooth.vcp.profile.action.VOLUME_CHANGED";
    public static final int CALL_STREAM = 0;
    private static final boolean DBG = true;
    public static final String EXTRA_MODE = "android.bluetooth.vcp.profile.extra.MODE";
    public static final String EXTRA_MUTE = "android.bluetooth.vcp.profile.extra.MUTE";
    public static final String EXTRA_VOLUME = "android.bluetooth.vcp.profile.extra.VOLUME";
    public static final int HFP = 2;
    public static final int LE_MEDIA = 16;
    public static final int LE_VOICE = 8192;
    public static final int MEDIA_STREAM = 1;
    public static final int MODE_BROADCAST = 2;
    public static final int MODE_NONE = 0;
    public static final int MODE_UNICAST = 1;
    public static final int MODE_UNICAST_BROADCAST = 3;
    private static final String TAG = "BluetoothVcp";
    private static final boolean VDBG = true;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final AttributionSource mAttributionSource = this.mAdapter.getAttributionSource();
    private final BluetoothProfileConnector<IBluetoothVcp> mProfileConnector;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothVcp(Context context, BluetoothProfile.ServiceListener listener) {
        BluetoothProfileConnector<IBluetoothVcp> bluetoothProfileConnector = new BluetoothProfileConnector(this, 26, TAG, IBluetoothVcp.class.getName()) { // from class: android.bluetooth.BluetoothVcp.1
            @Override // android.bluetooth.BluetoothProfileConnector
            /* renamed from: getServiceInterface */
            public IBluetoothVcp mo604getServiceInterface(IBinder service) {
                return IBluetoothVcp.Stub.asInterface(Binder.allowBlocking(service));
            }
        };
        this.mProfileConnector = bluetoothProfileConnector;
        bluetoothProfileConnector.connect(context, listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothVcp getService() {
        return this.mProfileConnector.getService();
    }

    public void finalize() {
        close();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        return null;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        log("getDevicesMatchingStates()");
        return null;
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        log("getConnectionState(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionState(device, this.mAttributionSource);
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

    public int getConnectionMode(BluetoothDevice device) {
        log("getConnectionMode(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionMode(device, this.mAttributionSource);
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

    public void setAbsoluteVolume(BluetoothDevice device, int volume) {
        log("setAbsoluteVolume(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                service.setAbsoluteVolume(device, volume, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    public int getAbsoluteVolume(BluetoothDevice device) {
        log("getAbsoluteVolume(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getAbsoluteVolume(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return -1;
    }

    public void setMute(BluetoothDevice device, boolean enableMute) {
        log("setMute(" + device + ") enableMute: " + enableMute);
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                service.setMute(device, enableMute, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    public boolean isMute(BluetoothDevice device) {
        log("isMute(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.isMute(device, this.mAttributionSource);
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

    public boolean setActiveProfile(BluetoothDevice device, int audioType, int profile) {
        log("setActiveProfile(" + device + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.setActiveProfile(device, audioType, profile, this.mAttributionSource);
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

    public int getActiveProfile(int audioType) {
        log("getActiveProfile(" + audioType + ")");
        IBluetoothVcp service = getService();
        if (service != null && isEnabled()) {
            try {
                return service.getActiveProfile(audioType, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return -1;
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
