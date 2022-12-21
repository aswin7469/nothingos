package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.fullscreen.FullscreenTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.p019wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideFullscreenTaskListenerFactory */
public final class WMShellBaseModule_ProvideFullscreenTaskListenerFactory implements Factory<FullscreenTaskListener> {
    private final Provider<Optional<FullscreenTaskListener>> fullscreenTaskListenerProvider;
    private final Provider<Optional<FullscreenUnfoldController>> optionalFullscreenUnfoldControllerProvider;
    private final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellBaseModule_ProvideFullscreenTaskListenerFactory(Provider<Optional<FullscreenTaskListener>> provider, Provider<SyncTransactionQueue> provider2, Provider<Optional<FullscreenUnfoldController>> provider3, Provider<Optional<RecentTasksController>> provider4) {
        this.fullscreenTaskListenerProvider = provider;
        this.syncQueueProvider = provider2;
        this.optionalFullscreenUnfoldControllerProvider = provider3;
        this.recentTasksOptionalProvider = provider4;
    }

    public FullscreenTaskListener get() {
        return provideFullscreenTaskListener(this.fullscreenTaskListenerProvider.get(), this.syncQueueProvider.get(), this.optionalFullscreenUnfoldControllerProvider.get(), this.recentTasksOptionalProvider.get());
    }

    public static WMShellBaseModule_ProvideFullscreenTaskListenerFactory create(Provider<Optional<FullscreenTaskListener>> provider, Provider<SyncTransactionQueue> provider2, Provider<Optional<FullscreenUnfoldController>> provider3, Provider<Optional<RecentTasksController>> provider4) {
        return new WMShellBaseModule_ProvideFullscreenTaskListenerFactory(provider, provider2, provider3, provider4);
    }

    public static FullscreenTaskListener provideFullscreenTaskListener(Optional<FullscreenTaskListener> optional, SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional2, Optional<RecentTasksController> optional3) {
        return (FullscreenTaskListener) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideFullscreenTaskListener(optional, syncTransactionQueue, optional2, optional3));
    }
}
