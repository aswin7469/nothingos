package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

public final class BroadcastScanAssistanceUtils {
    static void debug(String str, String str2) {
        Log.d(str, str2);
    }

    static boolean isLocalDevice(BluetoothDevice bluetoothDevice) {
        boolean equals = bluetoothDevice != null ? BluetoothAdapter.getDefaultAdapter().getAddress().equals(bluetoothDevice.getAddress()) : false;
        Log.d("BroadcastScanAsssitanceBroadcastScanAssistanceUtils", "isLocalBroadcastSource returns" + equals);
        return equals;
    }

    static AlertDialog showScanAssistError(Context context, String str, int i, DialogInterface.OnClickListener onClickListener) {
        return showScanAssistError(context, str, i, Utils.getLocalBtManager(context), onClickListener);
    }

    private static AlertDialog showScanAssistError(Context context, String str, int i, LocalBluetoothManager localBluetoothManager, DialogInterface.OnClickListener onClickListener) {
        String string = context.getString(i, new Object[]{str});
        Context foregroundActivity = localBluetoothManager.getForegroundActivity();
        if (localBluetoothManager.isForegroundActivity()) {
            try {
                return new AlertDialog.Builder(foregroundActivity).setTitle(R$string.bluetooth_error_title).setMessage((CharSequence) string).setPositiveButton(17039370, onClickListener).show();
            } catch (Exception e) {
                Log.e("BroadcastScanAsssitanceBroadcastScanAssistanceUtils", "Cannot show error dialog.", e);
                return null;
            }
        } else {
            Toast.makeText(context, string, 0).show();
            return null;
        }
    }

    static AlertDialog showScanAssistDetailsDialog(Context context, AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, CharSequence charSequence, CharSequence charSequence2, View view) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(17039370, onClickListener).setNegativeButton(17039360, onClickListener2).setView(view).create();
        } else if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog.setTitle(charSequence);
        alertDialog.setMessage(charSequence2);
        alertDialog.show();
        return alertDialog;
    }

    static AlertDialog showAssistanceGroupOptionsDialog(Context context, AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, CharSequence charSequence, CharSequence charSequence2) {
        if (alertDialog == null) {
            Log.d("BroadcastScanAsssitanceBroadcastScanAssistanceUtils", "showAssistanceGroupOptionsDialog creation");
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(R$string.yes, onClickListener).setNegativeButton(R$string.f142no, onClickListener2).create();
        } else if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog.setTitle(charSequence);
        alertDialog.setMessage(charSequence2);
        alertDialog.show();
        return alertDialog;
    }
}
