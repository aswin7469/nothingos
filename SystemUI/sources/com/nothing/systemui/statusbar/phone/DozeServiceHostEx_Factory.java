package com.nothing.systemui.statusbar.phone;

import dagger.internal.Factory;

public final class DozeServiceHostEx_Factory implements Factory<DozeServiceHostEx> {
    public DozeServiceHostEx get() {
        return newInstance();
    }

    public static DozeServiceHostEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DozeServiceHostEx newInstance() {
        return new DozeServiceHostEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DozeServiceHostEx_Factory INSTANCE = new DozeServiceHostEx_Factory();

        private InstanceHolder() {
        }
    }
}
