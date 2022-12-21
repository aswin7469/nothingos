package com.android.settingslib;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.lang.UProperty;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import androidx.preference.EditTextPreference;
import androidx.preference.EditTextPreferenceDialogFragment;

@Deprecated
public class CustomEditTextPreference extends EditTextPreference {
    private CustomPreferenceDialogFragment mFragment;

    /* access modifiers changed from: protected */
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomEditTextPreference(Context context) {
        super(context);
    }

    public EditText getEditText() {
        Dialog dialog;
        CustomPreferenceDialogFragment customPreferenceDialogFragment = this.mFragment;
        if (customPreferenceDialogFragment == null || (dialog = customPreferenceDialogFragment.getDialog()) == null) {
            return null;
        }
        return (EditText) dialog.findViewById(16908291);
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

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        EditText editText = (EditText) view.findViewById(16908291);
        if (editText != null) {
            editText.setInputType(UProperty.BIDI_MIRRORING_GLYPH);
            editText.requestFocus();
        }
    }

    /* access modifiers changed from: private */
    public void setFragment(CustomPreferenceDialogFragment customPreferenceDialogFragment) {
        this.mFragment = customPreferenceDialogFragment;
    }

    public static class CustomPreferenceDialogFragment extends EditTextPreferenceDialogFragment {
        public static CustomPreferenceDialogFragment newInstance(String str) {
            CustomPreferenceDialogFragment customPreferenceDialogFragment = new CustomPreferenceDialogFragment();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", str);
            customPreferenceDialogFragment.setArguments(bundle);
            return customPreferenceDialogFragment;
        }

        private CustomEditTextPreference getCustomizablePreference() {
            return (CustomEditTextPreference) getPreference();
        }

        /* access modifiers changed from: protected */
        public void onBindDialogView(View view) {
            super.onBindDialogView(view);
            getCustomizablePreference().onBindDialogView(view);
        }

        /* access modifiers changed from: protected */
        public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            super.onPrepareDialogBuilder(builder);
            getCustomizablePreference().setFragment(this);
            getCustomizablePreference().onPrepareDialogBuilder(builder, this);
        }

        public void onDialogClosed(boolean z) {
            super.onDialogClosed(z);
            getCustomizablePreference().onDialogClosed(z);
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            super.onClick(dialogInterface, i);
            getCustomizablePreference().onClick(dialogInterface, i);
        }
    }
}
