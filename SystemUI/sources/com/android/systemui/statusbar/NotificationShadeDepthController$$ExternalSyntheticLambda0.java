package com.android.systemui.statusbar;

import android.view.Choreographer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationShadeDepthController$$ExternalSyntheticLambda0 implements Choreographer.FrameCallback {
    public final /* synthetic */ NotificationShadeDepthController f$0;

    public /* synthetic */ NotificationShadeDepthController$$ExternalSyntheticLambda0(NotificationShadeDepthController notificationShadeDepthController) {
        this.f$0 = notificationShadeDepthController;
    }

    public final void doFrame(long j) {
        NotificationShadeDepthController.m3038updateBlurCallback$lambda1(this.f$0, j);
    }
}
