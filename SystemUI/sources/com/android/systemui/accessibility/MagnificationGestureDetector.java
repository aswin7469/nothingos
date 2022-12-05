package com.android.systemui.accessibility;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MagnificationGestureDetector {
    private final Handler mHandler;
    private final OnGestureListener mOnGestureListener;
    private int mTouchSlopSquare;
    private final PointF mPointerDown = new PointF();
    private final PointF mPointerLocation = new PointF(Float.NaN, Float.NaN);
    private boolean mDetectSingleTap = true;
    private boolean mDraggingDetected = false;
    private final Runnable mCancelTapGestureRunnable = new Runnable() { // from class: com.android.systemui.accessibility.MagnificationGestureDetector$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            MagnificationGestureDetector.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnGestureListener {
        boolean onDrag(float f, float f2);

        boolean onFinish(float f, float f2);

        boolean onSingleTap();

        boolean onStart(float f, float f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MagnificationGestureDetector(Context context, Handler handler, OnGestureListener onGestureListener) {
        int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
        this.mHandler = handler;
        this.mOnGestureListener = onGestureListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mDetectSingleTap = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onTouch(MotionEvent motionEvent) {
        boolean onStart;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        if (actionMasked == 0) {
            this.mPointerDown.set(rawX, rawY);
            this.mHandler.postAtTime(this.mCancelTapGestureRunnable, motionEvent.getDownTime() + ViewConfiguration.getLongPressTimeout());
            onStart = this.mOnGestureListener.onStart(rawX, rawY);
        } else {
            if (actionMasked == 1) {
                stopSingleTapDetectionIfNeeded(rawX, rawY);
                if (this.mDetectSingleTap) {
                    z = false | this.mOnGestureListener.onSingleTap();
                }
            } else if (actionMasked == 2) {
                stopSingleTapDetectionIfNeeded(rawX, rawY);
                onStart = notifyDraggingGestureIfNeeded(rawX, rawY);
            } else if (actionMasked != 3) {
                if (actionMasked != 5) {
                    return false;
                }
                stopSingleTapDetection();
                return false;
            }
            boolean onFinish = z | this.mOnGestureListener.onFinish(rawX, rawY);
            reset();
            return onFinish;
        }
        return false | onStart;
    }

    private void stopSingleTapDetectionIfNeeded(float f, float f2) {
        if (!this.mDraggingDetected && isLocationValid(this.mPointerDown)) {
            PointF pointF = this.mPointerDown;
            int i = (int) (pointF.x - f);
            int i2 = (int) (pointF.y - f2);
            if ((i * i) + (i2 * i2) <= this.mTouchSlopSquare) {
                return;
            }
            this.mDraggingDetected = true;
            stopSingleTapDetection();
        }
    }

    private void stopSingleTapDetection() {
        this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
        this.mDetectSingleTap = false;
    }

    private boolean notifyDraggingGestureIfNeeded(float f, float f2) {
        if (!this.mDraggingDetected) {
            return false;
        }
        if (!isLocationValid(this.mPointerLocation)) {
            this.mPointerLocation.set(this.mPointerDown);
        }
        PointF pointF = this.mPointerLocation;
        pointF.set(f, f2);
        return this.mOnGestureListener.onDrag(f - pointF.x, f2 - pointF.y);
    }

    private void reset() {
        resetPointF(this.mPointerDown);
        resetPointF(this.mPointerLocation);
        this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
        this.mDetectSingleTap = true;
        this.mDraggingDetected = false;
    }

    private static void resetPointF(PointF pointF) {
        pointF.x = Float.NaN;
        pointF.y = Float.NaN;
    }

    private static boolean isLocationValid(PointF pointF) {
        return !Float.isNaN(pointF.x) && !Float.isNaN(pointF.y);
    }
}
