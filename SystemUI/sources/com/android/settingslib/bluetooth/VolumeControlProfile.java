package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public class VolumeControlProfile implements LocalBluetoothProfile {
    static final String NAME = "VCP";
    private static final int ORDINAL = 23;
    private static final String TAG = "VolumeControlProfile";

    public boolean accessProfileEnabled() {
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
        return 23;
    }

    public int getProfileId() {
        return 23;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean isProfileReady() {
        return true;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return NAME;
    }
}
