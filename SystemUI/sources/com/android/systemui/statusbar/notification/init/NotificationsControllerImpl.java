package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationListController;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationsControllerImpl.kt */
/* loaded from: classes.dex */
public final class NotificationsControllerImpl implements NotificationsController {
    @NotNull
    private final AnimatedImageNotificationManager animatedImageNotificationManager;
    @NotNull
    private final NotificationClicker.Builder clickerBuilder;
    @NotNull
    private final DeviceProvisionedController deviceProvisionedController;
    @NotNull
    private final NotificationEntryManager entryManager;
    @NotNull
    private final FeatureFlags featureFlags;
    @NotNull
    private final NotificationGroupAlertTransferHelper groupAlertTransferHelper;
    @NotNull
    private final Lazy<NotificationGroupManagerLegacy> groupManagerLegacy;
    @NotNull
    private final HeadsUpController headsUpController;
    @NotNull
    private final HeadsUpManager headsUpManager;
    @NotNull
    private final HeadsUpViewBinder headsUpViewBinder;
    @NotNull
    private final NotificationRankingManager legacyRanker;
    @NotNull
    private final Lazy<NotifPipelineInitializer> newNotifPipeline;
    @NotNull
    private final NotifBindPipelineInitializer notifBindPipelineInitializer;
    @NotNull
    private final Lazy<NotifPipeline> notifPipeline;
    @NotNull
    private final NotificationListener notificationListener;
    @NotNull
    private final NotificationRowBinderImpl notificationRowBinder;
    @NotNull
    private final PeopleSpaceWidgetManager peopleSpaceWidgetManager;
    @NotNull
    private final RemoteInputUriController remoteInputUriController;
    @NotNull
    private final TargetSdkResolver targetSdkResolver;

    public NotificationsControllerImpl(@NotNull FeatureFlags featureFlags, @NotNull NotificationListener notificationListener, @NotNull NotificationEntryManager entryManager, @NotNull NotificationRankingManager legacyRanker, @NotNull Lazy<NotifPipeline> notifPipeline, @NotNull TargetSdkResolver targetSdkResolver, @NotNull Lazy<NotifPipelineInitializer> newNotifPipeline, @NotNull NotifBindPipelineInitializer notifBindPipelineInitializer, @NotNull DeviceProvisionedController deviceProvisionedController, @NotNull NotificationRowBinderImpl notificationRowBinder, @NotNull RemoteInputUriController remoteInputUriController, @NotNull Lazy<NotificationGroupManagerLegacy> groupManagerLegacy, @NotNull NotificationGroupAlertTransferHelper groupAlertTransferHelper, @NotNull HeadsUpManager headsUpManager, @NotNull HeadsUpController headsUpController, @NotNull HeadsUpViewBinder headsUpViewBinder, @NotNull NotificationClicker.Builder clickerBuilder, @NotNull AnimatedImageNotificationManager animatedImageNotificationManager, @NotNull PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(notificationListener, "notificationListener");
        Intrinsics.checkNotNullParameter(entryManager, "entryManager");
        Intrinsics.checkNotNullParameter(legacyRanker, "legacyRanker");
        Intrinsics.checkNotNullParameter(notifPipeline, "notifPipeline");
        Intrinsics.checkNotNullParameter(targetSdkResolver, "targetSdkResolver");
        Intrinsics.checkNotNullParameter(newNotifPipeline, "newNotifPipeline");
        Intrinsics.checkNotNullParameter(notifBindPipelineInitializer, "notifBindPipelineInitializer");
        Intrinsics.checkNotNullParameter(deviceProvisionedController, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(notificationRowBinder, "notificationRowBinder");
        Intrinsics.checkNotNullParameter(remoteInputUriController, "remoteInputUriController");
        Intrinsics.checkNotNullParameter(groupManagerLegacy, "groupManagerLegacy");
        Intrinsics.checkNotNullParameter(groupAlertTransferHelper, "groupAlertTransferHelper");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(headsUpController, "headsUpController");
        Intrinsics.checkNotNullParameter(headsUpViewBinder, "headsUpViewBinder");
        Intrinsics.checkNotNullParameter(clickerBuilder, "clickerBuilder");
        Intrinsics.checkNotNullParameter(animatedImageNotificationManager, "animatedImageNotificationManager");
        Intrinsics.checkNotNullParameter(peopleSpaceWidgetManager, "peopleSpaceWidgetManager");
        this.featureFlags = featureFlags;
        this.notificationListener = notificationListener;
        this.entryManager = entryManager;
        this.legacyRanker = legacyRanker;
        this.notifPipeline = notifPipeline;
        this.targetSdkResolver = targetSdkResolver;
        this.newNotifPipeline = newNotifPipeline;
        this.notifBindPipelineInitializer = notifBindPipelineInitializer;
        this.deviceProvisionedController = deviceProvisionedController;
        this.notificationRowBinder = notificationRowBinder;
        this.remoteInputUriController = remoteInputUriController;
        this.groupManagerLegacy = groupManagerLegacy;
        this.groupAlertTransferHelper = groupAlertTransferHelper;
        this.headsUpManager = headsUpManager;
        this.headsUpController = headsUpController;
        this.headsUpViewBinder = headsUpViewBinder;
        this.clickerBuilder = clickerBuilder;
        this.animatedImageNotificationManager = animatedImageNotificationManager;
        this.peopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void initialize(@NotNull StatusBar statusBar, @NotNull Optional<Bubbles> bubblesOptional, @NotNull NotificationPresenter presenter, @NotNull NotificationListContainer listContainer, @NotNull NotificationActivityStarter notificationActivityStarter, @NotNull NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(bubblesOptional, "bubblesOptional");
        Intrinsics.checkNotNullParameter(presenter, "presenter");
        Intrinsics.checkNotNullParameter(listContainer, "listContainer");
        Intrinsics.checkNotNullParameter(notificationActivityStarter, "notificationActivityStarter");
        Intrinsics.checkNotNullParameter(bindRowCallback, "bindRowCallback");
        this.notificationListener.registerAsSystemService();
        new NotificationListController(this.entryManager, listContainer, this.deviceProvisionedController).bind();
        this.notificationRowBinder.setNotificationClicker(this.clickerBuilder.build(Optional.of(statusBar), bubblesOptional, notificationActivityStarter));
        this.notificationRowBinder.setUpWithPresenter(presenter, listContainer, bindRowCallback);
        this.headsUpViewBinder.setPresenter(presenter);
        this.notifBindPipelineInitializer.initialize();
        this.animatedImageNotificationManager.bind();
        if (this.featureFlags.isNewNotifPipelineEnabled()) {
            this.newNotifPipeline.get().initialize(this.notificationListener, this.notificationRowBinder, listContainer);
        }
        if (this.featureFlags.isNewNotifPipelineRenderingEnabled()) {
            TargetSdkResolver targetSdkResolver = this.targetSdkResolver;
            NotifPipeline notifPipeline = this.notifPipeline.get();
            Intrinsics.checkNotNullExpressionValue(notifPipeline, "notifPipeline.get()");
            targetSdkResolver.initialize(notifPipeline);
        } else {
            this.targetSdkResolver.initialize(this.entryManager);
            this.remoteInputUriController.attach(this.entryManager);
            this.groupAlertTransferHelper.bind(this.entryManager, this.groupManagerLegacy.get());
            this.headsUpManager.addListener(this.groupManagerLegacy.get());
            this.headsUpManager.addListener(this.groupAlertTransferHelper);
            this.headsUpController.attach(this.entryManager, this.headsUpManager);
            this.groupManagerLegacy.get().setHeadsUpManager(this.headsUpManager);
            this.groupAlertTransferHelper.setHeadsUpManager(this.headsUpManager);
            this.entryManager.setRanker(this.legacyRanker);
            this.entryManager.attach(this.notificationListener);
        }
        this.peopleSpaceWidgetManager.attach(this.notificationListener);
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args, boolean z) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        if (z) {
            this.entryManager.dump(pw, "  ");
        }
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void requestNotificationUpdate(@NotNull String reason) {
        Intrinsics.checkNotNullParameter(reason, "reason");
        this.entryManager.updateNotifications(reason);
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void resetUserExpandedStates() {
        for (NotificationEntry notificationEntry : this.entryManager.getVisibleNotifications()) {
            notificationEntry.resetUserExpansion();
        }
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void setNotificationSnoozed(@NotNull StatusBarNotification sbn, @NotNull NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        Intrinsics.checkNotNullParameter(snoozeOption, "snoozeOption");
        if (snoozeOption.getSnoozeCriterion() != null) {
            this.notificationListener.snoozeNotification(sbn.getKey(), snoozeOption.getSnoozeCriterion().getId());
        } else {
            this.notificationListener.snoozeNotification(sbn.getKey(), snoozeOption.getMinutesToSnoozeFor() * 60 * 1000);
        }
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public int getActiveNotificationsCount() {
        return this.entryManager.getActiveNotificationsCount();
    }
}
