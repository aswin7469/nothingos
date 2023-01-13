package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.internal.widget.MessagingImageMessage;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, mo65043d2 = {"<anonymous>", "Landroid/graphics/drawable/AnimatedImageDrawable;", "view", "Landroid/view/View;", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$5 extends Lambda implements Function1<View, AnimatedImageDrawable> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$5 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$5();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$5() {
        super(1);
    }

    public final AnimatedImageDrawable invoke(View view) {
        MessagingImageMessage messagingImageMessage = view instanceof MessagingImageMessage ? (MessagingImageMessage) view : null;
        if (messagingImageMessage == null) {
            return null;
        }
        Drawable drawable = messagingImageMessage.getDrawable();
        if (drawable instanceof AnimatedImageDrawable) {
            return (AnimatedImageDrawable) drawable;
        }
        return null;
    }
}
