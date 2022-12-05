package androidx.leanback.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class ScaleFrameLayout extends FrameLayout {
    private float mChildScale;
    private float mLayoutScaleX;
    private float mLayoutScaleY;

    public ScaleFrameLayout(Context context) {
        this(context, null);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutScaleX = 1.0f;
        this.mLayoutScaleY = 1.0f;
        this.mChildScale = 1.0f;
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        child.setScaleX(this.mChildScale);
        child.setScaleY(this.mChildScale);
    }

    @Override // android.view.ViewGroup
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        boolean addViewInLayout = super.addViewInLayout(child, index, params, preventRequestLayout);
        if (addViewInLayout) {
            child.setScaleX(this.mChildScale);
            child.setScaleY(this.mChildScale);
        }
        return addViewInLayout;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00da  */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float pivotX;
        int paddingLeft;
        int i;
        int paddingRight;
        int paddingTop;
        int i2;
        int paddingBottom;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        ScaleFrameLayout scaleFrameLayout = this;
        int childCount = getChildCount();
        int layoutDirection = getLayoutDirection();
        if (layoutDirection == 1) {
            pivotX = getWidth() - getPivotX();
        } else {
            pivotX = getPivotX();
        }
        if (scaleFrameLayout.mLayoutScaleX != 1.0f) {
            int paddingLeft2 = getPaddingLeft();
            float f = scaleFrameLayout.mLayoutScaleX;
            paddingLeft = paddingLeft2 + ((int) ((pivotX - (pivotX / f)) + 0.5f));
            i = (int) ((((right - left) - pivotX) / f) + pivotX + 0.5f);
            paddingRight = getPaddingRight();
        } else {
            paddingLeft = getPaddingLeft();
            i = right - left;
            paddingRight = getPaddingRight();
        }
        int i10 = i - paddingRight;
        float pivotY = getPivotY();
        if (scaleFrameLayout.mLayoutScaleY != 1.0f) {
            int paddingTop2 = getPaddingTop();
            float f2 = scaleFrameLayout.mLayoutScaleY;
            paddingTop = paddingTop2 + ((int) ((pivotY - (pivotY / f2)) + 0.5f));
            i2 = (int) ((((bottom - top) - pivotY) / f2) + pivotY + 0.5f);
            paddingBottom = getPaddingBottom();
        } else {
            paddingTop = getPaddingTop();
            i2 = bottom - top;
            paddingBottom = getPaddingBottom();
        }
        int i11 = i2 - paddingBottom;
        int i12 = 0;
        while (i12 < childCount) {
            View childAt = scaleFrameLayout.getChildAt(i12);
            if (childAt.getVisibility() != 8) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i13 = layoutParams.gravity;
                if (i13 == -1) {
                    i13 = 8388659;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(i13, layoutDirection);
                int i14 = i13 & 112;
                int i15 = absoluteGravity & 7;
                if (i15 == 1) {
                    i3 = (((i10 - paddingLeft) - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i4 = layoutParams.rightMargin;
                } else if (i15 == 5) {
                    i3 = i10 - measuredWidth;
                    i4 = layoutParams.rightMargin;
                } else {
                    i5 = layoutParams.leftMargin + paddingLeft;
                    if (i14 != 16) {
                        i6 = (((i11 - paddingTop) - measuredHeight) / 2) + paddingTop + layoutParams.topMargin;
                        i7 = layoutParams.bottomMargin;
                    } else {
                        if (i14 == 48) {
                            i9 = layoutParams.topMargin;
                        } else if (i14 == 80) {
                            i6 = i11 - measuredHeight;
                            i7 = layoutParams.bottomMargin;
                        } else {
                            i9 = layoutParams.topMargin;
                        }
                        i8 = i9 + paddingTop;
                        childAt.layout(i5, i8, measuredWidth + i5, measuredHeight + i8);
                        childAt.setPivotX(pivotX - i5);
                        childAt.setPivotY(pivotY - i8);
                    }
                    i8 = i6 - i7;
                    childAt.layout(i5, i8, measuredWidth + i5, measuredHeight + i8);
                    childAt.setPivotX(pivotX - i5);
                    childAt.setPivotY(pivotY - i8);
                }
                i5 = i3 - i4;
                if (i14 != 16) {
                }
                i8 = i6 - i7;
                childAt.layout(i5, i8, measuredWidth + i5, measuredHeight + i8);
                childAt.setPivotX(pivotX - i5);
                childAt.setPivotY(pivotY - i8);
            }
            i12++;
            scaleFrameLayout = this;
        }
    }

    private static int getScaledMeasureSpec(int measureSpec, float scale) {
        return scale == 1.0f ? measureSpec : View.MeasureSpec.makeMeasureSpec((int) ((View.MeasureSpec.getSize(measureSpec) / scale) + 0.5f), View.MeasureSpec.getMode(measureSpec));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float f = this.mLayoutScaleX;
        if (f != 1.0f || this.mLayoutScaleY != 1.0f) {
            super.onMeasure(getScaledMeasureSpec(widthMeasureSpec, f), getScaledMeasureSpec(heightMeasureSpec, this.mLayoutScaleY));
            setMeasuredDimension((int) ((getMeasuredWidth() * this.mLayoutScaleX) + 0.5f), (int) ((getMeasuredHeight() * this.mLayoutScaleY) + 0.5f));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.View
    public void setForeground(Drawable d) {
        throw new UnsupportedOperationException();
    }
}
