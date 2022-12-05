package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger {
    @NotNull
    private final LogBuffer buffer;

    public NotificationEntryManagerLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logNotifAdded(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifAdded$2 notificationEntryManagerLogger$logNotifAdded$2 = NotificationEntryManagerLogger$logNotifAdded$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifAdded$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifUpdated(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifUpdated$2 notificationEntryManagerLogger$logNotifUpdated$2 = NotificationEntryManagerLogger$logNotifUpdated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifUpdated$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logInflationAborted(@NotNull String key, @NotNull String status, @NotNull String reason) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(reason, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationEntryManagerLogger$logInflationAborted$2 notificationEntryManagerLogger$logInflationAborted$2 = NotificationEntryManagerLogger$logInflationAborted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logInflationAborted$2);
            obtain.setStr1(key);
            obtain.setStr2(status);
            obtain.setStr3(reason);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifInflated(@NotNull String key, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationEntryManagerLogger$logNotifInflated$2 notificationEntryManagerLogger$logNotifInflated$2 = NotificationEntryManagerLogger$logNotifInflated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifInflated$2);
            obtain.setStr1(key);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logRemovalIntercepted(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logRemovalIntercepted$2 notificationEntryManagerLogger$logRemovalIntercepted$2 = NotificationEntryManagerLogger$logRemovalIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logRemovalIntercepted$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(@NotNull String key, @NotNull String extenderName, @NotNull String status) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(extenderName, "extenderName");
        Intrinsics.checkNotNullParameter(status, "status");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logLifetimeExtended$2 notificationEntryManagerLogger$logLifetimeExtended$2 = NotificationEntryManagerLogger$logLifetimeExtended$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logLifetimeExtended$2);
            obtain.setStr1(key);
            obtain.setStr2(extenderName);
            obtain.setStr3(status);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifRemoved(@NotNull String key, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifRemoved$2 notificationEntryManagerLogger$logNotifRemoved$2 = NotificationEntryManagerLogger$logNotifRemoved$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifRemoved$2);
            obtain.setStr1(key);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logFilterAndSort(@NotNull String reason) {
        Intrinsics.checkNotNullParameter(reason, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logFilterAndSort$2 notificationEntryManagerLogger$logFilterAndSort$2 = NotificationEntryManagerLogger$logFilterAndSort$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logFilterAndSort$2);
            obtain.setStr1(reason);
            logBuffer.push(obtain);
        }
    }
}
