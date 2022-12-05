package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: RelativeTouchListener.kt */
/* loaded from: classes2.dex */
public abstract class RelativeTouchListener implements View.OnTouchListener {
    private boolean movedEnough;
    private boolean performedLongClick;
    @NotNull
    private final PointF touchDown = new PointF();
    @NotNull
    private final PointF viewPositionOnTouchDown = new PointF();
    private final VelocityTracker velocityTracker = VelocityTracker.obtain();
    private int touchSlop = -1;

    public abstract boolean onDown(@NotNull View view, @NotNull MotionEvent motionEvent);

    public abstract void onMove(@NotNull View view, @NotNull MotionEvent motionEvent, float f, float f2, float f3, float f4);

    public abstract void onUp(@NotNull View view, @NotNull MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6);

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(@NotNull final View v, @NotNull MotionEvent ev) {
        Intrinsics.checkNotNullParameter(v, "v");
        Intrinsics.checkNotNullParameter(ev, "ev");
        addMovement(ev);
        float rawX = ev.getRawX() - this.touchDown.x;
        float rawY = ev.getRawY() - this.touchDown.y;
        int action = ev.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.movedEnough) {
                    this.velocityTracker.computeCurrentVelocity(1000);
                    PointF pointF = this.viewPositionOnTouchDown;
                    onUp(v, ev, pointF.x, pointF.y, rawX, rawY, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                } else if (!this.performedLongClick) {
                    v.performClick();
                } else {
                    v.getHandler().removeCallbacksAndMessages(null);
                }
                this.velocityTracker.clear();
                this.movedEnough = false;
            } else if (action == 2) {
                if (!this.movedEnough && ((float) Math.hypot(rawX, rawY)) > this.touchSlop && !this.performedLongClick) {
                    this.movedEnough = true;
                    v.getHandler().removeCallbacksAndMessages(null);
                }
                if (this.movedEnough) {
                    PointF pointF2 = this.viewPositionOnTouchDown;
                    onMove(v, ev, pointF2.x, pointF2.y, rawX, rawY);
                }
            }
        } else if (!onDown(v, ev)) {
            return false;
        } else {
            this.touchSlop = ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
            this.touchDown.set(ev.getRawX(), ev.getRawY());
            this.viewPositionOnTouchDown.set(v.getTranslationX(), v.getTranslationY());
            this.performedLongClick = false;
            v.getHandler().postDelayed(new Runnable() { // from class: com.android.wm.shell.bubbles.RelativeTouchListener$onTouch$1
                @Override // java.lang.Runnable
                public final void run() {
                    if (v.isLongClickable()) {
                        this.performedLongClick = v.performLongClick();
                    }
                }
            }, ViewConfiguration.getLongPressTimeout());
        }
        return true;
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }
}
