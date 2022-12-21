package com.android.p019wm.shell.onehanded;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.p019wm.shell.C3343R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.onehanded.OneHandedSettingsUtil */
public final class OneHandedSettingsUtil {
    private static final String ONE_HANDED_MODE_TARGET_NAME = AccessibilityShortcutController.ONE_HANDED_COMPONENT_NAME.getShortClassName();
    public static final int ONE_HANDED_TIMEOUT_LONG_IN_SECONDS = 12;
    public static final int ONE_HANDED_TIMEOUT_MEDIUM_IN_SECONDS = 8;
    public static final int ONE_HANDED_TIMEOUT_NEVER = 0;
    public static final int ONE_HANDED_TIMEOUT_SHORT_IN_SECONDS = 4;
    private static final String TAG = "OneHandedSettingsUtil";

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.onehanded.OneHandedSettingsUtil$OneHandedTimeout */
    public @interface OneHandedTimeout {
    }

    public Uri registerSettingsKeyObserver(String str, ContentResolver contentResolver, ContentObserver contentObserver, int i) {
        Uri uriFor = Settings.Secure.getUriFor(str);
        if (!(contentResolver == null || uriFor == null)) {
            contentResolver.registerContentObserver(uriFor, false, contentObserver, i);
        }
        return uriFor;
    }

    public void unregisterSettingsKeyObserver(ContentResolver contentResolver, ContentObserver contentObserver) {
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(contentObserver);
        }
    }

    public boolean getSettingsOneHandedModeEnabled(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_enabled", 0, i) == 1;
    }

    public boolean setOneHandedModeEnabled(ContentResolver contentResolver, int i, int i2) {
        return Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_enabled", i, i2);
    }

    public boolean getSettingsTapsAppToExit(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "taps_app_to_exit", 1, i) == 1;
    }

    public int getSettingsOneHandedModeTimeout(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_timeout", 8, i);
    }

    public boolean getSettingsSwipeToNotificationEnabled(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "swipe_bottom_to_notification_enabled", 0, i) == 1;
    }

    public int getTutorialShownCounts(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_tutorial_show_count", 0, i);
    }

    public boolean getShortcutEnabled(ContentResolver contentResolver, int i) {
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", i);
        if (!TextUtils.isEmpty(stringForUser) && stringForUser.contains(ONE_HANDED_MODE_TARGET_NAME)) {
            return true;
        }
        String stringForUser2 = Settings.Secure.getStringForUser(contentResolver, "accessibility_shortcut_target_service", i);
        if (TextUtils.isEmpty(stringForUser2) || !stringForUser2.contains(ONE_HANDED_MODE_TARGET_NAME)) {
            return false;
        }
        return true;
    }

    public boolean setTutorialShownCounts(ContentResolver contentResolver, int i, int i2) {
        return Settings.Secure.putIntForUser(contentResolver, "one_handed_tutorial_show_count", i, i2);
    }

    public boolean getOneHandedModeActivated(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_activated", 0, i) == 1;
    }

    public boolean setOneHandedModeActivated(ContentResolver contentResolver, int i, int i2) {
        return Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_activated", i, i2);
    }

    public int getTransitionDuration(Context context) {
        return context.getResources().getInteger(C3343R.integer.config_one_handed_translate_animation_duration);
    }

    public float getTranslationFraction(Context context) {
        return context.getResources().getFraction(C3343R.fraction.config_one_handed_offset, 1, 1);
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, ContentResolver contentResolver, int i) {
        printWriter.println(TAG);
        printWriter.print("  isOneHandedModeEnable=");
        printWriter.println(getSettingsOneHandedModeEnabled(contentResolver, i));
        printWriter.print("  isSwipeToNotificationEnabled=");
        printWriter.println(getSettingsSwipeToNotificationEnabled(contentResolver, i));
        printWriter.print("  oneHandedTimeOut=");
        printWriter.println(getSettingsOneHandedModeTimeout(contentResolver, i));
        printWriter.print("  tapsAppToExit=");
        printWriter.println(getSettingsTapsAppToExit(contentResolver, i));
        printWriter.print("  shortcutActivated=");
        printWriter.println(getOneHandedModeActivated(contentResolver, i));
        printWriter.print("  tutorialShownCounts=");
        printWriter.println(getTutorialShownCounts(contentResolver, i));
    }
}
