package com.android.systemui.toast;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ToastUI_Factory implements Factory<ToastUI> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ToastFactory> toastFactoryProvider;
    private final Provider<ToastLogger> toastLoggerProvider;

    public ToastUI_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ToastFactory> provider3, Provider<ToastLogger> provider4) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.toastFactoryProvider = provider3;
        this.toastLoggerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ToastUI mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.commandQueueProvider.mo1933get(), this.toastFactoryProvider.mo1933get(), this.toastLoggerProvider.mo1933get());
    }

    public static ToastUI_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ToastFactory> provider3, Provider<ToastLogger> provider4) {
        return new ToastUI_Factory(provider, provider2, provider3, provider4);
    }

    public static ToastUI newInstance(Context context, CommandQueue commandQueue, ToastFactory toastFactory, ToastLogger toastLogger) {
        return new ToastUI(context, commandQueue, toastFactory, toastLogger);
    }
}