package com.android.systemui.flags;

import android.content.res.Resources;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class FeatureFlagsRelease_Factory implements Factory<FeatureFlagsRelease> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<SystemPropertiesHelper> systemPropertiesProvider;

    public FeatureFlagsRelease_Factory(Provider<Resources> provider, Provider<SystemPropertiesHelper> provider2, Provider<DumpManager> provider3) {
        this.resourcesProvider = provider;
        this.systemPropertiesProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public FeatureFlagsRelease get() {
        return newInstance(this.resourcesProvider.get(), this.systemPropertiesProvider.get(), this.dumpManagerProvider.get());
    }

    public static FeatureFlagsRelease_Factory create(Provider<Resources> provider, Provider<SystemPropertiesHelper> provider2, Provider<DumpManager> provider3) {
        return new FeatureFlagsRelease_Factory(provider, provider2, provider3);
    }

    public static FeatureFlagsRelease newInstance(Resources resources, SystemPropertiesHelper systemPropertiesHelper, DumpManager dumpManager) {
        return new FeatureFlagsRelease(resources, systemPropertiesHelper, dumpManager);
    }
}
