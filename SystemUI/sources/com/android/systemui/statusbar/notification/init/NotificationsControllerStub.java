package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationsControllerStub.kt */
/* loaded from: classes.dex */
public final class NotificationsControllerStub implements NotificationsController {
    @NotNull
    private final NotificationListener notificationListener;

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public int getActiveNotificationsCount() {
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void requestNotificationUpdate(@NotNull String reason) {
        Intrinsics.checkNotNullParameter(reason, "reason");
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void resetUserExpandedStates() {
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void setNotificationSnoozed(@NotNull StatusBarNotification sbn, @NotNull NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        Intrinsics.checkNotNullParameter(snoozeOption, "snoozeOption");
    }

    public NotificationsControllerStub(@NotNull NotificationListener notificationListener) {
        Intrinsics.checkNotNullParameter(notificationListener, "notificationListener");
        this.notificationListener = notificationListener;
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
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args, boolean z) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println();
        pw.println("Notification handling disabled");
        pw.println();
    }
}
