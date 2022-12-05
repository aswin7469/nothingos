package com.nt.settings.about;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NtAboutPhoneEditTextDialog extends AlertDialog {
    private static final String TAG = NtAboutPhoneEditTextDialog.class.getSimpleName();
    private Context mContext;
    private AlertDialog mDialog;
    private EditText mEditText;
    private OnDialogDismissListener mOnDialogDismissListener;
    private NtAboutPhoneBasicInfoPreference mPreference;
    private final EditTextWatcher mTextWatcher = new EditTextWatcher();

    /* loaded from: classes2.dex */
    public interface OnDialogDismissListener {
        void OnDialogDismiss(String str, boolean z);
    }

    public NtAboutPhoneEditTextDialog(Context context, Preference preference, String str, String str2, String str3, int i) {
        super(context);
        init(context, preference, false, str, str2, str3, i);
    }

    private void init(Context context, Preference preference, boolean z, String str, String str2, String str3, int i) {
        EditText editText;
        this.mContext = context;
        this.mPreference = (NtAboutPhoneBasicInfoPreference) preference;
        View inflate = View.inflate(context, R.layout.nt_edittext_dialog, null);
        EditText editText2 = (EditText) inflate.findViewById(R.id.nt_name);
        this.mEditText = editText2;
        editText2.setHint(str2);
        this.mEditText.setText(str3);
        boolean z2 = true;
        this.mEditText.setSingleLine(true);
        this.mEditText.setSelectAllOnFocus(true);
        if (i != -1) {
            this.mEditText.setInputType(i);
        }
        EditText editText3 = this.mEditText;
        if (editText3 != null && !TextUtils.isEmpty(editText3.getText())) {
            EditText editText4 = this.mEditText;
            editText4.setSelection(editText4.getText().length());
        }
        if (this.mPreference.getValidator() != null && (editText = this.mEditText) != null) {
            editText.removeTextChangedListener(this.mTextWatcher);
            this.mEditText.addTextChangedListener(this.mTextWatcher);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneEditTextDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                NtAboutPhoneEditTextDialog ntAboutPhoneEditTextDialog = NtAboutPhoneEditTextDialog.this;
                ntAboutPhoneEditTextDialog.handleDialogDismiss(ntAboutPhoneEditTextDialog.mEditText, true);
            }
        });
        builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneEditTextDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                NtAboutPhoneEditTextDialog ntAboutPhoneEditTextDialog = NtAboutPhoneEditTextDialog.this;
                ntAboutPhoneEditTextDialog.handleDialogDismiss(ntAboutPhoneEditTextDialog.mEditText, false);
            }
        });
        builder.setView(inflate);
        AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.nt.settings.about.NtAboutPhoneEditTextDialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                NtAboutPhoneEditTextDialog.this.mEditText.requestFocus();
            }
        });
        create.setCanceledOnTouchOutside(false);
        create.show();
        this.mDialog = create;
        if (this.mEditText.getText().toString().trim().length() == 0) {
            z2 = false;
        }
        setPositiveButtonEnabled(z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDialogDismiss(EditText editText, boolean z) {
        OnDialogDismissListener onDialogDismissListener = this.mOnDialogDismissListener;
        if (onDialogDismissListener != null) {
            onDialogDismissListener.OnDialogDismiss(editText.getText().toString(), z);
        }
    }

    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDialogDismissListener = onDialogDismissListener;
    }

    private void setPositiveButtonEnabled(boolean z) {
        this.mDialog.findViewById(16908313).setEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class EditTextWatcher implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private EditTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            EditText editText = NtAboutPhoneEditTextDialog.this.mEditText;
            if (NtAboutPhoneEditTextDialog.this.mPreference.getValidator() == null || editText == null) {
                return;
            }
            NtAboutPhoneEditTextDialog.this.mDialog.getButton(-1).setEnabled(NtAboutPhoneEditTextDialog.this.mPreference.getValidator().isTextValid(editText.getText().toString().trim()));
        }
    }
}
