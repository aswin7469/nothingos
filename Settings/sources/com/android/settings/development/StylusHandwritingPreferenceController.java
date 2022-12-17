package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

class StylusHandwritingPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "stylus_handwriting";
    }

    StylusHandwritingPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "stylus_handwriting_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "stylus_handwriting_enabled", 0);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "stylus_handwriting_enabled", 0);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }
}
