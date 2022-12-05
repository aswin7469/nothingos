package com.android.systemui.statusbar.notification.collection.render;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public final class NodeControllerKt {
    @NotNull
    public static final String treeSpecToStr(@NotNull NodeSpec tree) {
        Intrinsics.checkNotNullParameter(tree, "tree");
        StringBuilder sb = new StringBuilder();
        treeSpecToStrHelper(tree, sb, "");
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().also { treeSpecToStrHelper(tree, it, \"\") }.toString()");
        return sb2;
    }

    private static final void treeSpecToStrHelper(NodeSpec nodeSpec, StringBuilder sb, String str) {
        sb.append(str + "ns{" + nodeSpec.getController().getNodeLabel());
        if (!nodeSpec.getChildren().isEmpty()) {
            String stringPlus = Intrinsics.stringPlus(str, "  ");
            for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
                treeSpecToStrHelper(nodeSpec2, sb, stringPlus);
            }
        }
    }
}
