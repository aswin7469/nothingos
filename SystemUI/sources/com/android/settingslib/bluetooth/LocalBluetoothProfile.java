package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public interface LocalBluetoothProfile {
    boolean accessProfileEnabled();

    int getConnectionPolicy(BluetoothDevice bluetoothDevice);

    int getConnectionStatus(BluetoothDevice bluetoothDevice);

    int getDrawableResource(BluetoothClass bluetoothClass);

    int getNameResource(BluetoothDevice bluetoothDevice);

    int getOrdinal();

    int getProfileId();

    int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice);

    boolean isAutoConnectable();

    boolean isEnabled(BluetoothDevice bluetoothDevice);

    boolean isProfileReady();

    boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z);
}
