package androidx.leanback.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.util.Property;

public class FitWidthBitmapDrawable extends Drawable {
    public static final Property<FitWidthBitmapDrawable, Integer> PROPERTY_VERTICAL_OFFSET = getVerticalOffsetIntProperty();
    BitmapState mBitmapState;
    final Rect mDest;
    boolean mMutated;

    static class BitmapState extends Drawable.ConstantState {
        Bitmap mBitmap;
        final Rect mDefaultSource;
        int mOffset;
        Paint mPaint;
        Rect mSource;

        public int getChangingConfigurations() {
            return 0;
        }

        BitmapState() {
            this.mDefaultSource = new Rect();
            this.mPaint = new Paint();
        }

        BitmapState(BitmapState bitmapState) {
            Rect rect = new Rect();
            this.mDefaultSource = rect;
            this.mBitmap = bitmapState.mBitmap;
            this.mPaint = new Paint(bitmapState.mPaint);
            this.mSource = bitmapState.mSource != null ? new Rect(bitmapState.mSource) : null;
            rect.set(bitmapState.mDefaultSource);
            this.mOffset = bitmapState.mOffset;
        }

        public Drawable newDrawable() {
            return new FitWidthBitmapDrawable(this);
        }
    }

    public FitWidthBitmapDrawable() {
        this.mDest = new Rect();
        this.mMutated = false;
        this.mBitmapState = new BitmapState();
    }

    FitWidthBitmapDrawable(BitmapState bitmapState) {
        this.mDest = new Rect();
        this.mMutated = false;
        this.mBitmapState = bitmapState;
    }

    public Drawable.ConstantState getConstantState() {
        return this.mBitmapState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mBitmapState = new BitmapState(this.mBitmapState);
            this.mMutated = true;
        }
        return this;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmapState.mBitmap = bitmap;
        if (bitmap != null) {
            this.mBitmapState.mDefaultSource.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        } else {
            this.mBitmapState.mDefaultSource.set(0, 0, 0, 0);
        }
        this.mBitmapState.mSource = null;
    }

    public Bitmap getBitmap() {
        return this.mBitmapState.mBitmap;
    }

    public void setSource(Rect rect) {
        this.mBitmapState.mSource = rect;
    }

    public Rect getSource() {
        return this.mBitmapState.mSource;
    }

    public void setVerticalOffset(int i) {
        this.mBitmapState.mOffset = i;
        invalidateSelf();
    }

    public int getVerticalOffset() {
        return this.mBitmapState.mOffset;
    }

    public void draw(Canvas canvas) {
        if (this.mBitmapState.mBitmap != null) {
            Rect bounds = getBounds();
            this.mDest.left = 0;
            this.mDest.top = this.mBitmapState.mOffset;
            this.mDest.right = bounds.width();
            Rect validateSource = validateSource();
            float width = ((float) bounds.width()) / ((float) validateSource.width());
            Rect rect = this.mDest;
            rect.bottom = rect.top + ((int) (((float) validateSource.height()) * width));
            int save = canvas.save();
            canvas.clipRect(bounds);
            canvas.drawBitmap(this.mBitmapState.mBitmap, validateSource, this.mDest, this.mBitmapState.mPaint);
            canvas.restoreToCount(save);
        }
    }

    public void setAlpha(int i) {
        if (i != this.mBitmapState.mPaint.getAlpha()) {
            this.mBitmapState.mPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return this.mBitmapState.mPaint.getAlpha();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mBitmapState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getOpacity() {
        Bitmap bitmap = this.mBitmapState.mBitmap;
        return (bitmap == null || bitmap.hasAlpha() || this.mBitmapState.mPaint.getAlpha() < 255) ? -3 : -1;
    }

    private Rect validateSource() {
        if (this.mBitmapState.mSource == null) {
            return this.mBitmapState.mDefaultSource;
        }
        return this.mBitmapState.mSource;
    }

    static IntProperty<FitWidthBitmapDrawable> getVerticalOffsetIntProperty() {
        return new IntProperty<FitWidthBitmapDrawable>("verticalOffset") {
            public void setValue(FitWidthBitmapDrawable fitWidthBitmapDrawable, int i) {
                fitWidthBitmapDrawable.setVerticalOffset(i);
            }

            public Integer get(FitWidthBitmapDrawable fitWidthBitmapDrawable) {
                return Integer.valueOf(fitWidthBitmapDrawable.getVerticalOffset());
            }
        };
    }
}
