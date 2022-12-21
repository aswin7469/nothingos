package com.nothing.systemui.statusbar.notification.stack;

import dagger.internal.Factory;

public final class AmbientStateEx_Factory implements Factory<AmbientStateEx> {
    public AmbientStateEx get() {
        return newInstance();
    }

    public static AmbientStateEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static AmbientStateEx newInstance() {
        return new AmbientStateEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final AmbientStateEx_Factory INSTANCE = new AmbientStateEx_Factory();

        private InstanceHolder() {
        }
    }
}
