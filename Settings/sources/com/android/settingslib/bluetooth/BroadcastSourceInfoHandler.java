package com.android.settingslib.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.settingslib.bluetooth.BluetoothEventManager;
/* loaded from: classes.dex */
public class BroadcastSourceInfoHandler implements BluetoothEventManager.Handler {
    public static final /* synthetic */ int $r8$clinit = 0;
    private static final boolean V = Log.isLoggable("BroadcastSourceInfoHandler", 2);
    private final CachedBluetoothDeviceManager mDeviceManager;

    BroadcastSourceInfoHandler(CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
    }

    @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
    public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            Log.w("BroadcastSourceInfoHandler", "BroadcastSourceInfoHandler: device is null");
        } else if (intent.getAction() == null) {
            Log.w("BroadcastSourceInfoHandler", "BroadcastSourceInfoHandler: action is null");
        } else {
            BleBroadcastSourceInfo parcelableExtra = intent.getParcelableExtra("android.bluetooth.device.extra.SOURCE_INFO");
            int intExtra = intent.getIntExtra("android.bluetooth.device.extra.SOURCE_INFO_INDEX", Integer.MIN_VALUE);
            int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.MAX_NUM_SOURCE_INFOS", Integer.MIN_VALUE);
            if (V) {
                Log.d("BroadcastSourceInfoHandler", "Rcved :BCAST_RECEIVER_STATE Intent for : " + bluetoothDevice);
                Log.d("BroadcastSourceInfoHandler", "Rcvd BroadcastSourceInfo index=" + intExtra);
                Log.d("BroadcastSourceInfoHandler", "Rcvd max num of source Info=" + intExtra2);
                Log.d("BroadcastSourceInfoHandler", "Rcvd BroadcastSourceInfo=" + parcelableExtra);
            }
            CachedBluetoothDevice findDevice = this.mDeviceManager.findDevice(bluetoothDevice);
            VendorCachedBluetoothDevice vendorCachedBluetoothDevice = VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(findDevice, null);
            if (vendorCachedBluetoothDevice != null) {
                vendorCachedBluetoothDevice.onBroadcastReceiverStateChanged(parcelableExtra, intExtra, intExtra2);
                findDevice.lambda$refresh$0();
                return;
            }
            Log.e("BroadcastSourceInfoHandler", "No vCachedDevice created for this Device");
        }
    }
}
