package com.android.systemui.p012qs.tileimpl;

import android.widget.ImageView;
import com.android.systemui.plugins.p011qs.QSTile;

/* renamed from: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSIconViewImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ QSIconViewImpl f$0;
    public final /* synthetic */ ImageView f$1;
    public final /* synthetic */ QSTile.State f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ QSIconViewImpl$$ExternalSyntheticLambda1(QSIconViewImpl qSIconViewImpl, ImageView imageView, QSTile.State state, boolean z) {
        this.f$0 = qSIconViewImpl;
        this.f$1 = imageView;
        this.f$2 = state;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.m2962lambda$setIcon$0$comandroidsystemuiqstileimplQSIconViewImpl(this.f$1, this.f$2, this.f$3);
    }
}
