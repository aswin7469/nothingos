package com.android.systemui.statusbar.policy;

import android.view.View;
import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SmartReplyInflaterImpl$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ SmartReplyInflaterImpl f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ SmartReplyView.SmartReplies f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ SmartReplyView f$4;
    public final /* synthetic */ Button f$5;
    public final /* synthetic */ CharSequence f$6;

    public /* synthetic */ SmartReplyInflaterImpl$$ExternalSyntheticLambda0(SmartReplyInflaterImpl smartReplyInflaterImpl, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, SmartReplyView smartReplyView, Button button, CharSequence charSequence) {
        this.f$0 = smartReplyInflaterImpl;
        this.f$1 = notificationEntry;
        this.f$2 = smartReplies;
        this.f$3 = i;
        this.f$4 = smartReplyView;
        this.f$5 = button;
        this.f$6 = charSequence;
    }

    public final void onClick(View view) {
        SmartReplyInflaterImpl.m3248inflateReplyButton$lambda1$lambda0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, view);
    }
}
