package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BluetoothLeAudioHwOffloadPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    boolean mChanged = false;
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "bluetooth_disable_le_audio_hw_offload";
    }

    public BluetoothLeAudioHwOffloadPreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        BluetoothHwOffloadRebootDialog.show(this.mFragment);
        this.mChanged = true;
        return false;
    }

    public void updateState(Preference preference) {
        if (SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false) && SystemProperties.getBoolean("ro.bluetooth.leaudio_offload.supported", false)) {
            ((SwitchPreference) this.mPreference).setChecked(SystemProperties.getBoolean("persist.bluetooth.leaudio_offload.disabled", true));
            return;
        }
        this.mPreference.setEnabled(false);
        ((SwitchPreference) this.mPreference).setChecked(true);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        boolean z = false;
        if (SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false) && SystemProperties.getBoolean("ro.bluetooth.leaudio_offload.supported", false)) {
            z = true;
        }
        if (z) {
            ((SwitchPreference) this.mPreference).setChecked(true);
            SystemProperties.set("persist.bluetooth.leaudio_offload.disabled", "true");
        }
    }

    public boolean isDefaultValue() {
        boolean z = SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false) && SystemProperties.getBoolean("ro.bluetooth.leaudio_offload.supported", false);
        boolean z2 = SystemProperties.getBoolean("persist.bluetooth.leaudio_offload.disabled", false);
        if (z) {
            return z2;
        }
        return true;
    }

    public void onHwOffloadDialogConfirmed() {
        if (this.mChanged) {
            SystemProperties.set("persist.bluetooth.leaudio_offload.disabled", Boolean.toString(!SystemProperties.getBoolean("persist.bluetooth.leaudio_offload.disabled", false)));
        }
    }

    public void onHwOffloadDialogCanceled() {
        this.mChanged = false;
    }
}
