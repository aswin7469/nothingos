package com.android.systemui.dreams.complication;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ComplicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ComplicationLayoutEngine.ViewEntry f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ConstraintLayout.LayoutParams f$3;
    public final /* synthetic */ View f$4;
    public final /* synthetic */ boolean f$5;

    public /* synthetic */ ComplicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0(ComplicationLayoutEngine.ViewEntry viewEntry, boolean z, int i, ConstraintLayout.LayoutParams layoutParams, View view, boolean z2) {
        this.f$0 = viewEntry;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = layoutParams;
        this.f$4 = view;
        this.f$5 = z2;
    }

    public final void accept(Object obj) {
        this.f$0.mo32580xd35d1ce(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (Integer) obj);
    }
}
