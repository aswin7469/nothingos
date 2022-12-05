package com.android.systemui.dock;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DockManagerImpl_Factory implements Factory<DockManagerImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public DockManagerImpl mo1933get() {
        return newInstance();
    }

    public static DockManagerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DockManagerImpl newInstance() {
        return new DockManagerImpl();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final DockManagerImpl_Factory INSTANCE = new DockManagerImpl_Factory();
    }
}
