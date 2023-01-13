package com.android.systemui.statusbar.policy;

import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u000e\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005H\n¢\u0006\u0002\b\u0007"}, mo65043d2 = {"<anonymous>", "Landroid/widget/Button;", "index", "", "choice", "", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1 */
/* compiled from: SmartReplyStateInflater.kt */
final class C3198xfbefda06 extends Lambda implements Function2<Integer, CharSequence, Button> {
    final /* synthetic */ boolean $delayOnClickListener;
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    final /* synthetic */ SmartReplyView $smartReplyView;
    final /* synthetic */ SmartReplyStateInflaterImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C3198xfbefda06(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl, SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, boolean z) {
        super(2);
        this.this$0 = smartReplyStateInflaterImpl;
        this.$smartReplyView = smartReplyView;
        this.$entry = notificationEntry;
        this.$smartReplies = smartReplies;
        this.$delayOnClickListener = z;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke(((Number) obj).intValue(), (CharSequence) obj2);
    }

    public final Button invoke(int i, CharSequence charSequence) {
        SmartReplyInflater access$getSmartRepliesInflater$p = this.this$0.smartRepliesInflater;
        SmartReplyView smartReplyView = this.$smartReplyView;
        Intrinsics.checkNotNullExpressionValue(smartReplyView, "smartReplyView");
        NotificationEntry notificationEntry = this.$entry;
        SmartReplyView.SmartReplies smartReplies = this.$smartReplies;
        Intrinsics.checkNotNullExpressionValue(charSequence, "choice");
        return access$getSmartRepliesInflater$p.inflateReplyButton(smartReplyView, notificationEntry, smartReplies, i, charSequence, this.$delayOnClickListener);
    }
}
