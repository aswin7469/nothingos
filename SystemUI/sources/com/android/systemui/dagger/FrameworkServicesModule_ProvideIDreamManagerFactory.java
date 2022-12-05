package com.android.systemui.dagger;

import android.service.dreams.IDreamManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIDreamManagerFactory implements Factory<IDreamManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IDreamManager mo1933get() {
        return provideIDreamManager();
    }

    public static FrameworkServicesModule_ProvideIDreamManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IDreamManager provideIDreamManager() {
        return (IDreamManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIDreamManager());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIDreamManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIDreamManagerFactory();
    }
}
