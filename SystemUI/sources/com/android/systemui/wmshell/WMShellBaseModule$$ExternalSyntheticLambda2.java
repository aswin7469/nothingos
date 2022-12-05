package com.android.systemui.wmshell;

import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda2 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda2();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        HideDisplayCutout asHideDisplayCutout;
        asHideDisplayCutout = ((HideDisplayCutoutController) obj).asHideDisplayCutout();
        return asHideDisplayCutout;
    }
}
