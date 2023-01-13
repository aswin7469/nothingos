package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$sectioner$1$getComparator$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "compare", "", "entry1", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "entry2", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$sectioner$1$getComparator$1 extends NotifComparator {
    final /* synthetic */ ConversationCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ConversationCoordinator$sectioner$1$getComparator$1(ConversationCoordinator conversationCoordinator) {
        super("People");
        this.this$0 = conversationCoordinator;
    }

    public int compare(ListEntry listEntry, ListEntry listEntry2) {
        Intrinsics.checkNotNullParameter(listEntry, "entry1");
        Intrinsics.checkNotNullParameter(listEntry2, "entry2");
        return Intrinsics.compare(this.this$0.getPeopleType(listEntry2), this.this$0.getPeopleType(listEntry));
    }
}
