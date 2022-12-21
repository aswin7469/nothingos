package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SmartActionInflaterImpl$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ SmartActionInflaterImpl f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ SmartReplyView.SmartActions f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ Notification.Action f$4;

    public /* synthetic */ SmartActionInflaterImpl$$ExternalSyntheticLambda0(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action) {
        this.f$0 = smartActionInflaterImpl;
        this.f$1 = notificationEntry;
        this.f$2 = smartActions;
        this.f$3 = i;
        this.f$4 = action;
    }

    public final void onClick(View view) {
        SmartActionInflaterImpl.m3241inflateActionButton$lambda2$lambda1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, view);
    }
}
