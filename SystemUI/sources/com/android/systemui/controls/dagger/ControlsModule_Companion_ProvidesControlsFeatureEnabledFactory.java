package com.android.systemui.controls.dagger;

import android.content.pm.PackageManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ControlsModule_Companion_ProvidesControlsFeatureEnabledFactory implements Factory<Boolean> {
    private final Provider<PackageManager> pmProvider;

    public ControlsModule_Companion_ProvidesControlsFeatureEnabledFactory(Provider<PackageManager> provider) {
        this.pmProvider = provider;
    }

    public Boolean get() {
        return Boolean.valueOf(providesControlsFeatureEnabled(this.pmProvider.get()));
    }

    public static ControlsModule_Companion_ProvidesControlsFeatureEnabledFactory create(Provider<PackageManager> provider) {
        return new ControlsModule_Companion_ProvidesControlsFeatureEnabledFactory(provider);
    }

    public static boolean providesControlsFeatureEnabled(PackageManager packageManager) {
        return ControlsModule.Companion.providesControlsFeatureEnabled(packageManager);
    }
}
