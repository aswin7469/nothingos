package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger {
    @NotNull
    private final LogBuffer logBuffer;

    public NotificationSectionsLogger(@NotNull LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "logBuffer");
        this.logBuffer = logBuffer;
    }

    public final void logStartSectionUpdate(@NotNull String reason) {
        Intrinsics.checkNotNullParameter(reason, "reason");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logStartSectionUpdate$2 notificationSectionsLogger$logStartSectionUpdate$2 = new NotificationSectionsLogger$logStartSectionUpdate$2(reason);
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logStartSectionUpdate$2);
            obtain.setStr1(reason);
            logBuffer.push(obtain);
        }
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

    public final void logOther(int i, @NotNull Class<?> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logOther$2 notificationSectionsLogger$logOther$2 = NotificationSectionsLogger$logOther$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logOther$2);
            obtain.setInt1(i);
            obtain.setStr1(clazz.getName());
            logBuffer.push(obtain);
        }
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

    public final void logStr(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logStr$2 notificationSectionsLogger$logStr$2 = NotificationSectionsLogger$logStr$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logStr$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    private final void logPosition(int i, String str, boolean z) {
        String str2 = z ? " (HUN)" : "";
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logPosition$2 notificationSectionsLogger$logPosition$2 = NotificationSectionsLogger$logPosition$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logPosition$2);
            obtain.setInt1(i);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    private final void logPosition(int i, String str) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logPosition$4 notificationSectionsLogger$logPosition$4 = NotificationSectionsLogger$logPosition$4.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logPosition$4);
            obtain.setInt1(i);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }
}
