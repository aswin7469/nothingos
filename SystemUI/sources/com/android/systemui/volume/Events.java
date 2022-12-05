package com.android.systemui.volume;

import android.media.AudioSystem;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.plugins.VolumeDialogController;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class Events {
    public static Callback sCallback;
    private static final String TAG = Util.logTag(Events.class);
    private static final String[] EVENT_TAGS = {"show_dialog", "dismiss_dialog", "active_stream_changed", "expand", "key", "collection_started", "collection_stopped", "icon_click", "settings_click", "touch_level_changed", "level_changed", "internal_ringer_mode_changed", "external_ringer_mode_changed", "zen_mode_changed", "suppressor_changed", "mute_changed", "touch_level_done", "zen_mode_config_changed", "ringer_toggle", "show_usb_overheat_alarm", "dismiss_usb_overheat_alarm", "odi_captions_click", "odi_captions_tooltip_click"};
    public static final String[] DISMISS_REASONS = {"unknown", "touch_outside", "volume_controller", "timeout", "screen_off", "settings_clicked", "done_clicked", "a11y_stream_changed", "output_chooser", "usb_temperature_below_threshold"};
    public static final String[] SHOW_REASONS = {"unknown", "volume_changed", "remote_volume_changed", "usb_temperature_above_threshold"};
    @VisibleForTesting
    static MetricsLogger sLegacyLogger = new MetricsLogger();
    @VisibleForTesting
    static UiEventLogger sUiEventLogger = new UiEventLoggerImpl();

    /* loaded from: classes2.dex */
    public interface Callback {
        void writeEvent(long j, int i, Object[] objArr);

        void writeState(long j, VolumeDialogController.State state);
    }

    private static String ringerModeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "unknown" : "normal" : "vibrate" : "silent";
    }

    private static String zenModeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "unknown" : "alarms" : "no_interruptions" : "important_interruptions" : "off";
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public enum VolumeDialogOpenEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        VOLUME_DIALOG_SHOW_VOLUME_CHANGED(128),
        VOLUME_DIALOG_SHOW_REMOTE_VOLUME_CHANGED(129),
        VOLUME_DIALOG_SHOW_USB_TEMP_ALARM_CHANGED(130);
        
        private final int mId;

        VolumeDialogOpenEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogOpenEvent fromReasons(int i) {
            if (i != 1) {
                if (i == 2) {
                    return VOLUME_DIALOG_SHOW_REMOTE_VOLUME_CHANGED;
                }
                if (i == 3) {
                    return VOLUME_DIALOG_SHOW_USB_TEMP_ALARM_CHANGED;
                }
                return INVALID;
            }
            return VOLUME_DIALOG_SHOW_VOLUME_CHANGED;
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
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

        VolumeDialogCloseEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogCloseEvent fromReason(int i) {
            if (i != 1) {
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
                if (i == 9) {
                    return VOLUME_DIALOG_DISMISS_USB_TEMP_ALARM_CHANGED;
                }
                return INVALID;
            }
            return VOLUME_DIALOG_DISMISS_TOUCH_OUTSIDE;
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
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

        VolumeDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static VolumeDialogEvent fromIconState(int i) {
            if (i != 1) {
                if (i == 2) {
                    return VOLUME_DIALOG_MUTE_STREAM;
                }
                if (i == 3) {
                    return VOLUME_DIALOG_TO_VIBRATE_STREAM;
                }
                return INVALID;
            }
            return VOLUME_DIALOG_UNMUTE_STREAM;
        }

        static VolumeDialogEvent fromSliderLevel(int i) {
            return i == 0 ? VOLUME_DIALOG_SLIDER_TO_ZERO : VOLUME_DIALOG_SLIDER;
        }

        static VolumeDialogEvent fromKeyLevel(int i) {
            return i == 0 ? VOLUME_KEY_TO_ZERO : VOLUME_KEY;
        }

        static VolumeDialogEvent fromRingerMode(int i) {
            if (i != 0) {
                if (i == 1) {
                    return RINGER_MODE_VIBRATE;
                }
                if (i == 2) {
                    return RINGER_MODE_NORMAL;
                }
                return INVALID;
            }
            return RINGER_MODE_SILENT;
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public enum ZenModeEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ZEN_MODE_OFF(335),
        ZEN_MODE_IMPORTANT_ONLY(157),
        ZEN_MODE_ALARMS_ONLY(158),
        ZEN_MODE_NO_INTERRUPTIONS(159);
        
        private final int mId;

        ZenModeEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static ZenModeEvent fromZenMode(int i) {
            if (i != 0) {
                if (i == 1) {
                    return ZEN_MODE_IMPORTANT_ONLY;
                }
                if (i == 2) {
                    return ZEN_MODE_NO_INTERRUPTIONS;
                }
                if (i == 3) {
                    return ZEN_MODE_ALARMS_ONLY;
                }
                return INVALID;
            }
            return ZEN_MODE_OFF;
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x013c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String logEvent(int i, Object... objArr) {
        String[] strArr = EVENT_TAGS;
        if (i >= strArr.length) {
            return "";
        }
        StringBuilder sb = new StringBuilder("writeEvent ");
        sb.append(strArr[i]);
        if (objArr == null || objArr.length == 0) {
            if (i == 8) {
                sLegacyLogger.action(1386);
                sUiEventLogger.log(VolumeDialogEvent.VOLUME_DIALOG_SETTINGS_CLICK);
            }
            return sb.toString();
        }
        sb.append(" ");
        switch (i) {
            case 0:
                sLegacyLogger.visible(207);
                if (objArr.length > 1) {
                    Integer num = (Integer) objArr[0];
                    Boolean bool = (Boolean) objArr[1];
                    sLegacyLogger.histogram("volume_from_keyguard", bool.booleanValue() ? 1 : 0);
                    sUiEventLogger.log(VolumeDialogOpenEvent.fromReasons(num.intValue()));
                    sb.append(SHOW_REASONS[num.intValue()]);
                    sb.append(" keyguard=");
                    sb.append(bool);
                    break;
                }
                break;
            case 1:
                sLegacyLogger.hidden(207);
                Integer num2 = (Integer) objArr[0];
                sUiEventLogger.log(VolumeDialogCloseEvent.fromReason(num2.intValue()));
                sb.append(DISMISS_REASONS[num2.intValue()]);
                break;
            case 2:
                Integer num3 = (Integer) objArr[0];
                sLegacyLogger.action(210, num3.intValue());
                sUiEventLogger.log(VolumeDialogEvent.VOLUME_DIALOG_ACTIVE_STREAM_CHANGED);
                sb.append(AudioSystem.streamToString(num3.intValue()));
                break;
            case 3:
                Boolean bool2 = (Boolean) objArr[0];
                sLegacyLogger.visibility(208, bool2.booleanValue());
                sUiEventLogger.log(bool2.booleanValue() ? VolumeDialogEvent.VOLUME_DIALOG_EXPAND_DETAILS : VolumeDialogEvent.VOLUME_DIALOG_COLLAPSE_DETAILS);
                sb.append(bool2);
                break;
            case 4:
                if (objArr.length > 1) {
                    Integer num4 = (Integer) objArr[0];
                    sLegacyLogger.action(211, num4.intValue());
                    Integer num5 = (Integer) objArr[1];
                    sUiEventLogger.log(VolumeDialogEvent.fromKeyLevel(num5.intValue()));
                    sb.append(AudioSystem.streamToString(num4.intValue()));
                    sb.append(' ');
                    sb.append(num5);
                    break;
                }
                break;
            case 5:
            case 6:
            case 8:
            case 17:
            default:
                sb.append(Arrays.asList(objArr));
                break;
            case 7:
                if (objArr.length > 1) {
                    Integer num6 = (Integer) objArr[0];
                    sLegacyLogger.action(212, num6.intValue());
                    Integer num7 = (Integer) objArr[1];
                    sUiEventLogger.log(VolumeDialogEvent.fromIconState(num7.intValue()));
                    sb.append(AudioSystem.streamToString(num6.intValue()));
                    sb.append(' ');
                    sb.append(iconStateToString(num7.intValue()));
                    break;
                }
                break;
            case 9:
            case 10:
            case 15:
                if (objArr.length > 1) {
                    sb.append(AudioSystem.streamToString(((Integer) objArr[0]).intValue()));
                    sb.append(' ');
                    sb.append(objArr[1]);
                    break;
                }
                break;
            case 11:
                sb.append(ringerModeToString(((Integer) objArr[0]).intValue()));
                break;
            case 12:
                sLegacyLogger.action(213, ((Integer) objArr[0]).intValue());
                sb.append(ringerModeToString(((Integer) objArr[0]).intValue()));
                break;
            case 13:
                Integer num8 = (Integer) objArr[0];
                sb.append(zenModeToString(num8.intValue()));
                sUiEventLogger.log(ZenModeEvent.fromZenMode(num8.intValue()));
                break;
            case 14:
                if (objArr.length > 1) {
                    sb.append(objArr[0]);
                    sb.append(' ');
                    sb.append(objArr[1]);
                    break;
                }
                break;
            case 16:
                if (objArr.length > 1) {
                    Integer num9 = (Integer) objArr[1];
                    sLegacyLogger.action(209, num9.intValue());
                    sUiEventLogger.log(VolumeDialogEvent.fromSliderLevel(num9.intValue()));
                }
                if (objArr.length > 1) {
                }
                break;
            case 18:
                Integer num10 = (Integer) objArr[0];
                sLegacyLogger.action(1385, num10.intValue());
                sUiEventLogger.log(VolumeDialogEvent.fromRingerMode(num10.intValue()));
                sb.append(ringerModeToString(num10.intValue()));
                break;
            case 19:
                sLegacyLogger.visible(1457);
                sUiEventLogger.log(VolumeDialogEvent.USB_OVERHEAT_ALARM);
                if (objArr.length > 1) {
                    Boolean bool3 = (Boolean) objArr[1];
                    sLegacyLogger.histogram("show_usb_overheat_alarm", bool3.booleanValue() ? 1 : 0);
                    sb.append(SHOW_REASONS[((Integer) objArr[0]).intValue()]);
                    sb.append(" keyguard=");
                    sb.append(bool3);
                    break;
                }
                break;
            case 20:
                sLegacyLogger.hidden(1457);
                sUiEventLogger.log(VolumeDialogEvent.USB_OVERHEAT_ALARM_DISMISSED);
                if (objArr.length > 1) {
                    Boolean bool4 = (Boolean) objArr[1];
                    sLegacyLogger.histogram("dismiss_usb_overheat_alarm", bool4.booleanValue() ? 1 : 0);
                    sb.append(DISMISS_REASONS[((Integer) objArr[0]).intValue()]);
                    sb.append(" keyguard=");
                    sb.append(bool4);
                    break;
                }
                break;
        }
        return sb.toString();
    }

    public static void writeState(long j, VolumeDialogController.State state) {
        Callback callback = sCallback;
        if (callback != null) {
            callback.writeState(j, state);
        }
    }

    private static String iconStateToString(int i) {
        if (i != 1) {
            if (i == 2) {
                return "mute";
            }
            if (i == 3) {
                return "vibrate";
            }
            return "unknown_state_" + i;
        }
        return "unmute";
    }
}
