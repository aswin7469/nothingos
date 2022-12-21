package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.p019wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvideSplitScreenControllerFactory */
public final class WMShellModule_ProvideSplitScreenControllerFactory implements Factory<SplitScreenController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    private final Provider<IconProvider> iconProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<RecentTasksController>> recentTasksProvider;
    private final Provider<RootTaskDisplayAreaOrganizer> rootTaskDisplayAreaOrganizerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<StageTaskUnfoldController>> stageTaskUnfoldControllerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellModule_ProvideSplitScreenControllerFactory(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<Context> provider3, Provider<RootTaskDisplayAreaOrganizer> provider4, Provider<ShellExecutor> provider5, Provider<DisplayController> provider6, Provider<DisplayImeController> provider7, Provider<DisplayInsetsController> provider8, Provider<Transitions> provider9, Provider<TransactionPool> provider10, Provider<IconProvider> provider11, Provider<Optional<RecentTasksController>> provider12, Provider<Optional<StageTaskUnfoldController>> provider13) {
        this.shellTaskOrganizerProvider = provider;
        this.syncQueueProvider = provider2;
        this.contextProvider = provider3;
        this.rootTaskDisplayAreaOrganizerProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.displayControllerProvider = provider6;
        this.displayImeControllerProvider = provider7;
        this.displayInsetsControllerProvider = provider8;
        this.transitionsProvider = provider9;
        this.transactionPoolProvider = provider10;
        this.iconProvider = provider11;
        this.recentTasksProvider = provider12;
        this.stageTaskUnfoldControllerProvider = provider13;
    }

    public SplitScreenController get() {
        return provideSplitScreenController(this.shellTaskOrganizerProvider.get(), this.syncQueueProvider.get(), this.contextProvider.get(), this.rootTaskDisplayAreaOrganizerProvider.get(), this.mainExecutorProvider.get(), this.displayControllerProvider.get(), this.displayImeControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.transitionsProvider.get(), this.transactionPoolProvider.get(), this.iconProvider.get(), this.recentTasksProvider.get(), this.stageTaskUnfoldControllerProvider);
    }

    public static WMShellModule_ProvideSplitScreenControllerFactory create(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<Context> provider3, Provider<RootTaskDisplayAreaOrganizer> provider4, Provider<ShellExecutor> provider5, Provider<DisplayController> provider6, Provider<DisplayImeController> provider7, Provider<DisplayInsetsController> provider8, Provider<Transitions> provider9, Provider<TransactionPool> provider10, Provider<IconProvider> provider11, Provider<Optional<RecentTasksController>> provider12, Provider<Optional<StageTaskUnfoldController>> provider13) {
        return new WMShellModule_ProvideSplitScreenControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static SplitScreenController provideSplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, IconProvider iconProvider2, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        return (SplitScreenController) Preconditions.checkNotNullFromProvides(WMShellModule.provideSplitScreenController(shellTaskOrganizer, syncTransactionQueue, context, rootTaskDisplayAreaOrganizer, shellExecutor, displayController, displayImeController, displayInsetsController, transitions, transactionPool, iconProvider2, optional, provider));
    }
}
