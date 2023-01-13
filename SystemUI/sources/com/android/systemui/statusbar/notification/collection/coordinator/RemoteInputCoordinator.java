package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@CoordinatorScope
@Metadata(mo65042d1 = {"\u0000¨\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0003CDEB1\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0016J%\u0010,\u001a\u00020)2\u0006\u0010-\u001a\u00020.2\u000e\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020100H\u0016¢\u0006\u0002\u00102J\f\u00103\u001a\b\u0012\u0004\u0012\u0002040!J\u0010\u00105\u001a\u0002062\u0006\u00107\u001a\u000201H\u0016J\b\u00108\u001a\u00020)H\u0016J\u0010\u00109\u001a\u00020)2\u0006\u0010:\u001a\u00020;H\u0016J\u0018\u0010<\u001a\u00020)2\u0006\u0010:\u001a\u00020;2\u0006\u0010=\u001a\u00020>H\u0002J\u0010\u0010?\u001a\u00020)2\u0006\u0010:\u001a\u00020;H\u0016J\u0010\u0010@\u001a\u00020)2\u0006\u0010A\u001a\u00020BH\u0016R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R \u0010\u0015\u001a\u00060\u0016R\u00020\u00008\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR \u0010\u001b\u001a\u00060\u001cR\u00020\u00008\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u001d\u0010\u0018\u001a\u0004\b\u001e\u0010\u001fR\u0014\u0010 \u001a\b\u0012\u0004\u0012\u00020\"0!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R \u0010#\u001a\u00060$R\u00020\u00008\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b%\u0010\u0018\u001a\u0004\b&\u0010'¨\u0006F"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager$RemoteInputListener;", "Lcom/android/systemui/Dumpable;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "mRebuilder", "Lcom/android/systemui/statusbar/RemoteInputNotificationRebuilder;", "mNotificationRemoteInputManager", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager;", "mMainHandler", "Landroid/os/Handler;", "mSmartReplyController", "Lcom/android/systemui/statusbar/SmartReplyController;", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/statusbar/RemoteInputNotificationRebuilder;Lcom/android/systemui/statusbar/NotificationRemoteInputManager;Landroid/os/Handler;Lcom/android/systemui/statusbar/SmartReplyController;)V", "mCollectionListener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "getMCollectionListener", "()Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "mNotifUpdater", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/InternalNotifUpdater;", "mRemoteInputActiveExtender", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputActiveExtender;", "getMRemoteInputActiveExtender$annotations", "()V", "getMRemoteInputActiveExtender", "()Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputActiveExtender;", "mRemoteInputHistoryExtender", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputHistoryExtender;", "getMRemoteInputHistoryExtender$annotations", "getMRemoteInputHistoryExtender", "()Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputHistoryExtender;", "mRemoteInputLifetimeExtenders", "", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/SelfTrackingLifetimeExtender;", "mSmartReplyHistoryExtender", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$SmartReplyHistoryExtender;", "getMSmartReplyHistoryExtender$annotations", "getMSmartReplyHistoryExtender", "()Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$SmartReplyHistoryExtender;", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getLifetimeExtenders", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "isNotificationKeptForRemoteInputHistory", "", "key", "onPanelCollapsed", "onRemoteInputSent", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onSmartReplySent", "reply", "", "releaseNotificationIfKeptForRemoteInputHistory", "setRemoteInputController", "remoteInputController", "Lcom/android/systemui/statusbar/RemoteInputController;", "RemoteInputActiveExtender", "RemoteInputHistoryExtender", "SmartReplyHistoryExtender", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinator implements Coordinator, NotificationRemoteInputManager.RemoteInputListener, Dumpable {
    private final NotifCollectionListener mCollectionListener = new RemoteInputCoordinator$mCollectionListener$1(this);
    /* access modifiers changed from: private */
    public final Handler mMainHandler;
    /* access modifiers changed from: private */
    public InternalNotifUpdater mNotifUpdater;
    /* access modifiers changed from: private */
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    /* access modifiers changed from: private */
    public final RemoteInputNotificationRebuilder mRebuilder;
    private final RemoteInputActiveExtender mRemoteInputActiveExtender;
    private final RemoteInputHistoryExtender mRemoteInputHistoryExtender;
    private final List<SelfTrackingLifetimeExtender> mRemoteInputLifetimeExtenders;
    /* access modifiers changed from: private */
    public final SmartReplyController mSmartReplyController;
    private final SmartReplyHistoryExtender mSmartReplyHistoryExtender;

    public static /* synthetic */ void getMRemoteInputActiveExtender$annotations() {
    }

    public static /* synthetic */ void getMRemoteInputHistoryExtender$annotations() {
    }

    public static /* synthetic */ void getMSmartReplyHistoryExtender$annotations() {
    }

    @Inject
    public RemoteInputCoordinator(DumpManager dumpManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, NotificationRemoteInputManager notificationRemoteInputManager, @Main Handler handler, SmartReplyController smartReplyController) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(remoteInputNotificationRebuilder, "mRebuilder");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "mNotificationRemoteInputManager");
        Intrinsics.checkNotNullParameter(handler, "mMainHandler");
        Intrinsics.checkNotNullParameter(smartReplyController, "mSmartReplyController");
        this.mRebuilder = remoteInputNotificationRebuilder;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mMainHandler = handler;
        this.mSmartReplyController = smartReplyController;
        RemoteInputHistoryExtender remoteInputHistoryExtender = new RemoteInputHistoryExtender();
        this.mRemoteInputHistoryExtender = remoteInputHistoryExtender;
        SmartReplyHistoryExtender smartReplyHistoryExtender = new SmartReplyHistoryExtender();
        this.mSmartReplyHistoryExtender = smartReplyHistoryExtender;
        RemoteInputActiveExtender remoteInputActiveExtender = new RemoteInputActiveExtender();
        this.mRemoteInputActiveExtender = remoteInputActiveExtender;
        this.mRemoteInputLifetimeExtenders = CollectionsKt.listOf(remoteInputHistoryExtender, smartReplyHistoryExtender, remoteInputActiveExtender);
        dumpManager.registerDumpable(this);
    }

    public final RemoteInputHistoryExtender getMRemoteInputHistoryExtender() {
        return this.mRemoteInputHistoryExtender;
    }

    public final SmartReplyHistoryExtender getMSmartReplyHistoryExtender() {
        return this.mSmartReplyHistoryExtender;
    }

    public final RemoteInputActiveExtender getMRemoteInputActiveExtender() {
        return this.mRemoteInputActiveExtender;
    }

    public final List<NotifLifetimeExtender> getLifetimeExtenders() {
        return this.mRemoteInputLifetimeExtenders;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        this.mNotificationRemoteInputManager.setRemoteInputListener(this);
        for (SelfTrackingLifetimeExtender addNotificationLifetimeExtender : this.mRemoteInputLifetimeExtenders) {
            notifPipeline.addNotificationLifetimeExtender(addNotificationLifetimeExtender);
        }
        this.mNotifUpdater = notifPipeline.getInternalNotifUpdater("RemoteInputCoordinator");
        notifPipeline.addCollectionListener(this.mCollectionListener);
    }

    public final NotifCollectionListener getMCollectionListener() {
        return this.mCollectionListener;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        for (SelfTrackingLifetimeExtender dump : this.mRemoteInputLifetimeExtenders) {
            dump.dump(printWriter, strArr);
        }
    }

    public void onRemoteInputSent(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "onRemoteInputSent(entry=" + notificationEntry.getKey() + ')');
        }
        RemoteInputHistoryExtender remoteInputHistoryExtender = this.mRemoteInputHistoryExtender;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        remoteInputHistoryExtender.endLifetimeExtension(key);
        SmartReplyHistoryExtender smartReplyHistoryExtender = this.mSmartReplyHistoryExtender;
        String key2 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key2, "entry.key");
        smartReplyHistoryExtender.endLifetimeExtension(key2);
        RemoteInputActiveExtender remoteInputActiveExtender = this.mRemoteInputActiveExtender;
        String key3 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key3, "entry.key");
        remoteInputActiveExtender.endLifetimeExtensionAfterDelay(key3, 500);
    }

    /* access modifiers changed from: private */
    public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "onSmartReplySent(entry=" + notificationEntry.getKey() + ')');
        }
        StatusBarNotification rebuildForSendingSmartReply = this.mRebuilder.rebuildForSendingSmartReply(notificationEntry, charSequence);
        Intrinsics.checkNotNullExpressionValue(rebuildForSendingSmartReply, "mRebuilder.rebuildForSen…gSmartReply(entry, reply)");
        InternalNotifUpdater internalNotifUpdater = this.mNotifUpdater;
        if (internalNotifUpdater == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mNotifUpdater");
            internalNotifUpdater = null;
        }
        internalNotifUpdater.onInternalNotificationUpdate(rebuildForSendingSmartReply, "Adding smart reply spinner for sent");
        RemoteInputActiveExtender remoteInputActiveExtender = this.mRemoteInputActiveExtender;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        remoteInputActiveExtender.endLifetimeExtensionAfterDelay(key, 500);
    }

    public void onPanelCollapsed() {
        this.mRemoteInputActiveExtender.endAllLifetimeExtensions();
    }

    public boolean isNotificationKeptForRemoteInputHistory(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        return this.mRemoteInputHistoryExtender.isExtending(str) || this.mSmartReplyHistoryExtender.isExtending(str);
    }

    public void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "releaseNotificationIfKeptForRemoteInputHistory(entry=" + notificationEntry.getKey() + ')');
        }
        RemoteInputHistoryExtender remoteInputHistoryExtender = this.mRemoteInputHistoryExtender;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        remoteInputHistoryExtender.endLifetimeExtensionAfterDelay(key, 200);
        SmartReplyHistoryExtender smartReplyHistoryExtender = this.mSmartReplyHistoryExtender;
        String key2 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key2, "entry.key");
        smartReplyHistoryExtender.endLifetimeExtensionAfterDelay(key2, 200);
        RemoteInputActiveExtender remoteInputActiveExtender = this.mRemoteInputActiveExtender;
        String key3 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key3, "entry.key");
        remoteInputActiveExtender.endLifetimeExtensionAfterDelay(key3, 200);
    }

    public void setRemoteInputController(RemoteInputController remoteInputController) {
        Intrinsics.checkNotNullParameter(remoteInputController, "remoteInputController");
        this.mSmartReplyController.setCallback(new RemoteInputCoordinator$$ExternalSyntheticLambda0(this));
    }

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputHistoryExtender;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/SelfTrackingLifetimeExtender;", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;)V", "onStartedLifetimeExtension", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "queryShouldExtendLifetime", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: RemoteInputCoordinator.kt */
    public final class RemoteInputHistoryExtender extends SelfTrackingLifetimeExtender {
        public RemoteInputHistoryExtender() {
            super("RemoteInputCoordinator", "RemoteInputHistory", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForRemoteInputHistory(notificationEntry);
        }

        public void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            StatusBarNotification rebuildForRemoteInputReply = RemoteInputCoordinator.this.mRebuilder.rebuildForRemoteInputReply(notificationEntry);
            Intrinsics.checkNotNullExpressionValue(rebuildForRemoteInputReply, "mRebuilder.rebuildForRemoteInputReply(entry)");
            notificationEntry.onRemoteInputInserted();
            InternalNotifUpdater access$getMNotifUpdater$p = RemoteInputCoordinator.this.mNotifUpdater;
            if (access$getMNotifUpdater$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mNotifUpdater");
                access$getMNotifUpdater$p = null;
            }
            access$getMNotifUpdater$p.onInternalNotificationUpdate(rebuildForRemoteInputReply, "Extending lifetime of notification with remote input");
        }
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$SmartReplyHistoryExtender;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/SelfTrackingLifetimeExtender;", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;)V", "onCanceledLifetimeExtension", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onStartedLifetimeExtension", "queryShouldExtendLifetime", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: RemoteInputCoordinator.kt */
    public final class SmartReplyHistoryExtender extends SelfTrackingLifetimeExtender {
        public SmartReplyHistoryExtender() {
            super("RemoteInputCoordinator", "SmartReplyHistory", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForSmartReplyHistory(notificationEntry);
        }

        public void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            StatusBarNotification rebuildForCanceledSmartReplies = RemoteInputCoordinator.this.mRebuilder.rebuildForCanceledSmartReplies(notificationEntry);
            Intrinsics.checkNotNullExpressionValue(rebuildForCanceledSmartReplies, "mRebuilder.rebuildForCanceledSmartReplies(entry)");
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
            InternalNotifUpdater access$getMNotifUpdater$p = RemoteInputCoordinator.this.mNotifUpdater;
            if (access$getMNotifUpdater$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mNotifUpdater");
                access$getMNotifUpdater$p = null;
            }
            access$getMNotifUpdater$p.onInternalNotificationUpdate(rebuildForCanceledSmartReplies, "Extending lifetime of notification with smart reply");
        }

        public void onCanceledLifetimeExtension(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$RemoteInputActiveExtender;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/SelfTrackingLifetimeExtender;", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;)V", "queryShouldExtendLifetime", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: RemoteInputCoordinator.kt */
    public final class RemoteInputActiveExtender extends SelfTrackingLifetimeExtender {
        public RemoteInputActiveExtender() {
            super("RemoteInputCoordinator", "RemoteInputActive", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.isRemoteInputActive(notificationEntry);
        }
    }
}
