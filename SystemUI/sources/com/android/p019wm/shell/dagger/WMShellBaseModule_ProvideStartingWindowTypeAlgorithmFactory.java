package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory */
public final class WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {
    private final Provider<Optional<StartingWindowTypeAlgorithm>> startingWindowTypeAlgorithmProvider;

    public WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory(Provider<Optional<StartingWindowTypeAlgorithm>> provider) {
        this.startingWindowTypeAlgorithmProvider = provider;
    }

    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm(this.startingWindowTypeAlgorithmProvider.get());
    }

    public static WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory create(Provider<Optional<StartingWindowTypeAlgorithm>> provider) {
        return new WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory(provider);
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm(Optional<StartingWindowTypeAlgorithm> optional) {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingWindowTypeAlgorithm(optional));
    }
}
