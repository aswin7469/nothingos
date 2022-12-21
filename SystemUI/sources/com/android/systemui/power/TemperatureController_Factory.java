package com.android.systemui.power;

import dagger.internal.Factory;

public final class TemperatureController_Factory implements Factory<TemperatureController> {
    public TemperatureController get() {
        return newInstance();
    }

    public static TemperatureController_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TemperatureController newInstance() {
        return new TemperatureController();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TemperatureController_Factory INSTANCE = new TemperatureController_Factory();

        private InstanceHolder() {
        }
    }
}
