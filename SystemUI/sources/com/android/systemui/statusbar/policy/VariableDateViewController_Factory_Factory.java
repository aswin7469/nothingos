package com.android.systemui.statusbar.policy;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VariableDateViewController_Factory_Factory implements Factory<VariableDateViewController.Factory> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public VariableDateViewController_Factory_Factory(Provider<SystemClock> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3) {
        this.systemClockProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.handlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public VariableDateViewController.Factory mo1933get() {
        return newInstance(this.systemClockProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get(), this.handlerProvider.mo1933get());
    }

    public static VariableDateViewController_Factory_Factory create(Provider<SystemClock> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3) {
        return new VariableDateViewController_Factory_Factory(provider, provider2, provider3);
    }

    public static VariableDateViewController.Factory newInstance(SystemClock systemClock, BroadcastDispatcher broadcastDispatcher, Handler handler) {
        return new VariableDateViewController.Factory(systemClock, broadcastDispatcher, handler);
    }
}
