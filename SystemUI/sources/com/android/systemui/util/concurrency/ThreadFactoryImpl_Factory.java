package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class ThreadFactoryImpl_Factory implements Factory<ThreadFactoryImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ThreadFactoryImpl mo1933get() {
        return newInstance();
    }

    public static ThreadFactoryImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ThreadFactoryImpl newInstance() {
        return new ThreadFactoryImpl();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final ThreadFactoryImpl_Factory INSTANCE = new ThreadFactoryImpl_Factory();
    }
}
