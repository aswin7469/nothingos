package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothCodecStatus;
import java.util.UUID;
/* loaded from: classes.dex */
public interface BluetoothCallback {
    default void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
    }

    default void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    default void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    default void onAudioModeChanged() {
    }

    default void onBluetoothStateChanged(int i) {
    }

    default void onBroadcastKeyGenerated() {
    }

    default void onBroadcastStateChanged(int i) {
    }

    default void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    default void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    default void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    default void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    default void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
    }

    default void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
    }

    default void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
    }

    default void onScanningStateChanged(boolean z) {
    }
}
