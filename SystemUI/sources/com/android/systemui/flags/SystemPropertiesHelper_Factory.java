package com.android.systemui.flags;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemPropertiesHelper_Factory implements Factory<SystemPropertiesHelper> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public SystemPropertiesHelper mo1933get() {
        return newInstance();
    }

    public static SystemPropertiesHelper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemPropertiesHelper newInstance() {
        return new SystemPropertiesHelper();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final SystemPropertiesHelper_Factory INSTANCE = new SystemPropertiesHelper_Factory();
    }
}
