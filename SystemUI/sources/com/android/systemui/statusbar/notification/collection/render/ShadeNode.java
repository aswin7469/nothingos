package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u001a\u001a\u00020\u0018J\u0016\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u001eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0000X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00118F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/ShadeNode;", "", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "(Lcom/android/systemui/statusbar/notification/collection/render/NodeController;)V", "getController", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "label", "", "getLabel", "()Ljava/lang/String;", "parent", "getParent", "()Lcom/android/systemui/statusbar/notification/collection/render/ShadeNode;", "setParent", "(Lcom/android/systemui/statusbar/notification/collection/render/ShadeNode;)V", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "addChildAt", "", "child", "index", "", "getChildAt", "getChildCount", "moveChildTo", "removeChild", "isTransfer", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeViewDiffer.kt */
final class ShadeNode {
    private final NodeController controller;
    private ShadeNode parent;

    public ShadeNode(NodeController nodeController) {
        Intrinsics.checkNotNullParameter(nodeController, "controller");
        this.controller = nodeController;
    }

    public final NodeController getController() {
        return this.controller;
    }

    public final View getView() {
        return this.controller.getView();
    }

    public final ShadeNode getParent() {
        return this.parent;
    }

    public final void setParent(ShadeNode shadeNode) {
        this.parent = shadeNode;
    }

    public final String getLabel() {
        return this.controller.getNodeLabel();
    }

    public final View getChildAt(int i) {
        return this.controller.getChildAt(i);
    }

    public final int getChildCount() {
        return this.controller.getChildCount();
    }

    public final void addChildAt(ShadeNode shadeNode, int i) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.addChildAt(shadeNode.controller, i);
        shadeNode.controller.onViewAdded();
    }

    public final void moveChildTo(ShadeNode shadeNode, int i) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.moveChildTo(shadeNode.controller, i);
        shadeNode.controller.onViewMoved();
    }

    public final void removeChild(ShadeNode shadeNode, boolean z) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.removeChild(shadeNode.controller, z);
        shadeNode.controller.onViewRemoved();
    }
}
