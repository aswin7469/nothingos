package com.nothing.systemui.navigationbar;

import dagger.internal.Factory;

public final class NavigationBarInflaterViewEx_Factory implements Factory<NavigationBarInflaterViewEx> {
    public NavigationBarInflaterViewEx get() {
        return newInstance();
    }

    public static NavigationBarInflaterViewEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NavigationBarInflaterViewEx newInstance() {
        return new NavigationBarInflaterViewEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NavigationBarInflaterViewEx_Factory INSTANCE = new NavigationBarInflaterViewEx_Factory();

        private InstanceHolder() {
        }
    }
}
