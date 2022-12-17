package com.android.settings.development;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class BluetoothHwOffloadRebootDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {

    public interface OnHwOffloadDialogListener {
        void onHwOffloadDialogCanceled();

        void onHwOffloadDialogConfirmed();
    }

    public int getMetricsCategory() {
        return 1441;
    }

    public static void show(DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        FragmentManager supportFragmentManager = developmentSettingsDashboardFragment.getActivity().getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag("BluetoothHwOffloadReboot") == null) {
            BluetoothHwOffloadRebootDialog bluetoothHwOffloadRebootDialog = new BluetoothHwOffloadRebootDialog();
            bluetoothHwOffloadRebootDialog.setTargetFragment(developmentSettingsDashboardFragment, 0);
            bluetoothHwOffloadRebootDialog.show(supportFragmentManager, "BluetoothHwOffloadReboot");
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setMessage(R$string.bluetooth_disable_hw_offload_dialog_message).setTitle(R$string.bluetooth_disable_hw_offload_dialog_title).setPositiveButton(R$string.bluetooth_disable_hw_offload_dialog_confirm, (DialogInterface.OnClickListener) this).setNegativeButton(R$string.bluetooth_disable_hw_offload_dialog_cancel, (DialogInterface.OnClickListener) this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        OnHwOffloadDialogListener onHwOffloadDialogListener = (OnHwOffloadDialogListener) getTargetFragment();
        if (onHwOffloadDialogListener != null) {
            if (i == -1) {
                onHwOffloadDialogListener.onHwOffloadDialogConfirmed();
                ((PowerManager) getContext().getSystemService(PowerManager.class)).reboot((String) null);
                return;
            }
            onHwOffloadDialogListener.onHwOffloadDialogCanceled();
        }
    }
}
