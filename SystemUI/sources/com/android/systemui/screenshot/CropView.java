package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.SeekBar;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import java.util.List;

public class CropView extends View {
    private static final String TAG = "CropView";
    private int mActivePointerId;
    private final Paint mContainerBackgroundPaint;
    /* access modifiers changed from: private */
    public RectF mCrop;
    private CropInteractionListener mCropInteractionListener;
    /* access modifiers changed from: private */
    public final float mCropTouchMargin;
    private CropBoundary mCurrentDraggingBoundary;
    private float mEntranceInterpolation;
    private final ExploreByTouchHelper mExploreByTouchHelper;
    private int mExtraBottomPadding;
    private int mExtraTopPadding;
    private final Paint mHandlePaint;
    private int mImageWidth;
    private Range<Float> mMotionRange;
    private float mMovementStartValue;
    private final Paint mShadePaint;
    private float mStartingX;
    private float mStartingY;

    public enum CropBoundary {
        NONE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public interface CropInteractionListener {
        void onCropDragComplete();

        void onCropDragMoved(CropBoundary cropBoundary, float f, int i, float f2, float f3);

        void onCropDragStarted(CropBoundary cropBoundary, float f, int i, float f2, float f3);
    }

    public CropView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCrop = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.mCurrentDraggingBoundary = CropBoundary.NONE;
        this.mEntranceInterpolation = 1.0f;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C1893R.styleable.CropView, 0, 0);
        Paint paint = new Paint();
        this.mShadePaint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(4, 0), obtainStyledAttributes.getInteger(3, 255)));
        Paint paint2 = new Paint();
        this.mContainerBackgroundPaint = paint2;
        paint2.setColor(obtainStyledAttributes.getColor(0, 0));
        Paint paint3 = new Paint();
        this.mHandlePaint = paint3;
        paint3.setColor(obtainStyledAttributes.getColor(1, ViewCompat.MEASURED_STATE_MASK));
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth((float) obtainStyledAttributes.getDimensionPixelSize(2, 20));
        obtainStyledAttributes.recycle();
        this.mCropTouchMargin = getResources().getDisplayMetrics().density * 24.0f;
        AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
        this.mExploreByTouchHelper = accessibilityHelper;
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mCrop = this.mCrop;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCrop = savedState.mCrop;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lerp = MathUtils.lerp(this.mCrop.top, 0.0f, this.mEntranceInterpolation);
        float lerp2 = MathUtils.lerp(this.mCrop.bottom, 1.0f, this.mEntranceInterpolation);
        Canvas canvas2 = canvas;
        drawShade(canvas2, 0.0f, lerp, 1.0f, this.mCrop.top);
        drawShade(canvas2, 0.0f, this.mCrop.bottom, 1.0f, lerp2);
        drawShade(canvas2, 0.0f, this.mCrop.top, this.mCrop.left, this.mCrop.bottom);
        drawShade(canvas2, this.mCrop.right, this.mCrop.top, 1.0f, this.mCrop.bottom);
        drawContainerBackground(canvas2, 0.0f, 0.0f, 1.0f, lerp);
        drawContainerBackground(canvas2, 0.0f, lerp2, 1.0f, 1.0f);
        this.mHandlePaint.setAlpha((int) (this.mEntranceInterpolation * 255.0f));
        drawHorizontalHandle(canvas, this.mCrop.top, true);
        drawHorizontalHandle(canvas, this.mCrop.bottom, false);
        drawVerticalHandle(canvas, this.mCrop.left, true);
        drawVerticalHandle(canvas, this.mCrop.right, false);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        int fractionToVerticalPixels = fractionToVerticalPixels(this.mCrop.top);
        int fractionToVerticalPixels2 = fractionToVerticalPixels(this.mCrop.bottom);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 5) {
                            if (actionMasked == 6 && this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                                updateListener(1, motionEvent.getX(motionEvent.getActionIndex()));
                                return true;
                            }
                        } else if (this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                            updateListener(0, motionEvent.getX(motionEvent.getActionIndex()));
                            return true;
                        }
                    }
                } else if (this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex >= 0) {
                        if (isVertical(this.mCurrentDraggingBoundary)) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = this.mStartingY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = this.mStartingX;
                        }
                        setBoundaryPosition(this.mCurrentDraggingBoundary, this.mMotionRange.clamp(Float.valueOf(this.mMovementStartValue + pixelDistanceToFraction((float) ((int) (f2 - f)), this.mCurrentDraggingBoundary))).floatValue());
                        updateListener(2, motionEvent.getX(findPointerIndex));
                        invalidate();
                    }
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
            if (this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                int i = this.mActivePointerId;
                if (i == motionEvent.getPointerId(i)) {
                    updateListener(1, motionEvent.getX(0));
                    return true;
                }
            }
            return super.onTouchEvent(motionEvent);
        }
        CropBoundary nearestBoundary = nearestBoundary(motionEvent, fractionToVerticalPixels, fractionToVerticalPixels2, fractionToHorizontalPixels(this.mCrop.left), fractionToHorizontalPixels(this.mCrop.right));
        this.mCurrentDraggingBoundary = nearestBoundary;
        if (nearestBoundary != CropBoundary.NONE) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mStartingY = motionEvent.getY();
            this.mStartingX = motionEvent.getX();
            this.mMovementStartValue = getBoundaryPosition(this.mCurrentDraggingBoundary);
            updateListener(0, motionEvent.getX());
            this.mMotionRange = getAllowedValues(this.mCurrentDraggingBoundary);
        }
        return true;
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mExploreByTouchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.mExploreByTouchHelper.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        this.mExploreByTouchHelper.onFocusChanged(z, i, rect);
    }

    /* renamed from: com.android.systemui.screenshot.CropView$1 */
    static /* synthetic */ class C24301 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.systemui.screenshot.CropView$CropBoundary[] r0 = com.android.systemui.screenshot.CropView.CropBoundary.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary = r0
                com.android.systemui.screenshot.CropView$CropBoundary r1 = com.android.systemui.screenshot.CropView.CropBoundary.TOP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.screenshot.CropView$CropBoundary r1 = com.android.systemui.screenshot.CropView.CropBoundary.BOTTOM     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.screenshot.CropView$CropBoundary r1 = com.android.systemui.screenshot.CropView.CropBoundary.LEFT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.screenshot.CropView$CropBoundary r1 = com.android.systemui.screenshot.CropView.CropBoundary.RIGHT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.systemui.screenshot.CropView$CropBoundary r1 = com.android.systemui.screenshot.CropView.CropBoundary.NONE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenshot.CropView.C24301.<clinit>():void");
        }
    }

    public void setBoundaryPosition(CropBoundary cropBoundary, float f) {
        float floatValue = ((Float) getAllowedValues(cropBoundary).clamp(Float.valueOf(f))).floatValue();
        int i = C24301.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            this.mCrop.top = floatValue;
        } else if (i == 2) {
            this.mCrop.bottom = floatValue;
        } else if (i == 3) {
            this.mCrop.left = floatValue;
        } else if (i == 4) {
            this.mCrop.right = floatValue;
        } else if (i == 5) {
            Log.w(TAG, "No boundary selected");
        }
        invalidate();
    }

    /* access modifiers changed from: private */
    public float getBoundaryPosition(CropBoundary cropBoundary) {
        int i = C24301.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            return this.mCrop.top;
        }
        if (i == 2) {
            return this.mCrop.bottom;
        }
        if (i == 3) {
            return this.mCrop.left;
        }
        if (i != 4) {
            return 0.0f;
        }
        return this.mCrop.right;
    }

    /* access modifiers changed from: private */
    public static boolean isVertical(CropBoundary cropBoundary) {
        return cropBoundary == CropBoundary.TOP || cropBoundary == CropBoundary.BOTTOM;
    }

    public void animateBoundaryTo(CropBoundary cropBoundary, float f) {
        if (cropBoundary == CropBoundary.NONE) {
            Log.w(TAG, "No boundary selected for animation");
            return;
        }
        float boundaryPosition = getBoundaryPosition(cropBoundary);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new CropView$$ExternalSyntheticLambda0(this, cropBoundary, boundaryPosition, f));
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        valueAnimator.setDuration(750);
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        valueAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$animateBoundaryTo$0$com-android-systemui-screenshot-CropView */
    public /* synthetic */ void mo37287x648c44ab(CropBoundary cropBoundary, float f, float f2, ValueAnimator valueAnimator) {
        setBoundaryPosition(cropBoundary, MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
        invalidate();
    }

    public void animateEntrance() {
        this.mEntranceInterpolation = 0.0f;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new CropView$$ExternalSyntheticLambda1(this));
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        valueAnimator.setDuration(750);
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        valueAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$animateEntrance$1$com-android-systemui-screenshot-CropView */
    public /* synthetic */ void mo37288x765de9b9(ValueAnimator valueAnimator) {
        this.mEntranceInterpolation = valueAnimator.getAnimatedFraction();
        invalidate();
    }

    public void setExtraPadding(int i, int i2) {
        this.mExtraTopPadding = i;
        this.mExtraBottomPadding = i2;
        invalidate();
    }

    public void setImageWidth(int i) {
        this.mImageWidth = i;
        invalidate();
    }

    public Rect getCropBoundaries(int i, int i2) {
        float f = (float) i;
        float f2 = (float) i2;
        return new Rect((int) (this.mCrop.left * f), (int) (this.mCrop.top * f2), (int) (this.mCrop.right * f), (int) (this.mCrop.bottom * f2));
    }

    public void setCropInteractionListener(CropInteractionListener cropInteractionListener) {
        this.mCropInteractionListener = cropInteractionListener;
    }

    private Range getAllowedValues(CropBoundary cropBoundary) {
        int i = C24301.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.bottom - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.BOTTOM)));
        }
        if (i == 2) {
            return new Range(Float.valueOf(this.mCrop.top + pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.TOP)), Float.valueOf(1.0f));
        }
        if (i == 3) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.right - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.RIGHT)));
        }
        if (i != 4) {
            return null;
        }
        return new Range(Float.valueOf(this.mCrop.left + pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.LEFT)), Float.valueOf(1.0f));
    }

    private void updateListener(int i, float f) {
        if (this.mCropInteractionListener != null && isVertical(this.mCurrentDraggingBoundary)) {
            float boundaryPosition = getBoundaryPosition(this.mCurrentDraggingBoundary);
            if (i == 0) {
                this.mCropInteractionListener.onCropDragStarted(this.mCurrentDraggingBoundary, boundaryPosition, fractionToVerticalPixels(boundaryPosition), (this.mCrop.left + this.mCrop.right) / 2.0f, f);
            } else if (i == 1) {
                this.mCropInteractionListener.onCropDragComplete();
            } else if (i == 2) {
                this.mCropInteractionListener.onCropDragMoved(this.mCurrentDraggingBoundary, boundaryPosition, fractionToVerticalPixels(boundaryPosition), (this.mCrop.left + this.mCrop.right) / 2.0f, f);
            }
        }
    }

    private void drawShade(Canvas canvas, float f, float f2, float f3, float f4) {
        canvas.drawRect((float) fractionToHorizontalPixels(f), (float) fractionToVerticalPixels(f2), (float) fractionToHorizontalPixels(f3), (float) fractionToVerticalPixels(f4), this.mShadePaint);
    }

    private void drawContainerBackground(Canvas canvas, float f, float f2, float f3, float f4) {
        canvas.drawRect((float) fractionToHorizontalPixels(f), (float) fractionToVerticalPixels(f2), (float) fractionToHorizontalPixels(f3), (float) fractionToVerticalPixels(f4), this.mContainerBackgroundPaint);
    }

    private void drawHorizontalHandle(Canvas canvas, float f, boolean z) {
        float fractionToVerticalPixels = (float) fractionToVerticalPixels(f);
        canvas.drawLine((float) fractionToHorizontalPixels(this.mCrop.left), fractionToVerticalPixels, (float) fractionToHorizontalPixels(this.mCrop.right), fractionToVerticalPixels, this.mHandlePaint);
        float f2 = getResources().getDisplayMetrics().density * 8.0f;
        float fractionToHorizontalPixels = (float) ((fractionToHorizontalPixels(this.mCrop.left) + fractionToHorizontalPixels(this.mCrop.right)) / 2);
        canvas.drawArc(fractionToHorizontalPixels - f2, fractionToVerticalPixels - f2, fractionToHorizontalPixels + f2, fractionToVerticalPixels + f2, z ? 180.0f : 0.0f, 180.0f, true, this.mHandlePaint);
    }

    private void drawVerticalHandle(Canvas canvas, float f, boolean z) {
        float fractionToHorizontalPixels = (float) fractionToHorizontalPixels(f);
        canvas.drawLine(fractionToHorizontalPixels, (float) fractionToVerticalPixels(this.mCrop.top), fractionToHorizontalPixels, (float) fractionToVerticalPixels(this.mCrop.bottom), this.mHandlePaint);
        float f2 = getResources().getDisplayMetrics().density * 8.0f;
        float f3 = fractionToHorizontalPixels - f2;
        float fractionToVerticalPixels = (float) ((fractionToVerticalPixels(getBoundaryPosition(CropBoundary.TOP)) + fractionToVerticalPixels(getBoundaryPosition(CropBoundary.BOTTOM))) / 2);
        canvas.drawArc(f3, fractionToVerticalPixels - f2, fractionToHorizontalPixels + f2, fractionToVerticalPixels + f2, z ? 90.0f : 270.0f, 180.0f, true, this.mHandlePaint);
    }

    /* access modifiers changed from: private */
    public int fractionToVerticalPixels(float f) {
        return (int) (((float) this.mExtraTopPadding) + (f * ((float) getImageHeight())));
    }

    /* access modifiers changed from: private */
    public int fractionToHorizontalPixels(float f) {
        int width = getWidth();
        int i = this.mImageWidth;
        return (int) (((float) ((width - i) / 2)) + (f * ((float) i)));
    }

    private int getImageHeight() {
        return (getHeight() - this.mExtraTopPadding) - this.mExtraBottomPadding;
    }

    /* access modifiers changed from: private */
    public float pixelDistanceToFraction(float f, CropBoundary cropBoundary) {
        int i;
        if (isVertical(cropBoundary)) {
            i = getImageHeight();
        } else {
            i = this.mImageWidth;
        }
        return f / ((float) i);
    }

    private CropBoundary nearestBoundary(MotionEvent motionEvent, int i, int i2, int i3, int i4) {
        float f = (float) i;
        if (Math.abs(motionEvent.getY() - f) < this.mCropTouchMargin) {
            return CropBoundary.TOP;
        }
        float f2 = (float) i2;
        if (Math.abs(motionEvent.getY() - f2) < this.mCropTouchMargin) {
            return CropBoundary.BOTTOM;
        }
        if (motionEvent.getY() > f || motionEvent.getY() < f2) {
            if (Math.abs(motionEvent.getX() - ((float) i3)) < this.mCropTouchMargin) {
                return CropBoundary.LEFT;
            }
            if (Math.abs(motionEvent.getX() - ((float) i4)) < this.mCropTouchMargin) {
                return CropBoundary.RIGHT;
            }
        }
        return CropBoundary.NONE;
    }

    private class AccessibilityHelper extends ExploreByTouchHelper {
        private static final int BOTTOM_HANDLE_ID = 2;
        private static final int LEFT_HANDLE_ID = 3;
        private static final int RIGHT_HANDLE_ID = 4;
        private static final int TOP_HANDLE_ID = 1;

        AccessibilityHelper() {
            super(CropView.this);
        }

        /* access modifiers changed from: protected */
        public int getVirtualViewAt(float f, float f2) {
            CropView cropView = CropView.this;
            if (Math.abs(f2 - ((float) cropView.fractionToVerticalPixels(cropView.mCrop.top))) < CropView.this.mCropTouchMargin) {
                return 1;
            }
            CropView cropView2 = CropView.this;
            if (Math.abs(f2 - ((float) cropView2.fractionToVerticalPixels(cropView2.mCrop.bottom))) < CropView.this.mCropTouchMargin) {
                return 2;
            }
            CropView cropView3 = CropView.this;
            if (f2 <= ((float) cropView3.fractionToVerticalPixels(cropView3.mCrop.top))) {
                return -1;
            }
            CropView cropView4 = CropView.this;
            if (f2 >= ((float) cropView4.fractionToVerticalPixels(cropView4.mCrop.bottom))) {
                return -1;
            }
            CropView cropView5 = CropView.this;
            if (Math.abs(f - ((float) cropView5.fractionToHorizontalPixels(cropView5.mCrop.left))) < CropView.this.mCropTouchMargin) {
                return 3;
            }
            CropView cropView6 = CropView.this;
            return Math.abs(f - ((float) cropView6.fractionToHorizontalPixels(cropView6.mCrop.right))) < CropView.this.mCropTouchMargin ? 4 : -1;
        }

        /* access modifiers changed from: protected */
        public void getVisibleVirtualViews(List<Integer> list) {
            list.add(1);
            list.add(3);
            list.add(4);
            list.add(2);
        }

        /* access modifiers changed from: protected */
        public void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getBoundaryContentDescription(viewIdToBoundary(i)));
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            accessibilityNodeInfoCompat.setContentDescription(getBoundaryContentDescription(viewIdToBoundary));
            setNodePosition(getNodeRect(viewIdToBoundary), accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(SeekBar.class.getName());
            accessibilityNodeInfoCompat.addAction(4096);
            accessibilityNodeInfoCompat.addAction(8192);
        }

        /* access modifiers changed from: protected */
        public boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 != 4096 && i2 != 8192) {
                return false;
            }
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            CropView cropView = CropView.this;
            float access$400 = cropView.pixelDistanceToFraction(cropView.mCropTouchMargin, viewIdToBoundary);
            if (i2 == 4096) {
                access$400 = -access$400;
            }
            CropView cropView2 = CropView.this;
            cropView2.setBoundaryPosition(viewIdToBoundary, access$400 + cropView2.getBoundaryPosition(viewIdToBoundary));
            invalidateVirtualView(i);
            sendEventForVirtualView(i, 4);
            return true;
        }

        private CharSequence getBoundaryContentDescription(CropBoundary cropBoundary) {
            int i;
            int i2 = C24301.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
            if (i2 == 1) {
                i = C1893R.string.screenshot_top_boundary_pct;
            } else if (i2 == 2) {
                i = C1893R.string.screenshot_bottom_boundary_pct;
            } else if (i2 == 3) {
                i = C1893R.string.screenshot_left_boundary_pct;
            } else if (i2 != 4) {
                return "";
            } else {
                i = C1893R.string.screenshot_right_boundary_pct;
            }
            return CropView.this.getResources().getString(i, new Object[]{Integer.valueOf(Math.round(CropView.this.getBoundaryPosition(cropBoundary) * 100.0f))});
        }

        private CropBoundary viewIdToBoundary(int i) {
            if (i == 1) {
                return CropBoundary.TOP;
            }
            if (i == 2) {
                return CropBoundary.BOTTOM;
            }
            if (i == 3) {
                return CropBoundary.LEFT;
            }
            if (i != 4) {
                return CropBoundary.NONE;
            }
            return CropBoundary.RIGHT;
        }

        private Rect getNodeRect(CropBoundary cropBoundary) {
            if (CropView.isVertical(cropBoundary)) {
                CropView cropView = CropView.this;
                float access$100 = (float) cropView.fractionToVerticalPixels(cropView.getBoundaryPosition(cropBoundary));
                Rect rect = new Rect(0, (int) (access$100 - CropView.this.mCropTouchMargin), CropView.this.getWidth(), (int) (access$100 + CropView.this.mCropTouchMargin));
                if (rect.top >= 0) {
                    return rect;
                }
                rect.offset(0, -rect.top);
                return rect;
            }
            CropView cropView2 = CropView.this;
            float access$300 = (float) cropView2.fractionToHorizontalPixels(cropView2.getBoundaryPosition(cropBoundary));
            CropView cropView3 = CropView.this;
            CropView cropView4 = CropView.this;
            return new Rect((int) (access$300 - CropView.this.mCropTouchMargin), (int) (((float) cropView3.fractionToVerticalPixels(cropView3.mCrop.top)) + CropView.this.mCropTouchMargin), (int) (access$300 + CropView.this.mCropTouchMargin), (int) (((float) cropView4.fractionToVerticalPixels(cropView4.mCrop.bottom)) - CropView.this.mCropTouchMargin));
        }

        private void setNodePosition(Rect rect, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            int[] iArr = new int[2];
            CropView.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
        }
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (C24301) null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        RectF mCrop;

        /* synthetic */ SavedState(Parcel parcel, C24301 r2) {
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mCrop = (RectF) parcel.readParcelable(ClassLoader.getSystemClassLoader());
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mCrop, 0);
        }
    }
}
