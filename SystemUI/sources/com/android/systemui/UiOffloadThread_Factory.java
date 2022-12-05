package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class UiOffloadThread_Factory implements Factory<UiOffloadThread> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public UiOffloadThread mo1933get() {
        return newInstance();
    }

    public static UiOffloadThread_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UiOffloadThread newInstance() {
        return new UiOffloadThread();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final UiOffloadThread_Factory INSTANCE = new UiOffloadThread_Factory();
    }
}
