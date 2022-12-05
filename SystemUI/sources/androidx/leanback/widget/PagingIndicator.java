package androidx.leanback.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$color;
import androidx.leanback.R$dimen;
import androidx.leanback.R$drawable;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class PagingIndicator extends View {
    private static final TimeInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final Property<Dot, Float> DOT_ALPHA = new Property<Dot, Float>(Float.class, "alpha") { // from class: androidx.leanback.widget.PagingIndicator.1
        @Override // android.util.Property
        public Float get(Dot dot) {
            return Float.valueOf(dot.getAlpha());
        }

        @Override // android.util.Property
        public void set(Dot dot, Float value) {
            dot.setAlpha(value.floatValue());
        }
    };
    private static final Property<Dot, Float> DOT_DIAMETER = new Property<Dot, Float>(Float.class, "diameter") { // from class: androidx.leanback.widget.PagingIndicator.2
        @Override // android.util.Property
        public Float get(Dot dot) {
            return Float.valueOf(dot.getDiameter());
        }

        @Override // android.util.Property
        public void set(Dot dot, Float value) {
            dot.setDiameter(value.floatValue());
        }
    };
    private static final Property<Dot, Float> DOT_TRANSLATION_X = new Property<Dot, Float>(Float.class, "translation_x") { // from class: androidx.leanback.widget.PagingIndicator.3
        @Override // android.util.Property
        public Float get(Dot dot) {
            return Float.valueOf(dot.getTranslationX());
        }

        @Override // android.util.Property
        public void set(Dot dot, Float value) {
            dot.setTranslationX(value.floatValue());
        }
    };
    private final AnimatorSet mAnimator;
    Bitmap mArrow;
    final int mArrowDiameter;
    private final int mArrowGap;
    Paint mArrowPaint;
    final int mArrowRadius;
    final Rect mArrowRect;
    final float mArrowToBgRatio;
    final Paint mBgPaint;
    private int mCurrentPage;
    int mDotCenterY;
    final int mDotDiameter;
    int mDotFgSelectColor;
    private final int mDotGap;
    final int mDotRadius;
    private int[] mDotSelectedNextX;
    private int[] mDotSelectedPrevX;
    private int[] mDotSelectedX;
    private Dot[] mDots;
    final Paint mFgPaint;
    private final AnimatorSet mHideAnimator;
    boolean mIsLtr;
    private int mPageCount;
    private int mPreviousPage;
    private final int mShadowRadius;
    private final AnimatorSet mShowAnimator;

    public PagingIndicator(Context context) {
        this(context, null, 0);
    }

    public PagingIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagingIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimator = animatorSet;
        Resources resources = getResources();
        int[] iArr = R$styleable.PagingIndicator;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyle, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, defStyle, 0);
        int dimensionFromTypedArray = getDimensionFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_lbDotRadius, R$dimen.lb_page_indicator_dot_radius);
        this.mDotRadius = dimensionFromTypedArray;
        this.mDotDiameter = dimensionFromTypedArray * 2;
        int dimensionFromTypedArray2 = getDimensionFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_arrowRadius, R$dimen.lb_page_indicator_arrow_radius);
        this.mArrowRadius = dimensionFromTypedArray2;
        int i = dimensionFromTypedArray2 * 2;
        this.mArrowDiameter = i;
        this.mDotGap = getDimensionFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_dotToDotGap, R$dimen.lb_page_indicator_dot_gap);
        this.mArrowGap = getDimensionFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_dotToArrowGap, R$dimen.lb_page_indicator_arrow_gap);
        int colorFromTypedArray = getColorFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_dotBgColor, R$color.lb_page_indicator_dot);
        Paint paint = new Paint(1);
        this.mBgPaint = paint;
        paint.setColor(colorFromTypedArray);
        this.mDotFgSelectColor = getColorFromTypedArray(obtainStyledAttributes, R$styleable.PagingIndicator_arrowBgColor, R$color.lb_page_indicator_arrow_background);
        if (this.mArrowPaint == null) {
            int i2 = R$styleable.PagingIndicator_arrowColor;
            if (obtainStyledAttributes.hasValue(i2)) {
                setArrowColor(obtainStyledAttributes.getColor(i2, 0));
            }
        }
        obtainStyledAttributes.recycle();
        this.mIsLtr = resources.getConfiguration().getLayoutDirection() == 0;
        int color = resources.getColor(R$color.lb_page_indicator_arrow_shadow);
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.lb_page_indicator_arrow_shadow_radius);
        this.mShadowRadius = dimensionPixelSize;
        Paint paint2 = new Paint(1);
        this.mFgPaint = paint2;
        float dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.lb_page_indicator_arrow_shadow_offset);
        paint2.setShadowLayer(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize2, color);
        this.mArrow = loadArrow();
        this.mArrowRect = new Rect(0, 0, this.mArrow.getWidth(), this.mArrow.getHeight());
        this.mArrowToBgRatio = this.mArrow.getWidth() / i;
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mShowAnimator = animatorSet2;
        animatorSet2.playTogether(createDotAlphaAnimator(0.0f, 1.0f), createDotDiameterAnimator(dimensionFromTypedArray * 2, dimensionFromTypedArray2 * 2), createDotTranslationXAnimator());
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.mHideAnimator = animatorSet3;
        animatorSet3.playTogether(createDotAlphaAnimator(1.0f, 0.0f), createDotDiameterAnimator(dimensionFromTypedArray2 * 2, dimensionFromTypedArray * 2), createDotTranslationXAnimator());
        animatorSet.playTogether(animatorSet2, animatorSet3);
        setLayerType(1, null);
    }

    private int getDimensionFromTypedArray(TypedArray typedArray, int attr, int defaultId) {
        return typedArray.getDimensionPixelOffset(attr, getResources().getDimensionPixelOffset(defaultId));
    }

    private int getColorFromTypedArray(TypedArray typedArray, int attr, int defaultId) {
        return typedArray.getColor(attr, getResources().getColor(defaultId));
    }

    private Bitmap loadArrow() {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R$drawable.lb_ic_nav_arrow);
        if (this.mIsLtr) {
            return decodeResource;
        }
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(decodeResource, 0, 0, decodeResource.getWidth(), decodeResource.getHeight(), matrix, false);
    }

    public void setArrowColor(int color) {
        if (this.mArrowPaint == null) {
            this.mArrowPaint = new Paint();
        }
        this.mArrowPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    private Animator createDotAlphaAnimator(float from, float to) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, DOT_ALPHA, from, to);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(DECELERATE_INTERPOLATOR);
        return ofFloat;
    }

    private Animator createDotDiameterAnimator(float from, float to) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, DOT_DIAMETER, from, to);
        ofFloat.setDuration(417L);
        ofFloat.setInterpolator(DECELERATE_INTERPOLATOR);
        return ofFloat;
    }

    private Animator createDotTranslationXAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, DOT_TRANSLATION_X, (-this.mArrowGap) + this.mDotGap, 0.0f);
        ofFloat.setDuration(417L);
        ofFloat.setInterpolator(DECELERATE_INTERPOLATOR);
        return ofFloat;
    }

    private void calculateDotPositions() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int width = getWidth() - getPaddingRight();
        int requiredWidth = getRequiredWidth();
        int i = (paddingLeft + width) / 2;
        int i2 = this.mPageCount;
        int[] iArr = new int[i2];
        this.mDotSelectedX = iArr;
        int[] iArr2 = new int[i2];
        this.mDotSelectedPrevX = iArr2;
        int[] iArr3 = new int[i2];
        this.mDotSelectedNextX = iArr3;
        int i3 = 1;
        if (this.mIsLtr) {
            int i4 = i - (requiredWidth / 2);
            int i5 = this.mDotRadius;
            int i6 = this.mDotGap;
            int i7 = this.mArrowGap;
            iArr[0] = ((i4 + i5) - i6) + i7;
            iArr2[0] = i4 + i5;
            iArr3[0] = ((i4 + i5) - (i6 * 2)) + (i7 * 2);
            while (i3 < this.mPageCount) {
                int[] iArr4 = this.mDotSelectedX;
                int[] iArr5 = this.mDotSelectedPrevX;
                int i8 = i3 - 1;
                int i9 = iArr5[i8];
                int i10 = this.mArrowGap;
                iArr4[i3] = i9 + i10;
                iArr5[i3] = iArr5[i8] + this.mDotGap;
                this.mDotSelectedNextX[i3] = iArr4[i8] + i10;
                i3++;
            }
        } else {
            int i11 = i + (requiredWidth / 2);
            int i12 = this.mDotRadius;
            int i13 = this.mDotGap;
            int i14 = this.mArrowGap;
            iArr[0] = ((i11 - i12) + i13) - i14;
            iArr2[0] = i11 - i12;
            iArr3[0] = ((i11 - i12) + (i13 * 2)) - (i14 * 2);
            while (i3 < this.mPageCount) {
                int[] iArr6 = this.mDotSelectedX;
                int[] iArr7 = this.mDotSelectedPrevX;
                int i15 = i3 - 1;
                int i16 = iArr7[i15];
                int i17 = this.mArrowGap;
                iArr6[i3] = i16 - i17;
                iArr7[i3] = iArr7[i15] - this.mDotGap;
                this.mDotSelectedNextX[i3] = iArr6[i15] - i17;
                i3++;
            }
        }
        this.mDotCenterY = paddingTop + this.mArrowRadius;
        adjustDotPosition();
    }

    int getPageCount() {
        return this.mPageCount;
    }

    int[] getDotSelectedX() {
        return this.mDotSelectedX;
    }

    int[] getDotSelectedLeftX() {
        return this.mDotSelectedPrevX;
    }

    int[] getDotSelectedRightX() {
        return this.mDotSelectedNextX;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getDesiredHeight();
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (mode == Integer.MIN_VALUE) {
            desiredHeight = Math.min(desiredHeight, View.MeasureSpec.getSize(heightMeasureSpec));
        } else if (mode == 1073741824) {
            desiredHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        }
        int desiredWidth = getDesiredWidth();
        int mode2 = View.MeasureSpec.getMode(widthMeasureSpec);
        if (mode2 == Integer.MIN_VALUE) {
            desiredWidth = Math.min(desiredWidth, View.MeasureSpec.getSize(widthMeasureSpec));
        } else if (mode2 == 1073741824) {
            desiredWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        setMeasuredDimension(width, height);
        calculateDotPositions();
    }

    private int getDesiredHeight() {
        return getPaddingTop() + this.mArrowDiameter + getPaddingBottom() + this.mShadowRadius;
    }

    private int getRequiredWidth() {
        return (this.mDotRadius * 2) + (this.mArrowGap * 2) + ((this.mPageCount - 3) * this.mDotGap);
    }

    private int getDesiredWidth() {
        return getPaddingLeft() + getRequiredWidth() + getPaddingRight();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < this.mPageCount; i++) {
            this.mDots[i].draw(canvas);
        }
    }

    private void adjustDotPosition() {
        int i;
        float f;
        int i2 = 0;
        while (true) {
            i = this.mCurrentPage;
            f = -1.0f;
            if (i2 >= i) {
                break;
            }
            this.mDots[i2].deselect();
            Dot[] dotArr = this.mDots;
            Dot dot = dotArr[i2];
            if (i2 != this.mPreviousPage) {
                f = 1.0f;
            }
            dot.mDirection = f;
            dotArr[i2].mCenterX = this.mDotSelectedPrevX[i2];
            i2++;
        }
        this.mDots[i].select();
        Dot[] dotArr2 = this.mDots;
        int i3 = this.mCurrentPage;
        Dot dot2 = dotArr2[i3];
        if (this.mPreviousPage >= i3) {
            f = 1.0f;
        }
        dot2.mDirection = f;
        dotArr2[i3].mCenterX = this.mDotSelectedX[i3];
        while (true) {
            i3++;
            if (i3 < this.mPageCount) {
                this.mDots[i3].deselect();
                Dot[] dotArr3 = this.mDots;
                dotArr3[i3].mDirection = 1.0f;
                dotArr3[i3].mCenterX = this.mDotSelectedNextX[i3];
            } else {
                return;
            }
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        boolean z = layoutDirection == 0;
        if (this.mIsLtr != z) {
            this.mIsLtr = z;
            this.mArrow = loadArrow();
            Dot[] dotArr = this.mDots;
            if (dotArr != null) {
                for (Dot dot : dotArr) {
                    dot.onRtlPropertiesChanged();
                }
            }
            calculateDotPositions();
            invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class Dot {
        float mAlpha;
        float mArrowImageRadius;
        float mCenterX;
        float mDiameter;
        float mDirection;
        int mFgColor;
        float mLayoutDirection;
        float mRadius;
        float mTranslationX;
        final /* synthetic */ PagingIndicator this$0;

        void select() {
            this.mTranslationX = 0.0f;
            this.mCenterX = 0.0f;
            PagingIndicator pagingIndicator = this.this$0;
            this.mDiameter = pagingIndicator.mArrowDiameter;
            float f = pagingIndicator.mArrowRadius;
            this.mRadius = f;
            this.mArrowImageRadius = f * pagingIndicator.mArrowToBgRatio;
            this.mAlpha = 1.0f;
            adjustAlpha();
        }

        void deselect() {
            this.mTranslationX = 0.0f;
            this.mCenterX = 0.0f;
            PagingIndicator pagingIndicator = this.this$0;
            this.mDiameter = pagingIndicator.mDotDiameter;
            float f = pagingIndicator.mDotRadius;
            this.mRadius = f;
            this.mArrowImageRadius = f * pagingIndicator.mArrowToBgRatio;
            this.mAlpha = 0.0f;
            adjustAlpha();
        }

        public void adjustAlpha() {
            this.mFgColor = Color.argb(Math.round(this.mAlpha * 255.0f), Color.red(this.this$0.mDotFgSelectColor), Color.green(this.this$0.mDotFgSelectColor), Color.blue(this.this$0.mDotFgSelectColor));
        }

        public float getAlpha() {
            return this.mAlpha;
        }

        public void setAlpha(float alpha) {
            this.mAlpha = alpha;
            adjustAlpha();
            this.this$0.invalidate();
        }

        public float getTranslationX() {
            return this.mTranslationX;
        }

        public void setTranslationX(float translationX) {
            this.mTranslationX = translationX * this.mDirection * this.mLayoutDirection;
            this.this$0.invalidate();
        }

        public float getDiameter() {
            return this.mDiameter;
        }

        public void setDiameter(float diameter) {
            this.mDiameter = diameter;
            float f = diameter / 2.0f;
            this.mRadius = f;
            PagingIndicator pagingIndicator = this.this$0;
            this.mArrowImageRadius = f * pagingIndicator.mArrowToBgRatio;
            pagingIndicator.invalidate();
        }

        void draw(Canvas canvas) {
            float f = this.mCenterX + this.mTranslationX;
            PagingIndicator pagingIndicator = this.this$0;
            canvas.drawCircle(f, pagingIndicator.mDotCenterY, this.mRadius, pagingIndicator.mBgPaint);
            if (this.mAlpha > 0.0f) {
                this.this$0.mFgPaint.setColor(this.mFgColor);
                PagingIndicator pagingIndicator2 = this.this$0;
                canvas.drawCircle(f, pagingIndicator2.mDotCenterY, this.mRadius, pagingIndicator2.mFgPaint);
                PagingIndicator pagingIndicator3 = this.this$0;
                Bitmap bitmap = pagingIndicator3.mArrow;
                Rect rect = pagingIndicator3.mArrowRect;
                float f2 = this.mArrowImageRadius;
                int i = this.this$0.mDotCenterY;
                canvas.drawBitmap(bitmap, rect, new Rect((int) (f - f2), (int) (i - f2), (int) (f + f2), (int) (i + f2)), this.this$0.mArrowPaint);
            }
        }

        void onRtlPropertiesChanged() {
            this.mLayoutDirection = this.this$0.mIsLtr ? 1.0f : -1.0f;
        }
    }
}
