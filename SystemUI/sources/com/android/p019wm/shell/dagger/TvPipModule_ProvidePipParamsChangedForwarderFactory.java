package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipParamsChangedForwarderFactory */
public final class TvPipModule_ProvidePipParamsChangedForwarderFactory implements Factory<PipParamsChangedForwarder> {
    public PipParamsChangedForwarder get() {
        return providePipParamsChangedForwarder();
    }

    public static TvPipModule_ProvidePipParamsChangedForwarderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return (PipParamsChangedForwarder) Preconditions.checkNotNullFromProvides(TvPipModule.providePipParamsChangedForwarder());
    }

    /* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipParamsChangedForwarderFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvPipModule_ProvidePipParamsChangedForwarderFactory INSTANCE = new TvPipModule_ProvidePipParamsChangedForwarderFactory();

        private InstanceHolder() {
        }
    }
}
