package com.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistLogger_Factory implements Factory<AssistLogger> {
    private final Provider<AssistUtils> assistUtilsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PhoneStateMonitor> phoneStateMonitorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public AssistLogger_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.phoneStateMonitorProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AssistLogger mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.assistUtilsProvider.mo1933get(), this.phoneStateMonitorProvider.mo1933get());
    }

    public static AssistLogger_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4) {
        return new AssistLogger_Factory(provider, provider2, provider3, provider4);
    }

    public static AssistLogger newInstance(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor) {
        return new AssistLogger(context, uiEventLogger, assistUtils, phoneStateMonitor);
    }
}
