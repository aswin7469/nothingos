package com.android.systemui.dagger;

import android.app.ActivityTaskManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideActivityTaskManagerFactory implements Factory<ActivityTaskManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ActivityTaskManager mo1933get() {
        return provideActivityTaskManager();
    }

    public static FrameworkServicesModule_ProvideActivityTaskManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ActivityTaskManager provideActivityTaskManager() {
        return (ActivityTaskManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideActivityTaskManager());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideActivityTaskManagerFactory INSTANCE = new FrameworkServicesModule_ProvideActivityTaskManagerFactory();
    }
}
