package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BluetoothA2dpHwOffloadPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    boolean mChanged = false;
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "bluetooth_disable_a2dp_hw_offload";
    }

    public BluetoothA2dpHwOffloadPreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        BluetoothHwOffloadRebootDialog.show(this.mFragment);
        this.mChanged = true;
        return false;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false)) {
            ((SwitchPreference) this.mPreference).setChecked(SystemProperties.getBoolean("persist.bluetooth.a2dp_offload.disabled", false));
            return;
        }
        this.mPreference.setEnabled(false);
        ((SwitchPreference) this.mPreference).setChecked(true);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        if (SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false)) {
            ((SwitchPreference) this.mPreference).setChecked(false);
            SystemProperties.set("persist.bluetooth.a2dp_offload.disabled", "false");
        }
    }

    public boolean isDefaultValue() {
        boolean z = SystemProperties.getBoolean("ro.bluetooth.a2dp_offload.supported", false);
        boolean z2 = SystemProperties.getBoolean("persist.bluetooth.a2dp_offload.disabled", false);
        if (!z || !z2) {
            return true;
        }
        return false;
    }

    public void onHwOffloadDialogConfirmed() {
        if (this.mChanged) {
            boolean z = SystemProperties.getBoolean("persist.bluetooth.a2dp_offload.disabled", false);
            SystemProperties.set("persist.bluetooth.a2dp_offload.disabled", Boolean.toString(!z));
            if (z) {
                SystemProperties.set("persist.bluetooth.leaudio_offload.disabled", Boolean.toString(!z));
            }
        }
    }

    public void onHwOffloadDialogCanceled() {
        this.mChanged = false;
    }
}
