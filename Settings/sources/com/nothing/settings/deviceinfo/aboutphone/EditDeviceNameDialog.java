package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class EditDeviceNameDialog extends AlertDialog {
    private Context mContext;
    private AlertDialog mDialog;
    /* access modifiers changed from: private */
    public EditText mEditText;
    private OnDialogDismissListener mOnDialogDismissListener;
    /* access modifiers changed from: private */
    public DeviceBasicInfoPreference mPreference;
    private final EditTextWatcher mTextWatcher = new EditTextWatcher();

    public interface OnDialogDismissListener {
        void OnDialogDismiss(String str, boolean z);
    }

    public EditDeviceNameDialog(Context context, Preference preference, String str, String str2) {
        super(context);
        this.mContext = context;
        initial(context, preference, str, str2);
    }

    private void initial(Context context, Preference preference, String str, String str2) {
        this.mPreference = (DeviceBasicInfoPreference) preference;
        View inflate = View.inflate(context, R$layout.about_edit_device_name_layout, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(R$id.edit_text);
        this.mEditText = editText;
        editText.setHint(str2);
        this.mEditText.setText(str2);
        this.mEditText.setSingleLine(true);
        this.mEditText.setSelectAllOnFocus(true);
        if (!TextUtils.isEmpty(this.mEditText.getText())) {
            EditText editText2 = this.mEditText;
            editText2.setSelection(editText2.getText().length());
        }
        if (this.mPreference.getValidator() != null) {
            this.mEditText.removeTextChangedListener(this.mTextWatcher);
            this.mEditText.addTextChangedListener(this.mTextWatcher);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle((CharSequence) str);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) new EditDeviceNameDialog$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) new EditDeviceNameDialog$$ExternalSyntheticLambda1(this));
        builder.setView(inflate);
        AlertDialog create = builder.create();
        create.setOnShowListener(new EditDeviceNameDialog$$ExternalSyntheticLambda2(this));
        create.setCanceledOnTouchOutside(false);
        create.show();
        this.mDialog = create;
        setPositiveButtonEnabled(!TextUtils.isEmpty(this.mEditText.getText()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initial$0(DialogInterface dialogInterface, int i) {
        handleDialogDismiss(this.mEditText, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initial$1(DialogInterface dialogInterface, int i) {
        handleDialogDismiss(this.mEditText, false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initial$2(DialogInterface dialogInterface) {
        this.mEditText.requestFocus();
    }

    public void handleDialogDismiss(EditText editText, boolean z) {
        OnDialogDismissListener onDialogDismissListener = this.mOnDialogDismissListener;
        if (onDialogDismissListener != null) {
            onDialogDismissListener.OnDialogDismiss(editText.getText().toString(), z);
        }
    }

    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDialogDismissListener = onDialogDismissListener;
    }

    /* access modifiers changed from: private */
    public void setPositiveButtonEnabled(boolean z) {
        this.mDialog.getButton(-1).setEnabled(z);
    }

    public class EditTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private EditTextWatcher() {
        }

        public void afterTextChanged(Editable editable) {
            EditText r2 = EditDeviceNameDialog.this.mEditText;
            if (EditDeviceNameDialog.this.mPreference.getValidator() != null && r2 != null) {
                EditDeviceNameDialog editDeviceNameDialog = EditDeviceNameDialog.this;
                editDeviceNameDialog.setPositiveButtonEnabled(editDeviceNameDialog.mPreference.getValidator().isTextValid(r2.getText().toString().trim()));
            }
        }
    }
}
