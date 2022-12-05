package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideSplitScreenControllerFactory implements Factory<Optional<SplitScreenController>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<RootTaskDisplayAreaOrganizer> rootTaskDisplayAreaOrganizerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideSplitScreenControllerFactory(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<Context> provider3, Provider<RootTaskDisplayAreaOrganizer> provider4, Provider<ShellExecutor> provider5, Provider<DisplayImeController> provider6, Provider<Transitions> provider7, Provider<TransactionPool> provider8) {
        this.shellTaskOrganizerProvider = provider;
        this.syncQueueProvider = provider2;
        this.contextProvider = provider3;
        this.rootTaskDisplayAreaOrganizerProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.displayImeControllerProvider = provider6;
        this.transitionsProvider = provider7;
        this.transactionPoolProvider = provider8;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<SplitScreenController> mo1933get() {
        return provideSplitScreenController(this.shellTaskOrganizerProvider.mo1933get(), this.syncQueueProvider.mo1933get(), this.contextProvider.mo1933get(), this.rootTaskDisplayAreaOrganizerProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.displayImeControllerProvider.mo1933get(), this.transitionsProvider.mo1933get(), this.transactionPoolProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideSplitScreenControllerFactory create(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<Context> provider3, Provider<RootTaskDisplayAreaOrganizer> provider4, Provider<ShellExecutor> provider5, Provider<DisplayImeController> provider6, Provider<Transitions> provider7, Provider<TransactionPool> provider8) {
        return new WMShellBaseModule_ProvideSplitScreenControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static Optional<SplitScreenController> provideSplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayImeController displayImeController, Transitions transitions, TransactionPool transactionPool) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSplitScreenController(shellTaskOrganizer, syncTransactionQueue, context, rootTaskDisplayAreaOrganizer, shellExecutor, displayImeController, transitions, transactionPool));
    }
}
