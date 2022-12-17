package com.android.settings.biometrics.face;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class FaceEnrollAccessibilityDialog extends InstrumentedDialogFragment {
    private DialogInterface.OnClickListener mPositiveButtonListener;

    public int getMetricsCategory() {
        return 1506;
    }

    public static FaceEnrollAccessibilityDialog newInstance() {
        return new FaceEnrollAccessibilityDialog();
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.mPositiveButtonListener = onClickListener;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int i = R$string.f145x89cc8ccc;
        int i2 = R$string.f146x2371810;
        builder.setMessage(i).setNegativeButton(i2, new FaceEnrollAccessibilityDialog$$ExternalSyntheticLambda0()).setPositiveButton(R$string.f147xf7e2a5d4, new FaceEnrollAccessibilityDialog$$ExternalSyntheticLambda1(this));
        return builder.create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        this.mPositiveButtonListener.onClick(dialogInterface, i);
    }
}
