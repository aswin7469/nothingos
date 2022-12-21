package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPbap;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;

public class PbapServerProfile implements LocalBluetoothProfile {
    public static final String NAME = "PBAP Server";
    private static final int ORDINAL = 6;
    static final ParcelUuid[] PBAB_CLIENT_UUIDS = {BluetoothUuid.HSP, BluetoothUuid.HFP, BluetoothUuid.PBAP_PCE};
    private static final String TAG = "PbapServerProfile";
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public BluetoothPbap mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return -1;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302817;
    }

    public int getOrdinal() {
        return 6;
    }

    public int getProfileId() {
        return 6;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public String toString() {
        return NAME;
    }

    private final class PbapServiceListener implements BluetoothProfile.ServiceListener {
        private PbapServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothPbap unused = PbapServerProfile.this.mService = (BluetoothPbap) bluetoothProfile;
            boolean unused2 = PbapServerProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            boolean unused = PbapServerProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    PbapServerProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new PbapServiceListener(), 6);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothPbap bluetoothPbap = this.mService;
        if (bluetoothPbap == null) {
            return 0;
        }
        return bluetoothPbap.getConnectionState(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothPbap bluetoothPbap = this.mService;
        if (bluetoothPbap != null && !z) {
            return bluetoothPbap.setConnectionPolicy(bluetoothDevice, 0);
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
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(6, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up PBAP proxy", th);
            }
        }
    }
}
