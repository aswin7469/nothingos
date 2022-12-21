package com.nothing.systemui.statusbar.policy;

import dagger.internal.Factory;

public final class BatteryControllerImplEx_Factory implements Factory<BatteryControllerImplEx> {
    public BatteryControllerImplEx get() {
        return newInstance();
    }

    public static BatteryControllerImplEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BatteryControllerImplEx newInstance() {
        return new BatteryControllerImplEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BatteryControllerImplEx_Factory INSTANCE = new BatteryControllerImplEx_Factory();

        private InstanceHolder() {
        }
    }
}
