package com.android.p019wm.shell.dagger;

import android.view.IWindowManager;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayInsetsControllerFactory */
public final class WMShellBaseModule_ProvideDisplayInsetsControllerFactory implements Factory<DisplayInsetsController> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideDisplayInsetsControllerFactory(Provider<IWindowManager> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        this.wmServiceProvider = provider;
        this.displayControllerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public DisplayInsetsController get() {
        return provideDisplayInsetsController(this.wmServiceProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayInsetsControllerFactory create(Provider<IWindowManager> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideDisplayInsetsControllerFactory(provider, provider2, provider3);
    }

    public static DisplayInsetsController provideDisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor) {
        return (DisplayInsetsController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayInsetsController(iWindowManager, displayController, shellExecutor));
    }
}
