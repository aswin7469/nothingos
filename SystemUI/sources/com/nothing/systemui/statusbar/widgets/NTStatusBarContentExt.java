package com.nothing.systemui.statusbar.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;

public class NTStatusBarContentExt extends LinearLayout {
    private View mBattery;
    private View mCenterIcons;
    private View mClock;
    private int mDotPadding;
    private int mDotWidth;
    private View mHeadsUp;
    private View mLeft;
    private View mLeftMinContent;
    private View mRight;
    private View mSpace;
    private int mStaticDotRadius;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public NTStatusBarContentExt(Context context) {
        this(context, (AttributeSet) null);
    }

    public NTStatusBarContentExt(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NTStatusBarContentExt(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NTStatusBarContentExt(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDotPadding = getResources().getDimensionPixelSize(C1894R.dimen.overflow_icon_dot_padding);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.overflow_dot_radius);
        this.mStaticDotRadius = dimensionPixelSize;
        this.mDotWidth = (dimensionPixelSize * 2) + this.mDotPadding;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(C1894R.C1898id.status_bar_left_side_container);
        this.mLeft = findViewById;
        this.mLeftMinContent = findViewById.findViewById(C1894R.C1898id.status_bar_left_side_min_content);
        this.mHeadsUp = this.mLeft.findViewById(C1894R.C1898id.heads_up_status_bar_view);
        this.mClock = this.mLeft.findViewById(C1894R.C1898id.clock);
        this.mSpace = findViewById(C1894R.C1898id.cutout_space_view);
        this.mCenterIcons = findViewById(C1894R.C1898id.centered_icon_area);
        View findViewById2 = findViewById(C1894R.C1898id.system_icon_area);
        this.mRight = findViewById2;
        this.mBattery = findViewById2.findViewById(C1894R.C1898id.battery);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mSpace.getVisibility() == 0) {
            setWeight(this.mLeft, 1, 0);
            setWeight(this.mRight, 1, 0);
            super.onMeasure(i, i2);
            return;
        }
        setWeight(this.mLeft, 0, -2);
        setWeight(this.mRight, 0, -2);
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        measureChild(this.mLeft, View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i2);
        measureChild(this.mCenterIcons, View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i2);
        measureChild(this.mRight, View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i2);
        int measuredWidth = this.mLeft.getMeasuredWidth();
        int measuredWidth2 = this.mCenterIcons.getMeasuredWidth();
        int measuredWidth3 = this.mRight.getMeasuredWidth();
        int leftSideMinWidth = getLeftSideMinWidth();
        int rightSideMinWidth = getRightSideMinWidth();
        setWeight(this.mLeft, 1, 0);
        super.onMeasure(i, i2);
        setWeight(this.mLeft, 0, -2);
        if (measuredWidth2 > 0) {
            int i3 = (size - measuredWidth2) >> 1;
            int max = Math.max(i3, Math.max(leftSideMinWidth, rightSideMinWidth));
            measureChildWidth(this.mLeft, max, i2);
            measureChildWidth(this.mRight, max, i2);
            if (max > i3) {
                measureChildWidth(this.mCenterIcons, measuredWidth2 - (max << 1), i2);
                return;
            }
            return;
        }
        int i4 = size - (measuredWidth + measuredWidth3);
        if (i4 >= 0) {
            measureChildWidth(this.mLeft, measuredWidth + i4, i2);
            return;
        }
        int i5 = size - measuredWidth3;
        if (i5 >= leftSideMinWidth) {
            measureChildWidth(this.mLeft, i5, i2);
            measureChildWidth(this.mRight, measuredWidth3, i2);
            return;
        }
        int i6 = size - leftSideMinWidth;
        if (i6 >= rightSideMinWidth) {
            measureChildWidth(this.mRight, i6, i2);
            measureChildWidth(this.mLeft, leftSideMinWidth, i2);
            return;
        }
        measureChildWidth(this.mRight, rightSideMinWidth, i2);
        measureChildWidth(this.mLeft, size - rightSideMinWidth, i2);
    }

    private int getLeftSideMinWidth() {
        int i = 0;
        int measuredWidth = this.mHeadsUp.getVisibility() == 0 ? this.mHeadsUp.getMeasuredWidth() : 0;
        if (this.mClock.getVisibility() == 0) {
            i = this.mClock.getMeasuredWidth();
        }
        return Math.max(Math.max(measuredWidth, i) + this.mDotWidth, this.mLeftMinContent.getMeasuredWidth());
    }

    private int getRightSideMinWidth() {
        return this.mBattery.getMeasuredWidth();
    }

    private void measureChildWidth(View view, int i, int i2) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), view.getLayoutParams().height));
    }

    private static void setWeight(View view, int i, int i2) {
        float f = (float) i;
        if (((LinearLayout.LayoutParams) view.getLayoutParams()).weight != f) {
            ((LinearLayout.LayoutParams) view.getLayoutParams()).weight = f;
        }
        if (((LinearLayout.LayoutParams) view.getLayoutParams()).width != i2) {
            ((LinearLayout.LayoutParams) view.getLayoutParams()).width = i2;
        }
    }
}
