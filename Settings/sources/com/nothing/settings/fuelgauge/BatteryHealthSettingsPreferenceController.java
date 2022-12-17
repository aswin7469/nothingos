package com.nothing.settings.fuelgauge;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class BatteryHealthSettingsPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "nt_battery_health_toggle";
    }

    public boolean isAvailable() {
        return true;
    }

    public BatteryHealthSettingsPreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "nt_battery_health", 0) != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_battery_health", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }
}
