package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.transition.Transitions;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideCompatUIControllerFactory */
public final class WMShellBaseModule_ProvideCompatUIControllerFactory implements Factory<CompatUIController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    private final Provider<DisplayImeController> imeControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;
    private final Provider<Transitions> transitionsLazyProvider;

    public WMShellBaseModule_ProvideCompatUIControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayInsetsController> provider3, Provider<DisplayImeController> provider4, Provider<SyncTransactionQueue> provider5, Provider<ShellExecutor> provider6, Provider<Transitions> provider7) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.displayInsetsControllerProvider = provider3;
        this.imeControllerProvider = provider4;
        this.syncQueueProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.transitionsLazyProvider = provider7;
    }

    public CompatUIController get() {
        return provideCompatUIController(this.contextProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.imeControllerProvider.get(), this.syncQueueProvider.get(), this.mainExecutorProvider.get(), DoubleCheck.lazy(this.transitionsLazyProvider));
    }

    public static WMShellBaseModule_ProvideCompatUIControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayInsetsController> provider3, Provider<DisplayImeController> provider4, Provider<SyncTransactionQueue> provider5, Provider<ShellExecutor> provider6, Provider<Transitions> provider7) {
        return new WMShellBaseModule_ProvideCompatUIControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static CompatUIController provideCompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, ShellExecutor shellExecutor, Lazy<Transitions> lazy) {
        return (CompatUIController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideCompatUIController(context, displayController, displayInsetsController, displayImeController, syncTransactionQueue, shellExecutor, lazy));
    }
}
