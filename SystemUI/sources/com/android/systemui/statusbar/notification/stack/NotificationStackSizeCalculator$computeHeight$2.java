package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator$StackHeight;", "it", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationStackSizeCalculator.kt */
final class NotificationStackSizeCalculator$computeHeight$2 extends Lambda implements Function1<Integer, NotificationStackSizeCalculator.StackHeight> {
    final /* synthetic */ Sequence<NotificationStackSizeCalculator.StackHeight> $heightPerMaxNotifications;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotificationStackSizeCalculator$computeHeight$2(Sequence<NotificationStackSizeCalculator.StackHeight> sequence) {
        super(1);
        this.$heightPerMaxNotifications = sequence;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    public final NotificationStackSizeCalculator.StackHeight invoke(int i) {
        return (NotificationStackSizeCalculator.StackHeight) SequencesKt.last(this.$heightPerMaxNotifications);
    }
}
