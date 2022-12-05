package com.android.systemui.dump;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LogBufferFreezer.kt */
/* loaded from: classes.dex */
public final class LogBufferFreezer {
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final DelayableExecutor executor;
    private final long freezeDuration;
    @Nullable
    private Runnable pendingToken;

    public LogBufferFreezer(@NotNull DumpManager dumpManager, @NotNull DelayableExecutor executor, long j) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.dumpManager = dumpManager;
        this.executor = executor;
        this.freezeDuration = j;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public LogBufferFreezer(@NotNull DumpManager dumpManager, @NotNull DelayableExecutor executor) {
        this(dumpManager, executor, TimeUnit.MINUTES.toMillis(5L));
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(executor, "executor");
    }

    public final void attach(@NotNull BroadcastDispatcher broadcastDispatcher) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        broadcastDispatcher.registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.dump.LogBufferFreezer$attach$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@Nullable Context context, @Nullable Intent intent) {
                LogBufferFreezer.this.onBugreportStarted();
            }
        }, new IntentFilter("com.android.internal.intent.action.BUGREPORT_STARTED"), this.executor, UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onBugreportStarted() {
        Runnable runnable = this.pendingToken;
        if (runnable != null) {
            runnable.run();
        }
        Log.i("LogBufferFreezer", "Freezing log buffers");
        this.dumpManager.freezeBuffers();
        this.pendingToken = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.dump.LogBufferFreezer$onBugreportStarted$1
            @Override // java.lang.Runnable
            public final void run() {
                DumpManager dumpManager;
                Log.i("LogBufferFreezer", "Unfreezing log buffers");
                LogBufferFreezer.this.pendingToken = null;
                dumpManager = LogBufferFreezer.this.dumpManager;
                dumpManager.unfreezeBuffers();
            }
        }, this.freezeDuration);
    }
}
