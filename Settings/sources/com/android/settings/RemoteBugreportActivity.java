package com.android.settings;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

public class RemoteBugreportActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        int intExtra = getIntent().getIntExtra("android.app.extra.bugreport_notification_type", -1);
        if (intExtra == 2) {
            new AlertDialog.Builder(this).setMessage((CharSequence) devicePolicyManager.getResources().getString("Settings.SHARING_REMOTE_BUGREPORT_MESSAGE", new RemoteBugreportActivity$$ExternalSyntheticLambda0(this))).setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    RemoteBugreportActivity.this.finish();
                }
            }).setNegativeButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RemoteBugreportActivity.this.finish();
                }
            }).create().show();
        } else if (intExtra == 1 || intExtra == 3) {
            if (intExtra == 1) {
                i = R$string.share_remote_bugreport_dialog_message;
            } else {
                i = R$string.share_remote_bugreport_dialog_message_finished;
            }
            new AlertDialog.Builder(this).setTitle((CharSequence) devicePolicyManager.getResources().getString("Settings.SHARE_REMOTE_BUGREPORT_DIALOG_TITLE", new RemoteBugreportActivity$$ExternalSyntheticLambda1(this))).setMessage((CharSequence) devicePolicyManager.getResources().getString(intExtra == 1 ? "Settings.SHARE_REMOTE_BUGREPORT_NOT_FINISHED_REQUEST_CONSENT" : "Settings.SHARE_REMOTE_BUGREPORT_FINISHED_REQUEST_CONSENT", new RemoteBugreportActivity$$ExternalSyntheticLambda2(this, i))).setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    RemoteBugreportActivity.this.finish();
                }
            }).setNegativeButton(R$string.decline_remote_bugreport_action, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RemoteBugreportActivity.this.sendBroadcastAsUser(new Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED"), UserHandle.SYSTEM, "android.permission.DUMP");
                    RemoteBugreportActivity.this.finish();
                }
            }).setPositiveButton(R$string.share_remote_bugreport_action, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RemoteBugreportActivity.this.sendBroadcastAsUser(new Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED"), UserHandle.SYSTEM, "android.permission.DUMP");
                    RemoteBugreportActivity.this.finish();
                }
            }).create().show();
        } else {
            Log.e("RemoteBugreportActivity", "Incorrect dialog type, no dialog shown. Received: " + intExtra);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$0() {
        return getString(R$string.sharing_remote_bugreport_dialog_message);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$1() {
        return getString(R$string.share_remote_bugreport_dialog_title);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$2(int i) {
        return getString(i);
    }
}
