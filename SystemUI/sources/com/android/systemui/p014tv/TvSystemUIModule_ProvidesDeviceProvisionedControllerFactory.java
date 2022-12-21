package com.android.systemui.p014tv;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.tv.TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory */
public final class TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory implements Factory<DeviceProvisionedController> {
    private final Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerProvider;

    public TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory(Provider<DeviceProvisionedControllerImpl> provider) {
        this.deviceProvisionedControllerProvider = provider;
    }

    public DeviceProvisionedController get() {
        return providesDeviceProvisionedController(this.deviceProvisionedControllerProvider.get());
    }

    public static TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory create(Provider<DeviceProvisionedControllerImpl> provider) {
        return new TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory(provider);
    }

    public static DeviceProvisionedController providesDeviceProvisionedController(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl) {
        return (DeviceProvisionedController) Preconditions.checkNotNullFromProvides(TvSystemUIModule.providesDeviceProvisionedController(deviceProvisionedControllerImpl));
    }
}
