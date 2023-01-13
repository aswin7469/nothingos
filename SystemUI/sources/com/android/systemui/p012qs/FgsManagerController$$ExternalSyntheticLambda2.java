package com.android.systemui.p012qs;

import android.view.View;
import com.nothing.systemui.p024qs.ForegroundServiceManagerDialog;

/* renamed from: com.android.systemui.qs.FgsManagerController$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FgsManagerController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ View f$0;
    public final /* synthetic */ ForegroundServiceManagerDialog f$1;
    public final /* synthetic */ FgsManagerController f$2;

    public /* synthetic */ FgsManagerController$$ExternalSyntheticLambda2(View view, ForegroundServiceManagerDialog foregroundServiceManagerDialog, FgsManagerController fgsManagerController) {
        this.f$0 = view;
        this.f$1 = foregroundServiceManagerDialog;
        this.f$2 = fgsManagerController;
    }

    public final void run() {
        FgsManagerController.m2894showDialog$lambda22$lambda19(this.f$0, this.f$1, this.f$2);
    }
}
