package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import com.android.systemui.statusbar.notification.NotificationUtilsKt;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0012\u0010\r\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u0010J\u001a\u0010\u0011\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013J\u0012\u0010\u0014\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u0010J\u0012\u0010\u0015\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u0010J\u001a\u0010\u0016\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u000bJ$\u0010\u0018\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0019\u001a\u0004\u0018\u00010\bJ\u0012\u0010\u001a\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u0010J\u0012\u0010\u001b\u001a\u00020\u00062\n\u0010\u000e\u001a\u00060\u000fR\u00020\u0010J\u0010\u0010\u001c\u001a\u00020\u00062\b\u0010\u001d\u001a\u0004\u0018\u00010\u0013J\u0016\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!J\u001e\u0010\"\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!2\u0006\u0010#\u001a\u00020\u000bJ\u0016\u0010$\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u000bJ\u000e\u0010&\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u000e\u0010'\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u000e\u0010(\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u000e\u0010)\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u0016\u0010*\u001a\u00020\u00062\u0006\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020\u000bJ\u001e\u0010-\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010.\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u0013J\u001e\u0010/\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010.\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u0013J\u000e\u00100\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u000e\u00101\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u0016\u00102\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u000bJ\u000e\u00103\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0013J\u0016\u00104\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u00105\u001a\u000206J\u000e\u00107\u001a\u00020\u00062\u0006\u00108\u001a\u000209J\u0016\u0010:\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u00108\u001a\u000209R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006;"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logChildDismissed", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "logDismissAll", "userId", "", "logDismissOnAlreadyCanceledEntry", "logFutureDismissalAlreadyCancelledByServer", "dismissal", "Lcom/android/systemui/statusbar/notification/collection/NotifCollection$FutureDismissal;", "Lcom/android/systemui/statusbar/notification/collection/NotifCollection;", "logFutureDismissalDismissing", "type", "", "logFutureDismissalDoubleCancelledByServer", "logFutureDismissalDoubleRun", "logFutureDismissalGotSystemServerCancel", "cancellationReason", "logFutureDismissalMismatchedEntry", "latestEntry", "logFutureDismissalRegistered", "logFutureDismissalReused", "logIgnoredError", "message", "logLifetimeExtended", "key", "extender", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "logLifetimeExtensionEnded", "totalExtenders", "logNoNotificationToRemoveWithKey", "reason", "logNonExistentNotifDismissed", "logNotifClearAllDismissalIntercepted", "logNotifDismissed", "logNotifDismissedIntercepted", "logNotifGroupPosted", "groupKey", "batchSize", "logNotifInternalUpdate", "name", "logNotifInternalUpdateFailed", "logNotifPosted", "logNotifReleased", "logNotifRemoved", "logNotifUpdated", "logRankingMissing", "rankingMap", "Landroid/service/notification/NotificationListenerService$RankingMap;", "logRemoteExceptionOnClearAllNotifications", "e", "Landroid/os/RemoteException;", "logRemoteExceptionOnNotificationClear", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger {
    private final LogBuffer buffer;

    @Inject
    public NotifCollectionLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logNotifPosted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifPosted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifGroupPosted(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifGroupPosted$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifUpdated(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifUpdated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifRemoved(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifReleased(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifReleased$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifDismissed(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifDismissed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNonExistentNotifDismissed(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNonExistentNotifDismissed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logChildDismissed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logChildDismissed$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        logBuffer.commit(obtain);
    }

    public final void logDismissAll(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logDismissAll$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDismissOnAlreadyCanceledEntry(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNotifDismissedIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifDismissedIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifClearAllDismissalIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifInternalUpdate(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(str3, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifInternalUpdate$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifInternalUpdateFailed(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(str3, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifInternalUpdateFailed$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNoNotificationToRemoveWithKey(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.ERROR, NotifCollectionLogger$logNoNotificationToRemoveWithKey$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logRankingMissing(String str, NotificationListenerService.RankingMap rankingMap) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logRankingMissing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logRankingMissing$4.INSTANCE));
        String[] orderedKeys = rankingMap.getOrderedKeys();
        Intrinsics.checkNotNullExpressionValue(orderedKeys, "rankingMap.orderedKeys");
        for (String str1 : orderedKeys) {
            LogBuffer logBuffer3 = this.buffer;
            LogMessage obtain2 = logBuffer3.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logRankingMissing$6.INSTANCE);
            obtain2.setStr1(str1);
            logBuffer3.commit(obtain2);
        }
    }

    public final void logRemoteExceptionOnNotificationClear(String str, RemoteException remoteException) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(remoteException, "e");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WTF, NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(remoteException.toString());
        logBuffer.commit(obtain);
    }

    public final void logRemoteExceptionOnClearAllNotifications(RemoteException remoteException) {
        Intrinsics.checkNotNullParameter(remoteException, "e");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WTF, C2713x8e3a9df8.INSTANCE);
        obtain.setStr1(remoteException.toString());
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtended(String str, NotifLifetimeExtender notifLifetimeExtender) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notifLifetimeExtender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logLifetimeExtended$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(notifLifetimeExtender.getName());
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtensionEnded(String str, NotifLifetimeExtender notifLifetimeExtender, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notifLifetimeExtender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logLifetimeExtensionEnded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(notifLifetimeExtender.getName());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logIgnoredError(String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.ERROR, NotifCollectionLogger$logIgnoredError$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalReused(NotifCollection.FutureDismissal futureDismissal) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logFutureDismissalReused$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalRegistered(NotifCollection.FutureDismissal futureDismissal) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalRegistered$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDoubleCancelledByServer(NotifCollection.FutureDismissal futureDismissal) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, C2712x6470ec54.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDoubleRun(NotifCollection.FutureDismissal futureDismissal) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logFutureDismissalDoubleRun$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalAlreadyCancelledByServer(NotifCollection.FutureDismissal futureDismissal) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, C2711x8a4d0b95.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalGotSystemServerCancel(NotifCollection.FutureDismissal futureDismissal, int i) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalGotSystemServerCancel$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDismissing(NotifCollection.FutureDismissal futureDismissal, String str) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        Intrinsics.checkNotNullParameter(str, "type");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalDismissing$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalMismatchedEntry(NotifCollection.FutureDismissal futureDismissal, String str, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(futureDismissal, "dismissal");
        Intrinsics.checkNotNullParameter(str, "type");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logFutureDismissalMismatchedEntry$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setStr2(str);
        obtain.setStr3(NotificationUtilsKt.getLogKey((ListEntry) notificationEntry));
        logBuffer.commit(obtain);
    }
}
