package com.android.systemui.wmshell;

import com.android.wm.shell.common.FloatingContentCoordinator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideFloatingContentCoordinatorFactory implements Factory<FloatingContentCoordinator> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public FloatingContentCoordinator mo1933get() {
        return provideFloatingContentCoordinator();
    }

    public static WMShellBaseModule_ProvideFloatingContentCoordinatorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return (FloatingContentCoordinator) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideFloatingContentCoordinator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellBaseModule_ProvideFloatingContentCoordinatorFactory INSTANCE = new WMShellBaseModule_ProvideFloatingContentCoordinatorFactory();
    }
}
