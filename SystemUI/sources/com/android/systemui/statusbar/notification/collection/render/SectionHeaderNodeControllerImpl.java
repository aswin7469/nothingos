package com.android.systemui.statusbar.notification.collection.render;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SectionHeaderController.kt */
/* loaded from: classes.dex */
public final class SectionHeaderNodeControllerImpl implements NodeController, SectionHeaderController {
    @Nullable
    private SectionHeaderView _view;
    @NotNull
    private final ActivityStarter activityStarter;
    @Nullable
    private View.OnClickListener clearAllClickListener;
    @NotNull
    private final String clickIntentAction;
    private final int headerTextResId;
    @NotNull
    private final LayoutInflater layoutInflater;
    @NotNull
    private final String nodeLabel;
    @NotNull
    private final View.OnClickListener onHeaderClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl$onHeaderClickListener$1
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            ActivityStarter activityStarter;
            String str;
            activityStarter = SectionHeaderNodeControllerImpl.this.activityStarter;
            str = SectionHeaderNodeControllerImpl.this.clickIntentAction;
            activityStarter.startActivity(new Intent(str), true, true, 536870912);
        }
    };

    public SectionHeaderNodeControllerImpl(@NotNull String nodeLabel, @NotNull LayoutInflater layoutInflater, int i, @NotNull ActivityStarter activityStarter, @NotNull String clickIntentAction) {
        Intrinsics.checkNotNullParameter(nodeLabel, "nodeLabel");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(clickIntentAction, "clickIntentAction");
        this.nodeLabel = nodeLabel;
        this.layoutInflater = layoutInflater;
        this.headerTextResId = i;
        this.activityStarter = activityStarter;
        this.clickIntentAction = clickIntentAction;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void addChildAt(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.addChildAt(this, nodeController, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @Nullable
    public View getChildAt(int i) {
        return NodeController.DefaultImpls.getChildAt(this, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public int getChildCount() {
        return NodeController.DefaultImpls.getChildCount(this);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void moveChildTo(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.moveChildTo(this, nodeController, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void removeChild(@NotNull NodeController nodeController, boolean z) {
        NodeController.DefaultImpls.removeChild(this, nodeController, z);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @NotNull
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0047  */
    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void reinflateView(@NotNull ViewGroup parent) {
        int indexOfChild;
        View.OnClickListener onClickListener;
        Intrinsics.checkNotNullParameter(parent, "parent");
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            ViewGroup transientContainer = sectionHeaderView.getTransientContainer();
            if (transientContainer != null) {
                transientContainer.removeView(sectionHeaderView);
            }
            if (sectionHeaderView.getParent() == parent) {
                indexOfChild = parent.indexOfChild(sectionHeaderView);
                parent.removeView(sectionHeaderView);
                View inflate = this.layoutInflater.inflate(R$layout.status_bar_notification_section_header, parent, false);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView");
                SectionHeaderView sectionHeaderView2 = (SectionHeaderView) inflate;
                sectionHeaderView2.setHeaderText(this.headerTextResId);
                sectionHeaderView2.setOnHeaderClickListener(this.onHeaderClickListener);
                onClickListener = this.clearAllClickListener;
                if (onClickListener != null) {
                    sectionHeaderView2.setOnClearAllClickListener(onClickListener);
                }
                if (indexOfChild != -1) {
                    parent.addView(sectionHeaderView2, indexOfChild);
                }
                this._view = sectionHeaderView2;
            }
        }
        indexOfChild = -1;
        View inflate2 = this.layoutInflater.inflate(R$layout.status_bar_notification_section_header, parent, false);
        Objects.requireNonNull(inflate2, "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView");
        SectionHeaderView sectionHeaderView22 = (SectionHeaderView) inflate2;
        sectionHeaderView22.setHeaderText(this.headerTextResId);
        sectionHeaderView22.setOnHeaderClickListener(this.onHeaderClickListener);
        onClickListener = this.clearAllClickListener;
        if (onClickListener != null) {
        }
        if (indexOfChild != -1) {
        }
        this._view = sectionHeaderView22;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    @Nullable
    public SectionHeaderView getHeaderView() {
        return this._view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public void setOnClearAllClickListener(@NotNull View.OnClickListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.clearAllClickListener = listener;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView == null) {
            return;
        }
        sectionHeaderView.setOnClearAllClickListener(listener);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    @NotNull
    public View getView() {
        SectionHeaderView sectionHeaderView = this._view;
        Intrinsics.checkNotNull(sectionHeaderView);
        return sectionHeaderView;
    }
}
