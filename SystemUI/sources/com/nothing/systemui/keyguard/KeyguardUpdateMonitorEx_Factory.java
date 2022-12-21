package com.nothing.systemui.keyguard;

import dagger.internal.Factory;

public final class KeyguardUpdateMonitorEx_Factory implements Factory<KeyguardUpdateMonitorEx> {
    public KeyguardUpdateMonitorEx get() {
        return newInstance();
    }

    public static KeyguardUpdateMonitorEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static KeyguardUpdateMonitorEx newInstance() {
        return new KeyguardUpdateMonitorEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final KeyguardUpdateMonitorEx_Factory INSTANCE = new KeyguardUpdateMonitorEx_Factory();

        private InstanceHolder() {
        }
    }
}
