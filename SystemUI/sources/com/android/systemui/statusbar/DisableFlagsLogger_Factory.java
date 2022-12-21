package com.android.systemui.statusbar;

import dagger.internal.Factory;

public final class DisableFlagsLogger_Factory implements Factory<DisableFlagsLogger> {
    public DisableFlagsLogger get() {
        return newInstance();
    }

    public static DisableFlagsLogger_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DisableFlagsLogger newInstance() {
        return new DisableFlagsLogger();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DisableFlagsLogger_Factory INSTANCE = new DisableFlagsLogger_Factory();

        private InstanceHolder() {
        }
    }
}
