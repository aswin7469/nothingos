package com.android.systemui.statusbar.phone.shade.transition;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ShadeTransitionController.kt */
final class ShadeTransitionController$splitShadeOverScroller$2 extends Lambda implements Function0<SplitShadeOverScroller> {
    final /* synthetic */ ShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ShadeTransitionController$splitShadeOverScroller$2(ShadeTransitionController shadeTransitionController) {
        super(0);
        this.this$0 = shadeTransitionController;
    }

    public final SplitShadeOverScroller invoke() {
        return this.this$0.splitShadeOverScrollerFactory.create(this.this$0.getQs(), this.this$0.getNotificationStackScrollLayoutController());
    }
}
