package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
/* loaded from: classes.dex */
public class GroupForgetDialogFragment extends InstrumentedDialogFragment {
    private static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private int mGroupId;
    private GroupUtils mGroupUtils;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    public static GroupForgetDialogFragment newInstance(int i) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("groupid", i);
        GroupForgetDialogFragment groupForgetDialogFragment = new GroupForgetDialogFragment();
        groupForgetDialogFragment.setArguments(bundle);
        return groupForgetDialogFragment;
    }

    String getGroupTitle() {
        int i = getArguments().getInt("groupid");
        this.mGroupId = i;
        return this.mGroupUtils.getGroupTitle(i);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.GroupForgetDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                GroupForgetDialogFragment.this.lambda$onCreateDialog$0(dialogInterface, i);
            }
        };
        Context context = getContext();
        this.mGroupUtils = new GroupUtils(context);
        AlertDialog create = new AlertDialog.Builder(context).setPositiveButton(R.string.groupaudio_unpair_dialog_forget_confirm_button, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        create.setTitle(R.string.groupaudio_unpair_dialog_title);
        create.setMessage(context.getString(R.string.groupaudio_unpair_dialog_body, getGroupTitle()));
        return create;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        forget();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void forget() {
        this.mGroupUtils.forgetGroup(this.mGroupId);
    }
}
