package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u0010\u001a\u00020\u000eH\u0016J\u0018\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u0012\u001a\u00020\u000bH\u0016J\b\u0010\u0013\u001a\u00020\u000bH\u0016J\b\u0010\u0014\u001a\u00020\u000bH\u0016J\u0018\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0018À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "", "nodeLabel", "", "getNodeLabel", "()Ljava/lang/String;", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "addChildAt", "", "child", "index", "", "getChildAt", "getChildCount", "moveChildTo", "onViewAdded", "onViewMoved", "onViewRemoved", "removeChild", "isTransfer", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NodeController.kt */
public interface NodeController {
    int getChildCount() {
        return 0;
    }

    String getNodeLabel();

    View getView();

    void onViewAdded() {
    }

    void onViewMoved() {
    }

    void onViewRemoved() {
    }

    View getChildAt(int i) {
        throw new RuntimeException("Not supported");
    }

    void addChildAt(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        throw new RuntimeException("Not supported");
    }

    void moveChildTo(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        throw new RuntimeException("Not supported");
    }

    void removeChild(NodeController nodeController, boolean z) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        throw new RuntimeException("Not supported");
    }
}
