package com.android.systemui.wmshell;

import com.android.p019wm.shell.pip.Pip;
import com.android.systemui.model.SysUiState;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda0 implements SysUiState.SysUiStateCallback {
    public final /* synthetic */ WMShell f$0;
    public final /* synthetic */ Pip f$1;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda0(WMShell wMShell, Pip pip) {
        this.f$0 = wMShell;
        this.f$1 = pip;
    }

    public final void onSystemUiStateChanged(int i) {
        this.f$0.m3330lambda$initPip$0$comandroidsystemuiwmshellWMShell(this.f$1, i);
    }
}
