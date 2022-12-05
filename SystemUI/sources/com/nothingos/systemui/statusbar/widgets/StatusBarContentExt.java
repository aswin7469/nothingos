package com.nothingos.systemui.statusbar.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.phone.StatusIconContainer;
/* loaded from: classes2.dex */
public class StatusBarContentExt extends LinearLayout {
    private View mBattery;
    private View mCenterIcons;
    private View mHeadsUp;
    private View mLeft;
    private View mLeftMinContent;
    private View mRight;
    private View mSpace;

    public StatusBarContentExt(Context context) {
        this(context, null);
    }

    public StatusBarContentExt(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public StatusBarContentExt(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public StatusBarContentExt(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(R$id.status_bar_left_side_container);
        this.mLeft = findViewById;
        this.mLeftMinContent = findViewById.findViewById(R$id.status_bar_left_side_min_content);
        this.mHeadsUp = this.mLeft.findViewById(R$id.heads_up_status_bar_view);
        this.mSpace = findViewById(R$id.cutout_space_view);
        this.mCenterIcons = findViewById(R$id.centered_icon_area);
        View findViewById2 = findViewById(R$id.system_icon_area);
        this.mRight = findViewById2;
        this.mBattery = findViewById2.findViewById(R$id.battery);
        ((StatusIconContainer) findViewById(R$id.statusIcons)).setMoreIcons(true);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
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
            if (max <= i3) {
                return;
            }
            measureChildWidth(this.mCenterIcons, measuredWidth2 - (max << 1), i2);
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
        return Math.max(this.mHeadsUp.getVisibility() == 0 ? this.mHeadsUp.getMeasuredWidth() : 0, this.mLeftMinContent.getMeasuredWidth());
    }

    private int getRightSideMinWidth() {
        return this.mBattery.getMeasuredWidth();
    }

    private void measureChildWidth(View view, int i, int i2) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), LinearLayout.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), view.getLayoutParams().height));
    }

    private static void setWeight(View view, int i, int i2) {
        float f = i;
        if (((LinearLayout.LayoutParams) view.getLayoutParams()).weight != f) {
            ((LinearLayout.LayoutParams) view.getLayoutParams()).weight = f;
        }
        if (((LinearLayout.LayoutParams) view.getLayoutParams()).width != i2) {
            ((LinearLayout.LayoutParams) view.getLayoutParams()).width = i2;
        }
    }
}
