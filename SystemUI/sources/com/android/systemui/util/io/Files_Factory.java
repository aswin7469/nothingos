package com.android.systemui.util.io;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class Files_Factory implements Factory<Files> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Files mo1933get() {
        return newInstance();
    }

    public static Files_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Files newInstance() {
        return new Files();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final Files_Factory INSTANCE = new Files_Factory();
    }
}
