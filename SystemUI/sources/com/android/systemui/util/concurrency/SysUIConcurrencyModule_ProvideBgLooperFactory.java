package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBgLooperFactory implements Factory<Looper> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Looper mo1933get() {
        return provideBgLooper();
    }

    public static SysUIConcurrencyModule_ProvideBgLooperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Looper provideBgLooper() {
        return (Looper) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBgLooper());
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final SysUIConcurrencyModule_ProvideBgLooperFactory INSTANCE = new SysUIConcurrencyModule_ProvideBgLooperFactory();
    }
}
