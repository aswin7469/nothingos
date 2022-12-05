package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$2 extends Lambda implements Function1<View, ConversationLayout> {
    public static final ConversationNotificationManager$1$onNotificationRankingUpdated$2 INSTANCE = new ConversationNotificationManager$1$onNotificationRankingUpdated$2();

    ConversationNotificationManager$1$onNotificationRankingUpdated$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final ConversationLayout mo1949invoke(View view) {
        if (view instanceof ConversationLayout) {
            return (ConversationLayout) view;
        }
        return null;
    }
}
