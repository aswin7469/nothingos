package com.nothing.systemui.p024qs.tileimpl;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.tileimpl.QSTileImplEx_Factory */
public final class QSTileImplEx_Factory implements Factory<QSTileImplEx> {
    public QSTileImplEx get() {
        return newInstance();
    }

    public static QSTileImplEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSTileImplEx newInstance() {
        return new QSTileImplEx();
    }

    /* renamed from: com.nothing.systemui.qs.tileimpl.QSTileImplEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QSTileImplEx_Factory INSTANCE = new QSTileImplEx_Factory();

        private InstanceHolder() {
        }
    }
}
