package com.android.systemui;

import android.graphics.Bitmap;
import com.android.systemui.ImageWallpaper;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ ImageWallpaper.GLEngine f$0;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda5(ImageWallpaper.GLEngine gLEngine) {
        this.f$0 = gLEngine;
    }

    public final void accept(Object obj) {
        this.f$0.updateMiniBitmapAndNotify((Bitmap) obj);
    }
}
