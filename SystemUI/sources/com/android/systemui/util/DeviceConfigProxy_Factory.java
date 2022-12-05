package com.android.systemui.util;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class DeviceConfigProxy_Factory implements Factory<DeviceConfigProxy> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public DeviceConfigProxy mo1933get() {
        return newInstance();
    }

    public static DeviceConfigProxy_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DeviceConfigProxy newInstance() {
        return new DeviceConfigProxy();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final DeviceConfigProxy_Factory INSTANCE = new DeviceConfigProxy_Factory();
    }
}
