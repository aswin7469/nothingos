package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0006\u0010\r\u001a\u00020\u000eJ\u0017\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002¢\u0006\u0002\u0010\u0012J\u0018\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/AnimatedImageNotificationManager;", "", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "bindEventManager", "Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "(Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;)V", "isStatusBarExpanded", "", "bind", "", "updateAnimatedImageDrawables", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Lkotlin/Unit;", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "animating", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager {
    private final BindEventManager bindEventManager;
    private final HeadsUpManager headsUpManager;
    /* access modifiers changed from: private */
    public boolean isStatusBarExpanded;
    /* access modifiers changed from: private */
    public final CommonNotifCollection notifCollection;
    private final StatusBarStateController statusBarStateController;

    @Inject
    public AnimatedImageNotificationManager(CommonNotifCollection commonNotifCollection, BindEventManager bindEventManager2, HeadsUpManager headsUpManager2, StatusBarStateController statusBarStateController2) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(bindEventManager2, "bindEventManager");
        Intrinsics.checkNotNullParameter(headsUpManager2, "headsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        this.notifCollection = commonNotifCollection;
        this.bindEventManager = bindEventManager2;
        this.headsUpManager = headsUpManager2;
        this.statusBarStateController = statusBarStateController2;
    }

    public final void bind() {
        this.headsUpManager.addListener(new AnimatedImageNotificationManager$bind$1(this));
        this.statusBarStateController.addCallback(new AnimatedImageNotificationManager$bind$2(this));
        this.bindEventManager.addListener(new AnimatedImageNotificationManager$bind$3(this));
    }

    /* access modifiers changed from: private */
    public final Unit updateAnimatedImageDrawables(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row == null) {
            return null;
        }
        updateAnimatedImageDrawables(row, row.isHeadsUp() || this.isStatusBarExpanded);
        return Unit.INSTANCE;
    }

    private final void updateAnimatedImageDrawables(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        Sequence sequence;
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        if (layouts == null || (sequence = ArraysKt.asSequence((T[]) layouts)) == null) {
            sequence = SequencesKt.emptySequence();
        }
        for (AnimatedImageDrawable animatedImageDrawable : SequencesKt.mapNotNull(SequencesKt.flatMap(SequencesKt.flatMap(SequencesKt.flatMap(sequence, AnimatedImageNotificationManager$updateAnimatedImageDrawables$2.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$3.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$4.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$5.INSTANCE)) {
            if (z) {
                animatedImageDrawable.start();
            } else {
                animatedImageDrawable.stop();
            }
        }
    }
}
