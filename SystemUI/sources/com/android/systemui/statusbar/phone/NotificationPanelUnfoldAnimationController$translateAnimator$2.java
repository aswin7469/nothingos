package com.android.systemui.statusbar.phone;

import com.android.systemui.C1893R;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationPanelUnfoldAnimationController.kt */
final class NotificationPanelUnfoldAnimationController$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    final /* synthetic */ NaturalRotationUnfoldProgressProvider $progressProvider;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotificationPanelUnfoldAnimationController$translateAnimator$2(NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.$progressProvider = naturalRotationUnfoldProgressProvider;
    }

    public final UnfoldConstantTranslateAnimator invoke() {
        return new UnfoldConstantTranslateAnimator(SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1893R.C1897id.quick_settings_panel, UnfoldConstantTranslateAnimator.Direction.LEFT, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1893R.C1897id.notification_stack_scroller, UnfoldConstantTranslateAnimator.Direction.RIGHT, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1893R.C1897id.rightLayout, UnfoldConstantTranslateAnimator.Direction.RIGHT, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1893R.C1897id.clock, UnfoldConstantTranslateAnimator.Direction.LEFT, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(C1893R.C1897id.date, UnfoldConstantTranslateAnimator.Direction.LEFT, (Function0) null, 4, (DefaultConstructorMarker) null)), this.$progressProvider);
    }
}
