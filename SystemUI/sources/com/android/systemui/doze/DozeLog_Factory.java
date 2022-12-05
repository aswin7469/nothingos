package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeLog_Factory implements Factory<DozeLog> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<DozeLogger> loggerProvider;

    public DozeLog_Factory(Provider<KeyguardUpdateMonitor> provider, Provider<DumpManager> provider2, Provider<DozeLogger> provider3) {
        this.keyguardUpdateMonitorProvider = provider;
        this.dumpManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DozeLog mo1933get() {
        return newInstance(this.keyguardUpdateMonitorProvider.mo1933get(), this.dumpManagerProvider.mo1933get(), this.loggerProvider.mo1933get());
    }

    public static DozeLog_Factory create(Provider<KeyguardUpdateMonitor> provider, Provider<DumpManager> provider2, Provider<DozeLogger> provider3) {
        return new DozeLog_Factory(provider, provider2, provider3);
    }

    public static DozeLog newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, DozeLogger dozeLogger) {
        return new DozeLog(keyguardUpdateMonitor, dumpManager, dozeLogger);
    }
}
