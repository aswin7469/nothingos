package com.nothing.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsHbmProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory implements Factory<UdfpsHbmProvider> {
    public UdfpsHbmProvider get() {
        return optionalUdfpsHbmProvider();
    }

    public static NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UdfpsHbmProvider optionalUdfpsHbmProvider() {
        return (UdfpsHbmProvider) Preconditions.checkNotNullFromProvides(NTUdfpsHbmModule.optionalUdfpsHbmProvider());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory INSTANCE = new NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory();

        private InstanceHolder() {
        }
    }
}
