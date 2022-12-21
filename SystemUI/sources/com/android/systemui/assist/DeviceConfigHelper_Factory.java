package com.android.systemui.assist;

import dagger.internal.Factory;

public final class DeviceConfigHelper_Factory implements Factory<DeviceConfigHelper> {
    public DeviceConfigHelper get() {
        return newInstance();
    }

    public static DeviceConfigHelper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DeviceConfigHelper newInstance() {
        return new DeviceConfigHelper();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DeviceConfigHelper_Factory INSTANCE = new DeviceConfigHelper_Factory();

        private InstanceHolder() {
        }
    }
}
