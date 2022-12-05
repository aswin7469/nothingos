package com.android.systemui.statusbar.phone;

import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StatusBarLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class StatusBarLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    private final ActivityLaunchAnimator.Controller delegate;
    private final boolean isLaunchForActivity;
    @NotNull
    private final StatusBar statusBar;

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    @NotNull
    public ActivityLaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.delegate.setLaunchContainer(viewGroup);
    }

    public StatusBarLaunchAnimatorController(@NotNull ActivityLaunchAnimator.Controller delegate, @NotNull StatusBar statusBar, boolean z) {
        Intrinsics.checkNotNullParameter(delegate, "delegate");
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        this.delegate = delegate;
        this.statusBar = statusBar;
        this.isLaunchForActivity = z;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
        if (!z) {
            this.statusBar.collapsePanelOnMainThread();
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
        this.statusBar.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        if (!z) {
            this.statusBar.collapsePanelWithDuration(500);
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationEnd(boolean z) {
        this.delegate.onLaunchAnimationEnd(z);
        this.statusBar.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.statusBar.onLaunchAnimationEnd(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationProgress(@NotNull ActivityLaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.delegate.onLaunchAnimationProgress(state, f, f2);
        this.statusBar.getNotificationPanelViewController().applyLaunchAnimationProgress(f2);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
        this.statusBar.onLaunchAnimationCancelled(this.isLaunchForActivity);
    }
}
