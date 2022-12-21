package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;

public class BackDropView extends FrameLayout {
    private Runnable mOnVisibilityChangedRunnable;

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public BackDropView(Context context) {
        super(context);
    }

    public BackDropView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BackDropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public BackDropView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        Runnable runnable;
        super.onVisibilityChanged(view, i);
        if (view == this && (runnable = this.mOnVisibilityChangedRunnable) != null) {
            runnable.run();
        }
    }

    public void setOnVisibilityChangedRunnable(Runnable runnable) {
        this.mOnVisibilityChangedRunnable = runnable;
    }
}
