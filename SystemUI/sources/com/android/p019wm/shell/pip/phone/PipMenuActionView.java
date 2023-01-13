package com.android.p019wm.shell.pip.phone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.pip.phone.PipMenuActionView */
public class PipMenuActionView extends FrameLayout {
    private View mCustomCloseBackground;
    private ImageView mImageView;

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

    public PipMenuActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(C3353R.C3356id.image);
        this.mCustomCloseBackground = findViewById(C3353R.C3356id.custom_close_bg);
    }

    public void setImageDrawable(Drawable drawable) {
        this.mImageView.setImageDrawable(drawable);
    }

    public void setCustomCloseBackgroundVisibility(int i) {
        this.mCustomCloseBackground.setVisibility(i);
    }
}
