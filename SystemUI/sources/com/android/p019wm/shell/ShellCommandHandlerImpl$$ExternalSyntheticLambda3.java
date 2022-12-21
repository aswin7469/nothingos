package com.android.p019wm.shell;

import com.android.p019wm.shell.apppairs.AppPairsController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda3(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((AppPairsController) obj).unpair(this.f$0);
    }
}
