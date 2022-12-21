package com.android.p019wm.shell.dagger;

import android.view.IWindowManager;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayImeControllerFactory */
public final class WMShellBaseModule_ProvideDisplayImeControllerFactory implements Factory<DisplayImeController> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<DisplayImeController>> overrideDisplayImeControllerProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideDisplayImeControllerFactory(Provider<Optional<DisplayImeController>> provider, Provider<IWindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5, Provider<TransactionPool> provider6) {
        this.overrideDisplayImeControllerProvider = provider;
        this.wmServiceProvider = provider2;
        this.displayControllerProvider = provider3;
        this.displayInsetsControllerProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.transactionPoolProvider = provider6;
    }

    public DisplayImeController get() {
        return provideDisplayImeController(this.overrideDisplayImeControllerProvider.get(), this.wmServiceProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.mainExecutorProvider.get(), this.transactionPoolProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayImeControllerFactory create(Provider<Optional<DisplayImeController>> provider, Provider<IWindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5, Provider<TransactionPool> provider6) {
        return new WMShellBaseModule_ProvideDisplayImeControllerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DisplayImeController provideDisplayImeController(Optional<DisplayImeController> optional, IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        return (DisplayImeController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayImeController(optional, iWindowManager, displayController, displayInsetsController, shellExecutor, transactionPool));
    }
}
