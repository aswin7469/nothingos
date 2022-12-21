package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.util.ListenerSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0016J\b\u0010\u0010\u001a\u00020\u000eH\u0016J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0016J\u0006\u0010\u0012\u001a\u00020\u000eJ\b\u0010\u0013\u001a\u00020\u000eH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/FakeNotifPipelineChoreographer;", "Lcom/android/systemui/statusbar/notification/collection/NotifPipelineChoreographer;", "()V", "isScheduled", "", "()Z", "setScheduled", "(Z)V", "listeners", "Lcom/android/systemui/util/ListenerSet;", "Ljava/lang/Runnable;", "getListeners", "()Lcom/android/systemui/util/ListenerSet;", "addOnEvalListener", "", "onEvalListener", "cancel", "removeOnEvalListener", "runIfScheduled", "schedule", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifPipelineChoreographer.kt */
public final class FakeNotifPipelineChoreographer implements NotifPipelineChoreographer {
    private boolean isScheduled;
    private final ListenerSet<Runnable> listeners = new ListenerSet<>();

    public final boolean isScheduled() {
        return this.isScheduled;
    }

    public final void setScheduled(boolean z) {
        this.isScheduled = z;
    }

    public final ListenerSet<Runnable> getListeners() {
        return this.listeners;
    }

    public final void runIfScheduled() {
        if (this.isScheduled) {
            this.isScheduled = false;
            for (Runnable run : this.listeners) {
                run.run();
            }
        }
    }

    public void schedule() {
        this.isScheduled = true;
    }

    public void cancel() {
        this.isScheduled = false;
    }

    public void addOnEvalListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onEvalListener");
        this.listeners.addIfAbsent(runnable);
    }

    public void removeOnEvalListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onEvalListener");
        this.listeners.remove(runnable);
    }
}
