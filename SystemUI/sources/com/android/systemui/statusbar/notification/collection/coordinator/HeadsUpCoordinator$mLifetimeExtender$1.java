package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000eH\u0016¨\u0006\u000f"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$mLifetimeExtender$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "cancelLifetimeExtension", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getName", "", "maybeExtendLifetime", "", "reason", "", "setCallback", "callback", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender$OnEndLifetimeExtensionCallback;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mLifetimeExtender$1 implements NotifLifetimeExtender {
    final /* synthetic */ HeadsUpCoordinator this$0;

    public String getName() {
        return "HeadsUpCoordinator";
    }

    HeadsUpCoordinator$mLifetimeExtender$1(HeadsUpCoordinator headsUpCoordinator) {
        this.this$0 = headsUpCoordinator;
    }

    public void setCallback(NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        Intrinsics.checkNotNullParameter(onEndLifetimeExtensionCallback, "callback");
        this.this$0.mEndLifetimeExtension = onEndLifetimeExtensionCallback;
    }

    public boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (this.this$0.mHeadsUpManager.canRemoveImmediately(notificationEntry.getKey())) {
            return false;
        }
        if (this.this$0.isSticky(notificationEntry)) {
            this.this$0.mNotifsExtendingLifetime.put(notificationEntry, this.this$0.mExecutor.executeDelayed(new HeadsUpCoordinator$mLifetimeExtender$1$$ExternalSyntheticLambda0(this.this$0, notificationEntry), this.this$0.mHeadsUpManager.getEarliestRemovalTime(notificationEntry.getKey())));
            return true;
        }
        this.this$0.mExecutor.execute(new HeadsUpCoordinator$mLifetimeExtender$1$$ExternalSyntheticLambda1(this.this$0, notificationEntry));
        this.this$0.mNotifsExtendingLifetime.put(notificationEntry, null);
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: maybeExtendLifetime$lambda-0  reason: not valid java name */
    public static final void m3104maybeExtendLifetime$lambda0(HeadsUpCoordinator headsUpCoordinator, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(headsUpCoordinator, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        headsUpCoordinator.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
    }

    /* access modifiers changed from: private */
    /* renamed from: maybeExtendLifetime$lambda-1  reason: not valid java name */
    public static final void m3105maybeExtendLifetime$lambda1(HeadsUpCoordinator headsUpCoordinator, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(headsUpCoordinator, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        headsUpCoordinator.mHeadsUpManager.removeNotification(notificationEntry.getKey(), false);
    }

    public void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Runnable runnable = (Runnable) this.this$0.mNotifsExtendingLifetime.remove(notificationEntry);
        if (runnable != null) {
            runnable.run();
        }
    }
}
