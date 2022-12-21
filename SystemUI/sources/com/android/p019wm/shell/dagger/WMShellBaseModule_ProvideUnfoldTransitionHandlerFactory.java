package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldTransitionHandler;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory */
public final class WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory implements Factory<Optional<UnfoldTransitionHandler>> {
    private final Provider<ShellExecutor> executorProvider;
    private final Provider<Optional<ShellUnfoldProgressProvider>> progressProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory(Provider<Optional<ShellUnfoldProgressProvider>> provider, Provider<TransactionPool> provider2, Provider<Transitions> provider3, Provider<ShellExecutor> provider4) {
        this.progressProvider = provider;
        this.transactionPoolProvider = provider2;
        this.transitionsProvider = provider3;
        this.executorProvider = provider4;
    }

    public Optional<UnfoldTransitionHandler> get() {
        return provideUnfoldTransitionHandler(this.progressProvider.get(), this.transactionPoolProvider.get(), this.transitionsProvider.get(), this.executorProvider.get());
    }

    public static WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory create(Provider<Optional<ShellUnfoldProgressProvider>> provider, Provider<TransactionPool> provider2, Provider<Transitions> provider3, Provider<ShellExecutor> provider4) {
        return new WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory(provider, provider2, provider3, provider4);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional, java.util.Optional<com.android.wm.shell.unfold.ShellUnfoldProgressProvider>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.p019wm.shell.unfold.UnfoldTransitionHandler> provideUnfoldTransitionHandler(java.util.Optional<com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider> r0, com.android.p019wm.shell.common.TransactionPool r1, com.android.p019wm.shell.transition.Transitions r2, com.android.p019wm.shell.common.ShellExecutor r3) {
        /*
            java.util.Optional r0 = com.android.p019wm.shell.dagger.WMShellBaseModule.provideUnfoldTransitionHandler(r0, r1, r2, r3)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory.provideUnfoldTransitionHandler(java.util.Optional, com.android.wm.shell.common.TransactionPool, com.android.wm.shell.transition.Transitions, com.android.wm.shell.common.ShellExecutor):java.util.Optional");
    }
}
