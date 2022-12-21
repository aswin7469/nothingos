package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellTaskOrganizerFactory */
public final class WMShellBaseModule_ProvideShellTaskOrganizerFactory implements Factory<ShellTaskOrganizer> {
    private final Provider<CompatUIController> compatUIProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;

    public WMShellBaseModule_ProvideShellTaskOrganizerFactory(Provider<ShellExecutor> provider, Provider<Context> provider2, Provider<CompatUIController> provider3, Provider<Optional<RecentTasksController>> provider4) {
        this.mainExecutorProvider = provider;
        this.contextProvider = provider2;
        this.compatUIProvider = provider3;
        this.recentTasksOptionalProvider = provider4;
    }

    public ShellTaskOrganizer get() {
        return provideShellTaskOrganizer(this.mainExecutorProvider.get(), this.contextProvider.get(), this.compatUIProvider.get(), this.recentTasksOptionalProvider.get());
    }

    public static WMShellBaseModule_ProvideShellTaskOrganizerFactory create(Provider<ShellExecutor> provider, Provider<Context> provider2, Provider<CompatUIController> provider3, Provider<Optional<RecentTasksController>> provider4) {
        return new WMShellBaseModule_ProvideShellTaskOrganizerFactory(provider, provider2, provider3, provider4);
    }

    public static ShellTaskOrganizer provideShellTaskOrganizer(ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        return (ShellTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellTaskOrganizer(shellExecutor, context, compatUIController, optional));
    }
}
