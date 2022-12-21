package com.android.systemui.statusbar.notification.collection.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0001H\u0002¨\u0006\t"}, mo64987d2 = {"treeSpecToStr", "", "tree", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "treeSpecToStrHelper", "", "sb", "Ljava/lang/StringBuilder;", "indent", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NodeController.kt */
public final class NodeControllerKt {
    public static final String treeSpecToStr(NodeSpec nodeSpec) {
        Intrinsics.checkNotNullParameter(nodeSpec, "tree");
        StringBuilder sb = new StringBuilder();
        treeSpecToStrHelper(nodeSpec, sb, "");
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().also { t…ree, it, \"\") }.toString()");
        return sb2;
    }

    private static final void treeSpecToStrHelper(NodeSpec nodeSpec, StringBuilder sb, String str) {
        sb.append(str + '{' + nodeSpec.getController().getNodeLabel() + "}\n");
        if (!nodeSpec.getChildren().isEmpty()) {
            String str2 = str + "  ";
            for (NodeSpec treeSpecToStrHelper : nodeSpec.getChildren()) {
                treeSpecToStrHelper(treeSpecToStrHelper, sb, str2);
            }
        }
    }
}
