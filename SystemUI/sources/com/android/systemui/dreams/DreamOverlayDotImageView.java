package com.android.systemui.dreams;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.AlphaOptimizedImageView;

public class DreamOverlayDotImageView extends AlphaOptimizedImageView {
    private final int mDotColor;

    public DreamOverlayDotImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DreamOverlayDotImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DreamOverlayDotImageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DreamOverlayDotImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C1893R.styleable.DreamOverlayDotImageView, 0, 0);
        try {
            this.mDotColor = obtainStyledAttributes.getColor(0, -1);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setImageDrawable(new DotDrawable(this.mDotColor));
    }

    private static class DotDrawable extends Drawable {
        private final Rect mBounds = new Rect();
        private Bitmap mDotBitmap;
        private final int mDotColor;
        private final Paint mPaint = new Paint(1);

        public int getOpacity() {
            return 0;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        DotDrawable(int i) {
            this.mDotColor = i;
        }

        public void draw(Canvas canvas) {
            if (!this.mBounds.isEmpty()) {
                if (this.mDotBitmap == null) {
                    this.mDotBitmap = createBitmap(this.mBounds.width(), this.mBounds.height());
                }
                canvas.drawBitmap(this.mDotBitmap, (Rect) null, this.mBounds, this.mPaint);
            }
        }

        /* access modifiers changed from: protected */
        public void onBoundsChange(Rect rect) {
            super.onBoundsChange(rect);
            this.mBounds.set(rect.left, rect.top, rect.right, rect.bottom);
            this.mDotBitmap = null;
        }

        private Bitmap createBitmap(int i, int i2) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint(1);
            paint.setColor(this.mDotColor);
            canvas.drawCircle(((float) i) / 2.0f, ((float) i2) / 2.0f, ((float) Math.min(i, i2)) / 2.0f, paint);
            return createBitmap;
        }
    }
}
