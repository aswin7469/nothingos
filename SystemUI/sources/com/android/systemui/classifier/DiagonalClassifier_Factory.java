package com.android.systemui.classifier;

import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DiagonalClassifier_Factory implements Factory<DiagonalClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;

    public DiagonalClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        this.dataProvider = provider;
        this.deviceConfigProxyProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DiagonalClassifier mo1933get() {
        return newInstance(this.dataProvider.mo1933get(), this.deviceConfigProxyProvider.mo1933get());
    }

    public static DiagonalClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        return new DiagonalClassifier_Factory(provider, provider2);
    }

    public static DiagonalClassifier newInstance(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        return new DiagonalClassifier(falsingDataProvider, deviceConfigProxy);
    }
}
