package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.bluetooth.BroadcastProfile;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothBroadcastPinFragment extends InstrumentedDialogFragment implements RadioGroup.OnCheckedChangeListener {
    private Context mContext;
    private TextView mCurrentPinView;
    AlertDialog mAlertDialog = null;
    private Dialog mDialog = null;
    private Button mOkButton = null;
    private String mCurrentPin = "4308";
    private int mUserSelectedPinConfiguration = -1;
    private List<Integer> mRadioButtonIds = new ArrayList();
    private List<String> mRadioButtonStrings = new ArrayList();

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1390;
    }

    private int getDialogTitle() {
        return R.string.bluetooth_broadcast_pin_configure_dialog;
    }

    private void updatePinConfiguration() {
        Log.d("BluetoothBroadcastPinFragment", "updatePinConfiguration with " + Integer.toString(this.mUserSelectedPinConfiguration));
        if (this.mUserSelectedPinConfiguration == -1) {
            Log.e("BluetoothBroadcastPinFragment", "no pin selected");
            return;
        }
        BroadcastProfile broadcastProfile = (BroadcastProfile) Utils.getLocalBtManager(this.mContext).getProfileManager().getBroadcastProfile();
        int i = this.mUserSelectedPinConfiguration;
        if (i != 0) {
            broadcastProfile.setEncryption(true, i, false);
        } else {
            broadcastProfile.setEncryption(false, i, false);
        }
    }

    @Override // com.android.settings.core.instrumentation.InstrumentedDialogFragment, com.android.settingslib.core.lifecycle.ObservableDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        Log.d("BluetoothBroadcastPinFragment", "onAttach");
        super.onAttach(context);
        this.mContext = context;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        Log.d("BluetoothBroadcastPinFragment", "onCreate");
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        Log.d("BluetoothBroadcastPinFragment", "onActivityCreated");
        super.onActivityCreated(bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Log.d("BluetoothBroadcastPinFragment", "onCreateDialog - enter");
        if (bundle != null) {
            Log.e("BluetoothBroadcastPinFragment", "savedInstanceState != null");
        }
        this.mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(getDialogTitle()).setView(createDialogView()).setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.BluetoothBroadcastPinFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BluetoothBroadcastPinFragment.this.lambda$onCreateDialog$0(dialogInterface, i);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        Log.d("BluetoothBroadcastPinFragment", "onCreateDialog - exit");
        return this.mAlertDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        updatePinConfiguration();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        Log.d("BluetoothBroadcastPinFragment", "onSaveInstanceState");
    }

    private int getRadioButtonGroupId() {
        return R.id.bluetooth_broadcast_pin_config_radio_group;
    }

    @Override // android.widget.RadioGroup.OnCheckedChangeListener
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.d("BluetoothBroadcastPinFragment", "Index changed to " + i);
        int indexOf = this.mRadioButtonIds.indexOf(Integer.valueOf(i));
        Log.d("BluetoothBroadcastPinFragment", "index");
        this.mUserSelectedPinConfiguration = Integer.parseInt(getContext().getResources().getStringArray(R.array.bluetooth_broadcast_pin_config_values)[indexOf]);
        Log.d("BluetoothBroadcastPinFragment", "Selected Pin Configuration " + Integer.toString(this.mUserSelectedPinConfiguration));
    }

    private View createDialogView() {
        Log.d("BluetoothBroadcastPinFragment", "onCreateDialogView - enter");
        View inflate = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.xml.bluetooth_broadcast_pin_config, (ViewGroup) null);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(getRadioButtonGroupId());
        if (radioGroup == null) {
            Log.e("BluetoothBroadcastPinFragment", "Not able to find RadioGroup");
            return null;
        }
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(this);
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_broadcast_pin_unencrypted));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_broadcast_pin_4));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_broadcast_pin_16));
        for (String str : getContext().getResources().getStringArray(R.array.bluetooth_broadcast_pin_config_titles)) {
            this.mRadioButtonStrings.add(str);
        }
        for (int i = 0; i < this.mRadioButtonStrings.size(); i++) {
            RadioButton radioButton = (RadioButton) inflate.findViewById(this.mRadioButtonIds.get(i).intValue());
            if (radioButton == null) {
                Log.e("BluetoothBroadcastPinFragment", "Unable to show dialog by no radio button:" + this.mRadioButtonIds.get(i));
                return null;
            }
            radioButton.setText(this.mRadioButtonStrings.get(i));
            radioButton.setEnabled(true);
        }
        this.mCurrentPinView = (TextView) inflate.findViewById(R.id.bluetooth_broadcast_current_pin);
        Log.d("BluetoothBroadcastPinFragment", "onCreateDialogView - exit");
        return inflate;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableDialogFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Log.d("BluetoothBroadcastPinFragment", "onDestroy");
        this.mAlertDialog = null;
        this.mOkButton = null;
        this.mCurrentPinView = null;
        this.mRadioButtonIds = new ArrayList();
        this.mRadioButtonStrings = new ArrayList();
        this.mUserSelectedPinConfiguration = -1;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableDialogFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        Log.d("BluetoothBroadcastPinFragment", "onResume");
        if (this.mOkButton == null) {
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog != null) {
                Button button = alertDialog.getButton(-1);
                this.mOkButton = button;
                button.setEnabled(true);
                return;
            }
            Log.d("BluetoothBroadcastPinFragment", "onResume: mAlertDialog is null");
        }
    }
}
