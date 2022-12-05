package com.android.settingslib.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistCallback;
import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSyncHelper;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import androidx.annotation.Keep;
import com.android.settingslib.R$string;
import java.util.ArrayList;
import java.util.List;
@Keep
/* loaded from: classes.dex */
public class BCProfile implements LocalBluetoothProfile {
    static final String NAME = "BCProfile";
    private static final int ORDINAL = 1;
    private static final String TAG = "BCProfile";
    private static boolean V = true;
    private Context mContext;
    private final CachedBluetoothDeviceManager mDeviceManager;
    private boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    private BluetoothSyncHelper mService;

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return 100;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302330;
    }

    public int getOrdinal() {
        return 1;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 27;
    }

    public String toString() {
        return "BCProfile";
    }

    /* loaded from: classes.dex */
    private final class BassclientServiceListener implements BluetoothProfile.ServiceListener {
        private BassclientServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("BCProfile", "BassclientService connected");
            BCProfile.this.mService = (BluetoothSyncHelper) bluetoothProfile;
            BCProfile.this.mIsProfileReady = true;
            BCProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.d("BCProfile", "BassclientService disconnected");
            BCProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        if (z) {
            Log.d("BCProfile", "BCProfile: " + bluetoothDevice + ":" + z);
            if (this.mService.getConnectionPolicy(bluetoothDevice) >= 100) {
                return false;
            }
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return bluetoothSyncHelper.setConnectionPolicy(bluetoothDevice, 0);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        return bluetoothSyncHelper != null && bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) > 0;
    }

    BCProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new BassclientServiceListener(), 27);
    }

    public boolean isAutoConnectable() {
        if (this.mService == null) {
            return false;
        }
        Log.d("BCProfile", "isAutoConnectable return false");
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return getDevicesByStates(new int[]{2, 1, 3});
    }

    public List<BluetoothDevice> getConnectableDevices() {
        return getDevicesByStates(new int[]{0, 2, 1, 3});
    }

    private List<BluetoothDevice> getDevicesByStates(int[] iArr) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return new ArrayList(0);
        }
        return bluetoothSyncHelper.getDevicesMatchingConnectionStates(iArr);
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        Log.d("BCProfile", "BCProfile Connect to  device: " + bluetoothDevice);
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        return bluetoothSyncHelper.connect(bluetoothDevice);
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        Log.d("BCProfile", "BCProfile disonnect to  device: " + bluetoothDevice);
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        if (bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) > 100) {
            this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return this.mService.disconnect(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return 0;
        }
        return bluetoothSyncHelper.getConnectionState(bluetoothDevice);
    }

    public int getPreferred(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return -1;
        }
        return bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice);
    }

    public void setPreferred(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return;
        }
        if (z) {
            if (bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) == 100) {
                return;
            }
            this.mService.setConnectionPolicy(bluetoothDevice, 100);
            return;
        }
        bluetoothSyncHelper.setConnectionPolicy(bluetoothDevice, -1);
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_bc;
    }

    public BleBroadcastAudioScanAssistManager getBSAManager(BluetoothDevice bluetoothDevice, BleBroadcastAudioScanAssistCallback bleBroadcastAudioScanAssistCallback) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            Log.d("BCProfile", "getBroadcastAudioScanAssistManager: service is null");
            return null;
        }
        return bluetoothSyncHelper.getBleBroadcastAudioScanAssistManager(bluetoothDevice, bleBroadcastAudioScanAssistCallback);
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus != 0) {
            if (connectionStatus == 2) {
                return R$string.bluetooth_bc_profile_summary_connected;
            }
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return R$string.bluetooth_bc_profile_summary_use_for;
    }

    protected void finalize() {
        Log.d("BCProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(27, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("BCProfile", "Error cleaning up BAss client proxy", th);
            }
        }
    }

    static boolean isBCSupported() {
        Log.d("BCProfile", "BassClientProfile: isBCSupported returns true");
        return true;
    }

    public static boolean isBASeeker(BluetoothDevice bluetoothDevice) {
        ParcelUuid[] uuids = bluetoothDevice != null ? bluetoothDevice.getUuids() : null;
        ParcelUuid fromString = ParcelUuid.fromString("0000184F-0000-1000-8000-00805F9B34FB");
        boolean z = false;
        z = false;
        if (isBCSupported() && uuids != null) {
            boolean z2 = false;
            for (ParcelUuid parcelUuid : uuids) {
                if (parcelUuid.equals(fromString)) {
                    Log.d("BCProfile", "SD uuid present");
                    z2 = true;
                }
            }
            z = z2;
        }
        Log.d("BCProfile", "isBASeeker returns:" + z);
        return z;
    }
}
