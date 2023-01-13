package com.android.systemui.statusbar.notification.collection;

import android.view.Choreographer;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.concurrency.DelayableExecutor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0003\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\rH\u0016J\b\u0010\u0012\u001a\u00020\u0010H\u0016J\b\u0010\u0013\u001a\u00020\u0010H\u0002J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\rH\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifPipelineChoreographerImpl;", "Lcom/android/systemui/statusbar/notification/collection/NotifPipelineChoreographer;", "viewChoreographer", "Landroid/view/Choreographer;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Landroid/view/Choreographer;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "frameCallback", "Landroid/view/Choreographer$FrameCallback;", "isScheduled", "", "listeners", "Lcom/android/systemui/util/ListenerSet;", "Ljava/lang/Runnable;", "timeoutSubscription", "addOnEvalListener", "", "onEvalListener", "cancel", "onTimeout", "removeOnEvalListener", "schedule", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifPipelineChoreographer.kt */
final class NotifPipelineChoreographerImpl implements NotifPipelineChoreographer {
    private final DelayableExecutor executor;
    private final Choreographer.FrameCallback frameCallback = new NotifPipelineChoreographerImpl$$ExternalSyntheticLambda0(this);
    private boolean isScheduled;
    private final ListenerSet<Runnable> listeners = new ListenerSet<>();
    private Runnable timeoutSubscription;
    private final Choreographer viewChoreographer;

    @Inject
    public NotifPipelineChoreographerImpl(Choreographer choreographer, @Main DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(choreographer, "viewChoreographer");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        this.viewChoreographer = choreographer;
        this.executor = delayableExecutor;
    }

    /* access modifiers changed from: private */
    /* renamed from: frameCallback$lambda-1  reason: not valid java name */
    public static final void m3100frameCallback$lambda1(NotifPipelineChoreographerImpl notifPipelineChoreographerImpl, long j) {
        Intrinsics.checkNotNullParameter(notifPipelineChoreographerImpl, "this$0");
        if (notifPipelineChoreographerImpl.isScheduled) {
            notifPipelineChoreographerImpl.isScheduled = false;
            Runnable runnable = notifPipelineChoreographerImpl.timeoutSubscription;
            if (runnable != null) {
                runnable.run();
            }
            for (Runnable run : notifPipelineChoreographerImpl.listeners) {
                run.run();
            }
        }
    }

    public void schedule() {
        if (!this.isScheduled) {
            this.isScheduled = true;
            this.viewChoreographer.postFrameCallback(this.frameCallback);
            if (this.isScheduled) {
                this.timeoutSubscription = this.executor.executeDelayed(new NotifPipelineChoreographerImpl$$ExternalSyntheticLambda1(this), 100);
            }
        }
    }

    public void cancel() {
        if (this.isScheduled) {
            Runnable runnable = this.timeoutSubscription;
            if (runnable != null) {
                runnable.run();
            }
            this.viewChoreographer.removeFrameCallback(this.frameCallback);
        }
    }

    public void addOnEvalListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onEvalListener");
        this.listeners.addIfAbsent(runnable);
    }

    public void removeOnEvalListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onEvalListener");
        this.listeners.remove(runnable);
    }

    /* access modifiers changed from: private */
    public final void onTimeout() {
        if (this.isScheduled) {
            this.isScheduled = false;
            this.viewChoreographer.removeFrameCallback(this.frameCallback);
            for (Runnable run : this.listeners) {
                run.run();
            }
        }
    }
}
