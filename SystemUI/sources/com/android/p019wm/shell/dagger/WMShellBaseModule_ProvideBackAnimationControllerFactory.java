package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.back.BackAnimationController;
import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationControllerFactory */
public final class WMShellBaseModule_ProvideBackAnimationControllerFactory implements Factory<Optional<BackAnimationController>> {
    private final Provider<Handler> backgroundHandlerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> shellExecutorProvider;

    public WMShellBaseModule_ProvideBackAnimationControllerFactory(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.shellExecutorProvider = provider2;
        this.backgroundHandlerProvider = provider3;
    }

    public Optional<BackAnimationController> get() {
        return provideBackAnimationController(this.contextProvider.get(), this.shellExecutorProvider.get(), this.backgroundHandlerProvider.get());
    }

    public static WMShellBaseModule_ProvideBackAnimationControllerFactory create(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<Handler> provider3) {
        return new WMShellBaseModule_ProvideBackAnimationControllerFactory(provider, provider2, provider3);
    }

    public static Optional<BackAnimationController> provideBackAnimationController(Context context, ShellExecutor shellExecutor, Handler handler) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideBackAnimationController(context, shellExecutor, handler));
    }
}
