package com.android.systemui.p012qs;

import android.os.Handler;
import android.os.UserManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.FooterActionsController_Factory */
public final class FooterActionsController_Factory implements Factory<FooterActionsController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSFgsManagerFooter> fgsManagerFooterControllerProvider;
    private final Provider<GlobalActionsDialogLite> globalActionsDialogProvider;
    private final Provider<GlobalSettings> globalSettingProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<MultiUserSwitchController.Factory> multiUserSwitchControllerFactoryProvider;
    private final Provider<QSSecurityFooter> securityFooterControllerProvider;
    private final Provider<Boolean> showPMLiteButtonProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserTracker> userTrackerProvider;
    private final Provider<FooterActionsView> viewProvider;

    public FooterActionsController_Factory(Provider<FooterActionsView> provider, Provider<MultiUserSwitchController.Factory> provider2, Provider<ActivityStarter> provider3, Provider<UserManager> provider4, Provider<UserTracker> provider5, Provider<UserInfoController> provider6, Provider<DeviceProvisionedController> provider7, Provider<QSSecurityFooter> provider8, Provider<QSFgsManagerFooter> provider9, Provider<FalsingManager> provider10, Provider<MetricsLogger> provider11, Provider<GlobalActionsDialogLite> provider12, Provider<UiEventLogger> provider13, Provider<Boolean> provider14, Provider<GlobalSettings> provider15, Provider<Handler> provider16) {
        this.viewProvider = provider;
        this.multiUserSwitchControllerFactoryProvider = provider2;
        this.activityStarterProvider = provider3;
        this.userManagerProvider = provider4;
        this.userTrackerProvider = provider5;
        this.userInfoControllerProvider = provider6;
        this.deviceProvisionedControllerProvider = provider7;
        this.securityFooterControllerProvider = provider8;
        this.fgsManagerFooterControllerProvider = provider9;
        this.falsingManagerProvider = provider10;
        this.metricsLoggerProvider = provider11;
        this.globalActionsDialogProvider = provider12;
        this.uiEventLoggerProvider = provider13;
        this.showPMLiteButtonProvider = provider14;
        this.globalSettingProvider = provider15;
        this.handlerProvider = provider16;
    }

    public FooterActionsController get() {
        return newInstance(this.viewProvider.get(), this.multiUserSwitchControllerFactoryProvider.get(), this.activityStarterProvider.get(), this.userManagerProvider.get(), this.userTrackerProvider.get(), this.userInfoControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.securityFooterControllerProvider.get(), this.fgsManagerFooterControllerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.globalActionsDialogProvider, this.uiEventLoggerProvider.get(), this.showPMLiteButtonProvider.get().booleanValue(), this.globalSettingProvider.get(), this.handlerProvider.get());
    }

    public static FooterActionsController_Factory create(Provider<FooterActionsView> provider, Provider<MultiUserSwitchController.Factory> provider2, Provider<ActivityStarter> provider3, Provider<UserManager> provider4, Provider<UserTracker> provider5, Provider<UserInfoController> provider6, Provider<DeviceProvisionedController> provider7, Provider<QSSecurityFooter> provider8, Provider<QSFgsManagerFooter> provider9, Provider<FalsingManager> provider10, Provider<MetricsLogger> provider11, Provider<GlobalActionsDialogLite> provider12, Provider<UiEventLogger> provider13, Provider<Boolean> provider14, Provider<GlobalSettings> provider15, Provider<Handler> provider16) {
        return new FooterActionsController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static FooterActionsController newInstance(FooterActionsView footerActionsView, MultiUserSwitchController.Factory factory, ActivityStarter activityStarter, UserManager userManager, UserTracker userTracker, UserInfoController userInfoController, DeviceProvisionedController deviceProvisionedController, QSSecurityFooter qSSecurityFooter, QSFgsManagerFooter qSFgsManagerFooter, FalsingManager falsingManager, MetricsLogger metricsLogger, Provider<GlobalActionsDialogLite> provider, UiEventLogger uiEventLogger, boolean z, GlobalSettings globalSettings, Handler handler) {
        return new FooterActionsController(footerActionsView, factory, activityStarter, userManager, userTracker, userInfoController, deviceProvisionedController, qSSecurityFooter, qSFgsManagerFooter, falsingManager, metricsLogger, provider, uiEventLogger, z, globalSettings, handler);
    }
}
