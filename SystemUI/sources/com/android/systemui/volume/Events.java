package com.android.systemui.volume;

import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.p004os.EnvironmentCompat;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.plugins.VolumeDialogController;

public class Events {
    public static final String[] DISMISS_REASONS = {EnvironmentCompat.MEDIA_UNKNOWN, "touch_outside", "volume_controller", "timeout", "screen_off", "settings_clicked", "done_clicked", "a11y_stream_changed", "output_chooser", "usb_temperature_below_threshold"};
    public static final int DISMISS_REASON_DONE_CLICKED = 6;
    public static final int DISMISS_REASON_OUTPUT_CHOOSER = 8;
    public static final int DISMISS_REASON_SCREEN_OFF = 4;
    public static final int DISMISS_REASON_SETTINGS_CLICKED = 5;
    public static final int DISMISS_REASON_TIMEOUT = 3;
    public static final int DISMISS_REASON_TOUCH_OUTSIDE = 1;
    public static final int DISMISS_REASON_UNKNOWN = 0;
    public static final int DISMISS_REASON_USB_OVERHEAD_ALARM_CHANGED = 9;
    public static final int DISMISS_REASON_VOLUME_CONTROLLER = 2;
    public static final int DISMISS_STREAM_GONE = 7;
    public static final int EVENT_ACTIVE_STREAM_CHANGED = 2;
    public static final int EVENT_COLLECTION_STARTED = 5;
    public static final int EVENT_COLLECTION_STOPPED = 6;
    public static final int EVENT_DISMISS_DIALOG = 1;
    public static final int EVENT_DISMISS_USB_OVERHEAT_ALARM = 20;
    public static final int EVENT_EXPAND = 3;
    public static final int EVENT_EXTERNAL_RINGER_MODE_CHANGED = 12;
    public static final int EVENT_ICON_CLICK = 7;
    public static final int EVENT_INTERNAL_RINGER_MODE_CHANGED = 11;
    public static final int EVENT_KEY = 4;
    public static final int EVENT_LEVEL_CHANGED = 10;
    public static final int EVENT_MUTE_CHANGED = 15;
    public static final int EVENT_ODI_CAPTIONS_CLICK = 21;
    public static final int EVENT_ODI_CAPTIONS_TOOLTIP_CLICK = 22;
    public static final int EVENT_RINGER_TOGGLE = 18;
    public static final int EVENT_SETTINGS_CLICK = 8;
    public static final int EVENT_SHOW_DIALOG = 0;
    public static final int EVENT_SHOW_USB_OVERHEAT_ALARM = 19;
    public static final int EVENT_SUPPRESSOR_CHANGED = 14;
    private static final String[] EVENT_TAGS = {"show_dialog", "dismiss_dialog", "active_stream_changed", "expand", "key", "collection_started", "collection_stopped", "icon_click", "settings_click", "touch_level_changed", "level_changed", "internal_ringer_mode_changed", "external_ringer_mode_changed", "zen_mode_changed", "suppressor_changed", "mute_changed", "touch_level_done", "zen_mode_config_changed", "ringer_toggle", "show_usb_overheat_alarm", "dismiss_usb_overheat_alarm", "odi_captions_click", "odi_captions_tooltip_click"};
    public static final int EVENT_TOUCH_LEVEL_CHANGED = 9;
    public static final int EVENT_TOUCH_LEVEL_DONE = 16;
    public static final int EVENT_ZEN_CONFIG_CHANGED = 17;
    public static final int EVENT_ZEN_MODE_CHANGED = 13;
    public static final int ICON_STATE_MUTE = 2;
    public static final int ICON_STATE_UNKNOWN = 0;
    public static final int ICON_STATE_UNMUTE = 1;
    public static final int ICON_STATE_VIBRATE = 3;
    public static final String[] SHOW_REASONS = {EnvironmentCompat.MEDIA_UNKNOWN, "volume_changed", "remote_volume_changed", "usb_temperature_above_threshold"};
    public static final int SHOW_REASON_REMOTE_VOLUME_CHANGED = 2;
    public static final int SHOW_REASON_UNKNOWN = 0;
    public static final int SHOW_REASON_USB_OVERHEAD_ALARM_CHANGED = 3;
    public static final int SHOW_REASON_VOLUME_CHANGED = 1;
    private static final String TAG = Util.logTag(Events.class);
    public static Callback sCallback;
    static MetricsLogger sLegacyLogger = new MetricsLogger();
    static UiEventLogger sUiEventLogger = new UiEventLoggerImpl();

    public interface Callback {
        void writeEvent(long j, int i, Object[] objArr);

        void writeState(long j, VolumeDialogController.State state);
    }

    private static String ringerModeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? EnvironmentCompat.MEDIA_UNKNOWN : "normal" : "vibrate" : NotificationCompat.GROUP_KEY_SILENT;
    }

    private static String zenModeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? EnvironmentCompat.MEDIA_UNKNOWN : "alarms" : "no_interruptions" : "important_interruptions" : "off";
    }

    public enum VolumeDialogOpenEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        VOLUME_DIALOG_SHOW_VOLUME_CHANGED(128),
        VOLUME_DIALOG_SHOW_REMOTE_VOLUME_CHANGED(129),
        VOLUME_DIALOG_SHOW_USB_TEMP_ALARM_CHANGED(130);
        
        private final int mId;

        private VolumeDialogOpenEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogOpenEvent fromReasons(int i) {
            if (i == 1) {
                return VOLUME_DIALOG_SHOW_VOLUME_CHANGED;
            }
            if (i == 2) {
                return VOLUME_DIALOG_SHOW_REMOTE_VOLUME_CHANGED;
            }
            if (i != 3) {
                return INVALID;
            }
            return VOLUME_DIALOG_SHOW_USB_TEMP_ALARM_CHANGED;
        }
    }

    public enum VolumeDialogCloseEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        VOLUME_DIALOG_DISMISS_TOUCH_OUTSIDE(134),
        VOLUME_DIALOG_DISMISS_SYSTEM(135),
        VOLUME_DIALOG_DISMISS_TIMEOUT(136),
        VOLUME_DIALOG_DISMISS_SCREEN_OFF(137),
        VOLUME_DIALOG_DISMISS_SETTINGS(138),
        VOLUME_DIALOG_DISMISS_STREAM_GONE(140),
        VOLUME_DIALOG_DISMISS_USB_TEMP_ALARM_CHANGED(142);
        
        private final int mId;

        private VolumeDialogCloseEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogCloseEvent fromReason(int i) {
            if (i == 1) {
                return VOLUME_DIALOG_DISMISS_TOUCH_OUTSIDE;
            }
            if (i == 2) {
                return VOLUME_DIALOG_DISMISS_SYSTEM;
            }
            if (i == 3) {
                return VOLUME_DIALOG_DISMISS_TIMEOUT;
            }
            if (i == 4) {
                return VOLUME_DIALOG_DISMISS_SCREEN_OFF;
            }
            if (i == 5) {
                return VOLUME_DIALOG_DISMISS_SETTINGS;
            }
            if (i == 7) {
                return VOLUME_DIALOG_DISMISS_STREAM_GONE;
            }
            if (i != 9) {
                return INVALID;
            }
            return VOLUME_DIALOG_DISMISS_USB_TEMP_ALARM_CHANGED;
        }
    }

    public enum VolumeDialogEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        VOLUME_DIALOG_SETTINGS_CLICK(143),
        VOLUME_DIALOG_EXPAND_DETAILS(144),
        VOLUME_DIALOG_COLLAPSE_DETAILS(145),
        VOLUME_DIALOG_ACTIVE_STREAM_CHANGED(146),
        VOLUME_DIALOG_MUTE_STREAM(147),
        VOLUME_DIALOG_UNMUTE_STREAM(148),
        VOLUME_DIALOG_TO_VIBRATE_STREAM(149),
        VOLUME_DIALOG_SLIDER(150),
        VOLUME_DIALOG_SLIDER_TO_ZERO(151),
        VOLUME_KEY_TO_ZERO(152),
        VOLUME_KEY(153),
        RINGER_MODE_SILENT(154),
        RINGER_MODE_VIBRATE(155),
        RINGER_MODE_NORMAL(334),
        USB_OVERHEAT_ALARM(160),
        USB_OVERHEAT_ALARM_DISMISSED(161);
        
        private final int mId;

        private VolumeDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogEvent fromIconState(int i) {
            if (i == 1) {
                return VOLUME_DIALOG_UNMUTE_STREAM;
            }
            if (i == 2) {
                return VOLUME_DIALOG_MUTE_STREAM;
            }
            if (i != 3) {
                return INVALID;
            }
            return VOLUME_DIALOG_TO_VIBRATE_STREAM;
        }

        static VolumeDialogEvent fromSliderLevel(int i) {
            return i == 0 ? VOLUME_DIALOG_SLIDER_TO_ZERO : VOLUME_DIALOG_SLIDER;
        }

        static VolumeDialogEvent fromKeyLevel(int i) {
            return i == 0 ? VOLUME_KEY_TO_ZERO : VOLUME_KEY;
        }

        static VolumeDialogEvent fromRingerMode(int i) {
            if (i == 0) {
                return RINGER_MODE_SILENT;
            }
            if (i == 1) {
                return RINGER_MODE_VIBRATE;
            }
            if (i != 2) {
                return INVALID;
            }
            return RINGER_MODE_NORMAL;
        }
    }

    public enum ZenModeEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ZEN_MODE_OFF(335),
        ZEN_MODE_IMPORTANT_ONLY(157),
        ZEN_MODE_ALARMS_ONLY(158),
        ZEN_MODE_NO_INTERRUPTIONS(159);
        
        private final int mId;

        private ZenModeEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static ZenModeEvent fromZenMode(int i) {
            if (i == 0) {
                return ZEN_MODE_OFF;
            }
            if (i == 1) {
                return ZEN_MODE_IMPORTANT_ONLY;
            }
            if (i == 2) {
                return ZEN_MODE_NO_INTERRUPTIONS;
            }
            if (i != 3) {
                return INVALID;
            }
            return ZEN_MODE_ALARMS_ONLY;
        }
    }

    public static void writeEvent(int i, Object... objArr) {
        long currentTimeMillis = System.currentTimeMillis();
        Log.i(TAG, logEvent(i, objArr));
        Callback callback = sCallback;
        if (callback != null) {
            callback.writeEvent(currentTimeMillis, i, objArr);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0130, code lost:
        r0.append(ringerModeToString(r8[0].intValue()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0142, code lost:
        if (r8.length <= 1) goto L_0x027b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0144, code lost:
        r0.append(android.media.AudioSystem.streamToString(r8[0].intValue())).append(' ').append(r8[1]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x027f, code lost:
        return r0.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String logEvent(int r7, java.lang.Object... r8) {
        /*
            java.lang.String[] r0 = EVENT_TAGS
            int r1 = r0.length
            if (r7 < r1) goto L_0x0008
            java.lang.String r7 = ""
            return r7
        L_0x0008:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "writeEvent "
            r1.<init>((java.lang.String) r2)
            r0 = r0[r7]
            java.lang.StringBuilder r0 = r1.append((java.lang.String) r0)
            if (r8 == 0) goto L_0x0280
            int r1 = r8.length
            if (r1 != 0) goto L_0x001d
            goto L_0x0280
        L_0x001d:
            java.lang.String r1 = " "
            r0.append((java.lang.String) r1)
            r1 = 1457(0x5b1, float:2.042E-42)
            r2 = 207(0xcf, float:2.9E-43)
            java.lang.String r3 = " keyguard="
            r4 = 32
            r5 = 0
            r6 = 1
            switch(r7) {
                case 0: goto L_0x023f;
                case 1: goto L_0x021d;
                case 2: goto L_0x01fb;
                case 3: goto L_0x01d7;
                case 4: goto L_0x019f;
                case 5: goto L_0x002f;
                case 6: goto L_0x002f;
                case 7: goto L_0x015f;
                case 8: goto L_0x002f;
                case 9: goto L_0x0141;
                case 10: goto L_0x0141;
                case 11: goto L_0x0130;
                case 12: goto L_0x0121;
                case 13: goto L_0x0103;
                case 14: goto L_0x00ef;
                case 15: goto L_0x0141;
                case 16: goto L_0x00cf;
                case 17: goto L_0x002f;
                case 18: goto L_0x00a6;
                case 19: goto L_0x006f;
                case 20: goto L_0x0038;
                default: goto L_0x002f;
            }
        L_0x002f:
            java.util.List r7 = java.util.Arrays.asList(r8)
            r0.append((java.lang.Object) r7)
            goto L_0x027b
        L_0x0038:
            com.android.internal.logging.MetricsLogger r7 = sLegacyLogger
            r7.hidden(r1)
            com.android.internal.logging.UiEventLogger r7 = sUiEventLogger
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.USB_OVERHEAT_ALARM_DISMISSED
            r7.log(r1)
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r6]
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            boolean r2 = r7.booleanValue()
            java.lang.String r4 = "dismiss_usb_overheat_alarm"
            r1.histogram(r4, r2)
            r8 = r8[r5]
            java.lang.Integer r8 = (java.lang.Integer) r8
            java.lang.String[] r1 = DISMISS_REASONS
            int r8 = r8.intValue()
            r8 = r1[r8]
            java.lang.StringBuilder r8 = r0.append((java.lang.String) r8)
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r3)
            r8.append((java.lang.Object) r7)
            goto L_0x027b
        L_0x006f:
            com.android.internal.logging.MetricsLogger r7 = sLegacyLogger
            r7.visible(r1)
            com.android.internal.logging.UiEventLogger r7 = sUiEventLogger
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.USB_OVERHEAT_ALARM
            r7.log(r1)
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r6]
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            boolean r2 = r7.booleanValue()
            java.lang.String r4 = "show_usb_overheat_alarm"
            r1.histogram(r4, r2)
            r8 = r8[r5]
            java.lang.Integer r8 = (java.lang.Integer) r8
            java.lang.String[] r1 = SHOW_REASONS
            int r8 = r8.intValue()
            r8 = r1[r8]
            java.lang.StringBuilder r8 = r0.append((java.lang.String) r8)
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r3)
            r8.append((java.lang.Object) r7)
            goto L_0x027b
        L_0x00a6:
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r8 = sLegacyLogger
            r1 = 1385(0x569, float:1.941E-42)
            int r2 = r7.intValue()
            r8.action(r1, r2)
            com.android.internal.logging.UiEventLogger r8 = sUiEventLogger
            int r1 = r7.intValue()
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.fromRingerMode(r1)
            r8.log(r1)
            int r7 = r7.intValue()
            java.lang.String r7 = ringerModeToString(r7)
            r0.append((java.lang.String) r7)
            goto L_0x027b
        L_0x00cf:
            int r7 = r8.length
            if (r7 <= r6) goto L_0x0141
            r7 = r8[r6]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            r2 = 209(0xd1, float:2.93E-43)
            int r3 = r7.intValue()
            r1.action(r2, r3)
            com.android.internal.logging.UiEventLogger r1 = sUiEventLogger
            int r7 = r7.intValue()
            com.android.systemui.volume.Events$VolumeDialogEvent r7 = com.android.systemui.volume.Events.VolumeDialogEvent.fromSliderLevel(r7)
            r1.log(r7)
            goto L_0x0141
        L_0x00ef:
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r5]
            java.lang.StringBuilder r7 = r0.append((java.lang.Object) r7)
            java.lang.StringBuilder r7 = r7.append((char) r4)
            r8 = r8[r6]
            r7.append((java.lang.Object) r8)
            goto L_0x027b
        L_0x0103:
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r8 = r7.intValue()
            java.lang.String r8 = zenModeToString(r8)
            r0.append((java.lang.String) r8)
            com.android.internal.logging.UiEventLogger r8 = sUiEventLogger
            int r7 = r7.intValue()
            com.android.systemui.volume.Events$ZenModeEvent r7 = com.android.systemui.volume.Events.ZenModeEvent.fromZenMode(r7)
            r8.log(r7)
            goto L_0x027b
        L_0x0121:
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            r2 = 213(0xd5, float:2.98E-43)
            int r7 = r7.intValue()
            r1.action(r2, r7)
        L_0x0130:
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            java.lang.String r7 = ringerModeToString(r7)
            r0.append((java.lang.String) r7)
            goto L_0x027b
        L_0x0141:
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            java.lang.String r7 = android.media.AudioSystem.streamToString(r7)
            java.lang.StringBuilder r7 = r0.append((java.lang.String) r7)
            java.lang.StringBuilder r7 = r7.append((char) r4)
            r8 = r8[r6]
            r7.append((java.lang.Object) r8)
            goto L_0x027b
        L_0x015f:
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            r2 = 212(0xd4, float:2.97E-43)
            int r3 = r7.intValue()
            r1.action(r2, r3)
            r8 = r8[r6]
            java.lang.Integer r8 = (java.lang.Integer) r8
            com.android.internal.logging.UiEventLogger r1 = sUiEventLogger
            int r2 = r8.intValue()
            com.android.systemui.volume.Events$VolumeDialogEvent r2 = com.android.systemui.volume.Events.VolumeDialogEvent.fromIconState(r2)
            r1.log(r2)
            int r7 = r7.intValue()
            java.lang.String r7 = android.media.AudioSystem.streamToString(r7)
            java.lang.StringBuilder r7 = r0.append((java.lang.String) r7)
            java.lang.StringBuilder r7 = r7.append((char) r4)
            int r8 = r8.intValue()
            java.lang.String r8 = iconStateToString(r8)
            r7.append((java.lang.String) r8)
            goto L_0x027b
        L_0x019f:
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            r2 = 211(0xd3, float:2.96E-43)
            int r3 = r7.intValue()
            r1.action(r2, r3)
            r8 = r8[r6]
            java.lang.Integer r8 = (java.lang.Integer) r8
            com.android.internal.logging.UiEventLogger r1 = sUiEventLogger
            int r2 = r8.intValue()
            com.android.systemui.volume.Events$VolumeDialogEvent r2 = com.android.systemui.volume.Events.VolumeDialogEvent.fromKeyLevel(r2)
            r1.log(r2)
            int r7 = r7.intValue()
            java.lang.String r7 = android.media.AudioSystem.streamToString(r7)
            java.lang.StringBuilder r7 = r0.append((java.lang.String) r7)
            java.lang.StringBuilder r7 = r7.append((char) r4)
            r7.append((java.lang.Object) r8)
            goto L_0x027b
        L_0x01d7:
            r7 = r8[r5]
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            com.android.internal.logging.MetricsLogger r8 = sLegacyLogger
            r1 = 208(0xd0, float:2.91E-43)
            boolean r2 = r7.booleanValue()
            r8.visibility(r1, r2)
            com.android.internal.logging.UiEventLogger r8 = sUiEventLogger
            boolean r1 = r7.booleanValue()
            if (r1 == 0) goto L_0x01f1
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.VOLUME_DIALOG_EXPAND_DETAILS
            goto L_0x01f3
        L_0x01f1:
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.VOLUME_DIALOG_COLLAPSE_DETAILS
        L_0x01f3:
            r8.log(r1)
            r0.append((java.lang.Object) r7)
            goto L_0x027b
        L_0x01fb:
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.MetricsLogger r8 = sLegacyLogger
            r1 = 210(0xd2, float:2.94E-43)
            int r2 = r7.intValue()
            r8.action(r1, r2)
            com.android.internal.logging.UiEventLogger r8 = sUiEventLogger
            com.android.systemui.volume.Events$VolumeDialogEvent r1 = com.android.systemui.volume.Events.VolumeDialogEvent.VOLUME_DIALOG_ACTIVE_STREAM_CHANGED
            r8.log(r1)
            int r7 = r7.intValue()
            java.lang.String r7 = android.media.AudioSystem.streamToString(r7)
            r0.append((java.lang.String) r7)
            goto L_0x027b
        L_0x021d:
            com.android.internal.logging.MetricsLogger r7 = sLegacyLogger
            r7.hidden(r2)
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            com.android.internal.logging.UiEventLogger r8 = sUiEventLogger
            int r1 = r7.intValue()
            com.android.systemui.volume.Events$VolumeDialogCloseEvent r1 = com.android.systemui.volume.Events.VolumeDialogCloseEvent.fromReason(r1)
            r8.log(r1)
            java.lang.String[] r8 = DISMISS_REASONS
            int r7 = r7.intValue()
            r7 = r8[r7]
            r0.append((java.lang.String) r7)
            goto L_0x027b
        L_0x023f:
            com.android.internal.logging.MetricsLogger r7 = sLegacyLogger
            r7.visible(r2)
            int r7 = r8.length
            if (r7 <= r6) goto L_0x027b
            r7 = r8[r5]
            java.lang.Integer r7 = (java.lang.Integer) r7
            r8 = r8[r6]
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            com.android.internal.logging.MetricsLogger r1 = sLegacyLogger
            boolean r2 = r8.booleanValue()
            java.lang.String r4 = "volume_from_keyguard"
            r1.histogram(r4, r2)
            com.android.internal.logging.UiEventLogger r1 = sUiEventLogger
            int r2 = r7.intValue()
            com.android.systemui.volume.Events$VolumeDialogOpenEvent r2 = com.android.systemui.volume.Events.VolumeDialogOpenEvent.fromReasons(r2)
            r1.log(r2)
            java.lang.String[] r1 = SHOW_REASONS
            int r7 = r7.intValue()
            r7 = r1[r7]
            java.lang.StringBuilder r7 = r0.append((java.lang.String) r7)
            java.lang.StringBuilder r7 = r7.append((java.lang.String) r3)
            r7.append((java.lang.Object) r8)
        L_0x027b:
            java.lang.String r7 = r0.toString()
            return r7
        L_0x0280:
            r8 = 8
            if (r7 != r8) goto L_0x0292
            com.android.internal.logging.MetricsLogger r7 = sLegacyLogger
            r8 = 1386(0x56a, float:1.942E-42)
            r7.action(r8)
            com.android.internal.logging.UiEventLogger r7 = sUiEventLogger
            com.android.systemui.volume.Events$VolumeDialogEvent r8 = com.android.systemui.volume.Events.VolumeDialogEvent.VOLUME_DIALOG_SETTINGS_CLICK
            r7.log(r8)
        L_0x0292:
            java.lang.String r7 = r0.toString()
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.Events.logEvent(int, java.lang.Object[]):java.lang.String");
    }

    public static void writeState(long j, VolumeDialogController.State state) {
        Callback callback = sCallback;
        if (callback != null) {
            callback.writeState(j, state);
        }
    }

    private static String iconStateToString(int i) {
        if (i == 1) {
            return "unmute";
        }
        if (i != 2) {
            return i != 3 ? "unknown_state_" + i : "vibrate";
        }
        return "mute";
    }
}
