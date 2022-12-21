package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArrayMap;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.notification.interruption.HeadsUpControllerEx;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@CoordinatorScope
@Metadata(mo64986d1 = {"\u0000Æ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\t\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f*\u0004\u001a\u001d\")\b\u0007\u0018\u0000 U2\u00020\u0001:\u0002UVBK\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0001\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\u0010\u00103\u001a\u0002042\u0006\u00105\u001a\u00020 H\u0016J\u0010\u00106\u001a\u0002042\u0006\u00107\u001a\u00020-H\u0002J\u0010\u00108\u001a\u0002042\u0006\u00109\u001a\u00020%H\u0002J\u0010\u0010:\u001a\u0002042\u0006\u00109\u001a\u00020%H\u0002J,\u0010;\u001a\u0004\u0018\u00010%2\f\u0010<\u001a\b\u0012\u0004\u0012\u00020-0=2\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020@0?H\u0002J,\u0010A\u001a\u0004\u0018\u00010%2\f\u0010B\u001a\b\u0012\u0004\u0012\u00020%0=2\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020@0?H\u0002J\"\u0010C\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020@0D2\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0=H\u0002J \u0010G\u001a\u0002042\u0006\u00107\u001a\u00020-2\u0006\u0010H\u001a\u00020I2\u0006\u0010J\u001a\u00020\u0017H\u0002J\u0010\u0010K\u001a\u00020L2\u0006\u00109\u001a\u00020FH\u0002J\u0010\u0010M\u001a\u00020L2\u0006\u00109\u001a\u00020FH\u0002J\u0010\u0010N\u001a\u00020L2\u0006\u00109\u001a\u00020FH\u0002J\u0010\u0010O\u001a\u00020L2\u0006\u00109\u001a\u00020FH\u0002J\u0010\u0010P\u001a\u00020L2\u0006\u00109\u001a\u00020%H\u0002J\u0014\u0010Q\u001a\u0002042\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0=J\u0014\u0010R\u001a\u0002042\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0=J\u0010\u0010S\u001a\u0002042\u0006\u00109\u001a\u00020%H\u0002J\u0010\u0010T\u001a\u00020L2\u0006\u00109\u001a\u00020%H\u0002R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00180\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0004\n\u0002\u0010\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0004\n\u0002\u0010\u001eR\u000e\u0010\u001f\u001a\u00020 X.¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u00020\"X\u0004¢\u0006\u0004\n\u0002\u0010#R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010$\u001a\u0010\u0012\u0004\u0012\u00020%\u0012\u0006\u0012\u0004\u0018\u00010&0\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u00020)X\u0004¢\u0006\u0004\n\u0002\u0010*R*\u0010+\u001a\u001e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020-0,j\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020-`.X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010/\u001a\u000200¢\u0006\b\n\u0000\u001a\u0004\b1\u00102¨\u0006W²\u0006\u0016\u0010X\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020@0DX\u0002"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "mLogger", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinatorLogger;", "mSystemClock", "Lcom/android/systemui/util/time/SystemClock;", "mHeadsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "mHeadsUpViewBinder", "Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;", "mNotificationInterruptStateProvider", "Lcom/android/systemui/statusbar/notification/interruption/NotificationInterruptStateProvider;", "mRemoteInputManager", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager;", "mIncomingHeaderController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "mExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinatorLogger;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;Lcom/android/systemui/statusbar/notification/interruption/NotificationInterruptStateProvider;Lcom/android/systemui/statusbar/NotificationRemoteInputManager;Lcom/android/systemui/statusbar/notification/collection/render/NodeController;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "mEndLifetimeExtension", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender$OnEndLifetimeExtensionCallback;", "mEntriesBindingUntil", "Landroid/util/ArrayMap;", "", "", "mLifetimeExtender", "com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mLifetimeExtender$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mLifetimeExtender$1;", "mNotifCollectionListener", "com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mNotifCollectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mNotifCollectionListener$1;", "mNotifPipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "mNotifPromoter", "com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mNotifPromoter$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mNotifPromoter$1;", "mNotifsExtendingLifetime", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "Ljava/lang/Runnable;", "mNow", "mOnHeadsUpChangedListener", "com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mOnHeadsUpChangedListener$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mOnHeadsUpChangedListener$1;", "mPostedEntries", "Ljava/util/LinkedHashMap;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;", "Lkotlin/collections/LinkedHashMap;", "sectioner", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "getSectioner", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "attach", "", "pipeline", "bindForAsyncHeadsUp", "posted", "cancelHeadsUpBind", "entry", "endNotifLifetimeExtensionIfExtended", "findAlertOverride", "postedEntries", "", "locationLookupByKey", "Lkotlin/Function1;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", "findBestTransferChild", "logicalMembers", "getGroupLocationsByKey", "", "list", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "handlePostedEntry", "hunMutator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "scenario", "isAttemptingToShowHun", "", "isEntryBinding", "isGoingToShowHunNoRetract", "isGoingToShowHunStrict", "isSticky", "onBeforeFinalizeFilter", "onBeforeTransformGroups", "onHeadsUpViewBound", "shouldHunAgain", "Companion", "PostedEntry", "SystemUI_nothingRelease", "groupLocationsByKey"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator implements Coordinator {
    private static final long BIND_TIMEOUT = 1000;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "HeadsUpCoordinator";
    /* access modifiers changed from: private */
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback mEndLifetimeExtension;
    private final ArrayMap<String, Long> mEntriesBindingUntil = new ArrayMap<>();
    /* access modifiers changed from: private */
    public final DelayableExecutor mExecutor;
    /* access modifiers changed from: private */
    public final HeadsUpManager mHeadsUpManager;
    /* access modifiers changed from: private */
    public final HeadsUpViewBinder mHeadsUpViewBinder;
    /* access modifiers changed from: private */
    public final NodeController mIncomingHeaderController;
    private final HeadsUpCoordinator$mLifetimeExtender$1 mLifetimeExtender = new HeadsUpCoordinator$mLifetimeExtender$1(this);
    /* access modifiers changed from: private */
    public final HeadsUpCoordinatorLogger mLogger;
    private final HeadsUpCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new HeadsUpCoordinator$mNotifCollectionListener$1(this);
    /* access modifiers changed from: private */
    public NotifPipeline mNotifPipeline;
    private final HeadsUpCoordinator$mNotifPromoter$1 mNotifPromoter = new HeadsUpCoordinator$mNotifPromoter$1(this);
    /* access modifiers changed from: private */
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    /* access modifiers changed from: private */
    public final ArrayMap<NotificationEntry, Runnable> mNotifsExtendingLifetime = new ArrayMap<>();
    private long mNow = -1;
    private final HeadsUpCoordinator$mOnHeadsUpChangedListener$1 mOnHeadsUpChangedListener = new HeadsUpCoordinator$mOnHeadsUpChangedListener$1(this);
    /* access modifiers changed from: private */
    public final LinkedHashMap<String, PostedEntry> mPostedEntries = new LinkedHashMap<>();
    /* access modifiers changed from: private */
    public final NotificationRemoteInputManager mRemoteInputManager;
    private final SystemClock mSystemClock;
    private final NotifSectioner sectioner = new HeadsUpCoordinator$sectioner$1(this);

    @Inject
    public HeadsUpCoordinator(HeadsUpCoordinatorLogger headsUpCoordinatorLogger, SystemClock systemClock, HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationRemoteInputManager notificationRemoteInputManager, NodeController nodeController, @Main DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(headsUpCoordinatorLogger, "mLogger");
        Intrinsics.checkNotNullParameter(systemClock, "mSystemClock");
        Intrinsics.checkNotNullParameter(headsUpManager, "mHeadsUpManager");
        Intrinsics.checkNotNullParameter(headsUpViewBinder, "mHeadsUpViewBinder");
        Intrinsics.checkNotNullParameter(notificationInterruptStateProvider, "mNotificationInterruptStateProvider");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "mRemoteInputManager");
        Intrinsics.checkNotNullParameter(nodeController, "mIncomingHeaderController");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mExecutor");
        this.mLogger = headsUpCoordinatorLogger;
        this.mSystemClock = systemClock;
        this.mHeadsUpManager = headsUpManager;
        this.mHeadsUpViewBinder = headsUpViewBinder;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mIncomingHeaderController = nodeController;
        this.mExecutor = delayableExecutor;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        this.mNotifPipeline = notifPipeline;
        this.mHeadsUpManager.addListener(this.mOnHeadsUpChangedListener);
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeTransformGroupsListener(new HeadsUpCoordinator$$ExternalSyntheticLambda0(this));
        notifPipeline.addOnBeforeFinalizeFilterListener(new HeadsUpCoordinator$$ExternalSyntheticLambda1(this));
        notifPipeline.addPromoter(this.mNotifPromoter);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    /* access modifiers changed from: private */
    public final void onHeadsUpViewBound(NotificationEntry notificationEntry) {
        ((HeadsUpControllerEx) NTDependencyEx.get(HeadsUpControllerEx.class)).showNotification(notificationEntry);
        this.mEntriesBindingUntil.remove(notificationEntry.getKey());
    }

    public final void onBeforeTransformGroups(List<? extends ListEntry> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        this.mNow = this.mSystemClock.currentTimeMillis();
        if (!this.mPostedEntries.isEmpty()) {
            Object unused = HeadsUpCoordinatorKt.modifyHuns(this.mHeadsUpManager, new HeadsUpCoordinator$onBeforeTransformGroups$1(this));
        }
    }

    public final void onBeforeFinalizeFilter(List<? extends ListEntry> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        Object unused = HeadsUpCoordinatorKt.modifyHuns(this.mHeadsUpManager, new HeadsUpCoordinator$onBeforeFinalizeFilter$1(this, list));
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0044, code lost:
        if (r2.getSbn().getNotification().getGroupAlertBehavior() == 1) goto L_0x0048;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.notification.collection.NotificationEntry findAlertOverride(java.util.List<com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator.PostedEntry> r3, kotlin.jvm.functions.Function1<? super java.lang.String, ? extends com.android.systemui.statusbar.notification.collection.coordinator.GroupLocation> r4) {
        /*
            r2 = this;
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            kotlin.sequences.Sequence r2 = kotlin.collections.CollectionsKt.asSequence(r3)
            com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$findAlertOverride$1 r3 = com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$findAlertOverride$1.INSTANCE
            kotlin.jvm.functions.Function1 r3 = (kotlin.jvm.functions.Function1) r3
            kotlin.sequences.Sequence r2 = kotlin.sequences.SequencesKt.filter(r2, r3)
            com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$findAlertOverride$$inlined$sortedBy$1 r3 = new com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$findAlertOverride$$inlined$sortedBy$1
            r3.<init>()
            java.util.Comparator r3 = (java.util.Comparator) r3
            kotlin.sequences.Sequence r2 = kotlin.sequences.SequencesKt.sortedWith(r2, r3)
            java.lang.Object r2 = kotlin.sequences.SequencesKt.firstOrNull(r2)
            com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$PostedEntry r2 = (com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator.PostedEntry) r2
            r3 = 0
            if (r2 == 0) goto L_0x004b
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r2.getEntry()
            java.lang.String r0 = r2.getKey()
            java.lang.String r1 = "entry.key"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            java.lang.Object r4 = r4.invoke(r0)
            com.android.systemui.statusbar.notification.collection.coordinator.GroupLocation r0 = com.android.systemui.statusbar.notification.collection.coordinator.GroupLocation.Isolated
            if (r4 != r0) goto L_0x0047
            android.service.notification.StatusBarNotification r4 = r2.getSbn()
            android.app.Notification r4 = r4.getNotification()
            int r4 = r4.getGroupAlertBehavior()
            r0 = 1
            if (r4 != r0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r0 = 0
        L_0x0048:
            if (r0 == 0) goto L_0x004b
            r3 = r2
        L_0x004b:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator.findAlertOverride(java.util.List, kotlin.jvm.functions.Function1):com.android.systemui.statusbar.notification.collection.NotificationEntry");
    }

    /* access modifiers changed from: private */
    public final NotificationEntry findBestTransferChild(List<NotificationEntry> list, Function1<? super String, ? extends GroupLocation> function1) {
        return (NotificationEntry) SequencesKt.firstOrNull(SequencesKt.sortedWith(SequencesKt.filter(SequencesKt.filter(CollectionsKt.asSequence(list), HeadsUpCoordinator$findBestTransferChild$1.INSTANCE), new HeadsUpCoordinator$findBestTransferChild$2(function1)), ComparisonsKt.compareBy((Function1<? super T, ? extends Comparable<?>>[]) new Function1[]{new HeadsUpCoordinator$findBestTransferChild$3(this), HeadsUpCoordinator$findBestTransferChild$4.INSTANCE})));
    }

    /* access modifiers changed from: private */
    public final Map<String, GroupLocation> getGroupLocationsByKey(List<? extends ListEntry> list) {
        Map<String, GroupLocation> linkedHashMap = new LinkedHashMap<>();
        for (ListEntry listEntry : list) {
            if (listEntry instanceof NotificationEntry) {
                String key = ((NotificationEntry) listEntry).getKey();
                Intrinsics.checkNotNullExpressionValue(key, "topLevelEntry.key");
                linkedHashMap.put(key, GroupLocation.Isolated);
            } else if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    String key2 = summary.getKey();
                    Intrinsics.checkNotNullExpressionValue(key2, "summary.key");
                    linkedHashMap.put(key2, GroupLocation.Summary);
                }
                List<NotificationEntry> children = groupEntry.getChildren();
                Intrinsics.checkNotNullExpressionValue(children, "topLevelEntry.children");
                for (NotificationEntry key3 : children) {
                    String key4 = key3.getKey();
                    Intrinsics.checkNotNullExpressionValue(key4, "child.key");
                    linkedHashMap.put(key4, GroupLocation.Child);
                }
            } else {
                throw new IllegalStateException(("unhandled type " + listEntry).toString());
            }
        }
        return linkedHashMap;
    }

    /* access modifiers changed from: private */
    public final void handlePostedEntry(PostedEntry postedEntry, HunMutator hunMutator, String str) {
        this.mLogger.logPostedEntryWillEvaluate(postedEntry, str);
        if (postedEntry.getWasAdded()) {
            if (postedEntry.getShouldHeadsUpEver()) {
                bindForAsyncHeadsUp(postedEntry);
            }
        } else if (postedEntry.isHeadsUpAlready()) {
            if (postedEntry.getShouldHeadsUpEver()) {
                if (postedEntry.isAlerting()) {
                    hunMutator.updateNotification(postedEntry.getKey(), postedEntry.getShouldHeadsUpAgain());
                }
            } else if (postedEntry.isAlerting()) {
                hunMutator.removeNotification(postedEntry.getKey(), false);
            } else {
                cancelHeadsUpBind(postedEntry.getEntry());
            }
        } else if (postedEntry.getShouldHeadsUpEver() && postedEntry.getShouldHeadsUpAgain()) {
            bindForAsyncHeadsUp(postedEntry);
        }
    }

    /* access modifiers changed from: private */
    public final void cancelHeadsUpBind(NotificationEntry notificationEntry) {
        this.mEntriesBindingUntil.remove(notificationEntry.getKey());
        this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
    }

    private final void bindForAsyncHeadsUp(PostedEntry postedEntry) {
        this.mEntriesBindingUntil.put(postedEntry.getKey(), Long.valueOf(this.mNow + 1000));
        this.mHeadsUpViewBinder.bindHeadsUpView(postedEntry.getEntry(), new HeadsUpCoordinator$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public final boolean shouldHunAgain(NotificationEntry notificationEntry) {
        return !notificationEntry.hasInterrupted() || (notificationEntry.getSbn().getNotification().flags & 8) == 0;
    }

    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    /* access modifiers changed from: private */
    public final boolean isSticky(NotificationEntry notificationEntry) {
        return this.mHeadsUpManager.isSticky(notificationEntry.getKey());
    }

    /* access modifiers changed from: private */
    public final boolean isEntryBinding(ListEntry listEntry) {
        Long l = this.mEntriesBindingUntil.get(listEntry.getKey());
        return l != null && l.longValue() >= this.mNow;
    }

    private final boolean isAttemptingToShowHun(ListEntry listEntry) {
        return this.mHeadsUpManager.isAlerting(listEntry.getKey()) || isEntryBinding(listEntry);
    }

    /* access modifiers changed from: private */
    public final boolean isGoingToShowHunNoRetract(ListEntry listEntry) {
        PostedEntry postedEntry = this.mPostedEntries.get(listEntry.getKey());
        return postedEntry != null ? postedEntry.getCalculateShouldBeHeadsUpNoRetract() : isAttemptingToShowHun(listEntry);
    }

    /* access modifiers changed from: private */
    public final boolean isGoingToShowHunStrict(ListEntry listEntry) {
        PostedEntry postedEntry = this.mPostedEntries.get(listEntry.getKey());
        return postedEntry != null ? postedEntry.getCalculateShouldBeHeadsUpStrict() : isAttemptingToShowHun(listEntry);
    }

    /* access modifiers changed from: private */
    public final void endNotifLifetimeExtensionIfExtended(NotificationEntry notificationEntry) {
        if (this.mNotifsExtendingLifetime.containsKey(notificationEntry)) {
            Runnable remove = this.mNotifsExtendingLifetime.remove(notificationEntry);
            if (remove != null) {
                remove.run();
            }
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mEndLifetimeExtension;
            if (onEndLifetimeExtensionCallback != null) {
                onEndLifetimeExtensionCallback.onEndLifetimeExtension(this.mLifetimeExtender, notificationEntry);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$Companion;", "", "()V", "BIND_TIMEOUT", "", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: HeadsUpCoordinator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005¢\u0006\u0002\u0010\u000bJ\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0005HÆ\u0003J\t\u0010$\u001a\u00020\u0005HÆ\u0003J\t\u0010%\u001a\u00020\u0005HÆ\u0003J\t\u0010&\u001a\u00020\u0005HÆ\u0003J\t\u0010'\u001a\u00020\u0005HÆ\u0003J\t\u0010(\u001a\u00020\u0005HÆ\u0003JO\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u0005HÆ\u0001J\u0013\u0010*\u001a\u00020\u00052\b\u0010+\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010,\u001a\u00020-HÖ\u0001J\t\u0010.\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\f\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\t\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\u000e\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\n\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000e\"\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0016\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u000eR\u0011\u0010\u0017\u001a\u00020\u0018¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\b\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0014R\u001a\u0010\u0007\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u000e\"\u0004\b\u001e\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u000eR\u001a\u0010\u0006\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u000e\"\u0004\b!\u0010\u0014¨\u0006/"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "wasAdded", "", "wasUpdated", "shouldHeadsUpEver", "shouldHeadsUpAgain", "isAlerting", "isBinding", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;ZZZZZZ)V", "calculateShouldBeHeadsUpNoRetract", "getCalculateShouldBeHeadsUpNoRetract", "()Z", "calculateShouldBeHeadsUpStrict", "getCalculateShouldBeHeadsUpStrict", "getEntry", "()Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "setAlerting", "(Z)V", "setBinding", "isHeadsUpAlready", "key", "", "getKey", "()Ljava/lang/String;", "getShouldHeadsUpAgain", "setShouldHeadsUpAgain", "getShouldHeadsUpEver", "setShouldHeadsUpEver", "getWasAdded", "getWasUpdated", "setWasUpdated", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: HeadsUpCoordinator.kt */
    public static final class PostedEntry {
        private final NotificationEntry entry;
        private boolean isAlerting;
        private boolean isBinding;
        private final String key;
        private boolean shouldHeadsUpAgain;
        private boolean shouldHeadsUpEver;
        private final boolean wasAdded;
        private boolean wasUpdated;

        public static /* synthetic */ PostedEntry copy$default(PostedEntry postedEntry, NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i, Object obj) {
            if ((i & 1) != 0) {
                notificationEntry = postedEntry.entry;
            }
            if ((i & 2) != 0) {
                z = postedEntry.wasAdded;
            }
            boolean z7 = z;
            if ((i & 4) != 0) {
                z2 = postedEntry.wasUpdated;
            }
            boolean z8 = z2;
            if ((i & 8) != 0) {
                z3 = postedEntry.shouldHeadsUpEver;
            }
            boolean z9 = z3;
            if ((i & 16) != 0) {
                z4 = postedEntry.shouldHeadsUpAgain;
            }
            boolean z10 = z4;
            if ((i & 32) != 0) {
                z5 = postedEntry.isAlerting;
            }
            boolean z11 = z5;
            if ((i & 64) != 0) {
                z6 = postedEntry.isBinding;
            }
            return postedEntry.copy(notificationEntry, z7, z8, z9, z10, z11, z6);
        }

        public final NotificationEntry component1() {
            return this.entry;
        }

        public final boolean component2() {
            return this.wasAdded;
        }

        public final boolean component3() {
            return this.wasUpdated;
        }

        public final boolean component4() {
            return this.shouldHeadsUpEver;
        }

        public final boolean component5() {
            return this.shouldHeadsUpAgain;
        }

        public final boolean component6() {
            return this.isAlerting;
        }

        public final boolean component7() {
            return this.isBinding;
        }

        public final PostedEntry copy(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            return new PostedEntry(notificationEntry, z, z2, z3, z4, z5, z6);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PostedEntry)) {
                return false;
            }
            PostedEntry postedEntry = (PostedEntry) obj;
            return Intrinsics.areEqual((Object) this.entry, (Object) postedEntry.entry) && this.wasAdded == postedEntry.wasAdded && this.wasUpdated == postedEntry.wasUpdated && this.shouldHeadsUpEver == postedEntry.shouldHeadsUpEver && this.shouldHeadsUpAgain == postedEntry.shouldHeadsUpAgain && this.isAlerting == postedEntry.isAlerting && this.isBinding == postedEntry.isBinding;
        }

        public int hashCode() {
            int hashCode = this.entry.hashCode() * 31;
            boolean z = this.wasAdded;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (hashCode + (z ? 1 : 0)) * 31;
            boolean z3 = this.wasUpdated;
            if (z3) {
                z3 = true;
            }
            int i2 = (i + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.shouldHeadsUpEver;
            if (z4) {
                z4 = true;
            }
            int i3 = (i2 + (z4 ? 1 : 0)) * 31;
            boolean z5 = this.shouldHeadsUpAgain;
            if (z5) {
                z5 = true;
            }
            int i4 = (i3 + (z5 ? 1 : 0)) * 31;
            boolean z6 = this.isAlerting;
            if (z6) {
                z6 = true;
            }
            int i5 = (i4 + (z6 ? 1 : 0)) * 31;
            boolean z7 = this.isBinding;
            if (!z7) {
                z2 = z7;
            }
            return i5 + (z2 ? 1 : 0);
        }

        public String toString() {
            return "PostedEntry(entry=" + this.entry + ", wasAdded=" + this.wasAdded + ", wasUpdated=" + this.wasUpdated + ", shouldHeadsUpEver=" + this.shouldHeadsUpEver + ", shouldHeadsUpAgain=" + this.shouldHeadsUpAgain + ", isAlerting=" + this.isAlerting + ", isBinding=" + this.isBinding + ')';
        }

        public PostedEntry(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            Intrinsics.checkNotNullParameter(notificationEntry, "entry");
            this.entry = notificationEntry;
            this.wasAdded = z;
            this.wasUpdated = z2;
            this.shouldHeadsUpEver = z3;
            this.shouldHeadsUpAgain = z4;
            this.isAlerting = z5;
            this.isBinding = z6;
            String key2 = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key2, "entry.key");
            this.key = key2;
        }

        public final NotificationEntry getEntry() {
            return this.entry;
        }

        public final boolean getWasAdded() {
            return this.wasAdded;
        }

        public final boolean getWasUpdated() {
            return this.wasUpdated;
        }

        public final void setWasUpdated(boolean z) {
            this.wasUpdated = z;
        }

        public final boolean getShouldHeadsUpEver() {
            return this.shouldHeadsUpEver;
        }

        public final void setShouldHeadsUpEver(boolean z) {
            this.shouldHeadsUpEver = z;
        }

        public final boolean getShouldHeadsUpAgain() {
            return this.shouldHeadsUpAgain;
        }

        public final void setShouldHeadsUpAgain(boolean z) {
            this.shouldHeadsUpAgain = z;
        }

        public final boolean isAlerting() {
            return this.isAlerting;
        }

        public final void setAlerting(boolean z) {
            this.isAlerting = z;
        }

        public final boolean isBinding() {
            return this.isBinding;
        }

        public final void setBinding(boolean z) {
            this.isBinding = z;
        }

        public final String getKey() {
            return this.key;
        }

        public final boolean isHeadsUpAlready() {
            return this.isAlerting || this.isBinding;
        }

        public final boolean getCalculateShouldBeHeadsUpStrict() {
            return this.shouldHeadsUpEver && (this.wasAdded || this.shouldHeadsUpAgain || isHeadsUpAlready());
        }

        public final boolean getCalculateShouldBeHeadsUpNoRetract() {
            return isHeadsUpAlready() || (this.shouldHeadsUpEver && (this.wasAdded || this.shouldHeadsUpAgain));
        }
    }
}
