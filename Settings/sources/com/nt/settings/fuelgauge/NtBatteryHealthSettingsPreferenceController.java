package com.nt.settings.fuelgauge;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
/* loaded from: classes2.dex */
public class NtBatteryHealthSettingsPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "nt_battery_health_toggle";
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public NtBatteryHealthSettingsPreferenceController(Context context) {
        super(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        boolean z = false;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "nt_battery_health", 0) != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_battery_health", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }
}
