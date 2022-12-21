package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.DisableFlagsLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CollapsedStatusBarFragmentLogger_Factory implements Factory<CollapsedStatusBarFragmentLogger> {
    private final Provider<LogBuffer> bufferProvider;
    private final Provider<DisableFlagsLogger> disableFlagsLoggerProvider;

    public CollapsedStatusBarFragmentLogger_Factory(Provider<LogBuffer> provider, Provider<DisableFlagsLogger> provider2) {
        this.bufferProvider = provider;
        this.disableFlagsLoggerProvider = provider2;
    }

    public CollapsedStatusBarFragmentLogger get() {
        return newInstance(this.bufferProvider.get(), this.disableFlagsLoggerProvider.get());
    }

    public static CollapsedStatusBarFragmentLogger_Factory create(Provider<LogBuffer> provider, Provider<DisableFlagsLogger> provider2) {
        return new CollapsedStatusBarFragmentLogger_Factory(provider, provider2);
    }

    public static CollapsedStatusBarFragmentLogger newInstance(LogBuffer logBuffer, DisableFlagsLogger disableFlagsLogger) {
        return new CollapsedStatusBarFragmentLogger(logBuffer, disableFlagsLogger);
    }
}
