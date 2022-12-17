package com.nothing.settings.sound;

import android.content.Context;
import com.android.settings.R$bool;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.notification.SettingPref;
import com.android.settings.notification.SettingPrefController;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ScreenshotShutterPreferenceController extends SettingPrefController {
    public ScreenshotShutterPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "screenshot_shutter", "screenshot_shutter_enabled", 1, new int[0]);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_screenshot_shutter);
    }
}
