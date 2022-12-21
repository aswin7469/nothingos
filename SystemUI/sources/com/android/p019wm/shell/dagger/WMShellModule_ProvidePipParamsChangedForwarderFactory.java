package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipParamsChangedForwarderFactory */
public final class WMShellModule_ProvidePipParamsChangedForwarderFactory implements Factory<PipParamsChangedForwarder> {
    public PipParamsChangedForwarder get() {
        return providePipParamsChangedForwarder();
    }

    public static WMShellModule_ProvidePipParamsChangedForwarderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return (PipParamsChangedForwarder) Preconditions.checkNotNullFromProvides(WMShellModule.providePipParamsChangedForwarder());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipParamsChangedForwarderFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellModule_ProvidePipParamsChangedForwarderFactory INSTANCE = new WMShellModule_ProvidePipParamsChangedForwarderFactory();

        private InstanceHolder() {
        }
    }
}
