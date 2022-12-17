package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class HearingAidPairingDialogFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 2;
    }

    public static HearingAidPairingDialogFragment newInstance(CachedBluetoothDevice cachedBluetoothDevice) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("cached_device_side", cachedBluetoothDevice.getDeviceSide());
        HearingAidPairingDialogFragment hearingAidPairingDialogFragment = new HearingAidPairingDialogFragment();
        hearingAidPairingDialogFragment.setArguments(bundle);
        return hearingAidPairingDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        int i;
        int i2;
        int i3 = getArguments().getInt("cached_device_side");
        int i4 = R$string.bluetooth_pair_other_ear_dialog_title;
        if (i3 == 0) {
            i = R$string.bluetooth_pair_other_ear_dialog_left_ear_message;
        } else {
            i = R$string.bluetooth_pair_other_ear_dialog_right_ear_message;
        }
        if (i3 == 0) {
            i2 = R$string.bluetooth_pair_other_ear_dialog_right_ear_positive_button;
        } else {
            i2 = R$string.bluetooth_pair_other_ear_dialog_left_ear_positive_button;
        }
        return new AlertDialog.Builder(getActivity()).setTitle(i4).setMessage(i).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(i2, (DialogInterface.OnClickListener) new HearingAidPairingDialogFragment$$ExternalSyntheticLambda0(this)).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        positiveButtonListener();
    }

    private void positiveButtonListener() {
        new SubSettingLauncher(getActivity()).setDestination(BluetoothPairingDetail.class.getName()).setSourceMetricsCategory(2).launch();
    }
}
