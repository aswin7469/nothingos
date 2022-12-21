package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideSyncTransactionQueueFactory */
public final class WMShellBaseModule_ProvideSyncTransactionQueueFactory implements Factory<SyncTransactionQueue> {
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<TransactionPool> poolProvider;

    public WMShellBaseModule_ProvideSyncTransactionQueueFactory(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        this.poolProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    public SyncTransactionQueue get() {
        return provideSyncTransactionQueue(this.poolProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideSyncTransactionQueueFactory create(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        return new WMShellBaseModule_ProvideSyncTransactionQueueFactory(provider, provider2);
    }

    public static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        return (SyncTransactionQueue) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSyncTransactionQueue(transactionPool, shellExecutor));
    }
}