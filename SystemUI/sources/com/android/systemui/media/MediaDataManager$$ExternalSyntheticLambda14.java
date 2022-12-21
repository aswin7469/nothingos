package com.android.systemui.media;

import android.app.Notification;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda14 implements Runnable {
    public final /* synthetic */ Notification.Action f$0;
    public final /* synthetic */ MediaDataManager f$1;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda14(Notification.Action action, MediaDataManager mediaDataManager) {
        this.f$0 = action;
        this.f$1 = mediaDataManager;
    }

    public final void run() {
        MediaDataManager.m2787createActionsFromNotification$lambda27(this.f$0, this.f$1);
    }
}
