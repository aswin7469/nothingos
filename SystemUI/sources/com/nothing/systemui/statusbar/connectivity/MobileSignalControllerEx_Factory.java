package com.nothing.systemui.statusbar.connectivity;

import dagger.internal.Factory;

public final class MobileSignalControllerEx_Factory implements Factory<MobileSignalControllerEx> {
    public MobileSignalControllerEx get() {
        return newInstance();
    }

    public static MobileSignalControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MobileSignalControllerEx newInstance() {
        return new MobileSignalControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final MobileSignalControllerEx_Factory INSTANCE = new MobileSignalControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
