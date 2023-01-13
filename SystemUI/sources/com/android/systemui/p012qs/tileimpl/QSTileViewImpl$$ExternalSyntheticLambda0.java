package com.android.systemui.p012qs.tileimpl;

import com.android.systemui.plugins.p011qs.QSTile;

/* renamed from: com.android.systemui.qs.tileimpl.QSTileViewImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSTileViewImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ QSTileViewImpl f$0;
    public final /* synthetic */ QSTile.State f$1;

    public /* synthetic */ QSTileViewImpl$$ExternalSyntheticLambda0(QSTileViewImpl qSTileViewImpl, QSTile.State state) {
        this.f$0 = qSTileViewImpl;
        this.f$1 = state;
    }

    public final void run() {
        QSTileViewImpl.m2967onStateChanged$lambda8(this.f$0, this.f$1);
    }
}
