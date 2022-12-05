package com.android.systemui.util.time;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class SystemClockImpl_Factory implements Factory<SystemClockImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public SystemClockImpl mo1933get() {
        return newInstance();
    }

    public static SystemClockImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemClockImpl newInstance() {
        return new SystemClockImpl();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final SystemClockImpl_Factory INSTANCE = new SystemClockImpl_Factory();
    }
}
