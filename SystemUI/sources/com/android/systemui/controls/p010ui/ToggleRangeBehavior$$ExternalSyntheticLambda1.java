package com.android.systemui.controls.p010ui;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.controls.p010ui.ToggleRangeBehavior;

/* renamed from: com.android.systemui.controls.ui.ToggleRangeBehavior$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ToggleRangeBehavior$$ExternalSyntheticLambda1 implements View.OnTouchListener {
    public final /* synthetic */ GestureDetector f$0;
    public final /* synthetic */ ToggleRangeBehavior.ToggleRangeGestureListener f$1;
    public final /* synthetic */ ToggleRangeBehavior f$2;

    public /* synthetic */ ToggleRangeBehavior$$ExternalSyntheticLambda1(GestureDetector gestureDetector, ToggleRangeBehavior.ToggleRangeGestureListener toggleRangeGestureListener, ToggleRangeBehavior toggleRangeBehavior) {
        this.f$0 = gestureDetector;
        this.f$1 = toggleRangeGestureListener;
        this.f$2 = toggleRangeBehavior;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return ToggleRangeBehavior.m2731initialize$lambda0(this.f$0, this.f$1, this.f$2, view, motionEvent);
    }
}
