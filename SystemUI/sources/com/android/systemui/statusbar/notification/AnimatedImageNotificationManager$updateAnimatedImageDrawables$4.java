package com.android.systemui.statusbar.notification;

import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.MessagingGroup;
import com.android.systemui.util.ConvenienceExtensionsKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u00012\u000e\u0010\u0004\u001a\n \u0003*\u0004\u0018\u00010\u00050\u0005H\n¢\u0006\u0002\b\u0006"}, mo64987d2 = {"<anonymous>", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "kotlin.jvm.PlatformType", "messagingGroup", "Lcom/android/internal/widget/MessagingGroup;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 extends Lambda implements Function1<MessagingGroup, Sequence<? extends View>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$4();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$4() {
        super(1);
    }

    public final Sequence<View> invoke(MessagingGroup messagingGroup) {
        ViewGroup messageContainer = messagingGroup.getMessageContainer();
        Intrinsics.checkNotNullExpressionValue(messageContainer, "messagingGroup.messageContainer");
        return ConvenienceExtensionsKt.getChildren(messageContainer);
    }
}
