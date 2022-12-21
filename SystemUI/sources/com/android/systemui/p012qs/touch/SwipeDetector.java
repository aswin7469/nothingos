package com.android.systemui.p012qs.touch;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* renamed from: com.android.systemui.qs.touch.SwipeDetector */
public class SwipeDetector {
    private static final float ANIMATION_DURATION = 1200.0f;
    private static final boolean DBG = false;
    public static final int DIRECTION_BOTH = 3;
    public static final int DIRECTION_NEGATIVE = 2;
    public static final int DIRECTION_POSITIVE = 1;
    public static final Direction HORIZONTAL = new Direction() {
        /* access modifiers changed from: package-private */
        public float getDisplacement(MotionEvent motionEvent, int i, PointF pointF) {
            return motionEvent.getX(i) - pointF.x;
        }

        /* access modifiers changed from: package-private */
        public float getActiveTouchSlop(MotionEvent motionEvent, int i, PointF pointF) {
            return Math.abs(motionEvent.getY(i) - pointF.y);
        }
    };
    public static final float RELEASE_VELOCITY_PX_MS = 1.0f;
    public static final float SCROLL_VELOCITY_DAMPENING_RC = 15.915494f;
    private static final String TAG = "SwipeDetector";
    public static final Direction VERTICAL = new Direction() {
        /* access modifiers changed from: package-private */
        public float getDisplacement(MotionEvent motionEvent, int i, PointF pointF) {
            return motionEvent.getY(i) - pointF.y;
        }

        /* access modifiers changed from: package-private */
        public float getActiveTouchSlop(MotionEvent motionEvent, int i, PointF pointF) {
            return Math.abs(motionEvent.getX(i) - pointF.x);
        }
    };
    protected int mActivePointerId;
    private long mCurrentMillis;
    private final Direction mDir;
    private float mDisplacement;
    private final PointF mDownPos;
    private boolean mIgnoreSlopWhenSettling;
    private float mLastDisplacement;
    private final PointF mLastPos;
    private final Listener mListener;
    private int mScrollConditions;
    private ScrollState mState;
    private float mSubtractDisplacement;
    private final float mTouchSlop;
    private float mVelocity;

    /* renamed from: com.android.systemui.qs.touch.SwipeDetector$Direction */
    public static abstract class Direction {
        /* access modifiers changed from: package-private */
        public abstract float getActiveTouchSlop(MotionEvent motionEvent, int i, PointF pointF);

        /* access modifiers changed from: package-private */
        public abstract float getDisplacement(MotionEvent motionEvent, int i, PointF pointF);
    }

    /* renamed from: com.android.systemui.qs.touch.SwipeDetector$Listener */
    public interface Listener {
        boolean onDrag(float f, float f2);

        void onDragEnd(float f, boolean z);

        void onDragStart(boolean z);
    }

    /* renamed from: com.android.systemui.qs.touch.SwipeDetector$ScrollState */
    enum ScrollState {
        IDLE,
        DRAGGING,
        SETTLING
    }

    private static float computeDampeningFactor(float f) {
        return f / (15.915494f + f);
    }

    private static float interpolate(float f, float f2, float f3) {
        return ((1.0f - f3) * f) + (f3 * f2);
    }

    private void setState(ScrollState scrollState) {
        if (scrollState == ScrollState.DRAGGING) {
            initializeDragging();
            if (this.mState == ScrollState.IDLE) {
                reportDragStart(false);
            } else if (this.mState == ScrollState.SETTLING) {
                reportDragStart(true);
            }
        }
        if (scrollState == ScrollState.SETTLING) {
            reportDragEnd();
        }
        this.mState = scrollState;
    }

    public boolean isDraggingOrSettling() {
        return this.mState == ScrollState.DRAGGING || this.mState == ScrollState.SETTLING;
    }

    public boolean isIdleState() {
        return this.mState == ScrollState.IDLE;
    }

    public boolean isSettlingState() {
        return this.mState == ScrollState.SETTLING;
    }

    public boolean isDraggingState() {
        return this.mState == ScrollState.DRAGGING;
    }

    public SwipeDetector(Context context, Listener listener, Direction direction) {
        this((float) ViewConfiguration.get(context).getScaledTouchSlop(), listener, direction);
    }

    protected SwipeDetector(float f, Listener listener, Direction direction) {
        this.mActivePointerId = -1;
        this.mState = ScrollState.IDLE;
        this.mDownPos = new PointF();
        this.mLastPos = new PointF();
        this.mTouchSlop = f;
        this.mListener = listener;
        this.mDir = direction;
    }

    public void setDetectableScrollConditions(int i, boolean z) {
        this.mScrollConditions = i;
        this.mIgnoreSlopWhenSettling = z;
    }

    private boolean shouldScrollStart(MotionEvent motionEvent, int i) {
        if (Math.max(this.mDir.getActiveTouchSlop(motionEvent, i, this.mDownPos), this.mTouchSlop) > Math.abs(this.mDisplacement)) {
            return false;
        }
        int i2 = this.mScrollConditions;
        if (((i2 & 2) <= 0 || this.mDisplacement <= 0.0f) && ((i2 & 1) <= 0 || this.mDisplacement >= 0.0f)) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        int i = 0;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex != -1) {
                        this.mDisplacement = this.mDir.getDisplacement(motionEvent, findPointerIndex, this.mDownPos);
                        computeVelocity(this.mDir.getDisplacement(motionEvent, findPointerIndex, this.mLastPos), motionEvent.getEventTime());
                        if (this.mState != ScrollState.DRAGGING && shouldScrollStart(motionEvent, findPointerIndex)) {
                            setState(ScrollState.DRAGGING);
                        }
                        if (this.mState == ScrollState.DRAGGING) {
                            reportDragging();
                        }
                        this.mLastPos.set(motionEvent.getX(findPointerIndex), motionEvent.getY(findPointerIndex));
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        int actionIndex = motionEvent.getActionIndex();
                        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                            if (actionIndex == 0) {
                                i = 1;
                            }
                            this.mDownPos.set(motionEvent.getX(i) - (this.mLastPos.x - this.mDownPos.x), motionEvent.getY(i) - (this.mLastPos.y - this.mDownPos.y));
                            this.mLastPos.set(motionEvent.getX(i), motionEvent.getY(i));
                            this.mActivePointerId = motionEvent.getPointerId(i);
                        }
                    }
                }
            }
            if (this.mState == ScrollState.DRAGGING) {
                setState(ScrollState.SETTLING);
            }
        } else {
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mDownPos.set(motionEvent.getX(), motionEvent.getY());
            this.mLastPos.set(this.mDownPos);
            this.mLastDisplacement = 0.0f;
            this.mDisplacement = 0.0f;
            this.mVelocity = 0.0f;
            if (this.mState == ScrollState.SETTLING && this.mIgnoreSlopWhenSettling) {
                setState(ScrollState.DRAGGING);
            }
        }
        return true;
    }

    public void finishedScrolling() {
        setState(ScrollState.IDLE);
    }

    private boolean reportDragStart(boolean z) {
        this.mListener.onDragStart(!z);
        return true;
    }

    private void initializeDragging() {
        if (this.mState == ScrollState.SETTLING && this.mIgnoreSlopWhenSettling) {
            this.mSubtractDisplacement = 0.0f;
        }
        if (this.mDisplacement > 0.0f) {
            this.mSubtractDisplacement = this.mTouchSlop;
        } else {
            this.mSubtractDisplacement = -this.mTouchSlop;
        }
    }

    private boolean reportDragging() {
        float f = this.mDisplacement;
        if (f == this.mLastDisplacement) {
            return true;
        }
        this.mLastDisplacement = f;
        return this.mListener.onDrag(f - this.mSubtractDisplacement, this.mVelocity);
    }

    private void reportDragEnd() {
        Listener listener = this.mListener;
        float f = this.mVelocity;
        listener.onDragEnd(f, Math.abs(f) > 1.0f);
    }

    public float computeVelocity(float f, long j) {
        long j2 = this.mCurrentMillis;
        this.mCurrentMillis = j;
        float f2 = (float) (j - j2);
        float f3 = 0.0f;
        if (f2 > 0.0f) {
            f3 = f / f2;
        }
        if (Math.abs(this.mVelocity) < 0.001f) {
            this.mVelocity = f3;
        } else {
            this.mVelocity = interpolate(this.mVelocity, f3, computeDampeningFactor(f2));
        }
        return this.mVelocity;
    }

    public static long calculateDuration(float f, float f2) {
        float max = Math.max(2.0f, Math.abs(f * 0.5f));
        return (long) Math.max(100.0f, (ANIMATION_DURATION / max) * Math.max(0.2f, f2));
    }
}
