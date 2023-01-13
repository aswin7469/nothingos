package com.android.systemui.biometrics;

import android.view.Display;
import android.view.View;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SidefpsController$$ExternalSyntheticLambda0 implements LottieOnCompositionLoadedListener {
    public final /* synthetic */ SidefpsController f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ Display f$2;

    public /* synthetic */ SidefpsController$$ExternalSyntheticLambda0(SidefpsController sidefpsController, View view, Display display) {
        this.f$0 = sidefpsController;
        this.f$1 = view;
        this.f$2 = display;
    }

    public final void onCompositionLoaded(LottieComposition lottieComposition) {
        SidefpsController.m2578createOverlayForDisplay$lambda5(this.f$0, this.f$1, this.f$2, lottieComposition);
    }
}
