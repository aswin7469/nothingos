package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory implements Factory<BouncerSwipeTouchHandler.VelocityTrackerFactory> {
    public BouncerSwipeTouchHandler.VelocityTrackerFactory get() {
        return providesVelocityTrackerFactory();
    }

    public static BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BouncerSwipeTouchHandler.VelocityTrackerFactory providesVelocityTrackerFactory() {
        return (BouncerSwipeTouchHandler.VelocityTrackerFactory) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesVelocityTrackerFactory());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory INSTANCE = new BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory();

        private InstanceHolder() {
        }
    }
}
