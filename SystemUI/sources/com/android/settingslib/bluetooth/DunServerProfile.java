package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDun;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;

public final class DunServerProfile implements LocalBluetoothProfile {
    static final String NAME = "DUN Server";
    private static final int ORDINAL = 11;
    private static final String TAG = "DunServerProfile";
    /* access modifiers changed from: private */

    /* renamed from: V */
    public static boolean f241V = true;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public BluetoothDun mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return -1;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302341;
    }

    public int getOrdinal() {
        return 11;
    }

    public int getProfileId() {
        return 31;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return true;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class DunServiceListener implements BluetoothProfile.ServiceListener {
        private DunServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (DunServerProfile.f241V) {
                Log.d(DunServerProfile.TAG, "Bluetooth service connected");
            }
            BluetoothDun unused = DunServerProfile.this.mService = (BluetoothDun) bluetoothProfile;
            boolean unused2 = DunServerProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            if (DunServerProfile.f241V) {
                Log.d(DunServerProfile.TAG, "Bluetooth service disconnected");
            }
            boolean unused = DunServerProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    DunServerProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new DunServiceListener(), 31);
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        BluetoothDun bluetoothDun = this.mService;
        if (bluetoothDun == null) {
            return false;
        }
        return bluetoothDun.disconnect(bluetoothDevice);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothDun bluetoothDun = this.mService;
        if (bluetoothDun == null) {
            return 0;
        }
        return bluetoothDun.getConnectionState(bluetoothDevice);
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_dun;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_dun_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_dun_profile_summary_connected;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (f241V) {
            Log.d(TAG, "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(31, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up DUN proxy", th);
            }
        }
    }
}
