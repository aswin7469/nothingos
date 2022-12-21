package com.android.systemui.statusbar.dagger;

import com.android.systemui.animation.ActivityLaunchAnimator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory */
public final class C2625x512569cf implements Factory<ActivityLaunchAnimator> {
    public ActivityLaunchAnimator get() {
        return provideActivityLaunchAnimator();
    }

    public static C2625x512569cf create() {
        return InstanceHolder.INSTANCE;
    }

    public static ActivityLaunchAnimator provideActivityLaunchAnimator() {
        return (ActivityLaunchAnimator) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideActivityLaunchAnimator());
    }

    /* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final C2625x512569cf INSTANCE = new C2625x512569cf();

        private InstanceHolder() {
        }
    }
}
