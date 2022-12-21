package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationSectionLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001a\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014J\u0018\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J \u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\t\u001a\u00020\nH\u0002J\u0016\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u0017J\u000e\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsLogger;", "", "logBuffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logAlerting", "", "position", "", "isHeadsUp", "", "logAlertingHeader", "logConversation", "logConversationsHeader", "logForegroundService", "logHeadsUp", "logIncomingHeader", "logMediaControls", "logOther", "clazz", "Ljava/lang/Class;", "logPosition", "label", "", "logSilent", "logSilentHeader", "logStartSectionUpdate", "reason", "logStr", "str", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationSectionsLogger.kt */
public final class NotificationSectionsLogger {
    private final LogBuffer logBuffer;

    @Inject
    public NotificationSectionsLogger(@NotificationSectionLog LogBuffer logBuffer2) {
        Intrinsics.checkNotNullParameter(logBuffer2, "logBuffer");
        this.logBuffer = logBuffer2;
    }

    public final void logStartSectionUpdate(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessage obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, new NotificationSectionsLogger$logStartSectionUpdate$2(str));
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }

    public final void logIncomingHeader(int i) {
        logPosition(i, "INCOMING HEADER");
    }

    public final void logMediaControls(int i) {
        logPosition(i, "MEDIA CONTROLS");
    }

    public final void logConversationsHeader(int i) {
        logPosition(i, "CONVERSATIONS HEADER");
    }

    public final void logAlertingHeader(int i) {
        logPosition(i, "ALERTING HEADER");
    }

    public final void logSilentHeader(int i) {
        logPosition(i, "SILENT HEADER");
    }

    public final void logOther(int i, Class<?> cls) {
        Intrinsics.checkNotNullParameter(cls, "clazz");
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessage obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logOther$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(cls.getName());
        logBuffer2.commit(obtain);
    }

    public final void logHeadsUp(int i, boolean z) {
        logPosition(i, "Heads Up", z);
    }

    public final void logConversation(int i, boolean z) {
        logPosition(i, "Conversation", z);
    }

    public final void logAlerting(int i, boolean z) {
        logPosition(i, "Alerting", z);
    }

    public final void logSilent(int i, boolean z) {
        logPosition(i, "Silent", z);
    }

    public final void logForegroundService(int i, boolean z) {
        logPosition(i, "Foreground Service", z);
    }

    public final void logStr(String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessage obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logStr$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }

    private final void logPosition(int i, String str, boolean z) {
        String str2 = z ? " (HUN)" : "";
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessage obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logPosition$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer2.commit(obtain);
    }

    private final void logPosition(int i, String str) {
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessage obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logPosition$4.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }
}
