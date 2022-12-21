package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

public class HeadsetProfile implements LocalBluetoothProfile {
    static final String NAME = "HEADSET";
    private static final int ORDINAL = 0;
    private static final String TAG = "HeadsetProfile";
    static final ParcelUuid[] UUIDS = {BluetoothUuid.HSP, BluetoothUuid.HFP};
    private final BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothHeadset mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302337;
    }

    public int getOrdinal() {
        return 0;
    }

    public int getProfileId() {
        return 1;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class HeadsetServiceListener implements BluetoothProfile.ServiceListener {
        private HeadsetServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothHeadset unused = HeadsetProfile.this.mService = (BluetoothHeadset) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = HeadsetProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = HeadsetProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    Log.w(HeadsetProfile.TAG, "HeadsetProfile found new device: " + remove);
                    findDevice = HeadsetProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(HeadsetProfile.this, 2);
                findDevice.refresh();
            }
            boolean unused2 = HeadsetProfile.this.mIsProfileReady = true;
            HeadsetProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            HeadsetProfile.this.mProfileManager.callServiceDisconnectedListeners();
            boolean unused = HeadsetProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    HeadsetProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new HeadsetServiceListener(), 1);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset == null) {
            return 0;
        }
        return bluetoothHeadset.getConnectionState(bluetoothDevice);
    }

    public boolean setActiveDevice(BluetoothDevice bluetoothDevice) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return false;
        }
        if (bluetoothDevice == null) {
            return bluetoothAdapter.removeActiveDevice(1);
        }
        return bluetoothAdapter.setActiveDevice(bluetoothDevice, 1);
    }

    public BluetoothDevice getActiveDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return null;
        }
        List activeDevices = bluetoothAdapter.getActiveDevices(1);
        if (activeDevices.size() > 0) {
            return (BluetoothDevice) activeDevices.get(0);
        }
        return null;
    }

    public int getAudioState(BluetoothDevice bluetoothDevice) {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset == null) {
            return 10;
        }
        return bluetoothHeadset.getAudioState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null && bluetoothHeadset.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset == null) {
            return 0;
        }
        return bluetoothHeadset.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset == null) {
            return false;
        }
        if (!z) {
            return bluetoothHeadset.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothHeadset.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset == null) {
            return new ArrayList(0);
        }
        return bluetoothHeadset.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_headset;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_headset_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_headset_profile_summary_connected;
    }

    public boolean supportLowLatency(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, " execute supportLowLatency()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.isLowLatencySupported(bluetoothDevice);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    public boolean supportEq(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, " execute supportEq()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.isEqSupported(bluetoothDevice);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    public boolean getLowLatencyMode(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, " execute getLowLatencyMode()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.getLowLatencyMode(bluetoothDevice);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    public boolean getEqMode(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, " execute getEqMode()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.getEqMode(bluetoothDevice);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    public boolean setLowLatencyMode(BluetoothDevice bluetoothDevice, boolean z) {
        Log.d(TAG, " execute setLowLatencyMode()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.setLowLatencyMode(bluetoothDevice, z);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    public boolean setEqMode(BluetoothDevice bluetoothDevice, boolean z) {
        Log.d(TAG, " execute setEqMode()");
        BluetoothHeadset bluetoothHeadset = this.mService;
        if (bluetoothHeadset != null) {
            return bluetoothHeadset.setEqMode(bluetoothDevice, z);
        }
        Log.d(TAG, "mService is null.");
        return false;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(1, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up HID proxy", th);
            }
        }
    }
}
