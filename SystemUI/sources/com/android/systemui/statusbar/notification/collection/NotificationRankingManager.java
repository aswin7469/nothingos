package com.android.systemui.statusbar.notification.collection;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public class NotificationRankingManager implements LegacyNotificationRanker {
    @NotNull
    private final NotificationGroupManagerLegacy groupManager;
    @NotNull
    private final HeadsUpManager headsUpManager;
    @NotNull
    private final HighPriorityProvider highPriorityProvider;
    @NotNull
    private final NotificationEntryManager.KeyguardEnvironment keyguardEnvironment;
    @NotNull
    private final NotificationEntryManagerLogger logger;
    @NotNull
    private final Lazy mediaManager$delegate;
    @NotNull
    private final dagger.Lazy<NotificationMediaManager> mediaManagerLazy;
    @NotNull
    private final NotificationFilter notifFilter;
    @NotNull
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    @NotNull
    private final Comparator<NotificationEntry> rankingComparator = new Comparator<NotificationEntry>() { // from class: com.android.systemui.statusbar.notification.collection.NotificationRankingManager$rankingComparator$1
        @Override // java.util.Comparator
        public final int compare(NotificationEntry a, NotificationEntry b) {
            boolean isColorizedForegroundService;
            boolean isColorizedForegroundService2;
            boolean isImportantCall;
            boolean isImportantCall2;
            int peopleNotificationType;
            int peopleNotificationType2;
            boolean isImportantMedia;
            boolean isImportantMedia2;
            boolean isSystemMax;
            boolean isSystemMax2;
            boolean isHighPriority;
            boolean isHighPriority2;
            boolean usePeopleFiltering;
            PeopleNotificationIdentifier peopleNotificationIdentifier;
            HeadsUpManager headsUpManager;
            StatusBarNotification sbn = a.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn, "a.sbn");
            StatusBarNotification sbn2 = b.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn2, "b.sbn");
            int rank = a.getRanking().getRank();
            int rank2 = b.getRanking().getRank();
            Intrinsics.checkNotNullExpressionValue(a, "a");
            isColorizedForegroundService = NotificationRankingManagerKt.isColorizedForegroundService(a);
            Intrinsics.checkNotNullExpressionValue(b, "b");
            isColorizedForegroundService2 = NotificationRankingManagerKt.isColorizedForegroundService(b);
            isImportantCall = NotificationRankingManagerKt.isImportantCall(a);
            isImportantCall2 = NotificationRankingManagerKt.isImportantCall(b);
            peopleNotificationType = NotificationRankingManager.this.getPeopleNotificationType(a);
            peopleNotificationType2 = NotificationRankingManager.this.getPeopleNotificationType(b);
            isImportantMedia = NotificationRankingManager.this.isImportantMedia(a);
            isImportantMedia2 = NotificationRankingManager.this.isImportantMedia(b);
            isSystemMax = NotificationRankingManagerKt.isSystemMax(a);
            isSystemMax2 = NotificationRankingManagerKt.isSystemMax(b);
            boolean isRowHeadsUp = a.isRowHeadsUp();
            boolean isRowHeadsUp2 = b.isRowHeadsUp();
            isHighPriority = NotificationRankingManager.this.isHighPriority(a);
            isHighPriority2 = NotificationRankingManager.this.isHighPriority(b);
            if (isRowHeadsUp != isRowHeadsUp2) {
                if (!isRowHeadsUp) {
                    return 1;
                }
            } else if (isRowHeadsUp) {
                headsUpManager = NotificationRankingManager.this.headsUpManager;
                return headsUpManager.compare(a, b);
            } else if (isColorizedForegroundService != isColorizedForegroundService2) {
                if (!isColorizedForegroundService) {
                    return 1;
                }
            } else if (isImportantCall == isImportantCall2) {
                usePeopleFiltering = NotificationRankingManager.this.getUsePeopleFiltering();
                if (usePeopleFiltering && peopleNotificationType != peopleNotificationType2) {
                    peopleNotificationIdentifier = NotificationRankingManager.this.peopleNotificationIdentifier;
                    return peopleNotificationIdentifier.compareTo(peopleNotificationType, peopleNotificationType2);
                } else if (isImportantMedia != isImportantMedia2) {
                    if (!isImportantMedia) {
                        return 1;
                    }
                } else if (isSystemMax == isSystemMax2) {
                    if (isHighPriority != isHighPriority2) {
                        return Intrinsics.compare(isHighPriority ? 1 : 0, isHighPriority2 ? 1 : 0) * (-1);
                    }
                    return rank != rank2 ? rank - rank2 : Intrinsics.compare(sbn2.getNotification().when, sbn.getNotification().when);
                } else if (!isSystemMax) {
                    return 1;
                }
            } else if (!isImportantCall) {
                return 1;
            }
            return -1;
        }
    };
    @Nullable
    private NotificationListenerService.RankingMap rankingMap;
    @NotNull
    private final NotificationSectionsFeatureManager sectionsFeatureManager;

    public NotificationRankingManager(@NotNull dagger.Lazy<NotificationMediaManager> mediaManagerLazy, @NotNull NotificationGroupManagerLegacy groupManager, @NotNull HeadsUpManager headsUpManager, @NotNull NotificationFilter notifFilter, @NotNull NotificationEntryManagerLogger logger, @NotNull NotificationSectionsFeatureManager sectionsFeatureManager, @NotNull PeopleNotificationIdentifier peopleNotificationIdentifier, @NotNull HighPriorityProvider highPriorityProvider, @NotNull NotificationEntryManager.KeyguardEnvironment keyguardEnvironment) {
        Lazy lazy;
        Intrinsics.checkNotNullParameter(mediaManagerLazy, "mediaManagerLazy");
        Intrinsics.checkNotNullParameter(groupManager, "groupManager");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(notifFilter, "notifFilter");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(sectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(highPriorityProvider, "highPriorityProvider");
        Intrinsics.checkNotNullParameter(keyguardEnvironment, "keyguardEnvironment");
        this.mediaManagerLazy = mediaManagerLazy;
        this.groupManager = groupManager;
        this.headsUpManager = headsUpManager;
        this.notifFilter = notifFilter;
        this.logger = logger;
        this.sectionsFeatureManager = sectionsFeatureManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.highPriorityProvider = highPriorityProvider;
        this.keyguardEnvironment = keyguardEnvironment;
        lazy = LazyKt__LazyJVMKt.lazy(new NotificationRankingManager$mediaManager$2(this));
        this.mediaManager$delegate = lazy;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    @Nullable
    public NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }

    protected void setRankingMap(@Nullable NotificationListenerService.RankingMap rankingMap) {
        this.rankingMap = rankingMap;
    }

    private final NotificationMediaManager getMediaManager() {
        return (NotificationMediaManager) this.mediaManager$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getUsePeopleFiltering() {
        return this.sectionsFeatureManager.isFilteringEnabled();
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    @NotNull
    public List<NotificationEntry> updateRanking(@Nullable NotificationListenerService.RankingMap rankingMap, @NotNull Collection<NotificationEntry> entries, @NotNull String reason) {
        List<NotificationEntry> filterAndSortLocked;
        Intrinsics.checkNotNullParameter(entries, "entries");
        Intrinsics.checkNotNullParameter(reason, "reason");
        if (rankingMap != null) {
            setRankingMap(rankingMap);
            updateRankingForEntries(entries);
        }
        synchronized (this) {
            filterAndSortLocked = filterAndSortLocked(entries, reason);
        }
        return filterAndSortLocked;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public boolean isNotificationForCurrentProfiles(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        return this.keyguardEnvironment.isNotificationForCurrentProfiles(entry.getSbn());
    }

    private final List<NotificationEntry> filterAndSortLocked(Collection<NotificationEntry> collection, String str) {
        Sequence asSequence;
        this.logger.logFilterAndSort(str);
        asSequence = CollectionsKt___CollectionsKt.asSequence(collection);
        List<NotificationEntry> list = SequencesKt.toList(SequencesKt.sortedWith(SequencesKt.filterNot(asSequence, new NotificationRankingManager$filterAndSortLocked$filtered$1(this)), this.rankingComparator));
        for (NotificationEntry notificationEntry : collection) {
            notificationEntry.setBucket(getBucketForEntry(notificationEntry));
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean filter(NotificationEntry notificationEntry) {
        boolean shouldFilterOut = this.notifFilter.shouldFilterOut(notificationEntry);
        if (shouldFilterOut) {
            notificationEntry.resetInitializationTime();
        }
        return shouldFilterOut;
    }

    private final int getBucketForEntry(NotificationEntry notificationEntry) {
        boolean isImportantCall;
        boolean isSystemMax;
        boolean isColorizedForegroundService;
        isImportantCall = NotificationRankingManagerKt.isImportantCall(notificationEntry);
        boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
        boolean isImportantMedia = isImportantMedia(notificationEntry);
        isSystemMax = NotificationRankingManagerKt.isSystemMax(notificationEntry);
        isColorizedForegroundService = NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry);
        if (isColorizedForegroundService || isImportantCall) {
            return 3;
        }
        if (getUsePeopleFiltering() && isConversation(notificationEntry)) {
            return 4;
        }
        return (isRowHeadsUp || isImportantMedia || isSystemMax || isHighPriority(notificationEntry)) ? 5 : 6;
    }

    private final void updateRankingForEntries(Iterable<NotificationEntry> iterable) {
        NotificationListenerService.RankingMap rankingMap = getRankingMap();
        if (rankingMap == null) {
            return;
        }
        synchronized (iterable) {
            for (NotificationEntry notificationEntry : iterable) {
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                if (rankingMap.getRanking(notificationEntry.getKey(), ranking)) {
                    notificationEntry.setRanking(ranking);
                    String overrideGroupKey = ranking.getOverrideGroupKey();
                    if (!Objects.equals(notificationEntry.getSbn().getOverrideGroupKey(), overrideGroupKey)) {
                        String groupKey = notificationEntry.getSbn().getGroupKey();
                        boolean isGroup = notificationEntry.getSbn().isGroup();
                        boolean isGroupSummary = notificationEntry.getSbn().getNotification().isGroupSummary();
                        notificationEntry.getSbn().setOverrideGroupKey(overrideGroupKey);
                        this.groupManager.onEntryUpdated(notificationEntry, groupKey, isGroup, isGroupSummary);
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isImportantMedia(NotificationEntry notificationEntry) {
        return Intrinsics.areEqual(notificationEntry.getKey(), getMediaManager().getMediaNotificationKey()) && notificationEntry.getImportance() > 1;
    }

    private final boolean isConversation(NotificationEntry notificationEntry) {
        return getPeopleNotificationType(notificationEntry) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getPeopleNotificationType(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isHighPriority(NotificationEntry notificationEntry) {
        return this.highPriorityProvider.isHighPriority(notificationEntry);
    }
}
