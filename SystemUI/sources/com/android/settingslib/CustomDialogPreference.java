package com.android.settingslib;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragment;

@Deprecated
public class CustomDialogPreference extends DialogPreference {
    private CustomPreferenceDialogFragment mFragment;
    private DialogInterface.OnShowListener mOnShowListener;

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
    }

    /* access modifiers changed from: protected */
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
    }

    public CustomDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public CustomDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomDialogPreference(Context context) {
        super(context);
    }

    public boolean isDialogOpen() {
        return getDialog() != null && getDialog().isShowing();
    }

    public Dialog getDialog() {
        CustomPreferenceDialogFragment customPreferenceDialogFragment = this.mFragment;
        if (customPreferenceDialogFragment != null) {
            return customPreferenceDialogFragment.getDialog();
        }
        return null;
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;
    }

    /* access modifiers changed from: private */
    public void setFragment(CustomPreferenceDialogFragment customPreferenceDialogFragment) {
        this.mFragment = customPreferenceDialogFragment;
    }

    /* access modifiers changed from: private */
    public DialogInterface.OnShowListener getOnShowListener() {
        return this.mOnShowListener;
    }

    public static class CustomPreferenceDialogFragment extends PreferenceDialogFragment {
        public static CustomPreferenceDialogFragment newInstance(String str) {
            CustomPreferenceDialogFragment customPreferenceDialogFragment = new CustomPreferenceDialogFragment();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", str);
            customPreferenceDialogFragment.setArguments(bundle);
            return customPreferenceDialogFragment;
        }

        private CustomDialogPreference getCustomizablePreference() {
            return (CustomDialogPreference) getPreference();
        }

        /* access modifiers changed from: protected */
        public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            super.onPrepareDialogBuilder(builder);
            getCustomizablePreference().setFragment(this);
            getCustomizablePreference().onPrepareDialogBuilder(builder, this);
        }

        public void onDialogClosed(boolean z) {
            getCustomizablePreference().onDialogClosed(z);
        }

        /* access modifiers changed from: protected */
        public void onBindDialogView(View view) {
            super.onBindDialogView(view);
            getCustomizablePreference().onBindDialogView(view);
        }

        public Dialog onCreateDialog(Bundle bundle) {
            Dialog onCreateDialog = super.onCreateDialog(bundle);
            onCreateDialog.setOnShowListener(getCustomizablePreference().getOnShowListener());
            return onCreateDialog;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            super.onClick(dialogInterface, i);
            getCustomizablePreference().onClick(dialogInterface, i);
        }
    }
}
