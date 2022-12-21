package com.android.p019wm.shell.pip;

import android.media.MediaMetadata;
import com.android.p019wm.shell.pip.PipMediaController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ MediaMetadata f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda0(MediaMetadata mediaMetadata) {
        this.f$0 = mediaMetadata;
    }

    public final void accept(Object obj) {
        ((PipMediaController.MetadataListener) obj).onMediaMetadataChanged(this.f$0);
    }
}
