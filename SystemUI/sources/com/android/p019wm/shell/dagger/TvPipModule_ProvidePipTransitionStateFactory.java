package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipTransitionState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipTransitionStateFactory */
public final class TvPipModule_ProvidePipTransitionStateFactory implements Factory<PipTransitionState> {
    public PipTransitionState get() {
        return providePipTransitionState();
    }

    public static TvPipModule_ProvidePipTransitionStateFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipTransitionState providePipTransitionState() {
        return (PipTransitionState) Preconditions.checkNotNullFromProvides(TvPipModule.providePipTransitionState());
    }

    /* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipTransitionStateFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvPipModule_ProvidePipTransitionStateFactory INSTANCE = new TvPipModule_ProvidePipTransitionStateFactory();

        private InstanceHolder() {
        }
    }
}
