package com.android.systemui.biometrics;

import android.content.Context;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SidefpsControllerKt$$ExternalSyntheticLambda1 implements LottieOnCompositionLoadedListener {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ LottieAnimationView f$1;

    public /* synthetic */ SidefpsControllerKt$$ExternalSyntheticLambda1(Context context, LottieAnimationView lottieAnimationView) {
        this.f$0 = context;
        this.f$1 = lottieAnimationView;
    }

    public final void onCompositionLoaded(LottieComposition lottieComposition) {
        SidefpsControllerKt.m2583addOverlayDynamicColor$lambda1(this.f$0, this.f$1, lottieComposition);
    }
}
