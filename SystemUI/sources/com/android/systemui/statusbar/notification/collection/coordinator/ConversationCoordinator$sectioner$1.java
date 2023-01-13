package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0002\u0000\u0003\b\n\u0018\u00002\u00020\u0001J\r\u0010\u0002\u001a\u00020\u0003H\u0016¢\u0006\u0002\u0010\u0004J\n\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$sectioner$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "getComparator", "com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$sectioner$1$getComparator$1", "()Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$sectioner$1$getComparator$1;", "getHeaderNodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "isInSection", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$sectioner$1 extends NotifSectioner {
    final /* synthetic */ NodeController $peopleHeaderController;
    final /* synthetic */ ConversationCoordinator this$0;

    public NodeController getHeaderNodeController() {
        return null;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ConversationCoordinator$sectioner$1(ConversationCoordinator conversationCoordinator, NodeController nodeController) {
        super("People", 4);
        this.this$0 = conversationCoordinator;
        this.$peopleHeaderController = nodeController;
    }

    public boolean isInSection(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        return this.this$0.isConversation(listEntry);
    }

    public ConversationCoordinator$sectioner$1$getComparator$1 getComparator() {
        return new ConversationCoordinator$sectioner$1$getComparator$1(this.this$0);
    }
}
