package com.android.systemui.util.p016io;

import dagger.internal.Factory;

/* renamed from: com.android.systemui.util.io.Files_Factory */
public final class Files_Factory implements Factory<Files> {
    public Files get() {
        return newInstance();
    }

    public static Files_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Files newInstance() {
        return new Files();
    }

    /* renamed from: com.android.systemui.util.io.Files_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final Files_Factory INSTANCE = new Files_Factory();

        private InstanceHolder() {
        }
    }
}
