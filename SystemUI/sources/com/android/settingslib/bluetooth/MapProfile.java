package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothMap;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

public class MapProfile implements LocalBluetoothProfile {
    static final String NAME = "MAP";
    private static final String TAG = "MapProfile";
    static final ParcelUuid[] UUIDS = {BluetoothUuid.MAP, BluetoothUuid.MNS, BluetoothUuid.MAS};
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothMap mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302817;
    }

    public int getOrdinal() {
        return 9;
    }

    public int getProfileId() {
        return 9;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class MapServiceListener implements BluetoothProfile.ServiceListener {
        private MapServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothMap unused = MapProfile.this.mService = (BluetoothMap) bluetoothProfile;
            List connectedDevices = MapProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = MapProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(MapProfile.TAG, "MapProfile found new device: " + bluetoothDevice);
                    findDevice = MapProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(MapProfile.this, 2);
                findDevice.refresh();
            }
            MapProfile.this.mProfileManager.callServiceConnectedListeners();
            boolean unused2 = MapProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            MapProfile.this.mProfileManager.callServiceDisconnectedListeners();
            boolean unused = MapProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        Log.d(TAG, "isProfileReady(): " + this.mIsProfileReady);
        return this.mIsProfileReady;
    }

    MapProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new MapServiceListener(), 9);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothMap bluetoothMap = this.mService;
        if (bluetoothMap == null) {
            return 0;
        }
        return bluetoothMap.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothMap bluetoothMap = this.mService;
        if (bluetoothMap != null && bluetoothMap.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothMap bluetoothMap = this.mService;
        if (bluetoothMap == null) {
            return 0;
        }
        return bluetoothMap.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothMap bluetoothMap = this.mService;
        if (bluetoothMap == null) {
            return false;
        }
        if (!z) {
            return bluetoothMap.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothMap.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothMap bluetoothMap = this.mService;
        if (bluetoothMap == null) {
            return new ArrayList(0);
        }
        return bluetoothMap.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_map;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_map_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_map_profile_summary_connected;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(9, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up MAP proxy", th);
            }
        }
    }
}
