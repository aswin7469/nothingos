package com.nothing.systemui.keyguard.calendar;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CalendarManager_Factory implements Factory<CalendarManager> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public CalendarManager_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    public CalendarManager get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static CalendarManager_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new CalendarManager_Factory(provider, provider2, provider3);
    }

    public static CalendarManager newInstance(Context context, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new CalendarManager(context, handler, keyguardUpdateMonitor);
    }
}
