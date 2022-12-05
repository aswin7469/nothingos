package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger {
    @NotNull
    private final LogBuffer buffer;

    public NotifCollectionLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logNotifPosted(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifPosted$2 notifCollectionLogger$logNotifPosted$2 = NotifCollectionLogger$logNotifPosted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifPosted$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifGroupPosted(@NotNull String groupKey, int i) {
        Intrinsics.checkNotNullParameter(groupKey, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifGroupPosted$2 notifCollectionLogger$logNotifGroupPosted$2 = NotifCollectionLogger$logNotifGroupPosted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifGroupPosted$2);
            obtain.setStr1(groupKey);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifUpdated(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifUpdated$2 notifCollectionLogger$logNotifUpdated$2 = NotifCollectionLogger$logNotifUpdated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifUpdated$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifRemoved(@NotNull String key, int i) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifRemoved$2 notifCollectionLogger$logNotifRemoved$2 = NotifCollectionLogger$logNotifRemoved$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifRemoved$2);
            obtain.setStr1(key);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifReleased(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifReleased$2 notifCollectionLogger$logNotifReleased$2 = NotifCollectionLogger$logNotifReleased$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifReleased$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissed(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissed$2 notifCollectionLogger$logNotifDismissed$2 = NotifCollectionLogger$logNotifDismissed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissed$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logChildDismissed(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logChildDismissed$2 notifCollectionLogger$logChildDismissed$2 = NotifCollectionLogger$logChildDismissed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logChildDismissed$2);
            obtain.setStr1(entry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logDismissAll(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logDismissAll$2 notifCollectionLogger$logDismissAll$2 = NotifCollectionLogger$logDismissAll$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissAll$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logDismissOnAlreadyCanceledEntry(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 = NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2);
            obtain.setStr1(entry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissedIntercepted(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissedIntercepted$2 notifCollectionLogger$logNotifDismissedIntercepted$2 = NotifCollectionLogger$logNotifDismissedIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissedIntercepted$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifClearAllDismissalIntercepted(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2 notifCollectionLogger$logNotifClearAllDismissalIntercepted$2 = NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifClearAllDismissalIntercepted$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logNoNotificationToRemoveWithKey(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.ERROR;
        NotifCollectionLogger$logNoNotificationToRemoveWithKey$2 notifCollectionLogger$logNoNotificationToRemoveWithKey$2 = NotifCollectionLogger$logNoNotificationToRemoveWithKey$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNoNotificationToRemoveWithKey$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
    }

    public final void logRankingMissing(@NotNull String key, @NotNull NotificationListenerService.RankingMap rankingMap) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        NotifCollectionLogger$logRankingMissing$2 notifCollectionLogger$logRankingMissing$2 = NotifCollectionLogger$logRankingMissing$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRankingMissing$2);
            obtain.setStr1(key);
            logBuffer.push(obtain);
        }
        LogBuffer logBuffer2 = this.buffer;
        LogLevel logLevel2 = LogLevel.DEBUG;
        NotifCollectionLogger$logRankingMissing$4 notifCollectionLogger$logRankingMissing$4 = NotifCollectionLogger$logRankingMissing$4.INSTANCE;
        if (!logBuffer2.getFrozen()) {
            logBuffer2.push(logBuffer2.obtain("NotifCollection", logLevel2, notifCollectionLogger$logRankingMissing$4));
        }
        String[] orderedKeys = rankingMap.getOrderedKeys();
        Intrinsics.checkNotNullExpressionValue(orderedKeys, "rankingMap.orderedKeys");
        int i = 0;
        int length = orderedKeys.length;
        while (i < length) {
            String str = orderedKeys[i];
            i++;
            LogBuffer logBuffer3 = this.buffer;
            LogLevel logLevel3 = LogLevel.DEBUG;
            NotifCollectionLogger$logRankingMissing$6 notifCollectionLogger$logRankingMissing$6 = NotifCollectionLogger$logRankingMissing$6.INSTANCE;
            if (!logBuffer3.getFrozen()) {
                LogMessageImpl obtain2 = logBuffer3.obtain("NotifCollection", logLevel3, notifCollectionLogger$logRankingMissing$6);
                obtain2.setStr1(str);
                logBuffer3.push(obtain2);
            }
        }
    }

    public final void logRemoteExceptionOnNotificationClear(@NotNull String key, @NotNull RemoteException e) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(e, "e");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2 notifCollectionLogger$logRemoteExceptionOnNotificationClear$2 = NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnNotificationClear$2);
            obtain.setStr1(key);
            obtain.setStr2(e.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logRemoteExceptionOnClearAllNotifications(@NotNull RemoteException e) {
        Intrinsics.checkNotNullParameter(e, "e");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 = NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2);
            obtain.setStr1(e.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(@NotNull String key, @NotNull NotifLifetimeExtender extender) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(extender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtended$2 notifCollectionLogger$logLifetimeExtended$2 = NotifCollectionLogger$logLifetimeExtended$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtended$2);
            obtain.setStr1(key);
            obtain.setStr2(extender.getName());
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtensionEnded(@NotNull String key, @NotNull NotifLifetimeExtender extender, int i) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(extender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtensionEnded$2 notifCollectionLogger$logLifetimeExtensionEnded$2 = NotifCollectionLogger$logLifetimeExtensionEnded$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtensionEnded$2);
            obtain.setStr1(key);
            obtain.setStr2(extender.getName());
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }
}
