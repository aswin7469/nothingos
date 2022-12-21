package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingLayout;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u00012\u000e\u0010\u0004\u001a\n \u0003*\u0004\u0018\u00010\u00050\u0005H\n¢\u0006\u0002\b\u0006"}, mo64987d2 = {"<anonymous>", "Lkotlin/sequences/Sequence;", "Lcom/android/internal/widget/MessagingGroup;", "kotlin.jvm.PlatformType", "view", "Landroid/view/View;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationNotifications.kt */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 extends Lambda implements Function1<View, Sequence<? extends MessagingGroup>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$3();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$3() {
        super(1);
    }

    public final Sequence<MessagingGroup> invoke(View view) {
        ArrayList messagingGroups;
        ArrayList messagingGroups2;
        Sequence<MessagingGroup> asSequence;
        MessagingLayout messagingLayout = null;
        ConversationLayout conversationLayout = view instanceof ConversationLayout ? (ConversationLayout) view : null;
        if (conversationLayout != null && (messagingGroups2 = conversationLayout.getMessagingGroups()) != null && (asSequence = CollectionsKt.asSequence(messagingGroups2)) != null) {
            return asSequence;
        }
        if (view instanceof MessagingLayout) {
            messagingLayout = (MessagingLayout) view;
        }
        if (messagingLayout == null || (messagingGroups = messagingLayout.getMessagingGroups()) == null) {
            return SequencesKt.emptySequence();
        }
        return CollectionsKt.asSequence(messagingGroups);
    }
}
