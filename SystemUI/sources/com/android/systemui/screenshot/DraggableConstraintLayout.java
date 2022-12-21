package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOverlay;
import android.view.ViewTreeObserver;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.C1893R;

public class DraggableConstraintLayout extends ConstraintLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    private static final float VELOCITY_DP_PER_MS = 1.0f;
    /* access modifiers changed from: private */
    public View mActionsContainer;
    /* access modifiers changed from: private */
    public View mActionsContainerBackground;
    /* access modifiers changed from: private */
    public SwipeDismissCallbacks mCallbacks;
    private final DisplayMetrics mDisplayMetrics;
    private final GestureDetector mSwipeDetector;
    private final SwipeDismissHandler mSwipeDismissHandler;

    public interface SwipeDismissCallbacks {
        void onDismissComplete() {
        }

        void onInteraction() {
        }

        void onSwipeDismissInitiated(Animator animator) {
        }
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public DraggableConstraintLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        this.mContext.getDisplay().getRealMetrics(displayMetrics);
        SwipeDismissHandler swipeDismissHandler = new SwipeDismissHandler(this.mContext, this);
        this.mSwipeDismissHandler = swipeDismissHandler;
        setOnTouchListener(swipeDismissHandler);
        GestureDetector gestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() {
            final Rect mActionsRect = new Rect();

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                DraggableConstraintLayout.this.mActionsContainer.getBoundsOnScreen(this.mActionsRect);
                return !this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !DraggableConstraintLayout.this.mActionsContainer.canScrollHorizontally((int) f);
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
    }

    public void setCallbacks(SwipeDismissCallbacks swipeDismissCallbacks) {
        this.mCallbacks = swipeDismissCallbacks;
    }

    public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
        SwipeDismissCallbacks swipeDismissCallbacks = this.mCallbacks;
        if (swipeDismissCallbacks != null) {
            swipeDismissCallbacks.onInteraction();
        }
        return super.onInterceptHoverEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mActionsContainer = findViewById(C1893R.C1897id.actions_container);
        this.mActionsContainerBackground = findViewById(C1893R.C1897id.actions_container_background);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mSwipeDismissHandler.onTouch(this, motionEvent);
        }
        return this.mSwipeDetector.onTouchEvent(motionEvent);
    }

    public void cancelDismissal() {
        this.mSwipeDismissHandler.cancel();
    }

    public boolean isDismissing() {
        return this.mSwipeDismissHandler.isDismissing();
    }

    public void dismiss() {
        this.mSwipeDismissHandler.dismiss();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region region = new Region();
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getGlobalVisibleRect(rect);
            region.op(rect, Region.Op.UNION);
        }
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(region);
    }

    private class SwipeDismissHandler implements View.OnTouchListener {
        private static final float DISMISS_DISTANCE_THRESHOLD_DP = 20.0f;
        private static final String TAG = "SwipeDismissHandler";
        /* access modifiers changed from: private */
        public int mDirectionX;
        /* access modifiers changed from: private */
        public ValueAnimator mDismissAnimation;
        private final DisplayMetrics mDisplayMetrics;
        private final GestureDetector mGestureDetector;
        /* access modifiers changed from: private */
        public float mPreviousX;
        /* access modifiers changed from: private */
        public float mStartX;
        /* access modifiers changed from: private */
        public final DraggableConstraintLayout mView;

        SwipeDismissHandler(Context context, DraggableConstraintLayout draggableConstraintLayout) {
            this.mView = draggableConstraintLayout;
            this.mGestureDetector = new GestureDetector(context, new SwipeDismissGestureListener());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mDisplayMetrics = displayMetrics;
            context.getDisplay().getRealMetrics(displayMetrics);
            SwipeDismissCallbacks unused = DraggableConstraintLayout.this.mCallbacks = new SwipeDismissCallbacks(DraggableConstraintLayout.this) {
            };
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
            DraggableConstraintLayout.this.mCallbacks.onInteraction();
            if (motionEvent.getActionMasked() == 0) {
                float rawX = motionEvent.getRawX();
                this.mStartX = rawX;
                this.mPreviousX = rawX;
                return true;
            } else if (motionEvent.getActionMasked() != 1) {
                return onTouchEvent;
            } else {
                ValueAnimator valueAnimator = this.mDismissAnimation;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    return true;
                }
                if (isPastDismissThreshold()) {
                    dismiss();
                } else {
                    createSwipeReturnAnimation().start();
                }
                return true;
            }
        }

        class SwipeDismissGestureListener extends GestureDetector.SimpleOnGestureListener {
            SwipeDismissGestureListener() {
            }

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                SwipeDismissHandler.this.mView.setTranslationX(motionEvent2.getRawX() - SwipeDismissHandler.this.mStartX);
                int unused = SwipeDismissHandler.this.mDirectionX = motionEvent2.getRawX() < SwipeDismissHandler.this.mPreviousX ? -1 : 1;
                float unused2 = SwipeDismissHandler.this.mPreviousX = motionEvent2.getRawX();
                return true;
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (SwipeDismissHandler.this.mView.getTranslationX() * f <= 0.0f) {
                    return false;
                }
                if (SwipeDismissHandler.this.mDismissAnimation != null && SwipeDismissHandler.this.mDismissAnimation.isRunning()) {
                    return false;
                }
                ValueAnimator access$700 = SwipeDismissHandler.this.createSwipeDismissAnimation(f / 1000.0f);
                DraggableConstraintLayout.this.mCallbacks.onSwipeDismissInitiated(access$700);
                SwipeDismissHandler.this.dismiss(access$700);
                return true;
            }
        }

        private boolean isPastDismissThreshold() {
            float translationX = this.mView.getTranslationX();
            if (((float) this.mDirectionX) * translationX <= 0.0f || Math.abs(translationX) < FloatingWindowUtil.dpToPx(this.mDisplayMetrics, DISMISS_DISTANCE_THRESHOLD_DP)) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean isDismissing() {
            ValueAnimator valueAnimator = this.mDismissAnimation;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            if (isDismissing()) {
                this.mDismissAnimation.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void dismiss() {
            ValueAnimator createSwipeDismissAnimation = createSwipeDismissAnimation(FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 1.0f));
            DraggableConstraintLayout.this.mCallbacks.onSwipeDismissInitiated(createSwipeDismissAnimation);
            dismiss(createSwipeDismissAnimation);
        }

        /* access modifiers changed from: private */
        public void dismiss(ValueAnimator valueAnimator) {
            this.mDismissAnimation = valueAnimator;
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                private boolean mCancelled;

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!this.mCancelled) {
                        DraggableConstraintLayout.this.mCallbacks.onDismissComplete();
                    }
                }
            });
            this.mDismissAnimation.start();
        }

        /* access modifiers changed from: private */
        public ValueAnimator createSwipeDismissAnimation(float f) {
            int i;
            float min = Math.min(3.0f, Math.max(1.0f, f));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            float translationX = this.mView.getTranslationX();
            int layoutDirection = this.mView.getContext().getResources().getConfiguration().getLayoutDirection();
            int i2 = (translationX > 0.0f ? 1 : (translationX == 0.0f ? 0 : -1));
            if (i2 > 0 || (i2 == 0 && layoutDirection == 1)) {
                i = this.mDisplayMetrics.widthPixels;
            } else {
                i = DraggableConstraintLayout.this.mActionsContainerBackground.getRight() * -1;
            }
            float f2 = (float) i;
            float abs = Math.abs(f2 - translationX);
            ofFloat.addUpdateListener(new C2435xa3d759b9(this, translationX, f2));
            ofFloat.setDuration((long) (abs / Math.abs(min)));
            return ofFloat;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$createSwipeDismissAnimation$0$com-android-systemui-screenshot-DraggableConstraintLayout$SwipeDismissHandler */
        public /* synthetic */ void mo37322x80635606(float f, float f2, ValueAnimator valueAnimator) {
            this.mView.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
            this.mView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
        }

        private ValueAnimator createSwipeReturnAnimation() {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.addUpdateListener(new C2436xa3d759ba(this, this.mView.getTranslationX(), 0.0f));
            return ofFloat;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$createSwipeReturnAnimation$1$com-android-systemui-screenshot-DraggableConstraintLayout$SwipeDismissHandler */
        public /* synthetic */ void mo37323xb69fa77f(float f, float f2, ValueAnimator valueAnimator) {
            this.mView.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
        }
    }
}
