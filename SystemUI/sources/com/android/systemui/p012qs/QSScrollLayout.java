package com.android.systemui.p012qs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewParent;
import android.widget.LinearLayout;
import androidx.core.widget.NestedScrollView;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.touch.OverScroll;
import com.android.systemui.p012qs.touch.SwipeDetector;

/* renamed from: com.android.systemui.qs.QSScrollLayout */
public class QSScrollLayout extends NestedScrollView {
    /* access modifiers changed from: private */
    public static final Property<QSScrollLayout, Float> CONTENT_TRANS_Y = new Property<QSScrollLayout, Float>(Float.class, "qsScrollLayoutContentTransY") {
        public Float get(QSScrollLayout qSScrollLayout) {
            return Float.valueOf(qSScrollLayout.mContentTranslationY);
        }

        public void set(QSScrollLayout qSScrollLayout, Float f) {
            qSScrollLayout.setContentTranslationY(f.floatValue());
        }
    };
    /* access modifiers changed from: private */
    public float mContentTranslationY;
    private final int mFooterHeight = getResources().getDimensionPixelSize(C1893R.dimen.qs_footer_height);
    private int mLastMotionY;
    private final OverScrollHelper mOverScrollHelper;
    private final SwipeDetector mSwipeDetector;
    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

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

    public QSScrollLayout(Context context, View... viewArr) {
        super(context);
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        for (View addView : viewArr) {
            linearLayout.addView(addView);
        }
        addView(linearLayout);
        setOverScrollMode(2);
        OverScrollHelper overScrollHelper = new OverScrollHelper();
        this.mOverScrollHelper = overScrollHelper;
        SwipeDetector swipeDetector = new SwipeDetector(context, (SwipeDetector.Listener) overScrollHelper, SwipeDetector.VERTICAL);
        this.mSwipeDetector = swipeDetector;
        swipeDetector.setDetectableScrollConditions(3, true);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!canScrollVertically(1) && !canScrollVertically(-1)) {
            return false;
        }
        this.mSwipeDetector.onTouchEvent(motionEvent);
        if (super.onInterceptTouchEvent(motionEvent) || this.mOverScrollHelper.isInOverScroll()) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!canScrollVertically(1) && !canScrollVertically(-1)) {
            return false;
        }
        this.mSwipeDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.translate(0.0f, this.mContentTranslationY);
        super.dispatchDraw(canvas);
        canvas.translate(0.0f, -this.mContentTranslationY);
    }

    public boolean shouldIntercept(MotionEvent motionEvent) {
        if (motionEvent.getY() > ((float) (getBottom() - this.mFooterHeight))) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mLastMotionY = (int) motionEvent.getY();
        } else if (motionEvent.getActionMasked() == 2) {
            if (this.mLastMotionY >= 0 && Math.abs(motionEvent.getY() - ((float) this.mLastMotionY)) > ((float) this.mTouchSlop) && canScrollVertically(1)) {
                requestParentDisallowInterceptTouchEvent(true);
                this.mLastMotionY = (int) motionEvent.getY();
                return true;
            }
        } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.mLastMotionY = -1;
            requestParentDisallowInterceptTouchEvent(false);
        }
        return false;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    /* access modifiers changed from: private */
    public void setContentTranslationY(float f) {
        this.mContentTranslationY = f;
        invalidate();
    }

    /* renamed from: com.android.systemui.qs.QSScrollLayout$OverScrollHelper */
    private class OverScrollHelper implements SwipeDetector.Listener {
        private float mFirstDisplacement;
        private boolean mIsInOverScroll;

        public void onDragStart(boolean z) {
        }

        private OverScrollHelper() {
            this.mFirstDisplacement = 0.0f;
        }

        public boolean onDrag(float f, float f2) {
            boolean z = this.mIsInOverScroll;
            boolean z2 = true;
            if ((QSScrollLayout.this.canScrollVertically(1) || f >= 0.0f) && (QSScrollLayout.this.canScrollVertically(-1) || f <= 0.0f)) {
                z2 = false;
            }
            this.mIsInOverScroll = z2;
            if (z && !z2) {
                reset();
            } else if (z2) {
                if (Float.compare(this.mFirstDisplacement, 0.0f) == 0) {
                    this.mFirstDisplacement = f;
                }
                QSScrollLayout.this.setContentTranslationY(getDampedOverScroll(f - this.mFirstDisplacement));
            }
            return this.mIsInOverScroll;
        }

        public void onDragEnd(float f, boolean z) {
            reset();
        }

        private void reset() {
            if (Float.compare(QSScrollLayout.this.mContentTranslationY, 0.0f) != 0) {
                ObjectAnimator.ofFloat(QSScrollLayout.this, QSScrollLayout.CONTENT_TRANS_Y, new float[]{0.0f}).setDuration(100).start();
            }
            this.mIsInOverScroll = false;
            this.mFirstDisplacement = 0.0f;
        }

        public boolean isInOverScroll() {
            return this.mIsInOverScroll;
        }

        private float getDampedOverScroll(float f) {
            return (float) OverScroll.dampedScroll(f, QSScrollLayout.this.getHeight());
        }
    }
}
