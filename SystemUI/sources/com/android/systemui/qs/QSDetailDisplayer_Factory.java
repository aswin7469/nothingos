package com.android.systemui.qs;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class QSDetailDisplayer_Factory implements Factory<QSDetailDisplayer> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSDetailDisplayer mo1933get() {
        return newInstance();
    }

    public static QSDetailDisplayer_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSDetailDisplayer newInstance() {
        return new QSDetailDisplayer();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final QSDetailDisplayer_Factory INSTANCE = new QSDetailDisplayer_Factory();
    }
}
