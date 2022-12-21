package com.nothing.systemui.p024qs.tileimpl;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.tileimpl.QSIconViewImplEx_Factory */
public final class QSIconViewImplEx_Factory implements Factory<QSIconViewImplEx> {
    public QSIconViewImplEx get() {
        return newInstance();
    }

    public static QSIconViewImplEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSIconViewImplEx newInstance() {
        return new QSIconViewImplEx();
    }

    /* renamed from: com.nothing.systemui.qs.tileimpl.QSIconViewImplEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QSIconViewImplEx_Factory INSTANCE = new QSIconViewImplEx_Factory();

        private InstanceHolder() {
        }
    }
}
