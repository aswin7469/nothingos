package com.android.settings.network.telephony;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.android.settings.R$drawable;

public class ProgressDialogFragment extends DialogFragment {
    private OnDismissCallback mDismissCallback;

    public interface OnDismissCallback {
        void onProgressDialogDismiss();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onCreateDialog$0(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return 4 == i;
    }

    public static void show(FragmentManager fragmentManager, String str, OnDismissCallback onDismissCallback) {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag("ProgressDialogFragment");
        if (progressDialogFragment == null || !TextUtils.equals(progressDialogFragment.getArguments().getString("title"), str)) {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            if (progressDialogFragment != null) {
                beginTransaction.remove(progressDialogFragment);
            }
            ProgressDialogFragment progressDialogFragment2 = new ProgressDialogFragment();
            progressDialogFragment2.setDismissCallback(onDismissCallback);
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            progressDialogFragment2.setArguments(bundle);
            progressDialogFragment2.show(beginTransaction, "ProgressDialogFragment");
        }
    }

    public static void dismiss(FragmentManager fragmentManager) {
        DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag("ProgressDialogFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawableResource(R$drawable.sim_progress_dialog_rounded_bg);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getArguments().getString("title"));
        progressDialog.setOnKeyListener(new ProgressDialogFragment$$ExternalSyntheticLambda0());
        return progressDialog;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        OnDismissCallback onDismissCallback = this.mDismissCallback;
        if (onDismissCallback != null) {
            onDismissCallback.onProgressDialogDismiss();
        }
    }

    private void setDismissCallback(OnDismissCallback onDismissCallback) {
        this.mDismissCallback = onDismissCallback;
    }
}
