package com.android.systemui.statusbar;

import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
final class LockscreenShadeTransitionController$splitShadeOverScroller$2 extends Lambda implements Function0<SplitShadeLockScreenOverScroller> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    LockscreenShadeTransitionController$splitShadeOverScroller$2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final SplitShadeLockScreenOverScroller invoke() {
        SplitShadeLockScreenOverScroller.Factory access$getSplitShadeOverScrollerFactory$p = this.this$0.splitShadeOverScrollerFactory;
        C2304QS qs = this.this$0.getQS();
        NotificationStackScrollLayoutController access$getNsslController$p = this.this$0.nsslController;
        if (access$getNsslController$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            access$getNsslController$p = null;
        }
        return access$getSplitShadeOverScrollerFactory$p.create(qs, access$getNsslController$p);
    }
}
