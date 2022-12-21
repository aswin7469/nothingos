package com.android.p019wm.shell.pip;

import android.media.session.MediaSessionManager;
import java.util.List;

/* renamed from: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda3 implements MediaSessionManager.OnActiveSessionsChangedListener {
    public final /* synthetic */ PipMediaController f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda3(PipMediaController pipMediaController) {
        this.f$0 = pipMediaController;
    }

    public final void onActiveSessionsChanged(List list) {
        this.f$0.resolveActiveMediaController(list);
    }
}
