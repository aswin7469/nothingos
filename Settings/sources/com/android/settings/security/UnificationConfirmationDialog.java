package com.android.settings.security;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class UnificationConfirmationDialog extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 532;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        int i;
        int i2;
        SecuritySettings securitySettings = (SecuritySettings) getParentFragment();
        boolean z = getArguments().getBoolean("compliant");
        String str = z ? "Settings.WORK_PROFILE_UNIFY_LOCKS_DETAIL" : "Settings.WORK_PROFILE_UNIFY_LOCKS_NONCOMPLIANT";
        if (z) {
            i = R$string.lock_settings_profile_unification_dialog_body;
        } else {
            i = R$string.lock_settings_profile_unification_dialog_uncompliant_body;
        }
        AlertDialog.Builder message = new AlertDialog.Builder(getActivity()).setTitle(R$string.lock_settings_profile_unification_dialog_title).setMessage((CharSequence) ((DevicePolicyManager) getContext().getSystemService(DevicePolicyManager.class)).getResources().getString(str, new UnificationConfirmationDialog$$ExternalSyntheticLambda0(this, i)));
        if (z) {
            i2 = R$string.lock_settings_profile_unification_dialog_confirm;
        } else {
            i2 = R$string.lock_settings_profile_unification_dialog_uncompliant_confirm;
        }
        return message.setPositiveButton(i2, (DialogInterface.OnClickListener) new UnificationConfirmationDialog$$ExternalSyntheticLambda1(securitySettings)).setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreateDialog$0(int i) {
        return getString(i);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((SecuritySettings) getParentFragment()).updateUnificationPreference();
    }
}
