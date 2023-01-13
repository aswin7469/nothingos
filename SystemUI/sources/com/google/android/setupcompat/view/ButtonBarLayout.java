package com.google.android.setupcompat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import com.google.android.setupcompat.C3941R;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterActionButton;

public class ButtonBarLayout extends LinearLayout {
    private int originalPaddingLeft;
    private int originalPaddingRight;
    private boolean stacked = false;

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

    public ButtonBarLayout(Context context) {
        super(context);
    }

    public ButtonBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        boolean z;
        int i3;
        int size = View.MeasureSpec.getSize(i);
        setStacked(false);
        boolean z2 = true;
        if (View.MeasureSpec.getMode(i) == 1073741824) {
            i3 = View.MeasureSpec.makeMeasureSpec(0, 0);
            z = true;
        } else {
            z = false;
            i3 = i;
        }
        super.onMeasure(i3, i2);
        if (isFooterButtonsEventlyWeighted(getContext()) || getMeasuredWidth() <= size) {
            z2 = z;
        } else {
            setStacked(true);
        }
        if (z2) {
            super.onMeasure(i, i2);
        }
    }

    private void setStacked(boolean z) {
        if (this.stacked != z) {
            this.stacked = z;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                if (z) {
                    childAt.setTag(C3941R.C3943id.suc_customization_original_weight, Float.valueOf(layoutParams.weight));
                    layoutParams.weight = 0.0f;
                    layoutParams.leftMargin = 0;
                } else {
                    Float f = (Float) childAt.getTag(C3941R.C3943id.suc_customization_original_weight);
                    if (f != null) {
                        layoutParams.weight = f.floatValue();
                    }
                }
                childAt.setLayoutParams(layoutParams);
            }
            setOrientation(z ? 1 : 0);
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                bringChildToFront(getChildAt(i2));
            }
            if (z) {
                setHorizontalGravity(17);
                this.originalPaddingLeft = getPaddingLeft();
                int paddingRight = getPaddingRight();
                this.originalPaddingRight = paddingRight;
                int max = Math.max(this.originalPaddingLeft, paddingRight);
                setPadding(max, getPaddingTop(), max, getPaddingBottom());
                return;
            }
            setPadding(this.originalPaddingLeft, getPaddingTop(), this.originalPaddingRight, getPaddingBottom());
        }
    }

    private boolean isFooterButtonsEventlyWeighted(Context context) {
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if ((childAt instanceof FooterActionButton) && ((FooterActionButton) childAt).isPrimaryButtonStyle()) {
                i++;
            }
        }
        if (i == 2 && context.getResources().getConfiguration().smallestScreenWidthDp >= 600 && PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context)) {
            return true;
        }
        return false;
    }
}
