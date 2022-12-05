package com.android.systemui.statusbar.notification.people;

import android.app.NotificationChannel;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: PeopleNotificationIdentifier.kt */
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl implements PeopleNotificationIdentifier {
    @NotNull
    private final GroupMembershipManager groupManager;
    @NotNull
    private final NotificationPersonExtractor personExtractor;

    public PeopleNotificationIdentifierImpl(@NotNull NotificationPersonExtractor personExtractor, @NotNull GroupMembershipManager groupManager) {
        Intrinsics.checkNotNullParameter(personExtractor, "personExtractor");
        Intrinsics.checkNotNullParameter(groupManager, "groupManager");
        this.personExtractor = personExtractor;
        this.groupManager = groupManager;
    }

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public int getPeopleNotificationType(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        NotificationListenerService.Ranking ranking = entry.getRanking();
        Intrinsics.checkNotNullExpressionValue(ranking, "entry.ranking");
        int personTypeInfo = getPersonTypeInfo(ranking);
        if (personTypeInfo == 3) {
            return 3;
        }
        StatusBarNotification sbn = entry.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn, "entry.sbn");
        int upperBound = upperBound(personTypeInfo, extractPersonTypeInfo(sbn));
        if (upperBound != 3) {
            return upperBound(upperBound, getPeopleTypeOfSummary(entry));
        }
        return 3;
    }

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public int compareTo(int i, int i2) {
        return Intrinsics.compare(i2, i);
    }

    private final int upperBound(int i, int i2) {
        return Math.max(i, i2);
    }

    private final int getPersonTypeInfo(NotificationListenerService.Ranking ranking) {
        if (!ranking.isConversation()) {
            return 0;
        }
        if (ranking.getConversationShortcutInfo() == null) {
            return 1;
        }
        NotificationChannel channel = ranking.getChannel();
        return Intrinsics.areEqual(channel == null ? null : Boolean.valueOf(channel.isImportantConversation()), Boolean.TRUE) ? 3 : 2;
    }

    private final int extractPersonTypeInfo(StatusBarNotification statusBarNotification) {
        return this.personExtractor.isPersonNotification(statusBarNotification) ? 1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0014, code lost:
        r3 = kotlin.collections.CollectionsKt___CollectionsKt.asSequence(r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int getPeopleTypeOfSummary(NotificationEntry notificationEntry) {
        Sequence asSequence;
        int i = 0;
        if (!this.groupManager.isGroupSummary(notificationEntry)) {
            return 0;
        }
        List<NotificationEntry> children = this.groupManager.getChildren(notificationEntry);
        Sequence sequence = null;
        if (children != null && asSequence != null) {
            sequence = SequencesKt.map(asSequence, new PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1(this));
        }
        if (sequence == null) {
            return 0;
        }
        Iterator it = sequence.iterator();
        while (it.hasNext() && (i = upperBound(i, ((Number) it.next()).intValue())) != 3) {
        }
        return i;
    }
}
