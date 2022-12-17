package com.android.settings.notification;

import android.content.Context;
import com.android.settings.R$bool;
import com.android.settings.R$integer;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class CallConnectedTonePreferenceController extends SettingPrefController {
    public CallConnectedTonePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "call_connected_tones", "call_connected_tone_enabled", this.mContext.getResources().getInteger(R$integer.config_default_tone_after_connected), new int[0]) {
            public boolean isApplicable(Context context) {
                return context.getResources().getBoolean(R$bool.config_show_connect_tone_ui);
            }
        };
    }
}
