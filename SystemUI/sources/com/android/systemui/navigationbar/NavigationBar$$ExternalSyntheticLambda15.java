package com.android.systemui.navigationbar;

import com.android.p019wm.shell.pip.Pip;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda15 implements Consumer {
    public final /* synthetic */ NavigationBarView f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda15(NavigationBarView navigationBarView) {
        this.f$0 = navigationBarView;
    }

    public final void accept(Object obj) {
        this.f$0.addPipExclusionBoundsChangeListener((Pip) obj);
    }
}
