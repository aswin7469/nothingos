package com.android.p019wm.shell.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory */
public final class WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory implements Factory<Handler> {
    public Handler get() {
        return provideSharedBackgroundHandler();
    }

    public static WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideSharedBackgroundHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSharedBackgroundHandler());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory INSTANCE = new WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory();

        private InstanceHolder() {
        }
    }
}
