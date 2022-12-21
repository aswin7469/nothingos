package com.android.p019wm.shell.pip.phone;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

/* renamed from: com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda8 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ PipMenuActionView f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda8(PipMenuActionView pipMenuActionView) {
        this.f$0 = pipMenuActionView;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        PipMenuView.lambda$updateActionViews$6(this.f$0, drawable);
    }
}
