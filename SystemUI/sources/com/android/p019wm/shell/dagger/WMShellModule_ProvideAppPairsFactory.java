package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvideAppPairsFactory */
public final class WMShellModule_ProvideAppPairsFactory implements Factory<AppPairsController> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellModule_ProvideAppPairsFactory(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<DisplayController> provider3, Provider<ShellExecutor> provider4, Provider<DisplayImeController> provider5, Provider<DisplayInsetsController> provider6) {
        this.shellTaskOrganizerProvider = provider;
        this.syncQueueProvider = provider2;
        this.displayControllerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.displayImeControllerProvider = provider5;
        this.displayInsetsControllerProvider = provider6;
    }

    public AppPairsController get() {
        return provideAppPairs(this.shellTaskOrganizerProvider.get(), this.syncQueueProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get(), this.displayImeControllerProvider.get(), this.displayInsetsControllerProvider.get());
    }

    public static WMShellModule_ProvideAppPairsFactory create(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<DisplayController> provider3, Provider<ShellExecutor> provider4, Provider<DisplayImeController> provider5, Provider<DisplayInsetsController> provider6) {
        return new WMShellModule_ProvideAppPairsFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static AppPairsController provideAppPairs(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController) {
        return (AppPairsController) Preconditions.checkNotNullFromProvides(WMShellModule.provideAppPairs(shellTaskOrganizer, syncTransactionQueue, displayController, shellExecutor, displayImeController, displayInsetsController));
    }
}
