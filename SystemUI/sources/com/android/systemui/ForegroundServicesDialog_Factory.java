package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ForegroundServicesDialog_Factory implements Factory<ForegroundServicesDialog> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ForegroundServicesDialog mo1933get() {
        return newInstance();
    }

    public static ForegroundServicesDialog_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ForegroundServicesDialog newInstance() {
        return new ForegroundServicesDialog();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ForegroundServicesDialog_Factory INSTANCE = new ForegroundServicesDialog_Factory();
    }
}
