package com.android.systemui.statusbar.notification.collection.render;

import java.util.List;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00000\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "", "children", "", "getChildren", "()Ljava/util/List;", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "getController", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "parent", "getParent", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NodeController.kt */
public interface NodeSpec {
    List<NodeSpec> getChildren();

    NodeController getController();

    NodeSpec getParent();
}
