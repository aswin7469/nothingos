package com.android.settings.notification;

import android.content.Context;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class VibrateIconPreferenceController extends SettingPrefController {
    public boolean isAvailable() {
        return true;
    }

    public VibrateIconPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(3, "vibrate_icon", "status_bar_show_vibrate_icon", 0, new int[0]);
    }
}
