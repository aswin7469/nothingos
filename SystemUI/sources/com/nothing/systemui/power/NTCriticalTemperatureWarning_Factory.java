package com.nothing.systemui.power;

import dagger.internal.Factory;

public final class NTCriticalTemperatureWarning_Factory implements Factory<NTCriticalTemperatureWarning> {
    public NTCriticalTemperatureWarning get() {
        return newInstance();
    }

    public static NTCriticalTemperatureWarning_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NTCriticalTemperatureWarning newInstance() {
        return new NTCriticalTemperatureWarning();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NTCriticalTemperatureWarning_Factory INSTANCE = new NTCriticalTemperatureWarning_Factory();

        private InstanceHolder() {
        }
    }
}
