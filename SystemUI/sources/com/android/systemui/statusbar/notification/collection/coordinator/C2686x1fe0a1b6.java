package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$onBeforeFinalizeFilter$1$groupLocationsByKey$2 */
/* compiled from: HeadsUpCoordinator.kt */
final class C2686x1fe0a1b6 extends Lambda implements Function0<Map<String, ? extends GroupLocation>> {
    final /* synthetic */ List<ListEntry> $list;
    final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2686x1fe0a1b6(HeadsUpCoordinator headsUpCoordinator, List<? extends ListEntry> list) {
        super(0);
        this.this$0 = headsUpCoordinator;
        this.$list = list;
    }

    public final Map<String, GroupLocation> invoke() {
        return this.this$0.getGroupLocationsByKey(this.$list);
    }
}
