package com.android.systemui.statusbar.notification.row;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChannelRow$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChannelRow f$0;

    public /* synthetic */ ChannelRow$$ExternalSyntheticLambda0(ChannelRow channelRow) {
        this.f$0 = channelRow;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChannelRow.m3144playHighlight$lambda3(this.f$0, valueAnimator);
    }
}
