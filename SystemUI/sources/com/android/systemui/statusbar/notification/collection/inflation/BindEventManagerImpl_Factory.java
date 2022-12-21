package com.android.systemui.statusbar.notification.collection.inflation;

import dagger.internal.Factory;

public final class BindEventManagerImpl_Factory implements Factory<BindEventManagerImpl> {
    public BindEventManagerImpl get() {
        return newInstance();
    }

    public static BindEventManagerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BindEventManagerImpl newInstance() {
        return new BindEventManagerImpl();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BindEventManagerImpl_Factory INSTANCE = new BindEventManagerImpl_Factory();

        private InstanceHolder() {
        }
    }
}
