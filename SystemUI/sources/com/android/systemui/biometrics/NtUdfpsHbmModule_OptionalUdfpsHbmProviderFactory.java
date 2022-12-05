package com.android.systemui.biometrics;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class NtUdfpsHbmModule_OptionalUdfpsHbmProviderFactory implements Factory<UdfpsHbmProvider> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public UdfpsHbmProvider mo1933get() {
        return optionalUdfpsHbmProvider();
    }

    public static NtUdfpsHbmModule_OptionalUdfpsHbmProviderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UdfpsHbmProvider optionalUdfpsHbmProvider() {
        return (UdfpsHbmProvider) Preconditions.checkNotNullFromProvides(NtUdfpsHbmModule.optionalUdfpsHbmProvider());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final NtUdfpsHbmModule_OptionalUdfpsHbmProviderFactory INSTANCE = new NtUdfpsHbmModule_OptionalUdfpsHbmProviderFactory();
    }
}
