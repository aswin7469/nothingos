package com.android.systemui.wmshell;

import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda1 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda1();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Bubbles asBubbles;
        asBubbles = ((BubbleController) obj).asBubbles();
        return asBubbles;
    }
}
