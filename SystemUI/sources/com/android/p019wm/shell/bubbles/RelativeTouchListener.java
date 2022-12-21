package com.android.p019wm.shell.bubbles;

import android.graphics.PointF;
import android.icu.text.DateFormat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0018\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0011H&J8\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0018H&J\u0018\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0011H\u0016JH\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0018H&R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \f*\u0004\u0018\u00010\u000b0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/RelativeTouchListener;", "Landroid/view/View$OnTouchListener;", "()V", "movedEnough", "", "performedLongClick", "touchDown", "Landroid/graphics/PointF;", "touchSlop", "", "velocityTracker", "Landroid/view/VelocityTracker;", "kotlin.jvm.PlatformType", "viewPositionOnTouchDown", "addMovement", "", "event", "Landroid/view/MotionEvent;", "onDown", "v", "Landroid/view/View;", "ev", "onMove", "viewInitialX", "", "viewInitialY", "dx", "dy", "onTouch", "onUp", "velX", "velY", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.RelativeTouchListener */
/* compiled from: RelativeTouchListener.kt */
public abstract class RelativeTouchListener implements View.OnTouchListener {
    private boolean movedEnough;
    private boolean performedLongClick;
    private final PointF touchDown = new PointF();
    private int touchSlop = -1;
    private final VelocityTracker velocityTracker = VelocityTracker.obtain();
    private final PointF viewPositionOnTouchDown = new PointF();

    public abstract boolean onDown(View view, MotionEvent motionEvent);

    public abstract void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4);

    public abstract void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6);

    public boolean onTouch(View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        addMovement(motionEvent);
        float rawX = motionEvent.getRawX() - this.touchDown.x;
        float rawY = motionEvent.getRawY() - this.touchDown.y;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.movedEnough) {
                    this.velocityTracker.computeCurrentVelocity(1000);
                    onUp(view, motionEvent, this.viewPositionOnTouchDown.x, this.viewPositionOnTouchDown.y, rawX, rawY, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                } else if (!this.performedLongClick) {
                    view.performClick();
                } else {
                    view.getHandler().removeCallbacksAndMessages((Object) null);
                }
                this.velocityTracker.clear();
                this.movedEnough = false;
            } else if (action == 2) {
                if (!this.movedEnough && ((float) Math.hypot((double) rawX, (double) rawY)) > ((float) this.touchSlop) && !this.performedLongClick) {
                    this.movedEnough = true;
                    view.getHandler().removeCallbacksAndMessages((Object) null);
                }
                if (this.movedEnough) {
                    onMove(view, motionEvent, this.viewPositionOnTouchDown.x, this.viewPositionOnTouchDown.y, rawX, rawY);
                }
            }
        } else if (!onDown(view, motionEvent)) {
            return false;
        } else {
            this.touchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.touchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.viewPositionOnTouchDown.set(view.getTranslationX(), view.getTranslationY());
            this.performedLongClick = false;
            view.getHandler().postDelayed(new RelativeTouchListener$$ExternalSyntheticLambda0(view, this), (long) ViewConfiguration.getLongPressTimeout());
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: onTouch$lambda-0  reason: not valid java name */
    public static final void m3436onTouch$lambda0(View view, RelativeTouchListener relativeTouchListener) {
        Intrinsics.checkNotNullParameter(view, "$v");
        Intrinsics.checkNotNullParameter(relativeTouchListener, "this$0");
        if (view.isLongClickable()) {
            relativeTouchListener.performedLongClick = view.performLongClick();
        }
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }
}
