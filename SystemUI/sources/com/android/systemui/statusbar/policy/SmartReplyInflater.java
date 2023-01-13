package com.android.systemui.statusbar.policy;

import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J8\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/SmartReplyInflater;", "", "inflateReplyButton", "Landroid/widget/Button;", "parent", "Lcom/android/systemui/statusbar/policy/SmartReplyView;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "smartReplies", "Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartReplies;", "replyIndex", "", "choice", "", "delayOnClickListener", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public interface SmartReplyInflater {
    Button inflateReplyButton(SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, CharSequence charSequence, boolean z);
}
