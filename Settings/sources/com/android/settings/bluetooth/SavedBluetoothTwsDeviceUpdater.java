package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class SavedBluetoothTwsDeviceUpdater extends BluetoothDeviceUpdater {
    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "saved_bt_tws";
    }

    public SavedBluetoothTwsDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        return device.getBondState() == 12 && !device.isConnected() && device.isTwsPlusDevice() && !isGroupDevice(cachedBluetoothDevice) && !isPrivateAddr(cachedBluetoothDevice);
    }
}
