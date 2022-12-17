package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.android.settingslib.development.SystemPropPoker;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class ForceDarkPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "hwui_force_dark";
    }

    public ForceDarkPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SystemProperties.set("debug.hwui.force_dark", ((Boolean) obj).booleanValue() ? "true" : null);
        SystemPropPoker.getInstance().poke();
        return true;
    }

    public void updateState(Preference preference) {
        ((NtCustSwitchPreference) this.mPreference).setChecked(SystemProperties.getBoolean("debug.hwui.force_dark", false));
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        SystemProperties.set("debug.hwui.force_dark", (String) null);
        ((NtCustSwitchPreference) this.mPreference).setChecked(false);
    }
}
