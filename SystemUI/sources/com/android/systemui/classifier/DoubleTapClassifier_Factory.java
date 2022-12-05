package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DoubleTapClassifier_Factory implements Factory<DoubleTapClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;
    private final Provider<Float> doubleTapSlopProvider;
    private final Provider<Long> doubleTapTimeMsProvider;
    private final Provider<SingleTapClassifier> singleTapClassifierProvider;

    public DoubleTapClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<SingleTapClassifier> provider2, Provider<Float> provider3, Provider<Long> provider4) {
        this.dataProvider = provider;
        this.singleTapClassifierProvider = provider2;
        this.doubleTapSlopProvider = provider3;
        this.doubleTapTimeMsProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DoubleTapClassifier mo1933get() {
        return newInstance(this.dataProvider.mo1933get(), this.singleTapClassifierProvider.mo1933get(), this.doubleTapSlopProvider.mo1933get().floatValue(), this.doubleTapTimeMsProvider.mo1933get().longValue());
    }

    public static DoubleTapClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<SingleTapClassifier> provider2, Provider<Float> provider3, Provider<Long> provider4) {
        return new DoubleTapClassifier_Factory(provider, provider2, provider3, provider4);
    }

    public static DoubleTapClassifier newInstance(FalsingDataProvider falsingDataProvider, SingleTapClassifier singleTapClassifier, float f, long j) {
        return new DoubleTapClassifier(falsingDataProvider, singleTapClassifier, f, j);
    }
}
