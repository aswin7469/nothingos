package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
/* synthetic */ class ConversationNotificationManager$1$onNotificationRankingUpdated$1 extends FunctionReferenceImpl implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$1$onNotificationRankingUpdated$1 INSTANCE = new ConversationNotificationManager$1$onNotificationRankingUpdated$1();

    ConversationNotificationManager$1$onNotificationRankingUpdated$1() {
        super(1, Intrinsics.Kotlin.class, "getLayouts", "onNotificationRankingUpdated$getLayouts(Lcom/android/systemui/statusbar/notification/row/NotificationContentView;)Lkotlin/sequences/Sequence;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Sequence<View> mo1949invoke(@NotNull NotificationContentView p0) {
        Sequence<View> onNotificationRankingUpdated$getLayouts;
        Intrinsics.checkNotNullParameter(p0, "p0");
        onNotificationRankingUpdated$getLayouts = ConversationNotificationManager.AnonymousClass1.onNotificationRankingUpdated$getLayouts(p0);
        return onNotificationRankingUpdated$getLayouts;
    }
}
