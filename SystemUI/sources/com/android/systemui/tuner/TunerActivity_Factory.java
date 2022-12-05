package com.android.systemui.tuner;

import com.android.systemui.demomode.DemoModeController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TunerActivity_Factory implements Factory<TunerActivity> {
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public TunerActivity_Factory(Provider<DemoModeController> provider, Provider<TunerService> provider2) {
        this.demoModeControllerProvider = provider;
        this.tunerServiceProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TunerActivity mo1933get() {
        return newInstance(this.demoModeControllerProvider.mo1933get(), this.tunerServiceProvider.mo1933get());
    }

    public static TunerActivity_Factory create(Provider<DemoModeController> provider, Provider<TunerService> provider2) {
        return new TunerActivity_Factory(provider, provider2);
    }

    public static TunerActivity newInstance(DemoModeController demoModeController, TunerService tunerService) {
        return new TunerActivity(demoModeController, tunerService);
    }
}
