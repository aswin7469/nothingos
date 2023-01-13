package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, mo65043d2 = {"requireSummary", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "getRequireSummary", "(Lcom/android/systemui/statusbar/notification/collection/GroupEntry;)Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RenderExtensions.kt */
public final class RenderExtensionsKt {
    public static final NotificationEntry getRequireSummary(GroupEntry groupEntry) {
        Intrinsics.checkNotNullParameter(groupEntry, "<this>");
        NotificationEntry summary = groupEntry.getSummary();
        if (summary != null) {
            Intrinsics.checkNotNullExpressionValue(summary, "<get-requireSummary>");
            return summary;
        }
        throw new IllegalStateException(("No Summary: " + groupEntry).toString());
    }
}
