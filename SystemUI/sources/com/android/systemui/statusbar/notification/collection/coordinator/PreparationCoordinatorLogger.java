package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PreparationCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class PreparationCoordinatorLogger {
    @NotNull
    private final LogBuffer buffer;

    public PreparationCoordinatorLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logNotifInflated(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logNotifInflated$2 preparationCoordinatorLogger$logNotifInflated$2 = PreparationCoordinatorLogger$logNotifInflated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logNotifInflated$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logInflationAborted(@NotNull String key, @NotNull String reason) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(reason, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logInflationAborted$2 preparationCoordinatorLogger$logInflationAborted$2 = PreparationCoordinatorLogger$logInflationAborted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logInflationAborted$2);
            obtain.setStr1(key);
            obtain.setStr2(reason);
            logBuffer.push(obtain);
        }
    }

    public final void logGroupInflationTookTooLong(@NotNull String groupKey) {
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        PreparationCoordinatorLogger$logGroupInflationTookTooLong$2 preparationCoordinatorLogger$logGroupInflationTookTooLong$2 = PreparationCoordinatorLogger$logGroupInflationTookTooLong$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logGroupInflationTookTooLong$2);
            obtain.setStr1(groupKey);
            logBuffer.push(obtain);
        }
    }

    public final void logDelayingGroupRelease(@NotNull String groupKey, @NotNull String childKey) {
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        Intrinsics.checkNotNullParameter(childKey, "childKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logDelayingGroupRelease$2 preparationCoordinatorLogger$logDelayingGroupRelease$2 = PreparationCoordinatorLogger$logDelayingGroupRelease$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logDelayingGroupRelease$2);
            obtain.setStr1(groupKey);
            obtain.setStr2(childKey);
            logBuffer.push(obtain);
        }
    }
}
