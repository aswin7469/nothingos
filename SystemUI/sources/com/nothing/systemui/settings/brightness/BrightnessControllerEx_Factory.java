package com.nothing.systemui.settings.brightness;

import dagger.internal.Factory;

public final class BrightnessControllerEx_Factory implements Factory<BrightnessControllerEx> {
    public BrightnessControllerEx get() {
        return newInstance();
    }

    public static BrightnessControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BrightnessControllerEx newInstance() {
        return new BrightnessControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BrightnessControllerEx_Factory INSTANCE = new BrightnessControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
