package com.nothing.systemui.navigationbar;

import dagger.internal.Factory;

public final class NavigationBarControllerEx_Factory implements Factory<NavigationBarControllerEx> {
    public NavigationBarControllerEx get() {
        return newInstance();
    }

    public static NavigationBarControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NavigationBarControllerEx newInstance() {
        return new NavigationBarControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NavigationBarControllerEx_Factory INSTANCE = new NavigationBarControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
