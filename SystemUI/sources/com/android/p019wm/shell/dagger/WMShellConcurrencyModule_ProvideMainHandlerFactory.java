package com.android.p019wm.shell.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory */
public final class WMShellConcurrencyModule_ProvideMainHandlerFactory implements Factory<Handler> {
    public Handler get() {
        return provideMainHandler();
    }

    public static WMShellConcurrencyModule_ProvideMainHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideMainHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideMainHandler());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellConcurrencyModule_ProvideMainHandlerFactory INSTANCE = new WMShellConcurrencyModule_ProvideMainHandlerFactory();

        private InstanceHolder() {
        }
    }
}
