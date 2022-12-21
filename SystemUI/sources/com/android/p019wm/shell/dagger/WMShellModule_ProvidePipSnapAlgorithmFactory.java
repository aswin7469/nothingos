package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipSnapAlgorithmFactory */
public final class WMShellModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {
    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static WMShellModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.providePipSnapAlgorithm());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipSnapAlgorithmFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellModule_ProvidePipSnapAlgorithmFactory INSTANCE = new WMShellModule_ProvidePipSnapAlgorithmFactory();

        private InstanceHolder() {
        }
    }
}
