package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarIconControllerImpl_Factory implements Factory<StatusBarIconControllerImpl> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;

    public StatusBarIconControllerImpl_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<DemoModeController> provider3) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.demoModeControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public StatusBarIconControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.commandQueueProvider.mo1933get(), this.demoModeControllerProvider.mo1933get());
    }

    public static StatusBarIconControllerImpl_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<DemoModeController> provider3) {
        return new StatusBarIconControllerImpl_Factory(provider, provider2, provider3);
    }

    public static StatusBarIconControllerImpl newInstance(Context context, CommandQueue commandQueue, DemoModeController demoModeController) {
        return new StatusBarIconControllerImpl(context, commandQueue, demoModeController);
    }
}
