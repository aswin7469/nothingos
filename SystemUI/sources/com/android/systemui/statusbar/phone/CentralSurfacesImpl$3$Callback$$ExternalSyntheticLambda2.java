package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.OverlayPlugin;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda2(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((OverlayPlugin) obj).setCollapseDesired(this.f$0);
    }
}
