package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationListController;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.Optional;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000þ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 V2\u00020\u0001:\u0001VBó\u0001\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0003\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0003\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0003\u0012\u0006\u0010\u0019\u001a\u00020\u001a\u0012\u0006\u0010\u001b\u001a\u00020\u001c\u0012\u0006\u0010\u001d\u001a\u00020\u001e\u0012\u0006\u0010\u001f\u001a\u00020 \u0012\u0006\u0010!\u001a\u00020\"\u0012\f\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0003\u0012\u0006\u0010%\u001a\u00020&\u0012\u0006\u0010'\u001a\u00020(\u0012\u0006\u0010)\u001a\u00020*\u0012\u0006\u0010+\u001a\u00020,\u0012\u0006\u0010-\u001a\u00020.\u0012\u0006\u0010/\u001a\u000200\u0012\u0006\u00101\u001a\u000202\u0012\f\u00103\u001a\b\u0012\u0004\u0012\u00020504¢\u0006\u0002\u00106J+\u00107\u001a\u0002082\u0006\u00109\u001a\u00020:2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020=0<2\u0006\u0010>\u001a\u00020?H\u0016¢\u0006\u0002\u0010@J\b\u0010A\u001a\u00020BH\u0016J0\u0010C\u001a\u0002082\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020I2\u0006\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u00020MH\u0016J\u0010\u0010N\u001a\u0002082\u0006\u0010O\u001a\u00020=H\u0016J\b\u0010P\u001a\u000208H\u0016J\u0018\u0010Q\u001a\u0002082\u0006\u0010R\u001a\u00020S2\u0006\u0010T\u001a\u00020UH\u0016R\u000e\u0010/\u001a\u000200X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u0014\u00103\u001a\b\u0012\u0004\u0012\u00020504X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000¨\u0006W"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/init/NotificationsControllerImpl;", "Lcom/android/systemui/statusbar/notification/init/NotificationsController;", "centralSurfaces", "Ldagger/Lazy;", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "notifPipelineFlags", "Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "notificationListener", "Lcom/android/systemui/statusbar/NotificationListener;", "entryManager", "Lcom/android/systemui/statusbar/notification/NotificationEntryManager;", "debugModeFilterProvider", "Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;", "legacyRanker", "Lcom/android/systemui/statusbar/notification/collection/NotificationRankingManager;", "commonNotifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "notifPipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "notifLiveDataStore", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;", "targetSdkResolver", "Lcom/android/systemui/statusbar/notification/collection/TargetSdkResolver;", "newNotifPipelineInitializer", "Lcom/android/systemui/statusbar/notification/collection/init/NotifPipelineInitializer;", "notifBindPipelineInitializer", "Lcom/android/systemui/statusbar/notification/row/NotifBindPipelineInitializer;", "deviceProvisionedController", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;", "notificationRowBinder", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotificationRowBinderImpl;", "bindEventManagerImpl", "Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManagerImpl;", "remoteInputUriController", "Lcom/android/systemui/statusbar/policy/RemoteInputUriController;", "groupManagerLegacy", "Lcom/android/systemui/statusbar/notification/collection/legacy/NotificationGroupManagerLegacy;", "groupAlertTransferHelper", "Lcom/android/systemui/statusbar/phone/NotificationGroupAlertTransferHelper;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "headsUpController", "Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;", "headsUpViewBinder", "Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;", "clickerBuilder", "Lcom/android/systemui/statusbar/notification/NotificationClicker$Builder;", "animatedImageNotificationManager", "Lcom/android/systemui/statusbar/notification/AnimatedImageNotificationManager;", "peopleSpaceWidgetManager", "Lcom/android/systemui/people/widget/PeopleSpaceWidgetManager;", "bubblesOptional", "Ljava/util/Optional;", "Lcom/android/wm/shell/bubbles/Bubbles;", "(Ldagger/Lazy;Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;Lcom/android/systemui/statusbar/NotificationListener;Lcom/android/systemui/statusbar/notification/NotificationEntryManager;Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;Lcom/android/systemui/statusbar/notification/collection/NotificationRankingManager;Ldagger/Lazy;Ldagger/Lazy;Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;Lcom/android/systemui/statusbar/notification/collection/TargetSdkResolver;Ldagger/Lazy;Lcom/android/systemui/statusbar/notification/row/NotifBindPipelineInitializer;Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;Lcom/android/systemui/statusbar/notification/collection/inflation/NotificationRowBinderImpl;Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManagerImpl;Lcom/android/systemui/statusbar/policy/RemoteInputUriController;Ldagger/Lazy;Lcom/android/systemui/statusbar/phone/NotificationGroupAlertTransferHelper;Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;Lcom/android/systemui/statusbar/notification/NotificationClicker$Builder;Lcom/android/systemui/statusbar/notification/AnimatedImageNotificationManager;Lcom/android/systemui/people/widget/PeopleSpaceWidgetManager;Ljava/util/Optional;)V", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "dumpTruck", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;Z)V", "getActiveNotificationsCount", "", "initialize", "presenter", "Lcom/android/systemui/statusbar/NotificationPresenter;", "listContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "stackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "notificationActivityStarter", "Lcom/android/systemui/statusbar/notification/NotificationActivityStarter;", "bindRowCallback", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotificationRowBinderImpl$BindRowCallback;", "requestNotificationUpdate", "reason", "resetUserExpandedStates", "setNotificationSnoozed", "sbn", "Landroid/service/notification/StatusBarNotification;", "snoozeOption", "Lcom/android/systemui/plugins/statusbar/NotificationSwipeActionHelper$SnoozeOption;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationsControllerImpl.kt */
public final class NotificationsControllerImpl implements NotificationsController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean INITIALIZE_NEW_PIPELINE = true;
    private final AnimatedImageNotificationManager animatedImageNotificationManager;
    private final BindEventManagerImpl bindEventManagerImpl;
    private final Optional<Bubbles> bubblesOptional;
    private final Lazy<CentralSurfaces> centralSurfaces;
    private final NotificationClicker.Builder clickerBuilder;
    private final Lazy<CommonNotifCollection> commonNotifCollection;
    private final DebugModeFilterProvider debugModeFilterProvider;
    private final DeviceProvisionedController deviceProvisionedController;
    private final NotificationEntryManager entryManager;
    private final NotificationGroupAlertTransferHelper groupAlertTransferHelper;
    private final Lazy<NotificationGroupManagerLegacy> groupManagerLegacy;
    private final HeadsUpController headsUpController;
    private final HeadsUpManager headsUpManager;
    private final HeadsUpViewBinder headsUpViewBinder;
    private final NotificationRankingManager legacyRanker;
    private final Lazy<NotifPipelineInitializer> newNotifPipelineInitializer;
    private final NotifBindPipelineInitializer notifBindPipelineInitializer;
    private final NotifLiveDataStore notifLiveDataStore;
    private final Lazy<NotifPipeline> notifPipeline;
    private final NotifPipelineFlags notifPipelineFlags;
    private final NotificationListener notificationListener;
    private final NotificationRowBinderImpl notificationRowBinder;
    private final PeopleSpaceWidgetManager peopleSpaceWidgetManager;
    private final RemoteInputUriController remoteInputUriController;
    private final TargetSdkResolver targetSdkResolver;

    @Inject
    public NotificationsControllerImpl(Lazy<CentralSurfaces> lazy, NotifPipelineFlags notifPipelineFlags2, NotificationListener notificationListener2, NotificationEntryManager notificationEntryManager, DebugModeFilterProvider debugModeFilterProvider2, NotificationRankingManager notificationRankingManager, Lazy<CommonNotifCollection> lazy2, Lazy<NotifPipeline> lazy3, NotifLiveDataStore notifLiveDataStore2, TargetSdkResolver targetSdkResolver2, Lazy<NotifPipelineInitializer> lazy4, NotifBindPipelineInitializer notifBindPipelineInitializer2, DeviceProvisionedController deviceProvisionedController2, NotificationRowBinderImpl notificationRowBinderImpl, BindEventManagerImpl bindEventManagerImpl2, RemoteInputUriController remoteInputUriController2, Lazy<NotificationGroupManagerLegacy> lazy5, NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, HeadsUpManager headsUpManager2, HeadsUpController headsUpController2, HeadsUpViewBinder headsUpViewBinder2, NotificationClicker.Builder builder, AnimatedImageNotificationManager animatedImageNotificationManager2, PeopleSpaceWidgetManager peopleSpaceWidgetManager2, Optional<Bubbles> optional) {
        Lazy<CentralSurfaces> lazy6 = lazy;
        NotifPipelineFlags notifPipelineFlags3 = notifPipelineFlags2;
        NotificationListener notificationListener3 = notificationListener2;
        NotificationEntryManager notificationEntryManager2 = notificationEntryManager;
        DebugModeFilterProvider debugModeFilterProvider3 = debugModeFilterProvider2;
        NotificationRankingManager notificationRankingManager2 = notificationRankingManager;
        Lazy<CommonNotifCollection> lazy7 = lazy2;
        Lazy<NotifPipeline> lazy8 = lazy3;
        NotifLiveDataStore notifLiveDataStore3 = notifLiveDataStore2;
        TargetSdkResolver targetSdkResolver3 = targetSdkResolver2;
        Lazy<NotifPipelineInitializer> lazy9 = lazy4;
        NotifBindPipelineInitializer notifBindPipelineInitializer3 = notifBindPipelineInitializer2;
        DeviceProvisionedController deviceProvisionedController3 = deviceProvisionedController2;
        NotificationRowBinderImpl notificationRowBinderImpl2 = notificationRowBinderImpl;
        RemoteInputUriController remoteInputUriController3 = remoteInputUriController2;
        Intrinsics.checkNotNullParameter(lazy6, "centralSurfaces");
        Intrinsics.checkNotNullParameter(notifPipelineFlags3, "notifPipelineFlags");
        Intrinsics.checkNotNullParameter(notificationListener3, "notificationListener");
        Intrinsics.checkNotNullParameter(notificationEntryManager2, "entryManager");
        Intrinsics.checkNotNullParameter(debugModeFilterProvider3, "debugModeFilterProvider");
        Intrinsics.checkNotNullParameter(notificationRankingManager2, "legacyRanker");
        Intrinsics.checkNotNullParameter(lazy7, "commonNotifCollection");
        Intrinsics.checkNotNullParameter(lazy8, "notifPipeline");
        Intrinsics.checkNotNullParameter(notifLiveDataStore3, "notifLiveDataStore");
        Intrinsics.checkNotNullParameter(targetSdkResolver3, "targetSdkResolver");
        Intrinsics.checkNotNullParameter(lazy9, "newNotifPipelineInitializer");
        Intrinsics.checkNotNullParameter(notifBindPipelineInitializer3, "notifBindPipelineInitializer");
        Intrinsics.checkNotNullParameter(deviceProvisionedController3, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(notificationRowBinderImpl2, "notificationRowBinder");
        Intrinsics.checkNotNullParameter(bindEventManagerImpl2, "bindEventManagerImpl");
        Intrinsics.checkNotNullParameter(remoteInputUriController2, "remoteInputUriController");
        Intrinsics.checkNotNullParameter(lazy5, "groupManagerLegacy");
        Intrinsics.checkNotNullParameter(notificationGroupAlertTransferHelper, "groupAlertTransferHelper");
        Intrinsics.checkNotNullParameter(headsUpManager2, "headsUpManager");
        Intrinsics.checkNotNullParameter(headsUpController2, "headsUpController");
        Intrinsics.checkNotNullParameter(headsUpViewBinder2, "headsUpViewBinder");
        Intrinsics.checkNotNullParameter(builder, "clickerBuilder");
        Intrinsics.checkNotNullParameter(animatedImageNotificationManager2, "animatedImageNotificationManager");
        Intrinsics.checkNotNullParameter(peopleSpaceWidgetManager2, "peopleSpaceWidgetManager");
        Intrinsics.checkNotNullParameter(optional, "bubblesOptional");
        this.centralSurfaces = lazy6;
        this.notifPipelineFlags = notifPipelineFlags3;
        this.notificationListener = notificationListener3;
        this.entryManager = notificationEntryManager2;
        this.debugModeFilterProvider = debugModeFilterProvider3;
        this.legacyRanker = notificationRankingManager2;
        this.commonNotifCollection = lazy7;
        this.notifPipeline = lazy8;
        this.notifLiveDataStore = notifLiveDataStore3;
        this.targetSdkResolver = targetSdkResolver3;
        this.newNotifPipelineInitializer = lazy9;
        this.notifBindPipelineInitializer = notifBindPipelineInitializer3;
        this.deviceProvisionedController = deviceProvisionedController3;
        this.notificationRowBinder = notificationRowBinderImpl2;
        this.bindEventManagerImpl = bindEventManagerImpl2;
        this.remoteInputUriController = remoteInputUriController2;
        this.groupManagerLegacy = lazy5;
        this.groupAlertTransferHelper = notificationGroupAlertTransferHelper;
        this.headsUpManager = headsUpManager2;
        this.headsUpController = headsUpController2;
        this.headsUpViewBinder = headsUpViewBinder2;
        this.clickerBuilder = builder;
        this.animatedImageNotificationManager = animatedImageNotificationManager2;
        this.peopleSpaceWidgetManager = peopleSpaceWidgetManager2;
        this.bubblesOptional = optional;
    }

    public void initialize(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotifStackController notifStackController, NotificationActivityStarter notificationActivityStarter, NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        Intrinsics.checkNotNullParameter(notificationPresenter, "presenter");
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(notifStackController, "stackController");
        Intrinsics.checkNotNullParameter(notificationActivityStarter, "notificationActivityStarter");
        Intrinsics.checkNotNullParameter(bindRowCallback, "bindRowCallback");
        this.notificationListener.registerAsSystemService();
        new NotificationListController(this.entryManager, notificationListContainer, this.deviceProvisionedController).bind();
        this.notificationRowBinder.setNotificationClicker(this.clickerBuilder.build(Optional.m1745of(this.centralSurfaces.get()), this.bubblesOptional, notificationActivityStarter));
        this.notificationRowBinder.setUpWithPresenter(notificationPresenter, notificationListContainer, bindRowCallback);
        this.headsUpViewBinder.setPresenter(notificationPresenter);
        this.notifBindPipelineInitializer.initialize();
        this.animatedImageNotificationManager.bind();
        this.newNotifPipelineInitializer.get().initialize(this.notificationListener, this.notificationRowBinder, notificationListContainer, notifStackController);
        if (this.notifPipelineFlags.isNewPipelineEnabled()) {
            TargetSdkResolver targetSdkResolver2 = this.targetSdkResolver;
            NotifPipeline notifPipeline2 = this.notifPipeline.get();
            Intrinsics.checkNotNullExpressionValue(notifPipeline2, "notifPipeline.get()");
            targetSdkResolver2.initialize(notifPipeline2);
        } else {
            this.targetSdkResolver.initialize(this.entryManager);
            this.remoteInputUriController.attach(this.entryManager);
            this.groupAlertTransferHelper.bind(this.entryManager, this.groupManagerLegacy.get());
            this.bindEventManagerImpl.attachToLegacyPipeline(this.entryManager);
            this.headsUpManager.addListener(this.groupManagerLegacy.get());
            this.headsUpManager.addListener(this.groupAlertTransferHelper);
            this.headsUpController.attach(this.entryManager, this.headsUpManager);
            this.groupManagerLegacy.get().setHeadsUpManager(this.headsUpManager);
            this.groupAlertTransferHelper.setHeadsUpManager(this.headsUpManager);
            this.debugModeFilterProvider.registerInvalidationListener(new NotificationsControllerImpl$$ExternalSyntheticLambda0(this));
            this.entryManager.initialize(this.notificationListener, this.legacyRanker);
        }
        this.peopleSpaceWidgetManager.attach(this.notificationListener);
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-0  reason: not valid java name */
    public static final void m3120initialize$lambda0(NotificationsControllerImpl notificationsControllerImpl) {
        Intrinsics.checkNotNullParameter(notificationsControllerImpl, "this$0");
        notificationsControllerImpl.entryManager.updateNotifications("debug mode filter changed");
    }

    public void dump(PrintWriter printWriter, String[] strArr, boolean z) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        if (z) {
            this.entryManager.dump(printWriter, "  ");
        }
    }

    public void requestNotificationUpdate(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        this.entryManager.updateNotifications(str);
    }

    public void resetUserExpandedStates() {
        for (NotificationEntry resetUserExpansion : this.commonNotifCollection.get().getAllNotifs()) {
            resetUserExpansion.resetUserExpansion();
        }
    }

    public void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(snoozeOption, "snoozeOption");
        if (snoozeOption.getSnoozeCriterion() != null) {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), snoozeOption.getSnoozeCriterion().getId());
        } else {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), ((long) (snoozeOption.getMinutesToSnoozeFor() * 60)) * 1000);
        }
    }

    public int getActiveNotificationsCount() {
        return this.notifLiveDataStore.getActiveNotifCount().getValue().intValue();
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/init/NotificationsControllerImpl$Companion;", "", "()V", "INITIALIZE_NEW_PIPELINE", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotificationsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
