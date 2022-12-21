package com.android.systemui.navigationbar;

import com.android.p019wm.shell.pip.Pip;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TaskbarDelegate$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ TaskbarDelegate f$0;

    public /* synthetic */ TaskbarDelegate$$ExternalSyntheticLambda1(TaskbarDelegate taskbarDelegate) {
        this.f$0 = taskbarDelegate;
    }

    public final void accept(Object obj) {
        this.f$0.removePipExclusionBoundsChangeListener((Pip) obj);
    }
}
