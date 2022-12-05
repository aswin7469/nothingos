package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothVcp;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.R$string;
/* loaded from: classes.dex */
public class VcpProfile implements LocalBluetoothProfile {
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private final CachedBluetoothDeviceManager mDeviceManager;
    private boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    private BluetoothVcp mService;

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getOrdinal() {
        return 1;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 26;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "VCP";
    }

    /* loaded from: classes.dex */
    private final class VcpServiceListener implements BluetoothProfile.ServiceListener {
        private VcpServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            VcpProfile.this.mService = (BluetoothVcp) bluetoothProfile;
            Log.w("VcpProfile", "Bluetooth service Connected");
            VcpProfile.this.mIsProfileReady = true;
            VcpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.w("VcpProfile", "Bluetooth service Disconnected");
            VcpProfile.this.mIsProfileReady = false;
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VcpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new VcpServiceListener(), 26);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return 0;
        }
        return bluetoothVcp.getConnectionState(bluetoothDevice);
    }

    public int getConnectionMode(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return 0;
        }
        return bluetoothVcp.getConnectionMode(bluetoothDevice);
    }

    public void setAbsoluteVolume(BluetoothDevice bluetoothDevice, int i) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return;
        }
        bluetoothVcp.setAbsoluteVolume(bluetoothDevice, i);
    }

    public int getAbsoluteVolume(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return -1;
        }
        return bluetoothVcp.getAbsoluteVolume(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_vcp;
    }

    protected void finalize() {
        Log.d("VcpProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(26, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("VcpProfile", "Error cleaning up Vcp proxy", th);
            }
        }
    }
}
