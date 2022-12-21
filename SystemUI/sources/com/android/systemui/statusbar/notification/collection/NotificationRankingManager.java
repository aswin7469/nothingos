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
import javax.inject.Inject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\u0010\u001c\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001BU\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u0010\u0010+\u001a\u00020(2\u0006\u0010,\u001a\u00020\u001eH\u0002J$\u0010-\u001a\b\u0012\u0004\u0012\u00020\u001e0.2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001e002\u0006\u00101\u001a\u000202H\u0002J\u0010\u00103\u001a\u0002042\u0006\u0010,\u001a\u00020\u001eH\u0002J\u0010\u00105\u001a\u00020(2\u0006\u0010,\u001a\u00020\u001eH\u0016J.\u00106\u001a\b\u0012\u0004\u0012\u00020\u001e0.2\b\u00107\u001a\u0004\u0018\u00010!2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001e002\u0006\u00101\u001a\u000202H\u0016J\u0016\u00108\u001a\u0002092\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001e0:H\u0002J\f\u0010;\u001a\u000204*\u00020\u001eH\u0002J\f\u0010<\u001a\u00020(*\u00020\u001eH\u0002J\f\u0010=\u001a\u00020(*\u00020\u001eH\u0002J\f\u0010>\u001a\u00020(*\u00020\u001eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R#\u0010\u0016\u001a\n \u0017*\u0004\u0018\u00010\u00040\u00048BX\u0002¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u00020\u001e0\u001dj\b\u0012\u0004\u0012\u00020\u001e`\u001fX\u0004¢\u0006\u0002\n\u0000R(\u0010\"\u001a\u0004\u0018\u00010!2\b\u0010 \u001a\u0004\u0018\u00010!@TX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010'\u001a\u00020(8BX\u0004¢\u0006\u0006\u001a\u0004\b)\u0010*¨\u0006?"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotificationRankingManager;", "Lcom/android/systemui/statusbar/notification/collection/legacy/LegacyNotificationRanker;", "mediaManagerLazy", "Ldagger/Lazy;", "Lcom/android/systemui/statusbar/NotificationMediaManager;", "groupManager", "Lcom/android/systemui/statusbar/notification/collection/legacy/NotificationGroupManagerLegacy;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "notifFilter", "Lcom/android/systemui/statusbar/notification/NotificationFilter;", "logger", "Lcom/android/systemui/statusbar/notification/NotificationEntryManagerLogger;", "sectionsFeatureManager", "Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;", "peopleNotificationIdentifier", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "highPriorityProvider", "Lcom/android/systemui/statusbar/notification/collection/provider/HighPriorityProvider;", "keyguardEnvironment", "Lcom/android/systemui/statusbar/notification/NotificationEntryManager$KeyguardEnvironment;", "(Ldagger/Lazy;Lcom/android/systemui/statusbar/notification/collection/legacy/NotificationGroupManagerLegacy;Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/statusbar/notification/NotificationFilter;Lcom/android/systemui/statusbar/notification/NotificationEntryManagerLogger;Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;Lcom/android/systemui/statusbar/notification/collection/provider/HighPriorityProvider;Lcom/android/systemui/statusbar/notification/NotificationEntryManager$KeyguardEnvironment;)V", "mediaManager", "kotlin.jvm.PlatformType", "getMediaManager", "()Lcom/android/systemui/statusbar/NotificationMediaManager;", "mediaManager$delegate", "Lkotlin/Lazy;", "rankingComparator", "Ljava/util/Comparator;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "Lkotlin/Comparator;", "<set-?>", "Landroid/service/notification/NotificationListenerService$RankingMap;", "rankingMap", "getRankingMap", "()Landroid/service/notification/NotificationListenerService$RankingMap;", "setRankingMap", "(Landroid/service/notification/NotificationListenerService$RankingMap;)V", "usePeopleFiltering", "", "getUsePeopleFiltering", "()Z", "filter", "entry", "filterAndSortLocked", "", "entries", "", "reason", "", "getBucketForEntry", "", "isNotificationForCurrentProfiles", "updateRanking", "newRankingMap", "updateRankingForEntries", "", "", "getPeopleNotificationType", "isConversation", "isHighPriority", "isImportantMedia", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationRankingManager.kt */
public class NotificationRankingManager implements LegacyNotificationRanker {
    private final NotificationGroupManagerLegacy groupManager;
    private final HeadsUpManager headsUpManager;
    private final HighPriorityProvider highPriorityProvider;
    private final NotificationEntryManager.KeyguardEnvironment keyguardEnvironment;
    private final NotificationEntryManagerLogger logger;
    private final Lazy mediaManager$delegate = LazyKt.lazy(new NotificationRankingManager$mediaManager$2(this));
    /* access modifiers changed from: private */
    public final dagger.Lazy<NotificationMediaManager> mediaManagerLazy;
    private final NotificationFilter notifFilter;
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    private final Comparator<NotificationEntry> rankingComparator = new NotificationRankingManager$$ExternalSyntheticLambda0(this);
    private NotificationListenerService.RankingMap rankingMap;
    private final NotificationSectionsFeatureManager sectionsFeatureManager;

    @Inject
    public NotificationRankingManager(dagger.Lazy<NotificationMediaManager> lazy, NotificationGroupManagerLegacy notificationGroupManagerLegacy, HeadsUpManager headsUpManager2, NotificationFilter notificationFilter, NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationSectionsFeatureManager notificationSectionsFeatureManager, PeopleNotificationIdentifier peopleNotificationIdentifier2, HighPriorityProvider highPriorityProvider2, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment2) {
        Intrinsics.checkNotNullParameter(lazy, "mediaManagerLazy");
        Intrinsics.checkNotNullParameter(notificationGroupManagerLegacy, "groupManager");
        Intrinsics.checkNotNullParameter(headsUpManager2, "headsUpManager");
        Intrinsics.checkNotNullParameter(notificationFilter, "notifFilter");
        Intrinsics.checkNotNullParameter(notificationEntryManagerLogger, "logger");
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier2, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(highPriorityProvider2, "highPriorityProvider");
        Intrinsics.checkNotNullParameter(keyguardEnvironment2, "keyguardEnvironment");
        this.mediaManagerLazy = lazy;
        this.groupManager = notificationGroupManagerLegacy;
        this.headsUpManager = headsUpManager2;
        this.notifFilter = notificationFilter;
        this.logger = notificationEntryManagerLogger;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier2;
        this.highPriorityProvider = highPriorityProvider2;
        this.keyguardEnvironment = keyguardEnvironment2;
    }

    public NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }

    /* access modifiers changed from: protected */
    public void setRankingMap(NotificationListenerService.RankingMap rankingMap2) {
        this.rankingMap = rankingMap2;
    }

    private final NotificationMediaManager getMediaManager() {
        return (NotificationMediaManager) this.mediaManager$delegate.getValue();
    }

    private final boolean getUsePeopleFiltering() {
        return this.sectionsFeatureManager.isFilteringEnabled();
    }

    /* access modifiers changed from: private */
    /* renamed from: rankingComparator$lambda-0  reason: not valid java name */
    public static final int m3097rankingComparator$lambda0(NotificationRankingManager notificationRankingManager, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        NotificationRankingManager notificationRankingManager2 = notificationRankingManager;
        NotificationEntry notificationEntry3 = notificationEntry;
        NotificationEntry notificationEntry4 = notificationEntry2;
        Intrinsics.checkNotNullParameter(notificationRankingManager2, "this$0");
        StatusBarNotification sbn = notificationEntry.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn, "a.sbn");
        StatusBarNotification sbn2 = notificationEntry2.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn2, "b.sbn");
        int rank = notificationEntry.getRanking().getRank();
        int rank2 = notificationEntry2.getRanking().getRank();
        Intrinsics.checkNotNullExpressionValue(notificationEntry3, "a");
        boolean access$isColorizedForegroundService = NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry);
        Intrinsics.checkNotNullExpressionValue(notificationEntry4, "b");
        boolean access$isColorizedForegroundService2 = NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry2);
        boolean access$isImportantCall = NotificationRankingManagerKt.isImportantCall(notificationEntry);
        boolean access$isImportantCall2 = NotificationRankingManagerKt.isImportantCall(notificationEntry2);
        int peopleNotificationType = notificationRankingManager.getPeopleNotificationType(notificationEntry);
        int peopleNotificationType2 = notificationRankingManager2.getPeopleNotificationType(notificationEntry4);
        boolean isImportantMedia = notificationRankingManager.isImportantMedia(notificationEntry);
        boolean isImportantMedia2 = notificationRankingManager2.isImportantMedia(notificationEntry4);
        boolean access$isSystemMax = NotificationRankingManagerKt.isSystemMax(notificationEntry);
        StatusBarNotification statusBarNotification = sbn;
        boolean access$isSystemMax2 = NotificationRankingManagerKt.isSystemMax(notificationEntry2);
        StatusBarNotification statusBarNotification2 = sbn2;
        boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
        int i = rank;
        boolean isRowHeadsUp2 = notificationEntry2.isRowHeadsUp();
        int i2 = rank2;
        boolean isHighPriority = notificationRankingManager.isHighPriority(notificationEntry);
        boolean isHighPriority2 = notificationRankingManager2.isHighPriority(notificationEntry4);
        if (isRowHeadsUp != isRowHeadsUp2) {
            if (!isRowHeadsUp) {
                return 1;
            }
        } else if (isRowHeadsUp) {
            return notificationRankingManager2.headsUpManager.compare(notificationEntry3, notificationEntry4);
        } else {
            if (access$isColorizedForegroundService != access$isColorizedForegroundService2) {
                if (!access$isColorizedForegroundService) {
                    return 1;
                }
            } else if (access$isImportantCall != access$isImportantCall2) {
                if (!access$isImportantCall) {
                    return 1;
                }
            } else if (notificationRankingManager.getUsePeopleFiltering() && peopleNotificationType != peopleNotificationType2) {
                return notificationRankingManager2.peopleNotificationIdentifier.compareTo(peopleNotificationType, peopleNotificationType2);
            } else {
                if (isImportantMedia != isImportantMedia2) {
                    if (!isImportantMedia) {
                        return 1;
                    }
                } else if (access$isSystemMax == access$isSystemMax2) {
                    boolean z = isHighPriority;
                    if (z != isHighPriority2) {
                        return Intrinsics.compare(z ? 1 : 0, isHighPriority2 ? 1 : 0) * -1;
                    }
                    int i3 = i;
                    int i4 = i2;
                    if (i3 != i4) {
                        return i3 - i4;
                    }
                    return Intrinsics.compare(statusBarNotification2.getNotification().when, statusBarNotification.getNotification().when);
                } else if (!access$isSystemMax) {
                    return 1;
                }
            }
        }
        return -1;
    }

    public List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap2, Collection<NotificationEntry> collection, String str) {
        List<NotificationEntry> filterAndSortLocked;
        Intrinsics.checkNotNullParameter(collection, "entries");
        Intrinsics.checkNotNullParameter(str, "reason");
        if (rankingMap2 != null) {
            setRankingMap(rankingMap2);
            updateRankingForEntries(collection);
        }
        synchronized (this) {
            filterAndSortLocked = filterAndSortLocked(collection, str);
        }
        return filterAndSortLocked;
    }

    public boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return this.keyguardEnvironment.isNotificationForCurrentProfiles(notificationEntry.getSbn());
    }

    private final List<NotificationEntry> filterAndSortLocked(Collection<NotificationEntry> collection, String str) {
        this.logger.logFilterAndSort(str);
        Iterable<NotificationEntry> iterable = collection;
        List<NotificationEntry> list = SequencesKt.toList(SequencesKt.sortedWith(SequencesKt.filterNot(CollectionsKt.asSequence(iterable), new NotificationRankingManager$filterAndSortLocked$filtered$1(this)), this.rankingComparator));
        for (NotificationEntry notificationEntry : iterable) {
            notificationEntry.setBucket(getBucketForEntry(notificationEntry));
        }
        return list;
    }

    /* access modifiers changed from: private */
    public final boolean filter(NotificationEntry notificationEntry) {
        boolean shouldFilterOut = this.notifFilter.shouldFilterOut(notificationEntry);
        if (shouldFilterOut) {
            notificationEntry.resetInitializationTime();
        }
        return shouldFilterOut;
    }

    private final int getBucketForEntry(NotificationEntry notificationEntry) {
        boolean access$isImportantCall = NotificationRankingManagerKt.isImportantCall(notificationEntry);
        boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
        boolean isImportantMedia = isImportantMedia(notificationEntry);
        boolean access$isSystemMax = NotificationRankingManagerKt.isSystemMax(notificationEntry);
        if (NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry) || access$isImportantCall) {
            return 3;
        }
        if (!getUsePeopleFiltering() || !isConversation(notificationEntry)) {
            return (isRowHeadsUp || isImportantMedia || access$isSystemMax || isHighPriority(notificationEntry)) ? 5 : 6;
        }
        return 4;
    }

    private final void updateRankingForEntries(Iterable<NotificationEntry> iterable) {
        NotificationListenerService.RankingMap rankingMap2 = getRankingMap();
        if (rankingMap2 != null) {
            synchronized (iterable) {
                for (NotificationEntry next : iterable) {
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    if (rankingMap2.getRanking(next.getKey(), ranking)) {
                        next.setRanking(ranking);
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(next.getSbn().getOverrideGroupKey(), overrideGroupKey)) {
                            String groupKey = next.getSbn().getGroupKey();
                            boolean isGroup = next.getSbn().isGroup();
                            boolean isGroupSummary = next.getSbn().getNotification().isGroupSummary();
                            next.getSbn().setOverrideGroupKey(overrideGroupKey);
                            this.groupManager.onEntryUpdated(next, groupKey, isGroup, isGroupSummary);
                        }
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    private final boolean isImportantMedia(NotificationEntry notificationEntry) {
        return Intrinsics.areEqual((Object) notificationEntry.getKey(), (Object) getMediaManager().getMediaNotificationKey()) && notificationEntry.getImportance() > 1;
    }

    private final boolean isConversation(NotificationEntry notificationEntry) {
        return getPeopleNotificationType(notificationEntry) != 0;
    }

    private final int getPeopleNotificationType(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry);
    }

    private final boolean isHighPriority(NotificationEntry notificationEntry) {
        return this.highPriorityProvider.isHighPriority(notificationEntry);
    }
}
