package com.android.settings.development;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class BackAnimationPreferenceDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1925;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
    }

    private BackAnimationPreferenceDialog() {
    }

    public static void show(Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (supportFragmentManager.findFragmentByTag("BackAnimationDlg") == null) {
                BackAnimationPreferenceDialog backAnimationPreferenceDialog = new BackAnimationPreferenceDialog();
                backAnimationPreferenceDialog.setTargetFragment(fragment, 0);
                backAnimationPreferenceDialog.show(supportFragmentManager, "BackAnimationDlg");
            }
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(R$string.back_navigation_animation).setMessage(R$string.back_navigation_animation_dialog).setPositiveButton(17039370, this).create();
    }
}
