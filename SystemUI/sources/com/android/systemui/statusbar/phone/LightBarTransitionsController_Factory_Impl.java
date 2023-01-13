package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class LightBarTransitionsController_Factory_Impl implements LightBarTransitionsController.Factory {
    private final C4840LightBarTransitionsController_Factory delegateFactory;

    LightBarTransitionsController_Factory_Impl(C4840LightBarTransitionsController_Factory lightBarTransitionsController_Factory) {
        this.delegateFactory = lightBarTransitionsController_Factory;
    }

    public LightBarTransitionsController create(LightBarTransitionsController.DarkIntensityApplier darkIntensityApplier) {
        return this.delegateFactory.get(darkIntensityApplier);
    }

    public static Provider<LightBarTransitionsController.Factory> create(C4840LightBarTransitionsController_Factory lightBarTransitionsController_Factory) {
        return InstanceFactory.create(new LightBarTransitionsController_Factory_Impl(lightBarTransitionsController_Factory));
    }
}
