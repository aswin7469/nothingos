package com.android.systemui.assist;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AssistModule_ProvideBackgroundHandlerFactory implements Factory<Handler> {
    public Handler get() {
        return provideBackgroundHandler();
    }

    public static AssistModule_ProvideBackgroundHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideBackgroundHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(AssistModule.provideBackgroundHandler());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final AssistModule_ProvideBackgroundHandlerFactory INSTANCE = new AssistModule_ProvideBackgroundHandlerFactory();

        private InstanceHolder() {
        }
    }
}
