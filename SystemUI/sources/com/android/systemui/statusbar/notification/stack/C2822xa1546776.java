package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.phone.KeyguardBypassController;

/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda16 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2822xa1546776 implements KeyguardBypassController.OnBypassStateChangedListener {
    public final /* synthetic */ NotificationStackScrollLayout f$0;

    public /* synthetic */ C2822xa1546776(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.f$0 = notificationStackScrollLayout;
    }

    public final void onBypassStateChanged(boolean z) {
        this.f$0.setKeyguardBypassEnabled(z);
    }
}
