package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideTransactionPoolFactory */
public final class WMShellBaseModule_ProvideTransactionPoolFactory implements Factory<TransactionPool> {
    public TransactionPool get() {
        return provideTransactionPool();
    }

    public static WMShellBaseModule_ProvideTransactionPoolFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TransactionPool provideTransactionPool() {
        return (TransactionPool) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTransactionPool());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideTransactionPoolFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellBaseModule_ProvideTransactionPoolFactory INSTANCE = new WMShellBaseModule_ProvideTransactionPoolFactory();

        private InstanceHolder() {
        }
    }
}
