package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;

public class KeyguardPreviewContainer extends FrameLayout {
    private Drawable mBlackBarDrawable;

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

    public KeyguardPreviewContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        C29911 r1 = new Drawable() {
            public int getOpacity() {
                return -1;
            }

            public void setAlpha(int i) {
            }

            public void setColorFilter(ColorFilter colorFilter) {
            }

            public void draw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0, KeyguardPreviewContainer.this.getHeight() - KeyguardPreviewContainer.this.getPaddingBottom(), KeyguardPreviewContainer.this.getWidth(), KeyguardPreviewContainer.this.getHeight());
                canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
                canvas.restore();
            }
        };
        this.mBlackBarDrawable = r1;
        setBackground(r1);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        setPadding(0, 0, 0, windowInsets.getStableInsetBottom());
        return super.onApplyWindowInsets(windowInsets);
    }
}
