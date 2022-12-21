package com.nothing.systemui.p024qs;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.QuickStatusBarHeaderEx_Factory */
public final class QuickStatusBarHeaderEx_Factory implements Factory<QuickStatusBarHeaderEx> {
    public QuickStatusBarHeaderEx get() {
        return newInstance();
    }

    public static QuickStatusBarHeaderEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QuickStatusBarHeaderEx newInstance() {
        return new QuickStatusBarHeaderEx();
    }

    /* renamed from: com.nothing.systemui.qs.QuickStatusBarHeaderEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QuickStatusBarHeaderEx_Factory INSTANCE = new QuickStatusBarHeaderEx_Factory();

        private InstanceHolder() {
        }
    }
}
