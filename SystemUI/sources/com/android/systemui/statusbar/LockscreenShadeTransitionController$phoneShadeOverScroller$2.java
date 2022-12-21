package com.android.systemui.statusbar;

import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
final class LockscreenShadeTransitionController$phoneShadeOverScroller$2 extends Lambda implements Function0<SingleShadeLockScreenOverScroller> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    LockscreenShadeTransitionController$phoneShadeOverScroller$2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final SingleShadeLockScreenOverScroller invoke() {
        SingleShadeLockScreenOverScroller.Factory access$getSingleShadeOverScrollerFactory$p = this.this$0.singleShadeOverScrollerFactory;
        NotificationStackScrollLayoutController access$getNsslController$p = this.this$0.nsslController;
        if (access$getNsslController$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            access$getNsslController$p = null;
        }
        return access$getSingleShadeOverScrollerFactory$p.create(access$getNsslController$p);
    }
}
