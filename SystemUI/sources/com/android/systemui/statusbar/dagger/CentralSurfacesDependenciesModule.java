package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import android.content.Context;
import android.os.Handler;
import android.service.dreams.IDreamManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import java.util.concurrent.Executor;

@Module
public interface CentralSurfacesDependenciesModule {
    @Binds
    ManagedProfileController provideManagedProfileController(ManagedProfileControllerImpl managedProfileControllerImpl);

    @Binds
    NotificationRemoteInputManager.Callback provideNotificationRemoteInputManagerCallback(StatusBarRemoteInputCallback statusBarRemoteInputCallback);

    @Binds
    QSCarrierGroupController.SlotIndexResolver provideSlotIndexResolver(QSCarrierGroupController.SubscriptionManagerSlotIndexResolver subscriptionManagerSlotIndexResolver);

    @Binds
    StatusBarIconController provideStatusBarIconController(StatusBarIconControllerImpl statusBarIconControllerImpl);

    @Binds
    SysuiStatusBarStateController providesSysuiStatusBarStateController(StatusBarStateControllerImpl statusBarStateControllerImpl);

    @SysUISingleton
    @Provides
    static NotificationRemoteInputManager provideNotificationRemoteInputManager(Context context, NotifPipelineFlags notifPipelineFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, SmartReplyController smartReplyController, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, Lazy<Optional<CentralSurfaces>> lazy, StatusBarStateController statusBarStateController, Handler handler, RemoteInputUriController remoteInputUriController, NotificationClickNotifier notificationClickNotifier, ActionClickLogger actionClickLogger, DumpManager dumpManager) {
        return new NotificationRemoteInputManager(context, notifPipelineFlags, notificationLockscreenUserManager, smartReplyController, notificationVisibilityProvider, notificationEntryManager, remoteInputNotificationRebuilder, lazy, statusBarStateController, handler, remoteInputUriController, notificationClickNotifier, actionClickLogger, dumpManager);
    }

    @SysUISingleton
    @Provides
    static NotificationMediaManager provideNotificationMediaManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, NotifPipelineFlags notifPipelineFlags, @Main DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, DumpManager dumpManager) {
        return new NotificationMediaManager(context, lazy, lazy2, notificationVisibilityProvider, notificationEntryManager, mediaArtworkProcessor, keyguardBypassController, notifPipeline, notifCollection, notifPipelineFlags, delayableExecutor, mediaDataManager, dumpManager);
    }

    @SysUISingleton
    @Provides
    static SmartReplyController provideSmartReplyController(DumpManager dumpManager, NotificationVisibilityProvider notificationVisibilityProvider, IStatusBarService iStatusBarService, NotificationClickNotifier notificationClickNotifier) {
        return new SmartReplyController(dumpManager, notificationVisibilityProvider, iStatusBarService, notificationClickNotifier);
    }

    @SysUISingleton
    @Provides
    static NotificationViewHierarchyManager provideNotificationViewHierarchyManager(Context context, @Main Handler handler, FeatureFlags featureFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, VisualStabilityManager visualStabilityManager, StatusBarStateController statusBarStateController, NotificationEntryManager notificationEntryManager, KeyguardBypassController keyguardBypassController, Optional<Bubbles> optional, DynamicPrivacyController dynamicPrivacyController, DynamicChildBindController dynamicChildBindController, LowPriorityInflationHelper lowPriorityInflationHelper, AssistantFeedbackController assistantFeedbackController, NotifPipelineFlags notifPipelineFlags, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController) {
        return new NotificationViewHierarchyManager(context, handler, featureFlags, notificationLockscreenUserManager, notificationGroupManagerLegacy, visualStabilityManager, statusBarStateController, notificationEntryManager, keyguardBypassController, optional, dynamicPrivacyController, dynamicChildBindController, lowPriorityInflationHelper, assistantFeedbackController, notifPipelineFlags, keyguardUpdateMonitor, keyguardStateController);
    }

    @SysUISingleton
    @Provides
    static CommandQueue provideCommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        return new CommandQueue(context, protoTracer, commandRegistry);
    }

    @SysUISingleton
    @Provides
    static OngoingCallController provideOngoingCallController(Context context, CommonNotifCollection commonNotifCollection, SystemClock systemClock, ActivityStarter activityStarter, @Main Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager, StatusBarWindowController statusBarWindowController, SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler, StatusBarStateController statusBarStateController, OngoingCallFlags ongoingCallFlags) {
        Optional optional;
        Optional optional2;
        boolean isInImmersiveEnabled = ongoingCallFlags.isInImmersiveEnabled();
        if (isInImmersiveEnabled) {
            optional = Optional.m1751of(statusBarWindowController);
        } else {
            optional = Optional.empty();
        }
        Optional optional3 = optional;
        if (isInImmersiveEnabled) {
            optional2 = Optional.m1751of(swipeStatusBarAwayGestureHandler);
        } else {
            optional2 = Optional.empty();
        }
        OngoingCallController ongoingCallController = new OngoingCallController(context, commonNotifCollection, ongoingCallFlags, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger, dumpManager, optional3, optional2, statusBarStateController);
        ongoingCallController.init();
        return ongoingCallController;
    }

    @SysUISingleton
    @Provides
    static ActivityLaunchAnimator provideActivityLaunchAnimator() {
        return new ActivityLaunchAnimator();
    }

    @SysUISingleton
    @Provides
    static DialogLaunchAnimator provideDialogLaunchAnimator(IDreamManager iDreamManager) {
        return new DialogLaunchAnimator(iDreamManager);
    }
}
