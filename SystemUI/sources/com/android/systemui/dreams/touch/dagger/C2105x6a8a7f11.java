package com.android.systemui.dreams.touch.dagger;

import com.android.p019wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory */
public final class C2105x6a8a7f11 implements Factory<FlingAnimationUtils> {
    private final Provider<FlingAnimationUtils.Builder> flingAnimationUtilsBuilderProvider;

    public C2105x6a8a7f11(Provider<FlingAnimationUtils.Builder> provider) {
        this.flingAnimationUtilsBuilderProvider = provider;
    }

    public FlingAnimationUtils get() {
        return providesSwipeToBouncerFlingAnimationUtilsOpening(this.flingAnimationUtilsBuilderProvider);
    }

    public static C2105x6a8a7f11 create(Provider<FlingAnimationUtils.Builder> provider) {
        return new C2105x6a8a7f11(provider);
    }

    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsOpening(Provider<FlingAnimationUtils.Builder> provider) {
        return (FlingAnimationUtils) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesSwipeToBouncerFlingAnimationUtilsOpening(provider));
    }
}
