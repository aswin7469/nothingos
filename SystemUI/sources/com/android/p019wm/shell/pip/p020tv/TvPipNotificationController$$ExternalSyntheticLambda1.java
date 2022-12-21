package com.android.p019wm.shell.pip.p020tv;

import android.media.session.MediaSession;
import com.android.p019wm.shell.pip.PipMediaController;

/* renamed from: com.android.wm.shell.pip.tv.TvPipNotificationController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TvPipNotificationController$$ExternalSyntheticLambda1 implements PipMediaController.TokenListener {
    public final /* synthetic */ TvPipNotificationController f$0;

    public /* synthetic */ TvPipNotificationController$$ExternalSyntheticLambda1(TvPipNotificationController tvPipNotificationController) {
        this.f$0 = tvPipNotificationController;
    }

    public final void onMediaSessionTokenChanged(MediaSession.Token token) {
        this.f$0.onMediaSessionTokenChanged(token);
    }
}
