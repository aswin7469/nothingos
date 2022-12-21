package com.android.systemui.statusbar.notification.stack;

import java.util.function.BiConsumer;

/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2826xbae1b0c4 implements BiConsumer {
    public final /* synthetic */ NotificationRoundnessManager f$0;

    public /* synthetic */ C2826xbae1b0c4(NotificationRoundnessManager notificationRoundnessManager) {
        this.f$0 = notificationRoundnessManager;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.setExpanded(((Float) obj).floatValue(), ((Float) obj2).floatValue());
    }
}
