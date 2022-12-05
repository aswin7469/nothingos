package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager {
    @NotNull
    private final HeadsUpManager headsUpManager;
    private boolean isStatusBarExpanded;
    @NotNull
    private final NotificationEntryManager notificationEntryManager;
    @NotNull
    private final StatusBarStateController statusBarStateController;

    public AnimatedImageNotificationManager(@NotNull NotificationEntryManager notificationEntryManager, @NotNull HeadsUpManager headsUpManager, @NotNull StatusBarStateController statusBarStateController) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        this.notificationEntryManager = notificationEntryManager;
        this.headsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController;
    }

    public final void bind() {
        this.headsUpManager.addListener(new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$1
            @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
            public void onHeadsUpStateChanged(@NotNull NotificationEntry entry, boolean z) {
                boolean z2;
                boolean z3;
                Intrinsics.checkNotNullParameter(entry, "entry");
                ExpandableNotificationRow row = entry.getRow();
                if (row == null) {
                    return;
                }
                AnimatedImageNotificationManager animatedImageNotificationManager = AnimatedImageNotificationManager.this;
                if (!z) {
                    z3 = animatedImageNotificationManager.isStatusBarExpanded;
                    if (!z3) {
                        z2 = false;
                        animatedImageNotificationManager.updateAnimatedImageDrawables(row, z2);
                    }
                }
                z2 = true;
                animatedImageNotificationManager.updateAnimatedImageDrawables(row, z2);
            }
        });
        this.statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$2
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                NotificationEntryManager notificationEntryManager;
                AnimatedImageNotificationManager.this.isStatusBarExpanded = z;
                notificationEntryManager = AnimatedImageNotificationManager.this.notificationEntryManager;
                List<NotificationEntry> activeNotificationsForCurrentUser = notificationEntryManager.getActiveNotificationsForCurrentUser();
                Intrinsics.checkNotNullExpressionValue(activeNotificationsForCurrentUser, "notificationEntryManager.activeNotificationsForCurrentUser");
                AnimatedImageNotificationManager animatedImageNotificationManager = AnimatedImageNotificationManager.this;
                for (NotificationEntry notificationEntry : activeNotificationsForCurrentUser) {
                    ExpandableNotificationRow row = notificationEntry.getRow();
                    if (row != null) {
                        animatedImageNotificationManager.updateAnimatedImageDrawables(row, z || row.isHeadsUp());
                    }
                }
            }
        });
        this.notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$3
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryInflated(@NotNull NotificationEntry entry) {
                boolean z;
                Intrinsics.checkNotNullParameter(entry, "entry");
                ExpandableNotificationRow row = entry.getRow();
                if (row == null) {
                    return;
                }
                AnimatedImageNotificationManager animatedImageNotificationManager = AnimatedImageNotificationManager.this;
                z = animatedImageNotificationManager.isStatusBarExpanded;
                animatedImageNotificationManager.updateAnimatedImageDrawables(row, z || row.isHeadsUp());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryReinflated(@NotNull NotificationEntry entry) {
                Intrinsics.checkNotNullParameter(entry, "entry");
                onEntryInflated(entry);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateAnimatedImageDrawables(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence asSequence = layouts == null ? null : ArraysKt___ArraysKt.asSequence(layouts);
        if (asSequence == null) {
            asSequence = SequencesKt.emptySequence();
        }
        for (AnimatedImageDrawable animatedImageDrawable : SequencesKt.mapNotNull(SequencesKt.flatMap(SequencesKt.flatMap(SequencesKt.flatMap(asSequence, AnimatedImageNotificationManager$updateAnimatedImageDrawables$1.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$2.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$3.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$4.INSTANCE)) {
            if (z) {
                animatedImageDrawable.start();
            } else {
                animatedImageDrawable.stop();
            }
        }
    }
}
