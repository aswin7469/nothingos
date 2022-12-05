package com.android.systemui.dagger;

import android.view.IWindowManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIWindowManagerFactory implements Factory<IWindowManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IWindowManager mo1933get() {
        return provideIWindowManager();
    }

    public static FrameworkServicesModule_ProvideIWindowManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IWindowManager provideIWindowManager() {
        return (IWindowManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIWindowManager());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIWindowManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWindowManagerFactory();
    }
}
