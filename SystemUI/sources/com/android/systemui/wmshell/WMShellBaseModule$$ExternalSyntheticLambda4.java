package com.android.systemui.wmshell;

import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda4();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        OneHanded asOneHanded;
        asOneHanded = ((OneHandedController) obj).asOneHanded();
        return asOneHanded;
    }
}
