package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda10 implements ActivityLaunchAnimator.PendingIntentStarter {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ PendingIntent f$1;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda10(CentralSurfacesImpl centralSurfacesImpl, PendingIntent pendingIntent) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = pendingIntent;
    }

    public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
        return this.f$0.mo43909x578fd696(this.f$1, remoteAnimationAdapter);
    }
}
