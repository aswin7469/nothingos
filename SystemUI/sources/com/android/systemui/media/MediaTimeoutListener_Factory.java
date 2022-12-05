package com.android.systemui.media;

import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaTimeoutListener_Factory implements Factory<MediaTimeoutListener> {
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<MediaControllerFactory> mediaControllerFactoryProvider;

    public MediaTimeoutListener_Factory(Provider<MediaControllerFactory> provider, Provider<DelayableExecutor> provider2) {
        this.mediaControllerFactoryProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public MediaTimeoutListener mo1933get() {
        return newInstance(this.mediaControllerFactoryProvider.mo1933get(), this.mainExecutorProvider.mo1933get());
    }

    public static MediaTimeoutListener_Factory create(Provider<MediaControllerFactory> provider, Provider<DelayableExecutor> provider2) {
        return new MediaTimeoutListener_Factory(provider, provider2);
    }

    public static MediaTimeoutListener newInstance(MediaControllerFactory mediaControllerFactory, DelayableExecutor delayableExecutor) {
        return new MediaTimeoutListener(mediaControllerFactory, delayableExecutor);
    }
}
