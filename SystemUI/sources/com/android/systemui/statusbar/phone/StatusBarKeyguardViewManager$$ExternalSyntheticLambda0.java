package com.android.systemui.statusbar.phone;

import android.view.View;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarKeyguardViewManager$$ExternalSyntheticLambda0 implements Consumer {
    public final void accept(Object obj) {
        ((View) obj).animate().alpha(0.0f).setDuration(StatusBarKeyguardViewManager.NAV_BAR_CONTENT_FADE_DURATION).start();
    }
}
