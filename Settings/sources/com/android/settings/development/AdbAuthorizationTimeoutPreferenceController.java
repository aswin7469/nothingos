package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class AdbAuthorizationTimeoutPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener {
    private final Context mContext;

    public String getPreferenceKey() {
        return "adb_authorization_timeout";
    }

    public AdbAuthorizationTimeoutPreferenceController(Context context) {
        super(context);
        this.mContext = context;
    }

    public void updateState(Preference preference) {
        ((NtCustSwitchPreference) this.mPreference).setChecked(Settings.Global.getLong(this.mContext.getContentResolver(), "adb_allowed_connection_time", 604800000) == 0);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        writeSetting(((Boolean) obj).booleanValue());
        return true;
    }

    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        writeSetting(false);
        ((NtCustSwitchPreference) this.mPreference).setChecked(false);
    }

    private void writeSetting(boolean z) {
        Settings.Global.putLong(this.mContext.getContentResolver(), "adb_allowed_connection_time", !z ? 604800000 : 0);
    }
}
