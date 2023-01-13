package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J+\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J0\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u000bH\u0016J\b\u0010\u001e\u001a\u00020\u0006H\u0016J\u0018\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/init/NotificationsControllerStub;", "Lcom/android/systemui/statusbar/notification/init/NotificationsController;", "notificationListener", "Lcom/android/systemui/statusbar/NotificationListener;", "(Lcom/android/systemui/statusbar/NotificationListener;)V", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "dumpTruck", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;Z)V", "getActiveNotificationsCount", "", "initialize", "presenter", "Lcom/android/systemui/statusbar/NotificationPresenter;", "listContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "stackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "notificationActivityStarter", "Lcom/android/systemui/statusbar/notification/NotificationActivityStarter;", "bindRowCallback", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotificationRowBinderImpl$BindRowCallback;", "requestNotificationUpdate", "reason", "resetUserExpandedStates", "setNotificationSnoozed", "sbn", "Landroid/service/notification/StatusBarNotification;", "snoozeOption", "Lcom/android/systemui/plugins/statusbar/NotificationSwipeActionHelper$SnoozeOption;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationsControllerStub.kt */
public final class NotificationsControllerStub implements NotificationsController {
    private final NotificationListener notificationListener;

    public int getActiveNotificationsCount() {
        return 0;
    }

    public void requestNotificationUpdate(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
    }

    public void resetUserExpandedStates() {
    }

    public void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(snoozeOption, "snoozeOption");
    }

    @Inject
    public NotificationsControllerStub(NotificationListener notificationListener2) {
        Intrinsics.checkNotNullParameter(notificationListener2, "notificationListener");
        this.notificationListener = notificationListener2;
    }

    public void initialize(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotifStackController notifStackController, NotificationActivityStarter notificationActivityStarter, NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        Intrinsics.checkNotNullParameter(notificationPresenter, "presenter");
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(notifStackController, "stackController");
        Intrinsics.checkNotNullParameter(notificationActivityStarter, "notificationActivityStarter");
        Intrinsics.checkNotNullParameter(bindRowCallback, "bindRowCallback");
        this.notificationListener.registerAsSystemService();
    }

    public void dump(PrintWriter printWriter, String[] strArr, boolean z) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println();
        printWriter.println("Notification handling disabled");
        printWriter.println();
    }
}
