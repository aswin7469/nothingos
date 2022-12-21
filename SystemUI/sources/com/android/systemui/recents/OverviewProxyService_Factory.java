package com.android.systemui.recents;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class OverviewProxyService_Factory implements Factory<OverviewProxyService> {
    private final Provider<AssistUtils> assistUtilsProvider;
    private final Provider<Optional<BackAnimation>> backAnimationProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<NavigationBarController> navBarControllerLazyProvider;
    private final Provider<NavigationModeController> navModeControllerProvider;
    private final Provider<Optional<OneHanded>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<Optional<RecentTasks>> recentTasksProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<ShellTransitions> shellTransitionsProvider;
    private final Provider<Optional<SplitScreen>> splitScreenOptionalProvider;
    private final Provider<Optional<StartingSurface>> startingSurfaceProvider;
    private final Provider<NotificationShadeWindowController> statusBarWinControllerProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<KeyguardUnlockAnimationController> sysuiUnlockAnimationControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public OverviewProxyService_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<NavigationModeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<SysUiState> provider7, Provider<Optional<Pip>> provider8, Provider<Optional<SplitScreen>> provider9, Provider<Optional<OneHanded>> provider10, Provider<Optional<RecentTasks>> provider11, Provider<Optional<BackAnimation>> provider12, Provider<Optional<StartingSurface>> provider13, Provider<BroadcastDispatcher> provider14, Provider<ShellTransitions> provider15, Provider<ScreenLifecycle> provider16, Provider<UiEventLogger> provider17, Provider<KeyguardUnlockAnimationController> provider18, Provider<AssistUtils> provider19, Provider<DumpManager> provider20) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.navBarControllerLazyProvider = provider3;
        this.centralSurfacesOptionalLazyProvider = provider4;
        this.navModeControllerProvider = provider5;
        this.statusBarWinControllerProvider = provider6;
        this.sysUiStateProvider = provider7;
        this.pipOptionalProvider = provider8;
        this.splitScreenOptionalProvider = provider9;
        this.oneHandedOptionalProvider = provider10;
        this.recentTasksProvider = provider11;
        this.backAnimationProvider = provider12;
        this.startingSurfaceProvider = provider13;
        this.broadcastDispatcherProvider = provider14;
        this.shellTransitionsProvider = provider15;
        this.screenLifecycleProvider = provider16;
        this.uiEventLoggerProvider = provider17;
        this.sysuiUnlockAnimationControllerProvider = provider18;
        this.assistUtilsProvider = provider19;
        this.dumpManagerProvider = provider20;
    }

    public OverviewProxyService get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.navBarControllerLazyProvider), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.navModeControllerProvider.get(), this.statusBarWinControllerProvider.get(), this.sysUiStateProvider.get(), this.pipOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.recentTasksProvider.get(), this.backAnimationProvider.get(), this.startingSurfaceProvider.get(), this.broadcastDispatcherProvider.get(), this.shellTransitionsProvider.get(), this.screenLifecycleProvider.get(), this.uiEventLoggerProvider.get(), this.sysuiUnlockAnimationControllerProvider.get(), this.assistUtilsProvider.get(), this.dumpManagerProvider.get());
    }

    public static OverviewProxyService_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<NavigationModeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<SysUiState> provider7, Provider<Optional<Pip>> provider8, Provider<Optional<SplitScreen>> provider9, Provider<Optional<OneHanded>> provider10, Provider<Optional<RecentTasks>> provider11, Provider<Optional<BackAnimation>> provider12, Provider<Optional<StartingSurface>> provider13, Provider<BroadcastDispatcher> provider14, Provider<ShellTransitions> provider15, Provider<ScreenLifecycle> provider16, Provider<UiEventLogger> provider17, Provider<KeyguardUnlockAnimationController> provider18, Provider<AssistUtils> provider19, Provider<DumpManager> provider20) {
        return new OverviewProxyService_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20);
    }

    public static OverviewProxyService newInstance(Context context, CommandQueue commandQueue, Lazy<NavigationBarController> lazy, Lazy<Optional<CentralSurfaces>> lazy2, NavigationModeController navigationModeController, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Optional<Pip> optional, Optional<SplitScreen> optional2, Optional<OneHanded> optional3, Optional<RecentTasks> optional4, Optional<BackAnimation> optional5, Optional<StartingSurface> optional6, BroadcastDispatcher broadcastDispatcher, ShellTransitions shellTransitions, ScreenLifecycle screenLifecycle, UiEventLogger uiEventLogger, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AssistUtils assistUtils, DumpManager dumpManager) {
        return new OverviewProxyService(context, commandQueue, lazy, lazy2, navigationModeController, notificationShadeWindowController, sysUiState, optional, optional2, optional3, optional4, optional5, optional6, broadcastDispatcher, shellTransitions, screenLifecycle, uiEventLogger, keyguardUnlockAnimationController, assistUtils, dumpManager);
    }
}
