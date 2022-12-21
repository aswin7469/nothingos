package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipAppOpsListenerFactory */
public final class WMShellModule_ProvidePipAppOpsListenerFactory implements Factory<PipAppOpsListener> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipTouchHandler> pipTouchHandlerProvider;

    public WMShellModule_ProvidePipAppOpsListenerFactory(Provider<Context> provider, Provider<PipTouchHandler> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.pipTouchHandlerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public PipAppOpsListener get() {
        return providePipAppOpsListener(this.contextProvider.get(), this.pipTouchHandlerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvidePipAppOpsListenerFactory create(Provider<Context> provider, Provider<PipTouchHandler> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellModule_ProvidePipAppOpsListenerFactory(provider, provider2, provider3);
    }

    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTouchHandler pipTouchHandler, ShellExecutor shellExecutor) {
        return (PipAppOpsListener) Preconditions.checkNotNullFromProvides(WMShellModule.providePipAppOpsListener(context, pipTouchHandler, shellExecutor));
    }
}
