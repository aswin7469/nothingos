package com.android.systemui.dagger;

import dagger.internal.Factory;

public final class ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {
    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return ReferenceSystemUIModule.provideAllowNotificationLongPress();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory INSTANCE = new ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory();

        private InstanceHolder() {
        }
    }
}
