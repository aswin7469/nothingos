package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
final class ShadeNode {
    @NotNull
    private final NodeController controller;
    @Nullable
    private ShadeNode parent;
    @NotNull
    private final View view;

    public ShadeNode(@NotNull NodeController controller) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        this.controller = controller;
        this.view = controller.getView();
    }

    @NotNull
    public final NodeController getController() {
        return this.controller;
    }

    @NotNull
    public final View getView() {
        return this.view;
    }

    @Nullable
    public final ShadeNode getParent() {
        return this.parent;
    }

    public final void setParent(@Nullable ShadeNode shadeNode) {
        this.parent = shadeNode;
    }

    @NotNull
    public final String getLabel() {
        return this.controller.getNodeLabel();
    }

    @Nullable
    public final View getChildAt(int i) {
        return this.controller.getChildAt(i);
    }

    public final int getChildCount() {
        return this.controller.getChildCount();
    }

    public final void addChildAt(@NotNull ShadeNode child, int i) {
        Intrinsics.checkNotNullParameter(child, "child");
        this.controller.addChildAt(child.controller, i);
    }

    public final void moveChildTo(@NotNull ShadeNode child, int i) {
        Intrinsics.checkNotNullParameter(child, "child");
        this.controller.moveChildTo(child.controller, i);
    }

    public final void removeChild(@NotNull ShadeNode child, boolean z) {
        Intrinsics.checkNotNullParameter(child, "child");
        this.controller.removeChild(child.controller, z);
    }
}
