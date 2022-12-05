package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public final class NodeSpecImpl implements NodeSpec {
    @NotNull
    private final List<NodeSpec> children = new ArrayList();
    @NotNull
    private final NodeController controller;
    @Nullable
    private final NodeSpec parent;

    public NodeSpecImpl(@Nullable NodeSpec nodeSpec, @NotNull NodeController controller) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        this.parent = nodeSpec;
        this.controller = controller;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    @Nullable
    public NodeSpec getParent() {
        return this.parent;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    @NotNull
    public NodeController getController() {
        return this.controller;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    @NotNull
    public List<NodeSpec> getChildren() {
        return this.children;
    }
}
