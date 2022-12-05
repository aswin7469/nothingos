package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class HorizontalGridView extends BaseGridView {
    private boolean mFadingHighEdge;
    private boolean mFadingLowEdge;
    private LinearGradient mHighFadeShader;
    private int mHighFadeShaderLength;
    private int mHighFadeShaderOffset;
    private LinearGradient mLowFadeShader;
    private int mLowFadeShaderLength;
    private int mLowFadeShaderOffset;
    private Bitmap mTempBitmapHigh;
    private Bitmap mTempBitmapLow;
    private Paint mTempPaint;
    private Rect mTempRect;

    public HorizontalGridView(Context context) {
        this(context, null);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTempPaint = new Paint();
        this.mTempRect = new Rect();
        this.mLayoutManager.setOrientation(0);
        initAttributes(context, attrs);
    }

    @SuppressLint({"CustomViewStyleable"})
    protected void initAttributes(Context context, AttributeSet attrs) {
        initBaseGridViewAttributes(context, attrs);
        int[] iArr = R$styleable.lbHorizontalGridView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, 0, 0);
        setRowHeight(obtainStyledAttributes);
        setNumRows(obtainStyledAttributes.getInt(R$styleable.lbHorizontalGridView_numberOfRows, 1));
        obtainStyledAttributes.recycle();
        updateLayerType();
        Paint paint = new Paint();
        this.mTempPaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    void setRowHeight(TypedArray array) {
        int i = R$styleable.lbHorizontalGridView_rowHeight;
        if (array.peekValue(i) != null) {
            setRowHeight(array.getLayoutDimension(i, 0));
        }
    }

    public void setNumRows(int numRows) {
        this.mLayoutManager.setNumRows(numRows);
        requestLayout();
    }

    public void setRowHeight(int height) {
        this.mLayoutManager.setRowHeight(height);
        requestLayout();
    }

    private boolean needsFadingLowEdge() {
        if (!this.mFadingLowEdge) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.mLayoutManager.getOpticalLeft(getChildAt(i)) < getPaddingLeft() - this.mLowFadeShaderOffset) {
                return true;
            }
        }
        return false;
    }

    private boolean needsFadingHighEdge() {
        if (!this.mFadingHighEdge) {
            return false;
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (this.mLayoutManager.getOpticalRight(getChildAt(childCount)) > (getWidth() - getPaddingRight()) + this.mHighFadeShaderOffset) {
                return true;
            }
        }
        return false;
    }

    private Bitmap getTempBitmapLow() {
        Bitmap bitmap = this.mTempBitmapLow;
        if (bitmap == null || bitmap.getWidth() != this.mLowFadeShaderLength || this.mTempBitmapLow.getHeight() != getHeight()) {
            this.mTempBitmapLow = Bitmap.createBitmap(this.mLowFadeShaderLength, getHeight(), Bitmap.Config.ARGB_8888);
        }
        return this.mTempBitmapLow;
    }

    private Bitmap getTempBitmapHigh() {
        Bitmap bitmap = this.mTempBitmapHigh;
        if (bitmap == null || bitmap.getWidth() != this.mHighFadeShaderLength || this.mTempBitmapHigh.getHeight() != getHeight()) {
            this.mTempBitmapHigh = Bitmap.createBitmap(this.mHighFadeShaderLength, getHeight(), Bitmap.Config.ARGB_8888);
        }
        return this.mTempBitmapHigh;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void draw(Canvas canvas) {
        int i;
        boolean needsFadingLowEdge = needsFadingLowEdge();
        boolean needsFadingHighEdge = needsFadingHighEdge();
        if (!needsFadingLowEdge) {
            this.mTempBitmapLow = null;
        }
        if (!needsFadingHighEdge) {
            this.mTempBitmapHigh = null;
        }
        if (!needsFadingLowEdge && !needsFadingHighEdge) {
            super.draw(canvas);
            return;
        }
        int paddingLeft = this.mFadingLowEdge ? (getPaddingLeft() - this.mLowFadeShaderOffset) - this.mLowFadeShaderLength : 0;
        int width = this.mFadingHighEdge ? (getWidth() - getPaddingRight()) + this.mHighFadeShaderOffset + this.mHighFadeShaderLength : getWidth();
        int save = canvas.save();
        canvas.clipRect((this.mFadingLowEdge ? this.mLowFadeShaderLength : 0) + paddingLeft, 0, width - (this.mFadingHighEdge ? this.mHighFadeShaderLength : 0), getHeight());
        super.draw(canvas);
        canvas.restoreToCount(save);
        Canvas canvas2 = new Canvas();
        Rect rect = this.mTempRect;
        rect.top = 0;
        rect.bottom = getHeight();
        if (needsFadingLowEdge && this.mLowFadeShaderLength > 0) {
            Bitmap tempBitmapLow = getTempBitmapLow();
            tempBitmapLow.eraseColor(0);
            canvas2.setBitmap(tempBitmapLow);
            int save2 = canvas2.save();
            canvas2.clipRect(0, 0, this.mLowFadeShaderLength, getHeight());
            float f = -paddingLeft;
            canvas2.translate(f, 0.0f);
            super.draw(canvas2);
            canvas2.restoreToCount(save2);
            this.mTempPaint.setShader(this.mLowFadeShader);
            canvas2.drawRect(0.0f, 0.0f, this.mLowFadeShaderLength, getHeight(), this.mTempPaint);
            Rect rect2 = this.mTempRect;
            rect2.left = 0;
            rect2.right = this.mLowFadeShaderLength;
            canvas.translate(paddingLeft, 0.0f);
            Rect rect3 = this.mTempRect;
            canvas.drawBitmap(tempBitmapLow, rect3, rect3, (Paint) null);
            canvas.translate(f, 0.0f);
        }
        if (!needsFadingHighEdge || this.mHighFadeShaderLength <= 0) {
            return;
        }
        Bitmap tempBitmapHigh = getTempBitmapHigh();
        tempBitmapHigh.eraseColor(0);
        canvas2.setBitmap(tempBitmapHigh);
        int save3 = canvas2.save();
        canvas2.clipRect(0, 0, this.mHighFadeShaderLength, getHeight());
        canvas2.translate(-(width - this.mHighFadeShaderLength), 0.0f);
        super.draw(canvas2);
        canvas2.restoreToCount(save3);
        this.mTempPaint.setShader(this.mHighFadeShader);
        canvas2.drawRect(0.0f, 0.0f, this.mHighFadeShaderLength, getHeight(), this.mTempPaint);
        Rect rect4 = this.mTempRect;
        rect4.left = 0;
        rect4.right = this.mHighFadeShaderLength;
        canvas.translate(width - i, 0.0f);
        Rect rect5 = this.mTempRect;
        canvas.drawBitmap(tempBitmapHigh, rect5, rect5, (Paint) null);
        canvas.translate(-(width - this.mHighFadeShaderLength), 0.0f);
    }

    private void updateLayerType() {
        if (this.mFadingLowEdge || this.mFadingHighEdge) {
            setLayerType(2, null);
            setWillNotDraw(false);
            return;
        }
        setLayerType(0, null);
        setWillNotDraw(true);
    }
}
