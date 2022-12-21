package com.nothing.systemui.navigationbar;

import dagger.internal.Factory;

public final class NavigationBarEx_Factory implements Factory<NavigationBarEx> {
    public NavigationBarEx get() {
        return newInstance();
    }

    public static NavigationBarEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NavigationBarEx newInstance() {
        return new NavigationBarEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NavigationBarEx_Factory INSTANCE = new NavigationBarEx_Factory();

        private InstanceHolder() {
        }
    }
}
