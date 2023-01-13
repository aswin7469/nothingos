package com.nothing.systemui.statusbar.notification;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$startFlipOutAnimation$1$onAnimationEnd$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.statusbar.notification.NTLightweightHeadsupNotificationView$startFlipOutAnimation$1$onAnimationEnd$1 */
/* compiled from: NTLightweightHeadsupNotificationView.kt */
public final class C4233x13b23fa3 implements Runnable {
    final /* synthetic */ NTLightweightHeadsupNotificationView this$0;

    C4233x13b23fa3(NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView) {
        this.this$0 = nTLightweightHeadsupNotificationView;
    }

    public void run() {
        this.this$0.setReleased(true);
    }
}
