package com.android.keyguard;

import com.android.systemui.C1894R;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardUnfoldTransition.kt */
final class KeyguardUnfoldTransition$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    final /* synthetic */ NaturalRotationUnfoldProgressProvider $unfoldProgressProvider;
    final /* synthetic */ KeyguardUnfoldTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    KeyguardUnfoldTransition$translateAnimator$2(KeyguardUnfoldTransition keyguardUnfoldTransition, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.this$0 = keyguardUnfoldTransition;
        this.$unfoldProgressProvider = naturalRotationUnfoldProgressProvider;
    }

    public final UnfoldConstantTranslateAnimator invoke() {
        return new UnfoldConstantTranslateAnimator(SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.keyguard_status_area, UnfoldConstantTranslateAnimator.Direction.LEFT, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.controls_button, UnfoldConstantTranslateAnimator.Direction.LEFT, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.lockscreen_clock_view_large, UnfoldConstantTranslateAnimator.Direction.LEFT, this.this$0.filterSplitShadeOnly), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.lockscreen_clock_view, UnfoldConstantTranslateAnimator.Direction.LEFT, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.notification_stack_scroller, UnfoldConstantTranslateAnimator.Direction.RIGHT, this.this$0.filterSplitShadeOnly), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1894R.C1898id.wallet_button, UnfoldConstantTranslateAnimator.Direction.RIGHT, this.this$0.filterNever)), this.$unfoldProgressProvider);
    }
}
