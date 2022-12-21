package com.nothing.systemui.navigationbar.buttons;

import dagger.internal.Factory;

public final class KeyButtonViewEx_Factory implements Factory<KeyButtonViewEx> {
    public KeyButtonViewEx get() {
        return newInstance();
    }

    public static KeyButtonViewEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static KeyButtonViewEx newInstance() {
        return new KeyButtonViewEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final KeyButtonViewEx_Factory INSTANCE = new KeyButtonViewEx_Factory();

        private InstanceHolder() {
        }
    }
}
