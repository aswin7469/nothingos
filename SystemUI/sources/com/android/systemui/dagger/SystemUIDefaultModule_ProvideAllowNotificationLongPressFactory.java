package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Boolean mo1933get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return SystemUIDefaultModule.provideAllowNotificationLongPress();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory INSTANCE = new SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory();
    }
}
