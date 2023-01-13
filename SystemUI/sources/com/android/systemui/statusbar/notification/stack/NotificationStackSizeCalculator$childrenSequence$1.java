package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationStackSizeCalculator.kt */
final class NotificationStackSizeCalculator$childrenSequence$1 extends Lambda implements Function1<View, ExpandableView> {
    public static final NotificationStackSizeCalculator$childrenSequence$1 INSTANCE = new NotificationStackSizeCalculator$childrenSequence$1();

    NotificationStackSizeCalculator$childrenSequence$1() {
        super(1);
    }

    public final ExpandableView invoke(View view) {
        if (view != null) {
            return (ExpandableView) view;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.ExpandableView");
    }
}
