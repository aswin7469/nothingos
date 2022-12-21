package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPbapClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

public final class PbapClientProfile implements LocalBluetoothProfile {
    static final String NAME = "PbapClient";
    private static final int ORDINAL = 6;
    static final ParcelUuid[] SRC_UUIDS = {BluetoothUuid.PBAP_PSE};
    private static final String TAG = "PbapClientProfile";
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothPbapClient mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302817;
    }

    public int getOrdinal() {
        return 6;
    }

    public int getProfileId() {
        return 17;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class PbapClientServiceListener implements BluetoothProfile.ServiceListener {
        private PbapClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothPbapClient unused = PbapClientProfile.this.mService = (BluetoothPbapClient) bluetoothProfile;
            List connectedDevices = PbapClientProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = PbapClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(PbapClientProfile.TAG, "PbapClientProfile found new device: " + bluetoothDevice);
                    findDevice = PbapClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(PbapClientProfile.this, 2);
                findDevice.refresh();
            }
            boolean unused2 = PbapClientProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            boolean unused = PbapClientProfile.this.mIsProfileReady = false;
        }
    }

    private void refreshProfiles() {
        for (CachedBluetoothDevice onUuidChanged : this.mDeviceManager.getCachedDevicesCopy()) {
            onUuidChanged.onUuidChanged();
        }
    }

    public boolean pbapClientExists() {
        return this.mService != null;
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    PbapClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new PbapClientServiceListener(), 17);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return new ArrayList(0);
        }
        return bluetoothPbapClient.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return 0;
        }
        return bluetoothPbapClient.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient != null && bluetoothPbapClient.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return 0;
        }
        return bluetoothPbapClient.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothPbapClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothPbapClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_pbap;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_pbap_summary;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(17, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up PBAP Client proxy", th);
            }
        }
    }
}
