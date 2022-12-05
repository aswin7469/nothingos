package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideIsTestHarnessFactory implements Factory<Boolean> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Boolean mo1933get() {
        return Boolean.valueOf(provideIsTestHarness());
    }

    public static GlobalModule_ProvideIsTestHarnessFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideIsTestHarness() {
        return GlobalModule.provideIsTestHarness();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final GlobalModule_ProvideIsTestHarnessFactory INSTANCE = new GlobalModule_ProvideIsTestHarnessFactory();
    }
}
