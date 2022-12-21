package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory */
public final class WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory implements Factory<PipSurfaceTransactionHelper> {
    public PipSurfaceTransactionHelper get() {
        return providePipSurfaceTransactionHelper();
    }

    public static WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return (PipSurfaceTransactionHelper) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipSurfaceTransactionHelper());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory INSTANCE = new WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory();

        private InstanceHolder() {
        }
    }
}
