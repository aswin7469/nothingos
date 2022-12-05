package com.android.wm.shell.common.split;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
/* loaded from: classes2.dex */
public class DividerView extends FrameLayout implements View.OnTouchListener {
    private View mBackground;
    private GestureDetector mDoubleTapDetector;
    private DividerHandleView mHandle;
    private boolean mInteractive;
    private boolean mMoving;
    private SplitLayout mSplitLayout;
    private int mStartPos;
    private int mTouchElevation;
    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private VelocityTracker mVelocityTracker;
    private SurfaceControlViewHost mViewHost;

    public DividerView(Context context) {
        super(context);
    }

    public DividerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setup(SplitLayout splitLayout, SurfaceControlViewHost surfaceControlViewHost) {
        this.mSplitLayout = splitLayout;
        this.mViewHost = surfaceControlViewHost;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandle = (DividerHandleView) findViewById(R.id.docked_divider_handle);
        this.mBackground = findViewById(R.id.docked_divider_background);
        this.mTouchElevation = getResources().getDimensionPixelSize(R.dimen.docked_stack_divider_lift_elevation);
        this.mDoubleTapDetector = new GestureDetector(getContext(), new DoubleTapListener());
        this.mInteractive = true;
        setOnTouchListener(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0033, code lost:
        if (r6 != 3) goto L19;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float yVelocity;
        if (this.mSplitLayout == null || !this.mInteractive) {
            return false;
        }
        if (this.mDoubleTapDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        int action = motionEvent.getAction() & 255;
        boolean isLandscape = isLandscape();
        int rawX = (int) (isLandscape ? motionEvent.getRawX() : motionEvent.getRawY());
        if (action == 0) {
            VelocityTracker obtain = VelocityTracker.obtain();
            this.mVelocityTracker = obtain;
            obtain.addMovement(motionEvent);
            setTouching();
            this.mStartPos = rawX;
            this.mMoving = false;
        } else {
            if (action != 1) {
                if (action == 2) {
                    this.mVelocityTracker.addMovement(motionEvent);
                    if (!this.mMoving && Math.abs(rawX - this.mStartPos) > this.mTouchSlop) {
                        this.mStartPos = rawX;
                        this.mMoving = true;
                    }
                    if (this.mMoving) {
                        this.mSplitLayout.updateDivideBounds((this.mSplitLayout.getDividePosition() + rawX) - this.mStartPos);
                    }
                }
            }
            releaseTouching();
            if (this.mMoving) {
                this.mVelocityTracker.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                if (isLandscape) {
                    yVelocity = this.mVelocityTracker.getXVelocity();
                } else {
                    yVelocity = this.mVelocityTracker.getYVelocity();
                }
                int dividePosition = (this.mSplitLayout.getDividePosition() + rawX) - this.mStartPos;
                this.mSplitLayout.snapToTarget(dividePosition, this.mSplitLayout.findSnapTarget(dividePosition, yVelocity, false));
                this.mMoving = false;
            }
        }
        return true;
    }

    private void setTouching() {
        setSlippery(false);
        this.mHandle.setTouching(true, true);
        if (isLandscape()) {
            this.mBackground.animate().scaleX(1.4f);
        } else {
            this.mBackground.animate().scaleY(1.4f);
        }
        ViewPropertyAnimator animate = this.mBackground.animate();
        Interpolator interpolator = Interpolators.TOUCH_RESPONSE;
        animate.setInterpolator(interpolator).setDuration(150L).translationZ(this.mTouchElevation).start();
        this.mHandle.animate().setInterpolator(interpolator).setDuration(150L).translationZ(this.mTouchElevation).start();
    }

    private void releaseTouching() {
        setSlippery(true);
        this.mHandle.setTouching(false, true);
        ViewPropertyAnimator animate = this.mBackground.animate();
        Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
        animate.setInterpolator(interpolator).setDuration(200L).translationZ(0.0f).scaleX(1.0f).scaleY(1.0f).start();
        this.mHandle.animate().setInterpolator(interpolator).setDuration(200L).translationZ(0.0f).start();
    }

    private void setSlippery(boolean z) {
        if (this.mViewHost == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        int i = layoutParams.flags;
        if (((i & 536870912) != 0) == z) {
            return;
        }
        if (z) {
            layoutParams.flags = i | 536870912;
        } else {
            layoutParams.flags = (-536870913) & i;
        }
        this.mViewHost.relayout(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInteractive(boolean z) {
        if (z == this.mInteractive) {
            return;
        }
        this.mInteractive = z;
        releaseTouching();
        this.mHandle.setVisibility(this.mInteractive ? 0 : 4);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == 2;
    }

    /* loaded from: classes2.dex */
    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        private DoubleTapListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (DividerView.this.mSplitLayout != null) {
                DividerView.this.mSplitLayout.onDoubleTappedDivider();
                return true;
            }
            return true;
        }
    }
}
