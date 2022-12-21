package com.android.systemui.globalactions;

import android.animation.ValueAnimator;
import android.view.Window;
import com.android.systemui.globalactions.GlobalActionsDialogLite;

/* renamed from: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2133x58d1e4b3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GlobalActionsDialogLite.ActionsDialogLite f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ Window f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ C2133x58d1e4b3(GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite, boolean z, Window window, float f, int i) {
        this.f$0 = actionsDialogLite;
        this.f$1 = z;
        this.f$2 = window;
        this.f$3 = f;
        this.f$4 = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo32926x48b2e9d(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
