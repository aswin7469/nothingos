package com.android.p019wm.shell.pip;

import android.media.session.MediaSession;
import com.android.p019wm.shell.pip.PipMediaController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ MediaSession.Token f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda1(MediaSession.Token token) {
        this.f$0 = token;
    }

    public final void accept(Object obj) {
        ((PipMediaController.TokenListener) obj).onMediaSessionTokenChanged(this.f$0);
    }
}
