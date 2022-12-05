package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$resetBadgeUi$1 extends Lambda implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$resetBadgeUi$1 INSTANCE = new ConversationNotificationManager$resetBadgeUi$1();

    ConversationNotificationManager$resetBadgeUi$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Sequence<View> mo1949invoke(NotificationContentView notificationContentView) {
        Sequence<View> asSequence;
        View[] allViews = notificationContentView.getAllViews();
        Intrinsics.checkNotNullExpressionValue(allViews, "layout.allViews");
        asSequence = ArraysKt___ArraysKt.asSequence(allViews);
        return asSequence;
    }
}
