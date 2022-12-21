package com.android.systemui.statusbar.notification.collection.notifcollection;

import androidx.core.p004os.EnvironmentCompat;
import com.android.settingslib.accessibility.AccessibilityUtils;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u001a\u000e\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"TAG", "", "cancellationReasonDebugString", "reason", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLoggerKt {
    private static final String TAG = "NotifCollection";

    public static final String cancellationReasonDebugString(int i) {
        String str;
        StringBuilder append = new StringBuilder().append(i).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        switch (i) {
            case -1:
                str = "REASON_NOT_CANCELED";
                break;
            case 0:
                str = "REASON_UNKNOWN";
                break;
            case 1:
                str = "REASON_CLICK";
                break;
            case 2:
                str = "REASON_CANCEL";
                break;
            case 3:
                str = "REASON_CANCEL_ALL";
                break;
            case 4:
                str = "REASON_ERROR";
                break;
            case 5:
                str = "REASON_PACKAGE_CHANGED";
                break;
            case 6:
                str = "REASON_USER_STOPPED";
                break;
            case 7:
                str = "REASON_PACKAGE_BANNED";
                break;
            case 8:
                str = "REASON_APP_CANCEL";
                break;
            case 9:
                str = "REASON_APP_CANCEL_ALL";
                break;
            case 10:
                str = "REASON_LISTENER_CANCEL";
                break;
            case 11:
                str = "REASON_LISTENER_CANCEL_ALL";
                break;
            case 12:
                str = "REASON_GROUP_SUMMARY_CANCELED";
                break;
            case 13:
                str = "REASON_GROUP_OPTIMIZATION";
                break;
            case 14:
                str = "REASON_PACKAGE_SUSPENDED";
                break;
            case 15:
                str = "REASON_PROFILE_TURNED_OFF";
                break;
            case 16:
                str = "REASON_UNAUTOBUNDLED";
                break;
            case 17:
                str = "REASON_CHANNEL_BANNED";
                break;
            case 18:
                str = "REASON_SNOOZED";
                break;
            case 19:
                str = "REASON_TIMEOUT";
                break;
            case 20:
                str = "REASON_CHANNEL_REMOVED";
                break;
            case 21:
                str = "REASON_CLEAR_DATA";
                break;
            case 22:
                str = "REASON_ASSISTANT_CANCEL";
                break;
            default:
                str = EnvironmentCompat.MEDIA_UNKNOWN;
                break;
        }
        return append.append(str).toString();
    }
}
