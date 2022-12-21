package com.android.systemui.usb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;
import javax.inject.Inject;

public class UsbPermissionActivity extends UsbDialogActivity {
    private boolean mPermissionGranted = false;
    private UsbAudioWarningDialogMessage mUsbPermissionMessageHandler;

    public /* bridge */ /* synthetic */ void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        super.onCheckedChanged(compoundButton, z);
    }

    public /* bridge */ /* synthetic */ void onClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
    }

    @Inject
    public UsbPermissionActivity(UsbAudioWarningDialogMessage usbAudioWarningDialogMessage) {
        this.mUsbPermissionMessageHandler = usbAudioWarningDialogMessage;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUsbPermissionMessageHandler.init(0, this.mDialogHelper);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        boolean z = this.mDialogHelper.isUsbDevice() && this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission();
        String string = getString(this.mUsbPermissionMessageHandler.getPromptTitleId(), new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()});
        int messageId = this.mUsbPermissionMessageHandler.getMessageId();
        setAlertParams(string, messageId != 0 ? getString(messageId, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}) : null);
        if (!z && this.mDialogHelper.canBeDefault()) {
            addAlwaysUseCheckbox();
        }
        setupAlert();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (isFinishing()) {
            this.mDialogHelper.sendPermissionDialogResponse(this.mPermissionGranted);
        }
        super.onPause();
    }

    /* access modifiers changed from: package-private */
    public void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        if (isAlwaysUseChecked()) {
            this.mDialogHelper.setDefaultPackage();
        }
        this.mPermissionGranted = true;
        finish();
    }
}
