package com.android.systemui.wmshell;

import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda5();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        SplitScreen asSplitScreen;
        asSplitScreen = ((SplitScreenController) obj).asSplitScreen();
        return asSplitScreen;
    }
}
