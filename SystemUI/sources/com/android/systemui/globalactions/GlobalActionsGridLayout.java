package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class GlobalActionsGridLayout extends GlobalActionsLayout {
    public GlobalActionsGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @VisibleForTesting
    protected void setupListView() {
        ListGridLayout mo610getListView = mo610getListView();
        mo610getListView.setExpectedCount(this.mAdapter.countListItems());
        mo610getListView.setReverseSublists(shouldReverseSublists());
        mo610getListView.setReverseItems(shouldReverseListItems());
        mo610getListView.setSwapRowsAndColumns(shouldSwapRowsAndColumns());
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout, com.android.systemui.MultiListLayout
    public void onUpdateList() {
        setupListView();
        super.onUpdateList();
        updateSeparatedItemSize();
    }

    @VisibleForTesting
    protected void updateSeparatedItemSize() {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView.getChildCount() == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = separatedView.getChildAt(0).getLayoutParams();
        if (separatedView.getChildCount() == 1) {
            layoutParams.width = -1;
            layoutParams.height = -1;
            return;
        }
        layoutParams.width = -2;
        layoutParams.height = -2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsLayout, com.android.systemui.MultiListLayout
    /* renamed from: getListView  reason: collision with other method in class */
    public ListGridLayout mo610getListView() {
        return (ListGridLayout) super.mo610getListView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.MultiListLayout
    public void removeAllListViews() {
        ListGridLayout mo610getListView = mo610getListView();
        if (mo610getListView != null) {
            mo610getListView.removeAllItems();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    public void addToListView(View view, boolean z) {
        ListGridLayout mo610getListView = mo610getListView();
        if (mo610getListView != null) {
            mo610getListView.addItem(view);
        }
    }

    @Override // com.android.systemui.MultiListLayout
    public void removeAllItems() {
        ViewGroup separatedView = getSeparatedView();
        ListGridLayout mo610getListView = mo610getListView();
        if (separatedView != null) {
            separatedView.removeAllViews();
        }
        if (mo610getListView != null) {
            mo610getListView.removeAllItems();
        }
    }

    @VisibleForTesting
    protected boolean shouldReverseSublists() {
        return getCurrentRotation() == 3;
    }

    @VisibleForTesting
    protected boolean shouldSwapRowsAndColumns() {
        return getCurrentRotation() != 0;
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    protected boolean shouldReverseListItems() {
        int currentRotation = getCurrentRotation();
        boolean z = currentRotation == 0 || currentRotation == 3;
        return getCurrentLayoutDirection() == 1 ? !z : z;
    }

    @VisibleForTesting
    protected float getAnimationDistance() {
        return (mo610getListView().getRowCount() * getContext().getResources().getDimension(R$dimen.global_actions_grid_item_height)) / 2.0f;
    }

    @Override // com.android.systemui.MultiListLayout
    public float getAnimationOffsetX() {
        int currentRotation = getCurrentRotation();
        if (currentRotation != 1) {
            if (currentRotation == 3) {
                return -getAnimationDistance();
            }
            return 0.0f;
        }
        return getAnimationDistance();
    }
}
