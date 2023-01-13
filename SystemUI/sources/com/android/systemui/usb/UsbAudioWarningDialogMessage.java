package com.android.systemui.usb;

import android.util.Log;
import com.android.systemui.C1894R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class UsbAudioWarningDialogMessage {
    private static final String TAG = "UsbAudioWarningDialogMessage";
    public static final int TYPE_CONFIRM = 1;
    public static final int TYPE_PERMISSION = 0;
    private UsbDialogHelper mDialogHelper;
    private int mDialogType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogType {
    }

    public void init(int i, UsbDialogHelper usbDialogHelper) {
        this.mDialogType = i;
        this.mDialogHelper = usbDialogHelper;
    }

    /* access modifiers changed from: package-private */
    public boolean hasRecordPermission() {
        return this.mDialogHelper.packageHasAudioRecordingPermission();
    }

    /* access modifiers changed from: package-private */
    public boolean isUsbAudioDevice() {
        return this.mDialogHelper.isUsbDevice() && (this.mDialogHelper.deviceHasAudioCapture() || this.mDialogHelper.deviceHasAudioPlayback());
    }

    /* access modifiers changed from: package-private */
    public boolean hasAudioPlayback() {
        return this.mDialogHelper.deviceHasAudioPlayback();
    }

    /* access modifiers changed from: package-private */
    public boolean hasAudioCapture() {
        return this.mDialogHelper.deviceHasAudioCapture();
    }

    public int getMessageId() {
        if (!this.mDialogHelper.isUsbDevice()) {
            return getUsbAccessoryPromptId();
        }
        if (hasRecordPermission() && isUsbAudioDevice()) {
            return C1894R.string.usb_audio_device_prompt;
        }
        if (!hasRecordPermission() && isUsbAudioDevice() && hasAudioPlayback() && !hasAudioCapture()) {
            return C1894R.string.usb_audio_device_prompt;
        }
        if (!hasRecordPermission() && isUsbAudioDevice() && hasAudioCapture()) {
            return C1894R.string.usb_audio_device_prompt_warn;
        }
        Log.w(TAG, "Only shows title with empty content description!");
        return 0;
    }

    public int getPromptTitleId() {
        return this.mDialogType == 0 ? C1894R.string.usb_audio_device_permission_prompt_title : C1894R.string.usb_audio_device_confirm_prompt_title;
    }

    public int getUsbAccessoryPromptId() {
        return this.mDialogType == 0 ? C1894R.string.usb_accessory_permission_prompt : C1894R.string.usb_accessory_confirm_prompt;
    }
}
