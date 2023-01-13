package com.android.systemui.dump;

import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/dump/LogBufferFreezer;", "", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "freezeDuration", "", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/concurrency/DelayableExecutor;J)V", "pendingToken", "Ljava/lang/Runnable;", "attach", "", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "onBugreportStarted", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LogBufferFreezer.kt */
public final class LogBufferFreezer {
    private final DumpManager dumpManager;
    private final DelayableExecutor executor;
    private final long freezeDuration;
    private Runnable pendingToken;

    public LogBufferFreezer(DumpManager dumpManager2, @Main DelayableExecutor delayableExecutor, long j) {
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        this.dumpManager = dumpManager2;
        this.executor = delayableExecutor;
        this.freezeDuration = j;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Inject
    public LogBufferFreezer(DumpManager dumpManager2, @Main DelayableExecutor delayableExecutor) {
        this(dumpManager2, delayableExecutor, TimeUnit.MINUTES.toMillis(5));
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
    }

    public final void attach(BroadcastDispatcher broadcastDispatcher) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, new LogBufferFreezer$attach$1(this), new IntentFilter("com.android.internal.intent.action.BUGREPORT_STARTED"), this.executor, UserHandle.ALL, 0, (String) null, 48, (Object) null);
    }

    /* access modifiers changed from: private */
    public final void onBugreportStarted() {
        Runnable runnable = this.pendingToken;
        if (runnable != null) {
            runnable.run();
        }
        Log.i("LogBufferFreezer", "Freezing log buffers");
        this.dumpManager.freezeBuffers();
        this.pendingToken = this.executor.executeDelayed(new LogBufferFreezer$$ExternalSyntheticLambda0(this), this.freezeDuration);
    }

    /* access modifiers changed from: private */
    /* renamed from: onBugreportStarted$lambda-0  reason: not valid java name */
    public static final void m2754onBugreportStarted$lambda0(LogBufferFreezer logBufferFreezer) {
        Intrinsics.checkNotNullParameter(logBufferFreezer, "this$0");
        Log.i("LogBufferFreezer", "Unfreezing log buffers");
        logBufferFreezer.pendingToken = null;
        logBufferFreezer.dumpManager.unfreezeBuffers();
    }
}
