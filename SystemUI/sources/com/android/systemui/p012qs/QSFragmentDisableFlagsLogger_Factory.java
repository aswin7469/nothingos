package com.android.systemui.p012qs;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.DisableFlagsLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSFragmentDisableFlagsLogger_Factory */
public final class QSFragmentDisableFlagsLogger_Factory implements Factory<QSFragmentDisableFlagsLogger> {
    private final Provider<LogBuffer> bufferProvider;
    private final Provider<DisableFlagsLogger> disableFlagsLoggerProvider;

    public QSFragmentDisableFlagsLogger_Factory(Provider<LogBuffer> provider, Provider<DisableFlagsLogger> provider2) {
        this.bufferProvider = provider;
        this.disableFlagsLoggerProvider = provider2;
    }

    public QSFragmentDisableFlagsLogger get() {
        return newInstance(this.bufferProvider.get(), this.disableFlagsLoggerProvider.get());
    }

    public static QSFragmentDisableFlagsLogger_Factory create(Provider<LogBuffer> provider, Provider<DisableFlagsLogger> provider2) {
        return new QSFragmentDisableFlagsLogger_Factory(provider, provider2);
    }

    public static QSFragmentDisableFlagsLogger newInstance(LogBuffer logBuffer, DisableFlagsLogger disableFlagsLogger) {
        return new QSFragmentDisableFlagsLogger(logBuffer, disableFlagsLogger);
    }
}
