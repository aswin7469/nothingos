package com.nt.settings.sound;

import android.content.Context;
import android.os.Vibrator;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.notification.SettingPref;
import com.android.settings.notification.SettingPrefController;
import com.android.settingslib.core.lifecycle.Lifecycle;
/* loaded from: classes2.dex */
public class NtNotificationVibrationPreferenceController extends SettingPrefController {
    public NtNotificationVibrationPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "nt_notification_vibration", "notification_vibration_intensity", 0, new int[0]) { // from class: com.nt.settings.sound.NtNotificationVibrationPreferenceController.1
            @Override // com.android.settings.notification.SettingPref
            public boolean isApplicable(Context context2) {
                return NtNotificationVibrationPreferenceController.hasHaptic(context2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasHaptic(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        return vibrator != null && vibrator.hasVibrator();
    }
}
