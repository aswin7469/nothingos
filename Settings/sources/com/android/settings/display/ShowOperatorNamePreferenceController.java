package com.android.settings.display;

import android.content.Context;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class ShowOperatorNamePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    public String getPreferenceKey() {
        return "show_operator_name";
    }

    public ShowOperatorNamePreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(SubscriptionManager.getDefaultDataSubscriptionId())) == null || !configForSubId.getBoolean("show_operator_name_in_statusbar_bool", false)) {
            return false;
        }
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "show_operator_name", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = true;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "show_operator_name", 1) == 0) {
            z = false;
        }
        switchPreference.setChecked(z);
    }
}
