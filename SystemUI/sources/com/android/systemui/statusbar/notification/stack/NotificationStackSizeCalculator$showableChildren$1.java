package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "invoke", "(Lcom/android/systemui/statusbar/notification/row/ExpandableView;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationStackSizeCalculator.kt */
final class NotificationStackSizeCalculator$showableChildren$1 extends Lambda implements Function1<ExpandableView, Boolean> {
    final /* synthetic */ NotificationStackSizeCalculator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotificationStackSizeCalculator$showableChildren$1(NotificationStackSizeCalculator notificationStackSizeCalculator) {
        super(1);
        this.this$0 = notificationStackSizeCalculator;
    }

    public final Boolean invoke(ExpandableView expandableView) {
        Intrinsics.checkNotNullParameter(expandableView, "it");
        NotificationStackSizeCalculator notificationStackSizeCalculator = this.this$0;
        return Boolean.valueOf(notificationStackSizeCalculator.isShowable(expandableView, notificationStackSizeCalculator.onLockscreen()));
    }
}
