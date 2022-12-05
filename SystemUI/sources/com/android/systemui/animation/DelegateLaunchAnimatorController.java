package com.android.systemui.animation;

import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: DelegateLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public class DelegateLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    private final ActivityLaunchAnimator.Controller delegate;

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
    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationProgress(@NotNull ActivityLaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.delegate.onLaunchAnimationProgress(state, f, f2);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.delegate.setLaunchContainer(viewGroup);
    }

    public DelegateLaunchAnimatorController(@NotNull ActivityLaunchAnimator.Controller delegate) {
        Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.delegate = delegate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NotNull
    public final ActivityLaunchAnimator.Controller getDelegate() {
        return this.delegate;
    }
}
