package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import com.android.systemui.monet.ColorScheme;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ MediaData f$10;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ ColorScheme f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;
    public final /* synthetic */ Drawable f$7;
    public final /* synthetic */ int f$8;
    public final /* synthetic */ int f$9;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda8(MediaControlPanel mediaControlPanel, int i, String str, int i2, ColorScheme colorScheme, boolean z, boolean z2, Drawable drawable, int i3, int i4, MediaData mediaData) {
        this.f$0 = mediaControlPanel;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = i2;
        this.f$4 = colorScheme;
        this.f$5 = z;
        this.f$6 = z2;
        this.f$7 = drawable;
        this.f$8 = i3;
        this.f$9 = i4;
        this.f$10 = mediaData;
    }

    public final void run() {
        this.f$0.mo33653xd28ebdf0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10);
    }
}
