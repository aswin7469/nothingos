package com.android.systemui.dreams.touch.dagger;

import com.android.p019wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsClosingFactory */
public final class C2102x4f80e514 implements Factory<FlingAnimationUtils> {
    private final Provider<FlingAnimationUtils.Builder> flingAnimationUtilsBuilderProvider;

    public C2102x4f80e514(Provider<FlingAnimationUtils.Builder> provider) {
        this.flingAnimationUtilsBuilderProvider = provider;
    }

    public FlingAnimationUtils get() {
        return providesSwipeToBouncerFlingAnimationUtilsClosing(this.flingAnimationUtilsBuilderProvider);
    }

    public static C2102x4f80e514 create(Provider<FlingAnimationUtils.Builder> provider) {
        return new C2102x4f80e514(provider);
    }

    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsClosing(Provider<FlingAnimationUtils.Builder> provider) {
        return (FlingAnimationUtils) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesSwipeToBouncerFlingAnimationUtilsClosing(provider));
    }
}
