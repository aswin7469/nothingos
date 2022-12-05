package com.android.systemui.keyguard;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ScreenLifecycle_Factory implements Factory<ScreenLifecycle> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ScreenLifecycle mo1933get() {
        return newInstance();
    }

    public static ScreenLifecycle_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ScreenLifecycle newInstance() {
        return new ScreenLifecycle();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ScreenLifecycle_Factory INSTANCE = new ScreenLifecycle_Factory();
    }
}
