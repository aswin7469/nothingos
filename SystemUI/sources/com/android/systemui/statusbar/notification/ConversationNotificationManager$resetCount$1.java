package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import java.util.function.BiFunction;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$resetCount$1 implements BiFunction<String, ConversationNotificationManager.ConversationState, ConversationNotificationManager.ConversationState> {
    public static final ConversationNotificationManager$resetCount$1 INSTANCE = new ConversationNotificationManager$resetCount$1();

    ConversationNotificationManager$resetCount$1() {
    }

    @Override // java.util.function.BiFunction
    @Nullable
    public final ConversationNotificationManager.ConversationState apply(@NotNull String noName_0, @Nullable ConversationNotificationManager.ConversationState conversationState) {
        Intrinsics.checkNotNullParameter(noName_0, "$noName_0");
        if (conversationState == null) {
            return null;
        }
        return ConversationNotificationManager.ConversationState.copy$default(conversationState, 0, null, 2, null);
    }
}
