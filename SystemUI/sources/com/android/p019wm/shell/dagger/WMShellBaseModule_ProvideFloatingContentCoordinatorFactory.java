package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.FloatingContentCoordinator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory */
public final class WMShellBaseModule_ProvideFloatingContentCoordinatorFactory implements Factory<FloatingContentCoordinator> {
    public FloatingContentCoordinator get() {
        return provideFloatingContentCoordinator();
    }

    public static WMShellBaseModule_ProvideFloatingContentCoordinatorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return (FloatingContentCoordinator) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideFloatingContentCoordinator());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellBaseModule_ProvideFloatingContentCoordinatorFactory INSTANCE = new WMShellBaseModule_ProvideFloatingContentCoordinatorFactory();

        private InstanceHolder() {
        }
    }
}
