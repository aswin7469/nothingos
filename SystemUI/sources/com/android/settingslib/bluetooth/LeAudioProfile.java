package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

public class LeAudioProfile implements LocalBluetoothProfile {
    /* access modifiers changed from: private */
    public static boolean DEBUG = true;
    static final String NAME = "LE_AUDIO";
    private static final int ORDINAL = 1;
    private static final String TAG = "LeAudioProfile";
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothLeAudio mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 22;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class LeAudioServiceListener implements BluetoothProfile.ServiceListener {
        private LeAudioServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (LeAudioProfile.DEBUG) {
                Log.d(LeAudioProfile.TAG, "Bluetooth service connected");
            }
            BluetoothLeAudio unused = LeAudioProfile.this.mService = (BluetoothLeAudio) bluetoothProfile;
            List connectedDevices = LeAudioProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = LeAudioProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    if (LeAudioProfile.DEBUG) {
                        Log.d(LeAudioProfile.TAG, "LeAudioProfile found new device: " + bluetoothDevice);
                    }
                    findDevice = LeAudioProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(LeAudioProfile.this, 2);
                findDevice.refresh();
            }
            LeAudioProfile.this.mProfileManager.callServiceConnectedListeners();
            boolean unused2 = LeAudioProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            if (LeAudioProfile.DEBUG) {
                Log.d(LeAudioProfile.TAG, "Bluetooth service disconnected");
            }
            LeAudioProfile.this.mProfileManager.callServiceDisconnectedListeners();
            boolean unused = LeAudioProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    LeAudioProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new LeAudioServiceListener(), 22);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return new ArrayList(0);
        }
        return bluetoothLeAudio.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return false;
        }
        return bluetoothLeAudio.setConnectionPolicy(bluetoothDevice, 100);
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return false;
        }
        return bluetoothLeAudio.setConnectionPolicy(bluetoothDevice, 0);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return 0;
        }
        return bluetoothLeAudio.getConnectionState(bluetoothDevice);
    }

    public boolean setActiveDevice(BluetoothDevice bluetoothDevice) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return false;
        }
        if (bluetoothDevice == null) {
            return bluetoothAdapter.removeActiveDevice(2);
        }
        return bluetoothAdapter.setActiveDevice(bluetoothDevice, 2);
    }

    public List<BluetoothDevice> getActiveDevices() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return new ArrayList();
        }
        return bluetoothAdapter.getActiveDevices(22);
    }

    public BluetoothDevice getConnectedGroupLeadDevice(int i) {
        if (DEBUG) {
            Log.d(TAG, "getConnectedGroupLeadDevice");
        }
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio != null) {
            return bluetoothLeAudio.getConnectedGroupLeadDevice(i);
        }
        Log.e(TAG, "No service.");
        return null;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null || bluetoothLeAudio.getConnectionPolicy(bluetoothDevice) <= 0) {
            return false;
        }
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothLeAudio.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothLeAudio.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothLeAudio.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_le_audio;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_le_audio_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_le_audio_profile_summary_connected;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return C1757R.C1759drawable.ic_bt_le_audio;
    }

    public int getAudioLocation(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothLeAudio.getAudioLocation(bluetoothDevice);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (DEBUG) {
            Log.d(TAG, "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(22, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up LeAudio proxy", th);
            }
        }
    }
}
