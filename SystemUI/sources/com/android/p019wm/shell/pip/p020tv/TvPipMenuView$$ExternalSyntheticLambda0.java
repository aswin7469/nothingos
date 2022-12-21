package com.android.p019wm.shell.pip.p020tv;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

/* renamed from: com.android.wm.shell.pip.tv.TvPipMenuView$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TvPipMenuView$$ExternalSyntheticLambda0 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ TvPipMenuActionButton f$0;

    public /* synthetic */ TvPipMenuView$$ExternalSyntheticLambda0(TvPipMenuActionButton tvPipMenuActionButton) {
        this.f$0 = tvPipMenuActionButton;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        this.f$0.setImageDrawable(drawable);
    }
}
