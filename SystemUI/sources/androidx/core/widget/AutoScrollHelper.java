package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
/* loaded from: classes.dex */
public abstract class AutoScrollHelper implements View.OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private Runnable mRunnable;
    final View mTarget;
    final ClampedScroller mScroller = new ClampedScroller();
    private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
    private float[] mRelativeEdges = {0.0f, 0.0f};
    private float[] mMaximumEdges = {Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mRelativeVelocity = {0.0f, 0.0f};
    private float[] mMinimumVelocity = {0.0f, 0.0f};
    private float[] mMaximumVelocity = {Float.MAX_VALUE, Float.MAX_VALUE};

    static float constrain(float value, float min, float max) {
        return value > max ? max : value < min ? min : value;
    }

    static int constrain(int value, int min, int max) {
        return value > max ? max : value < min ? min : value;
    }

    public abstract boolean canTargetScrollHorizontally(int direction);

    public abstract boolean canTargetScrollVertically(int direction);

    public abstract void scrollTargetBy(int deltaX, int deltaY);

    public AutoScrollHelper(View target) {
        this.mTarget = target;
        float f = Resources.getSystem().getDisplayMetrics().density;
        float f2 = (int) ((1575.0f * f) + 0.5f);
        setMaximumVelocity(f2, f2);
        float f3 = (int) ((f * 315.0f) + 0.5f);
        setMinimumVelocity(f3, f3);
        setEdgeType(1);
        setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        setRelativeEdges(0.2f, 0.2f);
        setRelativeVelocity(1.0f, 1.0f);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(500);
        setRampDownDuration(500);
    }

    public AutoScrollHelper setEnabled(boolean enabled) {
        if (this.mEnabled && !enabled) {
            requestStop();
        }
        this.mEnabled = enabled;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float horizontalMax, float verticalMax) {
        float[] fArr = this.mMaximumVelocity;
        fArr[0] = horizontalMax / 1000.0f;
        fArr[1] = verticalMax / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float horizontalMin, float verticalMin) {
        float[] fArr = this.mMinimumVelocity;
        fArr[0] = horizontalMin / 1000.0f;
        fArr[1] = verticalMin / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float horizontal, float vertical) {
        float[] fArr = this.mRelativeVelocity;
        fArr[0] = horizontal / 1000.0f;
        fArr[1] = vertical / 1000.0f;
        return this;
    }

    public AutoScrollHelper setEdgeType(int type) {
        this.mEdgeType = type;
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float horizontal, float vertical) {
        float[] fArr = this.mRelativeEdges;
        fArr[0] = horizontal;
        fArr[1] = vertical;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float horizontalMax, float verticalMax) {
        float[] fArr = this.mMaximumEdges;
        fArr[0] = horizontalMax;
        fArr[1] = verticalMax;
        return this;
    }

    public AutoScrollHelper setActivationDelay(int delayMillis) {
        this.mActivationDelay = delayMillis;
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int durationMillis) {
        this.mScroller.setRampUpDuration(durationMillis);
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int durationMillis) {
        this.mScroller.setRampDownDuration(durationMillis);
        return this;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0013, code lost:
        if (r0 != 3) goto L12;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(View v, MotionEvent event) {
        if (!this.mEnabled) {
            return false;
        }
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            this.mNeedsCancel = true;
            this.mAlreadyDelayed = false;
        } else {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                }
            }
            requestStop();
            return this.mExclusive && this.mAnimating;
        }
        this.mScroller.setTargetVelocity(computeTargetVelocity(0, event.getX(), v.getWidth(), this.mTarget.getWidth()), computeTargetVelocity(1, event.getY(), v.getHeight(), this.mTarget.getHeight()));
        if (!this.mAnimating && shouldAnimate()) {
            startAnimating();
        }
        if (this.mExclusive) {
            return false;
        }
    }

    boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int verticalDirection = clampedScroller.getVerticalDirection();
        int horizontalDirection = clampedScroller.getHorizontalDirection();
        return (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
    }

    private void startAnimating() {
        int i;
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (!this.mAlreadyDelayed && (i = this.mActivationDelay) > 0) {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, i);
        } else {
            this.mRunnable.run();
        }
        this.mAlreadyDelayed = true;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private float computeTargetVelocity(int direction, float coordinate, float srcSize, float dstSize) {
        float edgeValue = getEdgeValue(this.mRelativeEdges[direction], srcSize, this.mMaximumEdges[direction], coordinate);
        int i = (edgeValue > 0.0f ? 1 : (edgeValue == 0.0f ? 0 : -1));
        if (i == 0) {
            return 0.0f;
        }
        float f = this.mRelativeVelocity[direction];
        float f2 = this.mMinimumVelocity[direction];
        float f3 = this.mMaximumVelocity[direction];
        float f4 = f * dstSize;
        if (i > 0) {
            return constrain(edgeValue * f4, f2, f3);
        }
        return -constrain((-edgeValue) * f4, f2, f3);
    }

    private float getEdgeValue(float relativeValue, float size, float maxValue, float current) {
        float interpolation;
        float constrain = constrain(relativeValue * size, 0.0f, maxValue);
        float constrainEdgeValue = constrainEdgeValue(size - current, constrain) - constrainEdgeValue(current, constrain);
        if (constrainEdgeValue < 0.0f) {
            interpolation = -this.mEdgeInterpolator.getInterpolation(-constrainEdgeValue);
        } else if (constrainEdgeValue <= 0.0f) {
            return 0.0f;
        } else {
            interpolation = this.mEdgeInterpolator.getInterpolation(constrainEdgeValue);
        }
        return constrain(interpolation, -1.0f, 1.0f);
    }

    private float constrainEdgeValue(float current, float leading) {
        if (leading == 0.0f) {
            return 0.0f;
        }
        int i = this.mEdgeType;
        if (i == 0 || i == 1) {
            if (current < leading) {
                if (current >= 0.0f) {
                    return 1.0f - (current / leading);
                }
                if (this.mAnimating && i == 1) {
                    return 1.0f;
                }
            }
        } else if (i == 2 && current < 0.0f) {
            return current / (-leading);
        }
        return 0.0f;
    }

    void cancelTargetTouch() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(obtain);
        obtain.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ScrollAnimationRunnable implements Runnable {
        ScrollAnimationRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AutoScrollHelper autoScrollHelper = AutoScrollHelper.this;
            if (!autoScrollHelper.mAnimating) {
                return;
            }
            if (autoScrollHelper.mNeedsReset) {
                autoScrollHelper.mNeedsReset = false;
                autoScrollHelper.mScroller.start();
            }
            ClampedScroller clampedScroller = AutoScrollHelper.this.mScroller;
            if (clampedScroller.isFinished() || !AutoScrollHelper.this.shouldAnimate()) {
                AutoScrollHelper.this.mAnimating = false;
                return;
            }
            AutoScrollHelper autoScrollHelper2 = AutoScrollHelper.this;
            if (autoScrollHelper2.mNeedsCancel) {
                autoScrollHelper2.mNeedsCancel = false;
                autoScrollHelper2.cancelTargetTouch();
            }
            clampedScroller.computeScrollDelta();
            AutoScrollHelper.this.scrollTargetBy(clampedScroller.getDeltaX(), clampedScroller.getDeltaY());
            ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ClampedScroller {
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1;
        private long mDeltaTime = 0;
        private int mDeltaX = 0;
        private int mDeltaY = 0;

        private float interpolateValue(float value) {
            return ((-4.0f) * value * value) + (value * 4.0f);
        }

        ClampedScroller() {
        }

        public void setRampUpDuration(int durationMillis) {
            this.mRampUpDuration = durationMillis;
        }

        public void setRampDownDuration(int durationMillis) {
            this.mRampDownDuration = durationMillis;
        }

        public void start() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = currentAnimationTimeMillis;
            this.mStopTime = -1L;
            this.mDeltaTime = currentAnimationTimeMillis;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }

        public void requestStop() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentAnimationTimeMillis - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = getValueAt(currentAnimationTimeMillis);
            this.mStopTime = currentAnimationTimeMillis;
        }

        public boolean isFinished() {
            return this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
        }

        private float getValueAt(long currentTime) {
            long j = this.mStartTime;
            if (currentTime < j) {
                return 0.0f;
            }
            long j2 = this.mStopTime;
            if (j2 < 0 || currentTime < j2) {
                return AutoScrollHelper.constrain(((float) (currentTime - j)) / this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
            }
            float f = this.mStopValue;
            return (1.0f - f) + (f * AutoScrollHelper.constrain(((float) (currentTime - j2)) / this.mEffectiveRampDown, 0.0f, 1.0f));
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float interpolateValue = interpolateValue(getValueAt(currentAnimationTimeMillis));
            this.mDeltaTime = currentAnimationTimeMillis;
            float f = ((float) (currentAnimationTimeMillis - this.mDeltaTime)) * interpolateValue;
            this.mDeltaX = (int) (this.mTargetVelocityX * f);
            this.mDeltaY = (int) (f * this.mTargetVelocityY);
        }

        public void setTargetVelocity(float x, float y) {
            this.mTargetVelocityX = x;
            this.mTargetVelocityY = y;
        }

        public int getHorizontalDirection() {
            float f = this.mTargetVelocityX;
            return (int) (f / Math.abs(f));
        }

        public int getVerticalDirection() {
            float f = this.mTargetVelocityY;
            return (int) (f / Math.abs(f));
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }
    }
}
