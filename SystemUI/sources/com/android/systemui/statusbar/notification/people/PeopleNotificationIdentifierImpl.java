package com.android.systemui.statusbar.notification.people;

import android.app.NotificationChannel;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0018\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\u00020\b*\u00020\t8BX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifierImpl;", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "personExtractor", "Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;", "groupManager", "Lcom/android/systemui/statusbar/notification/collection/render/GroupMembershipManager;", "(Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;Lcom/android/systemui/statusbar/notification/collection/render/GroupMembershipManager;)V", "personTypeInfo", "", "Landroid/service/notification/NotificationListenerService$Ranking;", "getPersonTypeInfo", "(Landroid/service/notification/NotificationListenerService$Ranking;)I", "compareTo", "a", "b", "extractPersonTypeInfo", "sbn", "Landroid/service/notification/StatusBarNotification;", "getPeopleNotificationType", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getPeopleTypeOfSummary", "upperBound", "type", "other", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleNotificationIdentifier.kt */
public final class PeopleNotificationIdentifierImpl implements PeopleNotificationIdentifier {
    private final GroupMembershipManager groupManager;
    private final NotificationPersonExtractor personExtractor;

    @Inject
    public PeopleNotificationIdentifierImpl(NotificationPersonExtractor notificationPersonExtractor, GroupMembershipManager groupMembershipManager) {
        Intrinsics.checkNotNullParameter(notificationPersonExtractor, "personExtractor");
        Intrinsics.checkNotNullParameter(groupMembershipManager, "groupManager");
        this.personExtractor = notificationPersonExtractor;
        this.groupManager = groupMembershipManager;
    }

    public int getPeopleNotificationType(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotificationListenerService.Ranking ranking = notificationEntry.getRanking();
        Intrinsics.checkNotNullExpressionValue(ranking, "entry.ranking");
        int personTypeInfo = getPersonTypeInfo(ranking);
        if (personTypeInfo == 3) {
            return 3;
        }
        StatusBarNotification sbn = notificationEntry.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn, "entry.sbn");
        int upperBound = upperBound(personTypeInfo, extractPersonTypeInfo(sbn));
        if (upperBound == 3) {
            return 3;
        }
        return upperBound(upperBound, getPeopleTypeOfSummary(notificationEntry));
    }

    public int compareTo(int i, int i2) {
        return Intrinsics.compare(i2, i);
    }

    private final int upperBound(int i, int i2) {
        return Math.max(i, i2);
    }

    private final int getPersonTypeInfo(NotificationListenerService.Ranking ranking) {
        boolean z = false;
        if (!ranking.isConversation()) {
            return 0;
        }
        if (ranking.getConversationShortcutInfo() == null) {
            return 1;
        }
        NotificationChannel channel = ranking.getChannel();
        if (channel != null && channel.isImportantConversation()) {
            z = true;
        }
        return z ? 3 : 2;
    }

    private final int extractPersonTypeInfo(StatusBarNotification statusBarNotification) {
        return this.personExtractor.isPersonNotification(statusBarNotification) ? 1 : 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002d A[LOOP:0: B:10:0x002d->B:13:0x0042, LOOP_START, PHI: r1 
      PHI: (r1v2 int) = (r1v0 int), (r1v3 int) binds: [B:9:0x0029, B:13:0x0042] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int getPeopleTypeOfSummary(com.android.systemui.statusbar.notification.collection.NotificationEntry r3) {
        /*
            r2 = this;
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r0 = r2.groupManager
            boolean r0 = r0.isGroupSummary(r3)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r0 = r2.groupManager
            com.android.systemui.statusbar.notification.collection.ListEntry r3 = (com.android.systemui.statusbar.notification.collection.ListEntry) r3
            java.util.List r3 = r0.getChildren(r3)
            if (r3 == 0) goto L_0x0044
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            kotlin.sequences.Sequence r3 = kotlin.collections.CollectionsKt.asSequence(r3)
            if (r3 == 0) goto L_0x0044
            com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1 r0 = new com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1
            r0.<init>(r2)
            kotlin.jvm.functions.Function1 r0 = (kotlin.jvm.functions.Function1) r0
            kotlin.sequences.Sequence r3 = kotlin.sequences.SequencesKt.map(r3, r0)
            if (r3 == 0) goto L_0x0044
            java.util.Iterator r3 = r3.iterator()
        L_0x002d:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x0044
            java.lang.Object r0 = r3.next()
            java.lang.Number r0 = (java.lang.Number) r0
            int r0 = r0.intValue()
            int r1 = r2.upperBound(r1, r0)
            r0 = 3
            if (r1 != r0) goto L_0x002d
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl.getPeopleTypeOfSummary(com.android.systemui.statusbar.notification.collection.NotificationEntry):int");
    }
}
