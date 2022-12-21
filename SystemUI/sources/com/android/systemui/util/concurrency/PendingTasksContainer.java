package com.android.systemui.util.concurrency;

import com.android.settingslib.datetime.ZoneGetter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005J\u000e\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/util/concurrency/PendingTasksContainer;", "", "()V", "completionCallback", "Ljava/util/concurrent/atomic/AtomicReference;", "Ljava/lang/Runnable;", "pendingTasksCount", "Ljava/util/concurrent/atomic/AtomicInteger;", "getPendingCount", "", "onTasksComplete", "", "onComplete", "registerTask", "name", "", "reset", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PendingTasksContainer.kt */
public final class PendingTasksContainer {
    private AtomicReference<Runnable> completionCallback = new AtomicReference<>();
    private AtomicInteger pendingTasksCount = new AtomicInteger(0);

    public final Runnable registerTask(String str) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        this.pendingTasksCount.incrementAndGet();
        return new PendingTasksContainer$$ExternalSyntheticLambda0(this, str);
    }

    /* access modifiers changed from: private */
    /* renamed from: registerTask$lambda-0  reason: not valid java name */
    public static final void m3307registerTask$lambda0(PendingTasksContainer pendingTasksContainer, String str) {
        Runnable andSet;
        Intrinsics.checkNotNullParameter(pendingTasksContainer, "this$0");
        Intrinsics.checkNotNullParameter(str, "$name");
        if (pendingTasksContainer.pendingTasksCount.decrementAndGet() == 0 && (andSet = pendingTasksContainer.completionCallback.getAndSet(null)) != null) {
            andSet.run();
        }
    }

    public final void reset() {
        this.completionCallback = new AtomicReference<>();
        this.pendingTasksCount = new AtomicInteger(0);
    }

    public final void onTasksComplete(Runnable runnable) {
        Runnable andSet;
        Intrinsics.checkNotNullParameter(runnable, "onComplete");
        this.completionCallback.set(runnable);
        if (this.pendingTasksCount.get() == 0 && (andSet = this.completionCallback.getAndSet(null)) != null) {
            andSet.run();
        }
    }

    public final int getPendingCount() {
        return this.pendingTasksCount.get();
    }
}
