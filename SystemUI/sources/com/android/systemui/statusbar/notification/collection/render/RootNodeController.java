package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0013\u001a\u00020\u0011H\u0016J\u0018\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\bXD¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0018"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/RootNodeController;", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "listContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "view", "Landroid/view/View;", "(Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;Landroid/view/View;)V", "nodeLabel", "", "getNodeLabel", "()Ljava/lang/String;", "getView", "()Landroid/view/View;", "addChildAt", "", "child", "index", "", "getChildAt", "getChildCount", "moveChildTo", "removeChild", "isTransfer", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RootNodeController.kt */
public final class RootNodeController implements NodeController {
    private final NotificationListContainer listContainer;
    private final String nodeLabel = "<root>";
    private final View view;

    public RootNodeController(NotificationListContainer notificationListContainer, View view2) {
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(view2, "view");
        this.listContainer = notificationListContainer;
        this.view = view2;
    }

    public View getView() {
        return this.view;
    }

    public String getNodeLabel() {
        return this.nodeLabel;
    }

    public View getChildAt(int i) {
        return this.listContainer.getContainerChildAt(i);
    }

    public int getChildCount() {
        return this.listContainer.getContainerChildCount();
    }

    public void addChildAt(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        this.listContainer.addContainerViewAt(nodeController.getView(), i);
        this.listContainer.onNotificationViewUpdateFinished();
        View view2 = nodeController.getView();
        ExpandableNotificationRow expandableNotificationRow = view2 instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view2 : null;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setChangingPosition(false);
        }
    }

    public void moveChildTo(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        this.listContainer.changeViewPosition((ExpandableView) nodeController.getView(), i);
    }

    public void removeChild(NodeController nodeController, boolean z) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        if (z) {
            this.listContainer.setChildTransferInProgress(true);
            View view2 = nodeController.getView();
            ExpandableNotificationRow expandableNotificationRow = view2 instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view2 : null;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.setChangingPosition(true);
            }
        }
        this.listContainer.removeContainerView(nodeController.getView());
        if (z) {
            this.listContainer.setChildTransferInProgress(false);
        }
    }
}
