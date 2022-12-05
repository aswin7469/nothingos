package com.android.systemui.dump;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DumpManager_Factory implements Factory<DumpManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public DumpManager mo1933get() {
        return newInstance();
    }

    public static DumpManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DumpManager newInstance() {
        return new DumpManager();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final DumpManager_Factory INSTANCE = new DumpManager_Factory();
    }
}
