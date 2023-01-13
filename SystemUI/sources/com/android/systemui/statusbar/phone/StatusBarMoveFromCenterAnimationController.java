package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.WindowManager;
import com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.unfold.SysUIUnfoldScope;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0002\u0013\u0014B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\fJ\u0019\u0010\u000e\u001a\u00020\f2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010¢\u0006\u0002\u0010\u0012R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00060\nR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController;", "", "progressProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "windowManager", "Landroid/view/WindowManager;", "(Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;Landroid/view/WindowManager;)V", "moveFromCenterAnimator", "Lcom/android/systemui/shared/animation/UnfoldMoveFromCenterAnimator;", "transitionListener", "Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController$TransitionListener;", "onStatusBarWidthChanged", "", "onViewDetached", "onViewsReady", "viewsToAnimate", "", "Landroid/view/View;", "([Landroid/view/View;)V", "StatusBarIconsAlphaProvider", "TransitionListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@SysUIUnfoldScope
/* compiled from: StatusBarMoveFromCenterAnimationController.kt */
public final class StatusBarMoveFromCenterAnimationController {
    /* access modifiers changed from: private */
    public final UnfoldMoveFromCenterAnimator moveFromCenterAnimator;
    private final ScopedUnfoldTransitionProgressProvider progressProvider;
    private final TransitionListener transitionListener = new TransitionListener();

    @Inject
    public StatusBarMoveFromCenterAnimationController(ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, WindowManager windowManager) {
        Intrinsics.checkNotNullParameter(scopedUnfoldTransitionProgressProvider, "progressProvider");
        Intrinsics.checkNotNullParameter(windowManager, "windowManager");
        this.progressProvider = scopedUnfoldTransitionProgressProvider;
        this.moveFromCenterAnimator = new UnfoldMoveFromCenterAnimator(windowManager, (UnfoldMoveFromCenterAnimator.TranslationApplier) null, new PhoneStatusBarViewController.StatusBarViewsCenterProvider(), new StatusBarIconsAlphaProvider(), 2, (DefaultConstructorMarker) null);
    }

    public final void onViewsReady(View[] viewArr) {
        Intrinsics.checkNotNullParameter(viewArr, "viewsToAnimate");
        this.moveFromCenterAnimator.updateDisplayProperties();
        for (View registerViewForAnimation : viewArr) {
            this.moveFromCenterAnimator.registerViewForAnimation(registerViewForAnimation);
        }
        this.progressProvider.addCallback((UnfoldTransitionProgressProvider.TransitionProgressListener) this.transitionListener);
    }

    public final void onViewDetached() {
        this.progressProvider.removeCallback((UnfoldTransitionProgressProvider.TransitionProgressListener) this.transitionListener);
        this.moveFromCenterAnimator.clearRegisteredViews();
    }

    public final void onStatusBarWidthChanged() {
        this.moveFromCenterAnimator.updateDisplayProperties();
        this.moveFromCenterAnimator.updateViewPositions();
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController$TransitionListener;", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "(Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController;)V", "onTransitionFinished", "", "onTransitionProgress", "progress", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StatusBarMoveFromCenterAnimationController.kt */
    private final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionProgress(float f) {
            StatusBarMoveFromCenterAnimationController.this.moveFromCenterAnimator.onTransitionProgress(f);
        }

        public void onTransitionFinished() {
            StatusBarMoveFromCenterAnimationController.this.moveFromCenterAnimator.onTransitionProgress(1.0f);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController$StatusBarIconsAlphaProvider;", "Lcom/android/systemui/shared/animation/UnfoldMoveFromCenterAnimator$AlphaProvider;", "()V", "getAlpha", "", "progress", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StatusBarMoveFromCenterAnimationController.kt */
    private static final class StatusBarIconsAlphaProvider implements UnfoldMoveFromCenterAnimator.AlphaProvider {
        public float getAlpha(float f) {
            return Math.max(0.0f, (f - 0.75f) / 0.25f);
        }
    }
}
