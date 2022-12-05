package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: RowContentBindStageLogger.kt */
/* loaded from: classes.dex */
public final class RowContentBindStageLogger {
    @NotNull
    private final LogBuffer buffer;

    public RowContentBindStageLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logStageParams(@NotNull String notifKey, @NotNull String stageParams) {
        Intrinsics.checkNotNullParameter(notifKey, "notifKey");
        Intrinsics.checkNotNullParameter(stageParams, "stageParams");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        RowContentBindStageLogger$logStageParams$2 rowContentBindStageLogger$logStageParams$2 = RowContentBindStageLogger$logStageParams$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("RowContentBindStage", logLevel, rowContentBindStageLogger$logStageParams$2);
            obtain.setStr1(notifKey);
            obtain.setStr2(stageParams);
            logBuffer.push(obtain);
        }
    }
}
