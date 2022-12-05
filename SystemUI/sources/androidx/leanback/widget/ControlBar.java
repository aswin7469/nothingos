package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
class ControlBar extends LinearLayout {
    private int mChildMarginFromCenter;
    private OnChildFocusedListener mOnChildFocusedListener;
    int mLastFocusIndex = -1;
    boolean mDefaultFocusToMiddle = true;

    /* loaded from: classes.dex */
    public interface OnChildFocusedListener {
        void onChildFocusedListener(View child, View focused);
    }

    public ControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int getDefaultFocusIndex() {
        if (this.mDefaultFocusToMiddle) {
            return getChildCount() / 2;
        }
        return 0;
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (getChildCount() > 0) {
            int i = this.mLastFocusIndex;
            if (getChildAt((i < 0 || i >= getChildCount()) ? getDefaultFocusIndex() : this.mLastFocusIndex).requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (direction == 33 || direction == 130) {
            int i = this.mLastFocusIndex;
            if (i >= 0 && i < getChildCount()) {
                views.add(getChildAt(this.mLastFocusIndex));
                return;
            } else if (getChildCount() <= 0) {
                return;
            } else {
                views.add(getChildAt(getDefaultFocusIndex()));
                return;
            }
        }
        super.addFocusables(views, direction, focusableMode);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        this.mLastFocusIndex = indexOfChild(child);
        OnChildFocusedListener onChildFocusedListener = this.mOnChildFocusedListener;
        if (onChildFocusedListener != null) {
            onChildFocusedListener.onChildFocusedListener(child, focused);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mChildMarginFromCenter <= 0) {
            return;
        }
        int i = 0;
        int i2 = 0;
        while (i < getChildCount() - 1) {
            View childAt = getChildAt(i);
            i++;
            View childAt2 = getChildAt(i);
            int measuredWidth = this.mChildMarginFromCenter - ((childAt.getMeasuredWidth() + childAt2.getMeasuredWidth()) / 2);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
            layoutParams.setMarginStart(measuredWidth);
            childAt2.setLayoutParams(layoutParams);
            i2 += measuredWidth - layoutParams.getMarginStart();
        }
        setMeasuredDimension(getMeasuredWidth() + i2, getMeasuredHeight());
    }
}
