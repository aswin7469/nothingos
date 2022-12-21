package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/internal/widget/ConversationLayout;", "invoke", "(Lcom/android/internal/widget/ConversationLayout;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
final class ConversationNotificationManager$updateNotificationRanking$3 extends Lambda implements Function1<ConversationLayout, Boolean> {
    final /* synthetic */ boolean $important;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ConversationNotificationManager$updateNotificationRanking$3(boolean z) {
        super(1);
        this.$important = z;
    }

    public final Boolean invoke(ConversationLayout conversationLayout) {
        Intrinsics.checkNotNullParameter(conversationLayout, "it");
        return Boolean.valueOf(conversationLayout.isImportantConversation() == this.$important);
    }
}
