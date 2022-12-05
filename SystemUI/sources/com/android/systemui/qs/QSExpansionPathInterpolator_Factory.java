package com.android.systemui.qs;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class QSExpansionPathInterpolator_Factory implements Factory<QSExpansionPathInterpolator> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSExpansionPathInterpolator mo1933get() {
        return newInstance();
    }

    public static QSExpansionPathInterpolator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSExpansionPathInterpolator newInstance() {
        return new QSExpansionPathInterpolator();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final QSExpansionPathInterpolator_Factory INSTANCE = new QSExpansionPathInterpolator_Factory();
    }
}
