package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "delay", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.LockscreenShadeTransitionController$onDraggedDown$animationHandler$1 */
/* compiled from: LockscreenShadeTransitionController.kt */
final class C2564x21fef5f extends Lambda implements Function1<Long, Unit> {
    final /* synthetic */ View $startingChild;
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2564x21fef5f(View view, LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(1);
        this.$startingChild = view;
        this.this$0 = lockscreenShadeTransitionController;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).longValue());
        return Unit.INSTANCE;
    }

    public final void invoke(long j) {
        View view = this.$startingChild;
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).onExpandedByGesture(true);
        }
        this.this$0.getNotificationPanelController().animateToFullShade(j);
        this.this$0.getNotificationPanelController().setTransitionToFullShadeAmount(0.0f, true, j);
        this.this$0.forceApplyAmount = true;
        this.this$0.logger.logDragDownAmountReset();
        this.this$0.setDragDownAmount(0.0f);
        this.this$0.forceApplyAmount = false;
    }
}
