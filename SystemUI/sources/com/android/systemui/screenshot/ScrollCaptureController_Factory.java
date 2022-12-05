package com.android.systemui.screenshot;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrollCaptureController_Factory implements Factory<ScrollCaptureController> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<ScrollCaptureClient> clientProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ImageTileSet> imageTileSetProvider;
    private final Provider<UiEventLogger> loggerProvider;

    public ScrollCaptureController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4, Provider<UiEventLogger> provider5) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.clientProvider = provider3;
        this.imageTileSetProvider = provider4;
        this.loggerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ScrollCaptureController mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.bgExecutorProvider.mo1933get(), this.clientProvider.mo1933get(), this.imageTileSetProvider.mo1933get(), this.loggerProvider.mo1933get());
    }

    public static ScrollCaptureController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4, Provider<UiEventLogger> provider5) {
        return new ScrollCaptureController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ScrollCaptureController newInstance(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, Object obj, UiEventLogger uiEventLogger) {
        return new ScrollCaptureController(context, executor, scrollCaptureClient, (ImageTileSet) obj, uiEventLogger);
    }
}
