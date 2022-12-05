package com.android.systemui.dagger;

import android.content.pm.IPackageManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIPackageManagerFactory implements Factory<IPackageManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IPackageManager mo1933get() {
        return provideIPackageManager();
    }

    public static FrameworkServicesModule_ProvideIPackageManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IPackageManager provideIPackageManager() {
        return (IPackageManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIPackageManager());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIPackageManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIPackageManagerFactory();
    }
}
