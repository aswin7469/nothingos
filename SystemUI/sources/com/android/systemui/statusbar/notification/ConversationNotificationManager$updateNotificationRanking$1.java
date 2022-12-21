package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
/* synthetic */ class ConversationNotificationManager$updateNotificationRanking$1 extends FunctionReferenceImpl implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$updateNotificationRanking$1 INSTANCE = new ConversationNotificationManager$updateNotificationRanking$1();

    ConversationNotificationManager$updateNotificationRanking$1() {
        super(1, Intrinsics.Kotlin.class, "getLayouts", "updateNotificationRanking$getLayouts(Lcom/android/systemui/statusbar/notification/row/NotificationContentView;)Lkotlin/sequences/Sequence;", 0);
    }

    public final Sequence<View> invoke(NotificationContentView notificationContentView) {
        Intrinsics.checkNotNullParameter(notificationContentView, "p0");
        return ConversationNotificationManager.updateNotificationRanking$getLayouts(notificationContentView);
    }
}
