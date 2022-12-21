package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u00012\u000e\u0010\u0004\u001a\n \u0003*\u0004\u0018\u00010\u00050\u0005H\n¢\u0006\u0002\b\u0006"}, mo64987d2 = {"<anonymous>", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "kotlin.jvm.PlatformType", "layout", "Lcom/android/systemui/statusbar/notification/row/NotificationContentView;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
final class ConversationNotificationManager$resetBadgeUi$1 extends Lambda implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$resetBadgeUi$1 INSTANCE = new ConversationNotificationManager$resetBadgeUi$1();

    ConversationNotificationManager$resetBadgeUi$1() {
        super(1);
    }

    public final Sequence<View> invoke(NotificationContentView notificationContentView) {
        View[] allViews = notificationContentView.getAllViews();
        Intrinsics.checkNotNullExpressionValue(allViews, "layout.allViews");
        return ArraysKt.asSequence((T[]) (Object[]) allViews);
    }
}
