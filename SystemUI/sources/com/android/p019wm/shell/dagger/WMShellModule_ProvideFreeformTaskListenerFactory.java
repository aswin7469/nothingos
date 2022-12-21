package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.freeform.FreeformTaskListener;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvideFreeformTaskListenerFactory */
public final class WMShellModule_ProvideFreeformTaskListenerFactory implements Factory<FreeformTaskListener> {
    private final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellModule_ProvideFreeformTaskListenerFactory(Provider<SyncTransactionQueue> provider) {
        this.syncQueueProvider = provider;
    }

    public FreeformTaskListener get() {
        return provideFreeformTaskListener(this.syncQueueProvider.get());
    }

    public static WMShellModule_ProvideFreeformTaskListenerFactory create(Provider<SyncTransactionQueue> provider) {
        return new WMShellModule_ProvideFreeformTaskListenerFactory(provider);
    }

    public static FreeformTaskListener provideFreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        return (FreeformTaskListener) Preconditions.checkNotNullFromProvides(WMShellModule.provideFreeformTaskListener(syncTransactionQueue));
    }
}
