package com.android.systemui.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.INetworkPolicyManager;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.systemui.C1893R;
import java.sql.Types;

public class NetworkOverLimitActivity extends Activity {
    private static final String TAG = "NetworkOverLimitActivity";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final NetworkTemplate networkTemplate = (NetworkTemplate) getIntent().getParcelableExtra("android.net.NETWORK_TEMPLATE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getLimitedDialogTitleForTemplate(networkTemplate));
        builder.setMessage(C1893R.string.data_usage_disabled_dialog);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(C1893R.string.data_usage_disabled_dialog_enable, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NetworkOverLimitActivity.this.snoozePolicy(networkTemplate);
            }
        });
        AlertDialog create = builder.create();
        create.getWindow().setType(Types.ARRAY);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                NetworkOverLimitActivity.this.finish();
            }
        });
        create.show();
    }

    /* access modifiers changed from: private */
    public void snoozePolicy(NetworkTemplate networkTemplate) {
        try {
            INetworkPolicyManager.Stub.asInterface(ServiceManager.getService("netpolicy")).snoozeLimit(networkTemplate);
        } catch (RemoteException e) {
            Log.w(TAG, "problem snoozing network policy", e);
        }
    }

    private static int getLimitedDialogTitleForTemplate(NetworkTemplate networkTemplate) {
        return networkTemplate.getMatchRule() != 1 ? C1893R.string.data_usage_disabled_dialog_title : C1893R.string.data_usage_disabled_dialog_mobile_title;
    }
}
