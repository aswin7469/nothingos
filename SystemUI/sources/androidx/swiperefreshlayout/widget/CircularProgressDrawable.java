package androidx.swiperefreshlayout.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.util.Preconditions;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
/* loaded from: classes.dex */
public class CircularProgressDrawable extends Drawable implements Animatable {
    private Animator mAnimator;
    boolean mFinishing;
    private Resources mResources;
    private final Ring mRing;
    private float mRotation;
    float mRotationCount;
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final int[] COLORS = {-16777216};

    private int evaluateColorChange(float fraction, int startValue, int endValue) {
        int i = (startValue >> 24) & 255;
        int i2 = (startValue >> 16) & 255;
        int i3 = (startValue >> 8) & 255;
        int i4 = startValue & 255;
        return ((i + ((int) ((((endValue >> 24) & 255) - i) * fraction))) << 24) | ((i2 + ((int) ((((endValue >> 16) & 255) - i2) * fraction))) << 16) | ((i3 + ((int) ((((endValue >> 8) & 255) - i3) * fraction))) << 8) | (i4 + ((int) (fraction * ((endValue & 255) - i4))));
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public CircularProgressDrawable(Context context) {
        this.mResources = ((Context) Preconditions.checkNotNull(context)).getResources();
        Ring ring = new Ring();
        this.mRing = ring;
        ring.setColors(COLORS);
        setStrokeWidth(2.5f);
        setupAnimators();
    }

    private void setSizeParameters(float centerRadius, float strokeWidth, float arrowWidth, float arrowHeight) {
        Ring ring = this.mRing;
        float f = this.mResources.getDisplayMetrics().density;
        ring.setStrokeWidth(strokeWidth * f);
        ring.setCenterRadius(centerRadius * f);
        ring.setColorIndex(0);
        ring.setArrowDimensions(arrowWidth * f, arrowHeight * f);
    }

    public void setStyle(int size) {
        if (size == 0) {
            setSizeParameters(11.0f, 3.0f, 12.0f, 6.0f);
        } else {
            setSizeParameters(7.5f, 2.5f, 10.0f, 5.0f);
        }
        invalidateSelf();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mRing.setStrokeWidth(strokeWidth);
        invalidateSelf();
    }

    public void setArrowEnabled(boolean show) {
        this.mRing.setShowArrow(show);
        invalidateSelf();
    }

    public void setArrowScale(float scale) {
        this.mRing.setArrowScale(scale);
        invalidateSelf();
    }

    public void setStartEndTrim(float start, float end) {
        this.mRing.setStartTrim(start);
        this.mRing.setEndTrim(end);
        invalidateSelf();
    }

    public void setProgressRotation(float rotation) {
        this.mRing.setRotation(rotation);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        canvas.rotate(this.mRotation, bounds.exactCenterX(), bounds.exactCenterY());
        this.mRing.draw(canvas, bounds);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mRing.setAlpha(alpha);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mRing.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mRing.setColorFilter(colorFilter);
        invalidateSelf();
    }

    private void setRotation(float rotation) {
        this.mRotation = rotation;
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.mAnimator.isRunning();
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        this.mAnimator.cancel();
        this.mRing.storeOriginals();
        if (this.mRing.getEndTrim() != this.mRing.getStartTrim()) {
            this.mFinishing = true;
            this.mAnimator.setDuration(666L);
            this.mAnimator.start();
            return;
        }
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
        this.mAnimator.setDuration(1332L);
        this.mAnimator.start();
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.mAnimator.cancel();
        setRotation(0.0f);
        this.mRing.setShowArrow(false);
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
        invalidateSelf();
    }

    void updateRingColor(float interpolatedTime, Ring ring) {
        if (interpolatedTime > 0.75f) {
            ring.setColor(evaluateColorChange((interpolatedTime - 0.75f) / 0.25f, ring.getStartingColor(), ring.getNextColor()));
        } else {
            ring.setColor(ring.getStartingColor());
        }
    }

    private void applyFinishTranslation(float interpolatedTime, Ring ring) {
        updateRingColor(interpolatedTime, ring);
        float floor = (float) (Math.floor(ring.getStartingRotation() / 0.8f) + 1.0d);
        ring.setStartTrim(ring.getStartingStartTrim() + (((ring.getStartingEndTrim() - 0.01f) - ring.getStartingStartTrim()) * interpolatedTime));
        ring.setEndTrim(ring.getStartingEndTrim());
        ring.setRotation(ring.getStartingRotation() + ((floor - ring.getStartingRotation()) * interpolatedTime));
    }

    void applyTransformation(float interpolatedTime, Ring ring, boolean lastFrame) {
        float interpolation;
        float f;
        if (this.mFinishing) {
            applyFinishTranslation(interpolatedTime, ring);
        } else if (interpolatedTime == 1.0f && !lastFrame) {
        } else {
            float startingRotation = ring.getStartingRotation();
            if (interpolatedTime < 0.5f) {
                interpolation = ring.getStartingStartTrim();
                f = (MATERIAL_INTERPOLATOR.getInterpolation(interpolatedTime / 0.5f) * 0.79f) + 0.01f + interpolation;
            } else {
                float startingStartTrim = ring.getStartingStartTrim() + 0.79f;
                interpolation = startingStartTrim - (((1.0f - MATERIAL_INTERPOLATOR.getInterpolation((interpolatedTime - 0.5f) / 0.5f)) * 0.79f) + 0.01f);
                f = startingStartTrim;
            }
            ring.setStartTrim(interpolation);
            ring.setEndTrim(f);
            ring.setRotation(startingRotation + (0.20999998f * interpolatedTime));
            setRotation((interpolatedTime + this.mRotationCount) * 216.0f);
        }
    }

    private void setupAnimators() {
        final Ring ring = this.mRing;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.swiperefreshlayout.widget.CircularProgressDrawable.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float floatValue = ((Float) animation.getAnimatedValue()).floatValue();
                CircularProgressDrawable.this.updateRingColor(floatValue, ring);
                CircularProgressDrawable.this.applyTransformation(floatValue, ring, false);
                CircularProgressDrawable.this.invalidateSelf();
            }
        });
        ofFloat.setRepeatCount(-1);
        ofFloat.setRepeatMode(1);
        ofFloat.setInterpolator(LINEAR_INTERPOLATOR);
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: androidx.swiperefreshlayout.widget.CircularProgressDrawable.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                CircularProgressDrawable.this.mRotationCount = 0.0f;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
                CircularProgressDrawable.this.applyTransformation(1.0f, ring, true);
                ring.storeOriginals();
                ring.goToNextColor();
                CircularProgressDrawable circularProgressDrawable = CircularProgressDrawable.this;
                if (circularProgressDrawable.mFinishing) {
                    circularProgressDrawable.mFinishing = false;
                    animator.cancel();
                    animator.setDuration(1332L);
                    animator.start();
                    ring.setShowArrow(false);
                    return;
                }
                circularProgressDrawable.mRotationCount += 1.0f;
            }
        });
        this.mAnimator = ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Ring {
        Path mArrow;
        int mArrowHeight;
        final Paint mArrowPaint;
        int mArrowWidth;
        final Paint mCirclePaint;
        int mColorIndex;
        int[] mColors;
        int mCurrentColor;
        final Paint mPaint;
        float mRingCenterRadius;
        boolean mShowArrow;
        float mStartingEndTrim;
        float mStartingRotation;
        float mStartingStartTrim;
        final RectF mTempBounds = new RectF();
        float mStartTrim = 0.0f;
        float mEndTrim = 0.0f;
        float mRotation = 0.0f;
        float mStrokeWidth = 5.0f;
        float mArrowScale = 1.0f;
        int mAlpha = 255;

        Ring() {
            Paint paint = new Paint();
            this.mPaint = paint;
            Paint paint2 = new Paint();
            this.mArrowPaint = paint2;
            Paint paint3 = new Paint();
            this.mCirclePaint = paint3;
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setAntiAlias(true);
            paint3.setColor(0);
        }

        void setArrowDimensions(float width, float height) {
            this.mArrowWidth = (int) width;
            this.mArrowHeight = (int) height;
        }

        void draw(Canvas c, Rect bounds) {
            RectF rectF = this.mTempBounds;
            float f = this.mRingCenterRadius;
            float f2 = (this.mStrokeWidth / 2.0f) + f;
            if (f <= 0.0f) {
                f2 = (Math.min(bounds.width(), bounds.height()) / 2.0f) - Math.max((this.mArrowWidth * this.mArrowScale) / 2.0f, this.mStrokeWidth / 2.0f);
            }
            rectF.set(bounds.centerX() - f2, bounds.centerY() - f2, bounds.centerX() + f2, bounds.centerY() + f2);
            float f3 = this.mStartTrim;
            float f4 = this.mRotation;
            float f5 = (f3 + f4) * 360.0f;
            float f6 = ((this.mEndTrim + f4) * 360.0f) - f5;
            this.mPaint.setColor(this.mCurrentColor);
            this.mPaint.setAlpha(this.mAlpha);
            float f7 = this.mStrokeWidth / 2.0f;
            rectF.inset(f7, f7);
            c.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, this.mCirclePaint);
            float f8 = -f7;
            rectF.inset(f8, f8);
            c.drawArc(rectF, f5, f6, false, this.mPaint);
            drawTriangle(c, f5, f6, rectF);
        }

        void drawTriangle(Canvas c, float startAngle, float sweepAngle, RectF bounds) {
            if (this.mShowArrow) {
                Path path = this.mArrow;
                if (path == null) {
                    Path path2 = new Path();
                    this.mArrow = path2;
                    path2.setFillType(Path.FillType.EVEN_ODD);
                } else {
                    path.reset();
                }
                this.mArrow.moveTo(0.0f, 0.0f);
                this.mArrow.lineTo(this.mArrowWidth * this.mArrowScale, 0.0f);
                Path path3 = this.mArrow;
                float f = this.mArrowScale;
                path3.lineTo((this.mArrowWidth * f) / 2.0f, this.mArrowHeight * f);
                this.mArrow.offset(((Math.min(bounds.width(), bounds.height()) / 2.0f) + bounds.centerX()) - ((this.mArrowWidth * this.mArrowScale) / 2.0f), bounds.centerY() + (this.mStrokeWidth / 2.0f));
                this.mArrow.close();
                this.mArrowPaint.setColor(this.mCurrentColor);
                this.mArrowPaint.setAlpha(this.mAlpha);
                c.save();
                c.rotate(startAngle + sweepAngle, bounds.centerX(), bounds.centerY());
                c.drawPath(this.mArrow, this.mArrowPaint);
                c.restore();
            }
        }

        void setColors(int[] colors) {
            this.mColors = colors;
            setColorIndex(0);
        }

        void setColor(int color) {
            this.mCurrentColor = color;
        }

        void setColorIndex(int index) {
            this.mColorIndex = index;
            this.mCurrentColor = this.mColors[index];
        }

        int getNextColor() {
            return this.mColors[getNextColorIndex()];
        }

        int getNextColorIndex() {
            return (this.mColorIndex + 1) % this.mColors.length;
        }

        void goToNextColor() {
            setColorIndex(getNextColorIndex());
        }

        void setColorFilter(ColorFilter filter) {
            this.mPaint.setColorFilter(filter);
        }

        void setAlpha(int alpha) {
            this.mAlpha = alpha;
        }

        int getAlpha() {
            return this.mAlpha;
        }

        void setStrokeWidth(float strokeWidth) {
            this.mStrokeWidth = strokeWidth;
            this.mPaint.setStrokeWidth(strokeWidth);
        }

        void setStartTrim(float startTrim) {
            this.mStartTrim = startTrim;
        }

        float getStartTrim() {
            return this.mStartTrim;
        }

        float getStartingStartTrim() {
            return this.mStartingStartTrim;
        }

        float getStartingEndTrim() {
            return this.mStartingEndTrim;
        }

        int getStartingColor() {
            return this.mColors[this.mColorIndex];
        }

        void setEndTrim(float endTrim) {
            this.mEndTrim = endTrim;
        }

        float getEndTrim() {
            return this.mEndTrim;
        }

        void setRotation(float rotation) {
            this.mRotation = rotation;
        }

        void setCenterRadius(float centerRadius) {
            this.mRingCenterRadius = centerRadius;
        }

        void setShowArrow(boolean show) {
            if (this.mShowArrow != show) {
                this.mShowArrow = show;
            }
        }

        void setArrowScale(float scale) {
            if (scale != this.mArrowScale) {
                this.mArrowScale = scale;
            }
        }

        float getStartingRotation() {
            return this.mStartingRotation;
        }

        void storeOriginals() {
            this.mStartingStartTrim = this.mStartTrim;
            this.mStartingEndTrim = this.mEndTrim;
            this.mStartingRotation = this.mRotation;
        }

        void resetOriginals() {
            this.mStartingStartTrim = 0.0f;
            this.mStartingEndTrim = 0.0f;
            this.mStartingRotation = 0.0f;
            setStartTrim(0.0f);
            setEndTrim(0.0f);
            setRotation(0.0f);
        }
    }
}
