package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory */
public final class WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory implements Factory<ShellExecutor> {
    public ShellExecutor get() {
        return provideShellAnimationExecutor();
    }

    public static WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideShellAnimationExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellAnimationExecutor());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory();

        private InstanceHolder() {
        }
    }
}
