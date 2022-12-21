package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory */
public final class WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory implements Factory<KidsModeTaskOrganizer> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    private final Provider<SyncTransactionQueue> syncTransactionQueueProvider;

    public WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory(Provider<ShellExecutor> provider, Provider<Handler> provider2, Provider<Context> provider3, Provider<SyncTransactionQueue> provider4, Provider<DisplayController> provider5, Provider<DisplayInsetsController> provider6, Provider<Optional<RecentTasksController>> provider7) {
        this.mainExecutorProvider = provider;
        this.mainHandlerProvider = provider2;
        this.contextProvider = provider3;
        this.syncTransactionQueueProvider = provider4;
        this.displayControllerProvider = provider5;
        this.displayInsetsControllerProvider = provider6;
        this.recentTasksOptionalProvider = provider7;
    }

    public KidsModeTaskOrganizer get() {
        return provideKidsModeTaskOrganizer(this.mainExecutorProvider.get(), this.mainHandlerProvider.get(), this.contextProvider.get(), this.syncTransactionQueueProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.recentTasksOptionalProvider.get());
    }

    public static WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory create(Provider<ShellExecutor> provider, Provider<Handler> provider2, Provider<Context> provider3, Provider<SyncTransactionQueue> provider4, Provider<DisplayController> provider5, Provider<DisplayInsetsController> provider6, Provider<Optional<RecentTasksController>> provider7) {
        return new WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static KidsModeTaskOrganizer provideKidsModeTaskOrganizer(ShellExecutor shellExecutor, Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional) {
        return (KidsModeTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideKidsModeTaskOrganizer(shellExecutor, handler, context, syncTransactionQueue, displayController, displayInsetsController, optional));
    }
}
