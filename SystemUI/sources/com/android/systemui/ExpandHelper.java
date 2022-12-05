package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.policy.ScrollAdapter;
import com.android.wm.shell.animation.FlingAnimationUtils;
/* loaded from: classes.dex */
public class ExpandHelper implements Gefingerpoken {
    private Callback mCallback;
    private Context mContext;
    private float mCurrentHeight;
    private View mEventSource;
    private boolean mExpanding;
    private FlingAnimationUtils mFlingAnimationUtils;
    private boolean mHasPopped;
    private float mInitialTouchFocusY;
    private float mInitialTouchSpan;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private int mLargeSize;
    private float mLastFocusY;
    private float mLastMotionY;
    private float mLastSpanY;
    private float mMaximumStretch;
    private float mNaturalHeight;
    private float mOldHeight;
    private boolean mOnlyMovements;
    private float mPullGestureMinXSpan;
    private ExpandableView mResizedView;
    private ScaleGestureDetector mSGD;
    private ObjectAnimator mScaleAnimation;
    private ViewScaler mScaler;
    private ScrollAdapter mScrollAdapter;
    private int mSmallSize;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private boolean mWatchingForPull;
    private int mExpansionStyle = 0;
    private boolean mEnabled = true;
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.android.systemui.ExpandHelper.1
        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (!ExpandHelper.this.mOnlyMovements) {
                ExpandHelper expandHelper = ExpandHelper.this;
                expandHelper.startExpanding(expandHelper.mResizedView, 4);
            }
            return ExpandHelper.this.mExpanding;
        }
    };
    private int mGravity = 48;
    private final float mSlopMultiplier = ViewConfiguration.getAmbiguousGestureMultiplier();

    /* loaded from: classes.dex */
    public interface Callback {
        boolean canChildBeExpanded(View view);

        void expansionStateChanged(boolean z);

        ExpandableView getChildAtPosition(float f, float f2);

        ExpandableView getChildAtRawPosition(float f, float f2);

        int getMaxExpandHeight(ExpandableView expandableView);

        void setExpansionCancelled(View view);

        void setUserExpandedChild(View view, boolean z);

        void setUserLockedChild(View view, boolean z);
    }

    @VisibleForTesting
    ObjectAnimator getScaleAnimation() {
        return this.mScaleAnimation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewScaler {
        ExpandableView mView;

        public ViewScaler() {
        }

        public void setView(ExpandableView expandableView) {
            this.mView = expandableView;
        }

        public void setHeight(float f) {
            this.mView.setActualHeight((int) f);
            ExpandHelper.this.mCurrentHeight = f;
        }

        public float getHeight() {
            return this.mView.getActualHeight();
        }

        public int getNaturalHeight() {
            return ExpandHelper.this.mCallback.getMaxExpandHeight(this.mView);
        }
    }

    public ExpandHelper(Context context, Callback callback, int i, int i2) {
        this.mSmallSize = i;
        this.mMaximumStretch = i * 2.0f;
        this.mLargeSize = i2;
        this.mContext = context;
        this.mCallback = callback;
        ViewScaler viewScaler = new ViewScaler();
        this.mScaler = viewScaler;
        this.mScaleAnimation = ObjectAnimator.ofFloat(viewScaler, "height", 0.0f);
        this.mPullGestureMinXSpan = this.mContext.getResources().getDimension(R$dimen.pull_span_min);
        this.mTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
        this.mSGD = new ScaleGestureDetector(context, this.mScaleGestureListener);
        this.mFlingAnimationUtils = new FlingAnimationUtils(this.mContext.getResources().getDisplayMetrics(), 0.3f);
    }

    @VisibleForTesting
    void updateExpansion() {
        float currentSpan = (this.mSGD.getCurrentSpan() - this.mInitialTouchSpan) * 1.0f;
        float focusY = (this.mSGD.getFocusY() - this.mInitialTouchFocusY) * 1.0f * (this.mGravity == 80 ? -1.0f : 1.0f);
        float abs = Math.abs(focusY) + Math.abs(currentSpan) + 1.0f;
        this.mScaler.setHeight(clamp(((focusY * Math.abs(focusY)) / abs) + ((currentSpan * Math.abs(currentSpan)) / abs) + this.mOldHeight));
        this.mLastFocusY = this.mSGD.getFocusY();
        this.mLastSpanY = this.mSGD.getCurrentSpan();
    }

    private float clamp(float f) {
        int i = this.mSmallSize;
        if (f < i) {
            f = i;
        }
        float f2 = this.mNaturalHeight;
        return f > f2 ? f2 : f;
    }

    private ExpandableView findView(float f, float f2) {
        View view = this.mEventSource;
        if (view != null) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            return this.mCallback.getChildAtRawPosition(f + iArr[0], f2 + iArr[1]);
        }
        return this.mCallback.getChildAtPosition(f, f2);
    }

    private boolean isInside(View view, float f, float f2) {
        if (view == null) {
            return false;
        }
        View view2 = this.mEventSource;
        if (view2 != null) {
            int[] iArr = new int[2];
            view2.getLocationOnScreen(iArr);
            f += iArr[0];
            f2 += iArr[1];
        }
        int[] iArr2 = new int[2];
        view.getLocationOnScreen(iArr2);
        float f3 = f - iArr2[0];
        float f4 = f2 - iArr2[1];
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        return ((f3 > ((float) view.getWidth()) ? 1 : (f3 == ((float) view.getWidth()) ? 0 : -1)) < 0) & ((f4 > ((float) view.getHeight()) ? 1 : (f4 == ((float) view.getHeight()) ? 0 : -1)) < 0);
    }

    public void setEventSource(View view) {
        this.mEventSource = view;
    }

    public void setScrollAdapter(ScrollAdapter scrollAdapter) {
        this.mScrollAdapter = scrollAdapter;
    }

    private float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return this.mTouchSlop * this.mSlopMultiplier;
        }
        return this.mTouchSlop;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0054, code lost:
        if (r0 != 3) goto L20;
     */
    @Override // com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (!isEnabled()) {
            return false;
        }
        trackVelocity(motionEvent);
        int action = motionEvent.getAction();
        this.mSGD.onTouchEvent(motionEvent);
        int focusX = (int) this.mSGD.getFocusX();
        float focusY = (int) this.mSGD.getFocusY();
        this.mInitialTouchFocusY = focusY;
        float currentSpan = this.mSGD.getCurrentSpan();
        this.mInitialTouchSpan = currentSpan;
        this.mLastFocusY = this.mInitialTouchFocusY;
        this.mLastSpanY = currentSpan;
        boolean z2 = true;
        if (this.mExpanding) {
            this.mLastMotionY = motionEvent.getRawY();
            maybeRecycleVelocityTracker(motionEvent);
            return true;
        } else if (action == 2 && (this.mExpansionStyle & 1) != 0) {
            return true;
        } else {
            int i = action & 255;
            if (i == 0) {
                ScrollAdapter scrollAdapter = this.mScrollAdapter;
                if (scrollAdapter == null || !isInside(scrollAdapter.getHostView(), focusX, focusY) || !this.mScrollAdapter.isScrolledToTop()) {
                    z2 = false;
                }
                this.mWatchingForPull = z2;
                ExpandableView findView = findView(focusX, focusY);
                this.mResizedView = findView;
                if (findView != null && !this.mCallback.canChildBeExpanded(findView)) {
                    this.mResizedView = null;
                    this.mWatchingForPull = false;
                }
                this.mInitialTouchY = motionEvent.getRawY();
                this.mInitialTouchX = motionEvent.getRawX();
            } else {
                if (i != 1) {
                    if (i == 2) {
                        float currentSpanX = this.mSGD.getCurrentSpanX();
                        if (currentSpanX > this.mPullGestureMinXSpan && currentSpanX > this.mSGD.getCurrentSpanY() && !this.mExpanding) {
                            startExpanding(this.mResizedView, 2);
                            this.mWatchingForPull = false;
                        }
                        if (this.mWatchingForPull) {
                            float rawY = motionEvent.getRawY() - this.mInitialTouchY;
                            float rawX = motionEvent.getRawX() - this.mInitialTouchX;
                            if (rawY > getTouchSlop(motionEvent) && rawY > Math.abs(rawX)) {
                                this.mWatchingForPull = false;
                                ExpandableView expandableView = this.mResizedView;
                                if (expandableView != null && !isFullyExpanded(expandableView) && startExpanding(this.mResizedView, 1)) {
                                    this.mLastMotionY = motionEvent.getRawY();
                                    this.mInitialTouchY = motionEvent.getRawY();
                                    this.mHasPopped = false;
                                }
                            }
                        }
                    }
                }
                if (motionEvent.getActionMasked() == 3) {
                    z = true;
                }
                finishExpanding(z, getCurrentVelocity());
                clearView();
            }
            this.mLastMotionY = motionEvent.getRawY();
            maybeRecycleVelocityTracker(motionEvent);
            return this.mExpanding;
        }
    }

    private void trackVelocity(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 2) {
                return;
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            return;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private void maybeRecycleVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker != null) {
            if (motionEvent.getActionMasked() != 3 && motionEvent.getActionMasked() != 1) {
                return;
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private float getCurrentVelocity() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
            return this.mVelocityTracker.getYVelocity();
        }
        return 0.0f;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    private boolean isEnabled() {
        return this.mEnabled;
    }

    private boolean isFullyExpanded(ExpandableView expandableView) {
        return expandableView.getIntrinsicHeight() == expandableView.getMaxContentHeight() && (!expandableView.isSummaryWithChildren() || expandableView.areChildrenExpanded());
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnabled() || this.mExpanding) {
            trackVelocity(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            this.mSGD.onTouchEvent(motionEvent);
            int focusX = (int) this.mSGD.getFocusX();
            int focusY = (int) this.mSGD.getFocusY();
            if (this.mOnlyMovements) {
                this.mLastMotionY = motionEvent.getRawY();
                return false;
            }
            if (actionMasked == 0) {
                ScrollAdapter scrollAdapter = this.mScrollAdapter;
                this.mWatchingForPull = scrollAdapter != null && isInside(scrollAdapter.getHostView(), (float) focusX, (float) focusY);
                this.mResizedView = findView(focusX, focusY);
                this.mInitialTouchX = motionEvent.getRawX();
                this.mInitialTouchY = motionEvent.getRawY();
            } else {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        if (this.mWatchingForPull) {
                            float rawY = motionEvent.getRawY() - this.mInitialTouchY;
                            float rawX = motionEvent.getRawX() - this.mInitialTouchX;
                            if (rawY > getTouchSlop(motionEvent) && rawY > Math.abs(rawX)) {
                                this.mWatchingForPull = false;
                                ExpandableView expandableView = this.mResizedView;
                                if (expandableView != null && !isFullyExpanded(expandableView) && startExpanding(this.mResizedView, 1)) {
                                    this.mInitialTouchY = motionEvent.getRawY();
                                    this.mLastMotionY = motionEvent.getRawY();
                                    this.mHasPopped = false;
                                }
                            }
                        }
                        boolean z = this.mExpanding;
                        if (z && (this.mExpansionStyle & 1) != 0) {
                            float rawY2 = (motionEvent.getRawY() - this.mLastMotionY) + this.mCurrentHeight;
                            float clamp = clamp(rawY2);
                            boolean z2 = rawY2 > this.mNaturalHeight;
                            if (rawY2 < this.mSmallSize) {
                                z2 = true;
                            }
                            if (!this.mHasPopped) {
                                View view = this.mEventSource;
                                if (view != null) {
                                    view.performHapticFeedback(1);
                                }
                                this.mHasPopped = true;
                            }
                            this.mScaler.setHeight(clamp);
                            this.mLastMotionY = motionEvent.getRawY();
                            if (z2) {
                                this.mCallback.expansionStateChanged(false);
                            } else {
                                this.mCallback.expansionStateChanged(true);
                            }
                            return true;
                        } else if (z) {
                            updateExpansion();
                            this.mLastMotionY = motionEvent.getRawY();
                            return true;
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 5 || actionMasked == 6) {
                            this.mInitialTouchY += this.mSGD.getFocusY() - this.mLastFocusY;
                            this.mInitialTouchSpan += this.mSGD.getCurrentSpan() - this.mLastSpanY;
                        }
                    }
                }
                finishExpanding(!isEnabled() || motionEvent.getActionMasked() == 3, getCurrentVelocity());
                clearView();
            }
            this.mLastMotionY = motionEvent.getRawY();
            maybeRecycleVelocityTracker(motionEvent);
            return this.mResizedView != null;
        }
        return false;
    }

    @VisibleForTesting
    boolean startExpanding(ExpandableView expandableView, int i) {
        if (!(expandableView instanceof ExpandableNotificationRow)) {
            return false;
        }
        this.mExpansionStyle = i;
        if (this.mExpanding && expandableView == this.mResizedView) {
            return true;
        }
        this.mExpanding = true;
        this.mCallback.expansionStateChanged(true);
        this.mCallback.setUserLockedChild(expandableView, true);
        this.mScaler.setView(expandableView);
        float height = this.mScaler.getHeight();
        this.mOldHeight = height;
        this.mCurrentHeight = height;
        if (this.mCallback.canChildBeExpanded(expandableView)) {
            this.mNaturalHeight = this.mScaler.getNaturalHeight();
            this.mSmallSize = expandableView.getCollapsedHeight();
        } else {
            this.mNaturalHeight = this.mOldHeight;
        }
        return true;
    }

    @VisibleForTesting
    void finishExpanding(boolean z, float f) {
        finishExpanding(z, f, true);
    }

    private void finishExpanding(boolean z, float f, boolean z2) {
        final boolean z3;
        if (!this.mExpanding) {
            return;
        }
        float height = this.mScaler.getHeight();
        float f2 = this.mOldHeight;
        int i = this.mSmallSize;
        boolean z4 = true;
        boolean z5 = f2 == ((float) i);
        if (!z) {
            z3 = (!z5 ? height >= f2 || f > 0.0f : height > f2 && f >= 0.0f) | (this.mNaturalHeight == ((float) i));
        } else {
            z3 = !z5;
        }
        if (this.mScaleAnimation.isRunning()) {
            this.mScaleAnimation.cancel();
        }
        this.mCallback.expansionStateChanged(false);
        int naturalHeight = this.mScaler.getNaturalHeight();
        if (!z3) {
            naturalHeight = this.mSmallSize;
        }
        float f3 = naturalHeight;
        int i2 = (f3 > height ? 1 : (f3 == height ? 0 : -1));
        if (i2 != 0 && this.mEnabled && z2) {
            this.mScaleAnimation.setFloatValues(f3);
            this.mScaleAnimation.setupStartValues();
            final ExpandableView expandableView = this.mResizedView;
            this.mScaleAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.ExpandHelper.2
                public boolean mCancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        ExpandHelper.this.mCallback.setUserExpandedChild(expandableView, z3);
                        if (!ExpandHelper.this.mExpanding) {
                            ExpandHelper.this.mScaler.setView(null);
                        }
                    } else {
                        ExpandHelper.this.mCallback.setExpansionCancelled(expandableView);
                    }
                    ExpandHelper.this.mCallback.setUserLockedChild(expandableView, false);
                    ExpandHelper.this.mScaleAnimation.removeListener(this);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }
            });
            if (f < 0.0f) {
                z4 = false;
            }
            if (z3 != z4) {
                f = 0.0f;
            }
            this.mFlingAnimationUtils.apply(this.mScaleAnimation, height, f3, f);
            this.mScaleAnimation.start();
        } else {
            if (i2 != 0) {
                this.mScaler.setHeight(f3);
            }
            this.mCallback.setUserExpandedChild(this.mResizedView, z3);
            this.mCallback.setUserLockedChild(this.mResizedView, false);
            this.mScaler.setView(null);
        }
        this.mExpanding = false;
        this.mExpansionStyle = 0;
    }

    private void clearView() {
        this.mResizedView = null;
    }

    public void cancelImmediately() {
        cancel(false);
    }

    public void cancel() {
        cancel(true);
    }

    private void cancel(boolean z) {
        finishExpanding(true, 0.0f, z);
        clearView();
        this.mSGD = new ScaleGestureDetector(this.mContext, this.mScaleGestureListener);
    }

    public void onlyObserveMovements(boolean z) {
        this.mOnlyMovements = z;
    }
}
