package com.android.systemui.statusbar.notification;

import android.animation.ValueAnimator;
import android.widget.ImageView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationIconDozeHelper$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ NotificationIconDozeHelper f$0;
    public final /* synthetic */ ImageView f$1;

    public /* synthetic */ NotificationIconDozeHelper$$ExternalSyntheticLambda0(NotificationIconDozeHelper notificationIconDozeHelper, ImageView imageView) {
        this.f$0 = notificationIconDozeHelper;
        this.f$1 = imageView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo39817xb841aef9(this.f$1, valueAnimator);
    }
}
