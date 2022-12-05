package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
/* loaded from: classes.dex */
public class GroupBluetoothProfileSwitchConfirmDialog extends InstrumentedDialogFragment {
    private int mGroupId = -1;
    private GroupUtils mGroupUtils;
    private BluetoothDetailsProfilesController mProfileController;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
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

    String getGroupTitle() {
        int i = getArguments().getInt("group_id");
        this.mGroupId = i;
        return this.mGroupUtils.getGroupTitle(i);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                GroupBluetoothProfileSwitchConfirmDialog.this.lambda$onCreateDialog$0(dialogInterface, i);
            }
        };
        DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothProfileSwitchConfirmDialog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                GroupBluetoothProfileSwitchConfirmDialog.this.lambda$onCreateDialog$1(dialogInterface, i);
            }
        };
        Context context = getContext();
        this.mGroupUtils = new GroupUtils(context);
        AlertDialog create = new AlertDialog.Builder(context).setPositiveButton(R.string.group_confirm_dialog_apply_button, onClickListener).setNegativeButton(17039360, onClickListener2).create();
        create.setTitle(R.string.group_apply_changes_dialog_title);
        create.setMessage(context.getString(R.string.group_confirm_dialog_body, getGroupTitle()));
        return create;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        onPositiveButtonClicked();
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        onNegativeButtonClicked();
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPairingController(BluetoothDetailsProfilesController bluetoothDetailsProfilesController) {
        if (isPairingControllerSet()) {
            throw new IllegalStateException("The controller can only be set once. Forcibly replacing it will lead to undefined behavior");
        }
        this.mProfileController = bluetoothDetailsProfilesController;
    }

    boolean isPairingControllerSet() {
        return this.mProfileController != null;
    }

    private void onPositiveButtonClicked() {
        this.mProfileController.onDialogPositiveClick();
    }

    private void onNegativeButtonClicked() {
        this.mProfileController.onDialogNegativeClick();
    }
}
