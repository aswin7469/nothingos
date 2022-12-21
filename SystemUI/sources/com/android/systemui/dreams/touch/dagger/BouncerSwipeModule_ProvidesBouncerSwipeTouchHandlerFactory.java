package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory implements Factory<DreamTouchHandler> {
    private final Provider<BouncerSwipeTouchHandler> touchHandlerProvider;

    public BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory(Provider<BouncerSwipeTouchHandler> provider) {
        this.touchHandlerProvider = provider;
    }

    public DreamTouchHandler get() {
        return providesBouncerSwipeTouchHandler(this.touchHandlerProvider.get());
    }

    public static BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory create(Provider<BouncerSwipeTouchHandler> provider) {
        return new BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory(provider);
    }

    public static DreamTouchHandler providesBouncerSwipeTouchHandler(BouncerSwipeTouchHandler bouncerSwipeTouchHandler) {
        return (DreamTouchHandler) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler));
    }
}
