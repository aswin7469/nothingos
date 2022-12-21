package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowControllerFactory */
public final class WMShellBaseModule_ProvideStartingWindowControllerFactory implements Factory<StartingWindowController> {
    private final Provider<Context> contextProvider;
    private final Provider<IconProvider> iconProvider;
    private final Provider<TransactionPool> poolProvider;
    private final Provider<ShellExecutor> splashScreenExecutorProvider;
    private final Provider<StartingWindowTypeAlgorithm> startingWindowTypeAlgorithmProvider;

    public WMShellBaseModule_ProvideStartingWindowControllerFactory(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<StartingWindowTypeAlgorithm> provider3, Provider<IconProvider> provider4, Provider<TransactionPool> provider5) {
        this.contextProvider = provider;
        this.splashScreenExecutorProvider = provider2;
        this.startingWindowTypeAlgorithmProvider = provider3;
        this.iconProvider = provider4;
        this.poolProvider = provider5;
    }

    public StartingWindowController get() {
        return provideStartingWindowController(this.contextProvider.get(), this.splashScreenExecutorProvider.get(), this.startingWindowTypeAlgorithmProvider.get(), this.iconProvider.get(), this.poolProvider.get());
    }

    public static WMShellBaseModule_ProvideStartingWindowControllerFactory create(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<StartingWindowTypeAlgorithm> provider3, Provider<IconProvider> provider4, Provider<TransactionPool> provider5) {
        return new WMShellBaseModule_ProvideStartingWindowControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static StartingWindowController provideStartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, IconProvider iconProvider2, TransactionPool transactionPool) {
        return (StartingWindowController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingWindowController(context, shellExecutor, startingWindowTypeAlgorithm, iconProvider2, transactionPool));
    }
}
