package com.android.systemui.p012qs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;

/* renamed from: com.android.systemui.qs.QuickTileLayout */
public class QuickTileLayout extends LinearLayout {
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

    public QuickTileLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public QuickTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setGravity(17);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(layoutParams.height, layoutParams.height);
        LinearLayout.LayoutParams layoutParams3 = layoutParams2;
        layoutParams2.weight = 1.0f;
        super.addView(view, i, layoutParams2);
    }
}
