package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SmartspaceDedupingCoordinator$$ExternalSyntheticLambda2 implements NotificationLockscreenUserManager.KeyguardNotificationSuppressor {
    public final /* synthetic */ SmartspaceDedupingCoordinator f$0;

    public /* synthetic */ SmartspaceDedupingCoordinator$$ExternalSyntheticLambda2(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.f$0 = smartspaceDedupingCoordinator;
    }

    public final boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry) {
        return SmartspaceDedupingCoordinator.m3113attach$lambda0(this.f$0, notificationEntry);
    }
}
