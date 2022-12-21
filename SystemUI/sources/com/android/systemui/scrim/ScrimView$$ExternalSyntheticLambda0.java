package com.android.systemui.scrim;

import com.android.internal.colorextraction.ColorExtractor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScrimView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ScrimView f$0;
    public final /* synthetic */ ColorExtractor.GradientColors f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ScrimView$$ExternalSyntheticLambda0(ScrimView scrimView, ColorExtractor.GradientColors gradientColors, boolean z) {
        this.f$0 = scrimView;
        this.f$1 = gradientColors;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.m3007lambda$setColors$3$comandroidsystemuiscrimScrimView(this.f$1, this.f$2);
    }
}
