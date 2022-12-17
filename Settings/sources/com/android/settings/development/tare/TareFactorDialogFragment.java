package com.android.settings.development.tare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.android.settings.R$array;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.Utils;

public class TareFactorDialogFragment extends DialogFragment {
    private final String mFactorKey;
    private final int mFactorPolicy;
    private final String mFactorTitle;
    private final long mFactorValue;
    /* access modifiers changed from: private */
    public EditText mFactorValueView;
    private final TareFactorController mTareFactorController;
    /* access modifiers changed from: private */
    public Spinner mUnitSpinner;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
    }

    public TareFactorDialogFragment(String str, String str2, long j, int i, TareFactorController tareFactorController) {
        this.mFactorTitle = str;
        this.mFactorKey = str2;
        this.mFactorValue = j;
        this.mFactorPolicy = i;
        this.mTareFactorController = tareFactorController;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(this.mFactorTitle).setView(createDialogView()).setPositiveButton(R$string.tare_dialog_confirm_button_title, new TareFactorDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(17039360, new TareFactorDialogFragment$$ExternalSyntheticLambda1()).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        String obj = this.mFactorValueView.getText().toString();
        long j = this.mFactorValue;
        try {
            j = Long.parseLong(obj);
            if (this.mUnitSpinner.getSelectedItemPosition() == 0) {
                j *= 1000000000;
            }
        } catch (NumberFormatException e) {
            Log.e("TareDialogFragment", "Error parsing '" + obj + "'. Using " + this.mFactorValue + " instead", e);
        }
        this.mTareFactorController.updateValue(this.mFactorKey, j, this.mFactorPolicy);
    }

    private View createDialogView() {
        View inflate = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R$layout.dialog_edittext_dropdown, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(R$id.edittext);
        this.mFactorValueView = editText;
        editText.setInputType(2);
        this.mUnitSpinner = (Spinner) inflate.findViewById(R$id.spinner);
        this.mUnitSpinner.setAdapter(new ArrayAdapter(getActivity(), 17367048, getResources().getStringArray(R$array.tare_units)));
        long j = this.mFactorValue;
        int i = 0;
        if (j % 1000000000 == 0) {
            this.mFactorValueView.setText(String.format("%d", new Object[]{Long.valueOf(j / 1000000000)}));
        } else {
            this.mFactorValueView.setText(String.format("%d", new Object[]{Long.valueOf(j)}));
            i = 1;
        }
        this.mUnitSpinner.setSelection(i);
        this.mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(i) {
            private int mSelectedPosition;
            final /* synthetic */ int val$unitIdx;

            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            {
                this.val$unitIdx = r2;
                this.mSelectedPosition = r2;
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (this.mSelectedPosition != i) {
                    this.mSelectedPosition = i;
                    String obj = TareFactorDialogFragment.this.mFactorValueView.getText().toString();
                    try {
                        long parseLong = Long.parseLong(obj);
                        TareFactorDialogFragment.this.mFactorValueView.setText(String.format("%d", new Object[]{Long.valueOf(TareFactorDialogFragment.this.mUnitSpinner.getSelectedItemPosition() == 0 ? parseLong / 1000000000 : parseLong * 1000000000)}));
                    } catch (NumberFormatException e) {
                        Log.e("TareDialogFragment", "Error parsing '" + obj + "'", e);
                    }
                }
            }
        });
        Utils.setEditTextCursorPosition(this.mFactorValueView);
        return inflate;
    }
}
