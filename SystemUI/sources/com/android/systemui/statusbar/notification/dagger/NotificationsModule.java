package com.android.systemui.statusbar.notification.dagger;

import android.app.INotificationManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerModule;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsModule;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.inflation.OnUserInteractionCallbackImpl;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.OnUserInteractionCallbackImplLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManagerImpl;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManagerImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.icon.ConversationIconManager;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProviderModule;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLoggerImpl;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotifPanelEventsModule;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

@Module(includes = {CoordinatorsModule.class, KeyguardNotificationVisibilityProviderModule.class, NotifPanelEventsModule.class, NotifPipelineChoreographerModule.class, NotificationSectionHeadersModule.class})
public interface NotificationsModule {
    @SysUISingleton
    @Provides
    static NotifGutsViewManager provideNotifGutsViewManager(NotificationGutsManager notificationGutsManager) {
        return notificationGutsManager;
    }

    @Binds
    BindEventManager bindBindEventManagerImpl(BindEventManagerImpl bindEventManagerImpl);

    @Binds
    StackScrollAlgorithm.BypassController bindBypassController(KeyguardBypassController keyguardBypassController);

    @Binds
    ConversationIconManager bindConversationIconManager(IconManager iconManager);

    @Binds
    NotifInflater bindNotifInflater(NotifInflaterImpl notifInflaterImpl);

    @Binds
    NotifLiveDataStore bindNotifLiveDataStore(NotifLiveDataStoreImpl notifLiveDataStoreImpl);

    @Binds
    NotificationInterruptStateProvider bindNotificationInterruptStateProvider(NotificationInterruptStateProviderImpl notificationInterruptStateProviderImpl);

    @Binds
    StackScrollAlgorithm.SectionProvider bindSectionProvider(NotificationSectionsManager notificationSectionsManager);

    @SysUISingleton
    @Binds
    NotificationVisibilityProvider provideNotificationVisibilityProvider(NotificationVisibilityProviderImpl notificationVisibilityProviderImpl);

    @SysUISingleton
    @Provides
    static NotificationEntryManager provideNotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotifPipelineFlags notifPipelineFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, IStatusBarService iStatusBarService, NotifLiveDataStoreImpl notifLiveDataStoreImpl, DumpManager dumpManager) {
        return new NotificationEntryManager(notificationEntryManagerLogger, notificationGroupManagerLegacy, notifPipelineFlags, lazy, lazy2, leakDetector, iStatusBarService, notifLiveDataStoreImpl, dumpManager);
    }

    @SysUISingleton
    @Provides
    static NotificationGutsManager provideNotificationGutsManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, @Main Handler handler, @Background Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController, DumpManager dumpManager) {
        return new NotificationGutsManager(context, lazy, handler, handler2, accessibilityManager, highPriorityProvider, iNotificationManager, notificationEntryManager, peopleSpaceWidgetManager, launcherApps, shortcutManager, channelEditorDialogController, userContextProvider, assistantFeedbackController, optional, uiEventLogger, onUserInteractionCallback, shadeController, dumpManager);
    }

    @SysUISingleton
    @Provides
    static VisualStabilityManager provideVisualStabilityManager(NotificationEntryManager notificationEntryManager, VisualStabilityProvider visualStabilityProvider, @Main Handler handler, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle, DumpManager dumpManager) {
        return new VisualStabilityManager(notificationEntryManager, visualStabilityProvider, handler, statusBarStateController, wakefulnessLifecycle, dumpManager);
    }

    @SysUISingleton
    @Provides
    static NotificationLogger provideNotificationLogger(NotificationListener notificationListener, @UiBackground Executor executor, NotifPipelineFlags notifPipelineFlags, NotifLiveDataStore notifLiveDataStore, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, StatusBarStateController statusBarStateController, NotificationLogger.ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        return new NotificationLogger(notificationListener, executor, notifPipelineFlags, notifLiveDataStore, notificationVisibilityProvider, notificationEntryManager, notifPipeline, statusBarStateController, expansionStateLogger, notificationPanelLogger);
    }

    @SysUISingleton
    @Provides
    static NotificationPanelLogger provideNotificationPanelLogger() {
        return new NotificationPanelLoggerImpl();
    }

    @SysUISingleton
    @Provides
    static GroupMembershipManager provideGroupMembershipManager(NotifPipelineFlags notifPipelineFlags, Lazy<NotificationGroupManagerLegacy> lazy) {
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            return new GroupMembershipManagerImpl();
        }
        return lazy.get();
    }

    @SysUISingleton
    @Provides
    static GroupExpansionManager provideGroupExpansionManager(NotifPipelineFlags notifPipelineFlags, Lazy<GroupMembershipManager> lazy, Lazy<NotificationGroupManagerLegacy> lazy2) {
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            return new GroupExpansionManagerImpl(lazy.get());
        }
        return lazy2.get();
    }

    @SysUISingleton
    @Provides
    static NotificationsController provideNotificationsController(Context context, Provider<NotificationsControllerImpl> provider, Provider<NotificationsControllerStub> provider2) {
        if (context.getResources().getBoolean(C1894R.bool.config_renderNotifications)) {
            return provider.get();
        }
        return provider2.get();
    }

    @SysUISingleton
    @Provides
    static CommonNotifCollection provideCommonNotifCollection(NotifPipelineFlags notifPipelineFlags, Lazy<NotifPipeline> lazy, NotificationEntryManager notificationEntryManager) {
        return notifPipelineFlags.isNewPipelineEnabled() ? lazy.get() : notificationEntryManager;
    }

    @SysUISingleton
    @Provides
    static NotifShadeEventSource provideNotifShadeEventSource(NotifPipelineFlags notifPipelineFlags, Lazy<ShadeEventCoordinator> lazy, Lazy<LegacyNotificationPresenterExtensions> lazy2) {
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            return lazy.get();
        }
        return lazy2.get();
    }

    @SysUISingleton
    @Provides
    static OnUserInteractionCallback provideOnUserInteractionCallback(NotifPipelineFlags notifPipelineFlags, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, Lazy<NotifCollection> lazy, Lazy<NotificationVisibilityProvider> lazy2, Lazy<VisualStabilityCoordinator> lazy3, NotificationEntryManager notificationEntryManager, VisualStabilityManager visualStabilityManager, Lazy<GroupMembershipManager> lazy4) {
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            return new OnUserInteractionCallbackImpl(lazy2.get(), lazy.get(), headsUpManager, statusBarStateController, lazy3.get());
        }
        return new OnUserInteractionCallbackImplLegacy(notificationEntryManager, lazy2.get(), headsUpManager, statusBarStateController, visualStabilityManager, lazy4.get());
    }
}
