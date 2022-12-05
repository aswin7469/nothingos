package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
public final class ShadeViewDiffer {
    @NotNull
    private final ShadeViewDifferLogger logger;
    @NotNull
    private final Map<NodeController, ShadeNode> nodes;
    @NotNull
    private final ShadeNode rootNode;
    @NotNull
    private final Map<View, ShadeNode> views = new LinkedHashMap();

    public ShadeViewDiffer(@NotNull NodeController rootController, @NotNull ShadeViewDifferLogger logger) {
        Map<NodeController, ShadeNode> mutableMapOf;
        Intrinsics.checkNotNullParameter(rootController, "rootController");
        Intrinsics.checkNotNullParameter(logger, "logger");
        this.logger = logger;
        ShadeNode shadeNode = new ShadeNode(rootController);
        this.rootNode = shadeNode;
        mutableMapOf = MapsKt__MapsKt.mutableMapOf(TuplesKt.to(rootController, shadeNode));
        this.nodes = mutableMapOf;
    }

    public final void applySpec(@NotNull NodeSpec spec) {
        Intrinsics.checkNotNullParameter(spec, "spec");
        Map<NodeController, NodeSpec> treeToMap = treeToMap(spec);
        if (!Intrinsics.areEqual(spec.getController(), this.rootNode.getController())) {
            throw new IllegalArgumentException("Tree root " + spec.getController().getNodeLabel() + " does not match own root at " + this.rootNode.getLabel());
        }
        detachChildren(this.rootNode, treeToMap);
        attachChildren(this.rootNode, treeToMap);
    }

    private final void detachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        NodeSpec nodeSpec = map.get(shadeNode.getController());
        int childCount = shadeNode.getChildCount() - 1;
        if (childCount >= 0) {
            while (true) {
                int i = childCount - 1;
                ShadeNode shadeNode2 = this.views.get(shadeNode.getChildAt(childCount));
                if (shadeNode2 != null) {
                    maybeDetachChild(shadeNode, nodeSpec, shadeNode2, map.get(shadeNode2.getController()));
                    if (shadeNode2.getController().getChildCount() > 0) {
                        detachChildren(shadeNode2, map);
                    }
                }
                if (i < 0) {
                    return;
                }
                childCount = i;
            }
        }
    }

    private final void maybeDetachChild(ShadeNode shadeNode, NodeSpec nodeSpec, ShadeNode shadeNode2, NodeSpec nodeSpec2) {
        NodeSpec parent = nodeSpec2 == null ? null : nodeSpec2.getParent();
        ShadeNode node = parent == null ? null : getNode(parent);
        if (!Intrinsics.areEqual(node, shadeNode)) {
            boolean z = node == null;
            if (z) {
                this.nodes.remove(shadeNode2.getController());
                this.views.remove(shadeNode2.getController().getView());
            }
            if (z && nodeSpec == null) {
                this.logger.logSkippingDetach(shadeNode2.getLabel(), shadeNode.getLabel());
                return;
            }
            this.logger.logDetachingChild(shadeNode2.getLabel(), !z, shadeNode.getLabel(), node == null ? null : node.getLabel());
            shadeNode.removeChild(shadeNode2, !z);
            shadeNode2.setParent(null);
        }
    }

    private final void attachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        NodeSpec nodeSpec = map.get(shadeNode.getController());
        if (nodeSpec == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        int i = 0;
        for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
            int i2 = i + 1;
            View childAt = shadeNode.getChildAt(i);
            ShadeNode node = getNode(nodeSpec2);
            if (!Intrinsics.areEqual(node.getView(), childAt)) {
                ShadeNode parent = node.getParent();
                if (parent == null) {
                    this.logger.logAttachingChild(node.getLabel(), shadeNode.getLabel());
                    shadeNode.addChildAt(node, i);
                    node.setParent(shadeNode);
                } else if (Intrinsics.areEqual(parent, shadeNode)) {
                    this.logger.logMovingChild(node.getLabel(), shadeNode.getLabel(), i);
                    shadeNode.moveChildTo(node, i);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Child ");
                    sb.append(node.getLabel());
                    sb.append(" should have parent ");
                    sb.append(shadeNode.getLabel());
                    sb.append(" but is actually ");
                    ShadeNode parent2 = node.getParent();
                    sb.append((Object) (parent2 == null ? null : parent2.getLabel()));
                    throw new IllegalStateException(sb.toString());
                }
            }
            if (!nodeSpec2.getChildren().isEmpty()) {
                attachChildren(node, map);
            }
            i = i2;
        }
    }

    private final ShadeNode getNode(NodeSpec nodeSpec) {
        ShadeNode shadeNode = this.nodes.get(nodeSpec.getController());
        if (shadeNode == null) {
            ShadeNode shadeNode2 = new ShadeNode(nodeSpec.getController());
            this.nodes.put(shadeNode2.getController(), shadeNode2);
            this.views.put(shadeNode2.getView(), shadeNode2);
            return shadeNode2;
        }
        return shadeNode;
    }

    private final Map<NodeController, NodeSpec> treeToMap(NodeSpec nodeSpec) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            registerNodes(nodeSpec, linkedHashMap);
            return linkedHashMap;
        } catch (DuplicateNodeException e) {
            this.logger.logDuplicateNodeInTree(nodeSpec, e);
            throw e;
        }
    }

    private final void registerNodes(NodeSpec nodeSpec, Map<NodeController, NodeSpec> map) {
        if (map.containsKey(nodeSpec.getController())) {
            throw new DuplicateNodeException("Node " + nodeSpec.getController().getNodeLabel() + " appears more than once");
        }
        map.put(nodeSpec.getController(), nodeSpec);
        if (!(!nodeSpec.getChildren().isEmpty())) {
            return;
        }
        for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
            registerNodes(nodeSpec2, map);
        }
    }
}
