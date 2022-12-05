package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
/* loaded from: classes.dex */
public class LogdLevelPreferenceController extends AbstractPreferenceController implements Preference.OnPreferenceChangeListener {
    private ListPreference mLogdLevel;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "persist.sys.log_reject_level";
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public LogdLevelPreferenceController(Context context) {
        super(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            ListPreference listPreference = (ListPreference) preferenceScreen.findPreference("persist.sys.log_reject_level");
            this.mLogdLevel = listPreference;
            if (listPreference == null) {
                return;
            }
            listPreference.setValue(String.valueOf(SystemProperties.get("persist.sys.log_reject_level")));
            ListPreference listPreference2 = this.mLogdLevel;
            listPreference2.setSummary(listPreference2.getEntry());
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mLogdLevel) {
            writeLogdLevelOption(obj);
            this.mLogdLevel.setValue(String.valueOf(SystemProperties.get("persist.sys.log_reject_level")));
            ListPreference listPreference = this.mLogdLevel;
            listPreference.setSummary(listPreference.getEntry());
            return true;
        }
        return false;
    }

    private void writeLogdLevelOption(Object obj) {
        SystemProperties.set("persist.sys.log_reject_level", String.valueOf(obj));
    }
}
