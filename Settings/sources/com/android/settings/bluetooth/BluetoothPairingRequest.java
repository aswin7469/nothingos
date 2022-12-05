package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.UserHandle;
/* loaded from: classes.dex */
public final class BluetoothPairingRequest extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null || !action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            return;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        int intExtra = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_VARIANT", Integer.MIN_VALUE);
        String str = null;
        String address = bluetoothDevice != null ? bluetoothDevice.getAddress() : null;
        if (bluetoothDevice != null) {
            str = bluetoothDevice.getName();
        }
        boolean shouldShowDialogInForeground = LocalBluetoothPreferences.shouldShowDialogInForeground(context, address, str);
        if (intExtra == 3 && bluetoothDevice.canBondWithoutDialog()) {
            bluetoothDevice.setPairingConfirmation(true);
        } else if (powerManager.isInteractive() && shouldShowDialogInForeground) {
            context.startActivityAsUser(BluetoothPairingService.getPairingDialogIntent(context, intent, 1), UserHandle.CURRENT);
        } else {
            intent.setClass(context, BluetoothPairingService.class);
            intent.setAction("android.bluetooth.device.action.PAIRING_REQUEST");
            context.startServiceAsUser(intent, UserHandle.CURRENT);
        }
    }
}
