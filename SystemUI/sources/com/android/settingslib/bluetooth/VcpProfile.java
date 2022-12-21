package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothVcp;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;

public class VcpProfile implements LocalBluetoothProfile {
    static final String NAME = "VCP";
    private static final int ORDINAL = 1;
    private static final String TAG = "VcpProfile";

    /* renamed from: V */
    private static boolean f243V = true;
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothVcp mService;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return -1;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 34;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return NAME;
    }

    private final class VcpServiceListener implements BluetoothProfile.ServiceListener {
        private VcpServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothVcp unused = VcpProfile.this.mService = (BluetoothVcp) bluetoothProfile;
            Log.w(VcpProfile.TAG, "Bluetooth service Connected");
            boolean unused2 = VcpProfile.this.mIsProfileReady = true;
            VcpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            Log.w(VcpProfile.TAG, "Bluetooth service Disconnected");
            boolean unused = VcpProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    VcpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new VcpServiceListener(), 34);
    }

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
        if (bluetoothVcp != null) {
            bluetoothVcp.setAbsoluteVolume(bluetoothDevice, i);
        }
    }

    public int getAbsoluteVolume(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return -1;
        }
        return bluetoothVcp.getAbsoluteVolume(bluetoothDevice);
    }

    public void setMute(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp != null) {
            bluetoothVcp.setMute(bluetoothDevice, z);
        }
    }

    public boolean isMute(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return false;
        }
        return bluetoothVcp.isMute(bluetoothDevice);
    }

    public boolean setActiveProfile(BluetoothDevice bluetoothDevice, int i, int i2) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp != null) {
            return bluetoothVcp.setActiveProfile(bluetoothDevice, i, i2);
        }
        return false;
    }

    public int getActiveProfile(int i) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp != null) {
            return bluetoothVcp.getActiveProfile(i);
        }
        return -1;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_vcp;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(34, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up Vcp proxy", th);
            }
        }
    }
}
