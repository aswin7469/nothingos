package com.nothing.keyguard;

import dagger.internal.Factory;

public final class LockIconViewControllerEx_Factory implements Factory<LockIconViewControllerEx> {
    public LockIconViewControllerEx get() {
        return newInstance();
    }

    public static LockIconViewControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static LockIconViewControllerEx newInstance() {
        return new LockIconViewControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final LockIconViewControllerEx_Factory INSTANCE = new LockIconViewControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
