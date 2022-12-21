package com.android.systemui.statusbar;

import android.app.Notification;
import android.app.RemoteInputHistoryItem;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.inject.Inject;

@SysUISingleton
public class RemoteInputNotificationRebuilder {
    private final Context mContext;

    @Inject
    RemoteInputNotificationRebuilder(Context context) {
        this.mContext = context;
    }

    public StatusBarNotification rebuildForSendingSmartReply(NotificationEntry notificationEntry, CharSequence charSequence) {
        return rebuildWithRemoteInputInserted(notificationEntry, charSequence, true, (String) null, (Uri) null);
    }

    public StatusBarNotification rebuildForCanceledSmartReplies(NotificationEntry notificationEntry) {
        return rebuildWithRemoteInputInserted(notificationEntry, (CharSequence) null, false, (String) null, (Uri) null);
    }

    public StatusBarNotification rebuildForRemoteInputReply(NotificationEntry notificationEntry) {
        CharSequence charSequence = notificationEntry.remoteInputText;
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = notificationEntry.remoteInputTextWhenReset;
        }
        return rebuildWithRemoteInputInserted(notificationEntry, charSequence, false, notificationEntry.remoteInputMimeType, notificationEntry.remoteInputUri);
    }

    /* access modifiers changed from: package-private */
    public StatusBarNotification rebuildWithRemoteInputInserted(NotificationEntry notificationEntry, CharSequence charSequence, boolean z, String str, Uri uri) {
        RemoteInputHistoryItem remoteInputHistoryItem;
        CharSequence charSequence2 = charSequence;
        Uri uri2 = uri;
        StatusBarNotification sbn = notificationEntry.getSbn();
        Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(this.mContext, sbn.getNotification().clone());
        if (!(charSequence2 == null && uri2 == null)) {
            if (uri2 != null) {
                remoteInputHistoryItem = new RemoteInputHistoryItem(str, uri2, charSequence2);
            } else {
                remoteInputHistoryItem = new RemoteInputHistoryItem(charSequence2);
            }
            Parcelable[] parcelableArray = sbn.getNotification().extras.getParcelableArray("android.remoteInputHistoryItems");
            recoverBuilder.setRemoteInputHistory(parcelableArray != null ? (RemoteInputHistoryItem[]) Stream.concat(Stream.m1780of(remoteInputHistoryItem), Arrays.stream((T[]) parcelableArray).map(new RemoteInputNotificationRebuilder$$ExternalSyntheticLambda0())).toArray(new RemoteInputNotificationRebuilder$$ExternalSyntheticLambda1()) : new RemoteInputHistoryItem[]{remoteInputHistoryItem});
        }
        recoverBuilder.setShowRemoteInputSpinner(z);
        recoverBuilder.setHideSmartReplies(true);
        Notification build = recoverBuilder.build();
        build.contentView = sbn.getNotification().contentView;
        build.bigContentView = sbn.getNotification().bigContentView;
        build.headsUpContentView = sbn.getNotification().headsUpContentView;
        return new StatusBarNotification(sbn.getPackageName(), sbn.getOpPkg(), sbn.getId(), sbn.getTag(), sbn.getUid(), sbn.getInitialPid(), build, sbn.getUser(), sbn.getOverrideGroupKey(), sbn.getPostTime());
    }

    static /* synthetic */ RemoteInputHistoryItem lambda$rebuildWithRemoteInputInserted$0(Parcelable parcelable) {
        return (RemoteInputHistoryItem) parcelable;
    }

    static /* synthetic */ RemoteInputHistoryItem[] lambda$rebuildWithRemoteInputInserted$1(int i) {
        return new RemoteInputHistoryItem[i];
    }
}
