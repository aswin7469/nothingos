package com.android.settings.security;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
/* loaded from: classes.dex */
public class UnificationConfirmationDialog extends InstrumentedDialogFragment {
    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 532;
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        int i;
        int i2;
        final SecuritySettings securitySettings = (SecuritySettings) getParentFragment();
        boolean z = getArguments().getBoolean("compliant");
        AlertDialog.Builder title = new AlertDialog.Builder(getActivity()).setTitle(R.string.lock_settings_profile_unification_dialog_title);
        if (z) {
            i = R.string.lock_settings_profile_unification_dialog_body;
        } else {
            i = R.string.lock_settings_profile_unification_dialog_uncompliant_body;
        }
        AlertDialog.Builder message = title.setMessage(i);
        if (z) {
            i2 = R.string.lock_settings_profile_unification_dialog_confirm;
        } else {
            i2 = R.string.lock_settings_profile_unification_dialog_uncompliant_confirm;
        }
        return message.setPositiveButton(i2, new DialogInterface.OnClickListener() { // from class: com.android.settings.security.UnificationConfirmationDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                SecuritySettings.this.startUnification();
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((SecuritySettings) getParentFragment()).updateUnificationPreference();
    }
}
