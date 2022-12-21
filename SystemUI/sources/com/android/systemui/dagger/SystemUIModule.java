package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.clock.ClockModule;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.p019wm.shell.dagger.DynamicOverride;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.appops.dagger.AppOpsModule;
import com.android.systemui.assist.AssistModule;
import com.android.systemui.biometrics.AlternateUdfpsTouchProvider;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.biometrics.dagger.BiometricsModule;
import com.android.systemui.classifier.FalsingModule;
import com.android.systemui.controls.dagger.ControlsModule;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.dagger.DemoModeModule;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.dreams.dagger.DreamModule;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FlagsModule;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.log.dagger.LogModule;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.privacy.PrivacyModule;
import com.android.systemui.recents.Recents;
import com.android.systemui.screenshot.dagger.ScreenshotModule;
import com.android.systemui.settings.dagger.SettingsModule;
import com.android.systemui.smartspace.dagger.SmartspaceModule;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.QsFrameTranslateModule;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.people.PeopleHubModule;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.row.dagger.NotificationRowComponent;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.dagger.SmartRepliesInflationModule;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule;
import com.android.systemui.statusbar.window.StatusBarWindowModule;
import com.android.systemui.tuner.dagger.TunerModule;
import com.android.systemui.unfold.SysUIUnfoldModule;
import com.android.systemui.user.UserModule;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule;
import com.android.systemui.util.dagger.UtilModule;
import com.android.systemui.util.sensors.SensorModule;
import com.android.systemui.util.settings.SettingsUtilModule;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.util.time.SystemClockImpl;
import com.android.systemui.wallet.dagger.WalletModule;
import com.android.systemui.wmshell.BubblesManager;
import com.nothing.systemui.biometrics.NTUdfpsHbmModule;
import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import java.util.concurrent.Executor;

@Module(includes = {AppOpsModule.class, AssistModule.class, BiometricsModule.class, ClockModule.class, DreamModule.class, ControlsModule.class, DemoModeModule.class, FalsingModule.class, FlagsModule.class, LogModule.class, PeopleHubModule.class, PluginModule.class, PrivacyModule.class, QsFrameTranslateModule.class, ScreenshotModule.class, SensorModule.class, SettingsModule.class, SettingsUtilModule.class, SmartRepliesInflationModule.class, SmartspaceModule.class, StatusBarPolicyModule.class, StatusBarWindowModule.class, SysUIConcurrencyModule.class, SysUIUnfoldModule.class, TunerModule.class, UserModule.class, UtilModule.class, WalletModule.class, NTUdfpsHbmModule.class}, subcomponents = {CentralSurfacesComponent.class, NavigationBarComponent.class, NotificationRowComponent.class, DozeComponent.class, ExpandableNotificationRowComponent.class, KeyguardBouncerComponent.class, NotificationShelfComponent.class, FragmentService.FragmentCreator.class})
public abstract class SystemUIModule {
    /* access modifiers changed from: package-private */
    @Binds
    public abstract BootCompleteCache bindBootCompleteCache(BootCompleteCacheImpl bootCompleteCacheImpl);

    @Binds
    public abstract ContextComponentHelper bindComponentHelper(ContextComponentResolver contextComponentResolver);

    @Binds
    public abstract NotificationRowBinder bindNotificationRowBinder(NotificationRowBinderImpl notificationRowBinderImpl);

    /* access modifiers changed from: package-private */
    @SysUISingleton
    @Binds
    public abstract SystemClock bindSystemClock(SystemClockImpl systemClockImpl);

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract BcSmartspaceDataPlugin optionalBcSmartspaceDataPlugin();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract CentralSurfaces optionalCentralSurfaces();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract CommandQueue optionalCommandQueue();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract HeadsUpManager optionalHeadsUpManager();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract LowLightClockController optionalLowLightClockController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract Recents optionalRecents();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract UdfpsHbmProvider optionalUdfpsHbmProvider();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract AlternateUdfpsTouchProvider optionalUdfpsTouchProvider();

    @SysUISingleton
    @Provides
    static SysUiState provideSysUiState(DumpManager dumpManager) {
        SysUiState sysUiState = new SysUiState();
        dumpManager.registerDumpable(sysUiState);
        return sysUiState;
    }

    @Provides
    static SystemUIFactory getSystemUIFactory() {
        return SystemUIFactory.getInstance();
    }

    @SysUISingleton
    @Provides
    static Optional<BubblesManager> provideBubblesManager(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, @Main Executor executor) {
        return Optional.ofNullable(BubblesManager.create(context, optional, notificationShadeWindowController, keyguardStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationVisibilityProvider, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, commonNotifCollection, notifPipeline, sysUiState, notifPipelineFlags, dumpManager, executor));
    }

    @SysUISingleton
    @Provides
    static Optional<LowLightClockController> provideLowLightClockController(@DynamicOverride Optional<LowLightClockController> optional) {
        if (!optional.isPresent() || !optional.get().isLowLightClockEnabled()) {
            return Optional.empty();
        }
        return optional;
    }
}
