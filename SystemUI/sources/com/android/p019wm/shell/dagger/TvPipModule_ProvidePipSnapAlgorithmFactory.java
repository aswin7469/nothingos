package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipSnapAlgorithmFactory */
public final class TvPipModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {
    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static TvPipModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(TvPipModule.providePipSnapAlgorithm());
    }

    /* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipSnapAlgorithmFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvPipModule_ProvidePipSnapAlgorithmFactory INSTANCE = new TvPipModule_ProvidePipSnapAlgorithmFactory();

        private InstanceHolder() {
        }
    }
}
