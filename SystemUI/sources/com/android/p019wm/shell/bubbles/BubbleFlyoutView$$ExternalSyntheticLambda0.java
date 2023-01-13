package com.android.p019wm.shell.bubbles;

import android.graphics.PointF;
import com.android.p019wm.shell.bubbles.Bubble;

/* renamed from: com.android.wm.shell.bubbles.BubbleFlyoutView$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleFlyoutView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BubbleFlyoutView f$0;
    public final /* synthetic */ Bubble.FlyoutMessage f$1;
    public final /* synthetic */ PointF f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ BubbleFlyoutView$$ExternalSyntheticLambda0(BubbleFlyoutView bubbleFlyoutView, Bubble.FlyoutMessage flyoutMessage, PointF pointF, boolean z) {
        this.f$0 = bubbleFlyoutView;
        this.f$1 = flyoutMessage;
        this.f$2 = pointF;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.mo48584xf07c2bb3(this.f$1, this.f$2, this.f$3);
    }
}
