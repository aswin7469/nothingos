package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\r"}, mo65043d2 = {"com/android/systemui/statusbar/NotificationShadeDepthController$statusBarStateCallback$1", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController$StateListener;", "onDozeAmountChanged", "", "linear", "", "eased", "onDozingChanged", "isDozing", "", "onStateChanged", "newState", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController$statusBarStateCallback$1 implements StatusBarStateController.StateListener {
    final /* synthetic */ NotificationShadeDepthController this$0;

    NotificationShadeDepthController$statusBarStateCallback$1(NotificationShadeDepthController notificationShadeDepthController) {
        this.this$0 = notificationShadeDepthController;
    }

    public void onStateChanged(int i) {
        NotificationShadeDepthController notificationShadeDepthController = this.this$0;
        notificationShadeDepthController.updateShadeAnimationBlur(notificationShadeDepthController.getShadeExpansion(), this.this$0.prevTracking, this.this$0.prevShadeVelocity, this.this$0.prevShadeDirection);
        NotificationShadeDepthController.scheduleUpdate$default(this.this$0, (View) null, 1, (Object) null);
    }

    public void onDozingChanged(boolean z) {
        if (z) {
            this.this$0.getShadeAnimation().finishIfRunning();
            this.this$0.getBrightnessMirrorSpring().finishIfRunning();
        }
    }

    public void onDozeAmountChanged(float f, float f2) {
        NotificationShadeDepthController notificationShadeDepthController = this.this$0;
        notificationShadeDepthController.setWakeAndUnlockBlurRadius(notificationShadeDepthController.blurUtils.blurRadiusOfRatio(f2));
        NotificationShadeDepthController.scheduleUpdate$default(this.this$0, (View) null, 1, (Object) null);
    }
}
