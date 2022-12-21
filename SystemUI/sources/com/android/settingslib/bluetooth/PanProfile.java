package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.HashMap;
import java.util.List;

public class PanProfile implements LocalBluetoothProfile {
    static final String NAME = "PAN";
    private static final int ORDINAL = 4;
    private static final String TAG = "PanProfile";
    private final HashMap<BluetoothDevice, Integer> mDeviceRoleMap = new HashMap<>();
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public BluetoothPan mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return -1;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302341;
    }

    public int getOrdinal() {
        return 4;
    }

    public int getProfileId() {
        return 5;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class PanServiceListener implements BluetoothProfile.ServiceListener {
        private PanServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothPan unused = PanProfile.this.mService = (BluetoothPan) bluetoothProfile;
            boolean unused2 = PanProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            boolean unused = PanProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    PanProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new PanServiceListener(), 5);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothPan bluetoothPan = this.mService;
        if (bluetoothPan == null) {
            return 0;
        }
        return bluetoothPan.getConnectionState(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothPan bluetoothPan = this.mService;
        if (bluetoothPan == null) {
            return false;
        }
        if (!z) {
            return bluetoothPan.setConnectionPolicy(bluetoothDevice, 0);
        }
        List<BluetoothDevice> connectedDevices = bluetoothPan.getConnectedDevices();
        if (connectedDevices != null) {
            for (BluetoothDevice connectionPolicy : connectedDevices) {
                this.mService.setConnectionPolicy(connectionPolicy, 0);
            }
        }
        return this.mService.setConnectionPolicy(bluetoothDevice, 100);
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        if (isLocalRoleNap(bluetoothDevice)) {
            return C1757R.string.bluetooth_profile_pan_nap;
        }
        return C1757R.string.bluetooth_profile_pan;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_pan_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        if (isLocalRoleNap(bluetoothDevice)) {
            return C1757R.string.bluetooth_pan_nap_profile_summary_connected;
        }
        return C1757R.string.bluetooth_pan_user_profile_summary_connected;
    }

    /* access modifiers changed from: package-private */
    public void setLocalRole(BluetoothDevice bluetoothDevice, int i) {
        this.mDeviceRoleMap.put(bluetoothDevice, Integer.valueOf(i));
    }

    /* access modifiers changed from: package-private */
    public boolean isLocalRoleNap(BluetoothDevice bluetoothDevice) {
        if (!this.mDeviceRoleMap.containsKey(bluetoothDevice) || this.mDeviceRoleMap.get(bluetoothDevice).intValue() != 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(5, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up PAN proxy", th);
            }
        }
    }
}
