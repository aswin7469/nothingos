package com.android.systemui.statusbar;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotifInteractionLog;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\f\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bJ\u0018\u0010\r\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/ActionClickLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logInitialClick", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "pendingIntent", "Landroid/app/PendingIntent;", "logKeyguardGone", "logRemoteInputWasHandled", "logStartingIntentWithDefaultHandler", "logWaitingToCloseKeyguard", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ActionClickLogger.kt */
public final class ActionClickLogger {
    private final LogBuffer buffer;

    @Inject
    public ActionClickLogger(@NotifInteractionLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logInitialClick(NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        NotificationListenerService.Ranking ranking;
        NotificationChannel channel;
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logInitialClick$2.INSTANCE);
        String str = null;
        obtain.setStr1(notificationEntry != null ? notificationEntry.getKey() : null);
        if (!(notificationEntry == null || (ranking = notificationEntry.getRanking()) == null || (channel = ranking.getChannel()) == null)) {
            str = channel.getId();
        }
        obtain.setStr2(str);
        obtain.setStr3(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logRemoteInputWasHandled(NotificationEntry notificationEntry) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logRemoteInputWasHandled$2.INSTANCE);
        if (notificationEntry != null) {
            str = notificationEntry.getKey();
        } else {
            str = null;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logStartingIntentWithDefaultHandler(NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logStartingIntentWithDefaultHandler$2.INSTANCE);
        obtain.setStr1(notificationEntry != null ? notificationEntry.getKey() : null);
        obtain.setStr2(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logWaitingToCloseKeyguard(PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logWaitingToCloseKeyguard$2.INSTANCE);
        obtain.setStr1(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logKeyguardGone(PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logKeyguardGone$2.INSTANCE);
        obtain.setStr1(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }
}
