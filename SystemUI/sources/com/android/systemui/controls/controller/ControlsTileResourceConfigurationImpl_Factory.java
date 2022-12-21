package com.android.systemui.controls.controller;

import dagger.internal.Factory;

public final class ControlsTileResourceConfigurationImpl_Factory implements Factory<ControlsTileResourceConfigurationImpl> {
    public ControlsTileResourceConfigurationImpl get() {
        return newInstance();
    }

    public static ControlsTileResourceConfigurationImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ControlsTileResourceConfigurationImpl newInstance() {
        return new ControlsTileResourceConfigurationImpl();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final ControlsTileResourceConfigurationImpl_Factory INSTANCE = new ControlsTileResourceConfigurationImpl_Factory();

        private InstanceHolder() {
        }
    }
}
