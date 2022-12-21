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
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;
import java.util.UUID;

public class DeviceGroupClientProfile implements LocalBluetoothProfile {
    private static final String GROUP_APP = "com.android.settings";
    static final String NAME = "DeviceGroup Client";
    private static final int ORDINAL = 3;
    private static final String TAG = "DeviceGroupClientProfile";
    /* access modifiers changed from: private */
    public String mCallingPackage;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public final BluetoothGroupCallback mGroupCallback = new BluetoothGroupCallback() {
        public void onNewGroupFound(int i, BluetoothDevice bluetoothDevice, UUID uuid) {
            Log.d(DeviceGroupClientProfile.TAG, "onNewGroupFound()");
            CachedBluetoothDevice findDevice = DeviceGroupClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = DeviceGroupClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchNewGroupFound(findDevice, i, uuid);
            Log.d(DeviceGroupClientProfile.TAG, "Start Group Discovery for Audio capable device");
            DeviceGroupClientProfile.this.mService.startGroupDiscovery(i);
        }

        public void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
            Log.d(DeviceGroupClientProfile.TAG, "onGroupDiscoveryStatusChanged()");
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchGroupDiscoveryStatusChanged(i, i2, i3);
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothDeviceGroup mService;

    public boolean accessProfileEnabled() {
        return false;
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getOrdinal() {
        return 3;
    }

    public int getPreferred(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getProfileId() {
        return 32;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isPreferred(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public void setPreferred(BluetoothDevice bluetoothDevice, boolean z) {
    }

    public String toString() {
        return NAME;
    }

    DeviceGroupClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        this.mCallingPackage = ActivityThread.currentOpPackageName();
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new GroupClientServiceListener(), 32);
    }

    private final class GroupClientServiceListener implements BluetoothProfile.ServiceListener {
        private GroupClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothDeviceGroup unused = DeviceGroupClientProfile.this.mService = (BluetoothDeviceGroup) bluetoothProfile;
            boolean unused2 = DeviceGroupClientProfile.this.mIsProfileReady = true;
            Log.d(DeviceGroupClientProfile.TAG, "onServiceConnected: mCallingPackage = " + DeviceGroupClientProfile.this.mCallingPackage);
            if ("com.android.settings".equals(DeviceGroupClientProfile.this.mCallingPackage)) {
                DeviceGroupClientProfile.this.mService.registerGroupClientApp(DeviceGroupClientProfile.this.mGroupCallback, new Handler(Looper.getMainLooper()));
            }
        }

        public void onServiceDisconnected(int i) {
            boolean unused = DeviceGroupClientProfile.this.mIsProfileReady = false;
        }
    }

    public boolean connectGroup(int i) {
        Log.d(TAG, "connectGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        boolean z = false;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e(TAG, "connectGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e(TAG, "Requested device group not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w(TAG, "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else if (!findDevice.isConnected()) {
                findDevice.connect(true);
                z = true;
            }
        }
        return z;
    }

    public boolean disconnectGroup(int i) {
        Log.d(TAG, "disconnectGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        boolean z = false;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e(TAG, "connectGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e(TAG, "Requested device group is not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w(TAG, "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else if (findDevice.isConnected()) {
                findDevice.disconnect();
                z = true;
            }
        }
        return z;
    }

    public boolean forgetGroup(int i) {
        Log.d(TAG, "forgetGroup(): groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup == null || !this.mIsProfileReady) {
            Log.e(TAG, "forgetGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
            return false;
        }
        DeviceGroup group = bluetoothDeviceGroup.getGroup(i);
        if (group == null || group.getDeviceGroupMembers().size() == 0) {
            Log.e(TAG, "Requested device group is not found");
            return false;
        }
        for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w(TAG, "CachedBluetoothDevice not found for device: " + bluetoothDevice);
            } else {
                findDevice.unpair();
            }
        }
        return true;
    }

    public boolean startGroupDiscovery(int i) {
        Log.d(TAG, "startGroupDiscovery: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null && this.mIsProfileReady) {
            return bluetoothDeviceGroup.startGroupDiscovery(i);
        }
        Log.e(TAG, "startGroupDiscovery:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
        return false;
    }

    public boolean stopGroupDiscovery(int i) {
        Log.d(TAG, "stopGroupDiscovery: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null && this.mIsProfileReady) {
            return bluetoothDeviceGroup.stopGroupDiscovery(i);
        }
        Log.e(TAG, "stopGroupDiscovery:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
        return false;
    }

    public DeviceGroup getGroup(int i) {
        Log.d(TAG, "getGroup: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null && this.mIsProfileReady) {
            return bluetoothDeviceGroup.getGroup(i, true);
        }
        Log.e(TAG, "getGroup:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
        return null;
    }

    public List<DeviceGroup> getDiscoveredGroups() {
        Log.d(TAG, "getDiscoveredGroups");
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null && this.mIsProfileReady) {
            return bluetoothDeviceGroup.getDiscoveredGroups(true);
        }
        Log.e(TAG, "getDiscoveredGroups:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
        return null;
    }

    public boolean isGroupDiscoveryInProgress(int i) {
        Log.d(TAG, "isGroupDiscoveryInProgress: groupId = " + i);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null) {
            return bluetoothDeviceGroup.isGroupDiscoveryInProgress(i);
        }
        Log.e(TAG, "Not connected to Profile Service. Return.");
        return false;
    }

    public int getRemoteDeviceGroupId(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "getRemoteDeviceGroupId: device = " + bluetoothDevice);
        BluetoothDeviceGroup bluetoothDeviceGroup = this.mService;
        if (bluetoothDeviceGroup != null && this.mIsProfileReady) {
            return bluetoothDeviceGroup.getRemoteDeviceGroupId(bluetoothDevice, (ParcelUuid) null, true);
        }
        Log.e(TAG, "getRemoteDeviceGroupId:  mService = " + this.mService + " mIsProfileReady = " + this.mIsProfileReady);
        return 16;
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return this.mService != null;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(32, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up BluetoothDeviceGroup proxy Object", th);
            }
        }
    }
}
