package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* renamed from: com.android.systemui.statusbar.NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2583x9a54ce8a implements Runnable {
    public final /* synthetic */ NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ C2583x9a54ce8a(NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender, NotificationEntry notificationEntry) {
        this.f$0 = legacyRemoteInputLifetimeExtender;
        this.f$1 = notificationEntry;
    }

    public final void run() {
        this.f$0.mo38840x9dd93c8f(this.f$1);
    }
}
