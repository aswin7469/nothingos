package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDun;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.R$string;
/* loaded from: classes.dex */
public final class DunServerProfile implements LocalBluetoothProfile {
    private static boolean V = true;
    private boolean mIsProfileReady;
    private BluetoothDun mService;

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302333;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getOrdinal() {
        return 11;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 23;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return true;
    }

    public String toString() {
        return "DUN Server";
    }

    /* loaded from: classes.dex */
    private final class DunServiceListener implements BluetoothProfile.ServiceListener {
        private DunServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (DunServerProfile.V) {
                Log.d("DunServerProfile", "Bluetooth service connected");
            }
            DunServerProfile.this.mService = (BluetoothDun) bluetoothProfile;
            DunServerProfile.this.mIsProfileReady = true;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            if (DunServerProfile.V) {
                Log.d("DunServerProfile", "Bluetooth service disconnected");
            }
            DunServerProfile.this.mIsProfileReady = false;
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DunServerProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new DunServiceListener(), 23);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothDun bluetoothDun = this.mService;
        if (bluetoothDun == null) {
            return 0;
        }
        return bluetoothDun.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_dun;
    }

    protected void finalize() {
        if (V) {
            Log.d("DunServerProfile", "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(23, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("DunServerProfile", "Error cleaning up DUN proxy", th);
            }
        }
    }
}