package com.android.systemui.util.concurrency;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class GlobalConcurrencyModule_ProvideHandlerFactory implements Factory<Handler> {
    public Handler get() {
        return provideHandler();
    }

    public static GlobalConcurrencyModule_ProvideHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(GlobalConcurrencyModule.provideHandler());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final GlobalConcurrencyModule_ProvideHandlerFactory INSTANCE = new GlobalConcurrencyModule_ProvideHandlerFactory();

        private InstanceHolder() {
        }
    }
}
