package com.android.systemui.classifier;

import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DistanceClassifier_Factory implements Factory<DistanceClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;

    public DistanceClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        this.dataProvider = provider;
        this.deviceConfigProxyProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DistanceClassifier mo1933get() {
        return newInstance(this.dataProvider.mo1933get(), this.deviceConfigProxyProvider.mo1933get());
    }

    public static DistanceClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        return new DistanceClassifier_Factory(provider, provider2);
    }

    public static DistanceClassifier newInstance(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        return new DistanceClassifier(falsingDataProvider, deviceConfigProxy);
    }
}
