package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class ExecutionImpl_Factory implements Factory<ExecutionImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ExecutionImpl mo1933get() {
        return newInstance();
    }

    public static ExecutionImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ExecutionImpl newInstance() {
        return new ExecutionImpl();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final ExecutionImpl_Factory INSTANCE = new ExecutionImpl_Factory();
    }
}
