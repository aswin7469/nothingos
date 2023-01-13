package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "hunMutator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$onBeforeFinalizeFilter$1 extends Lambda implements Function1<HunMutator, Unit> {
    final /* synthetic */ List<ListEntry> $list;
    final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$onBeforeFinalizeFilter$1(HeadsUpCoordinator headsUpCoordinator, List<? extends ListEntry> list) {
        super(1);
        this.this$0 = headsUpCoordinator;
        this.$list = list;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((HunMutator) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(HunMutator hunMutator) {
        Object obj;
        boolean z;
        HunMutator hunMutator2 = hunMutator;
        Intrinsics.checkNotNullParameter(hunMutator2, "hunMutator");
        if (!this.this$0.mPostedEntries.isEmpty()) {
            Collection values = this.this$0.mPostedEntries.values();
            Intrinsics.checkNotNullExpressionValue(values, "mPostedEntries.values");
            Map linkedHashMap = new LinkedHashMap();
            for (Object next : values) {
                String groupKey = ((HeadsUpCoordinator.PostedEntry) next).getEntry().getSbn().getGroupKey();
                Object obj2 = linkedHashMap.get(groupKey);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap.put(groupKey, obj2);
                }
                ((List) obj2).add(next);
            }
            NotifPipeline access$getMNotifPipeline$p = this.this$0.mNotifPipeline;
            if (access$getMNotifPipeline$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mNotifPipeline");
                access$getMNotifPipeline$p = null;
            }
            Sequence filter = SequencesKt.filter(CollectionsKt.asSequence(access$getMNotifPipeline$p.getAllNotifs()), new C2687xa0ab4184(linkedHashMap));
            Map linkedHashMap2 = new LinkedHashMap();
            for (Object next2 : filter) {
                String groupKey2 = ((NotificationEntry) next2).getSbn().getGroupKey();
                Object obj3 = linkedHashMap2.get(groupKey2);
                if (obj3 == null) {
                    obj3 = new ArrayList();
                    linkedHashMap2.put(groupKey2, obj3);
                }
                ((List) obj3).add(next2);
            }
            Lazy lazy = LazyKt.lazy(new C2686x1fe0a1b6(this.this$0, this.$list));
            this.this$0.mLogger.logEvaluatingGroups(linkedHashMap.size());
            HeadsUpCoordinator headsUpCoordinator = this.this$0;
            for (Map.Entry entry : linkedHashMap.entrySet()) {
                String str = (String) entry.getKey();
                List<HeadsUpCoordinator.PostedEntry> list = (List) entry.getValue();
                List list2 = (List) linkedHashMap2.get(str);
                if (list2 == null) {
                    list2 = CollectionsKt.emptyList();
                }
                Iterator it = list2.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        obj = null;
                        break;
                    }
                    obj = it.next();
                    if (((NotificationEntry) obj).getSbn().getNotification().isGroupSummary()) {
                        break;
                    }
                }
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                HeadsUpCoordinatorLogger access$getMLogger$p = headsUpCoordinator.mLogger;
                Intrinsics.checkNotNullExpressionValue(str, "groupKey");
                access$getMLogger$p.logEvaluatingGroup(str, list.size(), list2.size());
                if (notificationEntry == null) {
                    for (HeadsUpCoordinator.PostedEntry postedEntry : list) {
                        Intrinsics.checkNotNullExpressionValue(postedEntry, "it");
                        headsUpCoordinator.handlePostedEntry(postedEntry, hunMutator2, "logical-summary-missing");
                    }
                } else {
                    ListEntry listEntry = notificationEntry;
                    if (!headsUpCoordinator.isGoingToShowHunStrict(listEntry)) {
                        for (HeadsUpCoordinator.PostedEntry postedEntry2 : list) {
                            Intrinsics.checkNotNullExpressionValue(postedEntry2, "it");
                            headsUpCoordinator.handlePostedEntry(postedEntry2, hunMutator2, "logical-summary-not-alerting");
                        }
                    } else {
                        NotificationEntry access$findAlertOverride = headsUpCoordinator.findAlertOverride(list, new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$3(m3111invoke$lambda2(lazy)));
                        String str2 = access$findAlertOverride != null ? "alertOverride" : "undefined";
                        Map<String, GroupLocation> r14 = m3111invoke$lambda2(lazy);
                        String key = notificationEntry.getKey();
                        Intrinsics.checkNotNullExpressionValue(key, "logicalSummary.key");
                        boolean containsKey = r14.containsKey(key);
                        if (!containsKey && access$findAlertOverride == null && (access$findAlertOverride = headsUpCoordinator.findBestTransferChild(list2, new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4(m3111invoke$lambda2(lazy)))) != null) {
                            str2 = "bestChild";
                        }
                        NotificationEntry notificationEntry2 = access$findAlertOverride;
                        String str3 = str2;
                        if (notificationEntry2 == null) {
                            for (HeadsUpCoordinator.PostedEntry postedEntry3 : list) {
                                Intrinsics.checkNotNullExpressionValue(postedEntry3, "it");
                                headsUpCoordinator.handlePostedEntry(postedEntry3, hunMutator2, "no-transfer-target");
                            }
                        } else {
                            HeadsUpCoordinator.PostedEntry postedEntry4 = (HeadsUpCoordinator.PostedEntry) headsUpCoordinator.mPostedEntries.get(notificationEntry.getKey());
                            if (!containsKey) {
                                if (postedEntry4 != null) {
                                    postedEntry4.setShouldHeadsUpEver(false);
                                    if (postedEntry4 != null) {
                                        z = false;
                                        headsUpCoordinator.handlePostedEntry(postedEntry4, hunMutator2, "detached-summary-remove-alert");
                                    }
                                }
                                boolean isAlerting = headsUpCoordinator.mHeadsUpManager.isAlerting(notificationEntry.getKey());
                                boolean access$isEntryBinding = headsUpCoordinator.isEntryBinding(listEntry);
                                z = false;
                                postedEntry4 = new HeadsUpCoordinator.PostedEntry(notificationEntry, false, false, false, false, isAlerting, access$isEntryBinding);
                                headsUpCoordinator.handlePostedEntry(postedEntry4, hunMutator2, "detached-summary-remove-alert");
                            } else {
                                z = false;
                                if (postedEntry4 != null) {
                                    headsUpCoordinator.mLogger.logPostedEntryWillNotEvaluate(postedEntry4, "attached-summary-transferred");
                                }
                            }
                            boolean z2 = z;
                            for (HeadsUpCoordinator.PostedEntry postedEntry5 : SequencesKt.filter(CollectionsKt.asSequence(list), new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6(notificationEntry))) {
                                if (Intrinsics.areEqual((Object) notificationEntry2.getKey(), (Object) postedEntry5.getKey())) {
                                    postedEntry5.setShouldHeadsUpEver(true);
                                    postedEntry5.setShouldHeadsUpAgain(true);
                                    Intrinsics.checkNotNullExpressionValue(postedEntry5, "postedEntry");
                                    headsUpCoordinator.handlePostedEntry(postedEntry5, hunMutator2, "child-alert-transfer-target-" + str3);
                                    z2 = true;
                                } else {
                                    Intrinsics.checkNotNullExpressionValue(postedEntry5, "postedEntry");
                                    headsUpCoordinator.handlePostedEntry(postedEntry5, hunMutator2, "child-alert-non-target");
                                }
                            }
                            if (!z2) {
                                headsUpCoordinator.handlePostedEntry(new HeadsUpCoordinator.PostedEntry(notificationEntry2, false, false, true, true, headsUpCoordinator.mHeadsUpManager.isAlerting(notificationEntry2.getKey()), headsUpCoordinator.isEntryBinding(notificationEntry2)), hunMutator2, "non-posted-child-alert-transfer-target-" + str3);
                            }
                        }
                    }
                }
            }
            this.this$0.mPostedEntries.clear();
        }
    }

    /* renamed from: invoke$lambda-2  reason: not valid java name */
    private static final Map<String, GroupLocation> m3111invoke$lambda2(Lazy<? extends Map<String, ? extends GroupLocation>> lazy) {
        return (Map) lazy.getValue();
    }
}
