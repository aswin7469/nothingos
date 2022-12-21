package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadsetClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

final class HfpClientProfile implements LocalBluetoothProfile {
    static final String NAME = "HEADSET_CLIENT";
    private static final int ORDINAL = 0;
    static final ParcelUuid[] SRC_UUIDS = {BluetoothUuid.HSP_AG, BluetoothUuid.HFP_AG};
    private static final String TAG = "HfpClientProfile";
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothHeadsetClient mService;

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
        return 16;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class HfpClientServiceListener implements BluetoothProfile.ServiceListener {
        private HfpClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothHeadsetClient unused = HfpClientProfile.this.mService = (BluetoothHeadsetClient) bluetoothProfile;
            List connectedDevices = HfpClientProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = HfpClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(HfpClientProfile.TAG, "HfpClient profile found new device: " + bluetoothDevice);
                    findDevice = HfpClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(HfpClientProfile.this, 2);
                findDevice.refresh();
            }
            boolean unused2 = HfpClientProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            boolean unused = HfpClientProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    HfpClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new HfpClientServiceListener(), 16);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return new ArrayList(0);
        }
        return bluetoothHeadsetClient.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return 0;
        }
        return bluetoothHeadsetClient.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient != null && bluetoothHeadsetClient.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return 0;
        }
        return bluetoothHeadsetClient.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothHeadsetClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothHeadsetClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
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

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(16, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up HfpClient proxy", th);
            }
        }
    }
}
