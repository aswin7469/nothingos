package com.android.systemui.statusbar.notification.row;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* renamed from: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2748x70846d1a implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SurfaceControl.Transaction f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ C2748x70846d1a(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        this.f$0 = transaction;
        this.f$1 = surfaceControl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ExpandableNotificationRowDragController.lambda$fadeOutAndRemoveDragSurface$1(this.f$0, this.f$1, valueAnimator);
    }
}
