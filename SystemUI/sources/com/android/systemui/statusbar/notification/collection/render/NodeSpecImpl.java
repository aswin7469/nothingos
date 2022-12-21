package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005R\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecImpl;", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "parent", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "(Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;Lcom/android/systemui/statusbar/notification/collection/render/NodeController;)V", "children", "", "getChildren", "()Ljava/util/List;", "getController", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "getParent", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NodeController.kt */
public final class NodeSpecImpl implements NodeSpec {
    private final List<NodeSpec> children = new ArrayList();
    private final NodeController controller;
    private final NodeSpec parent;

    public NodeSpecImpl(NodeSpec nodeSpec, NodeController nodeController) {
        Intrinsics.checkNotNullParameter(nodeController, "controller");
        this.parent = nodeSpec;
        this.controller = nodeController;
    }

    public NodeSpec getParent() {
        return this.parent;
    }

    public NodeController getController() {
        return this.controller;
    }

    public List<NodeSpec> getChildren() {
        return this.children;
    }
}
