package com.android.systemui.wmshell;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda3();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        LegacySplitScreen asLegacySplitScreen;
        asLegacySplitScreen = ((LegacySplitScreenController) obj).asLegacySplitScreen();
        return asLegacySplitScreen;
    }
}
