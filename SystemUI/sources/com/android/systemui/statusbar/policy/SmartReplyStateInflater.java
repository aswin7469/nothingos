package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public interface SmartReplyStateInflater {
    @NotNull
    InflatedSmartReplyState inflateSmartReplyState(@NotNull NotificationEntry notificationEntry);

    @NotNull
    InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(@NotNull Context context, @NotNull Context context2, @NotNull NotificationEntry notificationEntry, @Nullable InflatedSmartReplyState inflatedSmartReplyState, @NotNull InflatedSmartReplyState inflatedSmartReplyState2);
}
