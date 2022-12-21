package com.android.systemui.usb.p015tv;

import android.view.View;
import com.android.systemui.C1893R;

/* renamed from: com.android.systemui.usb.tv.TvUsbConfirmActivity */
public class TvUsbConfirmActivity extends TvUsbDialogActivity {
    private static final String TAG = "TvUsbConfirmActivity";

    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        int i;
        super.onResume();
        if (this.mDialogHelper.isUsbDevice()) {
            i = this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission() ? C1893R.string.usb_device_confirm_prompt_warn : C1893R.string.usb_device_confirm_prompt;
        } else {
            i = C1893R.string.usb_accessory_confirm_prompt;
        }
        initUI(this.mDialogHelper.getAppName(), getString(i, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}));
    }

    /* access modifiers changed from: package-private */
    public void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        this.mDialogHelper.confirmDialogStartActivity();
        finish();
    }
}
