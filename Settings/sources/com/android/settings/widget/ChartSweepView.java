package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import com.android.settings.R;
import com.android.settings.R$styleable;
/* loaded from: classes.dex */
public class ChartSweepView extends View {
    private ChartAxis mAxis;
    private View.OnClickListener mClickListener;
    private Rect mContentOffset;
    private long mDragInterval;
    private int mFollowAxis;
    private int mLabelColor;
    private DynamicLayout mLabelLayout;
    private int mLabelMinSize;
    private float mLabelOffset;
    private float mLabelSize;
    private SpannableStringBuilder mLabelTemplate;
    private int mLabelTemplateRes;
    private long mLabelValue;
    private OnSweepListener mListener;
    private Rect mMargins;
    private float mNeighborMargin;
    private ChartSweepView[] mNeighbors;
    private Paint mOutlinePaint;
    private int mSafeRegion;
    private Drawable mSweep;
    private Point mSweepOffset;
    private Rect mSweepPadding;
    private int mTouchMode;
    private MotionEvent mTracking;
    private float mTrackingStart;
    private long mValidAfter;
    private ChartSweepView mValidAfterDynamic;
    private long mValidBefore;
    private ChartSweepView mValidBeforeDynamic;
    private long mValue;

    /* loaded from: classes.dex */
    public interface OnSweepListener {
        void onSweep(ChartSweepView chartSweepView, boolean z);

        void requestEdit(ChartSweepView chartSweepView);
    }

    @Override // android.view.View
    public void addOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    @Override // android.view.View
    public void removeOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    public ChartSweepView(Context context) {
        this(context, null);
    }

    public ChartSweepView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChartSweepView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSweepPadding = new Rect();
        this.mContentOffset = new Rect();
        this.mSweepOffset = new Point();
        this.mMargins = new Rect();
        this.mOutlinePaint = new Paint();
        this.mTouchMode = 0;
        this.mDragInterval = 1L;
        this.mNeighbors = new ChartSweepView[0];
        this.mClickListener = new View.OnClickListener() { // from class: com.android.settings.widget.ChartSweepView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ChartSweepView.this.dispatchRequestEdit();
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ChartSweepView, i, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.ChartSweepView_labelColor, -16776961);
        setSweepDrawable(obtainStyledAttributes.getDrawable(R$styleable.ChartSweepView_sweepDrawable), color);
        setFollowAxis(obtainStyledAttributes.getInt(R$styleable.ChartSweepView_followAxis, -1));
        setNeighborMargin(obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_neighborMargin, 0));
        setSafeRegion(obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_safeRegion, 0));
        setLabelMinSize(obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_labelSize, 0));
        setLabelTemplate(obtainStyledAttributes.getResourceId(R$styleable.ChartSweepView_labelTemplate, 0));
        setLabelColor(color);
        setBackgroundResource(R.drawable.data_usage_sweep_background);
        this.mOutlinePaint.setColor(-65536);
        this.mOutlinePaint.setStrokeWidth(1.0f);
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        obtainStyledAttributes.recycle();
        setClickable(true);
        setOnClickListener(this.mClickListener);
        setWillNotDraw(false);
    }

    public void setNeighbors(ChartSweepView... chartSweepViewArr) {
        this.mNeighbors = chartSweepViewArr;
    }

    public int getFollowAxis() {
        return this.mFollowAxis;
    }

    public Rect getMargins() {
        return this.mMargins;
    }

    public void setDragInterval(long j) {
        this.mDragInterval = j;
    }

    private float getTargetInset() {
        float f;
        int i;
        if (this.mFollowAxis == 1) {
            int intrinsicHeight = this.mSweep.getIntrinsicHeight();
            Rect rect = this.mSweepPadding;
            int i2 = rect.top;
            f = i2 + (((intrinsicHeight - i2) - rect.bottom) / 2.0f);
            i = this.mSweepOffset.y;
        } else {
            int intrinsicWidth = this.mSweep.getIntrinsicWidth();
            Rect rect2 = this.mSweepPadding;
            int i3 = rect2.left;
            f = i3 + (((intrinsicWidth - i3) - rect2.right) / 2.0f);
            i = this.mSweepOffset.x;
        }
        return f + i;
    }

    private void dispatchOnSweep(boolean z) {
        OnSweepListener onSweepListener = this.mListener;
        if (onSweepListener != null) {
            onSweepListener.onSweep(this, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchRequestEdit() {
        OnSweepListener onSweepListener = this.mListener;
        if (onSweepListener != null) {
            onSweepListener.requestEdit(this);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setFocusable(z);
        requestLayout();
    }

    public void setSweepDrawable(Drawable drawable, int i) {
        Drawable drawable2 = this.mSweep;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mSweep);
        }
        if (drawable != null) {
            drawable.setCallback(this);
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
            drawable.setVisible(getVisibility() == 0, false);
            this.mSweep = drawable;
            drawable.setTint(i);
            drawable.getPadding(this.mSweepPadding);
        } else {
            this.mSweep = null;
        }
        invalidate();
    }

    public void setFollowAxis(int i) {
        this.mFollowAxis = i;
    }

    public void setLabelMinSize(int i) {
        this.mLabelMinSize = i;
        invalidateLabelTemplate();
    }

    public void setLabelTemplate(int i) {
        this.mLabelTemplateRes = i;
        invalidateLabelTemplate();
    }

    public void setLabelColor(int i) {
        this.mLabelColor = i;
        invalidateLabelTemplate();
    }

    private void invalidateLabelTemplate() {
        if (this.mLabelTemplateRes != 0) {
            CharSequence text = getResources().getText(this.mLabelTemplateRes);
            TextPaint textPaint = new TextPaint(1);
            textPaint.density = getResources().getDisplayMetrics().density;
            textPaint.setCompatibilityScaling(getResources().getCompatibilityInfo().applicationScale);
            textPaint.setColor(this.mLabelColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            this.mLabelTemplate = spannableStringBuilder;
            this.mLabelLayout = DynamicLayout.Builder.obtain(spannableStringBuilder, textPaint, 1024).setAlignment(Layout.Alignment.ALIGN_RIGHT).setIncludePad(false).setUseLineSpacingFromFallbacks(true).build();
            invalidateLabel();
        } else {
            this.mLabelTemplate = null;
            this.mLabelLayout = null;
        }
        invalidate();
        requestLayout();
    }

    private void invalidateLabel() {
        ChartAxis chartAxis;
        if (this.mLabelTemplate != null && (chartAxis = this.mAxis) != null) {
            this.mLabelValue = chartAxis.buildLabel(getResources(), this.mLabelTemplate, this.mValue);
            setContentDescription(this.mLabelTemplate);
            invalidateLabelOffset();
            invalidate();
            return;
        }
        this.mLabelValue = this.mValue;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:
        if (r0 < 0.0f) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void invalidateLabelOffset() {
        float f;
        float f2 = 0.0f;
        if (this.mFollowAxis == 1) {
            if (this.mValidAfterDynamic != null) {
                this.mLabelSize = Math.max(getLabelWidth(this), getLabelWidth(this.mValidAfterDynamic));
                f = getLabelTop(this.mValidAfterDynamic) - getLabelBottom(this);
            } else if (this.mValidBeforeDynamic != null) {
                this.mLabelSize = Math.max(getLabelWidth(this), getLabelWidth(this.mValidBeforeDynamic));
                float labelTop = getLabelTop(this) - getLabelBottom(this.mValidBeforeDynamic);
                if (labelTop < 0.0f) {
                    f = -labelTop;
                    f2 = f / 2.0f;
                }
            } else {
                this.mLabelSize = getLabelWidth(this);
            }
        }
        this.mLabelSize = Math.max(this.mLabelSize, this.mLabelMinSize);
        if (f2 != this.mLabelOffset) {
            this.mLabelOffset = f2;
            invalidate();
            ChartSweepView chartSweepView = this.mValidAfterDynamic;
            if (chartSweepView != null) {
                chartSweepView.invalidateLabelOffset();
            }
            ChartSweepView chartSweepView2 = this.mValidBeforeDynamic;
            if (chartSweepView2 == null) {
                return;
            }
            chartSweepView2.invalidateLabelOffset();
        }
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.setVisible(i == 0, false);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mSweep || super.verifyDrawable(drawable);
    }

    public ChartAxis getAxis() {
        return this.mAxis;
    }

    public void setValue(long j) {
        this.mValue = j;
        invalidateLabel();
    }

    public long getValue() {
        return this.mValue;
    }

    public long getLabelValue() {
        return this.mLabelValue;
    }

    public float getPoint() {
        if (isEnabled()) {
            return this.mAxis.convertToPoint(this.mValue);
        }
        return 0.0f;
    }

    public void setNeighborMargin(float f) {
        this.mNeighborMargin = f;
    }

    public void setSafeRegion(int i) {
        this.mSafeRegion = i;
    }

    public boolean isTouchCloserTo(MotionEvent motionEvent, ChartSweepView chartSweepView) {
        return chartSweepView.getTouchDistanceFromTarget(motionEvent) < getTouchDistanceFromTarget(motionEvent);
    }

    private float getTouchDistanceFromTarget(MotionEvent motionEvent) {
        if (this.mFollowAxis == 0) {
            return Math.abs(motionEvent.getX() - (getX() + getTargetInset()));
        }
        return Math.abs(motionEvent.getY() - (getY() + getTargetInset()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00f8, code lost:
        if (r12.getX() < r11.mLabelLayout.getWidth()) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00fa, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0126, code lost:
        if (r12.getY() < r11.mLabelLayout.getHeight()) goto L38;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        Rect parentContentRect;
        Rect computeClampRect;
        long convertToValue;
        if (!isEnabled()) {
            return false;
        }
        View view = (View) getParent();
        int action = motionEvent.getAction();
        if (action == 0) {
            if (this.mFollowAxis == 1) {
                z = motionEvent.getX() > ((float) (getWidth() - (this.mSweepPadding.right * 8)));
                if (this.mLabelLayout != null) {
                }
                z2 = false;
            } else {
                z = motionEvent.getY() > ((float) (getHeight() - (this.mSweepPadding.bottom * 8)));
                if (this.mLabelLayout != null) {
                }
                z2 = false;
            }
            MotionEvent copy = motionEvent.copy();
            copy.offsetLocation(getLeft(), getTop());
            for (ChartSweepView chartSweepView : this.mNeighbors) {
                if (isTouchCloserTo(copy, chartSweepView)) {
                    return false;
                }
            }
            if (!z) {
                if (z2) {
                    this.mTouchMode = 2;
                    return true;
                }
                this.mTouchMode = 0;
                return false;
            }
            if (this.mFollowAxis == 1) {
                this.mTrackingStart = getTop() - this.mMargins.top;
            } else {
                this.mTrackingStart = getLeft() - this.mMargins.left;
            }
            this.mTracking = motionEvent.copy();
            this.mTouchMode = 1;
            if (!view.isActivated()) {
                view.setActivated(true);
            }
            return true;
        } else if (action == 1) {
            int i = this.mTouchMode;
            if (i == 2) {
                performClick();
            } else if (i == 1) {
                this.mTrackingStart = 0.0f;
                this.mTracking = null;
                this.mValue = this.mLabelValue;
                dispatchOnSweep(true);
                setTranslationX(0.0f);
                setTranslationY(0.0f);
                requestLayout();
            }
            this.mTouchMode = 0;
            return true;
        } else if (action != 2) {
            return false;
        } else {
            if (this.mTouchMode == 2) {
                return true;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
            if (computeClampRect(getParentContentRect()).isEmpty()) {
                return true;
            }
            if (this.mFollowAxis == 1) {
                float constrain = MathUtils.constrain(this.mTrackingStart + (motionEvent.getRawY() - this.mTracking.getRawY()), computeClampRect.top, computeClampRect.bottom);
                setTranslationY(constrain - (getTop() - this.mMargins.top));
                convertToValue = this.mAxis.convertToValue(constrain - parentContentRect.top);
            } else {
                float constrain2 = MathUtils.constrain(this.mTrackingStart + (motionEvent.getRawX() - this.mTracking.getRawX()), computeClampRect.left, computeClampRect.right);
                setTranslationX(constrain2 - (getLeft() - this.mMargins.left));
                convertToValue = this.mAxis.convertToValue(constrain2 - parentContentRect.left);
            }
            setValue(convertToValue - (convertToValue % this.mDragInterval));
            dispatchOnSweep(false);
            return true;
        }
    }

    private Rect getParentContentRect() {
        View view = (View) getParent();
        return new Rect(view.getPaddingLeft(), view.getPaddingTop(), view.getWidth() - view.getPaddingRight(), view.getHeight() - view.getPaddingBottom());
    }

    private long getValidAfterDynamic() {
        ChartSweepView chartSweepView = this.mValidAfterDynamic;
        if (chartSweepView == null || !chartSweepView.isEnabled()) {
            return Long.MIN_VALUE;
        }
        return chartSweepView.getValue();
    }

    private long getValidBeforeDynamic() {
        ChartSweepView chartSweepView = this.mValidBeforeDynamic;
        if (chartSweepView == null || !chartSweepView.isEnabled()) {
            return Long.MAX_VALUE;
        }
        return chartSweepView.getValue();
    }

    private Rect computeClampRect(Rect rect) {
        Rect buildClampRect = buildClampRect(rect, this.mValidAfter, this.mValidBefore, 0.0f);
        if (!buildClampRect.intersect(buildClampRect(rect, getValidAfterDynamic(), getValidBeforeDynamic(), this.mNeighborMargin))) {
            buildClampRect.setEmpty();
        }
        return buildClampRect;
    }

    private Rect buildClampRect(Rect rect, long j, long j2, float f) {
        ChartAxis chartAxis = this.mAxis;
        boolean z = false;
        boolean z2 = (j == Long.MIN_VALUE || j == Long.MAX_VALUE) ? false : true;
        if (j2 != Long.MIN_VALUE && j2 != Long.MAX_VALUE) {
            z = true;
        }
        float convertToPoint = chartAxis.convertToPoint(j) + f;
        float convertToPoint2 = this.mAxis.convertToPoint(j2) - f;
        Rect rect2 = new Rect(rect);
        if (this.mFollowAxis == 1) {
            if (z) {
                rect2.bottom = rect2.top + ((int) convertToPoint2);
            }
            if (z2) {
                rect2.top = (int) (rect2.top + convertToPoint);
            }
        } else {
            if (z) {
                rect2.right = rect2.left + ((int) convertToPoint2);
            }
            if (z2) {
                rect2.left = (int) (rect2.left + convertToPoint);
            }
        }
        return rect2;
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mSweep.isStateful()) {
            this.mSweep.setState(getDrawableState());
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (isEnabled() && this.mLabelLayout != null) {
            int intrinsicHeight = this.mSweep.getIntrinsicHeight();
            int height = this.mLabelLayout.getHeight();
            Point point = this.mSweepOffset;
            point.x = 0;
            point.y = 0;
            point.y = (int) ((height / 2) - getTargetInset());
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), Math.max(intrinsicHeight, height));
        } else {
            Point point2 = this.mSweepOffset;
            point2.x = 0;
            point2.y = 0;
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), this.mSweep.getIntrinsicHeight());
        }
        if (this.mFollowAxis == 1) {
            int intrinsicHeight2 = this.mSweep.getIntrinsicHeight();
            Rect rect = this.mSweepPadding;
            int i3 = rect.top;
            int i4 = (intrinsicHeight2 - i3) - rect.bottom;
            Rect rect2 = this.mMargins;
            rect2.top = -(i3 + (i4 / 2));
            rect2.bottom = 0;
            rect2.left = -rect.left;
            rect2.right = rect.right;
        } else {
            int intrinsicWidth = this.mSweep.getIntrinsicWidth();
            Rect rect3 = this.mSweepPadding;
            int i5 = rect3.left;
            int i6 = (intrinsicWidth - i5) - rect3.right;
            Rect rect4 = this.mMargins;
            rect4.left = -(i5 + (i6 / 2));
            rect4.right = 0;
            rect4.top = -rect3.top;
            rect4.bottom = rect3.bottom;
        }
        this.mContentOffset.set(0, 0, 0, 0);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (this.mFollowAxis == 0) {
            int i7 = measuredWidth * 3;
            setMeasuredDimension(i7, measuredHeight);
            Rect rect5 = this.mContentOffset;
            rect5.left = (i7 - measuredWidth) / 2;
            int i8 = this.mSweepPadding.bottom * 2;
            rect5.bottom -= i8;
            this.mMargins.bottom += i8;
        } else {
            int i9 = measuredHeight * 2;
            setMeasuredDimension(measuredWidth, i9);
            this.mContentOffset.offset(0, (i9 - measuredHeight) / 2);
            int i10 = this.mSweepPadding.right * 2;
            this.mContentOffset.right -= i10;
            this.mMargins.right += i10;
        }
        Point point3 = this.mSweepOffset;
        Rect rect6 = this.mContentOffset;
        point3.offset(rect6.left, rect6.top);
        Rect rect7 = this.mMargins;
        Point point4 = this.mSweepOffset;
        rect7.offset(-point4.x, -point4.y);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        invalidateLabelOffset();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (!isEnabled() || this.mLabelLayout == null) {
            i = 0;
        } else {
            int save = canvas.save();
            Rect rect = this.mContentOffset;
            canvas.translate(rect.left + (this.mLabelSize - 1024.0f), rect.top + this.mLabelOffset);
            this.mLabelLayout.draw(canvas);
            canvas.restoreToCount(save);
            i = ((int) this.mLabelSize) + this.mSafeRegion;
        }
        if (this.mFollowAxis == 1) {
            Drawable drawable = this.mSweep;
            int i2 = this.mSweepOffset.y;
            drawable.setBounds(i, i2, width + this.mContentOffset.right, drawable.getIntrinsicHeight() + i2);
        } else {
            Drawable drawable2 = this.mSweep;
            int i3 = this.mSweepOffset.x;
            drawable2.setBounds(i3, i, drawable2.getIntrinsicWidth() + i3, height + this.mContentOffset.bottom);
        }
        this.mSweep.draw(canvas);
    }

    public static float getLabelTop(ChartSweepView chartSweepView) {
        return chartSweepView.getY() + chartSweepView.mContentOffset.top;
    }

    public static float getLabelBottom(ChartSweepView chartSweepView) {
        return getLabelTop(chartSweepView) + chartSweepView.mLabelLayout.getHeight();
    }

    public static float getLabelWidth(ChartSweepView chartSweepView) {
        return Layout.getDesiredWidth(chartSweepView.mLabelLayout.getText(), chartSweepView.mLabelLayout.getPaint());
    }
}
