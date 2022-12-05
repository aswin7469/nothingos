package com.android.systemui.dagger;

import android.app.IActivityTaskManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIActivityTaskManagerFactory implements Factory<IActivityTaskManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IActivityTaskManager mo1933get() {
        return provideIActivityTaskManager();
    }

    public static FrameworkServicesModule_ProvideIActivityTaskManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IActivityTaskManager provideIActivityTaskManager() {
        return (IActivityTaskManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIActivityTaskManager());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIActivityTaskManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIActivityTaskManagerFactory();
    }
}
