package com.android.systemui.dagger;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideDisplayIdFactory implements Factory<Integer> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideDisplayIdFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public Integer mo1933get() {
        return Integer.valueOf(provideDisplayId(this.contextProvider.mo1933get()));
    }

    public static FrameworkServicesModule_ProvideDisplayIdFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideDisplayIdFactory(provider);
    }

    public static int provideDisplayId(Context context) {
        return FrameworkServicesModule.provideDisplayId(context);
    }
}
