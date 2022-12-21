package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory implements Factory<BouncerSwipeTouchHandler.ValueAnimatorCreator> {
    public BouncerSwipeTouchHandler.ValueAnimatorCreator get() {
        return providesValueAnimatorCreator();
    }

    public static BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BouncerSwipeTouchHandler.ValueAnimatorCreator providesValueAnimatorCreator() {
        return (BouncerSwipeTouchHandler.ValueAnimatorCreator) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesValueAnimatorCreator());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory INSTANCE = new BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory();

        private InstanceHolder() {
        }
    }
}
