package com.android.p019wm.shell.bubbles;

import com.android.p019wm.shell.bubbles.Bubbles;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ Bubbles.SysuiProxy f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda9(Bubbles.SysuiProxy sysuiProxy) {
        this.f$0 = sysuiProxy;
    }

    public final void accept(Object obj) {
        this.f$0.onUnbubbleConversation((String) obj);
    }
}
