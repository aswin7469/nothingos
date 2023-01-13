package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import kotlin.Function;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
/* synthetic */ class AnimatedImageNotificationManager$bind$3 implements BindEventManager.Listener, FunctionAdapter {
    final /* synthetic */ AnimatedImageNotificationManager $tmp0;

    AnimatedImageNotificationManager$bind$3(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.$tmp0 = animatedImageNotificationManager;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof BindEventManager.Listener) || !(obj instanceof FunctionAdapter)) {
            return false;
        }
        return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
    }

    public final Function<?> getFunctionDelegate() {
        return new AdaptedFunctionReference(1, this.$tmp0, AnimatedImageNotificationManager.class, "updateAnimatedImageDrawables", "updateAnimatedImageDrawables(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Lkotlin/Unit;", 8);
    }

    public final int hashCode() {
        return getFunctionDelegate().hashCode();
    }

    public final void onViewBound(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "p0");
        Unit unused = this.$tmp0.updateAnimatedImageDrawables(notificationEntry);
    }
}
