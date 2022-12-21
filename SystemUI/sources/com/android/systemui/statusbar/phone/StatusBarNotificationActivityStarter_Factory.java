package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.service.dreams.IDreamManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class StatusBarNotificationActivityStarter_Factory implements Factory<StatusBarNotificationActivityStarter> {
    private final Provider<ActivityIntentHelper> activityIntentHelperProvider;
    private final Provider<ActivityLaunchAnimator> activityLaunchAnimatorProvider;
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<AssistManager> assistManagerLazyProvider;
    private final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    private final Provider<CentralSurfaces> centralSurfacesProvider;
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<IDreamManager> dreamManagerProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    private final Provider<KeyguardManager> keyguardManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<StatusBarNotificationActivityStarterLogger> loggerProvider;
    private final Provider<Handler> mainThreadHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationLaunchAnimatorControllerProvider> notificationAnimationProvider;
    private final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    private final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    private final Provider<NotificationPanelViewController> panelProvider;
    private final Provider<NotificationPresenter> presenterProvider;
    private final Provider<StatusBarRemoteInputCallback> remoteInputCallbackProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Executor> uiBgExecutorProvider;
    private final Provider<NotificationVisibilityProvider> visibilityProvider;

    public StatusBarNotificationActivityStarter_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<NotificationEntryManager> provider5, Provider<NotifPipeline> provider6, Provider<NotificationVisibilityProvider> provider7, Provider<HeadsUpManagerPhone> provider8, Provider<ActivityStarter> provider9, Provider<NotificationClickNotifier> provider10, Provider<StatusBarStateController> provider11, Provider<StatusBarKeyguardViewManager> provider12, Provider<KeyguardManager> provider13, Provider<IDreamManager> provider14, Provider<Optional<BubblesManager>> provider15, Provider<AssistManager> provider16, Provider<NotificationRemoteInputManager> provider17, Provider<GroupMembershipManager> provider18, Provider<NotificationLockscreenUserManager> provider19, Provider<ShadeController> provider20, Provider<KeyguardStateController> provider21, Provider<NotificationInterruptStateProvider> provider22, Provider<LockPatternUtils> provider23, Provider<StatusBarRemoteInputCallback> provider24, Provider<ActivityIntentHelper> provider25, Provider<NotifPipelineFlags> provider26, Provider<MetricsLogger> provider27, Provider<StatusBarNotificationActivityStarterLogger> provider28, Provider<OnUserInteractionCallback> provider29, Provider<CentralSurfaces> provider30, Provider<NotificationPresenter> provider31, Provider<NotificationPanelViewController> provider32, Provider<ActivityLaunchAnimator> provider33, Provider<NotificationLaunchAnimatorControllerProvider> provider34) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.mainThreadHandlerProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.entryManagerProvider = provider5;
        this.notifPipelineProvider = provider6;
        this.visibilityProvider = provider7;
        this.headsUpManagerProvider = provider8;
        this.activityStarterProvider = provider9;
        this.clickNotifierProvider = provider10;
        this.statusBarStateControllerProvider = provider11;
        this.statusBarKeyguardViewManagerProvider = provider12;
        this.keyguardManagerProvider = provider13;
        this.dreamManagerProvider = provider14;
        this.bubblesManagerOptionalProvider = provider15;
        this.assistManagerLazyProvider = provider16;
        this.remoteInputManagerProvider = provider17;
        this.groupMembershipManagerProvider = provider18;
        this.lockscreenUserManagerProvider = provider19;
        this.shadeControllerProvider = provider20;
        this.keyguardStateControllerProvider = provider21;
        this.notificationInterruptStateProvider = provider22;
        this.lockPatternUtilsProvider = provider23;
        this.remoteInputCallbackProvider = provider24;
        this.activityIntentHelperProvider = provider25;
        this.notifPipelineFlagsProvider = provider26;
        this.metricsLoggerProvider = provider27;
        this.loggerProvider = provider28;
        this.onUserInteractionCallbackProvider = provider29;
        this.centralSurfacesProvider = provider30;
        this.presenterProvider = provider31;
        this.panelProvider = provider32;
        this.activityLaunchAnimatorProvider = provider33;
        this.notificationAnimationProvider = provider34;
    }

    public StatusBarNotificationActivityStarter get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.mainThreadHandlerProvider.get(), this.uiBgExecutorProvider.get(), this.entryManagerProvider.get(), this.notifPipelineProvider.get(), this.visibilityProvider.get(), this.headsUpManagerProvider.get(), this.activityStarterProvider.get(), this.clickNotifierProvider.get(), this.statusBarStateControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.keyguardManagerProvider.get(), this.dreamManagerProvider.get(), this.bubblesManagerOptionalProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.remoteInputManagerProvider.get(), this.groupMembershipManagerProvider.get(), this.lockscreenUserManagerProvider.get(), this.shadeControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.notificationInterruptStateProvider.get(), this.lockPatternUtilsProvider.get(), this.remoteInputCallbackProvider.get(), this.activityIntentHelperProvider.get(), this.notifPipelineFlagsProvider.get(), this.metricsLoggerProvider.get(), this.loggerProvider.get(), this.onUserInteractionCallbackProvider.get(), this.centralSurfacesProvider.get(), this.presenterProvider.get(), this.panelProvider.get(), this.activityLaunchAnimatorProvider.get(), this.notificationAnimationProvider.get());
    }

    public static StatusBarNotificationActivityStarter_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<NotificationEntryManager> provider5, Provider<NotifPipeline> provider6, Provider<NotificationVisibilityProvider> provider7, Provider<HeadsUpManagerPhone> provider8, Provider<ActivityStarter> provider9, Provider<NotificationClickNotifier> provider10, Provider<StatusBarStateController> provider11, Provider<StatusBarKeyguardViewManager> provider12, Provider<KeyguardManager> provider13, Provider<IDreamManager> provider14, Provider<Optional<BubblesManager>> provider15, Provider<AssistManager> provider16, Provider<NotificationRemoteInputManager> provider17, Provider<GroupMembershipManager> provider18, Provider<NotificationLockscreenUserManager> provider19, Provider<ShadeController> provider20, Provider<KeyguardStateController> provider21, Provider<NotificationInterruptStateProvider> provider22, Provider<LockPatternUtils> provider23, Provider<StatusBarRemoteInputCallback> provider24, Provider<ActivityIntentHelper> provider25, Provider<NotifPipelineFlags> provider26, Provider<MetricsLogger> provider27, Provider<StatusBarNotificationActivityStarterLogger> provider28, Provider<OnUserInteractionCallback> provider29, Provider<CentralSurfaces> provider30, Provider<NotificationPresenter> provider31, Provider<NotificationPanelViewController> provider32, Provider<ActivityLaunchAnimator> provider33, Provider<NotificationLaunchAnimatorControllerProvider> provider34) {
        return new StatusBarNotificationActivityStarter_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34);
    }

    public static StatusBarNotificationActivityStarter newInstance(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider2, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, NotifPipelineFlags notifPipelineFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback, CentralSurfaces centralSurfaces, NotificationPresenter notificationPresenter, NotificationPanelViewController notificationPanelViewController, ActivityLaunchAnimator activityLaunchAnimator, NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider) {
        return new StatusBarNotificationActivityStarter(context, commandQueue, handler, executor, notificationEntryManager, notifPipeline, notificationVisibilityProvider, headsUpManagerPhone, activityStarter, notificationClickNotifier, statusBarStateController, statusBarKeyguardViewManager, keyguardManager, iDreamManager, optional, lazy, notificationRemoteInputManager, groupMembershipManager, notificationLockscreenUserManager, shadeController, keyguardStateController, notificationInterruptStateProvider2, lockPatternUtils, statusBarRemoteInputCallback, activityIntentHelper, notifPipelineFlags, metricsLogger, statusBarNotificationActivityStarterLogger, onUserInteractionCallback, centralSurfaces, notificationPresenter, notificationPanelViewController, activityLaunchAnimator, notificationLaunchAnimatorControllerProvider);
    }
}
