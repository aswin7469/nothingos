package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartReplyInflaterImpl$onSmartReplyClick$1 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ Button $button;
    final /* synthetic */ CharSequence $choice;
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ int $replyIndex;
    final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    final /* synthetic */ SmartReplyView $smartReplyView;
    final /* synthetic */ SmartReplyInflaterImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartReplyInflaterImpl$onSmartReplyClick$1(SmartReplyInflaterImpl smartReplyInflaterImpl, SmartReplyView.SmartReplies smartReplies, Button button, CharSequence charSequence, int i, NotificationEntry notificationEntry, SmartReplyView smartReplyView) {
        super(0);
        this.this$0 = smartReplyInflaterImpl;
        this.$smartReplies = smartReplies;
        this.$button = button;
        this.$choice = charSequence;
        this.$replyIndex = i;
        this.$entry = notificationEntry;
        this.$smartReplyView = smartReplyView;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1951invoke() {
        return Boolean.valueOf(mo1951invoke());
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final Boolean mo1951invoke() {
        SmartReplyConstants smartReplyConstants;
        SmartReplyController smartReplyController;
        Intent createRemoteInputIntent;
        Context context;
        NotificationRemoteInputManager notificationRemoteInputManager;
        smartReplyConstants = this.this$0.constants;
        if (smartReplyConstants.getEffectiveEditChoicesBeforeSending(this.$smartReplies.remoteInput.getEditChoicesBeforeSending())) {
            notificationRemoteInputManager = this.this$0.remoteInputManager;
            Button button = this.$button;
            SmartReplyView.SmartReplies smartReplies = this.$smartReplies;
            RemoteInput remoteInput = smartReplies.remoteInput;
            notificationRemoteInputManager.activateRemoteInput(button, new RemoteInput[]{remoteInput}, remoteInput, smartReplies.pendingIntent, new NotificationEntry.EditedSuggestionInfo(this.$choice, this.$replyIndex));
        } else {
            smartReplyController = this.this$0.smartReplyController;
            smartReplyController.smartReplySent(this.$entry, this.$replyIndex, this.$button.getText(), NotificationLogger.getNotificationLocation(this.$entry).toMetricsEventEnum(), false);
            this.$entry.setHasSentReply();
            try {
                createRemoteInputIntent = this.this$0.createRemoteInputIntent(this.$smartReplies, this.$choice);
                PendingIntent pendingIntent = this.$smartReplies.pendingIntent;
                context = this.this$0.context;
                pendingIntent.send(context, 0, createRemoteInputIntent);
            } catch (PendingIntent.CanceledException e) {
                Log.w("SmartReplyViewInflater", "Unable to send smart reply", e);
            }
            this.$smartReplyView.hideSmartSuggestions();
        }
        return null;
    }
}
