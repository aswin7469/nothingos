package com.android.systemui.dagger;

import com.android.systemui.SystemUIFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SystemUIModule_GetSystemUIFactoryFactory implements Factory<SystemUIFactory> {
    public SystemUIFactory get() {
        return getSystemUIFactory();
    }

    public static SystemUIModule_GetSystemUIFactoryFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemUIFactory getSystemUIFactory() {
        return (SystemUIFactory) Preconditions.checkNotNullFromProvides(SystemUIModule.getSystemUIFactory());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final SystemUIModule_GetSystemUIFactoryFactory INSTANCE = new SystemUIModule_GetSystemUIFactoryFactory();

        private InstanceHolder() {
        }
    }
}
