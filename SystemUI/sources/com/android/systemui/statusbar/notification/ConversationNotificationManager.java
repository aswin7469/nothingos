package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Context context;
    @NotNull
    private final Handler mainHandler;
    @NotNull
    private final NotificationEntryManager notificationEntryManager;
    @NotNull
    private final NotificationGroupManagerLegacy notificationGroupManager;
    @NotNull
    private final ConcurrentHashMap<String, ConversationState> states = new ConcurrentHashMap<>();
    private boolean notifPanelCollapsed = true;

    public ConversationNotificationManager(@NotNull NotificationEntryManager notificationEntryManager, @NotNull NotificationGroupManagerLegacy notificationGroupManager, @NotNull Context context, @NotNull Handler mainHandler) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(notificationGroupManager, "notificationGroupManager");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mainHandler, "mainHandler");
        this.notificationEntryManager = notificationEntryManager;
        this.notificationGroupManager = notificationGroupManager;
        this.context = context;
        this.mainHandler = mainHandler;
        notificationEntryManager.addNotificationEntryListener(new AnonymousClass1());
    }

    /* compiled from: ConversationNotifications.kt */
    /* renamed from: com.android.systemui.statusbar.notification.ConversationNotificationManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 implements NotificationEntryListener {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final Sequence<View> onNotificationRankingUpdated$getLayouts(NotificationContentView notificationContentView) {
            return SequencesKt.sequenceOf(notificationContentView.getContractedChild(), notificationContentView.getExpandedChild(), notificationContentView.getHeadsUpChild());
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onNotificationRankingUpdated(@NotNull NotificationListenerService.RankingMap rankingMap) {
            NotificationContentView[] layouts;
            Sequence<ConversationLayout> filterNot;
            Sequence flatMap;
            Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
            NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
            Set keySet = ConversationNotificationManager.this.states.keySet();
            Intrinsics.checkNotNullExpressionValue(keySet, "states.keys");
            for (NotificationEntry notificationEntry : SequencesKt.mapNotNull(CollectionsKt.asSequence(keySet), new ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1(ConversationNotificationManager.this))) {
                if (rankingMap.getRanking(notificationEntry.getSbn().getKey(), ranking) && ranking.isConversation()) {
                    final boolean isImportantConversation = ranking.getChannel().isImportantConversation();
                    ExpandableNotificationRow row = notificationEntry.getRow();
                    Sequence sequence = null;
                    Sequence asSequence = (row == null || (layouts = row.getLayouts()) == null) ? null : ArraysKt.asSequence(layouts);
                    if (asSequence != null && (flatMap = SequencesKt.flatMap(asSequence, ConversationNotificationManager$1$onNotificationRankingUpdated$1.INSTANCE)) != null) {
                        sequence = SequencesKt.mapNotNull(flatMap, ConversationNotificationManager$1$onNotificationRankingUpdated$2.INSTANCE);
                    }
                    boolean z = false;
                    if (sequence != null && (filterNot = SequencesKt.filterNot(sequence, new ConversationNotificationManager$1$onNotificationRankingUpdated$3(isImportantConversation))) != null) {
                        ConversationNotificationManager conversationNotificationManager = ConversationNotificationManager.this;
                        boolean z2 = false;
                        for (final ConversationLayout conversationLayout : filterNot) {
                            if (isImportantConversation && notificationEntry.isMarkedForUserTriggeredMovement()) {
                                conversationNotificationManager.mainHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$1$onNotificationRankingUpdated$4$1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        conversationLayout.setIsImportantConversation(isImportantConversation, true);
                                    }
                                }, 960L);
                            } else {
                                conversationLayout.setIsImportantConversation(isImportantConversation, false);
                            }
                            z2 = true;
                        }
                        z = z2;
                    }
                    if (z) {
                        ConversationNotificationManager.this.notificationGroupManager.updateIsolation(notificationEntry);
                    }
                }
            }
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryInflated(@NotNull final NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            if (!entry.getRanking().isConversation()) {
                return;
            }
            ExpandableNotificationRow row = entry.getRow();
            if (row != null) {
                final ConversationNotificationManager conversationNotificationManager = ConversationNotificationManager.this;
                row.setOnExpansionChangedListener(new ExpandableNotificationRow.OnExpansionChangedListener() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$1$onEntryInflated$1
                    @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.OnExpansionChangedListener
                    public final void onExpansionChanged(final boolean z) {
                        ExpandableNotificationRow row2 = NotificationEntry.this.getRow();
                        if (!Intrinsics.areEqual(row2 == null ? null : Boolean.valueOf(row2.isShown()), Boolean.TRUE) || !z) {
                            ConversationNotificationManager.AnonymousClass1.onEntryInflated$updateCount(conversationNotificationManager, NotificationEntry.this, z);
                            return;
                        }
                        ExpandableNotificationRow row3 = NotificationEntry.this.getRow();
                        final ConversationNotificationManager conversationNotificationManager2 = conversationNotificationManager;
                        final NotificationEntry notificationEntry = NotificationEntry.this;
                        row3.performOnIntrinsicHeightReached(new Runnable() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$1$onEntryInflated$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ConversationNotificationManager.AnonymousClass1.onEntryInflated$updateCount(conversationNotificationManager2, notificationEntry, z);
                            }
                        });
                    }
                });
            }
            ConversationNotificationManager conversationNotificationManager2 = ConversationNotificationManager.this;
            ExpandableNotificationRow row2 = entry.getRow();
            onEntryInflated$updateCount(conversationNotificationManager2, entry, Intrinsics.areEqual(row2 == null ? null : Boolean.valueOf(row2.isExpanded()), Boolean.TRUE));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onEntryInflated$updateCount(ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry, boolean z) {
            if (z) {
                if (conversationNotificationManager.notifPanelCollapsed && !notificationEntry.isPinnedAndExpanded()) {
                    return;
                }
                String key = notificationEntry.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "entry.key");
                conversationNotificationManager.resetCount(key);
                ExpandableNotificationRow row = notificationEntry.getRow();
                if (row == null) {
                    return;
                }
                conversationNotificationManager.resetBadgeUi(row);
            }
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryReinflated(@NotNull NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            onEntryInflated(entry);
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryRemoved(@NotNull NotificationEntry entry, @Nullable NotificationVisibility notificationVisibility, boolean z, int i) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            ConversationNotificationManager.this.removeTrackedEntry(entry);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean shouldIncrementUnread(ConversationState conversationState, Notification.Builder builder) {
        if ((conversationState.getNotification().flags & 8) != 0) {
            return false;
        }
        return Notification.areStyledNotificationsVisiblyDifferent(Notification.Builder.recoverBuilder(this.context, conversationState.getNotification()), builder);
    }

    public final int getUnreadCount(@NotNull final NotificationEntry entry, @NotNull final Notification.Builder recoveredBuilder) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(recoveredBuilder, "recoveredBuilder");
        ConversationState compute = this.states.compute(entry.getKey(), new BiFunction<String, ConversationState, ConversationState>() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$getUnreadCount$1
            @Override // java.util.function.BiFunction
            @Nullable
            public final ConversationNotificationManager.ConversationState apply(@NotNull String noName_0, @Nullable ConversationNotificationManager.ConversationState conversationState) {
                boolean shouldIncrementUnread;
                Intrinsics.checkNotNullParameter(noName_0, "$noName_0");
                int i = 1;
                if (conversationState != null) {
                    shouldIncrementUnread = this.shouldIncrementUnread(conversationState, recoveredBuilder);
                    i = shouldIncrementUnread ? conversationState.getUnreadCount() + 1 : conversationState.getUnreadCount();
                }
                Notification notification = NotificationEntry.this.getSbn().getNotification();
                Intrinsics.checkNotNullExpressionValue(notification, "entry.sbn.notification");
                return new ConversationNotificationManager.ConversationState(i, notification);
            }
        });
        Intrinsics.checkNotNull(compute);
        return compute.getUnreadCount();
    }

    public final void onNotificationPanelExpandStateChanged(boolean z) {
        this.notifPanelCollapsed = z;
        if (z) {
            return;
        }
        final Map map = MapsKt.toMap(SequencesKt.mapNotNull(MapsKt.asSequence(this.states), new ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1(this)));
        this.states.replaceAll(new BiFunction<String, ConversationState, ConversationState>() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$1
            @Override // java.util.function.BiFunction
            @NotNull
            public final ConversationNotificationManager.ConversationState apply(@NotNull String key, @NotNull ConversationNotificationManager.ConversationState state) {
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(state, "state");
                return map.containsKey(key) ? ConversationNotificationManager.ConversationState.copy$default(state, 0, null, 2, null) : state;
            }
        });
        for (ExpandableNotificationRow expandableNotificationRow : SequencesKt.mapNotNull(CollectionsKt.asSequence(map.values()), ConversationNotificationManager$onNotificationPanelExpandStateChanged$2.INSTANCE)) {
            resetBadgeUi(expandableNotificationRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetCount(String str) {
        this.states.compute(str, ConversationNotificationManager$resetCount$1.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeTrackedEntry(NotificationEntry notificationEntry) {
        this.states.remove(notificationEntry.getKey());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetBadgeUi(ExpandableNotificationRow expandableNotificationRow) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence asSequence = layouts == null ? null : ArraysKt.asSequence(layouts);
        if (asSequence == null) {
            asSequence = SequencesKt.emptySequence();
        }
        for (ConversationLayout conversationLayout : SequencesKt.mapNotNull(SequencesKt.flatMap(asSequence, ConversationNotificationManager$resetBadgeUi$1.INSTANCE), ConversationNotificationManager$resetBadgeUi$2.INSTANCE)) {
            conversationLayout.setUnreadCount(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ConversationNotifications.kt */
    /* loaded from: classes.dex */
    public static final class ConversationState {
        @NotNull
        private final Notification notification;
        private final int unreadCount;

        public static /* synthetic */ ConversationState copy$default(ConversationState conversationState, int i, Notification notification, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = conversationState.unreadCount;
            }
            if ((i2 & 2) != 0) {
                notification = conversationState.notification;
            }
            return conversationState.copy(i, notification);
        }

        @NotNull
        public final ConversationState copy(int i, @NotNull Notification notification) {
            Intrinsics.checkNotNullParameter(notification, "notification");
            return new ConversationState(i, notification);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConversationState)) {
                return false;
            }
            ConversationState conversationState = (ConversationState) obj;
            return this.unreadCount == conversationState.unreadCount && Intrinsics.areEqual(this.notification, conversationState.notification);
        }

        public int hashCode() {
            return (Integer.hashCode(this.unreadCount) * 31) + this.notification.hashCode();
        }

        @NotNull
        public String toString() {
            return "ConversationState(unreadCount=" + this.unreadCount + ", notification=" + this.notification + ')';
        }

        public ConversationState(int i, @NotNull Notification notification) {
            Intrinsics.checkNotNullParameter(notification, "notification");
            this.unreadCount = i;
            this.notification = notification;
        }

        @NotNull
        public final Notification getNotification() {
            return this.notification;
        }

        public final int getUnreadCount() {
            return this.unreadCount;
        }
    }

    /* compiled from: ConversationNotifications.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
