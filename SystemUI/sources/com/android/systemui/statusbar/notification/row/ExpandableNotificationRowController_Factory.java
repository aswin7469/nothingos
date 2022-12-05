package com.android.systemui.statusbar.notification.row;

import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wmshell.BubblesManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowController_Factory implements Factory<ExpandableNotificationRowController> {
    private final Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
    private final Provider<Boolean> allowLongPressProvider;
    private final Provider<String> appNameProvider;
    private final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    private final Provider<SystemClock> clockProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<GroupExpansionManager> groupExpansionManagerProvider;
    private final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<NotificationListContainer> listContainerProvider;
    private final Provider<NotificationMediaManager> mediaManagerProvider;
    private final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    private final Provider<String> notificationKeyProvider;
    private final Provider<NotificationLogger> notificationLoggerProvider;
    private final Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
    private final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    private final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    private final Provider<PluginManager> pluginManagerProvider;
    private final Provider<RowContentBindStage> rowContentBindStageProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<ExpandableNotificationRow> viewProvider;

    public ExpandableNotificationRowController_Factory(Provider<ExpandableNotificationRow> provider, Provider<NotificationListContainer> provider2, Provider<ActivatableNotificationViewController> provider3, Provider<NotificationMediaManager> provider4, Provider<PluginManager> provider5, Provider<SystemClock> provider6, Provider<String> provider7, Provider<String> provider8, Provider<KeyguardBypassController> provider9, Provider<GroupMembershipManager> provider10, Provider<GroupExpansionManager> provider11, Provider<RowContentBindStage> provider12, Provider<NotificationLogger> provider13, Provider<HeadsUpManager> provider14, Provider<ExpandableNotificationRow.OnExpandClickListener> provider15, Provider<StatusBarStateController> provider16, Provider<NotificationGutsManager> provider17, Provider<Boolean> provider18, Provider<OnUserInteractionCallback> provider19, Provider<FalsingManager> provider20, Provider<FalsingCollector> provider21, Provider<PeopleNotificationIdentifier> provider22, Provider<Optional<BubblesManager>> provider23) {
        this.viewProvider = provider;
        this.listContainerProvider = provider2;
        this.activatableNotificationViewControllerProvider = provider3;
        this.mediaManagerProvider = provider4;
        this.pluginManagerProvider = provider5;
        this.clockProvider = provider6;
        this.appNameProvider = provider7;
        this.notificationKeyProvider = provider8;
        this.keyguardBypassControllerProvider = provider9;
        this.groupMembershipManagerProvider = provider10;
        this.groupExpansionManagerProvider = provider11;
        this.rowContentBindStageProvider = provider12;
        this.notificationLoggerProvider = provider13;
        this.headsUpManagerProvider = provider14;
        this.onExpandClickListenerProvider = provider15;
        this.statusBarStateControllerProvider = provider16;
        this.notificationGutsManagerProvider = provider17;
        this.allowLongPressProvider = provider18;
        this.onUserInteractionCallbackProvider = provider19;
        this.falsingManagerProvider = provider20;
        this.falsingCollectorProvider = provider21;
        this.peopleNotificationIdentifierProvider = provider22;
        this.bubblesManagerOptionalProvider = provider23;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ExpandableNotificationRowController mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.listContainerProvider.mo1933get(), this.activatableNotificationViewControllerProvider.mo1933get(), this.mediaManagerProvider.mo1933get(), this.pluginManagerProvider.mo1933get(), this.clockProvider.mo1933get(), this.appNameProvider.mo1933get(), this.notificationKeyProvider.mo1933get(), this.keyguardBypassControllerProvider.mo1933get(), this.groupMembershipManagerProvider.mo1933get(), this.groupExpansionManagerProvider.mo1933get(), this.rowContentBindStageProvider.mo1933get(), this.notificationLoggerProvider.mo1933get(), this.headsUpManagerProvider.mo1933get(), this.onExpandClickListenerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.notificationGutsManagerProvider.mo1933get(), this.allowLongPressProvider.mo1933get().booleanValue(), this.onUserInteractionCallbackProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.falsingCollectorProvider.mo1933get(), this.peopleNotificationIdentifierProvider.mo1933get(), this.bubblesManagerOptionalProvider.mo1933get());
    }

    public static ExpandableNotificationRowController_Factory create(Provider<ExpandableNotificationRow> provider, Provider<NotificationListContainer> provider2, Provider<ActivatableNotificationViewController> provider3, Provider<NotificationMediaManager> provider4, Provider<PluginManager> provider5, Provider<SystemClock> provider6, Provider<String> provider7, Provider<String> provider8, Provider<KeyguardBypassController> provider9, Provider<GroupMembershipManager> provider10, Provider<GroupExpansionManager> provider11, Provider<RowContentBindStage> provider12, Provider<NotificationLogger> provider13, Provider<HeadsUpManager> provider14, Provider<ExpandableNotificationRow.OnExpandClickListener> provider15, Provider<StatusBarStateController> provider16, Provider<NotificationGutsManager> provider17, Provider<Boolean> provider18, Provider<OnUserInteractionCallback> provider19, Provider<FalsingManager> provider20, Provider<FalsingCollector> provider21, Provider<PeopleNotificationIdentifier> provider22, Provider<Optional<BubblesManager>> provider23) {
        return new ExpandableNotificationRowController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23);
    }

    public static ExpandableNotificationRowController newInstance(ExpandableNotificationRow expandableNotificationRow, NotificationListContainer notificationListContainer, ActivatableNotificationViewController activatableNotificationViewController, NotificationMediaManager notificationMediaManager, PluginManager pluginManager, SystemClock systemClock, String str, String str2, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, RowContentBindStage rowContentBindStage, NotificationLogger notificationLogger, HeadsUpManager headsUpManager, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, StatusBarStateController statusBarStateController, NotificationGutsManager notificationGutsManager, boolean z, OnUserInteractionCallback onUserInteractionCallback, FalsingManager falsingManager, FalsingCollector falsingCollector, PeopleNotificationIdentifier peopleNotificationIdentifier, Optional<BubblesManager> optional) {
        return new ExpandableNotificationRowController(expandableNotificationRow, notificationListContainer, activatableNotificationViewController, notificationMediaManager, pluginManager, systemClock, str, str2, keyguardBypassController, groupMembershipManager, groupExpansionManager, rowContentBindStage, notificationLogger, headsUpManager, onExpandClickListener, statusBarStateController, notificationGutsManager, z, onUserInteractionCallback, falsingManager, falsingCollector, peopleNotificationIdentifier, optional);
    }
}
