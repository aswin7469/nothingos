package com.android.systemui.assist.p009ui;

import android.content.Context;
import android.view.WindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.assist.ui.DefaultUiController_Factory */
public final class DefaultUiController_Factory implements Factory<DefaultUiController> {
    private final Provider<AssistLogger> assistLoggerProvider;
    private final Provider<AssistManager> assistManagerLazyProvider;
    private final Provider<Context> contextProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public DefaultUiController_Factory(Provider<Context> provider, Provider<AssistLogger> provider2, Provider<WindowManager> provider3, Provider<MetricsLogger> provider4, Provider<AssistManager> provider5) {
        this.contextProvider = provider;
        this.assistLoggerProvider = provider2;
        this.windowManagerProvider = provider3;
        this.metricsLoggerProvider = provider4;
        this.assistManagerLazyProvider = provider5;
    }

    public DefaultUiController get() {
        return newInstance(this.contextProvider.get(), this.assistLoggerProvider.get(), this.windowManagerProvider.get(), this.metricsLoggerProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider));
    }

    public static DefaultUiController_Factory create(Provider<Context> provider, Provider<AssistLogger> provider2, Provider<WindowManager> provider3, Provider<MetricsLogger> provider4, Provider<AssistManager> provider5) {
        return new DefaultUiController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static DefaultUiController newInstance(Context context, AssistLogger assistLogger, WindowManager windowManager, MetricsLogger metricsLogger, Lazy<AssistManager> lazy) {
        return new DefaultUiController(context, assistLogger, windowManager, metricsLogger, lazy);
    }
}
