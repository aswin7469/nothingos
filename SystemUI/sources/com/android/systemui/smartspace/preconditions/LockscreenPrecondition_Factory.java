package com.android.systemui.smartspace.preconditions;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockscreenPrecondition_Factory implements Factory<LockscreenPrecondition> {
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;

    public LockscreenPrecondition_Factory(Provider<FeatureFlags> provider, Provider<DeviceProvisionedController> provider2, Provider<Execution> provider3) {
        this.featureFlagsProvider = provider;
        this.deviceProvisionedControllerProvider = provider2;
        this.executionProvider = provider3;
    }

    public LockscreenPrecondition get() {
        return newInstance(this.featureFlagsProvider.get(), this.deviceProvisionedControllerProvider.get(), this.executionProvider.get());
    }

    public static LockscreenPrecondition_Factory create(Provider<FeatureFlags> provider, Provider<DeviceProvisionedController> provider2, Provider<Execution> provider3) {
        return new LockscreenPrecondition_Factory(provider, provider2, provider3);
    }

    public static LockscreenPrecondition newInstance(FeatureFlags featureFlags, DeviceProvisionedController deviceProvisionedController, Execution execution) {
        return new LockscreenPrecondition(featureFlags, deviceProvisionedController, execution);
    }
}
