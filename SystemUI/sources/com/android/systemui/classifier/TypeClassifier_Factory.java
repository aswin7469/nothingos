package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TypeClassifier_Factory implements Factory<TypeClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;

    public TypeClassifier_Factory(Provider<FalsingDataProvider> provider) {
        this.dataProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TypeClassifier mo1933get() {
        return newInstance(this.dataProvider.mo1933get());
    }

    public static TypeClassifier_Factory create(Provider<FalsingDataProvider> provider) {
        return new TypeClassifier_Factory(provider);
    }

    public static TypeClassifier newInstance(FalsingDataProvider falsingDataProvider) {
        return new TypeClassifier(falsingDataProvider);
    }
}
