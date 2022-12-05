package com.android.settings.notification;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;
/* loaded from: classes.dex */
public class CallConnectedTonePreferenceController extends SettingPrefController {
    public CallConnectedTonePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "call_connected_tones", "call_connected_tone_enabled", this.mContext.getResources().getInteger(R.integer.config_default_tone_after_connected), new int[0]) { // from class: com.android.settings.notification.CallConnectedTonePreferenceController.1
            @Override // com.android.settings.notification.SettingPref
            public boolean isApplicable(Context context2) {
                return context2.getResources().getBoolean(R.bool.config_show_connect_tone_ui);
            }
        };
    }
}
