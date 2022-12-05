package com.android.settingslib.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDeviceGroup;
import android.bluetooth.BluetoothGroupCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.DeviceGroup;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.UUID;
/* loaded from: classes.dex */
public class DeviceGroupClientProfile implements LocalBluetoothProfile {
    private final CachedBluetoothDeviceManager mDeviceManager;
    private boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    private BluetoothDeviceGroup mService;
    private final BluetoothGroupCallback mGroupCallback = new BluetoothGroupCallback() { // from class: com.android.settingslib.bluetooth.DeviceGroupClientProfile.1
        public void onNewGroupFound(int i, BluetoothDevice bluetoothDevice, UUID uuid) {
            Log.d("DeviceGroupClientProfile", "onNewGroupFound()");
            CachedBluetoothDevice findDevice = DeviceGroupClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = DeviceGroupClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchNewGroupFound(findDevice, i, uuid);
            Log.d("DeviceGroupClientProfile", "Start Group Discovery for Audio capable device");
            DeviceGroupClientProfile.this.mService.startGroupDiscovery(i);
        }

        public void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
            Log.d("DeviceGroupClientProfile", "onGroupDiscoveryStatusChanged()");
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchGroupDiscoveryStatusChanged(i, i2, i3);
        }
    };
    private String mCallingPackage = ActivityThread.currentOpPackageName();

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return false;
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
    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getOrdinal() {
        return 3;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 24;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "DeviceGroup Client";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceGroupClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new GroupClientServiceListener(), 24);
    }

    /* loaded from: classes.dex */
    private final class GroupClientServiceListener implements BluetoothProfile.ServiceListener {
        private GroupClientServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            DeviceGroupClientProfile.this.mService = (BluetoothDeviceGroup) bluetoothProfile;
            DeviceGroupClientProfile.this.mIsProfileReady = true;
            Log.d("DeviceGroupClientProfile", "onServiceConnected: mCallingPackage = " + DeviceGroupClientProfile.this.mCallingPackage);
            if ("com.android.settings".equals(DeviceGroupClientProfile.this.mCallingPackage)) {
                DeviceGroupClientProfile.this.mService.registerGroupClientApp(DeviceGroupClientProfile.this.mGroupCallback, new Handler(Looper.getMainLooper()));
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            DeviceGroupClientProfile.this.mIsProfileReady = false;
        }
    }

    public boolean connectGroup(int i) {
        Log.d("DeviceGroupClientProfile", "connectGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        boolean z = false;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "connectGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e("DeviceGroupClientProfile", "Requested device group not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("DeviceGroupClientProfile", "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else if (!findDevice.isConnected()) {
                findDevice.connect(true);
                z = true;
            }
        }
        return z;
    }

    public boolean disconnectGroup(int i) {
        Log.d("DeviceGroupClientProfile", "disconnectGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        boolean z = false;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "connectGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e("DeviceGroupClientProfile", "Requested device group is not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("DeviceGroupClientProfile", "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else if (findDevice.isConnected()) {
                findDevice.disconnect();
                z = true;
            }
        }
        return z;
    }

    public boolean forgetGroup(int i) {
        Log.d("DeviceGroupClientProfile", "forgetGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "forgetGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e("DeviceGroupClientProfile", "Requested device group is not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("DeviceGroupClientProfile", "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else {
                findDevice.unpair();
            }
        }
        return true;
    }

    public boolean startGroupDiscovery(int i) {
        Log.d("DeviceGroupClientProfile", "startGroupDiscovery: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "startGroupDiscovery:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        return bluetoothDeviceGroup.startGroupDiscovery(i);
    }

    public boolean stopGroupDiscovery(int i) {
        Log.d("DeviceGroupClientProfile", "stopGroupDiscovery: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "stopGroupDiscovery:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        return bluetoothDeviceGroup.stopGroupDiscovery(i);
    }

    public DeviceGroup getGroup(int i) {
        Log.d("DeviceGroupClientProfile", "getGroup: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e("DeviceGroupClientProfile", "getGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return null;
        }
        return bluetoothDeviceGroup.getGroup(i, true);
    }

    public boolean isGroupDiscoveryInProgress(int i) {
        Log.d("DeviceGroupClientProfile", "isGroupDiscoveryInProgress: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null) {
            Log.e("DeviceGroupClientProfile", "Not connected to Profile Service. Return.");
            return false;
        }
        return bluetoothDeviceGroup.isGroupDiscoveryInProgress(i);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return this.mService != null;
    }

    protected void finalize() {
        Log.d("DeviceGroupClientProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(24, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("DeviceGroupClientProfile", "Error cleaning up BluetoothDeviceGroup proxy Object", th);
            }
        }
    }
}