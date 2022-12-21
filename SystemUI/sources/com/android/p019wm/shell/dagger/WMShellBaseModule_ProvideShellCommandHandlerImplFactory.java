package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.ShellCommandHandlerImpl;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerImplFactory */
public final class WMShellBaseModule_ProvideShellCommandHandlerImplFactory implements Factory<ShellCommandHandlerImpl> {
    private final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    private final Provider<Optional<HideDisplayCutoutController>> hideDisplayCutoutProvider;
    private final Provider<KidsModeTaskOrganizer> kidsModeTaskOrganizerProvider;
    private final Provider<Optional<LegacySplitScreenController>> legacySplitScreenOptionalProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<OneHandedController>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;

    public WMShellBaseModule_ProvideShellCommandHandlerImplFactory(Provider<ShellTaskOrganizer> provider, Provider<KidsModeTaskOrganizer> provider2, Provider<Optional<LegacySplitScreenController>> provider3, Provider<Optional<SplitScreenController>> provider4, Provider<Optional<Pip>> provider5, Provider<Optional<OneHandedController>> provider6, Provider<Optional<HideDisplayCutoutController>> provider7, Provider<Optional<AppPairsController>> provider8, Provider<Optional<RecentTasksController>> provider9, Provider<ShellExecutor> provider10) {
        this.shellTaskOrganizerProvider = provider;
        this.kidsModeTaskOrganizerProvider = provider2;
        this.legacySplitScreenOptionalProvider = provider3;
        this.splitScreenOptionalProvider = provider4;
        this.pipOptionalProvider = provider5;
        this.oneHandedOptionalProvider = provider6;
        this.hideDisplayCutoutProvider = provider7;
        this.appPairsOptionalProvider = provider8;
        this.recentTasksOptionalProvider = provider9;
        this.mainExecutorProvider = provider10;
    }

    public ShellCommandHandlerImpl get() {
        return provideShellCommandHandlerImpl(this.shellTaskOrganizerProvider.get(), this.kidsModeTaskOrganizerProvider.get(), this.legacySplitScreenOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.pipOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.hideDisplayCutoutProvider.get(), this.appPairsOptionalProvider.get(), this.recentTasksOptionalProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideShellCommandHandlerImplFactory create(Provider<ShellTaskOrganizer> provider, Provider<KidsModeTaskOrganizer> provider2, Provider<Optional<LegacySplitScreenController>> provider3, Provider<Optional<SplitScreenController>> provider4, Provider<Optional<Pip>> provider5, Provider<Optional<OneHandedController>> provider6, Provider<Optional<HideDisplayCutoutController>> provider7, Provider<Optional<AppPairsController>> provider8, Provider<Optional<RecentTasksController>> provider9, Provider<ShellExecutor> provider10) {
        return new WMShellBaseModule_ProvideShellCommandHandlerImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static ShellCommandHandlerImpl provideShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, ShellExecutor shellExecutor) {
        return (ShellCommandHandlerImpl) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellCommandHandlerImpl(shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, optional5, optional6, optional7, shellExecutor));
    }
}
