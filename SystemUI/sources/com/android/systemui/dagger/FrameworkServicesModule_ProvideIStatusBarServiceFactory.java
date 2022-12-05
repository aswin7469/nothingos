package com.android.systemui.dagger;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIStatusBarServiceFactory implements Factory<IStatusBarService> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IStatusBarService mo1933get() {
        return provideIStatusBarService();
    }

    public static FrameworkServicesModule_ProvideIStatusBarServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IStatusBarService provideIStatusBarService() {
        return (IStatusBarService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIStatusBarService());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIStatusBarServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIStatusBarServiceFactory();
    }
}
