package com.android.settings.deviceinfo;

import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.deviceinfo.AbstractUptimePreferenceController;

public class UptimePreferenceController extends AbstractUptimePreferenceController implements PreferenceControllerMixin {
    public UptimePreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("up_time");
        if (findPreference != null) {
            findPreference.setVisible(false);
        }
    }
}
