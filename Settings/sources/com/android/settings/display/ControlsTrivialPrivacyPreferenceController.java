package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class ControlsTrivialPrivacyPreferenceController extends TogglePreferenceController {
    private static final String DEPENDENCY_SETTING_KEY = "lockscreen_show_controls";
    private static final String SETTING_KEY = "lockscreen_allow_trivial_controls";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ControlsTrivialPrivacyPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 0) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z ? 1 : 0);
    }

    public CharSequence getSummary() {
        if (getAvailabilityStatus() == 5) {
            return this.mContext.getText(R$string.lockscreen_trivial_disabled_controls_summary);
        }
        return this.mContext.getText(R$string.lockscreen_trivial_controls_summary);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != 5);
        refreshSummary(preference);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }

    public int getAvailabilityStatus() {
        return showDeviceControlsSettingsEnabled() ? 0 : 5;
    }

    private boolean showDeviceControlsSettingsEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), DEPENDENCY_SETTING_KEY, 0) != 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setDependency("lockscreen_privacy_controls_switch");
    }
}
