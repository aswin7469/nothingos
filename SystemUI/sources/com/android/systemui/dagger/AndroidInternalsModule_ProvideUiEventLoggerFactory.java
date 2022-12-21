package com.android.systemui.dagger;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AndroidInternalsModule_ProvideUiEventLoggerFactory implements Factory<UiEventLogger> {
    public UiEventLogger get() {
        return provideUiEventLogger();
    }

    public static AndroidInternalsModule_ProvideUiEventLoggerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UiEventLogger provideUiEventLogger() {
        return (UiEventLogger) Preconditions.checkNotNullFromProvides(AndroidInternalsModule.provideUiEventLogger());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final AndroidInternalsModule_ProvideUiEventLoggerFactory INSTANCE = new AndroidInternalsModule_ProvideUiEventLoggerFactory();

        private InstanceHolder() {
        }
    }
}
