package com.nothing.systemui.p024qs;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.QSFragmentEx_Factory */
public final class QSFragmentEx_Factory implements Factory<QSFragmentEx> {
    public QSFragmentEx get() {
        return newInstance();
    }

    public static QSFragmentEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSFragmentEx newInstance() {
        return new QSFragmentEx();
    }

    /* renamed from: com.nothing.systemui.qs.QSFragmentEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QSFragmentEx_Factory INSTANCE = new QSFragmentEx_Factory();

        private InstanceHolder() {
        }
    }
}
