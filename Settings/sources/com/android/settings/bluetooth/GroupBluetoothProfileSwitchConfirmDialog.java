package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class GroupBluetoothProfileSwitchConfirmDialog extends InstrumentedDialogFragment {
    private int mGroupId = -1;
    private GroupUtils mGroupUtils;
    private BluetoothDetailsProfilesController mProfileController;

    public int getMetricsCategory() {
        return 0;
    }

    public static GroupBluetoothProfileSwitchConfirmDialog newInstance(int i) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("group_id", i);
        GroupBluetoothProfileSwitchConfirmDialog groupBluetoothProfileSwitchConfirmDialog = new GroupBluetoothProfileSwitchConfirmDialog();
        groupBluetoothProfileSwitchConfirmDialog.setArguments(bundle);
        return groupBluetoothProfileSwitchConfirmDialog;
    }

    /* access modifiers changed from: package-private */
    public String getGroupTitle() {
        int i = getArguments().getInt("group_id");
        this.mGroupId = i;
        return this.mGroupUtils.getGroupTitle(i);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        C0803x87ceaba9 groupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda0 = new C0803x87ceaba9(this);
        C0804x87ceabaa groupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda1 = new C0804x87ceabaa(this);
        Context context = getContext();
        this.mGroupUtils = new GroupUtils(context);
        AlertDialog create = new AlertDialog.Builder(context).setPositiveButton(R$string.group_confirm_dialog_apply_button, (DialogInterface.OnClickListener) groupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda0).setNegativeButton(17039360, (DialogInterface.OnClickListener) groupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda1).create();
        create.setTitle(R$string.group_apply_changes_dialog_title);
        create.setMessage(context.getString(R$string.group_confirm_dialog_body, new Object[]{getGroupTitle()}));
        return create;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        onPositiveButtonClicked();
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        onNegativeButtonClicked();
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void setPairingController(BluetoothDetailsProfilesController bluetoothDetailsProfilesController) {
        if (!isPairingControllerSet()) {
            this.mProfileController = bluetoothDetailsProfilesController;
            return;
        }
        throw new IllegalStateException("The controller can only be set once. Forcibly replacing it will lead to undefined behavior");
    }

    /* access modifiers changed from: package-private */
    public boolean isPairingControllerSet() {
        return this.mProfileController != null;
    }

    private void onPositiveButtonClicked() {
        this.mProfileController.onDialogPositiveClick();
    }

    private void onNegativeButtonClicked() {
        this.mProfileController.onDialogNegativeClick();
    }
}
