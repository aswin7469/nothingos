package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import com.android.settingslib.C1757R;

final class OppProfile implements LocalBluetoothProfile {
    static final String NAME = "OPP";
    private static final int ORDINAL = 2;

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

    public int getOrdinal() {
        return 2;
    }

    public int getProfileId() {
        return 20;
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

    public boolean isProfileReady() {
        return true;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return NAME;
    }

    OppProfile() {
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_opp;
    }
}
