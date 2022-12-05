package android.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothBroadcast;
import android.content.AttributionSource;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
/* loaded from: classes.dex */
public final class BluetoothBroadcast implements BluetoothProfile {
    public static final String ACTION_BROADCAST_AUDIO_STATE_CHANGED = "android.bluetooth.broadcast.profile.action.BROADCAST_AUDIO_STATE_CHANGED";
    public static final String ACTION_BROADCAST_ENCRYPTION_KEY_GENERATED = "android.bluetooth.broadcast.profile.action.BROADCAST_ENCRYPTION_KEY_GENERATED";
    public static final String ACTION_BROADCAST_STATE_CHANGED = "android.bluetooth.broadcast.profile.action.BROADCAST_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final int STATE_DISABLED = 10;
    public static final int STATE_DISABLING = 13;
    public static final int STATE_ENABLED = 12;
    public static final int STATE_ENABLING = 11;
    public static final int STATE_NOT_PLAYING = 11;
    public static final int STATE_PLAYING = 10;
    public static final int STATE_STREAMING = 14;
    private static final String TAG = "BluetoothBroadcast";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final AttributionSource mAttributionSource = this.mAdapter.getAttributionSource();
    private final BluetoothProfileConnector<IBluetoothBroadcast> mProfileConnector;

    BluetoothBroadcast(Context context, BluetoothProfile.ServiceListener listener) {
        BluetoothProfileConnector<IBluetoothBroadcast> bluetoothProfileConnector = new BluetoothProfileConnector(this, 25, TAG, IBluetoothBroadcast.class.getName()) { // from class: android.bluetooth.BluetoothBroadcast.1
            @Override // android.bluetooth.BluetoothProfileConnector
            /* renamed from: getServiceInterface */
            public IBluetoothBroadcast mo604getServiceInterface(IBinder service) {
                return IBluetoothBroadcast.Stub.asInterface(Binder.allowBlocking(service));
            }
        };
        this.mProfileConnector = bluetoothProfileConnector;
        bluetoothProfileConnector.connect(context, listener);
    }

    void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothBroadcast getService() {
        return this.mProfileConnector.getService();
    }

    public void finalize() {
    }

    public boolean SetBroadcastMode(boolean enable) {
        log("EnableBroadcast");
        String packageName = ActivityThread.currentPackageName();
        try {
            IBluetoothBroadcast service = getService();
            if (service != null && isEnabled()) {
                return service.SetBroadcast(enable, packageName, this.mAttributionSource);
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    public boolean SetEncryption(boolean enable, int enc_len, boolean use_existing) {
        log("SetEncryption");
        String packageName = ActivityThread.currentPackageName();
        try {
            IBluetoothBroadcast service = getService();
            if (service != null && isEnabled()) {
                return service.SetEncryption(enable, enc_len, use_existing, packageName, this.mAttributionSource);
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public byte[] GetEncryptionKey() {
        log("GetBroadcastEncryptionKey");
        String packageName = ActivityThread.currentPackageName();
        try {
            IBluetoothBroadcast service = getService();
            if (service != null && isEnabled()) {
                return service.GetEncryptionKey(packageName, this.mAttributionSource);
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return null;
        }
    }

    public int GetBroadcastStatus() {
        log("GetBroadcastStatus");
        String packageName = ActivityThread.currentPackageName();
        try {
            IBluetoothBroadcast service = getService();
            if (service != null && isEnabled()) {
                return service.GetBroadcastStatus(packageName, this.mAttributionSource);
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return 10;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 10;
        }
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
