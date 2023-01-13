package com.android.systemui.media;

import android.app.Notification;
import com.android.systemui.plugins.ActivityStarter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda8 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ MediaDataManager f$0;
    public final /* synthetic */ Notification.Action f$1;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda8(MediaDataManager mediaDataManager, Notification.Action action) {
        this.f$0 = mediaDataManager;
        this.f$1 = action;
    }

    public final boolean onDismiss() {
        return MediaDataManager.m2793createActionsFromNotification$lambda27$lambda25(this.f$0, this.f$1);
    }
}
