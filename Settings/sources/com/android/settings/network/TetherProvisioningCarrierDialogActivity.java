package com.android.settings.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.settings.R$string;

public class TetherProvisioningCarrierDialogActivity extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new AlertDialog.Builder(this).setTitle(R$string.wifi_tether_carrier_unsupport_dialog_title).setMessage(R$string.wifi_tether_carrier_unsupport_dialog_content).setCancelable(false).setPositiveButton(17039379, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                TetherProvisioningCarrierDialogActivity.this.finish();
            }
        }).show();
    }
}
