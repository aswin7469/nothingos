package com.google.android.setupdesign.gesture;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public final class ConsecutiveTapsGestureDetector {
    private final int consecutiveTapTimeout;
    private final int consecutiveTapTouchSlopSquare;
    private int consecutiveTapsCounter;
    private final OnConsecutiveTapsListener listener;
    private MotionEvent previousTapEvent;
    private final View view;

    public interface OnConsecutiveTapsListener {
        void onConsecutiveTaps(int i);
    }

    public ConsecutiveTapsGestureDetector(OnConsecutiveTapsListener onConsecutiveTapsListener, View view2) {
        this(onConsecutiveTapsListener, view2, ViewConfiguration.getDoubleTapTimeout());
    }

    public ConsecutiveTapsGestureDetector(OnConsecutiveTapsListener onConsecutiveTapsListener, View view2, int i) {
        this.consecutiveTapsCounter = 0;
        this.listener = onConsecutiveTapsListener;
        this.view = view2;
        this.consecutiveTapTimeout = i;
        int scaledDoubleTapSlop = ViewConfiguration.get(view2.getContext()).getScaledDoubleTapSlop();
        this.consecutiveTapTouchSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            Rect rect = new Rect();
            int[] iArr = new int[2];
            this.view.getLocationOnScreen(iArr);
            int i = iArr[0];
            rect.set(i, iArr[1], this.view.getWidth() + i, iArr[1] + this.view.getHeight());
            if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                if (isConsecutiveTap(motionEvent)) {
                    this.consecutiveTapsCounter++;
                } else {
                    this.consecutiveTapsCounter = 1;
                }
                this.listener.onConsecutiveTaps(this.consecutiveTapsCounter);
            } else {
                this.consecutiveTapsCounter = 0;
            }
            MotionEvent motionEvent2 = this.previousTapEvent;
            if (motionEvent2 != null) {
                motionEvent2.recycle();
            }
            this.previousTapEvent = MotionEvent.obtain(motionEvent);
        }
    }

    public void resetCounter() {
        this.consecutiveTapsCounter = 0;
    }

    private boolean isConsecutiveTap(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = this.previousTapEvent;
        if (motionEvent2 == null) {
            return false;
        }
        double x = (double) (motionEvent2.getX() - motionEvent.getX());
        double y = (double) (this.previousTapEvent.getY() - motionEvent.getY());
        long eventTime = motionEvent.getEventTime() - this.previousTapEvent.getEventTime();
        if ((x * x) + (y * y) > ((double) this.consecutiveTapTouchSlopSquare) || eventTime >= ((long) this.consecutiveTapTimeout)) {
            return false;
        }
        return true;
    }
}
