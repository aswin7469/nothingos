package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public class SavedBluetoothTwsDeviceUpdater extends BluetoothDeviceUpdater {
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    protected String getPreferenceKey() {
        return "saved_bt_tws";
    }

    public SavedBluetoothTwsDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        return device.getBondState() == 12 && !device.isConnected() && device.isTwsPlusDevice() && !isGroupDevice(cachedBluetoothDevice) && !isPrivateAddr(cachedBluetoothDevice);
    }
}
