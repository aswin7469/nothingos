package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo65043d2 = {"<anonymous>", "", "heightResult", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator$StackHeight;", "invoke", "(Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator$StackHeight;)Ljava/lang/Boolean;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$computeMaxKeyguardNotifications$maxNotifications$1 */
/* compiled from: NotificationStackSizeCalculator.kt */
final class C2841xf2125ae4 extends Lambda implements Function1<NotificationStackSizeCalculator.StackHeight, Boolean> {
    final /* synthetic */ float $spaceForNotifications;
    final /* synthetic */ float $spaceForShelf;
    final /* synthetic */ NotificationStackSizeCalculator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2841xf2125ae4(NotificationStackSizeCalculator notificationStackSizeCalculator, float f, float f2) {
        super(1);
        this.this$0 = notificationStackSizeCalculator;
        this.$spaceForNotifications = f;
        this.$spaceForShelf = f2;
    }

    public final Boolean invoke(NotificationStackSizeCalculator.StackHeight stackHeight) {
        Intrinsics.checkNotNullParameter(stackHeight, "heightResult");
        return Boolean.valueOf(this.this$0.canStackFitInSpace(stackHeight, this.$spaceForNotifications, this.$spaceForShelf));
    }
}
