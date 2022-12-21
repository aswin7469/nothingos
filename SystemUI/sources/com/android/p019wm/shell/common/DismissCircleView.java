package com.android.p019wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.p019wm.shell.C3343R;

/* renamed from: com.android.wm.shell.common.DismissCircleView */
public class DismissCircleView extends FrameLayout {
    private final ImageView mIconView;

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

    public DismissCircleView(Context context) {
        super(context);
        ImageView imageView = new ImageView(getContext());
        this.mIconView = imageView;
        Resources resources = getResources();
        setBackground(resources.getDrawable(C3343R.C3345drawable.dismiss_circle_background));
        imageView.setImageDrawable(resources.getDrawable(C3343R.C3345drawable.pip_ic_close_white));
        addView(imageView);
        setViewSizes();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setViewSizes();
    }

    private void setViewSizes() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C3343R.dimen.dismiss_target_x_size);
        this.mIconView.setLayoutParams(new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 17));
    }
}
