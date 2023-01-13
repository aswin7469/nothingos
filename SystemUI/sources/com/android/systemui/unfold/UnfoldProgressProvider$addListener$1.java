package com.android.systemui.unfold;

import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/unfold/UnfoldProgressProvider$addListener$1", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "onTransitionFinished", "", "onTransitionProgress", "progress", "", "onTransitionStarted", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UnfoldProgressProvider.kt */
public final class UnfoldProgressProvider$addListener$1 implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    final /* synthetic */ Executor $executor;
    final /* synthetic */ ShellUnfoldProgressProvider.UnfoldListener $listener;

    UnfoldProgressProvider$addListener$1(Executor executor, ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        this.$executor = executor;
        this.$listener = unfoldListener;
    }

    /* access modifiers changed from: private */
    /* renamed from: onTransitionStarted$lambda-0  reason: not valid java name */
    public static final void m3286onTransitionStarted$lambda0(ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        Intrinsics.checkNotNullParameter(unfoldListener, "$listener");
        unfoldListener.onStateChangeStarted();
    }

    public void onTransitionStarted() {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda0(this.$listener));
    }

    /* access modifiers changed from: private */
    /* renamed from: onTransitionProgress$lambda-1  reason: not valid java name */
    public static final void m3285onTransitionProgress$lambda1(ShellUnfoldProgressProvider.UnfoldListener unfoldListener, float f) {
        Intrinsics.checkNotNullParameter(unfoldListener, "$listener");
        unfoldListener.onStateChangeProgress(f);
    }

    public void onTransitionProgress(float f) {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda2(this.$listener, f));
    }

    /* access modifiers changed from: private */
    /* renamed from: onTransitionFinished$lambda-2  reason: not valid java name */
    public static final void m3284onTransitionFinished$lambda2(ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        Intrinsics.checkNotNullParameter(unfoldListener, "$listener");
        unfoldListener.onStateChangeFinished();
    }

    public void onTransitionFinished() {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda1(this.$listener));
    }
}
