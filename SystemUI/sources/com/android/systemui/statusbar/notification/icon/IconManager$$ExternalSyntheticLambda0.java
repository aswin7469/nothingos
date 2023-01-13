package com.android.systemui.statusbar.notification.icon;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class IconManager$$ExternalSyntheticLambda0 implements NotificationEntry.OnSensitivityChangedListener {
    public final /* synthetic */ IconManager f$0;

    public /* synthetic */ IconManager$$ExternalSyntheticLambda0(IconManager iconManager) {
        this.f$0 = iconManager;
    }

    public final void onSensitivityChanged(NotificationEntry notificationEntry) {
        IconManager.m3123sensitivityListener$lambda0(this.f$0, notificationEntry);
    }
}
