package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothMapClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

public final class MapClientProfile implements LocalBluetoothProfile {
    static final String NAME = "MAP Client";
    private static final int ORDINAL = 0;
    private static final String TAG = "MapClientProfile";
    static final ParcelUuid[] UUIDS = {BluetoothUuid.MAS};
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothMapClient mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302817;
    }

    public int getOrdinal() {
        return 0;
    }

    public int getProfileId() {
        return 18;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class MapClientServiceListener implements BluetoothProfile.ServiceListener {
        private MapClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothMapClient unused = MapClientProfile.this.mService = (BluetoothMapClient) bluetoothProfile;
            List connectedDevices = MapClientProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = MapClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(MapClientProfile.TAG, "MapProfile found new device: " + bluetoothDevice);
                    findDevice = MapClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(MapClientProfile.this, 2);
                findDevice.refresh();
            }
            MapClientProfile.this.mProfileManager.callServiceConnectedListeners();
            boolean unused2 = MapClientProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            MapClientProfile.this.mProfileManager.callServiceDisconnectedListeners();
            boolean unused = MapClientProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        Log.d(TAG, "isProfileReady(): " + this.mIsProfileReady);
        return this.mIsProfileReady;
    }

    MapClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new MapClientServiceListener(), 18);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return 0;
        }
        return bluetoothMapClient.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient != null && bluetoothMapClient.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return 0;
        }
        return bluetoothMapClient.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothMapClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothMapClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return new ArrayList(0);
        }
        return bluetoothMapClient.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
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
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(18, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up MAP Client proxy", th);
            }
        }
    }
}
