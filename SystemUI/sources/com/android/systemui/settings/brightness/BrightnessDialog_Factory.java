package com.android.systemui.settings.brightness;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSlider;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightnessDialog_Factory implements Factory<BrightnessDialog> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<BrightnessSlider.Factory> factoryProvider;

    public BrightnessDialog_Factory(Provider<BroadcastDispatcher> provider, Provider<BrightnessSlider.Factory> provider2) {
        this.broadcastDispatcherProvider = provider;
        this.factoryProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public BrightnessDialog mo1933get() {
        return newInstance(this.broadcastDispatcherProvider.mo1933get(), this.factoryProvider.mo1933get());
    }

    public static BrightnessDialog_Factory create(Provider<BroadcastDispatcher> provider, Provider<BrightnessSlider.Factory> provider2) {
        return new BrightnessDialog_Factory(provider, provider2);
    }

    public static BrightnessDialog newInstance(BroadcastDispatcher broadcastDispatcher, BrightnessSlider.Factory factory) {
        return new BrightnessDialog(broadcastDispatcher, factory);
    }
}
