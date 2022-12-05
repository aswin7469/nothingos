package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothBroadcast;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import androidx.annotation.Keep;
import com.android.settingslib.R$string;
@Keep
/* loaded from: classes.dex */
public final class BroadcastProfile implements LocalBluetoothProfile {
    static final String NAME = "Broadcast";
    private static final int ORDINAL = 0;
    private static final String TAG = "BroadcastProfile";
    private static boolean V = true;
    private boolean mIsProfileReady = false;
    private BluetoothBroadcast mService;

    public boolean connect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getOrdinal() {
        return 0;
    }

    public int getPreferred(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean isPreferred(BluetoothDevice bluetoothDevice) {
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public void setPreferred(BluetoothDevice bluetoothDevice, boolean z) {
    }

    public String toString() {
        return NAME;
    }

    /* loaded from: classes.dex */
    private final class BroadcastListener implements BluetoothProfile.ServiceListener {
        private BroadcastListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (i == 25) {
                if (BroadcastProfile.V) {
                    Log.d(BroadcastProfile.TAG, "Bluetooth Broadcast service connected");
                }
                BroadcastProfile.this.mService = (BluetoothBroadcast) bluetoothProfile;
                BroadcastProfile.this.mIsProfileReady = true;
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            if (i == 25) {
                if (BroadcastProfile.V) {
                    Log.d(BroadcastProfile.TAG, "Bluetooth Broadcast service disconnected");
                }
                BroadcastProfile.this.mIsProfileReady = false;
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isProfileReady() {
        Log.d(TAG, "isProfileReady = " + this.mIsProfileReady);
        return this.mIsProfileReady;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        Log.d(TAG, "getProfileId");
        return 25;
    }

    BroadcastProfile(Context context) {
        Log.d(TAG, "BroadcastProfile constructor");
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new BroadcastListener(), 25);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        Log.d(TAG, "accessProfileEnabled");
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_broadcast;
    }

    public boolean setEncryption(boolean z, int i, boolean z2) {
        Log.d(TAG, "setEncryption");
        return this.mService.SetEncryption(z, i, z2);
    }

    public byte[] getEncryptionKey() {
        Log.d(TAG, "getEncryptionKey");
        return this.mService.GetEncryptionKey();
    }

    public int getBroadcastStatus() {
        Log.d(TAG, "getBroadcastStatus");
        return this.mService.GetBroadcastStatus();
    }

    public boolean setBroadcastMode(boolean z) {
        Log.d(TAG, "setBroadcastMode");
        return this.mService.SetBroadcastMode(z);
    }

    protected void finalize() {
        if (V) {
            Log.d(TAG, "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(25, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up Broadcast proxy", th);
            }
        }
    }
}
