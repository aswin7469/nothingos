package com.android.systemui.dagger;

import dagger.internal.Factory;

public final class FrameworkServicesModule_ProvideIsTestHarnessFactory implements Factory<Boolean> {
    public Boolean get() {
        return Boolean.valueOf(provideIsTestHarness());
    }

    public static FrameworkServicesModule_ProvideIsTestHarnessFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideIsTestHarness() {
        return FrameworkServicesModule.provideIsTestHarness();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final FrameworkServicesModule_ProvideIsTestHarnessFactory INSTANCE = new FrameworkServicesModule_ProvideIsTestHarnessFactory();

        private InstanceHolder() {
        }
    }
}
