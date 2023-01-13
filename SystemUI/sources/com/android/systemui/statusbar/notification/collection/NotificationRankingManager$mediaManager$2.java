package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.NotificationMediaManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/NotificationMediaManager;", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationRankingManager.kt */
final class NotificationRankingManager$mediaManager$2 extends Lambda implements Function0<NotificationMediaManager> {
    final /* synthetic */ NotificationRankingManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotificationRankingManager$mediaManager$2(NotificationRankingManager notificationRankingManager) {
        super(0);
        this.this$0 = notificationRankingManager;
    }

    public final NotificationMediaManager invoke() {
        return (NotificationMediaManager) this.this$0.mediaManagerLazy.get();
    }
}
