package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcastAssistant;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcastMetadata;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;

public class QrCodeScanModeController {
    private LocalBluetoothManager mLocalBluetoothManager;
    private LocalBluetoothLeBroadcastAssistant mLocalBroadcastAssistant;
    private LocalBluetoothLeBroadcastMetadata mLocalBroadcastMetadata = new LocalBluetoothLeBroadcastMetadata();
    private LocalBluetoothProfileManager mProfileManager;

    public QrCodeScanModeController(Context context) {
        Log.d("QrCodeScanModeController", "QrCodeScanModeController constructor.");
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.mLocalBluetoothManager = localBtManager;
        this.mProfileManager = localBtManager.getProfileManager();
        this.mLocalBroadcastAssistant = new LocalBluetoothLeBroadcastAssistant(context, new CachedBluetoothDeviceManager(context, this.mLocalBluetoothManager), this.mProfileManager);
    }

    private BluetoothLeBroadcastMetadata convertToBroadcastMetadata(String str) {
        return this.mLocalBroadcastMetadata.convertToBroadcastMetadata(str);
    }

    public void addSource(BluetoothDevice bluetoothDevice, String str, boolean z) {
        this.mLocalBroadcastAssistant.addSource(bluetoothDevice, convertToBroadcastMetadata(str), z);
    }
}
