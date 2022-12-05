package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class InternetDialogFactory_Factory implements Factory<InternetDialogFactory> {
    private final Provider<Context> contextProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<InternetDialogController> internetDialogControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public InternetDialogFactory_Factory(Provider<Handler> provider, Provider<Executor> provider2, Provider<InternetDialogController> provider3, Provider<Context> provider4, Provider<UiEventLogger> provider5) {
        this.handlerProvider = provider;
        this.executorProvider = provider2;
        this.internetDialogControllerProvider = provider3;
        this.contextProvider = provider4;
        this.uiEventLoggerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public InternetDialogFactory mo1933get() {
        return newInstance(this.handlerProvider.mo1933get(), this.executorProvider.mo1933get(), this.internetDialogControllerProvider.mo1933get(), this.contextProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get());
    }

    public static InternetDialogFactory_Factory create(Provider<Handler> provider, Provider<Executor> provider2, Provider<InternetDialogController> provider3, Provider<Context> provider4, Provider<UiEventLogger> provider5) {
        return new InternetDialogFactory_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static InternetDialogFactory newInstance(Handler handler, Executor executor, InternetDialogController internetDialogController, Context context, UiEventLogger uiEventLogger) {
        return new InternetDialogFactory(handler, executor, internetDialogController, context, uiEventLogger);
    }
}
