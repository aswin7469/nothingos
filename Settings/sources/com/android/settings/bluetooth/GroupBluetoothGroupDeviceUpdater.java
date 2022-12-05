package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public abstract class GroupBluetoothGroupDeviceUpdater extends GroupBluetoothDeviceUpdater {
    public GroupBluetoothGroupDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (!this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = new BluetoothDevicePreference(this.mPrefContext, cachedBluetoothDevice, true, i, true);
            bluetoothDevicePreference.setOnGearClickListener(this.mDeviceProfilesListener);
            this.mPreferenceMap.put(device, bluetoothDevicePreference);
            this.mDevicePreferenceCallback.onDeviceAdded(bluetoothDevicePreference);
        }
    }
}
