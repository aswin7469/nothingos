package com.nothing.systemui.statusbar.connectivity;

import dagger.internal.Factory;

public final class WifiSignalControllerEx_Factory implements Factory<WifiSignalControllerEx> {
    public WifiSignalControllerEx get() {
        return newInstance();
    }

    public static WifiSignalControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static WifiSignalControllerEx newInstance() {
        return new WifiSignalControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WifiSignalControllerEx_Factory INSTANCE = new WifiSignalControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
