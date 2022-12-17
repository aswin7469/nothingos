package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class MockModemPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String ALLOW_MOCK_MODEM_PROPERTY = "persist.radio.allow_mock_modem";

    public String getPreferenceKey() {
        return "allow_mock_modem";
    }

    public MockModemPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            SystemProperties.set(ALLOW_MOCK_MODEM_PROPERTY, ((Boolean) obj).booleanValue() ? "true" : "false");
            return true;
        } catch (RuntimeException e) {
            Log.e("MockModemPreferenceController", "Fail to set radio system property: " + e.getMessage());
            return true;
        }
    }

    public void updateState(Preference preference) {
        try {
            ((NtCustSwitchPreference) this.mPreference).setChecked(SystemProperties.getBoolean(ALLOW_MOCK_MODEM_PROPERTY, false));
        } catch (RuntimeException e) {
            Log.e("MockModemPreferenceController", "Fail to get radio system property: " + e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        try {
            SystemProperties.set(ALLOW_MOCK_MODEM_PROPERTY, "false");
            ((NtCustSwitchPreference) this.mPreference).setChecked(false);
        } catch (RuntimeException e) {
            Log.e("MockModemPreferenceController", "Fail to set radio system property: " + e.getMessage());
        }
    }
}
