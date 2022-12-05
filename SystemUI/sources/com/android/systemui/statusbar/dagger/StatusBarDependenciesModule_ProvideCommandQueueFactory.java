package com.android.systemui.statusbar.dagger;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.tracing.ProtoTracer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideCommandQueueFactory implements Factory<CommandQueue> {
    private final Provider<Context> contextProvider;
    private final Provider<ProtoTracer> protoTracerProvider;
    private final Provider<CommandRegistry> registryProvider;

    public StatusBarDependenciesModule_ProvideCommandQueueFactory(Provider<Context> provider, Provider<ProtoTracer> provider2, Provider<CommandRegistry> provider3) {
        this.contextProvider = provider;
        this.protoTracerProvider = provider2;
        this.registryProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public CommandQueue mo1933get() {
        return provideCommandQueue(this.contextProvider.mo1933get(), this.protoTracerProvider.mo1933get(), this.registryProvider.mo1933get());
    }

    public static StatusBarDependenciesModule_ProvideCommandQueueFactory create(Provider<Context> provider, Provider<ProtoTracer> provider2, Provider<CommandRegistry> provider3) {
        return new StatusBarDependenciesModule_ProvideCommandQueueFactory(provider, provider2, provider3);
    }

    public static CommandQueue provideCommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        return (CommandQueue) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideCommandQueue(context, protoTracer, commandRegistry));
    }
}
