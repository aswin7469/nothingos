package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSap;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

final class SapProfile implements LocalBluetoothProfile {
    static final String NAME = "SAP";
    private static final int ORDINAL = 10;
    private static final String TAG = "SapProfile";
    static final ParcelUuid[] UUIDS = {BluetoothUuid.SAP};
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothSap mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302817;
    }

    public int getOrdinal() {
        return 10;
    }

    public int getProfileId() {
        return 10;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class SapServiceListener implements BluetoothProfile.ServiceListener {
        private SapServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothSap unused = SapProfile.this.mService = (BluetoothSap) bluetoothProfile;
            List connectedDevices = SapProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = SapProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(SapProfile.TAG, "SapProfile found new device: " + bluetoothDevice);
                    findDevice = SapProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(SapProfile.this, 2);
                findDevice.refresh();
            }
            SapProfile.this.mProfileManager.callServiceConnectedListeners();
            boolean unused2 = SapProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            SapProfile.this.mProfileManager.callServiceDisconnectedListeners();
            boolean unused = SapProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    SapProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new SapServiceListener(), 10);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothSap bluetoothSap = this.mService;
        if (bluetoothSap == null) {
            return 0;
        }
        return bluetoothSap.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothSap bluetoothSap = this.mService;
        if (bluetoothSap != null && bluetoothSap.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothSap bluetoothSap = this.mService;
        if (bluetoothSap == null) {
            return 0;
        }
        return bluetoothSap.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothSap bluetoothSap = this.mService;
        if (bluetoothSap == null) {
            return false;
        }
        if (!z) {
            return bluetoothSap.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothSap.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothSap bluetoothSap = this.mService;
        if (bluetoothSap == null) {
            return new ArrayList(0);
        }
        return bluetoothSap.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_sap;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_sap_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_sap_profile_summary_connected;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(10, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up SAP proxy", th);
            }
        }
    }
}
