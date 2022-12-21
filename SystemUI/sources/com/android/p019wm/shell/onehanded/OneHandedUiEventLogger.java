package com.android.p019wm.shell.onehanded;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;

/* renamed from: com.android.wm.shell.onehanded.OneHandedUiEventLogger */
public class OneHandedUiEventLogger {
    public static final int EVENT_ONE_HANDED_SETTINGS_APP_TAPS_EXIT_OFF = 11;
    public static final int EVENT_ONE_HANDED_SETTINGS_APP_TAPS_EXIT_ON = 10;
    public static final int EVENT_ONE_HANDED_SETTINGS_ENABLED_OFF = 9;
    public static final int EVENT_ONE_HANDED_SETTINGS_ENABLED_ON = 8;
    public static final int EVENT_ONE_HANDED_SETTINGS_SHORTCUT_ENABLED_OFF = 21;
    public static final int EVENT_ONE_HANDED_SETTINGS_SHORTCUT_ENABLED_ON = 20;
    public static final int EVENT_ONE_HANDED_SETTINGS_SHOW_NOTIFICATION_ENABLED_OFF = 19;
    public static final int EVENT_ONE_HANDED_SETTINGS_SHOW_NOTIFICATION_ENABLED_ON = 18;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_EXIT_OFF = 13;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_EXIT_ON = 12;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_SECONDS_12 = 17;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_SECONDS_4 = 15;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_SECONDS_8 = 16;
    public static final int EVENT_ONE_HANDED_SETTINGS_TIMEOUT_SECONDS_NEVER = 14;
    public static final int EVENT_ONE_HANDED_TRIGGER_APP_TAPS_OUT = 5;
    public static final int EVENT_ONE_HANDED_TRIGGER_GESTURE_IN = 0;
    public static final int EVENT_ONE_HANDED_TRIGGER_GESTURE_OUT = 1;
    public static final int EVENT_ONE_HANDED_TRIGGER_OVERSPACE_OUT = 2;
    public static final int EVENT_ONE_HANDED_TRIGGER_POP_IME_OUT = 3;
    public static final int EVENT_ONE_HANDED_TRIGGER_ROTATION_OUT = 4;
    public static final int EVENT_ONE_HANDED_TRIGGER_SCREEN_OFF_OUT = 7;
    public static final int EVENT_ONE_HANDED_TRIGGER_TIMEOUT_OUT = 6;
    private static final String[] EVENT_TAGS = {"one_handed_trigger_gesture_in", "one_handed_trigger_gesture_out", "one_handed_trigger_overspace_out", "one_handed_trigger_pop_ime_out", "one_handed_trigger_rotation_out", "one_handed_trigger_app_taps_out", "one_handed_trigger_timeout_out", "one_handed_trigger_screen_off_out", "one_handed_settings_enabled_on", "one_handed_settings_enabled_off", "one_handed_settings_app_taps_exit_on", "one_handed_settings_app_taps_exit_off", "one_handed_settings_timeout_exit_on", "one_handed_settings_timeout_exit_off", "one_handed_settings_timeout_seconds_never", "one_handed_settings_timeout_seconds_4", "one_handed_settings_timeout_seconds_8", "one_handed_settings_timeout_seconds_12", "one_handed_settings_show_notification_enabled_on", "one_handed_settings_show_notification_enabled_off", "one_handed_settings_shortcut_enabled_on", "one_handed_settings_shortcut_enabled_off"};
    private static final String TAG = "OneHandedUiEventLogger";
    private final UiEventLogger mUiEventLogger;

    public OneHandedUiEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }

    /* renamed from: com.android.wm.shell.onehanded.OneHandedUiEventLogger$OneHandedTriggerEvent */
    public enum OneHandedTriggerEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ONE_HANDED_TRIGGER_GESTURE_IN(366),
        ONE_HANDED_TRIGGER_GESTURE_OUT(367),
        ONE_HANDED_TRIGGER_OVERSPACE_OUT(368),
        ONE_HANDED_TRIGGER_POP_IME_OUT(369),
        ONE_HANDED_TRIGGER_ROTATION_OUT(370),
        ONE_HANDED_TRIGGER_APP_TAPS_OUT(371),
        ONE_HANDED_TRIGGER_TIMEOUT_OUT(372),
        ONE_HANDED_TRIGGER_SCREEN_OFF_OUT(449);
        
        private final int mId;

        private OneHandedTriggerEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* renamed from: com.android.wm.shell.onehanded.OneHandedUiEventLogger$OneHandedSettingsTogglesEvent */
    public enum OneHandedSettingsTogglesEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON(356),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF(357),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON(358),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF(359),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON(StackStateAnimator.ANIMATION_DURATION_STANDARD),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF(361),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER(362),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4(363),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8(364),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12(365),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON(847),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF(848),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON(870),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF(871);
        
        private final int mId;

        private OneHandedSettingsTogglesEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public void writeEvent(int i) {
        logEvent(i);
    }

    private void logEvent(int i) {
        switch (i) {
            case 0:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_IN);
                return;
            case 1:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_OUT);
                return;
            case 2:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_OVERSPACE_OUT);
                return;
            case 3:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_POP_IME_OUT);
                return;
            case 4:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_ROTATION_OUT);
                return;
            case 5:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_APP_TAPS_OUT);
                return;
            case 6:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_TIMEOUT_OUT);
                return;
            case 7:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_SCREEN_OFF_OUT);
                return;
            case 8:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON);
                return;
            case 9:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF);
                return;
            case 10:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON);
                return;
            case 11:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF);
                return;
            case 12:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON);
                return;
            case 13:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF);
                return;
            case 14:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER);
                return;
            case 15:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4);
                return;
            case 16:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8);
                return;
            case 17:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12);
                return;
            case 18:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON);
                return;
            case 19:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF);
                return;
            case 20:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON);
                return;
            case 21:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF);
                return;
            default:
                return;
        }
    }
}
