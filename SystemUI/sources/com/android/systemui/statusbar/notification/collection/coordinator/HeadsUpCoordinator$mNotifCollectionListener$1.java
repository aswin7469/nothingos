package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u000b"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mNotifCollectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryAdded", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryCleanUp", "onEntryRemoved", "reason", "", "onEntryUpdated", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mNotifCollectionListener$1 implements NotifCollectionListener {
    final /* synthetic */ HeadsUpCoordinator this$0;

    HeadsUpCoordinator$mNotifCollectionListener$1(HeadsUpCoordinator headsUpCoordinator) {
        this.this$0 = headsUpCoordinator;
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean shouldHeadsUp = this.this$0.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry);
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        this.this$0.mPostedEntries.put(key, new HeadsUpCoordinator.PostedEntry(notificationEntry, true, false, shouldHeadsUp, true, false, false));
    }

    public void onEntryUpdated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        HeadsUpCoordinator.PostedEntry postedEntry = (HeadsUpCoordinator.PostedEntry) this.this$0.mPostedEntries.compute(notificationEntry.getKey(), new C2679xf16970b3(notificationEntry, this.this$0.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry), this.this$0.shouldHunAgain(notificationEntry), this.this$0.mHeadsUpManager.isAlerting(notificationEntry.getKey()), this.this$0.isEntryBinding(notificationEntry)));
        if (!(postedEntry != null && !postedEntry.getShouldHeadsUpEver())) {
            return;
        }
        if (postedEntry.isAlerting()) {
            this.this$0.mHeadsUpManager.removeNotification(postedEntry.getKey(), false);
        } else if (postedEntry.isBinding()) {
            this.this$0.cancelHeadsUpBind(postedEntry.getEntry());
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onEntryUpdated$lambda-1  reason: not valid java name */
    public static final HeadsUpCoordinator.PostedEntry m3106onEntryUpdated$lambda1(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, String str, HeadsUpCoordinator.PostedEntry postedEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        Intrinsics.checkNotNullParameter(str, "<anonymous parameter 0>");
        if (postedEntry == null) {
            return new HeadsUpCoordinator.PostedEntry(notificationEntry, false, true, z, z2, z3, z4);
        }
        boolean z5 = true;
        postedEntry.setWasUpdated(true);
        postedEntry.setShouldHeadsUpEver(postedEntry.getShouldHeadsUpEver() || z);
        if (!postedEntry.getShouldHeadsUpAgain() && !z2) {
            z5 = false;
        }
        postedEntry.setShouldHeadsUpAgain(z5);
        postedEntry.setAlerting(z3);
        postedEntry.setBinding(z4);
        return postedEntry;
    }

    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.this$0.mPostedEntries.remove(notificationEntry.getKey());
        this.this$0.cancelHeadsUpBind(notificationEntry);
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        if (this.this$0.mHeadsUpManager.isAlerting(key)) {
            this.this$0.mHeadsUpManager.removeNotification(notificationEntry.getKey(), this.this$0.mRemoteInputManager.isSpinning(key) && !NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY);
        }
    }

    public void onEntryCleanUp(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.this$0.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
    }
}
