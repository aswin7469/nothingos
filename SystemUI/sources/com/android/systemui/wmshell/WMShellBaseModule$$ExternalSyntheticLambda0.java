package com.android.systemui.wmshell;

import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda0 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda0();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        AppPairs asAppPairs;
        asAppPairs = ((AppPairsController) obj).asAppPairs();
        return asAppPairs;
    }
}
