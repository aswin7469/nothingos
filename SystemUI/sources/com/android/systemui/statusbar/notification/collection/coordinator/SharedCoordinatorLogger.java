package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SharedCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class SharedCoordinatorLogger {
    @NotNull
    private final LogBuffer buffer;

    public SharedCoordinatorLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logUserOrProfileChanged(int i, @NotNull String profiles) {
        Intrinsics.checkNotNullParameter(profiles, "profiles");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        SharedCoordinatorLogger$logUserOrProfileChanged$2 sharedCoordinatorLogger$logUserOrProfileChanged$2 = SharedCoordinatorLogger$logUserOrProfileChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotCurrentUserFilter", logLevel, sharedCoordinatorLogger$logUserOrProfileChanged$2);
            obtain.setInt1(i);
            obtain.setStr1(profiles);
            logBuffer.push(obtain);
        }
    }
}
