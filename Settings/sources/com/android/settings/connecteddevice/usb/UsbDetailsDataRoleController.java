package com.android.settings.connecteddevice.usb;

import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settingslib.widget.RadioButtonPreference;
/* loaded from: classes.dex */
public class UsbDetailsDataRoleController extends UsbDetailsController implements RadioButtonPreference.OnClickListener {
    private RadioButtonPreference mDevicePref;
    private final Runnable mFailureCallback = new Runnable() { // from class: com.android.settings.connecteddevice.usb.UsbDetailsDataRoleController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            UsbDetailsDataRoleController.this.lambda$new$0();
        }
    };
    private RadioButtonPreference mHostPref;
    private RadioButtonPreference mNextRolePref;
    private PreferenceCategory mPreferenceCategory;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "usb_details_data_role";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        RadioButtonPreference radioButtonPreference = this.mNextRolePref;
        if (radioButtonPreference != null) {
            radioButtonPreference.setSummary(R.string.usb_switching_failed);
            this.mNextRolePref = null;
        }
    }

    public UsbDetailsDataRoleController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mHostPref = makeRadioPreference(UsbBackend.dataRoleToString(1), R.string.usb_control_host);
        this.mDevicePref = makeRadioPreference(UsbBackend.dataRoleToString(2), R.string.usb_control_device);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.connecteddevice.usb.UsbDetailsController
    public void refresh(boolean z, long j, int i, int i2) {
        if (i2 == 2) {
            this.mDevicePref.setChecked(true);
            this.mHostPref.setChecked(false);
            this.mPreferenceCategory.setEnabled(true);
        } else if (i2 == 1) {
            this.mDevicePref.setChecked(false);
            this.mHostPref.setChecked(true);
            this.mPreferenceCategory.setEnabled(true);
        } else if (!z || i2 == 0) {
            this.mPreferenceCategory.setEnabled(false);
            if (this.mNextRolePref == null) {
                this.mHostPref.setSummary("");
                this.mDevicePref.setSummary("");
            }
        }
        RadioButtonPreference radioButtonPreference = this.mNextRolePref;
        if (radioButtonPreference == null || i2 == 0) {
            return;
        }
        if (UsbBackend.dataRoleFromString(radioButtonPreference.getKey()) == i2) {
            this.mNextRolePref.setSummary("");
        } else {
            this.mNextRolePref.setSummary(R.string.usb_switching_failed);
        }
        this.mNextRolePref = null;
        this.mHandler.removeCallbacks(this.mFailureCallback);
    }

    @Override // com.android.settingslib.widget.RadioButtonPreference.OnClickListener
    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        int dataRoleFromString = UsbBackend.dataRoleFromString(radioButtonPreference.getKey());
        if (dataRoleFromString == this.mUsbBackend.getDataRole() || this.mNextRolePref != null || Utils.isMonkeyRunning()) {
            return;
        }
        this.mUsbBackend.setDataRole(dataRoleFromString);
        this.mNextRolePref = radioButtonPreference;
        radioButtonPreference.setSummary(R.string.usb_switching);
        this.mHandler.postDelayed(this.mFailureCallback, this.mUsbBackend.areAllRolesSupported() ? 4000L : 15000L);
    }

    @Override // com.android.settings.connecteddevice.usb.UsbDetailsController, com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }

    private RadioButtonPreference makeRadioPreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(this.mPreferenceCategory.getContext());
        radioButtonPreference.setKey(str);
        radioButtonPreference.setTitle(i);
        radioButtonPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(radioButtonPreference);
        return radioButtonPreference;
    }
}
