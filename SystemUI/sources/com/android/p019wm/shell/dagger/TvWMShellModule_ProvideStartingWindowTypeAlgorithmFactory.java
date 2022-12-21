package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory */
public final class TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {
    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm();
    }

    public static TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(TvWMShellModule.provideStartingWindowTypeAlgorithm());
    }

    /* renamed from: com.android.wm.shell.dagger.TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory INSTANCE = new TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory();

        private InstanceHolder() {
        }
    }
}
