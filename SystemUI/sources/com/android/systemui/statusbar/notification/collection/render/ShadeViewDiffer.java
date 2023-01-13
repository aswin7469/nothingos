package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import android.view.View;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ$\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\t2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\u0012H\u0002J$\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\t2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\u0012H\u0002J\u0010\u0010\u0014\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J,\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\t2\b\u0010\u001a\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u001b\u001a\u00020\t2\b\u0010\u001c\u001a\u0004\u0018\u00010\u000eH\u0002J$\u0010\u001d\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\u000e2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\bH\u0002J\u001c\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\u00122\u0006\u0010!\u001a\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDiffer;", "", "rootController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "logger", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDifferLogger;", "(Lcom/android/systemui/statusbar/notification/collection/render/NodeController;Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDifferLogger;)V", "nodes", "", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeNode;", "rootNode", "applySpec", "", "spec", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "attachChildren", "parentNode", "specMap", "", "detachChildren", "getNode", "getViewLabel", "", "view", "Landroid/view/View;", "maybeDetachChild", "parentSpec", "childNode", "childSpec", "registerNodes", "node", "map", "treeToMap", "tree", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeViewDiffer.kt */
public final class ShadeViewDiffer {
    private final ShadeViewDifferLogger logger;
    private final Map<NodeController, ShadeNode> nodes;
    private final ShadeNode rootNode;

    public ShadeViewDiffer(NodeController nodeController, ShadeViewDifferLogger shadeViewDifferLogger) {
        Intrinsics.checkNotNullParameter(nodeController, "rootController");
        Intrinsics.checkNotNullParameter(shadeViewDifferLogger, "logger");
        this.logger = shadeViewDifferLogger;
        ShadeNode shadeNode = new ShadeNode(nodeController);
        this.rootNode = shadeNode;
        this.nodes = MapsKt.mutableMapOf(TuplesKt.m1802to(nodeController, shadeNode));
    }

    public final String getViewLabel(View view) {
        Object obj;
        String label;
        boolean z;
        Intrinsics.checkNotNullParameter(view, "view");
        Iterator it = this.nodes.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((ShadeNode) obj).getView() == view) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                break;
            }
        }
        ShadeNode shadeNode = (ShadeNode) obj;
        if (shadeNode != null && (label = shadeNode.getLabel()) != null) {
            return label;
        }
        String view2 = view.toString();
        Intrinsics.checkNotNullExpressionValue(view2, "view.toString()");
        return view2;
    }

    private final void detachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        Iterable values = this.nodes.values();
        Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(values, 10)), 16));
        for (Object next : values) {
            linkedHashMap.put(((ShadeNode) next).getView(), next);
        }
        detachChildren$detachRecursively(linkedHashMap, this, shadeNode, map);
    }

    private static final void detachChildren$detachRecursively(Map<View, ShadeNode> map, ShadeViewDiffer shadeViewDiffer, ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map2) {
        NodeSpec nodeSpec = (NodeSpec) map2.get(shadeNode.getController());
        int childCount = shadeNode.getChildCount();
        while (true) {
            childCount--;
            if (-1 < childCount) {
                ShadeNode shadeNode2 = map.get(shadeNode.getChildAt(childCount));
                if (shadeNode2 != null) {
                    shadeViewDiffer.maybeDetachChild(shadeNode, nodeSpec, shadeNode2, (NodeSpec) map2.get(shadeNode2.getController()));
                    if (shadeNode2.getController().getChildCount() > 0) {
                        detachChildren$detachRecursively(map, shadeViewDiffer, shadeNode2, map2);
                    }
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r4 = r20.getParent();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void maybeDetachChild(com.android.systemui.statusbar.notification.collection.render.ShadeNode r17, com.android.systemui.statusbar.notification.collection.render.NodeSpec r18, com.android.systemui.statusbar.notification.collection.render.ShadeNode r19, com.android.systemui.statusbar.notification.collection.render.NodeSpec r20) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r19
            r3 = 0
            if (r20 == 0) goto L_0x0014
            com.android.systemui.statusbar.notification.collection.render.NodeSpec r4 = r20.getParent()
            if (r4 == 0) goto L_0x0014
            com.android.systemui.statusbar.notification.collection.render.ShadeNode r4 = r0.getNode(r4)
            goto L_0x0015
        L_0x0014:
            r4 = r3
        L_0x0015:
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r1)
            if (r5 != 0) goto L_0x0052
            r5 = 0
            r6 = 1
            if (r4 != 0) goto L_0x0021
            r7 = r6
            goto L_0x0022
        L_0x0021:
            r7 = r5
        L_0x0022:
            if (r7 == 0) goto L_0x002d
            java.util.Map<com.android.systemui.statusbar.notification.collection.render.NodeController, com.android.systemui.statusbar.notification.collection.render.ShadeNode> r8 = r0.nodes
            com.android.systemui.statusbar.notification.collection.render.NodeController r9 = r19.getController()
            r8.remove(r9)
        L_0x002d:
            com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger r10 = r0.logger
            java.lang.String r11 = r19.getLabel()
            r12 = r7 ^ 1
            if (r18 != 0) goto L_0x0039
            r13 = r6
            goto L_0x003a
        L_0x0039:
            r13 = r5
        L_0x003a:
            java.lang.String r14 = r17.getLabel()
            if (r4 == 0) goto L_0x0046
            java.lang.String r0 = r4.getLabel()
            r15 = r0
            goto L_0x0047
        L_0x0046:
            r15 = r3
        L_0x0047:
            r10.logDetachingChild(r11, r12, r13, r14, r15)
            r0 = r7 ^ 1
            r1.removeChild(r2, r0)
            r2.setParent(r3)
        L_0x0052:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.ShadeViewDiffer.maybeDetachChild(com.android.systemui.statusbar.notification.collection.render.ShadeNode, com.android.systemui.statusbar.notification.collection.render.NodeSpec, com.android.systemui.statusbar.notification.collection.render.ShadeNode, com.android.systemui.statusbar.notification.collection.render.NodeSpec):void");
    }

    private final void attachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        Object obj = map.get(shadeNode.getController());
        if (obj != null) {
            int i = 0;
            for (NodeSpec next : ((NodeSpec) obj).getChildren()) {
                int i2 = i + 1;
                View childAt = shadeNode.getChildAt(i);
                ShadeNode node = getNode(next);
                if (!Intrinsics.areEqual((Object) node.getView(), (Object) childAt)) {
                    ShadeNode parent = node.getParent();
                    if (parent == null) {
                        this.logger.logAttachingChild(node.getLabel(), shadeNode.getLabel());
                        shadeNode.addChildAt(node, i);
                        node.setParent(shadeNode);
                    } else if (Intrinsics.areEqual((Object) parent, (Object) shadeNode)) {
                        this.logger.logMovingChild(node.getLabel(), shadeNode.getLabel(), i);
                        shadeNode.moveChildTo(node, i);
                    } else {
                        StringBuilder append = new StringBuilder("Child ").append(node.getLabel()).append(" should have parent ").append(shadeNode.getLabel()).append(" but is actually ");
                        ShadeNode parent2 = node.getParent();
                        throw new IllegalStateException(append.append(parent2 != null ? parent2.getLabel() : null).toString());
                    }
                }
                if (!next.getChildren().isEmpty()) {
                    attachChildren(node, map);
                }
                i = i2;
            }
            return;
        }
        throw new IllegalStateException("Required value was null.".toString());
    }

    private final ShadeNode getNode(NodeSpec nodeSpec) {
        ShadeNode shadeNode = this.nodes.get(nodeSpec.getController());
        if (shadeNode != null) {
            return shadeNode;
        }
        ShadeNode shadeNode2 = new ShadeNode(nodeSpec.getController());
        this.nodes.put(shadeNode2.getController(), shadeNode2);
        return shadeNode2;
    }

    private final Map<NodeController, NodeSpec> treeToMap(NodeSpec nodeSpec) {
        Map<NodeController, NodeSpec> linkedHashMap = new LinkedHashMap<>();
        try {
            registerNodes(nodeSpec, linkedHashMap);
            return linkedHashMap;
        } catch (DuplicateNodeException e) {
            this.logger.logDuplicateNodeInTree(nodeSpec, e);
            throw e;
        }
    }

    private final void registerNodes(NodeSpec nodeSpec, Map<NodeController, NodeSpec> map) {
        if (!map.containsKey(nodeSpec.getController())) {
            map.put(nodeSpec.getController(), nodeSpec);
            if (!nodeSpec.getChildren().isEmpty()) {
                for (NodeSpec registerNodes : nodeSpec.getChildren()) {
                    registerNodes(registerNodes, map);
                }
                return;
            }
            return;
        }
        throw new DuplicateNodeException("Node " + nodeSpec.getController().getNodeLabel() + " appears more than once");
    }

    public final void applySpec(NodeSpec nodeSpec) {
        Intrinsics.checkNotNullParameter(nodeSpec, "spec");
        Trace.beginSection("ShadeViewDiffer.applySpec");
        try {
            Map<NodeController, NodeSpec> treeToMap = treeToMap(nodeSpec);
            if (Intrinsics.areEqual((Object) nodeSpec.getController(), (Object) this.rootNode.getController())) {
                detachChildren(this.rootNode, treeToMap);
                attachChildren(this.rootNode, treeToMap);
                Unit unit = Unit.INSTANCE;
                return;
            }
            throw new IllegalArgumentException("Tree root " + nodeSpec.getController().getNodeLabel() + " does not match own root at " + this.rootNode.getLabel());
        } finally {
            Trace.endSection();
        }
    }
}
