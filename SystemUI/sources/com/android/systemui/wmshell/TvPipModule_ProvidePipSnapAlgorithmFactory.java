package com.android.systemui.wmshell;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public PipSnapAlgorithm mo1933get() {
        return providePipSnapAlgorithm();
    }

    public static TvPipModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(TvPipModule.providePipSnapAlgorithm());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final TvPipModule_ProvidePipSnapAlgorithmFactory INSTANCE = new TvPipModule_ProvidePipSnapAlgorithmFactory();
    }
}
