package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: RootNodeController.kt */
/* loaded from: classes.dex */
public final class RootNodeController implements NodeController {
    @NotNull
    private final NotificationListContainer listContainer;
    @NotNull
    private final String nodeLabel = "<root>";
    @NotNull
    private final View view;

    public RootNodeController(@NotNull NotificationListContainer listContainer, @NotNull View view) {
        Intrinsics.checkNotNullParameter(listContainer, "listContainer");
        Intrinsics.checkNotNullParameter(view, "view");
        this.listContainer = listContainer;
        this.view = view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @NotNull
    public View getView() {
        return this.view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @NotNull
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @Nullable
    public View getChildAt(int i) {
        return this.listContainer.getContainerChildAt(i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public int getChildCount() {
        return this.listContainer.getContainerChildCount();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void addChildAt(@NotNull NodeController child, int i) {
        Intrinsics.checkNotNullParameter(child, "child");
        this.listContainer.addContainerViewAt(child.getView(), i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void moveChildTo(@NotNull NodeController child, int i) {
        Intrinsics.checkNotNullParameter(child, "child");
        this.listContainer.changeViewPosition((ExpandableView) child.getView(), i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void removeChild(@NotNull NodeController child, boolean z) {
        Intrinsics.checkNotNullParameter(child, "child");
        if (z) {
            this.listContainer.setChildTransferInProgress(true);
        }
        this.listContainer.removeContainerView(child.getView());
        if (z) {
            this.listContainer.setChildTransferInProgress(false);
        }
    }
}
