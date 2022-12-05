package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import androidx.appcompat.R$styleable;
import com.android.internal.R;
/* loaded from: classes.dex */
public class MatchParentShrinkingLinearLayout extends ViewGroup {
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mBaselineAligned;
    @ViewDebug.ExportedProperty(category = "layout")
    private int mBaselineAlignedChildIndex;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {@ViewDebug.FlagToString(equals = -1, mask = -1, name = "NONE"), @ViewDebug.FlagToString(equals = 0, mask = 0, name = "NONE"), @ViewDebug.FlagToString(equals = 48, mask = 48, name = "TOP"), @ViewDebug.FlagToString(equals = 80, mask = 80, name = "BOTTOM"), @ViewDebug.FlagToString(equals = 3, mask = 3, name = "LEFT"), @ViewDebug.FlagToString(equals = 5, mask = 5, name = "RIGHT"), @ViewDebug.FlagToString(equals = 8388611, mask = 8388611, name = "START"), @ViewDebug.FlagToString(equals = 8388613, mask = 8388613, name = "END"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "CENTER_VERTICAL"), @ViewDebug.FlagToString(equals = 112, mask = 112, name = "FILL_VERTICAL"), @ViewDebug.FlagToString(equals = 1, mask = 1, name = "CENTER_HORIZONTAL"), @ViewDebug.FlagToString(equals = 7, mask = 7, name = "FILL_HORIZONTAL"), @ViewDebug.FlagToString(equals = 17, mask = 17, name = "CENTER"), @ViewDebug.FlagToString(equals = R$styleable.AppCompatTheme_windowActionModeOverlay, mask = R$styleable.AppCompatTheme_windowActionModeOverlay, name = "FILL"), @ViewDebug.FlagToString(equals = 8388608, mask = 8388608, name = "RELATIVE")}, formatToHexString = true)
    private int mGravity;
    private int mLayoutDirection;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mOrientation;
    private int mShowDividers;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mTotalLength;
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mUseLargestChild;
    @ViewDebug.ExportedProperty(category = "layout")
    private float mWeightSum;

    int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    int measureNullChild(int i) {
        return 0;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public MatchParentShrinkingLinearLayout(Context context) {
        this(context, null);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public MatchParentShrinkingLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        this.mLayoutDirection = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayout, i, i2);
        int i3 = obtainStyledAttributes.getInt(1, -1);
        if (i3 >= 0) {
            setOrientation(i3);
        }
        int i4 = obtainStyledAttributes.getInt(0, -1);
        if (i4 >= 0) {
            setGravity(i4);
        }
        boolean z = obtainStyledAttributes.getBoolean(2, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(4, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(3, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(6, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(5));
        this.mShowDividers = obtainStyledAttributes.getInt(7, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        boolean z = false;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (drawable == null) {
            z = true;
        }
        setWillNotDraw(z);
        requestLayout();
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int bottom;
        int virtualChildCount = getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((ViewGroup.MarginLayoutParams) ((LayoutParams) virtualChildAt.getLayoutParams())).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = virtualChildAt2.getBottom() + ((ViewGroup.MarginLayoutParams) ((LayoutParams) virtualChildAt2.getLayoutParams())).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int i;
        int left2;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = isLayoutRtl();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i2)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    left2 = virtualChildAt.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                } else {
                    left2 = (virtualChildAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, left2);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    left = virtualChildAt2.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin;
                    i = this.mDividerWidth;
                    right = left - i;
                } else {
                    right = virtualChildAt2.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                }
            } else if (isLayoutRtl) {
                right = getPaddingLeft();
            } else {
                left = getWidth() - getPaddingRight();
                i = this.mDividerWidth;
                right = left - i;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    @RemotableViewMethod
    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    @RemotableViewMethod
    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    @Override // android.view.View
    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i2 = this.mBaselineAlignedChildIndex;
        if (childCount <= i2) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(i2);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.mBaselineAlignedChildIndex != 0) {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
            return -1;
        }
        int i3 = this.mBaselineChildTop;
        if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
            if (i == 16) {
                i3 += ((((((ViewGroup) this).mBottom - ((ViewGroup) this).mTop) - ((ViewGroup) this).mPaddingTop) - ((ViewGroup) this).mPaddingBottom) - this.mTotalLength) / 2;
            } else if (i == 80) {
                i3 = ((((ViewGroup) this).mBottom - ((ViewGroup) this).mTop) - ((ViewGroup) this).mPaddingBottom) - this.mTotalLength;
            }
        }
        return i3 + ((ViewGroup.MarginLayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    @RemotableViewMethod
    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    @RemotableViewMethod
    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    protected boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        } else if (i == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            for (int i2 = i - 1; i2 >= 0; i2--) {
                if (getChildAt(i2).getVisibility() != 8) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:159:0x0364  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        float f;
        int i9;
        int i10;
        boolean z;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        LayoutParams layoutParams;
        View view;
        boolean z2;
        int i19;
        int i20 = 0;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i21 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        boolean z4 = false;
        boolean z5 = false;
        float f2 = 0.0f;
        boolean z6 = true;
        int i26 = Integer.MIN_VALUE;
        while (true) {
            int i27 = 8;
            int i28 = i24;
            if (i25 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i25);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i25);
                    i14 = virtualChildCount;
                    i15 = mode2;
                    i24 = i28;
                } else {
                    int i29 = i22;
                    if (virtualChildAt.getVisibility() == 8) {
                        i25 += getChildrenSkipCount(virtualChildAt, i25);
                        i14 = virtualChildCount;
                        i24 = i28;
                        i22 = i29;
                        i15 = mode2;
                    } else {
                        if (hasDividerBeforeChildAt(i25)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams2 = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f3 = layoutParams2.weight;
                        float f4 = f2 + f3;
                        if (mode2 == 1073741824 && ((ViewGroup.MarginLayoutParams) layoutParams2).height == 0 && f3 > 0.0f) {
                            int i30 = this.mTotalLength;
                            this.mTotalLength = Math.max(i30, ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + i30 + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin);
                            i17 = i23;
                            view = virtualChildAt;
                            i14 = virtualChildCount;
                            z4 = true;
                            i18 = i28;
                            i13 = i29;
                            i26 = i26;
                            i16 = i25;
                            i15 = mode2;
                            layoutParams = layoutParams2;
                        } else {
                            int i31 = i26;
                            if (((ViewGroup.MarginLayoutParams) layoutParams2).height != 0 || f3 <= 0.0f) {
                                i12 = Integer.MIN_VALUE;
                            } else {
                                ((ViewGroup.MarginLayoutParams) layoutParams2).height = -2;
                                i12 = 0;
                            }
                            i13 = i29;
                            int i32 = i12;
                            i14 = virtualChildCount;
                            i15 = mode2;
                            i16 = i25;
                            i17 = i23;
                            i18 = i28;
                            layoutParams = layoutParams2;
                            measureChildBeforeLayout(virtualChildAt, i25, i, 0, i2, f4 == 0.0f ? this.mTotalLength : 0);
                            if (i32 != Integer.MIN_VALUE) {
                                ((ViewGroup.MarginLayoutParams) layoutParams).height = i32;
                            }
                            int measuredHeight = virtualChildAt.getMeasuredHeight();
                            int i33 = this.mTotalLength;
                            view = virtualChildAt;
                            this.mTotalLength = Math.max(i33, i33 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(view));
                            i26 = z3 ? Math.max(measuredHeight, i31) : i31;
                        }
                        if (i21 >= 0 && i21 == i16 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i16 < i21 && layoutParams.weight > 0.0f) {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                        if (mode == 1073741824 || ((ViewGroup.MarginLayoutParams) layoutParams).width != -1) {
                            z2 = false;
                        } else {
                            z2 = true;
                            z5 = true;
                        }
                        int i34 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                        int measuredWidth = view.getMeasuredWidth() + i34;
                        int max = Math.max(i13, measuredWidth);
                        int combineMeasuredStates = ViewGroup.combineMeasuredStates(i20, view.getMeasuredState());
                        boolean z7 = z6 && ((ViewGroup.MarginLayoutParams) layoutParams).width == -1;
                        if (layoutParams.weight > 0.0f) {
                            if (!z2) {
                                i34 = measuredWidth;
                            }
                            i19 = Math.max(i17, i34);
                            i24 = i18;
                        } else {
                            i19 = i17;
                            if (!z2) {
                                i34 = measuredWidth;
                            }
                            i24 = Math.max(i18, i34);
                        }
                        i22 = max;
                        i20 = combineMeasuredStates;
                        z6 = z7;
                        i23 = i19;
                        i25 = getChildrenSkipCount(view, i16) + i16;
                        f2 = f4;
                    }
                }
                i25++;
                virtualChildCount = i14;
                mode2 = i15;
            } else {
                int i35 = i22;
                int i36 = i26;
                int i37 = virtualChildCount;
                int i38 = mode2;
                int i39 = i28;
                int i40 = i23;
                if (this.mTotalLength > 0) {
                    i3 = i37;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i37;
                }
                if (z3 && (i38 == Integer.MIN_VALUE || i38 == 0)) {
                    this.mTotalLength = 0;
                    int i41 = 0;
                    while (i41 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i41);
                        if (virtualChildAt2 == null) {
                            this.mTotalLength += measureNullChild(i41);
                        } else if (virtualChildAt2.getVisibility() == i27) {
                            i41 += getChildrenSkipCount(virtualChildAt2, i41);
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i42 = this.mTotalLength;
                            this.mTotalLength = Math.max(i42, i42 + i36 + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        i41++;
                        i27 = 8;
                    }
                }
                int i43 = this.mTotalLength + ((ViewGroup) this).mPaddingTop + ((ViewGroup) this).mPaddingBottom;
                this.mTotalLength = i43;
                int resolveSizeAndState = ViewGroup.resolveSizeAndState(Math.max(i43, getSuggestedMinimumHeight()), i2, 0);
                int i44 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z4 || (i44 != 0 && f2 > 0.0f)) {
                    float f5 = this.mWeightSum;
                    if (f5 > 0.0f) {
                        f2 = f5;
                    }
                    this.mTotalLength = 0;
                    int i45 = 0;
                    while (i45 < i3) {
                        View virtualChildAt3 = getVirtualChildAt(i45);
                        if (virtualChildAt3.getVisibility() == 8) {
                            i8 = i3;
                        } else {
                            LayoutParams layoutParams4 = (LayoutParams) virtualChildAt3.getLayoutParams();
                            float f6 = layoutParams4.weight;
                            if (f6 > 0.0f && i44 > 0) {
                                int i46 = (int) ((i44 * f6) / f2);
                                i44 -= i46;
                                f = f2 - f6;
                                i8 = i3;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, ((ViewGroup) this).mPaddingLeft + ((ViewGroup) this).mPaddingRight + ((ViewGroup.MarginLayoutParams) layoutParams4).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams4).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams4).width);
                                if (((ViewGroup.MarginLayoutParams) layoutParams4).height == 0) {
                                    i11 = 1073741824;
                                    if (i38 == 1073741824) {
                                        if (i46 <= 0) {
                                            i46 = 0;
                                        }
                                        virtualChildAt3.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i46, 1073741824));
                                        i20 = ViewGroup.combineMeasuredStates(i20, virtualChildAt3.getMeasuredState() & (-256));
                                    }
                                } else {
                                    i11 = 1073741824;
                                }
                                int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i46;
                                if (measuredHeight2 < 0) {
                                    measuredHeight2 = 0;
                                }
                                virtualChildAt3.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight2, i11));
                                i20 = ViewGroup.combineMeasuredStates(i20, virtualChildAt3.getMeasuredState() & (-256));
                            } else {
                                i8 = i3;
                                if (i44 >= 0 || ((ViewGroup.MarginLayoutParams) layoutParams4).height != -1) {
                                    f = f2;
                                } else {
                                    int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i, ((ViewGroup) this).mPaddingLeft + ((ViewGroup) this).mPaddingRight + ((ViewGroup.MarginLayoutParams) layoutParams4).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams4).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams4).width);
                                    int measuredHeight3 = virtualChildAt3.getMeasuredHeight() + i44;
                                    if (measuredHeight3 < 0) {
                                        measuredHeight3 = 0;
                                    }
                                    i44 -= measuredHeight3 - virtualChildAt3.getMeasuredHeight();
                                    f = f2;
                                    virtualChildAt3.measure(childMeasureSpec2, View.MeasureSpec.makeMeasureSpec(measuredHeight3, 1073741824));
                                    i20 = ViewGroup.combineMeasuredStates(i20, virtualChildAt3.getMeasuredState() & (-256));
                                }
                            }
                            float f7 = f;
                            int i47 = ((ViewGroup.MarginLayoutParams) layoutParams4).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams4).rightMargin;
                            int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i47;
                            i35 = Math.max(i35, measuredWidth2);
                            if (mode != 1073741824) {
                                i9 = i47;
                                i10 = -1;
                                if (((ViewGroup.MarginLayoutParams) layoutParams4).width == -1) {
                                    z = true;
                                    if (z) {
                                        measuredWidth2 = i9;
                                    }
                                    int max2 = Math.max(i39, measuredWidth2);
                                    boolean z8 = !z6 && ((ViewGroup.MarginLayoutParams) layoutParams4).width == i10;
                                    int i48 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i48, i48 + virtualChildAt3.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams4).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams4).bottomMargin + getNextLocationOffset(virtualChildAt3));
                                    z6 = z8;
                                    i39 = max2;
                                    f2 = f7;
                                }
                            } else {
                                i9 = i47;
                                i10 = -1;
                            }
                            z = false;
                            if (z) {
                            }
                            int max22 = Math.max(i39, measuredWidth2);
                            if (!z6) {
                            }
                            int i482 = this.mTotalLength;
                            this.mTotalLength = Math.max(i482, i482 + virtualChildAt3.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams4).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams4).bottomMargin + getNextLocationOffset(virtualChildAt3));
                            z6 = z8;
                            i39 = max22;
                            f2 = f7;
                        }
                        i45++;
                        i3 = i8;
                    }
                    i4 = i3;
                    i5 = i;
                    this.mTotalLength += ((ViewGroup) this).mPaddingTop + ((ViewGroup) this).mPaddingBottom;
                    i6 = i39;
                    i7 = i35;
                } else {
                    i6 = Math.max(i39, i40);
                    if (z3 && i38 != 1073741824) {
                        for (int i49 = 0; i49 < i3; i49++) {
                            View virtualChildAt4 = getVirtualChildAt(i49);
                            if (virtualChildAt4 != null && virtualChildAt4.getVisibility() != 8 && ((LayoutParams) virtualChildAt4.getLayoutParams()).weight > 0.0f) {
                                virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i36, 1073741824));
                            }
                        }
                    }
                    i4 = i3;
                    i7 = i35;
                    i5 = i;
                }
                if (z6 || mode == 1073741824) {
                    i6 = i7;
                }
                setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i6 + ((ViewGroup) this).mPaddingLeft + ((ViewGroup) this).mPaddingRight, getSuggestedMinimumWidth()), i5, i20), resolveSizeAndState);
                if (!z5) {
                    return;
                }
                forceUniformWidth(i4, i2);
                return;
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).width == -1) {
                    int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).height;
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = i4;
                }
            }
        }
    }

    void measureHorizontal(int i, int i2) {
        throw new IllegalStateException("horizontal mode not supported.");
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0091  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10 = ((ViewGroup) this).mPaddingLeft;
        int i11 = i3 - i;
        int i12 = ((ViewGroup) this).mPaddingRight;
        int i13 = i11 - i12;
        int i14 = (i11 - i10) - i12;
        int virtualChildCount = getVirtualChildCount();
        int i15 = this.mGravity;
        int i16 = i15 & 112;
        int i17 = i15 & 8388615;
        if (i16 == 16) {
            i5 = ((ViewGroup) this).mPaddingTop + (((i4 - i2) - this.mTotalLength) / 2);
        } else if (i16 == 80) {
            i5 = ((((ViewGroup) this).mPaddingTop + i4) - i2) - this.mTotalLength;
        } else {
            i5 = ((ViewGroup) this).mPaddingTop;
        }
        int i18 = 0;
        while (i18 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i18);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i18);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i19 = layoutParams.gravity;
                if (i19 < 0) {
                    i19 = i17;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(i19, getLayoutDirection()) & 7;
                if (absoluteGravity == 1) {
                    i6 = ((i14 - measuredWidth) / 2) + i10 + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    i7 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                } else if (absoluteGravity == 5) {
                    i6 = i13 - measuredWidth;
                    i7 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                } else {
                    i8 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + i10;
                    int i20 = i8;
                    if (hasDividerBeforeChildAt(i18)) {
                        i5 += this.mDividerHeight;
                    }
                    int i21 = i5 + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                    setChildFrame(virtualChildAt, i20, i21 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    i18 += getChildrenSkipCount(virtualChildAt, i18);
                    i5 = i21 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                    i9 = 1;
                    i18 += i9;
                }
                i8 = i6 - i7;
                int i202 = i8;
                if (hasDividerBeforeChildAt(i18)) {
                }
                int i212 = i5 + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                setChildFrame(virtualChildAt, i202, i212 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i18 += getChildrenSkipCount(virtualChildAt, i18);
                i5 = i212 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                i9 = 1;
                i18 += i9;
            }
            i9 = 1;
            i18 += i9;
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (i != this.mLayoutDirection) {
            this.mLayoutDirection = i;
            if (this.mOrientation != 0) {
                return;
            }
            requestLayout();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z;
        int i11;
        int i12;
        int i13;
        int i14;
        boolean isLayoutRtl = isLayoutRtl();
        int i15 = ((ViewGroup) this).mPaddingTop;
        int i16 = i4 - i2;
        int i17 = ((ViewGroup) this).mPaddingBottom;
        int i18 = i16 - i17;
        int i19 = (i16 - i15) - i17;
        int virtualChildCount = getVirtualChildCount();
        int i20 = this.mGravity;
        int i21 = i20 & 112;
        boolean z2 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = Gravity.getAbsoluteGravity(8388615 & i20, getLayoutDirection());
        boolean z3 = true;
        if (absoluteGravity == 1) {
            i5 = ((ViewGroup) this).mPaddingLeft + (((i3 - i) - this.mTotalLength) / 2);
        } else if (absoluteGravity == 5) {
            i5 = ((((ViewGroup) this).mPaddingLeft + i3) - i) - this.mTotalLength;
        } else {
            i5 = ((ViewGroup) this).mPaddingLeft;
        }
        if (isLayoutRtl) {
            i6 = virtualChildCount - 1;
            i7 = -1;
        } else {
            i6 = 0;
            i7 = 1;
        }
        int i22 = 0;
        while (i22 < virtualChildCount) {
            int i23 = i6 + (i7 * i22);
            View virtualChildAt = getVirtualChildAt(i23);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i23);
                z = z3;
                i8 = i15;
                i9 = virtualChildCount;
                i10 = i21;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i24 = i22;
                if (z2) {
                    i9 = virtualChildCount;
                    if (((ViewGroup.MarginLayoutParams) layoutParams).height != -1) {
                        i11 = virtualChildAt.getBaseline();
                        i12 = layoutParams.gravity;
                        if (i12 < 0) {
                            i12 = i21;
                        }
                        i13 = i12 & 112;
                        i10 = i21;
                        if (i13 == 16) {
                            if (i13 == 48) {
                                i14 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + i15;
                                if (i11 != -1) {
                                    z = true;
                                    i14 += iArr[1] - i11;
                                }
                            } else if (i13 != 80) {
                                i14 = i15;
                            } else {
                                i14 = (i18 - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                                if (i11 != -1) {
                                    i14 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i11);
                                }
                            }
                            z = true;
                        } else {
                            z = true;
                            i14 = ((((i19 - measuredHeight) / 2) + i15) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                        }
                        if (hasDividerBeforeChildAt(i23)) {
                            i5 += this.mDividerWidth;
                        }
                        int i25 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + i5;
                        i8 = i15;
                        setChildFrame(virtualChildAt, i25 + getLocationOffset(virtualChildAt), i14, measuredWidth, measuredHeight);
                        i22 = i24 + getChildrenSkipCount(virtualChildAt, i23);
                        i5 = i25 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                        i22++;
                        virtualChildCount = i9;
                        i21 = i10;
                        z3 = z;
                        i15 = i8;
                    }
                } else {
                    i9 = virtualChildCount;
                }
                i11 = -1;
                i12 = layoutParams.gravity;
                if (i12 < 0) {
                }
                i13 = i12 & 112;
                i10 = i21;
                if (i13 == 16) {
                }
                if (hasDividerBeforeChildAt(i23)) {
                }
                int i252 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + i5;
                i8 = i15;
                setChildFrame(virtualChildAt, i252 + getLocationOffset(virtualChildAt), i14, measuredWidth, measuredHeight);
                i22 = i24 + getChildrenSkipCount(virtualChildAt, i23);
                i5 = i252 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                i22++;
                virtualChildCount = i9;
                i21 = i10;
                z3 = z;
                i15 = i8;
            } else {
                i8 = i15;
                i9 = virtualChildCount;
                i10 = i21;
                z = true;
            }
            i22++;
            virtualChildCount = i9;
            i21 = i10;
            z3 = z;
            i15 = i8;
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    @RemotableViewMethod
    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= 8388611;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setHorizontalGravity(int i) {
        int i2 = i & 8388615;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | ((-8388616) & i3);
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & (-113));
            requestLayout();
        }
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i != 1) {
            return null;
        }
        return new LayoutParams(-1, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return MatchParentShrinkingLinearLayout.class.getName();
    }

    protected void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
        super.encodeProperties(viewHierarchyEncoder);
        viewHierarchyEncoder.addProperty("layout:baselineAligned", this.mBaselineAligned);
        viewHierarchyEncoder.addProperty("layout:baselineAlignedChildIndex", this.mBaselineAlignedChildIndex);
        viewHierarchyEncoder.addProperty("measurement:baselineChildTop", this.mBaselineChildTop);
        viewHierarchyEncoder.addProperty("measurement:orientation", this.mOrientation);
        viewHierarchyEncoder.addProperty("measurement:gravity", this.mGravity);
        viewHierarchyEncoder.addProperty("measurement:totalLength", this.mTotalLength);
        viewHierarchyEncoder.addProperty("layout:totalLength", this.mTotalLength);
        viewHierarchyEncoder.addProperty("layout:useLargestChild", this.mUseLargestChild);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = -1, to = "NONE"), @ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 48, to = "TOP"), @ViewDebug.IntToString(from = 80, to = "BOTTOM"), @ViewDebug.IntToString(from = 3, to = "LEFT"), @ViewDebug.IntToString(from = 5, to = "RIGHT"), @ViewDebug.IntToString(from = 8388611, to = "START"), @ViewDebug.IntToString(from = 8388613, to = "END"), @ViewDebug.IntToString(from = 16, to = "CENTER_VERTICAL"), @ViewDebug.IntToString(from = 112, to = "FILL_VERTICAL"), @ViewDebug.IntToString(from = 1, to = "CENTER_HORIZONTAL"), @ViewDebug.IntToString(from = 7, to = "FILL_HORIZONTAL"), @ViewDebug.IntToString(from = 17, to = "CENTER"), @ViewDebug.IntToString(from = R$styleable.AppCompatTheme_windowActionModeOverlay, to = "FILL")})
        public int gravity;
        @ViewDebug.ExportedProperty(category = "layout")
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayout_Layout);
            this.weight = obtainStyledAttributes.getFloat(3, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(0, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public String debug(String str) {
            return str + "MatchParentShrinkingLinearLayout.LayoutParams={width=" + ViewGroup.MarginLayoutParams.sizeToString(((ViewGroup.MarginLayoutParams) this).width) + ", height=" + ViewGroup.MarginLayoutParams.sizeToString(((ViewGroup.MarginLayoutParams) this).height) + " weight=" + this.weight + "}";
        }

        protected void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
            super.encodeProperties(viewHierarchyEncoder);
            viewHierarchyEncoder.addProperty("layout:weight", this.weight);
            viewHierarchyEncoder.addProperty("layout:gravity", this.gravity);
        }
    }
}
