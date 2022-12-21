package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipTransitionState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionStateFactory */
public final class WMShellModule_ProvidePipTransitionStateFactory implements Factory<PipTransitionState> {
    public PipTransitionState get() {
        return providePipTransitionState();
    }

    public static WMShellModule_ProvidePipTransitionStateFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipTransitionState providePipTransitionState() {
        return (PipTransitionState) Preconditions.checkNotNullFromProvides(WMShellModule.providePipTransitionState());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionStateFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellModule_ProvidePipTransitionStateFactory INSTANCE = new WMShellModule_ProvidePipTransitionStateFactory();

        private InstanceHolder() {
        }
    }
}
