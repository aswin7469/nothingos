package com.android.keyguard.mediator;

import android.os.Trace;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.PendingTasksContainer;
import java.util.Optional;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/keyguard/mediator/ScreenOnCoordinator;", "Lcom/android/systemui/keyguard/ScreenLifecycle$Observer;", "screenLifecycle", "Lcom/android/systemui/keyguard/ScreenLifecycle;", "unfoldComponent", "Ljava/util/Optional;", "Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "execution", "Lcom/android/systemui/util/concurrency/Execution;", "(Lcom/android/systemui/keyguard/ScreenLifecycle;Ljava/util/Optional;Lcom/android/systemui/util/concurrency/Execution;)V", "foldAodAnimationController", "Lcom/android/systemui/unfold/FoldAodAnimationController;", "pendingTasks", "Lcom/android/systemui/util/concurrency/PendingTasksContainer;", "unfoldLightRevealAnimation", "Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;", "onScreenTurnedOn", "", "onScreenTurningOn", "onDrawn", "Ljava/lang/Runnable;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ScreenOnCoordinator.kt */
public final class ScreenOnCoordinator implements ScreenLifecycle.Observer {
    private final Execution execution;
    private final FoldAodAnimationController foldAodAnimationController;
    private final PendingTasksContainer pendingTasks = new PendingTasksContainer();
    private final UnfoldLightRevealOverlayAnimation unfoldLightRevealAnimation;

    @Inject
    public ScreenOnCoordinator(ScreenLifecycle screenLifecycle, Optional<SysUIUnfoldComponent> optional, Execution execution2) {
        Intrinsics.checkNotNullParameter(screenLifecycle, "screenLifecycle");
        Intrinsics.checkNotNullParameter(optional, "unfoldComponent");
        Intrinsics.checkNotNullParameter(execution2, "execution");
        this.execution = execution2;
        Optional<U> map = optional.map(new ScreenOnCoordinator$$ExternalSyntheticLambda0());
        Intrinsics.checkNotNullExpressionValue(map, "unfoldComponent.map(\n   …htRevealOverlayAnimation)");
        this.unfoldLightRevealAnimation = (UnfoldLightRevealOverlayAnimation) map.orElse(null);
        Optional<U> map2 = optional.map(new ScreenOnCoordinator$$ExternalSyntheticLambda1());
        Intrinsics.checkNotNullExpressionValue(map2, "unfoldComponent.map(\n   …ldAodAnimationController)");
        this.foldAodAnimationController = (FoldAodAnimationController) map2.orElse(null);
        screenLifecycle.addObserver(this);
    }

    public void onScreenTurningOn(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onDrawn");
        this.execution.assertIsMainThread();
        Trace.beginSection("ScreenOnCoordinator#onScreenTurningOn");
        this.pendingTasks.reset();
        UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = this.unfoldLightRevealAnimation;
        if (unfoldLightRevealOverlayAnimation != null) {
            unfoldLightRevealOverlayAnimation.onScreenTurningOn(this.pendingTasks.registerTask("unfold-reveal"));
        }
        FoldAodAnimationController foldAodAnimationController2 = this.foldAodAnimationController;
        if (foldAodAnimationController2 != null) {
            foldAodAnimationController2.onScreenTurningOn(this.pendingTasks.registerTask("fold-to-aod"));
        }
        this.pendingTasks.onTasksComplete(new ScreenOnCoordinator$$ExternalSyntheticLambda2(runnable));
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    /* renamed from: onScreenTurningOn$lambda-0  reason: not valid java name */
    public static final void m2322onScreenTurningOn$lambda0(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "$onDrawn");
        runnable.run();
    }

    public void onScreenTurnedOn() {
        this.execution.assertIsMainThread();
        FoldAodAnimationController foldAodAnimationController2 = this.foldAodAnimationController;
        if (foldAodAnimationController2 != null) {
            foldAodAnimationController2.onScreenTurnedOn();
        }
        this.pendingTasks.reset();
    }
}
