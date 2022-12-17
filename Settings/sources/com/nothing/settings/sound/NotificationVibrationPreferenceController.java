package com.nothing.settings.sound;

import android.content.Context;
import android.os.Vibrator;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.notification.SettingPref;
import com.android.settings.notification.SettingPrefController;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class NotificationVibrationPreferenceController extends SettingPrefController {
    public NotificationVibrationPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "nt_notification_vibration", "notification_vibration_intensity", 1, new int[0]) {
            public boolean isApplicable(Context context) {
                return NotificationVibrationPreferenceController.hasHaptic(context);
            }
        };
    }

    public static boolean hasHaptic(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        return vibrator != null && vibrator.hasVibrator();
    }
}
