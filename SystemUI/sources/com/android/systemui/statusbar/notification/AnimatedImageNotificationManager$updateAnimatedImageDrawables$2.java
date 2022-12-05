package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingLayout;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 extends Lambda implements Function1<View, Sequence<? extends MessagingGroup>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$2();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Sequence<MessagingGroup> mo1949invoke(View view) {
        ArrayList messagingGroups;
        Sequence<MessagingGroup> emptySequence;
        ArrayList messagingGroups2;
        Sequence<MessagingGroup> sequence = null;
        ConversationLayout conversationLayout = view instanceof ConversationLayout ? (ConversationLayout) view : null;
        Sequence<MessagingGroup> asSequence = (conversationLayout == null || (messagingGroups = conversationLayout.getMessagingGroups()) == null) ? null : CollectionsKt___CollectionsKt.asSequence(messagingGroups);
        if (asSequence == null) {
            MessagingLayout messagingLayout = view instanceof MessagingLayout ? (MessagingLayout) view : null;
            if (messagingLayout != null && (messagingGroups2 = messagingLayout.getMessagingGroups()) != null) {
                sequence = CollectionsKt___CollectionsKt.asSequence(messagingGroups2);
            }
            if (sequence != null) {
                return sequence;
            }
            emptySequence = SequencesKt__SequencesKt.emptySequence();
            return emptySequence;
        }
        return asSequence;
    }
}
