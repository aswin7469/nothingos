package com.nothing.settings.sound;

import com.android.settings.notification.SettingPrefController;

public class VibrateForCallsSwitchPreferenceController extends SettingPrefController {
    public boolean isAvailable() {
        return true;
    }
}
