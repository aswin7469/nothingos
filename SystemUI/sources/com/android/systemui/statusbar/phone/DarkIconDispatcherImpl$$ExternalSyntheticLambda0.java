package com.android.systemui.statusbar.phone;

import android.widget.ImageView;
import com.android.systemui.plugins.DarkIconDispatcher;
import java.util.ArrayList;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DarkIconDispatcherImpl$$ExternalSyntheticLambda0 implements DarkIconDispatcher.DarkReceiver {
    public final /* synthetic */ DarkIconDispatcherImpl f$0;
    public final /* synthetic */ ImageView f$1;

    public /* synthetic */ DarkIconDispatcherImpl$$ExternalSyntheticLambda0(DarkIconDispatcherImpl darkIconDispatcherImpl, ImageView imageView) {
        this.f$0 = darkIconDispatcherImpl;
        this.f$1 = imageView;
    }

    public final void onDarkChanged(ArrayList arrayList, float f, int i) {
        this.f$0.mo43968xf0c28192(this.f$1, arrayList, f, i);
    }
}
