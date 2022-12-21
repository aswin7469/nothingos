package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.C1893R;

public class ExpandableIndicator extends ImageView {
    private boolean mExpanded;
    private boolean mIsDefaultDirection = true;

    public ExpandableIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        updateIndicatorDrawable();
        setContentDescription(getContentDescription(this.mExpanded));
    }

    public void setExpanded(boolean z) {
        if (z != this.mExpanded) {
            this.mExpanded = z;
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getContext().getDrawable(getDrawableResourceId(!z)).getConstantState().newDrawable();
            setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.forceAnimationOnUI();
            animatedVectorDrawable.start();
            setContentDescription(getContentDescription(z));
        }
    }

    public void setDefaultDirection(boolean z) {
        this.mIsDefaultDirection = z;
        updateIndicatorDrawable();
    }

    private int getDrawableResourceId(boolean z) {
        return this.mIsDefaultDirection ? z ? C1893R.C1895drawable.ic_volume_collapse_animation : C1893R.C1895drawable.ic_volume_expand_animation : z ? C1893R.C1895drawable.ic_volume_expand_animation : C1893R.C1895drawable.ic_volume_collapse_animation;
    }

    private String getContentDescription(boolean z) {
        if (z) {
            return this.mContext.getString(C1893R.string.accessibility_quick_settings_collapse);
        }
        return this.mContext.getString(C1893R.string.accessibility_quick_settings_expand);
    }

    private void updateIndicatorDrawable() {
        setImageResource(getDrawableResourceId(this.mExpanded));
    }
}
