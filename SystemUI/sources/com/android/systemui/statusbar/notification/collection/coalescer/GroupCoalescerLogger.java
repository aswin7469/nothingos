package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: GroupCoalescerLogger.kt */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger {
    @NotNull
    private final LogBuffer buffer;

    public GroupCoalescerLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logEventCoalesced(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        GroupCoalescerLogger$logEventCoalesced$2 groupCoalescerLogger$logEventCoalesced$2 = GroupCoalescerLogger$logEventCoalesced$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEventCoalesced$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logEmitBatch(@NotNull String groupKey) {
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        GroupCoalescerLogger$logEmitBatch$2 groupCoalescerLogger$logEmitBatch$2 = GroupCoalescerLogger$logEmitBatch$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEmitBatch$2);
            obtain.setStr1(groupKey);
            logBuffer.push(obtain);
        }
    }

    public final void logEarlyEmit(@NotNull String modifiedKey, @NotNull String groupKey) {
        Intrinsics.checkNotNullParameter(modifiedKey, "modifiedKey");
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        GroupCoalescerLogger$logEarlyEmit$2 groupCoalescerLogger$logEarlyEmit$2 = GroupCoalescerLogger$logEarlyEmit$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEarlyEmit$2);
            obtain.setStr1(modifiedKey);
            obtain.setStr2(groupKey);
            logBuffer.push(obtain);
        }
    }

    public final void logMaxBatchTimeout(@NotNull String modifiedKey, @NotNull String groupKey) {
        Intrinsics.checkNotNullParameter(modifiedKey, "modifiedKey");
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        GroupCoalescerLogger$logMaxBatchTimeout$2 groupCoalescerLogger$logMaxBatchTimeout$2 = GroupCoalescerLogger$logMaxBatchTimeout$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logMaxBatchTimeout$2);
            obtain.setStr1(modifiedKey);
            obtain.setStr2(groupKey);
            logBuffer.push(obtain);
        }
    }

    public final void logMissingRanking(@NotNull String forKey) {
        Intrinsics.checkNotNullParameter(forKey, "forKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        GroupCoalescerLogger$logMissingRanking$2 groupCoalescerLogger$logMissingRanking$2 = GroupCoalescerLogger$logMissingRanking$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logMissingRanking$2);
            obtain.setStr1(forKey);
            logBuffer.push(obtain);
        }
    }
}
