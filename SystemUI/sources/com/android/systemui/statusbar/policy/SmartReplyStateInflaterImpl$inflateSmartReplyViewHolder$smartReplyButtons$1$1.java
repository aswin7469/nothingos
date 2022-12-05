package com.android.systemui.statusbar.policy;

import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1 extends Lambda implements Function2<Integer, CharSequence, Button> {
    final /* synthetic */ boolean $delayOnClickListener;
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    final /* synthetic */ SmartReplyView $smartReplyView;
    final /* synthetic */ SmartReplyStateInflaterImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl, SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, boolean z) {
        super(2);
        this.this$0 = smartReplyStateInflaterImpl;
        this.$smartReplyView = smartReplyView;
        this.$entry = notificationEntry;
        this.$smartReplies = smartReplies;
        this.$delayOnClickListener = z;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Button mo1950invoke(Integer num, CharSequence charSequence) {
        return invoke(num.intValue(), charSequence);
    }

    @NotNull
    public final Button invoke(int i, CharSequence choice) {
        SmartReplyInflater smartReplyInflater;
        smartReplyInflater = this.this$0.smartRepliesInflater;
        SmartReplyView smartReplyView = this.$smartReplyView;
        Intrinsics.checkNotNullExpressionValue(smartReplyView, "smartReplyView");
        NotificationEntry notificationEntry = this.$entry;
        SmartReplyView.SmartReplies smartReplies = this.$smartReplies;
        Intrinsics.checkNotNullExpressionValue(choice, "choice");
        return smartReplyInflater.inflateReplyButton(smartReplyView, notificationEntry, smartReplies, i, choice, this.$delayOnClickListener);
    }
}
