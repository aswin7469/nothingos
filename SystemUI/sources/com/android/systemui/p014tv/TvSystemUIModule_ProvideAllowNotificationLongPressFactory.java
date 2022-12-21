package com.android.systemui.p014tv;

import dagger.internal.Factory;

/* renamed from: com.android.systemui.tv.TvSystemUIModule_ProvideAllowNotificationLongPressFactory */
public final class TvSystemUIModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {
    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static TvSystemUIModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return TvSystemUIModule.provideAllowNotificationLongPress();
    }

    /* renamed from: com.android.systemui.tv.TvSystemUIModule_ProvideAllowNotificationLongPressFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvSystemUIModule_ProvideAllowNotificationLongPressFactory INSTANCE = new TvSystemUIModule_ProvideAllowNotificationLongPressFactory();

        private InstanceHolder() {
        }
    }
}
