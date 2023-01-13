package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J2\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\f\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/SmartReplyStateInflater;", "", "inflateSmartReplyState", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "inflateSmartReplyViewHolder", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyViewHolder;", "sysuiContext", "Landroid/content/Context;", "notifPackageContext", "existingSmartReplyState", "newSmartReplyState", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public interface SmartReplyStateInflater {
    InflatedSmartReplyState inflateSmartReplyState(NotificationEntry notificationEntry);

    InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(Context context, Context context2, NotificationEntry notificationEntry, InflatedSmartReplyState inflatedSmartReplyState, InflatedSmartReplyState inflatedSmartReplyState2);
}
