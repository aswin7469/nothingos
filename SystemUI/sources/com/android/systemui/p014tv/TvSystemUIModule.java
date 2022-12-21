package com.android.systemui.p014tv;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.PowerManager;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dock.DockManagerImpl;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.p012qs.dagger.QSModule;
import com.android.systemui.p012qs.tileimpl.QSFactoryImpl;
import com.android.systemui.plugins.p011qs.QSFactory;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.dagger.PowerModule;
import com.android.systemui.privacy.MediaProjectionPrivacyItemMonitor;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SensorPrivacyControllerImpl;
import com.android.systemui.volume.dagger.VolumeModule;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Named;

@Module(includes = {PowerModule.class, QSModule.class, VolumeModule.class}, subcomponents = {})
/* renamed from: com.android.systemui.tv.TvSystemUIModule */
public abstract class TvSystemUIModule {
    @SysUISingleton
    @Provides
    @Named("allow_notif_longpress")
    static boolean provideAllowNotificationLongPress() {
        return true;
    }

    @SysUISingleton
    @Provides
    @Named("leak_report_email")
    static String provideLeakReportEmail() {
        return null;
    }

    /* access modifiers changed from: package-private */
    @Binds
    public abstract DockManager bindDockManager(DockManagerImpl dockManagerImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract HeadsUpManager bindHeadsUpManagerPhone(HeadsUpManagerPhone headsUpManagerPhone);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationEntryManager.KeyguardEnvironment bindKeyguardEnvironment(KeyguardEnvironmentImpl keyguardEnvironmentImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract KeyguardViewController bindKeyguardViewController(StatusBarKeyguardViewManager statusBarKeyguardViewManager);

    /* access modifiers changed from: package-private */
    @Binds
    @IntoSet
    public abstract PrivacyItemMonitor bindMediaProjectionPrivacyItemMonitor(MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationLockscreenUserManager bindNotificationLockscreenUserManager(NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationShadeWindowController bindNotificationShadeController(NotificationShadeWindowControllerImpl notificationShadeWindowControllerImpl);

    /* access modifiers changed from: package-private */
    @SysUISingleton
    @Binds
    public abstract QSFactory bindQSFactory(QSFactoryImpl qSFactoryImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract DozeHost provideDozeHost(DozeServiceHost dozeServiceHost);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract ShadeController provideShadeController(ShadeControllerImpl shadeControllerImpl);

    @SysUISingleton
    @Provides
    static BatteryController provideBatteryController(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, @Main Handler handler, @Background Handler handler2) {
        BatteryControllerImpl batteryControllerImpl = new BatteryControllerImpl(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2);
        batteryControllerImpl.init();
        return batteryControllerImpl;
    }

    @SysUISingleton
    @Provides
    static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        SensorPrivacyControllerImpl sensorPrivacyControllerImpl = new SensorPrivacyControllerImpl(sensorPrivacyManager);
        sensorPrivacyControllerImpl.init();
        return sensorPrivacyControllerImpl;
    }

    @SysUISingleton
    @Provides
    static IndividualSensorPrivacyController provideIndividualSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        IndividualSensorPrivacyControllerImpl individualSensorPrivacyControllerImpl = new IndividualSensorPrivacyControllerImpl(sensorPrivacyManager);
        individualSensorPrivacyControllerImpl.init();
        return individualSensorPrivacyControllerImpl;
    }

    @SysUISingleton
    @Provides
    static HeadsUpManagerPhone provideHeadsUpManagerPhone(Context context, HeadsUpManagerLogger headsUpManagerLogger, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, VisualStabilityProvider visualStabilityProvider, ConfigurationController configurationController) {
        return new HeadsUpManagerPhone(context, headsUpManagerLogger, statusBarStateController, keyguardBypassController, groupMembershipManager, visualStabilityProvider, configurationController);
    }

    @SysUISingleton
    @Provides
    static Recents provideRecents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        return new Recents(context, recentsImplementation, commandQueue);
    }

    @SysUISingleton
    @Provides
    static DeviceProvisionedController providesDeviceProvisionedController(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl) {
        deviceProvisionedControllerImpl.init();
        return deviceProvisionedControllerImpl;
    }

    @SysUISingleton
    @Provides
    static TvNotificationHandler provideTvNotificationHandler(Context context, NotificationListener notificationListener) {
        return new TvNotificationHandler(context, notificationListener);
    }
}
