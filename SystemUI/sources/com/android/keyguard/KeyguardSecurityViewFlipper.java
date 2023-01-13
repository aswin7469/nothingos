package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;
import com.android.systemui.C1894R;

public class KeyguardSecurityViewFlipper extends ViewFlipper {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardSecurityViewFlipper";
    private Rect mTempRect;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardSecurityViewFlipper(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardSecurityViewFlipper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTempRect = new Rect();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        this.mTempRect.set(0, 0, 0, 0);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                offsetRectIntoDescendantCoords(childAt, this.mTempRect);
                motionEvent.offsetLocation((float) this.mTempRect.left, (float) this.mTempRect.top);
                onTouchEvent = childAt.dispatchTouchEvent(motionEvent) || onTouchEvent;
                motionEvent.offsetLocation((float) (-this.mTempRect.left), (float) (-this.mTempRect.top));
            }
        }
        return onTouchEvent;
    }

    /* access modifiers changed from: package-private */
    public KeyguardInputView getSecurityView() {
        View childAt = getChildAt(getDisplayedChild());
        if (childAt instanceof KeyguardInputView) {
            return (KeyguardInputView) childAt;
        }
        return null;
    }

    public CharSequence getTitle() {
        KeyguardInputView securityView = getSecurityView();
        return securityView != null ? securityView.getTitle() : "";
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        boolean z = DEBUG;
        if (z && mode != Integer.MIN_VALUE) {
            Log.w(TAG, "onMeasure: widthSpec " + View.MeasureSpec.toString(i) + " should be AT_MOST");
        }
        if (z && mode2 != Integer.MIN_VALUE) {
            Log.w(TAG, "onMeasure: heightSpec " + View.MeasureSpec.toString(i2) + " should be AT_MOST");
        }
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int childCount = getChildCount();
        int i3 = size;
        int i4 = size2;
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() == 0) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.maxWidth > 0 && layoutParams.maxWidth < i3) {
                    i3 = layoutParams.maxWidth;
                }
                if (layoutParams.maxHeight > 0 && layoutParams.maxHeight < i4) {
                    i4 = layoutParams.maxHeight;
                }
            }
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int max = Math.max(0, i3 - paddingLeft);
        int max2 = Math.max(0, i4 - paddingTop);
        int i6 = mode == 1073741824 ? size : 0;
        int i7 = mode2 == 1073741824 ? size2 : 0;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt2 = getChildAt(i8);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            childAt2.measure(makeChildMeasureSpec(max, layoutParams2.width), makeChildMeasureSpec(max2, layoutParams2.height));
            i6 = Math.max(i6, Math.min(childAt2.getMeasuredWidth(), size - paddingLeft));
            i7 = Math.max(i7, Math.min(childAt2.getMeasuredHeight(), size2 - paddingTop));
        }
        setMeasuredDimension(i6 + paddingLeft, i7 + paddingTop);
    }

    private int makeChildMeasureSpec(int i, int i2) {
        int i3;
        if (i2 != -2) {
            i3 = 1073741824;
            if (i2 != -1) {
                i = Math.min(i, i2);
            }
        } else {
            i3 = Integer.MIN_VALUE;
        }
        return View.MeasureSpec.makeMeasureSpec(i, i3);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        @ViewDebug.ExportedProperty(category = "layout")
        public int maxHeight;
        @ViewDebug.ExportedProperty(category = "layout")
        public int maxWidth;

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.maxWidth = layoutParams.maxWidth;
            this.maxHeight = layoutParams.maxHeight;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.KeyguardSecurityViewFlipper_Layout, 0, 0);
            this.maxWidth = obtainStyledAttributes.getDimensionPixelSize(1, 0);
            this.maxHeight = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            obtainStyledAttributes.recycle();
        }

        /* access modifiers changed from: protected */
        public void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
            super.encodeProperties(viewHierarchyEncoder);
            viewHierarchyEncoder.addProperty("layout:maxWidth", this.maxWidth);
            viewHierarchyEncoder.addProperty("layout:maxHeight", this.maxHeight);
        }
    }
}
