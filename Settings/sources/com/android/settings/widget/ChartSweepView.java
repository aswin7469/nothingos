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
import android.view.MotionEvent;
import android.view.View;
import com.android.settings.R$drawable;
import com.android.settings.R$styleable;

public class ChartSweepView extends View {
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

    private void dispatchOnSweep(boolean z) {
    }

    /* access modifiers changed from: private */
    public void dispatchRequestEdit() {
    }

    public void addOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    public ChartAxis getAxis() {
        return null;
    }

    public void removeOnLayoutChangeListener(View.OnLayoutChangeListener onLayoutChangeListener) {
    }

    public ChartSweepView(Context context) {
        this(context, (AttributeSet) null);
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
        this.mDragInterval = 1;
        this.mNeighbors = new ChartSweepView[0];
        this.mClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                ChartSweepView.this.dispatchRequestEdit();
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ChartSweepView, i, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.ChartSweepView_labelColor, -16776961);
        setSweepDrawable(obtainStyledAttributes.getDrawable(R$styleable.ChartSweepView_sweepDrawable), color);
        setFollowAxis(obtainStyledAttributes.getInt(R$styleable.ChartSweepView_followAxis, -1));
        setNeighborMargin((float) obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_neighborMargin, 0));
        setSafeRegion(obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_safeRegion, 0));
        setLabelMinSize(obtainStyledAttributes.getDimensionPixelSize(R$styleable.ChartSweepView_labelSize, 0));
        setLabelTemplate(obtainStyledAttributes.getResourceId(R$styleable.ChartSweepView_labelTemplate, 0));
        setLabelColor(color);
        setBackgroundResource(R$drawable.data_usage_sweep_background);
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
            f = ((float) i2) + (((float) ((intrinsicHeight - i2) - rect.bottom)) / 2.0f);
            i = this.mSweepOffset.y;
        } else {
            int intrinsicWidth = this.mSweep.getIntrinsicWidth();
            Rect rect2 = this.mSweepPadding;
            int i3 = rect2.left;
            f = ((float) i3) + (((float) ((intrinsicWidth - i3) - rect2.right)) / 2.0f);
            i = this.mSweepOffset.x;
        }
        return f + ((float) i);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setFocusable(z);
        requestLayout();
    }

    public void setSweepDrawable(Drawable drawable, int i) {
        Drawable drawable2 = this.mSweep;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
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
        SpannableStringBuilder spannableStringBuilder = this.mLabelTemplate;
        this.mLabelValue = this.mValue;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0029, code lost:
        if (r0 < 0.0f) goto L_0x002b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invalidateLabelOffset() {
        /*
            r4 = this;
            int r0 = r4.mFollowAxis
            r1 = 0
            r2 = 1
            if (r0 != r2) goto L_0x0059
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            r2 = 1073741824(0x40000000, float:2.0)
            if (r0 == 0) goto L_0x002e
            float r0 = getLabelWidth(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidAfterDynamic
            float r3 = getLabelWidth(r3)
            float r0 = java.lang.Math.max(r0, r3)
            r4.mLabelSize = r0
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            float r0 = getLabelTop(r0)
            float r3 = getLabelBottom(r4)
            float r0 = r0 - r3
            int r3 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0059
        L_0x002b:
            float r1 = r0 / r2
            goto L_0x0059
        L_0x002e:
            com.android.settings.widget.ChartSweepView r0 = r4.mValidBeforeDynamic
            if (r0 == 0) goto L_0x0053
            float r0 = getLabelWidth(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidBeforeDynamic
            float r3 = getLabelWidth(r3)
            float r0 = java.lang.Math.max(r0, r3)
            r4.mLabelSize = r0
            float r0 = getLabelTop(r4)
            com.android.settings.widget.ChartSweepView r3 = r4.mValidBeforeDynamic
            float r3 = getLabelBottom(r3)
            float r0 = r0 - r3
            int r3 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0059
            float r0 = -r0
            goto L_0x002b
        L_0x0053:
            float r0 = getLabelWidth(r4)
            r4.mLabelSize = r0
        L_0x0059:
            float r0 = r4.mLabelSize
            int r2 = r4.mLabelMinSize
            float r2 = (float) r2
            float r0 = java.lang.Math.max(r0, r2)
            r4.mLabelSize = r0
            float r0 = r4.mLabelOffset
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 == 0) goto L_0x007d
            r4.mLabelOffset = r1
            r4.invalidate()
            com.android.settings.widget.ChartSweepView r0 = r4.mValidAfterDynamic
            if (r0 == 0) goto L_0x0076
            r0.invalidateLabelOffset()
        L_0x0076:
            com.android.settings.widget.ChartSweepView r4 = r4.mValidBeforeDynamic
            if (r4 == 0) goto L_0x007d
            r4.invalidateLabelOffset()
        L_0x007d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.ChartSweepView.invalidateLabelOffset():void");
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        Drawable drawable = this.mSweep;
        if (drawable != null) {
            drawable.setVisible(i == 0, false);
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mSweep || super.verifyDrawable(drawable);
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
        if (!isEnabled()) {
            return 0.0f;
        }
        throw null;
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

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00db, code lost:
        if (r12.getX() < ((float) r11.mLabelLayout.getWidth())) goto L_0x0108;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0106, code lost:
        if (r12.getY() < ((float) r11.mLabelLayout.getHeight())) goto L_0x0108;
     */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x015f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
            r11 = this;
            boolean r0 = r11.isEnabled()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            android.view.ViewParent r0 = r11.getParent()
            android.view.View r0 = (android.view.View) r0
            int r2 = r12.getAction()
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x00af
            r0 = 0
            if (r2 == r4) goto L_0x008d
            if (r2 == r3) goto L_0x001c
            return r1
        L_0x001c:
            int r1 = r11.mTouchMode
            if (r1 != r3) goto L_0x0021
            return r4
        L_0x0021:
            android.view.ViewParent r1 = r11.getParent()
            r1.requestDisallowInterceptTouchEvent(r4)
            android.graphics.Rect r1 = r11.getParentContentRect()
            android.graphics.Rect r2 = r11.computeClampRect(r1)
            boolean r3 = r2.isEmpty()
            if (r3 == 0) goto L_0x0037
            return r4
        L_0x0037:
            int r3 = r11.mFollowAxis
            if (r3 != r4) goto L_0x0064
            int r3 = r11.getTop()
            android.graphics.Rect r4 = r11.mMargins
            int r4 = r4.top
            int r3 = r3 - r4
            float r3 = (float) r3
            float r4 = r11.mTrackingStart
            float r12 = r12.getRawY()
            android.view.MotionEvent r5 = r11.mTracking
            float r5 = r5.getRawY()
            float r12 = r12 - r5
            float r4 = r4 + r12
            int r12 = r2.top
            float r12 = (float) r12
            int r2 = r2.bottom
            float r2 = (float) r2
            float r12 = android.util.MathUtils.constrain(r4, r12, r2)
            float r12 = r12 - r3
            r11.setTranslationY(r12)
            int r11 = r1.top
            throw r0
        L_0x0064:
            int r3 = r11.getLeft()
            android.graphics.Rect r4 = r11.mMargins
            int r4 = r4.left
            int r3 = r3 - r4
            float r3 = (float) r3
            float r4 = r11.mTrackingStart
            float r12 = r12.getRawX()
            android.view.MotionEvent r5 = r11.mTracking
            float r5 = r5.getRawX()
            float r12 = r12 - r5
            float r4 = r4 + r12
            int r12 = r2.left
            float r12 = (float) r12
            int r2 = r2.right
            float r2 = (float) r2
            float r12 = android.util.MathUtils.constrain(r4, r12, r2)
            float r12 = r12 - r3
            r11.setTranslationX(r12)
            int r11 = r1.left
            throw r0
        L_0x008d:
            int r12 = r11.mTouchMode
            if (r12 != r3) goto L_0x0095
            r11.performClick()
            goto L_0x00ac
        L_0x0095:
            if (r12 != r4) goto L_0x00ac
            r12 = 0
            r11.mTrackingStart = r12
            r11.mTracking = r0
            long r2 = r11.mLabelValue
            r11.mValue = r2
            r11.dispatchOnSweep(r4)
            r11.setTranslationX(r12)
            r11.setTranslationY(r12)
            r11.requestLayout()
        L_0x00ac:
            r11.mTouchMode = r1
            return r4
        L_0x00af:
            int r2 = r11.mFollowAxis
            if (r2 != r4) goto L_0x00de
            float r2 = r12.getX()
            int r5 = r11.getWidth()
            android.graphics.Rect r6 = r11.mSweepPadding
            int r6 = r6.right
            int r6 = r6 * 8
            int r5 = r5 - r6
            float r5 = (float) r5
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 <= 0) goto L_0x00c9
            r2 = r4
            goto L_0x00ca
        L_0x00c9:
            r2 = r1
        L_0x00ca:
            android.text.DynamicLayout r5 = r11.mLabelLayout
            if (r5 == 0) goto L_0x010a
            float r5 = r12.getX()
            android.text.DynamicLayout r6 = r11.mLabelLayout
            int r6 = r6.getWidth()
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x010a
            goto L_0x0108
        L_0x00de:
            float r2 = r12.getY()
            int r5 = r11.getHeight()
            android.graphics.Rect r6 = r11.mSweepPadding
            int r6 = r6.bottom
            int r6 = r6 * 8
            int r5 = r5 - r6
            float r5 = (float) r5
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 <= 0) goto L_0x00f4
            r2 = r4
            goto L_0x00f5
        L_0x00f4:
            r2 = r1
        L_0x00f5:
            android.text.DynamicLayout r5 = r11.mLabelLayout
            if (r5 == 0) goto L_0x010a
            float r5 = r12.getY()
            android.text.DynamicLayout r6 = r11.mLabelLayout
            int r6 = r6.getHeight()
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x010a
        L_0x0108:
            r5 = r4
            goto L_0x010b
        L_0x010a:
            r5 = r1
        L_0x010b:
            android.view.MotionEvent r6 = r12.copy()
            int r7 = r11.getLeft()
            float r7 = (float) r7
            int r8 = r11.getTop()
            float r8 = (float) r8
            r6.offsetLocation(r7, r8)
            com.android.settings.widget.ChartSweepView[] r7 = r11.mNeighbors
            int r8 = r7.length
            r9 = r1
        L_0x0120:
            if (r9 >= r8) goto L_0x012e
            r10 = r7[r9]
            boolean r10 = r11.isTouchCloserTo(r6, r10)
            if (r10 == 0) goto L_0x012b
            return r1
        L_0x012b:
            int r9 = r9 + 1
            goto L_0x0120
        L_0x012e:
            if (r2 == 0) goto L_0x015f
            int r1 = r11.mFollowAxis
            if (r1 != r4) goto L_0x0141
            int r1 = r11.getTop()
            android.graphics.Rect r2 = r11.mMargins
            int r2 = r2.top
            int r1 = r1 - r2
            float r1 = (float) r1
            r11.mTrackingStart = r1
            goto L_0x014d
        L_0x0141:
            int r1 = r11.getLeft()
            android.graphics.Rect r2 = r11.mMargins
            int r2 = r2.left
            int r1 = r1 - r2
            float r1 = (float) r1
            r11.mTrackingStart = r1
        L_0x014d:
            android.view.MotionEvent r12 = r12.copy()
            r11.mTracking = r12
            r11.mTouchMode = r4
            boolean r11 = r0.isActivated()
            if (r11 != 0) goto L_0x015e
            r0.setActivated(r4)
        L_0x015e:
            return r4
        L_0x015f:
            if (r5 == 0) goto L_0x0164
            r11.mTouchMode = r3
            return r4
        L_0x0164:
            r11.mTouchMode = r1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.widget.ChartSweepView.onTouchEvent(android.view.MotionEvent):boolean");
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
        if (j != Long.MIN_VALUE) {
            int i = (j > Long.MAX_VALUE ? 1 : (j == Long.MAX_VALUE ? 0 : -1));
        }
        if (j2 != Long.MIN_VALUE) {
            int i2 = (j2 > Long.MAX_VALUE ? 1 : (j2 == Long.MAX_VALUE ? 0 : -1));
        }
        throw null;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mSweep.isStateful()) {
            this.mSweep.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (!isEnabled() || this.mLabelLayout == null) {
            Point point = this.mSweepOffset;
            point.x = 0;
            point.y = 0;
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), this.mSweep.getIntrinsicHeight());
        } else {
            int intrinsicHeight = this.mSweep.getIntrinsicHeight();
            int height = this.mLabelLayout.getHeight();
            Point point2 = this.mSweepOffset;
            point2.x = 0;
            point2.y = 0;
            point2.y = (int) (((float) (height / 2)) - getTargetInset());
            setMeasuredDimension(this.mSweep.getIntrinsicWidth(), Math.max(intrinsicHeight, height));
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

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        invalidateLabelOffset();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (!isEnabled() || this.mLabelLayout == null) {
            i = 0;
        } else {
            int save = canvas.save();
            Rect rect = this.mContentOffset;
            canvas.translate(((float) rect.left) + (this.mLabelSize - 1024.0f), ((float) rect.top) + this.mLabelOffset);
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
        return chartSweepView.getY() + ((float) chartSweepView.mContentOffset.top);
    }

    public static float getLabelBottom(ChartSweepView chartSweepView) {
        return getLabelTop(chartSweepView) + ((float) chartSweepView.mLabelLayout.getHeight());
    }

    public static float getLabelWidth(ChartSweepView chartSweepView) {
        return Layout.getDesiredWidth(chartSweepView.mLabelLayout.getText(), chartSweepView.mLabelLayout.getPaint());
    }
}
