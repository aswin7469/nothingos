package com.android.p019wm.shell.compatui;

import com.android.p019wm.shell.common.DisplayLayout;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.compatui.CompatUIController$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CompatUIController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ DisplayLayout f$0;

    public /* synthetic */ CompatUIController$$ExternalSyntheticLambda2(DisplayLayout displayLayout) {
        this.f$0 = displayLayout;
    }

    public final void accept(Object obj) {
        ((CompatUIWindowManagerAbstract) obj).updateDisplayLayout(this.f$0);
    }
}
