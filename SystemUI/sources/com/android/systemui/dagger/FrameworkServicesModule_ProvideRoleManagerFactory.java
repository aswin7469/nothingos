package com.android.systemui.dagger;

import android.app.role.RoleManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideRoleManagerFactory implements Factory<RoleManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideRoleManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public RoleManager get() {
        return provideRoleManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideRoleManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideRoleManagerFactory(provider);
    }

    public static RoleManager provideRoleManager(Context context) {
        return (RoleManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideRoleManager(context));
    }
}
