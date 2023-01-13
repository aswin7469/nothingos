package com.android.systemui.statusbar.policy.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory */
public final class C3215xe335f1e6 implements Factory<String[]> {
    private final Provider<Resources> resourcesProvider;

    public C3215xe335f1e6(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public String[] get() {
        return providesDeviceStateRotationLockDefaults(this.resourcesProvider.get());
    }

    public static C3215xe335f1e6 create(Provider<Resources> provider) {
        return new C3215xe335f1e6(provider);
    }

    public static String[] providesDeviceStateRotationLockDefaults(Resources resources) {
        return (String[]) Preconditions.checkNotNullFromProvides(StatusBarPolicyModule.providesDeviceStateRotationLockDefaults(resources));
    }
}
