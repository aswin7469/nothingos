package com.nothing.systemui.navigationbar;

import dagger.internal.Factory;

public final class NavigationModeControllerEx_Factory implements Factory<NavigationModeControllerEx> {
    public NavigationModeControllerEx get() {
        return newInstance();
    }

    public static NavigationModeControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NavigationModeControllerEx newInstance() {
        return new NavigationModeControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NavigationModeControllerEx_Factory INSTANCE = new NavigationModeControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
