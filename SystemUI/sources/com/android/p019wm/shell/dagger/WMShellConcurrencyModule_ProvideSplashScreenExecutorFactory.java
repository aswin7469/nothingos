package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory */
public final class WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory implements Factory<ShellExecutor> {
    public ShellExecutor get() {
        return provideSplashScreenExecutor();
    }

    public static WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideSplashScreenExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSplashScreenExecutor());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory();

        private InstanceHolder() {
        }
    }
}
