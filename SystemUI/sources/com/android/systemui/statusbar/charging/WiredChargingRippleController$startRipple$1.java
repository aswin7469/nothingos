package com.android.systemui.statusbar.charging;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/statusbar/charging/WiredChargingRippleController$startRipple$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "view", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: WiredChargingRippleController.kt */
public final class WiredChargingRippleController$startRipple$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ WiredChargingRippleController this$0;

    public void onViewDetachedFromWindow(View view) {
    }

    WiredChargingRippleController$startRipple$1(WiredChargingRippleController wiredChargingRippleController) {
        this.this$0 = wiredChargingRippleController;
    }

    public void onViewAttachedToWindow(View view) {
        this.this$0.layoutRipple();
        this.this$0.getRippleView().startRipple(new C2609x366ed6a6(this.this$0));
        this.this$0.getRippleView().removeOnAttachStateChangeListener(this);
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttachedToWindow$lambda-0  reason: not valid java name */
    public static final void m3054onViewAttachedToWindow$lambda0(WiredChargingRippleController wiredChargingRippleController) {
        Intrinsics.checkNotNullParameter(wiredChargingRippleController, "this$0");
        wiredChargingRippleController.windowManager.removeView(wiredChargingRippleController.getRippleView());
    }
}
