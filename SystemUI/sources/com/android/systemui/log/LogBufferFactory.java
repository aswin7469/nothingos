package com.android.systemui.log;

import com.android.systemui.dump.DumpManager;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LogBufferFactory.kt */
/* loaded from: classes.dex */
public final class LogBufferFactory {
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final LogcatEchoTracker logcatEchoTracker;

    @NotNull
    public final LogBuffer create(@NotNull String name, int i) {
        Intrinsics.checkNotNullParameter(name, "name");
        return create$default(this, name, i, 0, 4, null);
    }

    public LogBufferFactory(@NotNull DumpManager dumpManager, @NotNull LogcatEchoTracker logcatEchoTracker) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(logcatEchoTracker, "logcatEchoTracker");
        this.dumpManager = dumpManager;
        this.logcatEchoTracker = logcatEchoTracker;
    }

    public static /* synthetic */ LogBuffer create$default(LogBufferFactory logBufferFactory, String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i2 = 10;
        }
        return logBufferFactory.create(str, i, i2);
    }

    @NotNull
    public final LogBuffer create(@NotNull String name, int i, int i2) {
        Intrinsics.checkNotNullParameter(name, "name");
        LogBuffer logBuffer = new LogBuffer(name, i, i2, this.logcatEchoTracker);
        this.dumpManager.registerBuffer(name, logBuffer);
        return logBuffer;
    }
}
