package com.android.p019wm.shell.onehanded;

import com.android.p019wm.shell.onehanded.OneHandedState;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.onehanded.OneHandedState$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OneHandedState$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ OneHandedState$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((OneHandedState.OnStateChangedListener) obj).onStateChanged(this.f$0);
    }
}
