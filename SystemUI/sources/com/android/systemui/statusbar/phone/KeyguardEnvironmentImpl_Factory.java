package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class KeyguardEnvironmentImpl_Factory implements Factory<KeyguardEnvironmentImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardEnvironmentImpl mo1933get() {
        return newInstance();
    }

    public static KeyguardEnvironmentImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static KeyguardEnvironmentImpl newInstance() {
        return new KeyguardEnvironmentImpl();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final KeyguardEnvironmentImpl_Factory INSTANCE = new KeyguardEnvironmentImpl_Factory();
    }
}
