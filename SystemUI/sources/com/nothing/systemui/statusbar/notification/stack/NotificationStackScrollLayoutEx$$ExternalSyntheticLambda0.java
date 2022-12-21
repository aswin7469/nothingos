package com.nothing.systemui.statusbar.notification.stack;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayoutEx$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ NotificationStackScrollLayoutEx f$0;

    public /* synthetic */ NotificationStackScrollLayoutEx$$ExternalSyntheticLambda0(NotificationStackScrollLayoutEx notificationStackScrollLayoutEx) {
        this.f$0 = notificationStackScrollLayoutEx;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        NotificationStackScrollLayoutEx.m3528resetScrollAnimation$lambda0(this.f$0, valueAnimator);
    }
}
