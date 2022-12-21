package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.RelativeLayout;

public class AlphaOptimizedRelativeLayout extends RelativeLayout {
    public boolean hasOverlappingRendering() {
        return false;
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public AlphaOptimizedRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
