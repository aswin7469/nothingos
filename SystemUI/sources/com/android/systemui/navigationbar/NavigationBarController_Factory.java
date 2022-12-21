package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.pip.Pip;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class NavigationBarController_Factory implements Factory<NavigationBarController> {
    private final Provider<AutoHideController> autoHideControllerProvider;
    private final Provider<Optional<BackAnimation>> backAnimationProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<LightBarController> lightBarControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NavBarHelper> navBarHelperProvider;
    private final Provider<NavigationBarComponent.Factory> navigationBarComponentFactoryProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<OverviewProxyService> overviewProxyServiceProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<SysUiState> sysUiFlagsContainerProvider;
    private final Provider<TaskbarDelegate> taskbarDelegateProvider;

    public NavigationBarController_Factory(Provider<Context> provider, Provider<OverviewProxyService> provider2, Provider<NavigationModeController> provider3, Provider<SysUiState> provider4, Provider<CommandQueue> provider5, Provider<Handler> provider6, Provider<ConfigurationController> provider7, Provider<NavBarHelper> provider8, Provider<TaskbarDelegate> provider9, Provider<NavigationBarComponent.Factory> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<DumpManager> provider12, Provider<AutoHideController> provider13, Provider<LightBarController> provider14, Provider<Optional<Pip>> provider15, Provider<Optional<BackAnimation>> provider16) {
        this.contextProvider = provider;
        this.overviewProxyServiceProvider = provider2;
        this.navigationModeControllerProvider = provider3;
        this.sysUiFlagsContainerProvider = provider4;
        this.commandQueueProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.navBarHelperProvider = provider8;
        this.taskbarDelegateProvider = provider9;
        this.navigationBarComponentFactoryProvider = provider10;
        this.statusBarKeyguardViewManagerProvider = provider11;
        this.dumpManagerProvider = provider12;
        this.autoHideControllerProvider = provider13;
        this.lightBarControllerProvider = provider14;
        this.pipOptionalProvider = provider15;
        this.backAnimationProvider = provider16;
    }

    public NavigationBarController get() {
        return newInstance(this.contextProvider.get(), this.overviewProxyServiceProvider.get(), this.navigationModeControllerProvider.get(), this.sysUiFlagsContainerProvider.get(), this.commandQueueProvider.get(), this.mainHandlerProvider.get(), this.configurationControllerProvider.get(), this.navBarHelperProvider.get(), this.taskbarDelegateProvider.get(), this.navigationBarComponentFactoryProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.dumpManagerProvider.get(), this.autoHideControllerProvider.get(), this.lightBarControllerProvider.get(), this.pipOptionalProvider.get(), this.backAnimationProvider.get());
    }

    public static NavigationBarController_Factory create(Provider<Context> provider, Provider<OverviewProxyService> provider2, Provider<NavigationModeController> provider3, Provider<SysUiState> provider4, Provider<CommandQueue> provider5, Provider<Handler> provider6, Provider<ConfigurationController> provider7, Provider<NavBarHelper> provider8, Provider<TaskbarDelegate> provider9, Provider<NavigationBarComponent.Factory> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<DumpManager> provider12, Provider<AutoHideController> provider13, Provider<LightBarController> provider14, Provider<Optional<Pip>> provider15, Provider<Optional<BackAnimation>> provider16) {
        return new NavigationBarController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static NavigationBarController newInstance(Context context, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, SysUiState sysUiState, CommandQueue commandQueue, Handler handler, ConfigurationController configurationController, NavBarHelper navBarHelper, TaskbarDelegate taskbarDelegate, NavigationBarComponent.Factory factory, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, AutoHideController autoHideController, LightBarController lightBarController, Optional<Pip> optional, Optional<BackAnimation> optional2) {
        return new NavigationBarController(context, overviewProxyService, navigationModeController, sysUiState, commandQueue, handler, configurationController, navBarHelper, taskbarDelegate, factory, statusBarKeyguardViewManager, dumpManager, autoHideController, lightBarController, optional, optional2);
    }
}
