package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DarkIconDispatcherImpl_Factory implements Factory<DarkIconDispatcherImpl> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;

    public DarkIconDispatcherImpl_Factory(Provider<Context> provider, Provider<CommandQueue> provider2) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DarkIconDispatcherImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.commandQueueProvider.mo1933get());
    }

    public static DarkIconDispatcherImpl_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2) {
        return new DarkIconDispatcherImpl_Factory(provider, provider2);
    }

    public static DarkIconDispatcherImpl newInstance(Context context, CommandQueue commandQueue) {
        return new DarkIconDispatcherImpl(context, commandQueue);
    }
}
