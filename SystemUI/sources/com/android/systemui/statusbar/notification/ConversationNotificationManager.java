package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import kotlin.Function;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 *2\u00020\u0001:\u0002*+B9\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u0010J\u0010\u0010\u001f\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0010\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\"H\u0002J\u0010\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\u0013H\u0002J\u0010\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020'H\u0002J\u0014\u0010(\u001a\u00020\u0010*\u00020\u00142\u0006\u0010)\u001a\u00020\u001aH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00140\u0012X\u0004¢\u0006\u0002\n\u0000¨\u0006,"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/ConversationNotificationManager;", "", "bindEventManager", "Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;", "notificationGroupManager", "Lcom/android/systemui/statusbar/notification/collection/legacy/NotificationGroupManagerLegacy;", "context", "Landroid/content/Context;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "featureFlags", "Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "mainHandler", "Landroid/os/Handler;", "(Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;Lcom/android/systemui/statusbar/notification/collection/legacy/NotificationGroupManagerLegacy;Landroid/content/Context;Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;Landroid/os/Handler;)V", "notifPanelCollapsed", "", "states", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/android/systemui/statusbar/notification/ConversationNotificationManager$ConversationState;", "getUnreadCount", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "recoveredBuilder", "Landroid/app/Notification$Builder;", "onEntryViewBound", "", "onNotificationPanelExpandStateChanged", "isCollapsed", "removeTrackedEntry", "resetBadgeUi", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "resetCount", "key", "updateNotificationRanking", "rankingMap", "Landroid/service/notification/NotificationListenerService$RankingMap;", "shouldIncrementUnread", "newBuilder", "Companion", "ConversationState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int IMPORTANCE_ANIMATION_DELAY = 960;
    private final BindEventManager bindEventManager;
    private final Context context;
    private final NotifPipelineFlags featureFlags;
    private final Handler mainHandler;
    /* access modifiers changed from: private */
    public final CommonNotifCollection notifCollection;
    private boolean notifPanelCollapsed = true;
    private final NotificationGroupManagerLegacy notificationGroupManager;
    private final ConcurrentHashMap<String, ConversationState> states = new ConcurrentHashMap<>();

    @Inject
    public ConversationNotificationManager(BindEventManager bindEventManager2, NotificationGroupManagerLegacy notificationGroupManagerLegacy, Context context2, CommonNotifCollection commonNotifCollection, NotifPipelineFlags notifPipelineFlags, @Main Handler handler) {
        Intrinsics.checkNotNullParameter(bindEventManager2, "bindEventManager");
        Intrinsics.checkNotNullParameter(notificationGroupManagerLegacy, "notificationGroupManager");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(notifPipelineFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        this.bindEventManager = bindEventManager2;
        this.notificationGroupManager = notificationGroupManagerLegacy;
        this.context = context2;
        this.notifCollection = commonNotifCollection;
        this.featureFlags = notifPipelineFlags;
        this.mainHandler = handler;
        commonNotifCollection.addCollectionListener(new NotifCollectionListener(this) {
            final /* synthetic */ ConversationNotificationManager this$0;

            {
                this.this$0 = r1;
            }

            public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                Intrinsics.checkNotNullParameter(rankingMap, "ranking");
                this.this$0.updateNotificationRanking(rankingMap);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                this.this$0.removeTrackedEntry(notificationEntry);
            }
        });
        bindEventManager2.addListener(new Object() {
            public final boolean equals(Object obj) {
                if (!(obj instanceof BindEventManager.Listener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ConversationNotificationManager.this, ConversationNotificationManager.class, "onEntryViewBound", "onEntryViewBound(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onViewBound(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "p0");
                ConversationNotificationManager.this.onEntryViewBound(notificationEntry);
            }
        });
    }

    /* access modifiers changed from: private */
    public static final Sequence<View> updateNotificationRanking$getLayouts(NotificationContentView notificationContentView) {
        return SequencesKt.sequenceOf(notificationContentView.getContractedChild(), notificationContentView.getExpandedChild(), notificationContentView.getHeadsUpChild());
    }

    /* access modifiers changed from: private */
    public final void updateNotificationRanking(NotificationListenerService.RankingMap rankingMap) {
        NotificationContentView[] layouts;
        Sequence asSequence;
        Sequence flatMap;
        Sequence mapNotNull;
        Sequence<ConversationLayout> filterNot;
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        Set<String> keySet = this.states.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "states.keys");
        for (NotificationEntry notificationEntry : SequencesKt.mapNotNull(CollectionsKt.asSequence(keySet), new C2650x2916e3cd(this))) {
            if (rankingMap.getRanking(notificationEntry.getSbn().getKey(), ranking) && ranking.isConversation()) {
                boolean isImportantConversation = ranking.getChannel().isImportantConversation();
                ExpandableNotificationRow row = notificationEntry.getRow();
                boolean z = false;
                if (!(row == null || (layouts = row.getLayouts()) == null || (asSequence = ArraysKt.asSequence((T[]) layouts)) == null || (flatMap = SequencesKt.flatMap(asSequence, ConversationNotificationManager$updateNotificationRanking$1.INSTANCE)) == null || (mapNotNull = SequencesKt.mapNotNull(flatMap, ConversationNotificationManager$updateNotificationRanking$2.INSTANCE)) == null || (filterNot = SequencesKt.filterNot(mapNotNull, new ConversationNotificationManager$updateNotificationRanking$3(isImportantConversation))) == null)) {
                    boolean z2 = false;
                    for (ConversationLayout conversationLayout : filterNot) {
                        if (!isImportantConversation || !notificationEntry.isMarkedForUserTriggeredMovement()) {
                            conversationLayout.setIsImportantConversation(isImportantConversation, false);
                        } else {
                            this.mainHandler.postDelayed(new ConversationNotificationManager$$ExternalSyntheticLambda1(conversationLayout, isImportantConversation), 960);
                        }
                        z2 = true;
                    }
                    z = z2;
                }
                if (z && !this.featureFlags.isNewPipelineEnabled()) {
                    this.notificationGroupManager.updateIsolation(notificationEntry);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateNotificationRanking$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3091updateNotificationRanking$lambda1$lambda0(ConversationLayout conversationLayout, boolean z) {
        Intrinsics.checkNotNullParameter(conversationLayout, "$layout");
        conversationLayout.setIsImportantConversation(z, true);
    }

    public final void onEntryViewBound(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (notificationEntry.getRanking().isConversation()) {
            ExpandableNotificationRow row = notificationEntry.getRow();
            if (row != null) {
                row.setOnExpansionChangedListener(new ConversationNotificationManager$$ExternalSyntheticLambda4(notificationEntry, this));
            }
            ExpandableNotificationRow row2 = notificationEntry.getRow();
            boolean z = false;
            if (row2 != null && row2.isExpanded()) {
                z = true;
            }
            onEntryViewBound$updateCount(this, notificationEntry, z);
        }
    }

    private static final void onEntryViewBound$updateCount(ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry, boolean z) {
        if (!z) {
            return;
        }
        if (!conversationNotificationManager.notifPanelCollapsed || notificationEntry.isPinnedAndExpanded()) {
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            conversationNotificationManager.resetCount(key);
            ExpandableNotificationRow row = notificationEntry.getRow();
            if (row != null) {
                conversationNotificationManager.resetBadgeUi(row);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onEntryViewBound$lambda-3  reason: not valid java name */
    public static final void m3087onEntryViewBound$lambda3(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        Intrinsics.checkNotNullParameter(conversationNotificationManager, "this$0");
        ExpandableNotificationRow row = notificationEntry.getRow();
        boolean z2 = false;
        if (row != null && row.isShown()) {
            z2 = true;
        }
        if (!z2 || !z) {
            onEntryViewBound$updateCount(conversationNotificationManager, notificationEntry, z);
        } else {
            notificationEntry.getRow().performOnIntrinsicHeightReached(new ConversationNotificationManager$$ExternalSyntheticLambda2(z, conversationNotificationManager, notificationEntry));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onEntryViewBound$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3088onEntryViewBound$lambda3$lambda2(boolean z, ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(conversationNotificationManager, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        onEntryViewBound$updateCount(conversationNotificationManager, notificationEntry, z);
    }

    private final boolean shouldIncrementUnread(ConversationState conversationState, Notification.Builder builder) {
        if ((conversationState.getNotification().flags & 8) != 0) {
            return false;
        }
        return Notification.areStyledNotificationsVisiblyDifferent(Notification.Builder.recoverBuilder(this.context, conversationState.getNotification()), builder);
    }

    public final int getUnreadCount(NotificationEntry notificationEntry, Notification.Builder builder) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(builder, "recoveredBuilder");
        ConversationState compute = this.states.compute(notificationEntry.getKey(), new ConversationNotificationManager$$ExternalSyntheticLambda5(notificationEntry, this, builder));
        Intrinsics.checkNotNull(compute);
        return compute.getUnreadCount();
    }

    /* access modifiers changed from: private */
    /* renamed from: getUnreadCount$lambda-5  reason: not valid java name */
    public static final ConversationState m3086getUnreadCount$lambda5(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager, Notification.Builder builder, String str, ConversationState conversationState) {
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        Intrinsics.checkNotNullParameter(conversationNotificationManager, "this$0");
        Intrinsics.checkNotNullParameter(builder, "$recoveredBuilder");
        Intrinsics.checkNotNullParameter(str, "<anonymous parameter 0>");
        int i = 1;
        if (conversationState != null) {
            i = conversationNotificationManager.shouldIncrementUnread(conversationState, builder) ? conversationState.getUnreadCount() + 1 : conversationState.getUnreadCount();
        }
        Notification notification = notificationEntry.getSbn().getNotification();
        Intrinsics.checkNotNullExpressionValue(notification, "entry.sbn.notification");
        return new ConversationState(i, notification);
    }

    public final void onNotificationPanelExpandStateChanged(boolean z) {
        this.notifPanelCollapsed = z;
        if (!z) {
            Map map = MapsKt.toMap(SequencesKt.mapNotNull(MapsKt.asSequence(this.states), new C2649x7388b338(this)));
            this.states.replaceAll(new ConversationNotificationManager$$ExternalSyntheticLambda0(map));
            for (ExpandableNotificationRow resetBadgeUi : SequencesKt.mapNotNull(CollectionsKt.asSequence(map.values()), C2648x5e24d3c0.INSTANCE)) {
                resetBadgeUi(resetBadgeUi);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onNotificationPanelExpandStateChanged$lambda-6  reason: not valid java name */
    public static final ConversationState m3089onNotificationPanelExpandStateChanged$lambda6(Map map, String str, ConversationState conversationState) {
        Intrinsics.checkNotNullParameter(map, "$expanded");
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(conversationState, AuthDialog.KEY_BIOMETRIC_STATE);
        return map.containsKey(str) ? ConversationState.copy$default(conversationState, 0, (Notification) null, 2, (Object) null) : conversationState;
    }

    private final void resetCount(String str) {
        this.states.compute(str, new ConversationNotificationManager$$ExternalSyntheticLambda3());
    }

    /* access modifiers changed from: private */
    /* renamed from: resetCount$lambda-8  reason: not valid java name */
    public static final ConversationState m3090resetCount$lambda8(String str, ConversationState conversationState) {
        Intrinsics.checkNotNullParameter(str, "<anonymous parameter 0>");
        if (conversationState != null) {
            return ConversationState.copy$default(conversationState, 0, (Notification) null, 2, (Object) null);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public final void removeTrackedEntry(NotificationEntry notificationEntry) {
        this.states.remove(notificationEntry.getKey());
    }

    private final void resetBadgeUi(ExpandableNotificationRow expandableNotificationRow) {
        Sequence sequence;
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        if (layouts == null || (sequence = ArraysKt.asSequence((T[]) layouts)) == null) {
            sequence = SequencesKt.emptySequence();
        }
        for (ConversationLayout unreadCount : SequencesKt.mapNotNull(SequencesKt.flatMap(sequence, ConversationNotificationManager$resetBadgeUi$1.INSTANCE), ConversationNotificationManager$resetBadgeUi$2.INSTANCE)) {
            unreadCount.setUnreadCount(0);
        }
    }

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/ConversationNotificationManager$ConversationState;", "", "unreadCount", "", "notification", "Landroid/app/Notification;", "(ILandroid/app/Notification;)V", "getNotification", "()Landroid/app/Notification;", "getUnreadCount", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ConversationNotifications.kt */
    private static final class ConversationState {
        private final Notification notification;
        private final int unreadCount;

        public static /* synthetic */ ConversationState copy$default(ConversationState conversationState, int i, Notification notification2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = conversationState.unreadCount;
            }
            if ((i2 & 2) != 0) {
                notification2 = conversationState.notification;
            }
            return conversationState.copy(i, notification2);
        }

        public final int component1() {
            return this.unreadCount;
        }

        public final Notification component2() {
            return this.notification;
        }

        public final ConversationState copy(int i, Notification notification2) {
            Intrinsics.checkNotNullParameter(notification2, "notification");
            return new ConversationState(i, notification2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConversationState)) {
                return false;
            }
            ConversationState conversationState = (ConversationState) obj;
            return this.unreadCount == conversationState.unreadCount && Intrinsics.areEqual((Object) this.notification, (Object) conversationState.notification);
        }

        public int hashCode() {
            return (Integer.hashCode(this.unreadCount) * 31) + this.notification.hashCode();
        }

        public String toString() {
            return "ConversationState(unreadCount=" + this.unreadCount + ", notification=" + this.notification + ')';
        }

        public ConversationState(int i, Notification notification2) {
            Intrinsics.checkNotNullParameter(notification2, "notification");
            this.unreadCount = i;
            this.notification = notification2;
        }

        public final Notification getNotification() {
            return this.notification;
        }

        public final int getUnreadCount() {
            return this.unreadCount;
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/ConversationNotificationManager$Companion;", "", "()V", "IMPORTANCE_ANIMATION_DELAY", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ConversationNotifications.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
