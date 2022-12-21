package com.android.systemui.biometrics;

import android.view.View;
import com.android.systemui.recents.OverviewProxyService;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/biometrics/SidefpsController$overviewProxyListener$1", "Lcom/android/systemui/recents/OverviewProxyService$OverviewProxyListener;", "onTaskbarStatusUpdated", "", "visible", "", "stashed", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SidefpsController.kt */
public final class SidefpsController$overviewProxyListener$1 implements OverviewProxyService.OverviewProxyListener {
    final /* synthetic */ SidefpsController this$0;

    SidefpsController$overviewProxyListener$1(SidefpsController sidefpsController) {
        this.this$0 = sidefpsController;
    }

    public void onTaskbarStatusUpdated(boolean z, boolean z2) {
        View access$getOverlayView$p = this.this$0.overlayView;
        if (access$getOverlayView$p != null) {
            SidefpsController sidefpsController = this.this$0;
            sidefpsController.handler.postDelayed(new C1974x2428d60f(sidefpsController, access$getOverlayView$p), 500);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onTaskbarStatusUpdated$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2576onTaskbarStatusUpdated$lambda1$lambda0(SidefpsController sidefpsController, View view) {
        Intrinsics.checkNotNullParameter(sidefpsController, "this$0");
        Intrinsics.checkNotNullParameter(view, "$view");
        sidefpsController.updateOverlayVisibility(view);
    }
}
