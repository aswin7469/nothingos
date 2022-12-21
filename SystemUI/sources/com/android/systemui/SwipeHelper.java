package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.function.Consumer;

public class SwipeHelper implements Gefingerpoken {
    private static final boolean CONSTRAIN_SWIPE = true;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_INVALIDATE = false;
    private static final int DEFAULT_ESCAPE_ANIMATION_DURATION = 200;
    private static final boolean DISMISS_IF_SWIPED_FAR_ENOUGH = true;
    private static final boolean FADE_OUT_DURING_SWIPE = true;
    private static final int MAX_DISMISS_VELOCITY = 4000;
    private static final int MAX_ESCAPE_ANIMATION_DURATION = 400;
    static final float MAX_SCROLL_SIZE_FRACTION = 0.3f;
    private static final boolean SLOW_ANIMATIONS = false;
    private static final int SNAP_ANIM_LEN = 150;
    public static final float SWIPED_FAR_ENOUGH_SIZE_FRACTION = 0.6f;
    private static final float SWIPE_ESCAPE_VELOCITY = 500.0f;
    static final float SWIPE_PROGRESS_FADE_END = 0.5f;
    static final String TAG = "com.android.systemui.SwipeHelper";

    /* renamed from: X */
    public static final int f290X = 0;

    /* renamed from: Y */
    public static final int f291Y = 1;
    /* access modifiers changed from: private */
    public final Callback mCallback;
    private boolean mCanCurrViewBeDimissed;
    private float mDensityScale;
    /* access modifiers changed from: private */
    public boolean mDisableHwLayers;
    /* access modifiers changed from: private */
    public final ArrayMap<View, Animator> mDismissPendingMap = new ArrayMap<>();
    /* access modifiers changed from: private */
    public final float[] mDownLocation = new float[2];
    private final boolean mFadeDependingOnAmountSwiped;
    private final FalsingManager mFalsingManager;
    private final int mFalsingThreshold;
    private final FeatureFlags mFeatureFlags;
    private final FlingAnimationUtils mFlingAnimationUtils;
    protected final Handler mHandler;
    private float mInitialTouchPos;
    private boolean mIsSwiping;
    /* access modifiers changed from: private */
    public boolean mLongPressSent;
    private final long mLongPressTimeout;
    private float mMaxSwipeProgress = 1.0f;
    private boolean mMenuRowIntercepting;
    private float mMinSwipeProgress = 0.0f;
    private float mPagingTouchSlop;
    private final Runnable mPerformLongPress = new Runnable() {
        private final int[] mViewOffset = new int[2];

        public void run() {
            if (SwipeHelper.this.mTouchedView != null && !SwipeHelper.this.mLongPressSent) {
                boolean unused = SwipeHelper.this.mLongPressSent = true;
                if (SwipeHelper.this.mTouchedView instanceof ExpandableNotificationRow) {
                    SwipeHelper.this.mTouchedView.getLocationOnScreen(this.mViewOffset);
                    int i = ((int) SwipeHelper.this.mDownLocation[0]) - this.mViewOffset[0];
                    int i2 = ((int) SwipeHelper.this.mDownLocation[1]) - this.mViewOffset[1];
                    SwipeHelper.this.mTouchedView.sendAccessibilityEvent(2);
                    ((ExpandableNotificationRow) SwipeHelper.this.mTouchedView).doLongClickCallback(i, i2);
                    SwipeHelper swipeHelper = SwipeHelper.this;
                    if (swipeHelper.isAvailableToDragAndDrop(swipeHelper.mTouchedView)) {
                        SwipeHelper.this.mCallback.onLongPressSent(SwipeHelper.this.mTouchedView);
                    }
                }
            }
        }
    };
    private float mPerpendicularInitialTouchPos;
    private final float mSlopMultiplier;
    /* access modifiers changed from: private */
    public boolean mSnappingChild;
    private final int mSwipeDirection;
    private boolean mTouchAboveFalsingThreshold;
    private int mTouchSlop;
    private float mTouchSlopMultiplier;
    /* access modifiers changed from: private */
    public View mTouchedView;
    private float mTranslation = 0.0f;
    private final VelocityTracker mVelocityTracker;

    /* access modifiers changed from: protected */
    public long getMaxEscapeAnimDuration() {
        return 400;
    }

    /* access modifiers changed from: protected */
    public float getUnscaledEscapeVelocity() {
        return SWIPE_ESCAPE_VELOCITY;
    }

    /* access modifiers changed from: protected */
    public boolean handleUpEvent(MotionEvent motionEvent, View view, float f, float f2) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onChildSnappedBack(View view, float f) {
    }

    /* access modifiers changed from: protected */
    public void onDismissChildWithAnimationFinished() {
    }

    public void onDownUpdate(View view, MotionEvent motionEvent) {
    }

    /* access modifiers changed from: protected */
    public void onMoveUpdate(View view, MotionEvent motionEvent, float f, float f2) {
    }

    /* access modifiers changed from: protected */
    public void onSnapChildWithAnimationFinished() {
    }

    /* access modifiers changed from: protected */
    public void prepareDismissAnimation(View view, Animator animator) {
    }

    /* access modifiers changed from: protected */
    public void prepareSnapBackAnimation(View view, Animator animator) {
    }

    public SwipeHelper(int i, Callback callback, Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags) {
        this.mCallback = callback;
        this.mHandler = new Handler();
        this.mSwipeDirection = i;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mPagingTouchSlop = (float) viewConfiguration.getScaledPagingTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mTouchSlopMultiplier = ViewConfiguration.getAmbiguousGestureMultiplier();
        this.mLongPressTimeout = (long) (((float) ViewConfiguration.getLongPressTimeout()) * 1.5f);
        this.mDensityScale = resources.getDisplayMetrics().density;
        this.mFalsingThreshold = resources.getDimensionPixelSize(C1893R.dimen.swipe_helper_falsing_threshold);
        this.mFadeDependingOnAmountSwiped = resources.getBoolean(C1893R.bool.config_fadeDependingOnAmountSwiped);
        this.mFalsingManager = falsingManager;
        this.mFeatureFlags = featureFlags;
        this.mFlingAnimationUtils = new FlingAnimationUtils(resources.getDisplayMetrics(), ((float) getMaxEscapeAnimDuration()) / 1000.0f);
    }

    public void setDensityScale(float f) {
        this.mDensityScale = f;
    }

    public void setPagingTouchSlop(float f) {
        this.mPagingTouchSlop = f;
    }

    public void setDisableHardwareLayers(boolean z) {
        this.mDisableHwLayers = z;
    }

    private float getPos(MotionEvent motionEvent) {
        return this.mSwipeDirection == 0 ? motionEvent.getX() : motionEvent.getY();
    }

    private float getPerpendicularPos(MotionEvent motionEvent) {
        return this.mSwipeDirection == 0 ? motionEvent.getY() : motionEvent.getX();
    }

    /* access modifiers changed from: protected */
    public float getTranslation(View view) {
        return this.mSwipeDirection == 0 ? view.getTranslationX() : view.getTranslationY();
    }

    private float getVelocity(VelocityTracker velocityTracker) {
        if (this.mSwipeDirection == 0) {
            return velocityTracker.getXVelocity();
        }
        return velocityTracker.getYVelocity();
    }

    /* access modifiers changed from: protected */
    public ObjectAnimator createTranslationAnimation(View view, float f) {
        return ObjectAnimator.ofFloat(view, this.mSwipeDirection == 0 ? View.TRANSLATION_X : View.TRANSLATION_Y, new float[]{f});
    }

    private float getPerpendicularVelocity(VelocityTracker velocityTracker) {
        if (this.mSwipeDirection == 0) {
            return velocityTracker.getYVelocity();
        }
        return velocityTracker.getXVelocity();
    }

    /* access modifiers changed from: protected */
    public Animator getViewTranslationAnimator(View view, float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ObjectAnimator createTranslationAnimation = createTranslationAnimation(view, f);
        if (animatorUpdateListener != null) {
            createTranslationAnimation.addUpdateListener(animatorUpdateListener);
        }
        return createTranslationAnimation;
    }

    /* access modifiers changed from: protected */
    public void setTranslation(View view, float f) {
        if (view != null) {
            if (this.mSwipeDirection == 0) {
                view.setTranslationX(f);
            } else {
                view.setTranslationY(f);
            }
        }
    }

    /* access modifiers changed from: protected */
    public float getSize(View view) {
        return (float) (this.mSwipeDirection == 0 ? view.getMeasuredWidth() : view.getMeasuredHeight());
    }

    public void setMinSwipeProgress(float f) {
        this.mMinSwipeProgress = f;
    }

    public void setMaxSwipeProgress(float f) {
        this.mMaxSwipeProgress = f;
    }

    private float getSwipeProgressForOffset(View view, float f) {
        return Math.min(Math.max(this.mMinSwipeProgress, Math.abs(f / getSize(view))), this.mMaxSwipeProgress);
    }

    private float getSwipeAlpha(float f) {
        if (this.mFadeDependingOnAmountSwiped) {
            return Math.max(1.0f - f, 0.0f);
        }
        return 1.0f - Math.max(0.0f, Math.min(1.0f, f / 0.5f));
    }

    /* access modifiers changed from: private */
    public void updateSwipeProgressFromOffset(View view, boolean z) {
        updateSwipeProgressFromOffset(view, z, getTranslation(view));
    }

    private void updateSwipeProgressFromOffset(View view, boolean z, float f) {
        float swipeProgressForOffset = getSwipeProgressForOffset(view, f);
        if (!this.mCallback.updateSwipeProgress(view, z, swipeProgressForOffset) && z) {
            if (!this.mDisableHwLayers) {
                if (swipeProgressForOffset == 0.0f || swipeProgressForOffset == 1.0f) {
                    view.setLayerType(0, (Paint) null);
                } else {
                    view.setLayerType(2, (Paint) null);
                }
            }
            view.setAlpha(getSwipeAlpha(swipeProgressForOffset));
        }
        invalidateGlobalRegion(view);
    }

    public static void invalidateGlobalRegion(View view) {
        invalidateGlobalRegion(view, new RectF((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom()));
    }

    public static void invalidateGlobalRegion(View view, RectF rectF) {
        while (view.getParent() != null && (view.getParent() instanceof View)) {
            view = (View) view.getParent();
            view.getMatrix().mapRect(rectF);
            view.invalidate((int) Math.floor((double) rectF.left), (int) Math.floor((double) rectF.top), (int) Math.ceil((double) rectF.right), (int) Math.ceil((double) rectF.bottom));
        }
    }

    public void cancelLongPress() {
        this.mHandler.removeCallbacks(this.mPerformLongPress);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0025, code lost:
        if (r0 != 3) goto L_0x0127;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r8) {
        /*
            r7 = this;
            android.view.View r0 = r7.mTouchedView
            boolean r1 = r0 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r1 == 0) goto L_0x0016
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r0
            com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin r0 = r0.getProvider()
            if (r0 == 0) goto L_0x0016
            android.view.View r1 = r7.mTouchedView
            boolean r0 = r0.onInterceptTouchEvent(r1, r8)
            r7.mMenuRowIntercepting = r0
        L_0x0016:
            int r0 = r8.getAction()
            r1 = 0
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L_0x00c9
            if (r0 == r2) goto L_0x00a6
            r4 = 2
            if (r0 == r4) goto L_0x0029
            r8 = 3
            if (r0 == r8) goto L_0x00a6
            goto L_0x0127
        L_0x0029:
            android.view.View r0 = r7.mTouchedView
            if (r0 == 0) goto L_0x0127
            boolean r0 = r7.mLongPressSent
            if (r0 != 0) goto L_0x0127
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            r0.addMovement(r8)
            float r0 = r7.getPos(r8)
            float r1 = r7.getPerpendicularPos(r8)
            float r5 = r7.mInitialTouchPos
            float r0 = r0 - r5
            float r5 = r7.mPerpendicularInitialTouchPos
            float r1 = r1 - r5
            int r5 = r8.getClassification()
            if (r5 != r2) goto L_0x0050
            float r5 = r7.mPagingTouchSlop
            float r6 = r7.mSlopMultiplier
            float r5 = r5 * r6
            goto L_0x0052
        L_0x0050:
            float r5 = r7.mPagingTouchSlop
        L_0x0052:
            float r6 = java.lang.Math.abs((float) r0)
            int r5 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x008c
            float r0 = java.lang.Math.abs((float) r0)
            float r1 = java.lang.Math.abs((float) r1)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x008c
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            android.view.View r1 = r7.mTouchedView
            boolean r0 = r0.canChildBeDragged(r1)
            if (r0 == 0) goto L_0x0087
            r7.mIsSwiping = r2
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            android.view.View r1 = r7.mTouchedView
            r0.onBeginDrag(r1)
            float r8 = r7.getPos(r8)
            r7.mInitialTouchPos = r8
            android.view.View r8 = r7.mTouchedView
            float r8 = r7.getTranslation(r8)
            r7.mTranslation = r8
        L_0x0087:
            r7.cancelLongPress()
            goto L_0x0127
        L_0x008c:
            int r8 = r8.getClassification()
            if (r8 != r4) goto L_0x0127
            android.os.Handler r8 = r7.mHandler
            java.lang.Runnable r0 = r7.mPerformLongPress
            boolean r8 = r8.hasCallbacks(r0)
            if (r8 == 0) goto L_0x0127
            r7.cancelLongPress()
            java.lang.Runnable r8 = r7.mPerformLongPress
            r8.run()
            goto L_0x0127
        L_0x00a6:
            boolean r8 = r7.mIsSwiping
            if (r8 != 0) goto L_0x00b5
            boolean r8 = r7.mLongPressSent
            if (r8 != 0) goto L_0x00b5
            boolean r8 = r7.mMenuRowIntercepting
            if (r8 == 0) goto L_0x00b3
            goto L_0x00b5
        L_0x00b3:
            r8 = r3
            goto L_0x00b6
        L_0x00b5:
            r8 = r2
        L_0x00b6:
            r7.mIsSwiping = r3
            r7.mTouchedView = r1
            r7.mLongPressSent = r3
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            r0.onLongPressSent(r1)
            r7.mMenuRowIntercepting = r3
            r7.cancelLongPress()
            if (r8 == 0) goto L_0x0127
            return r2
        L_0x00c9:
            r7.mTouchAboveFalsingThreshold = r3
            r7.mIsSwiping = r3
            r7.mSnappingChild = r3
            r7.mLongPressSent = r3
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            r0.onLongPressSent(r1)
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            r0.clear()
            r7.cancelLongPress()
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            android.view.View r0 = r0.getChildAtPosition(r8)
            r7.mTouchedView = r0
            if (r0 == 0) goto L_0x0127
            r7.onDownUpdate(r0, r8)
            com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
            android.view.View r1 = r7.mTouchedView
            boolean r0 = r0.canChildBeDismissed(r1)
            r7.mCanCurrViewBeDimissed = r0
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            r0.addMovement(r8)
            float r0 = r7.getPos(r8)
            r7.mInitialTouchPos = r0
            float r0 = r7.getPerpendicularPos(r8)
            r7.mPerpendicularInitialTouchPos = r0
            android.view.View r0 = r7.mTouchedView
            float r0 = r7.getTranslation(r0)
            r7.mTranslation = r0
            float[] r0 = r7.mDownLocation
            float r1 = r8.getRawX()
            r0[r3] = r1
            float[] r0 = r7.mDownLocation
            float r8 = r8.getRawY()
            r0[r2] = r8
            android.os.Handler r8 = r7.mHandler
            java.lang.Runnable r0 = r7.mPerformLongPress
            long r4 = r7.mLongPressTimeout
            r8.postDelayed(r0, r4)
        L_0x0127:
            boolean r8 = r7.mIsSwiping
            if (r8 != 0) goto L_0x0135
            boolean r8 = r7.mLongPressSent
            if (r8 != 0) goto L_0x0135
            boolean r7 = r7.mMenuRowIntercepting
            if (r7 == 0) goto L_0x0134
            goto L_0x0135
        L_0x0134:
            r2 = r3
        L_0x0135:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public void dismissChild(View view, float f, boolean z) {
        dismissChild(view, f, (Consumer<Boolean>) null, 0, z, 0, false);
    }

    public void dismissChild(final View view, float f, Consumer<Boolean> consumer, long j, boolean z, long j2, boolean z2) {
        float f2;
        long j3;
        View view2 = view;
        long j4 = j;
        final boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        boolean z3 = false;
        boolean z4 = view.getLayoutDirection() == 1;
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        boolean z5 = i == 0 && (getTranslation(view) == 0.0f || z2) && this.mSwipeDirection == 1;
        boolean z6 = i == 0 && (getTranslation(view) == 0.0f || z2) && z4;
        if ((Math.abs(f) > getEscapeVelocity() && f < 0.0f) || (getTranslation(view) < 0.0f && !z2)) {
            z3 = true;
        }
        if (z3 || z6 || z5) {
            f2 = -getTotalTranslationLength(view);
        } else {
            f2 = getTotalTranslationLength(view);
        }
        float f3 = f2;
        if (j2 == 0) {
            j3 = i != 0 ? Math.min(400, (long) ((int) ((Math.abs(f3 - getTranslation(view)) * 1000.0f) / Math.abs(f)))) : 200;
        } else {
            j3 = j2;
        }
        if (!this.mDisableHwLayers) {
            view.setLayerType(2, (Paint) null);
        }
        Animator viewTranslationAnimator = getViewTranslationAnimator(view, f3, new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeHelper.this.onTranslationUpdate(view, ((Float) valueAnimator.getAnimatedValue()).floatValue(), canChildBeDismissed);
            }
        });
        if (viewTranslationAnimator == null) {
            onDismissChildWithAnimationFinished();
            return;
        }
        if (z) {
            viewTranslationAnimator.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
            viewTranslationAnimator.setDuration(j3);
        } else {
            this.mFlingAnimationUtils.applyDismissing(viewTranslationAnimator, getTranslation(view), f3, f, getSize(view));
        }
        if (j4 > 0) {
            viewTranslationAnimator.setStartDelay(j4);
        }
        final Consumer<Boolean> consumer2 = consumer;
        viewTranslationAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean mCancelled;

            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                SwipeHelper.this.mCallback.onBeginDrag(view);
            }

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                SwipeHelper.this.updateSwipeProgressFromOffset(view, canChildBeDismissed);
                SwipeHelper.this.mDismissPendingMap.remove(view);
                View view = view;
                boolean isRemoved = view instanceof ExpandableNotificationRow ? ((ExpandableNotificationRow) view).isRemoved() : false;
                if (!this.mCancelled || isRemoved) {
                    SwipeHelper.this.mCallback.onChildDismissed(view);
                    SwipeHelper.this.resetSwipeState();
                }
                Consumer consumer = consumer2;
                if (consumer != null) {
                    consumer.accept(Boolean.valueOf(this.mCancelled));
                }
                if (!SwipeHelper.this.mDisableHwLayers) {
                    view.setLayerType(0, (Paint) null);
                }
                SwipeHelper.this.onDismissChildWithAnimationFinished();
            }
        });
        prepareDismissAnimation(view, viewTranslationAnimator);
        this.mDismissPendingMap.put(view, viewTranslationAnimator);
        viewTranslationAnimator.start();
    }

    /* access modifiers changed from: protected */
    public float getTotalTranslationLength(View view) {
        return getSize(view);
    }

    public void snapChild(final View view, float f, float f2) {
        final boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        Animator viewTranslationAnimator = getViewTranslationAnimator(view, f, new SwipeHelper$$ExternalSyntheticLambda0(this, view, canChildBeDismissed));
        if (viewTranslationAnimator == null) {
            onSnapChildWithAnimationFinished();
            return;
        }
        viewTranslationAnimator.addListener(new AnimatorListenerAdapter() {
            boolean wasCancelled = false;

            public void onAnimationCancel(Animator animator) {
                this.wasCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                boolean unused = SwipeHelper.this.mSnappingChild = false;
                if (!this.wasCancelled) {
                    SwipeHelper.this.updateSwipeProgressFromOffset(view, canChildBeDismissed);
                    SwipeHelper.this.resetSwipeState();
                }
                SwipeHelper.this.onSnapChildWithAnimationFinished();
            }
        });
        prepareSnapBackAnimation(view, viewTranslationAnimator);
        this.mSnappingChild = true;
        Animator animator = viewTranslationAnimator;
        this.mFlingAnimationUtils.apply(animator, getTranslation(view), f, f2, Math.abs(f - getTranslation(view)));
        viewTranslationAnimator.start();
        this.mCallback.onChildSnappedBack(view, f);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$snapChild$0$com-android-systemui-SwipeHelper  reason: not valid java name */
    public /* synthetic */ void m2528lambda$snapChild$0$comandroidsystemuiSwipeHelper(View view, boolean z, ValueAnimator valueAnimator) {
        onTranslationUpdate(view, ((Float) valueAnimator.getAnimatedValue()).floatValue(), z);
    }

    public void onTranslationUpdate(View view, float f, boolean z) {
        updateSwipeProgressFromOffset(view, z, f);
    }

    private void snapChildInstantly(View view) {
        boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        setTranslation(view, 0.0f);
        updateSwipeProgressFromOffset(view, canChildBeDismissed);
    }

    public void snapChildIfNeeded(View view, boolean z, float f) {
        if ((!this.mIsSwiping || this.mTouchedView != view) && !this.mSnappingChild) {
            Animator animator = this.mDismissPendingMap.get(view);
            boolean z2 = true;
            if (animator != null) {
                animator.cancel();
            } else if (getTranslation(view) == 0.0f) {
                z2 = false;
            }
            if (!z2) {
                return;
            }
            if (z) {
                snapChild(view, f, 0.0f);
            } else {
                snapChildInstantly(view);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
        if (r0 != 4) goto L_0x011a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
            r8 = this;
            boolean r0 = r8.mIsSwiping
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0026
            boolean r0 = r8.mMenuRowIntercepting
            if (r0 != 0) goto L_0x0026
            boolean r0 = r8.mLongPressSent
            if (r0 != 0) goto L_0x0026
            com.android.systemui.SwipeHelper$Callback r0 = r8.mCallback
            android.view.View r0 = r0.getChildAtPosition(r9)
            if (r0 == 0) goto L_0x0022
            com.android.systemui.SwipeHelper$Callback r0 = r8.mCallback
            android.view.View r0 = r0.getChildAtPosition(r9)
            r8.mTouchedView = r0
            r8.onInterceptTouchEvent(r9)
            return r2
        L_0x0022:
            r8.cancelLongPress()
            return r1
        L_0x0026:
            android.view.VelocityTracker r0 = r8.mVelocityTracker
            r0.addMovement(r9)
            int r0 = r9.getAction()
            r3 = 0
            if (r0 == r2) goto L_0x00d6
            r4 = 2
            if (r0 == r4) goto L_0x003d
            r4 = 3
            if (r0 == r4) goto L_0x00d6
            r4 = 4
            if (r0 == r4) goto L_0x003d
            goto L_0x011a
        L_0x003d:
            android.view.View r0 = r8.mTouchedView
            if (r0 == 0) goto L_0x011a
            float r0 = r8.getPos(r9)
            float r4 = r8.mInitialTouchPos
            float r0 = r0 - r4
            float r4 = java.lang.Math.abs((float) r0)
            int r5 = r8.getFalsingThreshold()
            float r5 = (float) r5
            int r5 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r5 < 0) goto L_0x0057
            r8.mTouchAboveFalsingThreshold = r2
        L_0x0057:
            boolean r5 = r8.mLongPressSent
            if (r5 == 0) goto L_0x0078
            float r0 = r8.getTouchSlop(r9)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x011a
            android.view.View r8 = r8.mTouchedView
            boolean r0 = r8 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r0 == 0) goto L_0x011a
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r8 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r8
            float r0 = r9.getX()
            float r9 = r9.getY()
            r8.doDragCallback(r0, r9)
            goto L_0x011a
        L_0x0078:
            com.android.systemui.SwipeHelper$Callback r5 = r8.mCallback
            android.view.View r6 = r8.mTouchedView
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0081
            r1 = r2
        L_0x0081:
            boolean r1 = r5.canChildBeDismissedInDirection(r6, r1)
            if (r1 != 0) goto L_0x00be
            android.view.View r1 = r8.mTouchedView
            float r1 = r8.getSize(r1)
            r5 = 1050253722(0x3e99999a, float:0.3)
            float r5 = r5 * r1
            int r6 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r6 < 0) goto L_0x009b
            if (r3 <= 0) goto L_0x0099
            r0 = r5
            goto L_0x00be
        L_0x0099:
            float r0 = -r5
            goto L_0x00be
        L_0x009b:
            com.android.systemui.SwipeHelper$Callback r3 = r8.mCallback
            int r3 = r3.getConstrainSwipeStartPosition()
            float r3 = (float) r3
            int r4 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r4 <= 0) goto L_0x00be
            float r4 = java.lang.Math.signum((float) r0)
            float r3 = r3 * r4
            int r3 = (int) r3
            float r3 = (float) r3
            float r0 = r0 - r3
            float r0 = r0 / r1
            double r0 = (double) r0
            r6 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r0 = r0 * r6
            double r0 = java.lang.Math.sin(r0)
            float r0 = (float) r0
            float r5 = r5 * r0
            float r0 = r3 + r5
        L_0x00be:
            android.view.View r1 = r8.mTouchedView
            float r3 = r8.mTranslation
            float r3 = r3 + r0
            r8.setTranslation(r1, r3)
            android.view.View r1 = r8.mTouchedView
            boolean r3 = r8.mCanCurrViewBeDimissed
            r8.updateSwipeProgressFromOffset(r1, r3)
            android.view.View r1 = r8.mTouchedView
            float r3 = r8.mTranslation
            float r3 = r3 + r0
            r8.onMoveUpdate(r1, r9, r3, r0)
            goto L_0x011a
        L_0x00d6:
            android.view.View r0 = r8.mTouchedView
            if (r0 != 0) goto L_0x00db
            goto L_0x011a
        L_0x00db:
            android.view.VelocityTracker r0 = r8.mVelocityTracker
            r4 = 1000(0x3e8, float:1.401E-42)
            float r5 = r8.getMaxVelocity()
            r0.computeCurrentVelocity(r4, r5)
            android.view.VelocityTracker r0 = r8.mVelocityTracker
            float r0 = r8.getVelocity(r0)
            android.view.View r4 = r8.mTouchedView
            float r5 = r8.getTranslation(r4)
            boolean r4 = r8.handleUpEvent(r9, r4, r0, r5)
            if (r4 != 0) goto L_0x0118
            boolean r9 = r8.isDismissGesture(r9)
            if (r9 == 0) goto L_0x0109
            android.view.View r9 = r8.mTouchedView
            boolean r3 = r8.swipedFastEnough()
            r3 = r3 ^ r2
            r8.dismissChild(r9, r0, r3)
            goto L_0x0115
        L_0x0109:
            com.android.systemui.SwipeHelper$Callback r9 = r8.mCallback
            android.view.View r4 = r8.mTouchedView
            r9.onDragCancelled(r4)
            android.view.View r9 = r8.mTouchedView
            r8.snapChild(r9, r3, r0)
        L_0x0115:
            r9 = 0
            r8.mTouchedView = r9
        L_0x0118:
            r8.mIsSwiping = r1
        L_0x011a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private int getFalsingThreshold() {
        return (int) (((float) this.mFalsingThreshold) * this.mCallback.getFalsingThresholdFactor());
    }

    private float getMaxVelocity() {
        return this.mDensityScale * 4000.0f;
    }

    /* access modifiers changed from: protected */
    public float getEscapeVelocity() {
        return getUnscaledEscapeVelocity() * this.mDensityScale;
    }

    /* access modifiers changed from: protected */
    public boolean swipedFarEnough() {
        return Math.abs(getTranslation(this.mTouchedView)) > getSize(this.mTouchedView) * 0.6f;
    }

    public boolean isDismissGesture(MotionEvent motionEvent) {
        float translation = getTranslation(this.mTouchedView);
        if (motionEvent.getActionMasked() != 1 || this.mFalsingManager.isUnlockingDisabled() || isFalseGesture()) {
            return false;
        }
        if (!swipedFastEnough() && !swipedFarEnough()) {
            return false;
        }
        if (this.mCallback.canChildBeDismissedInDirection(this.mTouchedView, translation > 0.0f)) {
            return true;
        }
        return false;
    }

    public boolean isFalseGesture() {
        boolean isAntiFalsingNeeded = this.mCallback.isAntiFalsingNeeded();
        if (this.mFalsingManager.isClassifierEnabled()) {
            if (!isAntiFalsingNeeded || !this.mFalsingManager.isFalseTouch(1)) {
                return false;
            }
        } else if (!isAntiFalsingNeeded || this.mTouchAboveFalsingThreshold) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean swipedFastEnough() {
        float velocity = getVelocity(this.mVelocityTracker);
        float translation = getTranslation(this.mTouchedView);
        if (Math.abs(velocity) <= getEscapeVelocity()) {
            return false;
        }
        return ((velocity > 0.0f ? 1 : (velocity == 0.0f ? 0 : -1)) > 0) == ((translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1)) > 0);
    }

    public boolean isSwiping() {
        return this.mIsSwiping;
    }

    public View getSwipedView() {
        if (this.mIsSwiping) {
            return this.mTouchedView;
        }
        return null;
    }

    public void resetSwipeState() {
        this.mTouchedView = null;
        this.mIsSwiping = false;
    }

    private float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mTouchSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    /* access modifiers changed from: private */
    public boolean isAvailableToDragAndDrop(View view) {
        PendingIntent pendingIntent;
        if (!this.mFeatureFlags.isEnabled(Flags.NOTIFICATION_DRAG_TO_CONTENTS) || !(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        boolean canBubble = expandableNotificationRow.getEntry().canBubble();
        Notification notification = expandableNotificationRow.getEntry().getSbn().getNotification();
        if (notification.contentIntent != null) {
            pendingIntent = notification.contentIntent;
        } else {
            pendingIntent = notification.fullScreenIntent;
        }
        return pendingIntent != null && pendingIntent.isActivity() && !canBubble;
    }

    public interface Callback {
        boolean canChildBeDismissed(View view);

        boolean canChildBeDragged(View view) {
            return true;
        }

        View getChildAtPosition(MotionEvent motionEvent);

        int getConstrainSwipeStartPosition() {
            return 0;
        }

        float getFalsingThresholdFactor();

        boolean isAntiFalsingNeeded();

        void onBeginDrag(View view);

        void onChildDismissed(View view);

        void onChildSnappedBack(View view, float f);

        void onDragCancelled(View view);

        void onLongPressSent(View view);

        boolean updateSwipeProgress(View view, boolean z, float f);

        boolean canChildBeDismissedInDirection(View view, boolean z) {
            return canChildBeDismissed(view);
        }
    }
}
