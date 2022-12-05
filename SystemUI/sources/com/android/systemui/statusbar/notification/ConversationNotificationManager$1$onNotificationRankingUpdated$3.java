package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$3 extends Lambda implements Function1<ConversationLayout, Boolean> {
    final /* synthetic */ boolean $important;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$1$onNotificationRankingUpdated$3(boolean z) {
        super(1);
        this.$important = z;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(ConversationLayout conversationLayout) {
        return Boolean.valueOf(invoke2(conversationLayout));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(@NotNull ConversationLayout it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.isImportantConversation() == this.$important;
    }
}
