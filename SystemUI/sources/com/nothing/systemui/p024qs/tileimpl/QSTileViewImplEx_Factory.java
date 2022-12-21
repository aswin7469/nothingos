package com.nothing.systemui.p024qs.tileimpl;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.tileimpl.QSTileViewImplEx_Factory */
public final class QSTileViewImplEx_Factory implements Factory<QSTileViewImplEx> {
    public QSTileViewImplEx get() {
        return newInstance();
    }

    public static QSTileViewImplEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSTileViewImplEx newInstance() {
        return new QSTileViewImplEx();
    }

    /* renamed from: com.nothing.systemui.qs.tileimpl.QSTileViewImplEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QSTileViewImplEx_Factory INSTANCE = new QSTileViewImplEx_Factory();

        private InstanceHolder() {
        }
    }
}
