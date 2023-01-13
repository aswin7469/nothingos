package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConversationNotificationManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ConversationLayout f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ConversationNotificationManager$$ExternalSyntheticLambda1(ConversationLayout conversationLayout, boolean z) {
        this.f$0 = conversationLayout;
        this.f$1 = z;
    }

    public final void run() {
        ConversationNotificationManager.m3095updateNotificationRanking$lambda1$lambda0(this.f$0, this.f$1);
    }
}
