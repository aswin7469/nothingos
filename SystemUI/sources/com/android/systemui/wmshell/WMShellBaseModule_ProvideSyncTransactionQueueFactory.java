package com.android.systemui.wmshell;

import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideSyncTransactionQueueFactory implements Factory<SyncTransactionQueue> {
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<TransactionPool> poolProvider;

    public WMShellBaseModule_ProvideSyncTransactionQueueFactory(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        this.poolProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public SyncTransactionQueue mo1933get() {
        return provideSyncTransactionQueue(this.poolProvider.mo1933get(), this.mainExecutorProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideSyncTransactionQueueFactory create(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        return new WMShellBaseModule_ProvideSyncTransactionQueueFactory(provider, provider2);
    }

    public static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        return (SyncTransactionQueue) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSyncTransactionQueue(transactionPool, shellExecutor));
    }
}
