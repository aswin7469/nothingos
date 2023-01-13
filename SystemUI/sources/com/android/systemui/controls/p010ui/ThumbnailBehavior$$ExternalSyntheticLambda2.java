package com.android.systemui.controls.p010ui;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;

/* renamed from: com.android.systemui.controls.ui.ThumbnailBehavior$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ThumbnailBehavior$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ThumbnailBehavior f$0;
    public final /* synthetic */ ClipDrawable f$1;
    public final /* synthetic */ Drawable f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ThumbnailBehavior$$ExternalSyntheticLambda2(ThumbnailBehavior thumbnailBehavior, ClipDrawable clipDrawable, Drawable drawable, int i) {
        this.f$0 = thumbnailBehavior;
        this.f$1 = clipDrawable;
        this.f$2 = drawable;
        this.f$3 = i;
    }

    public final void run() {
        ThumbnailBehavior.m2733bind$lambda2$lambda1(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
