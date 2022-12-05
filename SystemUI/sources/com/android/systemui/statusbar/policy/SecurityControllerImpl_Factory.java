package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SecurityControllerImpl_Factory implements Factory<SecurityControllerImpl> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;

    public SecurityControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.bgExecutorProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public SecurityControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.bgHandlerProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get(), this.bgExecutorProvider.mo1933get());
    }

    public static SecurityControllerImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4) {
        return new SecurityControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static SecurityControllerImpl newInstance(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        return new SecurityControllerImpl(context, handler, broadcastDispatcher, executor);
    }
}
