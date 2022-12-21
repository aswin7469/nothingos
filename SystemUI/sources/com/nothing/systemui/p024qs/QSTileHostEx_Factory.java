package com.nothing.systemui.p024qs;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.QSTileHostEx_Factory */
public final class QSTileHostEx_Factory implements Factory<QSTileHostEx> {
    public QSTileHostEx get() {
        return newInstance();
    }

    public static QSTileHostEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSTileHostEx newInstance() {
        return new QSTileHostEx();
    }

    /* renamed from: com.nothing.systemui.qs.QSTileHostEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final QSTileHostEx_Factory INSTANCE = new QSTileHostEx_Factory();

        private InstanceHolder() {
        }
    }
}
