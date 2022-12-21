package com.android.settingslib.graph;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.DrawableWrapper;
import android.os.Handler;
import android.telephony.CellSignalStrength;
import android.util.PathParser;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;

public class SignalDrawable extends DrawableWrapper {
    private static final long DOT_DELAY = 1000;
    private static final float DOT_PADDING = 0.0625f;
    private static final float DOT_SIZE = 0.125f;
    private static final int LEVEL_MASK = 255;
    private static final int NUM_DOTS = 3;
    private static final int NUM_LEVEL_MASK = 65280;
    private static final int NUM_LEVEL_SHIFT = 8;
    private static final float PAD = 0.083333336f;
    private static final int STATE_CARRIER_CHANGE = 3;
    private static final int STATE_CUT = 2;
    private static final int STATE_MASK = 16711680;
    private static final int STATE_SHIFT = 16;
    private static final String TAG = "SignalDrawable";
    private static final float VIEWPORT = 24.0f;
    private boolean mAnimating;
    private final Path mAttributionPath;
    private final Matrix mAttributionScaleMatrix;
    /* access modifiers changed from: private */
    public final Runnable mChangeDot;
    /* access modifiers changed from: private */
    public int mCurrentDot;
    private final float mCutoutHeightFraction;
    private final Path mCutoutPath;
    private final float mCutoutWidthFraction;
    private float mDarkIntensity;
    private final int mDarkModeFillColor;
    private final Paint mForegroundPaint = new Paint(1);
    private final Path mForegroundPath;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final int mIntrinsicSize;
    private final int mLightModeFillColor;
    private final Path mScaledAttributionPath;
    private final Paint mTransparentPaint;

    public static int getCarrierChangeState(int i) {
        return (i << 8) | 196608;
    }

    public static int getState(int i) {
        return (i & STATE_MASK) >> 16;
    }

    public static int getState(int i, int i2, boolean z) {
        return i | (i2 << 8) | ((z ? 2 : 0) << 16);
    }

    static /* synthetic */ int access$004(SignalDrawable signalDrawable) {
        int i = signalDrawable.mCurrentDot + 1;
        signalDrawable.mCurrentDot = i;
        return i;
    }

    public SignalDrawable(Context context) {
        super(context.getDrawable(17302851));
        Paint paint = new Paint(1);
        this.mTransparentPaint = paint;
        this.mCutoutPath = new Path();
        this.mForegroundPath = new Path();
        Path path = new Path();
        this.mAttributionPath = path;
        this.mAttributionScaleMatrix = new Matrix();
        this.mScaledAttributionPath = new Path();
        this.mDarkIntensity = -1.0f;
        this.mChangeDot = new Runnable() {
            public void run() {
                if (SignalDrawable.access$004(SignalDrawable.this) == 3) {
                    int unused = SignalDrawable.this.mCurrentDot = 0;
                }
                SignalDrawable.this.invalidateSelf();
                SignalDrawable.this.mHandler.postDelayed(SignalDrawable.this.mChangeDot, 1000);
            }
        };
        path.set(PathParser.createPathFromPathData(context.getString(17040040)));
        updateScaledAttributionPath();
        this.mCutoutWidthFraction = context.getResources().getFloat(17105112);
        this.mCutoutHeightFraction = context.getResources().getFloat(17105111);
        this.mDarkModeFillColor = Utils.getColorStateListDefaultColor(context, C1757R.C1758color.dark_mode_icon_color_single_tone);
        this.mLightModeFillColor = Utils.getColorStateListDefaultColor(context, C1757R.C1758color.light_mode_icon_color_single_tone);
        this.mIntrinsicSize = context.getResources().getDimensionPixelSize(C1757R.dimen.signal_icon_size);
        paint.setColor(context.getColor(17170445));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.mHandler = new Handler();
        setDarkIntensity(0.0f);
    }

    private void updateScaledAttributionPath() {
        if (getBounds().isEmpty()) {
            this.mAttributionScaleMatrix.setScale(1.0f, 1.0f);
        } else {
            this.mAttributionScaleMatrix.setScale(((float) getBounds().width()) / VIEWPORT, ((float) getBounds().height()) / VIEWPORT);
        }
        this.mAttributionPath.transform(this.mAttributionScaleMatrix, this.mScaledAttributionPath);
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicSize;
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicSize;
    }

    private void updateAnimation() {
        boolean z = isInState(3) && isVisible();
        if (z != this.mAnimating) {
            this.mAnimating = z;
            if (z) {
                this.mChangeDot.run();
            } else {
                this.mHandler.removeCallbacks(this.mChangeDot);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        super.onLevelChange(unpackLevel(i));
        updateAnimation();
        setTintList(ColorStateList.valueOf(this.mForegroundPaint.getColor()));
        invalidateSelf();
        return true;
    }

    private int unpackLevel(int i) {
        return (i & 255) + (((65280 & i) >> 8) == CellSignalStrength.getNumSignalStrengthLevels() + 1 ? 10 : 0);
    }

    public void setDarkIntensity(float f) {
        if (f != this.mDarkIntensity) {
            setTintList(ColorStateList.valueOf(getFillColor(f)));
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        int color = this.mForegroundPaint.getColor();
        this.mForegroundPaint.setColor(colorStateList.getDefaultColor());
        if (color != this.mForegroundPaint.getColor()) {
            invalidateSelf();
        }
    }

    private int getFillColor(float f) {
        return getColorForDarkIntensity(f, this.mLightModeFillColor, this.mDarkModeFillColor);
    }

    private int getColorForDarkIntensity(float f, int i, int i2) {
        return ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateScaledAttributionPath();
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        canvas.saveLayer((RectF) null, (Paint) null);
        float width = (float) getBounds().width();
        float height = (float) getBounds().height();
        boolean z = true;
        if (getLayoutDirection() != 1) {
            z = false;
        }
        if (z) {
            canvas.save();
            canvas.translate(width, 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        super.draw(canvas);
        this.mCutoutPath.reset();
        this.mCutoutPath.setFillType(Path.FillType.WINDING);
        float round = (float) Math.round(PAD * width);
        if (isInState(3)) {
            float f = DOT_SIZE * height;
            float f2 = height * DOT_PADDING;
            float f3 = f2 + f;
            float f4 = (width - round) - f;
            this.mForegroundPath.reset();
            float f5 = (height - round) - f;
            float f6 = f2;
            float f7 = f;
            drawDotAndPadding(f4, f5, f6, f7, 2);
            drawDotAndPadding(f4 - f3, f5, f6, f7, 1);
            drawDotAndPadding(f4 - (f3 * 2.0f), f5, f6, f7, 0);
            canvas.drawPath(this.mCutoutPath, this.mTransparentPaint);
            canvas.drawPath(this.mForegroundPath, this.mForegroundPaint);
        } else if (isInState(2)) {
            float f8 = (this.mCutoutWidthFraction * width) / VIEWPORT;
            float f9 = (this.mCutoutHeightFraction * height) / VIEWPORT;
            this.mCutoutPath.moveTo(width, height);
            this.mCutoutPath.rLineTo(-f8, 0.0f);
            this.mCutoutPath.rLineTo(0.0f, -f9);
            this.mCutoutPath.rLineTo(f8, 0.0f);
            this.mCutoutPath.rLineTo(0.0f, f9);
            canvas.drawPath(this.mCutoutPath, this.mTransparentPaint);
            canvas.drawPath(this.mScaledAttributionPath, this.mForegroundPaint);
        }
        if (z) {
            canvas.restore();
        }
        canvas.restore();
    }

    private void drawDotAndPadding(float f, float f2, float f3, float f4, int i) {
        if (i == this.mCurrentDot) {
            float f5 = f + f4;
            float f6 = f2 + f4;
            this.mForegroundPath.addRect(f, f2, f5, f6, Path.Direction.CW);
            this.mCutoutPath.addRect(f - f3, f2 - f3, f5 + f3, f6 + f3, Path.Direction.CW);
        }
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mForegroundPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        this.mForegroundPaint.setColorFilter(colorFilter);
    }

    public boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        updateAnimation();
        return visible;
    }

    private boolean isInState(int i) {
        return getState(getLevel()) == i;
    }

    public static int getEmptyState(int i) {
        return getState(0, i, true);
    }
}
