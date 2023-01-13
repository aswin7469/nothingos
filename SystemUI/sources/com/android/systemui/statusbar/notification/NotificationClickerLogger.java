package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotifInteractionLog;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/NotificationClickerLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logChildrenExpanded", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "logGutsExposed", "logMenuVisible", "logOnClick", "logParentMenuVisible", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationClickerLogger.kt */
public final class NotificationClickerLogger {
    private final LogBuffer buffer;

    @Inject
    public NotificationClickerLogger(@NotifInteractionLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logOnClick(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logOnClick$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        obtain.setStr2(notificationEntry.getRanking().getChannel().getId());
        logBuffer.commit(obtain);
    }

    public final void logMenuVisible(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logMenuVisible$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logParentMenuVisible(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logParentMenuVisible$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logChildrenExpanded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logChildrenExpanded$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logGutsExposed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logGutsExposed$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }
}
