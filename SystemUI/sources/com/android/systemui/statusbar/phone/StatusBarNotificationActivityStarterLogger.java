package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotifInteractionLog;
import com.android.systemui.statusbar.notification.NotificationUtilsKt;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u0012\u0010\u0014\u001a\u00020\u00062\n\u0010\u0015\u001a\u00060\u0016j\u0002`\u0017J\u000e\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarNotificationActivityStarterLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logExpandingBubble", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "logFullScreenIntentNotImportantEnough", "logFullScreenIntentSuppressedByDnD", "logHandleClickAfterKeyguardDismissed", "logHandleClickAfterPanelCollapsed", "logNonClickableNotification", "logSendPendingIntent", "pendingIntent", "Landroid/app/PendingIntent;", "result", "", "logSendingFullScreenIntent", "logSendingIntentFailed", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "logStartNotificationIntent", "logStartingActivityFromClick", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
public final class StatusBarNotificationActivityStarterLogger {
    private final LogBuffer buffer;

    @Inject
    public StatusBarNotificationActivityStarterLogger(@NotifInteractionLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logStartingActivityFromClick(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, C3112xbe0e6f79.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logHandleClickAfterKeyguardDismissed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, C3105x5700bdb1.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logHandleClickAfterPanelCollapsed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, C3106xe49d9e41.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logStartNotificationIntent(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, C3111x1850e613.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendPendingIntent(NotificationEntry notificationEntry, PendingIntent pendingIntent, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, C3108xf7e3e395.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        obtain.setStr2(pendingIntent.getIntent().toString());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logExpandingBubble(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logExpandingBubble$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendingIntentFailed(Exception exc) {
        Intrinsics.checkNotNullParameter(exc, "e");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.WARNING, C3110x40a889d.INSTANCE);
        obtain.setStr1(exc.toString());
        logBuffer.commit(obtain);
    }

    public final void logNonClickableNotification(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.ERROR, C3107x823c33d2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logFullScreenIntentSuppressedByDnD(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, C3104x7a36b302.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logFullScreenIntentNotImportantEnough(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, C3103x141720c8.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendingFullScreenIntent(NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, C3109x57d5767b.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        obtain.setStr2(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }
}
