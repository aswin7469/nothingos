package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.C1893R;

public class GlobalActionsColumnLayout extends GlobalActionsLayout {
    private boolean mLastSnap;

    public GlobalActionsColumnLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        post(new GlobalActionsColumnLayout$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public boolean shouldReverseListItems() {
        int currentRotation = getCurrentRotation();
        if (currentRotation == 0) {
            return false;
        }
        if (getCurrentLayoutDirection() == 1) {
            if (currentRotation == 1) {
                return true;
            }
            return false;
        } else if (currentRotation == 3) {
            return true;
        } else {
            return false;
        }
    }

    public void onUpdateList() {
        super.onUpdateList();
        updateChildOrdering();
    }

    private void updateChildOrdering() {
        if (shouldReverseListItems()) {
            getListView().bringToFront();
        } else {
            getSeparatedView().bringToFront();
        }
    }

    /* access modifiers changed from: protected */
    public void snapToPowerButton() {
        int powerButtonOffsetDistance = getPowerButtonOffsetDistance();
        int currentRotation = getCurrentRotation();
        if (currentRotation == 1) {
            setPadding(powerButtonOffsetDistance, 0, 0, 0);
            setGravity(51);
        } else if (currentRotation != 3) {
            setPadding(0, powerButtonOffsetDistance, 0, 0);
            setGravity(53);
        } else {
            setPadding(0, 0, powerButtonOffsetDistance, 0);
            setGravity(85);
        }
    }

    /* access modifiers changed from: protected */
    public void centerAlongEdge() {
        int currentRotation = getCurrentRotation();
        if (currentRotation == 1) {
            setPadding(0, 0, 0, 0);
            setGravity(49);
        } else if (currentRotation != 3) {
            setPadding(0, 0, 0, 0);
            setGravity(21);
        } else {
            setPadding(0, 0, 0, 0);
            setGravity(81);
        }
    }

    /* access modifiers changed from: protected */
    public int getPowerButtonOffsetDistance() {
        return Math.round(getContext().getResources().getDimension(C1893R.dimen.global_actions_top_padding));
    }

    /* access modifiers changed from: protected */
    public boolean shouldSnapToPowerButton() {
        int i;
        int i2;
        int powerButtonOffsetDistance = getPowerButtonOffsetDistance();
        View wrapper = getWrapper();
        if (getCurrentRotation() == 0) {
            i2 = wrapper.getMeasuredHeight();
            i = getMeasuredHeight();
        } else {
            i2 = wrapper.getMeasuredWidth();
            i = getMeasuredWidth();
        }
        return i2 + powerButtonOffsetDistance < i;
    }

    /* access modifiers changed from: protected */
    /* renamed from: updateSnap */
    public void mo32849x93fbe8b9() {
        boolean shouldSnapToPowerButton = shouldSnapToPowerButton();
        if (shouldSnapToPowerButton != this.mLastSnap) {
            if (shouldSnapToPowerButton) {
                snapToPowerButton();
            } else {
                centerAlongEdge();
            }
        }
        this.mLastSnap = shouldSnapToPowerButton;
    }

    /* access modifiers changed from: protected */
    public float getGridItemSize() {
        return getContext().getResources().getDimension(C1893R.dimen.global_actions_grid_item_height);
    }

    /* access modifiers changed from: protected */
    public float getAnimationDistance() {
        return getGridItemSize() / 2.0f;
    }

    public float getAnimationOffsetX() {
        if (getCurrentRotation() == 0) {
            return getAnimationDistance();
        }
        return 0.0f;
    }

    public float getAnimationOffsetY() {
        int currentRotation = getCurrentRotation();
        if (currentRotation == 1) {
            return -getAnimationDistance();
        }
        if (currentRotation != 3) {
            return 0.0f;
        }
        return getAnimationDistance();
    }
}
