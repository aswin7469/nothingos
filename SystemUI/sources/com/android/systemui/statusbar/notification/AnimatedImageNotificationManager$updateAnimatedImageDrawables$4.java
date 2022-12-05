package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.internal.widget.MessagingImageMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 extends Lambda implements Function1<View, AnimatedImageDrawable> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$4();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final AnimatedImageDrawable mo1949invoke(View view) {
        MessagingImageMessage messagingImageMessage = view instanceof MessagingImageMessage ? (MessagingImageMessage) view : null;
        if (messagingImageMessage == null) {
            return null;
        }
        Drawable drawable = messagingImageMessage.getDrawable();
        if (!(drawable instanceof AnimatedImageDrawable)) {
            return null;
        }
        return (AnimatedImageDrawable) drawable;
    }
}
