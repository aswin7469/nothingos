package com.google.android.setupdesign.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean checked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
        setFocusable(true);
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setFocusable(true);
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setFocusable(true);
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int i) {
        if (!this.checked) {
            return super.onCreateDrawableState(i);
        }
        return mergeDrawableStates(super.onCreateDrawableState(i + 1), new int[]{16842912});
    }

    public void setChecked(boolean z) {
        this.checked = z;
        refreshDrawableState();
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void toggle() {
        setChecked(!isChecked());
    }
}
