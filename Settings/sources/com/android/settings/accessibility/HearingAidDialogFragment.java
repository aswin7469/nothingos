package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.bluetooth.BluetoothPairingDetail;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class HearingAidDialogFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 1512;
    }

    public static HearingAidDialogFragment newInstance() {
        return new HearingAidDialogFragment();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(R$string.accessibility_hearingaid_pair_instructions_title).setMessage(R$string.accessibility_hearingaid_pair_instructions_message).setPositiveButton(R$string.accessibility_hearingaid_instruction_continue_button, (DialogInterface.OnClickListener) new HearingAidDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        launchBluetoothAddDeviceSetting();
    }

    private void launchBluetoothAddDeviceSetting() {
        new SubSettingLauncher(getActivity()).setDestination(BluetoothPairingDetail.class.getName()).setSourceMetricsCategory(2).launch();
    }
}
