package com.nothing.systemui.navigationbar;

import dagger.internal.Factory;

public final class NavigationBarViewEx_Factory implements Factory<NavigationBarViewEx> {
    public NavigationBarViewEx get() {
        return newInstance();
    }

    public static NavigationBarViewEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NavigationBarViewEx newInstance() {
        return new NavigationBarViewEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NavigationBarViewEx_Factory INSTANCE = new NavigationBarViewEx_Factory();

        private InstanceHolder() {
        }
    }
}
