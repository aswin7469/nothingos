package com.android.settings.applications.appinfo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class ButtonActionDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    int mId;

    public interface AppButtonsDialogListener {
        void handleDialogClick(int i);
    }

    public int getMetricsCategory() {
        return 558;
    }

    public static ButtonActionDialogFragment newInstance(int i) {
        ButtonActionDialogFragment buttonActionDialogFragment = new ButtonActionDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt("id", i);
        buttonActionDialogFragment.setArguments(bundle);
        return buttonActionDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        int i = getArguments().getInt("id");
        this.mId = i;
        AlertDialog createDialog = createDialog(i);
        if (createDialog != null) {
            return createDialog;
        }
        throw new IllegalArgumentException("unknown id " + this.mId);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mId == 2) {
            dialogInterface.dismiss();
        }
        ((AppButtonsDialogListener) getTargetFragment()).handleDialogClick(this.mId);
    }

    private AlertDialog createDialog(int i) {
        Context context = getContext();
        if (i == 0 || i == 1) {
            return new AlertDialog.Builder(context).setMessage(R$string.app_disable_dlg_text).setPositiveButton(R$string.app_disable_dlg_positive, (DialogInterface.OnClickListener) this).setNegativeButton(R$string.dlg_cancel, (DialogInterface.OnClickListener) null).create();
        }
        if (i != 2) {
            return null;
        }
        return new AlertDialog.Builder(context).setTitle(R$string.force_stop_dlg_title).setMessage(R$string.force_stop_dlg_text).setPositiveButton(R$string.dlg_ok, (DialogInterface.OnClickListener) this).setNegativeButton(R$string.dlg_cancel, (DialogInterface.OnClickListener) null).create();
    }
}
