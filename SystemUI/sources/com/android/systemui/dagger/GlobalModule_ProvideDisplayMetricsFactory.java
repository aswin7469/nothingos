package com.android.systemui.dagger;

import android.content.Context;
import android.util.DisplayMetrics;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideDisplayMetricsFactory implements Factory<DisplayMetrics> {
    private final Provider<Context> contextProvider;
    private final GlobalModule module;

    public GlobalModule_ProvideDisplayMetricsFactory(GlobalModule globalModule, Provider<Context> provider) {
        this.module = globalModule;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DisplayMetrics mo1933get() {
        return provideDisplayMetrics(this.module, this.contextProvider.mo1933get());
    }

    public static GlobalModule_ProvideDisplayMetricsFactory create(GlobalModule globalModule, Provider<Context> provider) {
        return new GlobalModule_ProvideDisplayMetricsFactory(globalModule, provider);
    }

    public static DisplayMetrics provideDisplayMetrics(GlobalModule globalModule, Context context) {
        return (DisplayMetrics) Preconditions.checkNotNullFromProvides(globalModule.provideDisplayMetrics(context));
    }
}
