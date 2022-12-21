package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothA2dpSink;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;

final class A2dpSinkProfile implements LocalBluetoothProfile {
    static final String NAME = "A2DPSink";
    private static final int ORDINAL = 5;
    static final ParcelUuid[] SRC_UUIDS = {BluetoothUuid.A2DP_SOURCE, BluetoothUuid.ADV_AUDIO_DIST};
    private static final String TAG = "A2dpSinkProfile";
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothA2dpSink mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302336;
    }

    public int getOrdinal() {
        return 5;
    }

    public int getProfileId() {
        return 11;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class A2dpSinkServiceListener implements BluetoothProfile.ServiceListener {
        private A2dpSinkServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothA2dpSink unused = A2dpSinkProfile.this.mService = (BluetoothA2dpSink) bluetoothProfile;
            List connectedDevices = A2dpSinkProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = A2dpSinkProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w(A2dpSinkProfile.TAG, "A2dpSinkProfile found new device: " + bluetoothDevice);
                    findDevice = A2dpSinkProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(A2dpSinkProfile.this, 2);
                findDevice.refresh();
            }
            boolean unused2 = A2dpSinkProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            boolean unused = A2dpSinkProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    A2dpSinkProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new A2dpSinkServiceListener(), 11);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink == null) {
            return new ArrayList(0);
        }
        return bluetoothA2dpSink.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink == null) {
            return 0;
        }
        return bluetoothA2dpSink.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink != null && bluetoothA2dpSink.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink == null) {
            return 0;
        }
        return bluetoothA2dpSink.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink == null) {
            return false;
        }
        if (!z) {
            return bluetoothA2dpSink.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothA2dpSink.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isAudioPlaying() {
        BluetoothA2dpSink bluetoothA2dpSink = this.mService;
        if (bluetoothA2dpSink == null) {
            return false;
        }
        List connectedDevices = bluetoothA2dpSink.getConnectedDevices();
        if (connectedDevices.isEmpty() || !this.mService.isAudioPlaying((BluetoothDevice) connectedDevices.get(0))) {
            return false;
        }
        return true;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_a2dp;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_a2dp_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_a2dp_profile_summary_connected;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(11, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up A2DP proxy", th);
            }
        }
    }
}
