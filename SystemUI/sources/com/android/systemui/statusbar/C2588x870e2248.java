package com.android.systemui.statusbar;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00030\u0001J\u0016\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0018\u00010\u0002R\u00020\u0003H\u0016J\u001e\u0010\u0007\u001a\u00020\b2\f\u0010\u0006\u001a\b\u0018\u00010\u0002R\u00020\u00032\u0006\u0010\t\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation$springAnimation$1", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation;", "Lcom/android/systemui/statusbar/NotificationShadeDepthController;", "getValue", "", "rect", "setValue", "", "value", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.NotificationShadeDepthController$DepthAnimation$springAnimation$1 */
/* compiled from: NotificationShadeDepthController.kt */
public final class C2588x870e2248 extends FloatPropertyCompat<NotificationShadeDepthController.DepthAnimation> {
    final /* synthetic */ NotificationShadeDepthController.DepthAnimation this$0;
    final /* synthetic */ NotificationShadeDepthController this$1;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2588x870e2248(NotificationShadeDepthController.DepthAnimation depthAnimation, NotificationShadeDepthController notificationShadeDepthController) {
        super("blurRadius");
        this.this$0 = depthAnimation;
        this.this$1 = notificationShadeDepthController;
    }

    public void setValue(NotificationShadeDepthController.DepthAnimation depthAnimation, float f) {
        this.this$0.setRadius(f);
        this.this$1.scheduleUpdate(this.this$0.view);
    }

    public float getValue(NotificationShadeDepthController.DepthAnimation depthAnimation) {
        return this.this$0.getRadius();
    }
}
