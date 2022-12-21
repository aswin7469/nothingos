package com.android.systemui.usb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.systemui.C1893R;

abstract class UsbDialogActivity extends AlertActivity implements DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "UsbDialogActivity";
    private CheckBox mAlwaysUse;
    private TextView mClearDefaultHint;
    UsbDialogHelper mDialogHelper;

    /* access modifiers changed from: package-private */
    public abstract void onConfirm();

    UsbDialogActivity() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        UsbDialogActivity.super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        try {
            this.mDialogHelper = new UsbDialogHelper(getApplicationContext(), getIntent());
        } catch (IllegalStateException e) {
            Log.e(TAG, "unable to initialize", e);
            finish();
        }
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [com.android.internal.app.AlertActivity, com.android.systemui.usb.UsbDialogActivity, android.app.Activity] */
    /* access modifiers changed from: protected */
    public void onResume() {
        UsbDialogActivity.super.onResume();
        this.mDialogHelper.registerUsbDisconnectedReceiver(this);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [com.android.internal.app.AlertActivity, com.android.systemui.usb.UsbDialogActivity, android.app.Activity] */
    /* access modifiers changed from: protected */
    public void onPause() {
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        if (usbDialogHelper != null) {
            usbDialogHelper.unregisterUsbDisconnectedReceiver(this);
        }
        UsbDialogActivity.super.onPause();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            onConfirm();
        } else {
            finish();
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        TextView textView = this.mClearDefaultHint;
        if (textView != null) {
            if (z) {
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAlertParams(String str, String str2) {
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = str;
        alertParams.mMessage = str2;
        alertParams.mPositiveButtonText = getString(17039370);
        alertParams.mNegativeButtonText = getString(17039360);
        alertParams.mPositiveButtonListener = this;
        alertParams.mNegativeButtonListener = this;
    }

    /* access modifiers changed from: package-private */
    public void addAlwaysUseCheckbox() {
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mView = ((LayoutInflater) getSystemService(LayoutInflater.class)).inflate(17367093, (ViewGroup) null);
        this.mAlwaysUse = (CheckBox) alertParams.mView.findViewById(16908774);
        if (this.mDialogHelper.isUsbAccessory()) {
            this.mAlwaysUse.setText(getString(C1893R.string.always_use_accessory, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}));
        } else {
            this.mAlwaysUse.setText(getString(C1893R.string.always_use_device, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}));
        }
        this.mAlwaysUse.setOnCheckedChangeListener(this);
        TextView textView = (TextView) alertParams.mView.findViewById(16908878);
        this.mClearDefaultHint = textView;
        textView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public boolean isAlwaysUseChecked() {
        CheckBox checkBox = this.mAlwaysUse;
        return checkBox != null && checkBox.isChecked();
    }
}
