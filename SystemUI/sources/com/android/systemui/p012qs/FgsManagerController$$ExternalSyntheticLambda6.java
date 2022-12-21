package com.android.systemui.p012qs;

import com.android.systemui.p012qs.FgsManagerController;

/* renamed from: com.android.systemui.qs.FgsManagerController$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FgsManagerController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ FgsManagerController.OnDialogDismissedListener f$0;

    public /* synthetic */ FgsManagerController$$ExternalSyntheticLambda6(FgsManagerController.OnDialogDismissedListener onDialogDismissedListener) {
        this.f$0 = onDialogDismissedListener;
    }

    public final void run() {
        this.f$0.onDialogDismissed();
    }
}
